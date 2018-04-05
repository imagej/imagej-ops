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

package net.imagej.ops.image.integral;

import net.imagej.ops.special.hybrid.AbstractUnaryHybridCI;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

/**
 * Implements the row-wise addition required for computations of squared
 * integral images.
 *
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> type of inputs
 */
class IntegralSquareAndAdd<I extends RealType<I>> extends
	AbstractUnaryHybridCI<IterableInterval<I>, IterableInterval<I>>
{

	@Override
	public void compute(final IterableInterval<I> input,
		final IterableInterval<I> output)
	{

		final Cursor<I> inputCursor = input.cursor();
		final Cursor<I> outputCursor = output.cursor();

		double tmp = 0.0d;
		while (outputCursor.hasNext()) {

			final I inputValue = inputCursor.next();
			final I outputValue = outputCursor.next();

			tmp += Math.pow(inputValue.getRealDouble(), 2);

			outputValue.setReal(tmp);
		}
	}

}
