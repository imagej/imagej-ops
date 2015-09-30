/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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
package net.imagej.ops.geom;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Area;
import net.imagej.ops.Ops.Geometric2D.BoundingBox;
import net.imagej.ops.Ops.Geometric2D.Centroid;
import net.imagej.ops.Ops.Geometric2D.ConvexHull;
import net.imagej.ops.Ops.Geometric2D.SmallestEnclosingRectangle;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link SmallestEnclosingRectangle}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Smallest Enclosing Rectangle", name = Geometric2D.SmallestEnclosingRectangle.NAME)
public class DefaultSmallestEnclosingRectangle
		extends
			AbstractFunctionOp<Polygon, Polygon>
		implements
			GeometricOp<Polygon, Polygon>,
			Contingent,
			Geometric2D.SmallestEnclosingRectangle {

	private FunctionOp<Polygon, Polygon> convexhullFunc;
	private FunctionOp<Polygon, RealLocalizable> centroidFunc;
	private FunctionOp<Polygon, DoubleType> areaFunc;
	private FunctionOp<Polygon, Polygon> boundingBoxFunc;

	@Override
	public void initialize() {
		convexhullFunc = ops().function(ConvexHull.class, Polygon.class,
				Polygon.class);
		centroidFunc = ops().function(Centroid.class, RealLocalizable.class,
				Polygon.class);
		areaFunc = ops().function(Area.class, DoubleType.class, Polygon.class);
		boundingBoxFunc = ops().function(BoundingBox.class, Polygon.class,
				Polygon.class);
	}

	/**
	 * Rotates the given Polygon consisting of a list of RealPoints by the given
	 * angle about the given center.
	 *
	 * @param inPoly
	 *            A Polygon consisting of a list of RealPoint RealPoints
	 * @param angle
	 *            the rotation angle
	 * @param center
	 *            the rotation center
	 * @return a rotated polygon
	 */
	private Polygon rotate(final Polygon inPoly, final double angle,
			final RealLocalizable center) {

		List<RealLocalizable> out = new ArrayList<RealLocalizable>();

		for (RealLocalizable RealPoint : inPoly.getVertices()) {

			// double angleInRadians = Math.toRadians(angleInDegrees);
			double cosTheta = Math.cos(angle);
			double sinTheta = Math.sin(angle);

			double x = cosTheta
					* (RealPoint.getDoublePosition(0) - center
							.getDoublePosition(0))
					- sinTheta
					* (RealPoint.getDoublePosition(1) - center
							.getDoublePosition(1))
					+ center.getDoublePosition(0);

			double y = sinTheta
					* (RealPoint.getDoublePosition(0) - center
							.getDoublePosition(0))
					+ cosTheta
					* (RealPoint.getDoublePosition(1) - center
							.getDoublePosition(1))
					+ center.getDoublePosition(1);

			out.add(new RealPoint(x, y));
		}

		return new Polygon(out);
	}

	@Override
	public Polygon compute(final Polygon input) {

		Polygon ch = convexhullFunc.compute(input);
		RealLocalizable cog = centroidFunc.compute(ch);

		Polygon minBounds = null;
		double minArea = Double.POSITIVE_INFINITY;
		// for each edge (i.e. line from P(i-1) to P(i)
		for (int i = 1; i < ch.getVertices().size() - 1; i++) {
			double angle = Math.atan2(ch.getVertices().get(i)
					.getDoublePosition(1)
					- ch.getVertices().get(i - 1).getDoublePosition(1), ch
					.getVertices().get(i).getDoublePosition(0)
					- ch.getVertices().get(i - 1).getDoublePosition(0));

			// rotate the polygon in such a manner that the line has an angle of
			// 0
			final Polygon rotatedPoly = rotate(ch, -angle, cog);

			// get the bounds
			final Polygon bounds = boundingBoxFunc.compute(rotatedPoly);

			// calculate the area of the bounds
			// double area = getBoundsArea(bounds);
			double area = areaFunc.compute(rotatedPoly).get();

			// if the area of the bounds is smaller, rotate it to match the
			// original polygon and save it.
			if (area < minArea) {
				minArea = area;
				minBounds = rotate(bounds, angle, cog);
			}
		}

		return minBounds;
	}

	@Override
	public boolean conforms() {
		return 2 == in().numDimensions();
	}

}
