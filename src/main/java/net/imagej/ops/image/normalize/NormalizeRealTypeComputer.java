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

package net.imagej.ops.image.normalize;

import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.converter.Converter;
import net.imglib2.type.numeric.RealType;

/**
 * Simple {@link UnaryComputerOp} and {@link Converter} to perform a
 * normalization.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Leon Yang
 */
class NormalizeRealTypeComputer<T extends RealType<T>> extends
	AbstractUnaryComputerOp<T, T> implements Converter<T, T>
{

	private double targetMin, targetMax, sourceMin, factor;

	public NormalizeRealTypeComputer() {}

	public NormalizeRealTypeComputer(final double sourceMin,
		final double sourceMax, final double targetMin, final double targetMax)
	{
		setup(sourceMin, sourceMax, targetMin, targetMax);
	}

	public void setup(final double sourceMin, final double sourceMax,
		final double targetMin, final double targetMax)
	{
		this.sourceMin = sourceMin;
		final double tmp = sourceMax;
		this.targetMin = targetMin;
		this.targetMax = targetMax;

		this.factor = 1.0d / (tmp - this.sourceMin) * (this.targetMax -
			this.targetMin);
	}

	@Override
	public void compute(final T input, final T output) {

		final double res = (input.getRealDouble() - sourceMin) * factor + targetMin;

		if (res > targetMax) {
			output.setReal(targetMax);
		}
		else if (res < targetMin) {
			output.setReal(targetMin);
		}
		else {
			output.setReal(res);
		}
	}

	@Override
	public void convert(T input, T output) {
		compute(input, output);
	}
}
