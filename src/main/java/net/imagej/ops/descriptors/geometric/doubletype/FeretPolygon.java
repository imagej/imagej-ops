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

package net.imagej.ops.descriptors.geometric.doubletype;

import java.awt.Polygon;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imagej.ops.descriptors.geometric.Feret;
import net.imagej.ops.descriptors.geometric.FeretResult;
import net.imglib2.Point;

import org.scijava.plugin.Plugin;

/**
 * Calculating {@link FeretResult} on {@link Polygon}
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, name = Feret.NAME, label = Feret.LABEL)
public class FeretPolygon extends AbstractFunction<Polygon, FeretResult>
		implements Feret {

	@Override
	public FeretResult compute(final Polygon input, FeretResult output) {
		if (output == null)
			output = new FeretResult();

		double maxDiameter = -Double.MAX_VALUE;
		final long[] tmp = { 0, 0 };
		final Point maxP1 = new Point(tmp);
		final Point maxP2 = new Point(tmp);

		for (int i = 0; i < input.npoints; i++) {
			for (int j = 0; j < input.npoints; j++) {
				if (i != j) {
					double dist = 0.0;

					dist += (input.xpoints[i] - input.xpoints[j])
							* (input.xpoints[i] - input.xpoints[j]);
					dist += (input.ypoints[i] - input.ypoints[j])
							* (input.ypoints[i] - input.ypoints[j]);

					if (dist > maxDiameter) {
						maxP1.setPosition(input.xpoints[i], 0);
						maxP1.setPosition(input.ypoints[i], 1);
						maxP2.setPosition(input.xpoints[j], 0);
						maxP2.setPosition(input.ypoints[j], 1);
						maxDiameter = dist;
					}
				}
			}
		}

		// sqrt for euclidean
		maxDiameter = Math.sqrt(maxDiameter);

		output.max = maxDiameter;
		output.p1 = maxP1;
		output.p2 = maxP2;

		return output;
	}
}
