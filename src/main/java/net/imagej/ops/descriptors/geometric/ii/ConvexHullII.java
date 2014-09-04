package net.imagej.ops.descriptors.geometric.ii;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.descriptors.geometric.ConvexHull;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

import org.scijava.plugin.Plugin;

/**
 * Uses Andrew's monotone chain algorithm to calculate the Convex Hull of a
 * Polygon.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = ConvexHull.NAME, label = ConvexHull.LABEL)
public class ConvexHullII extends
		AbstractOutputFunction<IterableInterval<?>, Polygon> implements
		ConvexHull {

	/**
	 * Andrew's monotone chain convex hull algorithm constructs the convex hull
	 * of a set of 2-dimensional points in O(n log n) time. It does so by first
	 * sorting the points lexicographically (first by x-coordinate, and in case
	 * of a tie, by y-coordinate), and then constructing upper and lower hulls
	 * of the points in O(n) time. An upper hull is the part of the convex hull,
	 * which is visible from the above. It runs from its rightmost point to the
	 * leftmost point in counterclockwise order. Lower hull is the remaining
	 * part of the convex hull.
	 */
	@Override
	public Polygon compute(final IterableInterval<?> input, final Polygon output) {

		if (input.numDimensions() != 2) {
			throw new IllegalArgumentException(
					"Convex Hull can only be computed in 2D");
		}

		// transfer iterableinterval into a list of point2d on which we can work
		// afterwards
		final Cursor<?> cursor = input.localizingCursor();
		final List<Point2D> points = new ArrayList<Point2D>();
		while (cursor.hasNext()) {
			cursor.fwd();
			points.add(new Point2D.Double(cursor.getDoublePosition(0), cursor
					.getDoublePosition(1)));
		}

		// Sort the points of P by x-coordinate (in case of a tie, sort by
		// y-coordinate).
		Collections.sort(points, new Comparator<Point2D>() {
			@Override
			public int compare(final Point2D o1, final Point2D o2) {
				final Double o1x = new Double(o1.getX());
				final Double o2x = new Double(o2.getX());
				final int result = o1x.compareTo(o2x);
				if (result == 0) {
					return new Double(o1.getY()).compareTo(new Double(o2.getY()));
				} else {
					return result;
				}
			}
		});

		// Initialize U and L as empty lists.
		// The lists will hold the vertices of upper and lower hulls
		// respectively.
		final List<Point2D> U = new ArrayList<Point2D>();
		final List<Point2D> L = new ArrayList<Point2D>();

		// build lower hull
		for (final Point2D p : points) {
			// while L contains at least two points and the sequence of last two
			// points of L and the point P[i] does not make a counter-clockwise
			// turn: remove the last point from L
			while (L.size() >= 2
					&& ccw(L.get(L.size() - 2), L.get(L.size() - 1), p) <= 0) {
				L.remove(L.size() - 1);
			}

			L.add(p);
		}

		// build upper hull
		Collections.reverse(points);
		for (final Point2D p : points) {
			// while U contains at least two points and the sequence of last two
			// points of U and the point P[i] does not make a counter-clockwise
			// turn: remove the last point from U
			while (U.size() >= 2
					&& ccw(U.get(U.size() - 2), U.get(U.size() - 1), p) <= 0) {
				U.remove(U.size() - 1);
			}

			U.add(p);
		}

		// Remove the last point of each list (it's the same as the first point
		// of the other list).
		L.remove(L.size() - 1);
		U.remove(U.size() - 1);

		// concatenate L and U
		L.addAll(U);

		// reset to zero...
		output.npoints = 0;
		
		// convert list into polygon
		for (final Point2D point2d : L) {
			output.addPoint((int) point2d.getX(), (int) point2d.getY());
		}

		return output;
	}

	/**
	 * 2D cross product of OA and OB vectors, i.e. z-component of their 3D cross
	 * product. Returns a positive value, if OAB makes a counter-clockwise turn,
	 * negative for clockwise turn, and zero if the points are collinear.
	 * 
	 * @param o
	 *            first point
	 * @param a
	 *            second point
	 * @param b
	 *            third point
	 * @return Returns a positive value, if OAB makes a counter-clockwise turn,
	 *         negative for clockwise turn, and zero if the points are
	 *         collinear.
	 */
	private double ccw(final Point2D o, final Point2D a, final Point2D b) {
		return (a.getX() - o.getX()) * (b.getY() - o.getY())
				- (a.getY() - o.getY()) * (b.getX() - o.getX());
	}

	@Override
	public Polygon createOutput(final IterableInterval<?> input) {
		return new Polygon();
	}

}
