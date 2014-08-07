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

package net.imagej.ops.invert;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Function;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.map.Map;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn
 */
@Plugin(type = Op.class, name = "invert", priority = Priority.NORMAL_PRIORITY + 1)
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
		ops.run(Map.class, output, input, invert);
		return output;
	}

	private class SignedRealInvert<II extends RealType<II>, OO extends RealType<OO>>
		extends AbstractFunction<II, OO>
	{

		@Override
		public OO compute(final II x, final OO output) {
			final double value = x.getRealDouble() * -1.0 - 1;
			output.setReal(value);
			return output;
		}

	}

	private class UnsignedRealInvert<II extends RealType<II>, OO extends RealType<OO>>
		extends AbstractFunction<II, OO>
	{

		private final double max;

		/**
		 * @param max - maximum value of the range to invert about
		 */
		public UnsignedRealInvert(final double max) {
			this.max = max;
		}

		@Override
		public OO compute(final II x, final OO output) {
			final double value = max - x.getRealDouble();
			output.setReal(value);
			return output;
		}

	}

}
