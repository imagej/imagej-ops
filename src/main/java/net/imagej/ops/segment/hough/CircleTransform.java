/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2016 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
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

package net.imagej.ops.segment.hough;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Segment.HoughCircle;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.localextrema.LocalExtrema;
import net.imglib2.algorithm.localextrema.LocalExtrema.MaximumCheck;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.roi.geom.real.ClosedSphere;
import net.imglib2.roi.geom.real.Sphere;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This op performs the Hough Circle Transform on a Binary RealType Image
 * (typically one that has first gone through edge detection.) This op performs
 * on a set radius on 2-D images.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = HoughCircle.class)
public class CircleTransform<T extends RealType<T>> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<T>, List<Sphere>> implements
	Contingent, Ops.Segment.HoughCircle
{

	@Parameter(min = "4", description = "The radius to search for, in pixels.")
	int radius;

	@Parameter(min = "2",
		description = "The number of points required to consider a circle.")
	int threshold;

	// LookUpTable. Holds values of rcos(theta) in lut[0] and rsin(theta) in
	// lut[1]. Made for convienence. Size [2][2 * radius].
	int[][] lut;

	// This method fills up the LookUpTable (lut).
	private void buildLUT() {
		// We create a total of 2*radius coordinates for a total of 2*radius checks
		// along the radius of the hypothetical circle. The length of the array
		// scales with the size of the radius, allowing for more accuracy with
		// larger circles.
		lut = new int[2 * radius][2];
		double increment = (Math.PI / radius);
		// fill the array with rcos(theta) in lut[0] and rsin(theta) in lut[1]
		for (int i = 0; i < 2 * radius; i++) {
			lut[i][0] = (int) Math.round((double) radius * Math.cos(i * increment));
			lut[i][1] = (int) Math.round((double) radius * Math.sin(i * increment));
		}
	}

	// scans the current output for any circles similar to the one just found (to
	// eliminate near duplicates).
	private boolean checkForSimilar(Point p, List<Sphere> output) {
		for (Sphere s : output) {
			double[] center = s.center();
			double Diff = Math.sqrt(Math.pow(p.getLongPosition(0) - center[0], 2) +
				Math.pow(p.getLongPosition(1) - center[1], 2));
			if (Diff < Math.sqrt(radius)) return true;
		}
		return false;
	}

	@Override
	public void compute(RandomAccessibleInterval<T> input, List<Sphere> output) {
		// create a value of 0 (false) to use past the edges of the RAI.
		final T falseValue = input.randomAccess().get().createVariable();
		// assign false to all values out of the bounds of the image.
		Img<IntType> acc = ops().create().img(input, new IntType());
		OutOfBoundsConstantValueFactory<T, RandomAccessibleInterval<T>> obcvf =
			new OutOfBoundsConstantValueFactory<T, RandomAccessibleInterval<T>>(
				falseValue);
		// cursor for both input and output
		Cursor<IntType> accCursor = acc.localizingCursor();
		// randomAccess for setting output value
		RandomAccess<IntType> accRA = acc.randomAccess();
		// randomAccess for obtaining input value
		RandomAccess<T> pointCheck = obcvf.create(input);
		// coordinates of position on hypothetical circle radius.
		long[] assertionPos = { 0, 0 };
		// count of verified points on the hypothetical circle.
		byte pointsOnCircle;

		// build lookup table based on radius
		buildLUT();

		// voting space
		while (accCursor.hasNext()) {
			accCursor.fwd();
			pointsOnCircle = 0;
			for (int[] i : lut) {
				// get x pos
				assertionPos[0] = accCursor.getLongPosition(0) + i[0];
				// get y pos
				assertionPos[1] = accCursor.getLongPosition(1) + i[1];

				pointCheck.setPosition(assertionPos);
				if (pointCheck.get().getRealDouble() != 0) pointsOnCircle++;
			}
			accRA.setPosition(accCursor);
			accRA.get().set(pointsOnCircle);
		}

		// find the local extrema
		ArrayList<Point> centers = LocalExtrema.findLocalExtrema(acc,
			new MaximumCheck<IntType>(new IntType(threshold)), Executors
				.newSingleThreadExecutor());

		// create spheres from the centers and add them to output
		for (Point p : centers) {
			double[] center = { p.getLongPosition(0), p.getLongPosition(1) };
			Sphere sphere = new ClosedSphere(center, radius);
			if (checkForSimilar(p, output) == false) output.add(sphere);
		}
	}

	@Override
	public boolean conforms() {
		return (in().numDimensions() == 2);
	}

}
