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

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.descriptors.geometric.Area;
import net.imagej.ops.descriptors.geometric.CenterOfGravity;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculating {@link CenterOfGravity} on {@link Polygon}
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, priority = -1, name = CenterOfGravity.NAME, label = CenterOfGravity.LABEL)
public class CenterOfGravityPolygon extends AbstractFunction<Polygon, double[]>
		implements CenterOfGravity {

	@Parameter
	private OpService ops;

	@Override
	public double[] compute(final Polygon input, double[] output) {

		if(output == null){
			output = new double[2];
		}

		final double area = ((DoubleType) ops.run(Area.class, input)).get();

		// Yang Mingqiang:
		// A Survey of Shape Feature Extraction Techniques
		// in Pattern Recognition Techniques, Technology and Applications, 2008
		for (int i = 0; i < input.npoints; i++) {
			final double x = input.xpoints[i];
			final double x1 = input.xpoints[i + 1];
			final double y = input.ypoints[i];
			final double y1 = input.ypoints[i + 1];

			output[0] += (x + x1) * (x * y1 - x1 * y);
			output[1] += (y + y1) * (x * y1 - x1 * y);
		}

		final double x = input.xpoints[input.npoints - 1];
		final double x1 = input.xpoints[0];
		final double y = input.ypoints[input.npoints - 1];
		final double y1 = input.ypoints[0];

		output[0] += (x + x1) * (x * y1 - x1 * y);
		output[1] += (y + y1) * (x * y1 - x1 * y);

		output[0] = (1 / (6 * area)) * Math.abs(output[0]);
		output[1] = (1 / (6 * area)) * Math.abs(output[1]);

		return output;
	}

}
