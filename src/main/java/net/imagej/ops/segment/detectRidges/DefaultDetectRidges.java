/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.segment.detectRidges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.roi.geom.real.DefaultWritablePolyline;
import net.imglib2.roi.geom.real.WritablePolyline;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Performs the Ridge Detection algorithm on a 2-Dimensional, gray-scale image.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Segment.DetectRidges.class)
public class DefaultDetectRidges<T extends RealType<T>> extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, List<? extends WritablePolyline>>
	implements Ops.Segment.DetectRidges, Contingent
{

	/**
	 * The diameter of the lines to search for.
	 */
	@Parameter
	double width;

	/**
	 * The threshold for which the gradient of a subsequent line point must be
	 * above.
	 */
	@Parameter
	double lowerThreshold;

	/**
	 * The threshold for which the gradient of a initial line point must be above.
	 */
	@Parameter
	double higherThreshold;

	@Parameter(min = "1")
	int ridgeLengthMin;

	/**
	 * The threshold for angle differences between the eigenvectors of two
	 * different ridge points. The eigenvector is fixed for angle differences
	 * above this threshold.
	 */
	double angleThreshold = 100;

	/**
	 * Recursively determines the next line point and adds it to the running list
	 * of line points.
	 * 
	 * @param gradientRA - the {@link RandomAccess} of the gradient image.
	 * @param pRA - the {@link RandomAccess} of the eigenvector image.
	 * @param nRA - the {@link RandomAccess} of the subpixel line location image.
	 * @param points - the {@link ArrayList} containing the line points.
	 * @param octant - integer denoting the octant of the last gradient vector,
	 *          oriented with 1 being 0 degrees and increasing in the
	 *          counterclockwise direction.
	 * @param lastnx - the x component of the gradient vector of the last line
	 *          point.
	 * @param lastny - the y component of the gradient vector of the last line
	 *          point.
	 * @param lastpx - the x component of the subpixel line location of the last
	 *          line point.
	 * @param lastpy - the y component of the subpixel line location of the last
	 *          line point.
	 */
	private void getNextPoint(RandomAccess<DoubleType> gradientRA,
		RandomAccess<DoubleType> pRA, RandomAccess<DoubleType> nRA,
		List<RealPoint> points, int octant, double lastnx, double lastny,
		double lastpx, double lastpy)
	{
		Point currentPos = new Point(gradientRA);
		// variables for the best line point of the three.
		Point salientPoint = new Point(gradientRA);
		double salientnx = 0;
		double salientny = 0;
		double salientpx = 0;
		double salientpy = 0;
		double bestSalience = Double.MAX_VALUE;
		boolean lastPointInLine = true;
		// check the three possible points that could continue the line, starting at
		// the octant after the given octant and rotating clockwise around the
		// current pixel.
		double lastAngle = RidgeDetectionUtils.getAngle(lastnx, lastny);

		for (int i = 1; i < 4; i++) {
			int[] modifier = RidgeDetectionUtils.getOctantCoords(octant + i);
			gradientRA.move(modifier[0], 0);
			gradientRA.move(modifier[1], 1);
			// make sure that we only do the calculations if there is a line point
			// there.
			if (gradientRA.get()
				.get() > lowerThreshold /*&& isMaxRA.get().get() > 0*/)
			{
				long[] vectorArr = { gradientRA.getLongPosition(0), gradientRA
					.getLongPosition(1), 0 };
				nRA.setPosition(vectorArr);
				double nx = nRA.get().get();
				nRA.fwd(2);
				double ny = nRA.get().get();
				pRA.setPosition(vectorArr);
				double px = pRA.get().get();
				pRA.fwd(2);
				double py = pRA.get().get();
				double currentAngle = RidgeDetectionUtils.getAngle(nx, ny);
				double subpixelDiff = Math.sqrt(Math.pow(px - lastpx, 2) + Math.pow(py -
					lastpy, 2));
				double angleDiff = Math.abs(currentAngle - lastAngle);
				lastPointInLine = false;
				// A salient line point will have the smallest combination of these
				// numbers relative to other potential line points.
				if (subpixelDiff + angleDiff < bestSalience) {
					// record the values of the new most salient pixel
					salientPoint = new Point(gradientRA);
					salientnx = nx;
					salientny = ny;
					salientpx = px;
					salientpy = py;
					bestSalience = subpixelDiff + angleDiff;
				}
				//set the values to zero so that they are not added to another line.
				gradientRA.get().set(0);

			}

			// reset our randomAccess for the next check
			gradientRA.setPosition(currentPos);
		}

		// set the current pixel to 0 in the first slice of eigenRA!
		gradientRA.get().setReal(0);

		// find the next line point as long as there is one to find
		if (!lastPointInLine) {
			// take the most salient point
			gradientRA.setPosition(salientPoint);
			points.add(RidgeDetectionUtils.get2DRealPoint(gradientRA
				.getDoublePosition(0) + salientpx, gradientRA.getDoublePosition(1) +
					salientpy));

			// the gradient vector itself refers to the greatest change in intensity,
			// and for a pixel on a line this vector will be perpendicular to the
			// direction of the line. But this vector can point to either the left or
			// the right of the line from the perspective of the detector, and there
			// is no guarantee that the vectors at line point will point off the same
			// side of the line. So if they point off different sides, set the current
			// vector by 180 degrees for the purposes of this detector. We set the
			// threshold for angle fixing just above 90 degrees since any lower would
			// prevent ridges curving.
			double potentialGradient = RidgeDetectionUtils.getAngle(salientnx,
				salientny);
			// we increase any angles too close to zero since, for example, having one
			// angle at 5 degrees and another at 355 would not satisfy the conditional
			// even though they are close enough to satisfy.
			if (lastAngle < angleThreshold) lastAngle += 360;
			if (potentialGradient < angleThreshold) potentialGradient += 360;

			if (Math.abs(potentialGradient - lastAngle) > angleThreshold) {
				salientnx = -salientnx;
				salientny = -salientny;
			}

			// perform the operation again on the new end of the line being formed.
			getNextPoint(gradientRA, pRA, nRA, points, RidgeDetectionUtils.getOctant(
				salientnx, salientny), salientnx, salientny, salientpx, salientpy);
		}
	}

	@Override
	public List<? extends WritablePolyline> calculate(RandomAccessibleInterval<T> input) {

		double sigma = (width / (2 * Math.sqrt(3)));

		// generate the metadata images
		RidgeDetectionMetadata ridgeDetectionMetadata = new RidgeDetectionMetadata(input, sigma,
			lowerThreshold, higherThreshold);

		// retrieve the metadata images
		Img<DoubleType> p_values = ridgeDetectionMetadata.getPValues();
		Img<DoubleType> n_values = ridgeDetectionMetadata.getNValues();
		Img<DoubleType> gradients = ridgeDetectionMetadata.getGradients();

		// create RandomAccesses for the metadata images
		OutOfBoundsConstantValueFactory<DoubleType, RandomAccessibleInterval<DoubleType>> oscvf =
			new OutOfBoundsConstantValueFactory<>(new DoubleType(0));
		RandomAccess<DoubleType> pRA = oscvf.create(p_values);
		RandomAccess<DoubleType> nRA = oscvf.create(n_values);
		RandomAccess<DoubleType> gradientRA = oscvf.create(gradients);

		// create the output polyline list.
		List<DefaultWritablePolyline> lines = new ArrayList<>();

		// start at the point of greatest maximum absolute value
		gradientRA.setPosition(RidgeDetectionUtils.getMaxCoords(gradients, true));

		// loop through the maximum values of the image
		while (Math.abs(gradientRA.get().get()) > higherThreshold) {

			// create the List of points that will be used to make the polyline
			List<RealPoint> points = new ArrayList<>();

			// get all of the necessary metadata from the image.
			long[] eigenvectorPos = { gradientRA.getLongPosition(0), gradientRA
				.getLongPosition(1), 0 };

			// obtain the n-values
			nRA.setPosition(eigenvectorPos);
			double eigenx = nRA.get().getRealDouble();
			nRA.fwd(2);
			double eigeny = nRA.get().getRealDouble();

			// obtain the p-values
			pRA.setPosition(eigenvectorPos);
			double px = pRA.get().getRealDouble();
			pRA.fwd(2);
			double py = pRA.get().getRealDouble();

			// start the list by adding the current point, which is the most line-like
			// point on the polyline
			points.add(RidgeDetectionUtils.get2DRealPoint(gradientRA
				.getDoublePosition(0) + px, gradientRA.getDoublePosition(1) + py));

			// go in the direction to the left of the perpendicular value
			getNextPoint(gradientRA, pRA, nRA, points, RidgeDetectionUtils.getOctant(
				eigenx, eigeny), eigenx, eigeny, px, py);

			// flip the array list around so that we get one cohesive line
			gradientRA.setPosition(new long[] { eigenvectorPos[0],
				eigenvectorPos[1] });
			Collections.reverse(points);

			// go in the opposite direction as before.
			eigenx = -eigenx;
			eigeny = -eigeny;
			getNextPoint(gradientRA, pRA, nRA, points, RidgeDetectionUtils.getOctant(
				eigenx, eigeny), eigenx, eigeny, px, py);

			// set the value to 0 so that it is not reused.
			gradientRA.get().setReal(0);

			// turn the list of points into a polyline, add to output list. If the
			// list has fewer vertices than the parameter, then we do not report it.
			if (points.size() > ridgeLengthMin) {
				DefaultWritablePolyline pline = new DefaultWritablePolyline(points);
				lines.add(pline);
			}
			// find the next max absolute value
			gradientRA.setPosition(RidgeDetectionUtils.getMaxCoords(gradients, true));
		}

		return lines;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

}
