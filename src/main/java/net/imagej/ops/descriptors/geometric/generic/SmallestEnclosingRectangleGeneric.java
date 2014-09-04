/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.descriptors.geometric.generic;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.descriptors.DescriptorService;
import net.imagej.ops.descriptors.geometric.ConvexHull;
import net.imagej.ops.descriptors.geometric.SmallestEnclosingRectangle;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link SmallestEnclosingRectangle}. Use
 * {@link DescriptorService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, label = SmallestEnclosingRectangle.LABEL, name = SmallestEnclosingRectangle.NAME)
public class SmallestEnclosingRectangleGeneric implements
		SmallestEnclosingRectangle {

	@Parameter(type = ItemIO.INPUT)
	private ConvexHull result;

	@Parameter(type = ItemIO.OUTPUT)
	private List<Point2D> out;

	@Override
	public List<Point2D> getOutput() {
		return out;
	}

	/**
	 * Rotates the given Polygon consisting of a list of Point2D points by the
	 * given angle about the given center point.
	 *
	 * @param inPoly
	 *            A Polygon consisting of a list of Point2D points
	 * @param angle
	 *            the rotation angle
	 * @param center
	 *            the rotation center
	 * @return
	 */
	private List<Point2D> rotate(List<Point2D> inPoly, double angle,
			Point2D center) {
		List<Point2D> outPoly = new ArrayList<Point2D>();
		AffineTransform at = new AffineTransform();
		at.rotate(angle);
		for (Point2D point2d : inPoly) {
			outPoly.add(at.transform(point2d, null));
		}
		return outPoly;
	}

	/**
	 * Calculates the area of the bounds of a given polygon.
	 *
	 * @param poly
	 *            A Polygon consisting of a list of Point2D points
	 * @return the area of the bounds
	 */
	private double getBoundsArea(List<Point2D> poly) {
		double min_x = Double.POSITIVE_INFINITY;
		double max_x = Double.NEGATIVE_INFINITY;
		double min_y = Double.POSITIVE_INFINITY;
		double max_y = Double.NEGATIVE_INFINITY;
		for (Point2D point2d : poly) {
			if (point2d.getX() < min_x) {
				min_x = point2d.getX();
			}
			if (point2d.getX() > max_x) {
				max_x = point2d.getX();
			}
			if (point2d.getY() < min_y) {
				min_y = point2d.getY();
			}
			if (point2d.getY() > max_y) {
				max_y = point2d.getY();
			}
		}
		return Math.abs(max_x - min_x) * Math.abs(max_y - min_y);
	}

	/**
	 * Calculates the bounds of a given polygon.
	 *
	 * @param poly
	 *            A Polygon consisting of a list of Point2D points
	 * @return the bounds
	 */
	private List<Point2D> getBounds(List<Point2D> poly) {
		double min_x = Double.POSITIVE_INFINITY;
		double max_x = Double.NEGATIVE_INFINITY;
		double min_y = Double.POSITIVE_INFINITY;
		double max_y = Double.NEGATIVE_INFINITY;
		for (Point2D point2d : poly) {
			if (point2d.getX() < min_x) {
				min_x = point2d.getX();
			}
			if (point2d.getX() > max_x) {
				max_x = point2d.getX();
			}
			if (point2d.getY() < min_y) {
				min_y = point2d.getY();
			}
			if (point2d.getY() > max_y) {
				max_y = point2d.getY();
			}
		}
		List<Point2D> bounds = new ArrayList<Point2D>();
		bounds.add(new Point2D.Double(min_x, min_y));
		bounds.add(new Point2D.Double(min_x, max_y));
		bounds.add(new Point2D.Double(max_x, max_y));
		bounds.add(new Point2D.Double(max_x, min_y));
		return bounds;
	}

	/**
	 * Calculate the centroid of this polygon, method taken from Wikipedia.
	 *
	 * @see <a
	 *      href="http://en.wikipedia.org/wiki/Centroid#Centroid_of_polygon">Wikipedia</a>
	 *
	 * @param polygon
	 *            A Polygon consisting of a list of Point2D points
	 * @return The Centroid of this Polygon.
	 */
	private Point2D getCenter(List<Point2D> polygon) {
		double area = getArea(polygon);
		double cx = 0;
		for (int i = 0; i < polygon.size() - 1; i++) {
			cx += (polygon.get(i).getX() + polygon.get(i + 1).getX())
					* (polygon.get(i).getX() * polygon.get(i + 1).getY() - polygon
							.get(i + 1).getX() * polygon.get(i).getY());
		}
		cx /= (6 * area);
		double cy = 0;
		for (int i = 0; i < polygon.size() - 1; i++) {
			cy += (polygon.get(i).getY() + polygon.get(i + 1).getY())
					* (polygon.get(i).getX() * polygon.get(i + 1).getY() - polygon
							.get(i + 1).getX() * polygon.get(i).getY());
		}
		cy /= (6 * area);
		return new Point2D.Double(cx, cy);
	}

	/**
	 * Calculate the area of this polygon, method taken from Wikipedia.
	 *
	 * @see <a
	 *      href="http://en.wikipedia.org/wiki/Centroid#Centroid_of_polygon">Wikipedia</a>
	 *
	 * @param polygon
	 *            A Polygon consisting of a list of Point2D points
	 * @return the area of this polygon
	 */
	private double getArea(List<Point2D> polygon) {
		double sum = 0;
		for (int i = 0; i < polygon.size() - 1; i++) {
			sum += (polygon.get(i).getX() * polygon.get(i + 1).getY() - polygon
					.get(i + 1).getX() * polygon.get(i).getY());
		}
		return sum / 2;
	}

	@Override
	public void run() {
		// first of all, calculate the center of the given polygon
		List<Point2D> polygon = new ArrayList<Point2D>();
		for (int i = 0; i < result.getOutput().npoints; i++) {
			polygon.add(new Point2D.Double(result.getOutput().xpoints[i],
					result.getOutput().ypoints[i]));
		}
		Point2D polygonCenter = getCenter(polygon);

		List<Point2D> minBounds = null;
		double minArea = Double.POSITIVE_INFINITY;
		// for each edge (i.e. line from P(i-1) to P(i)
		for (int i = 1; i < polygon.size(); i++) {
			double angle = Math.atan2(polygon.get(i).getY()
					- polygon.get(i - 1).getY(), polygon.get(i).getX()
					- polygon.get(i - 1).getX());
			// rotate the polygon in such a manner that the line has an angle of
			// 0
			List<Point2D> rotatedPoly = rotate(polygon, -angle, polygonCenter);
			// get the bounds
			List<Point2D> bounds = getBounds(rotatedPoly);
			// calculate the area of the bounds
			double area = getBoundsArea(bounds);
			// if the area of the bounds is smaller, rotate it to match the
			// original polygon and save it.
			if (area < minArea) {
				minArea = area;
				minBounds = rotate(bounds, angle, polygonCenter);
			}
		}

		out = minBounds;
	}

}
