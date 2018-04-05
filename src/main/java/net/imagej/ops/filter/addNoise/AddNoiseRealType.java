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

package net.imagej.ops.filter.addNoise;

import java.util.Random;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the addition of the real
 * component of an input real number with an amount of Gaussian noise.
 */
@Plugin(type = Ops.Filter.AddNoise.class)
public class AddNoiseRealType<I extends RealType<I>, O extends RealType<O>>
	extends AbstractUnaryComputerOp<I, O> implements Ops.Filter.AddNoise
{

	@Parameter
	private double rangeMin;

	@Parameter
	private double rangeMax;

	@Parameter
	private double rangeStdDev;

	@Parameter(required = false)
	private long seed = 0xabcdef1234567890L;
	
	private Random rng;

	// -- UnaryComputerOp methods --

	@Override
	public void compute(final I input, final O output) {
		if (rng == null) rng = new Random(seed);
		addNoise(input, output, rangeMin, rangeMax, rangeStdDev, rng);
	}

	// -- Static utility methods --

	public static <I extends RealType<I>, O extends RealType<O>> void addNoise(
		final I input, final O output, final double rangeMin, final double rangeMax,
		final double rangeStdDev, final Random rng)
	{
		int i = 0;
		do {
			final double newVal =
				input.getRealDouble() + (rng.nextGaussian() * rangeStdDev);
			if ((rangeMin <= newVal) && (newVal <= rangeMax)) {
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
