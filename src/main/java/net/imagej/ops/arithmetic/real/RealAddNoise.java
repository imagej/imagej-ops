
/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops.arithmetic.real;

import java.util.Random;

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.MathOps;
import net.imagej.ops.Op;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the addition of the real
 * component of an input real number with an amount of Gaussian noise.
 * 
 * @author Barry DeZonia
 * @author Jonathan Hale
 */
@Plugin(type = Op.class, name = MathOps.AddNoise.NAME)
public class RealAddNoise<I extends RealType<I>, O extends RealType<O>> extends
	AbstractStrictFunction<I, O> implements MathOps.AddNoise
{

	@Parameter
	private double rangeMin;
	@Parameter
	private double rangeMax;
	@Parameter
	private double rangeStdDev;
	@Parameter
	private Random rng;

	@Override
	public O compute(final I input, final O output) {
		if (rng == null) {
			rng = new Random(System.currentTimeMillis());
		}
		int i = 0;
		do {
			final double newVal =
				input.getRealDouble() + (rng.nextGaussian() * rangeStdDev);
			if ((rangeMin <= newVal) && (newVal <= rangeMax)) {
				output.setReal(newVal);
				return output;
			}
			if (i++ > 100) throw new IllegalArgumentException(
				"noise function failing to terminate. probably misconfigured.");
		}
		while (true);
	}
}
