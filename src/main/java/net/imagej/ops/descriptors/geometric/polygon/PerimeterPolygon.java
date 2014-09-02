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

package net.imagej.ops.descriptors.geometric.polygon;

import java.awt.Polygon;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.descriptors.geometric.CenterOfGravity;
import net.imagej.ops.descriptors.geometric.Perimeter;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * see: Perimeter of a polygon as used in imagej
 * (http://rsb.info.nih.gov/ij/developer
 * /source/ij/process/FloatPolygon.java.html)
 * 
 * @author Andreas Graumann
 * @author Christian Dietz
 */
@Plugin(type = Perimeter.class, priority = 1, label = Perimeter.LABEL, name = Perimeter.NAME)
public class PerimeterPolygon extends
		AbstractOutputFunction<Polygon, DoubleType> implements Perimeter {

	@Override
	public DoubleType compute(final Polygon input, final DoubleType output) {

		double dx, dy;
		double length = 0.0;

		for (int i = 0; i < input.npoints - 1; i++) {
			dx = Math.abs(input.xpoints[i + 1] - input.xpoints[i]);
			dy = Math.abs(input.ypoints[i + 1] - input.ypoints[i]);
			dx = (dx == 0) ? 0 : dx + 1;
			dy = (dy == 0) ? 0 : dy + 1;

			length += Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		}

		dx = Math.abs(input.xpoints[input.npoints - 1] - input.xpoints[0]);
		dy = Math.abs(input.ypoints[input.npoints - 1] - input.ypoints[0]);
		dx = (dx == 0) ? 0 : dx + 1;
		dy = (dy == 0) ? 0 : dy + 1;

		length += Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

		output.setReal(length);
		return output;
	}

	@Override
	public DoubleType createOutput(final Polygon input) {
		return new DoubleType();
	}
}
