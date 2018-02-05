/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.segment.ridgeDetection;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.Localizable;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.roi.geom.real.DefaultPolyline;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class)
public class EdgeDetection<T extends RealType<T>> extends
	AbstractBinaryFunctionOp<RandomAccessibleInterval<T>, List<DefaultPolyline>, List<DefaultPolyline>>
	implements Contingent
{

	@Parameter
	double sigma;

	@Override
	public List<DefaultPolyline> calculate(RandomAccessibleInterval<T> input,
		List<DefaultPolyline> lines)
	{
		List<DefaultPolyline> edges = new ArrayList<DefaultPolyline>();
		double edgeLimit = 2.5 * sigma;

		int[] derivatives = { 1, 1 };
		RandomAccessibleInterval<DoubleType> derivative =
			(RandomAccessibleInterval<DoubleType>) ops().run(Ops.Copy.RAI.class,
				input);
		RandomAccess<DoubleType> derivativeRA = derivative.randomAccess();

		ops().filter().derivativeGauss(input, derivative, sigma, derivatives);

		// loop through each line,find the edge if within the limit, otherwise
		// extrapolate for a nice output.
		for (int i = 0; i < lines.size(); i++) {
			DefaultPolyline currentLine = lines.get(i);
			List<RealPoint> leftPoints = new ArrayList<>();
			List<Integer> leftFailedIndices = new ArrayList<>();
			List<RealPoint> rightPoints = new ArrayList<>();
			List<Integer> rightFailedIndices = new ArrayList<>();

			for (int p = 0; p < currentLine.numVertices(); p++) {
				// find the direction tangent to the line
				RealPoint currentPoint = currentLine.vertex(p);
				if (p < currentLine.numVertices() - 1) {
					RealPoint nextPoint = currentLine.vertex(p + 1);
					double distx = nextPoint.getDoublePosition(0) - currentPoint
						.getDoublePosition(0);
					double disty = nextPoint.getDoublePosition(1) - currentPoint
						.getDoublePosition(1);
					int octant = RidgeDetectionUtils.getOctant(distx, disty);

					derivativeRA.setPosition((Localizable) currentPoint);
					// left edge
					int[] dist = new int[2];
					int[] modifier = RidgeDetectionUtils.getOctantCoords(octant - 2);
					boolean foundEdge = false;
					RealPoint leftEdge = new RealPoint();
					double leftEdgeValue = Double.MIN_VALUE;
					while (Math.sqrt(dist[0] * dist[0] + dist[1] * dist[1]) < edgeLimit &&
						!foundEdge)
					{
						// left edge - TODO
						derivativeRA.move(modifier[0], 0);
						derivativeRA.move(modifier[1], 1);

						// check value.
						double currentEdgeValue = derivativeRA.get().get();
						if (Math.abs(currentEdgeValue) > Math.abs(leftEdgeValue)) {
							leftEdge.setPosition(derivativeRA);
							leftEdgeValue = currentEdgeValue;
						}
						else {
							leftPoints.add(leftEdge);
							foundEdge = true;
						}
					}

					if (!foundEdge) { // TODO
						leftFailedIndices.add(p);

					}

					derivativeRA.setPosition((Localizable) currentPoint);
					// right edge
					dist = new int[2];
					modifier = RidgeDetectionUtils.getOctantCoords(octant - 2);
					foundEdge = false;
					RealPoint rightEdge = new RealPoint();
					double rightEdgeValue = Double.MIN_VALUE;
					while (Math.sqrt(dist[0] * dist[0] + dist[1] * dist[1]) < edgeLimit &&
						!foundEdge)
					{
						// right edge - TODO
						derivativeRA.move(modifier[0], 0);
						derivativeRA.move(modifier[1], 1);

						// check value.
						double currentEdgeValue = derivativeRA.get().get();
						if (Math.abs(currentEdgeValue) > Math.abs(rightEdgeValue)) {
							rightEdge.setPosition(derivativeRA);
							rightEdgeValue = currentEdgeValue;
						}
						else {
							rightPoints.add(rightEdge);
							foundEdge = true;
						}
					}

					if (!foundEdge) { // TODO
						rightFailedIndices.add(p);
					}
				}
			}

		}

		return edges;
	}

	/**
	 * When the edge detector cannot find an edge point for a certain line point,
	 * the best thing to do is extrapolate on in.
	 * 
	 * @param currentLine
	 * @param edge
	 * @param failedIndices
	 */
	private void extrapolate(DefaultPolyline currentLine, List<RealPoint> edge,
		List<Integer> failedIndices)
	{
		while (failedIndices.size() > 0) {
			int current = failedIndices.get(0);
			// i is the index of the last recorded edge point in edge, j is the index
			// of the next recorded edge point in edge. k is the index of the current
			// point
			int i = current - 1, j = current, k = current;
			while (failedIndices.contains(j))
				j++;
			RealPoint pi = currentLine.vertex(i);
			RealPoint pj = currentLine.vertex(j);
			RealPoint pk = currentLine.vertex(k);
			double a = Math.sqrt(Math.pow((pi.getDoublePosition(0) - pk
				.getDoublePosition(0)), 2) + Math.pow((pi.getDoublePosition(1) - pk
					.getDoublePosition(1)), 2));
			double b = Math.sqrt(Math.pow((pi.getDoublePosition(0) - pj
				.getDoublePosition(0)), 2) + Math.pow((pi.getDoublePosition(1) - pj
					.getDoublePosition(1)), 2));
		}

	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

}
