/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Op used to translate the center of an interval the origin. This is needed for
 * FFT operations
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PaddingIntervalOrigin.class,
	name = Ops.Filter.PaddingIntervalOrigin.NAME,
	priority = Priority.HIGH)
public class PaddingIntervalOrigin<T extends ComplexType<T>, I extends RandomAccessibleInterval<T>, O extends Interval>
	extends AbstractBinaryFunctionOp<I, Interval, O> implements
	Ops.Filter.PaddingIntervalOrigin
{

	@Override
	@SuppressWarnings("unchecked")
	public O calculate(final I input, final Interval centeredInterval) {

		int numDimensions = input.numDimensions();

		// compute where to place the final Interval for the input so that the
		// coordinate in the center
		// of the input is at position (0,0).
		final long[] min = new long[numDimensions];
		final long[] max = new long[numDimensions];

		for (int d = 0; d < numDimensions; ++d) {
			min[d] = input.min(d) + input.dimension(d) / 2;
			max[d] = min[d] + centeredInterval.dimension(d) - 1;
		}

		return (O) new FinalInterval(min, max);
	}
}
