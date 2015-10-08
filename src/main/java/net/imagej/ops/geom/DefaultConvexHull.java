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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.Ops.Geometric.ConvexHull;
import net.imagej.ops.geom.helper.Polytope;
import net.imagej.ops.geom.helper.ThePolygon;
import net.imglib2.RealLocalizable;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link ConvexHull}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Convex Hull", name = Geometric.ConvexHull.NAME)
public class DefaultConvexHull extends AbstractFunctionOp<List<RealLocalizable>, Polytope>
		implements
			GeometricOp<List<RealLocalizable>, Polytope>,
			Contingent,
			Geometric.ConvexHull {

	@Override
	public ThePolygon compute(final List<RealLocalizable> input) {
		// create a copy of the points because these will get resorted, etc.
		List<RealLocalizable> RealPoints = input;

		// Sort the RealPoints of P by x-coordinate (in case of a tie, sort by
		// y-coordinate).
		Collections.sort(RealPoints, new Comparator<RealLocalizable>() {

			@Override
			public int compare(final RealLocalizable o1,
					final RealLocalizable o2) {
				final Double o1x = new Double(o1.getDoublePosition(0));
				final Double o2x = new Double(o2.getDoublePosition(0));
				final int result = o1x.compareTo(o2x);
				if (result == 0) {
					return new Double(o1.getDoublePosition(1))
							.compareTo(new Double(o2.getDoublePosition(1)));
				}

				return result;
			}
		});

		// Initialize U and L as empty lists.
		// The lists will hold the vertices of upper and lower hulls
		// respectively.
		final List<RealLocalizable> U = new ArrayList<RealLocalizable>();
		final List<RealLocalizable> L = new ArrayList<RealLocalizable>();
		// build lower hull
		for (final RealLocalizable p : RealPoints) {
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
		for (final RealLocalizable p : RealPoints) {
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

		return new ThePolygon(L);
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
	 * @return Returns a positive value, if OAB makes a counter-clockwise wturn,
	 *         negative for clockwise turn, and zero if the RealPoints are
	 *         collinear.
	 */
	private double ccw(final RealLocalizable o, final RealLocalizable a,
			final RealLocalizable b) {
		return (a.getDoublePosition(0) - o.getDoublePosition(0))
				* (b.getDoublePosition(1) - o.getDoublePosition(1))
				- (a.getDoublePosition(1) - o.getDoublePosition(1))
				* (b.getDoublePosition(0) - o.getDoublePosition(0));
	}

	@Override
	public boolean conforms() {
		return !in().isEmpty() && in().get(0).numDimensions() == 2;
	}
}
