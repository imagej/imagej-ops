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

package net.imagej.ops.geom.geom2d;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.geom.GeomUtils;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geom.real.DefaultWritablePolygon2D;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@code geom.smallestBoundingBox}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Ops.Geometric.SmallestEnclosingBoundingBox.class,
	label = "Geometric (2D): Smallest Enclosing Rectangle")
public class DefaultSmallestEnclosingRectangle extends
	AbstractUnaryFunctionOp<Polygon2D, Polygon2D> implements Contingent,
	Ops.Geometric.SmallestEnclosingBoundingBox
{

	private UnaryFunctionOp<Polygon2D, Polygon2D> convexHullFunc;
	private UnaryFunctionOp<Polygon2D, RealLocalizable> centroidFunc;
	private UnaryFunctionOp<Polygon2D, DoubleType> areaFunc;
	private UnaryFunctionOp<Polygon2D, Polygon2D> boundingBoxFunc;

	@Override
	public void initialize() {
		convexHullFunc = Functions.unary(ops(),
			Ops.Geometric.ConvexHull.class, Polygon2D.class, in());
		centroidFunc = Functions.unary(ops(), Ops.Geometric.Centroid.class,
			RealLocalizable.class, in());
		areaFunc = Functions.unary(ops(), Ops.Geometric.Size.class,
			DoubleType.class, in());
		boundingBoxFunc = Functions.unary(ops(),
			Ops.Geometric.BoundingBox.class, Polygon2D.class, in());
	}

	/**
	 * Rotates the given Polygon2D consisting of a list of RealPoints by the
	 * given angle about the given center.
	 *
	 * @param inPoly A Polygon2D consisting of a list of RealPoint RealPoints
	 * @param angle the rotation angle
	 * @param center the rotation center
	 * @return a rotated polygon
	 */
	private Polygon2D rotate(final Polygon2D inPoly, final double angle,
		final RealLocalizable center)
	{

		List<RealLocalizable> out = new ArrayList<>();

		for (RealLocalizable RealPoint : GeomUtils.vertices(inPoly)) {

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

		return new DefaultWritablePolygon2D(out);
	}

	@Override
	public Polygon2D calculate(final Polygon2D input) {
		Polygon2D ch = convexHullFunc.calculate(input);
		RealLocalizable cog = centroidFunc.calculate(ch);

		Polygon2D minBounds = input;
		double minArea = Double.POSITIVE_INFINITY;
		// for each edge (i.e. line from P(i-1) to P(i)
		for (int i = 1; i < ch.numVertices() - 1; i++) {
			final double angle = Math.atan2(ch.vertex(i).getDoublePosition(1) - ch
				.vertex(i - 1).getDoublePosition(1), ch.vertex(i).getDoublePosition(0) -
					ch.vertex(i - 1).getDoublePosition(0));

			// rotate the polygon in such a manner that the line has an angle of 0
			final Polygon2D rotatedPoly = rotate(ch, -angle, cog);

			// get the bounds
			final Polygon2D bounds = boundingBoxFunc.calculate(rotatedPoly);

			// calculate the area of the bounds
			final double area = areaFunc.calculate(bounds).get();

			// if the area of the bounds is smaller, rotate it to match the
			// original polygon and save it.
			if (area < minArea) {
				minArea = area;
				minBounds = rotate(bounds, angle, cog);
			}
		}

		// edge (n-1) to 0
		final double angle = Math.atan2(ch.vertex(0).getDoublePosition(1) - ch
			.vertex(ch.numVertices() - 1).getDoublePosition(1), ch.vertex(0)
				.getDoublePosition(0) - ch.vertex(ch.numVertices() - 1)
					.getDoublePosition(0));

		// rotate the polygon in such a manner that the line has an angle of 0
		final Polygon2D rotatedPoly = rotate(ch, -angle, cog);

		// get the bounds
		final Polygon2D bounds = boundingBoxFunc.calculate(rotatedPoly);

		// calculate the area of the bounds
		final double area = areaFunc.calculate(bounds).get();

		// if the area of the bounds is smaller, rotate it to match the
		// original polygon and save it.
		if (area < minArea) {
			minArea = area;
			minBounds = rotate(bounds, angle, cog);
		}

		return minBounds;
	}

	@Override
	public boolean conforms() {
		return in() != null;
	}

}
