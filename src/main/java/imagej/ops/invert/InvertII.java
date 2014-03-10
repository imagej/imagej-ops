/*
 * #%L
 * A framework for reusable algorithms.
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

package imagej.ops.invert;

import imagej.ops.AbstractFunction;
import imagej.ops.Function;
import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.map.Mapper;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "invert")
public class InvertII<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<IterableInterval<I>, IterableInterval<O>> implements Invert
{

	@Parameter
	private OpService ops;

	@Override
	public IterableInterval<O> compute(IterableInterval<I> input,
		IterableInterval<O> output)
	{
		I inType = input.firstElement().createVariable();
		Function<I, O> invert;
		if (inType.getMinValue() < 0) {
			invert = new SignedRealInvert<I, O>();
		}
		else {
			invert = new UnsignedRealInvert<I, O>(inType.getMaxValue());
		}
		ops.run(Mapper.class, output, input, invert);
		return output;
	}

	private class SignedRealInvert<I extends RealType<I>, O extends RealType<O>>
		extends AbstractFunction<I, O>
	{

		@Override
		public O compute(final I x, final O output) {
			final double value = x.getRealDouble() * -1.0 - 1;
			output.setReal(value);
			return output;
		}

	}

	private class UnsignedRealInvert<I extends RealType<I>, O extends RealType<O>>
		extends AbstractFunction<I, O>
	{

		private final double max;

		/**
		 * @param max - maximum value of the range to invert about
		 */
		public UnsignedRealInvert(final double max) {
			this.max = max;
		}

		@Override
		public O compute(final I x, final O output) {
			final double value = max - x.getRealDouble();
			output.setReal(value);
			return output;
		}

	}

}
