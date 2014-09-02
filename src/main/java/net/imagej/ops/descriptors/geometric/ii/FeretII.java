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

package net.imagej.ops.descriptors.geometric.ii;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.descriptors.geometric.Feret;
import net.imagej.ops.descriptors.geometric.FeretResult;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.Point;

import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Feret.NAME, label = Feret.LABEL)
public class FeretII extends
		AbstractOutputFunction<IterableInterval<?>, FeretResult> implements
		Feret {

	@Override
	public FeretResult compute(final IterableInterval<?> input,
			final FeretResult output) {

		double maxDiameter = Double.MIN_VALUE;
		final Point maxP1 = new Point(input.numDimensions());
		final Point maxP2 = new Point(input.numDimensions());

		final Cursor<?> cursor1 = input.localizingCursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();

			final Cursor<?> cursor2 = input.localizingCursor();
			while (cursor2.hasNext()) {
				cursor2.fwd();

				double dist = 0.0f;
				for (int i = 0; i < cursor1.numDimensions(); i++) {
					double a = Math.abs(cursor1.getIntPosition(i)
							- cursor2.getIntPosition(i)) + 1;
					dist += a * a;

				}

				if (dist > maxDiameter) {
					maxDiameter = dist;
					maxP1.setPosition(cursor1);
					maxP2.setPosition(cursor2);
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

	@Override
	public FeretResult createOutput(final IterableInterval<?> input) {
		return new FeretResult();
	}
}
