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
package net.imagej.ops.geometric.interval;

import net.imagej.ops.Op;
import net.imagej.ops.geometric.interval.GeometricIntervalOps.BoundingBoxInterval;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
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
@Plugin(type = Op.class, name = BoundingBoxInterval.NAME)
public class DefaultBoundingBoxOp implements BoundingBoxInterval {

	@Parameter(type = ItemIO.OUTPUT)
	private Interval output;

	@Parameter(type = ItemIO.INPUT)
	private Polygon input;

	@Override
	public void run() {
		long min_x = Long.MAX_VALUE;
		long max_x = Long.MIN_VALUE;
		long min_y = Long.MAX_VALUE;
		long max_y = Long.MIN_VALUE;

		for (RealPoint point : input.getPoints()) {
			if (point.getDoublePosition(0) < min_x) {
				min_x = (long) point.getDoublePosition(0);
			}
			if (point.getDoublePosition(0) > max_x) {
				max_x = (long) point.getDoublePosition(0);
			}
			if (point.getDoublePosition(1) < min_y) {
				min_y = (long) point.getDoublePosition(1);
			}
			if (point.getDoublePosition(1) > max_y) {
				max_y = (long) point.getDoublePosition(1);
			}
		}

		output = new FinalInterval(new long[] { min_x, min_y }, new long[] {
				max_x, max_y });
	}

	@Override
	public Interval getOutput() {
		return output;
	}

	@Override
	public void setOutput(Interval output) {
		this.output = output;
	}
}
