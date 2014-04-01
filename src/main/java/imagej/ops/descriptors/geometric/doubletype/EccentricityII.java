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

package imagej.ops.descriptors.geometric.doubletype;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.descriptors.geometric.Eccentricity;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Calculating {@link Eccentricity} on {@link Iterable}
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, name = Eccentricity.NAME, label = Eccentricity.LABEL,
	priority = -1.0)
public class EccentricityII extends
	AbstractFunction<IterableInterval<?>, DoubleType> implements
	Eccentricity<IterableInterval<?>, DoubleType>
{

	@Override
	public DoubleType compute(final IterableInterval<?> input, DoubleType output)
	{

		if (output == null) output = new DoubleType();

		final Cursor<?> cursor = input.localizingCursor();

		final int d = cursor.numDimensions();
		final long[] c1 = new long[d];
		final long[] c2 = new long[d];

		for (int i = 0; i < d; i++) {
			c1[i] = Integer.MAX_VALUE;
			c2[i] = -Integer.MAX_VALUE;
		}

		// get corners of bounding box
		while (cursor.hasNext()) {
			cursor.fwd();
			for (int dim = 0; dim < d; dim++) {
				final int pos = cursor.getIntPosition(dim);
				c1[dim] = (c1[dim] > pos) ? pos : c1[dim];
				c2[dim] = (c2[dim] < pos) ? pos : c2[dim];
			}
		}

		final long[] length = new long[d];
		for (int dim = 0; dim < 2; dim++) {
			length[dim] = Math.abs(c1[dim] - c2[dim]);
		}

		double res = 0;
		if (length[0] > length[1]) {
			res = length[0] / length[1];
		}
		else {
			res = length[1] / length[0];
		}

		output.set(res);

		return output;
	}
}
