/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
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
package net.imagej.ops.geometric.polygon;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.geometric.GeometricOps.CenterOfGravity;
import net.imagej.ops.geometric.GeometricOps.ConvexHull;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.BoundingBoxPolygon;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.SmallestEnclosingRectanglePolygon;
import net.imagej.ops.statistics.GeometricOps.Area;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Computes the smallest enclosing rectangle of a polygon.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = SmallestEnclosingRectanglePolygon.NAME)
public class DefaultSmallestEnclosingRectangleOp implements
		SmallestEnclosingRectanglePolygon {

	@Parameter(type = ItemIO.OUTPUT)
	private Polygon output;

	@Parameter(type = ItemIO.INPUT)
	private Polygon input;

	@Parameter
	private OpService ops;

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
	 * @return
	 */
	private Polygon rotate(Polygon inPoly, double angle, RealPoint center) {
		Polygon outPoly = new Polygon();

		for (RealPoint RealPoint : inPoly.getPoints()) {

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

			outPoly.add(new RealPoint(x, y));
		}

		return outPoly;
	}

	@Override
	public void run() {

		Polygon poly = (Polygon) ops.run(ConvexHull.class, input);

		RealPoint polygonCenter = (RealPoint) ops.run(CenterOfGravity.class,
				poly);

		Polygon minBounds = null;
		double minArea = Double.POSITIVE_INFINITY;
		// for each edge (i.e. line from P(i-1) to P(i)
		for (int i = 1; i < poly.size() - 1; i++) {
			double angle = Math.atan2(poly.getPoints().get(i)
					.getDoublePosition(1)
					- poly.getPoints().get(i - 1).getDoublePosition(1), poly
					.getPoints().get(i).getDoublePosition(0)
					- poly.getPoints().get(i - 1).getDoublePosition(0));

			// rotate the polygon in such a manner that the line has an angle of
			// 0
			Polygon rotatedPoly = rotate(poly, -angle, polygonCenter);
			// get the bounds
			// Polygon bounds = getBounds(rotatedPoly);
			Polygon bounds = (Polygon) ops.run(BoundingBoxPolygon.class,
					rotatedPoly);

			// calculate the area of the bounds
			// double area = getBoundsArea(bounds);

			double area = ((DoubleType) ops.run(Area.class, bounds))
					.getRealDouble();

			// if the area of the bounds is smaller, rotate it to match the
			// original polygon and save it.
			if (area < minArea) {
				minArea = area;
				minBounds = rotate(bounds, angle, polygonCenter);
			}
		}

		output = minBounds;

	}

	@Override
	public Polygon getOutput() {
		return output;
	}

	@Override
	public void setOutput(Polygon output) {
		this.output = output;
	}
}
