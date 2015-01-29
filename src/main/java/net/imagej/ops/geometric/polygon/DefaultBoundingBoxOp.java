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
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.BoundingBoxPolygon;
import net.imglib2.RealPoint;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Returns the bounds of a polygon (a polygon consisting of four points).
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = BoundingBoxPolygon.NAME)
public class DefaultBoundingBoxOp implements BoundingBoxPolygon {

	@Parameter(type = ItemIO.OUTPUT)
	private Polygon output;

	@Parameter(type = ItemIO.INPUT)
	private Polygon input;

	@Override
	public void run() {
		double min_x = Double.POSITIVE_INFINITY;
		double max_x = Double.NEGATIVE_INFINITY;
		double min_y = Double.POSITIVE_INFINITY;
		double max_y = Double.NEGATIVE_INFINITY;

		for (RealPoint RealPoint : input.getPoints()) {
			if (RealPoint.getDoublePosition(0) < min_x) {
				min_x = RealPoint.getDoublePosition(0);
			}
			if (RealPoint.getDoublePosition(0) > max_x) {
				max_x = RealPoint.getDoublePosition(0);
			}
			if (RealPoint.getDoublePosition(1) < min_y) {
				min_y = RealPoint.getDoublePosition(1);
			}
			if (RealPoint.getDoublePosition(1) > max_y) {
				max_y = RealPoint.getDoublePosition(1);
			}
		}

		List<RealPoint> bounds = new ArrayList<RealPoint>();
		bounds.add(new RealPoint(min_x, min_y));
		bounds.add(new RealPoint(min_x, max_y));
		bounds.add(new RealPoint(max_x, max_y));
		bounds.add(new RealPoint(max_x, min_y));
		output = new Polygon(bounds);
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
