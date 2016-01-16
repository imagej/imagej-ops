/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.geom.geom2d;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@code geom.smallestBoundingBox}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Ops.Geometric.SmallestEnclosingBoundingBox.class,
	label = "Geometric (2D): Smallest Enclosing Rectangle")
public class DefaultSmallestEnclosingRectangle extends
	AbstractUnaryFunctionOp<Polygon, Polygon> implements Contingent,
	Ops.Geometric.SmallestEnclosingBoundingBox
{

	private UnaryFunctionOp<Polygon, Polygon> convexhullFunc;
	private UnaryFunctionOp<Polygon, RealLocalizable> centroidFunc;
	private UnaryFunctionOp<Polygon, DoubleType> areaFunc;
	private UnaryFunctionOp<Polygon, Polygon> boundingBoxFunc;

	@Override
	public void initialize() {
		convexhullFunc = Functions.unary(ops(), Ops.Geometric.ConvexHull.class, Polygon.class, in());
		centroidFunc = Functions.unary(ops(), Ops.Geometric.Centroid.class, RealLocalizable.class, in());
		areaFunc = Functions.unary(ops(), Ops.Geometric.Size.class, DoubleType.class, in());
		boundingBoxFunc = Functions.unary(ops(), Ops.Geometric.BoundingBox.class, Polygon.class, in());
	}

	/**
	 * Rotates the given Polygon consisting of a list of RealPoints by the given
	 * angle about the given center.
	 *
	 * @param inPoly A Polygon consisting of a list of RealPoint RealPoints
	 * @param angle the rotation angle
	 * @param center the rotation center
	 * @return a rotated polygon
	 */
	private Polygon rotate(final Polygon inPoly, final double angle,
		final RealLocalizable center)
	{

		List<RealLocalizable> out = new ArrayList<>();

		for (RealLocalizable RealPoint : inPoly.getVertices()) {

			// double angleInRadians = Math.toRadians(angleInDegrees);
			double cosTheta = Math.cos(angle);
			double sinTheta = Math.sin(angle);

			double x = cosTheta * (RealPoint.getDoublePosition(0) - center
				.getDoublePosition(0)) - sinTheta * (RealPoint.getDoublePosition(1) -
					center.getDoublePosition(1)) + center.getDoublePosition(0);

			double y = sinTheta * (RealPoint.getDoublePosition(0) - center
				.getDoublePosition(0)) + cosTheta * (RealPoint.getDoublePosition(1) -
					center.getDoublePosition(1)) + center.getDoublePosition(1);

			out.add(new RealPoint(x, y));
		}

		return new Polygon(out);
	}

	@Override
	public Polygon compute1(final Polygon input) {
		Polygon ch = convexhullFunc.compute1(input);
		RealLocalizable cog = centroidFunc.compute1(ch);

		Polygon minBounds = null;
		double minArea = Double.POSITIVE_INFINITY;
		// for each edge (i.e. line from P(i-1) to P(i)
		for (int i = 1; i < ch.getVertices().size() - 1; i++) {
			double angle = Math.atan2(ch.getVertices().get(i).getDoublePosition(1) -
				ch.getVertices().get(i - 1).getDoublePosition(1), ch.getVertices().get(
					i).getDoublePosition(0) - ch.getVertices().get(i - 1)
						.getDoublePosition(0));

			// rotate the polygon in such a manner that the line has an angle of
			// 0
			final Polygon rotatedPoly = rotate(ch, -angle, cog);

			// get the bounds
			final Polygon bounds = boundingBoxFunc.compute1(rotatedPoly);

			// calculate the area of the bounds
			// double area = getBoundsArea(bounds);
			double area = areaFunc.compute1(rotatedPoly).get();

			// if the area of the bounds is smaller, rotate it to match the
			// original polygon and save it.
			if (area < minArea) {
				minArea = area;
				minBounds = rotate(bounds, angle, cog);
			}
		}

		if(minBounds == null)
			minBounds = input;
		
		return minBounds;
	}

	@Override
	public boolean conforms() {
		return in() instanceof Polygon;
	}

}
