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

package net.imagej.ops.filter.addUniformNoise;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.MersenneTwisterFast;

/**
 * Sets the real output value of a {@link Realtype} T to a randomly generated
 * value x, bounded by (and including) the {@code rangeMin} and {@code rangeMax}
 * parameters, i.e. {@code rangeMin <= x <= rangeMax}.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Filter.AddUniformNoise.class)
public class AddUniformNoiseRealType<I extends RealType<I>, O extends RealType<O>>
	extends AbstractUnaryComputerOp<I, O> implements Ops.Filter.AddUniformNoise
{

	@Parameter
	private double rangeMin;

	@Parameter
	private double rangeMax;

	@Parameter(required = false)
	private long seed = 0xabcdef1234567890L;

	private MersenneTwisterFast rng;

	@Override
	public void initialize() {
		if (rng == null) rng = new MersenneTwisterFast(seed);
		if (rangeMax < rangeMin) {
			double temp = rangeMax;
			rangeMax = rangeMin;
			rangeMin = temp;
		}

		// if an unacceptable value is passed for either range param (i.e. a NaN
		// value or a Double so large that the maths will become infected by
		// INFINITY, do not proceed.
		if (!Double.isFinite(rangeMax - rangeMin))
			throw new IllegalArgumentException("range not allowed by op.");
	}

	@Override
	public void compute(I input, O output) {
		int i = 0;
		do {
			final double newVal = (rangeMax - rangeMin) * rng.nextDouble(true, true) +
				rangeMin + input.getRealDouble();
			if (newVal <= input.getMaxValue() && newVal >= input.getMinValue()) {
				output.setReal(newVal);
				return;
			}
			if (i++ > 100) {
				throw new IllegalArgumentException(
					"noise function failing to terminate. probably misconfigured.");
			}
		}
		while (true);
	}

}
