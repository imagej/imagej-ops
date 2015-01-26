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
package net.imagej.ops.geometric.polygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.imglib2.RealPoint;

/**
 * Polygon class.
 * 
 * Currently only a list of {@link Point2D} to be able to store points with
 * floating point coordinates.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public class Polygon {

	private final List<RealPoint> points;

	/**
	 * Default constructor, create a new empty polygon.
	 */
	public Polygon() {
		this(new ArrayList<RealPoint>());
	}

	/**
	 * Create a new polygon from the given list of points.
	 * 
	 * @param points
	 *            A list of points
	 */
	public Polygon(List<RealPoint> points) {
		this.points = points;
	}

	/**
	 * @return the number of points of this polygon.
	 */
	public int size() {
		return points.size() + 1;
	}

	/**
	 * 
	 * @param index
	 *            an index
	 * @return the point at the given index.
	 */
	public RealPoint getPoint(int index) {
		if (index < points.size()) {
			return points.get(index);
		} else if (index == points.size()) {
			return points.get(0);
		} else {
			throw new ArrayIndexOutOfBoundsException("");
		}
	}

	/**
	 * @return A list with the points of the polygon. DO NOT MODIFY!
	 */
	public List<RealPoint> getPoints() {
		return points;
	}

	/**
	 * Add the given point as the last one of the polygon.
	 * 
	 * @param point
	 *            a point
	 */
	public void add(RealPoint point) {
		points.add(point);
	}

	/**
	 * Remove the given point from the polygon.
	 * 
	 * @param point
	 *            a point
	 */
	public void remove(RealPoint point) {
		points.remove(point);
	}

	/**
	 * Remove the point at the given index from the polygon.
	 * 
	 * @param index
	 *            an index
	 */
	public void remove(int index) {
		points.remove(index);
	}

	/**
	 * Removes every point from the polygon.
	 */
	public void clear() {
		points.clear();
	}
}
