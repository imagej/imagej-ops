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

package net.imagej.ops.image.invert;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn (University of Konstanz)
 */
@Plugin(type = Ops.Image.Invert.class, priority = Priority.NORMAL_PRIORITY + 1)
public class InvertIterableInterval<I extends RealType<I>, O extends RealType<O>>
	extends AbstractUnaryComputerOp<IterableInterval<I>, IterableInterval<O>>
	implements Ops.Image.Invert
{

	private UnaryComputerOp<IterableInterval<I>, IterableInterval<O>> mapper;

	@Override
	public void initialize() {
		final I inType = in().firstElement().createVariable();
		final double minVal = inType.getMinValue();
		final UnaryComputerOp<I, O> invert = minVal < 0 ? new SignedRealInvert<>()
			: new UnsignedRealInvert<>(inType.getMaxValue());
		mapper = Computers.unary(ops(), Ops.Map.class, out(), in(), invert);
	}

	@Override
	public void compute1(final IterableInterval<I> input,
		final IterableInterval<O> output)
	{
		mapper.compute1(input, output);
	}

	private class SignedRealInvert<II extends RealType<II>, OO extends RealType<OO>>
		extends AbstractUnaryComputerOp<II, OO>
	{

		@Override
		public void compute1(final II x, final OO output) {
			final double value = x.getRealDouble() * -1.0 - 1;
			output.setReal(value);
		}

	}

	private class UnsignedRealInvert<II extends RealType<II>, OO extends RealType<OO>>
		extends AbstractUnaryComputerOp<II, OO>
	{

		private final double max;

		/**
		 * @param max - maximum value of the range to invert about
		 */
		public UnsignedRealInvert(final double max) {
			this.max = max;
		}

		@Override
		public void compute1(final II x, final OO output) {
			final double value = max - x.getRealDouble();
			output.setReal(value);
		}

	}

}
