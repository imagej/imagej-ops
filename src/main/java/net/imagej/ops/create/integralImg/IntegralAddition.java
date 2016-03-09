/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.create.integralImg;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCI;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

/**
 * Computes an integral image from an input image with order <i>n</i>.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Create.IntegralAdd.class, priority = Priority.LOW_PRIORITY)
public class IntegralAddition<I extends RealType<I>> extends
	AbstractUnaryHybridCI<IterableInterval<I>> implements Ops.Create.IntegralAdd
{

	@Override
	public void compute1(final IterableInterval<I> input,
		final IterableInterval<I> output)
	{
		// TODO Input should just be one-dimensional (check!)

		Cursor<I> inputCursor = input.cursor();
		Cursor<I> outputCursor = output.cursor();

		I previousOutputValue = null;

		while (outputCursor.hasNext()) {
			// TODO Second cursor which is one position behind

			I inputValue = inputCursor.next();
			I outputValue = outputCursor.next();

			if (previousOutputValue == null) {
				// TODO Test speed of copy vs. 2nd cursor
				previousOutputValue = outputValue.copy();
				continue;
			}

			previousOutputValue.add(inputValue);

			outputValue.set(previousOutputValue.copy());
		}
	}

}
