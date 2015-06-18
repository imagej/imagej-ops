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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.ConvexHullPolygon;
import net.imglib2.RealPoint;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Uses Andrew's monotone chain algorithm to calculate the Convex Hull of a
 * Polygon.
 *
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = ConvexHullPolygon.NAME)
public class DefaultConvexHullOp implements ConvexHullPolygon {

	@Parameter(type = ItemIO.OUTPUT)
	private Polygon output;

	@Parameter(type = ItemIO.INPUT)
	private Polygon input;

	/**
	 * Andrew's monotone chain convex hull algorithm constructs the convex hull
	 * of a set of 2-dimensional RealPoints in O(n log n) time. It does so by
	 * first sorting the RealPoints lexicographically (first by x-coordinate,
	 * and in case of a tie, by y-coordinate), and then constructing upper and
	 * lower hulls of the RealPoints in O(n) time. An upper hull is the part of
	 * the convex hull, which is visible from the above. It runs from its
	 * rightmost RealPoint to the leftmost RealPoint in counterclockwise order.
	 * Lower hull is the remaining part of the convex hull.
	 */
	@Override
	public void run() {

		// create a copy of the points because these will get resorted, etc.
		List<RealPoint> RealPoints = new ArrayList<RealPoint>(input.getPoints());

		// Sort the RealPoints of P by x-coordinate (in case of a tie, sort by
		// y-coordinate).
		Collections.sort(RealPoints, new Comparator<RealPoint>() {
			@Override
			public int compare(final RealPoint o1, final RealPoint o2) {
				final Double o1x = new Double(o1.getDoublePosition(0));
				final Double o2x = new Double(o2.getDoublePosition(0));
				final int result = o1x.compareTo(o2x);
				if (result == 0) {
					return new Double(o1.getDoublePosition(1))
							.compareTo(new Double(o2.getDoublePosition(1)));
				} else {
					return result;
				}
			}
		});
		// Initialize U and L as empty lists.
		// The lists will hold the vertices of upper and lower hulls
		// respectively.
		final List<RealPoint> U = new ArrayList<RealPoint>();
		final List<RealPoint> L = new ArrayList<RealPoint>();
		// build lower hull
		for (final RealPoint p : RealPoints) {
			// while L contains at least two RealPoints and the sequence of last
			// two
			// RealPoints of L and the RealPoint P[i] does not make a
			// counter-clockwise
			// turn: remove the last RealPoint from L
			while (L.size() >= 2
					&& ccw(L.get(L.size() - 2), L.get(L.size() - 1), p) <= 0) {
				L.remove(L.size() - 1);
			}
			L.add(p);
		}
		// build upper hull
		Collections.reverse(RealPoints);
		for (final RealPoint p : RealPoints) {
			// while U contains at least two RealPoints and the sequence of last
			// two
			// RealPoints of U and the RealPoint P[i] does not make a
			// counter-clockwise
			// turn: remove the last RealPoint from U
			while (U.size() >= 2
					&& ccw(U.get(U.size() - 2), U.get(U.size() - 1), p) <= 0) {
				U.remove(U.size() - 1);
			}
			U.add(p);
		}
		// Remove the last RealPoint of each list (it's the same as the first
		// RealPoint
		// of the other list).
		L.remove(L.size() - 1);
		U.remove(U.size() - 1);
		// concatenate L and U
		L.addAll(U);

		output = new Polygon(L);
	}

	/**
	 * 2D cross product of OA and OB vectors, i.e. z-component of their 3D cross
	 * product. Returns a positive value, if OAB makes a counter-clockwise turn,
	 * negative for clockwise turn, and zero if the RealPoints are collinear.
	 *
	 * @param o
	 *            first RealPoint
	 * @param a
	 *            second RealPoint
	 * @param b
	 *            third RealPoint
	 * @return Returns a positive value, if OAB makes a counter-clockwise turn,
	 *         negative for clockwise turn, and zero if the RealPoints are
	 *         collinear.
	 */
	private double ccw(final RealPoint o, final RealPoint a, final RealPoint b) {
		return (a.getDoublePosition(0) - o.getDoublePosition(0))
				* (b.getDoublePosition(1) - o.getDoublePosition(1))
				- (a.getDoublePosition(1) - o.getDoublePosition(1))
				* (b.getDoublePosition(0) - o.getDoublePosition(0));
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
