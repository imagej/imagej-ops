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
 * Adds a pseudorandomly generated value {@code x} to a {@link RealType}
 * {@code I}, such that {@code x} is (inclusively) bounded by {@code rangeMin}
 * and {@code rangeMax} parameters, i.e. {@code rangeMin <= x <= rangeMax}.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Filter.AddUniformNoise.class)
public class AddUniformNoiseRealType<I extends RealType<I>>
	extends AbstractUnaryComputerOp<I, I> implements Ops.Filter.AddUniformNoise
{

	/**
	 * The greatest that an input value can be decreased.
	 */
	@Parameter
	private double rangeMin;

	/**
	 * The greatest that an input value can be <b> increased </b>
	 */
	@Parameter
	private double rangeMax;

	@Parameter(required = false)
	private Long seed;

	private MersenneTwisterFast rng;

	@Override
	public void initialize() {
		// setup rng with seed only if one was provided.
		if (rng == null) rng = seed == null ? new MersenneTwisterFast()
			: new MersenneTwisterFast(seed);
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
	public void compute(I input, I output) {
		final double newVal = (rangeMax - rangeMin) * rng.nextDouble(true, true) +
			rangeMin + input.getRealDouble();
		
		setOutput(newVal, true, output);
	}
	
	protected static <I extends RealType<I>> void setOutput(double newVal, boolean clampOutput, I output) {
		// clamp output
		if (clampOutput) {
			output.setReal(Math.max(output.getMinValue(), Math.min(output.getMaxValue(),
				newVal)));
		}

		// wrap output
		else {
			double outVal = newVal;
			// when output larger than max value, add difference of output and max
			// value to the min value
			while (outVal > output.getMaxValue()) {
				outVal = output.getMinValue() + (outVal - output.getMaxValue() - 1);
			}
			// when output smaller than min value, subtract difference of output and
			// min value from the max value
			while (outVal < output.getMinValue()) {
				outVal = output.getMaxValue() - (output.getMinValue() - outVal - 1);
			}

			output.setReal(outVal);
		}
		
	}

}
