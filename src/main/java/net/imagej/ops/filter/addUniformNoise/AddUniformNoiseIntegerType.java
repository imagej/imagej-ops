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

import java.math.BigInteger;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.type.numeric.IntegerType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.MersenneTwisterFast;

/**
 * Adds a pseudorandomly generated value {@code x} to a {@link IntegerType}
 * {@code I}, such that {@code x} is (inclusively) bounded by {@code rangeMin}
 * and {@code rangeMax} parameters, i.e. {@code rangeMin <= x <= rangeMax}.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Filter.AddUniformNoise.class, priority = Priority.HIGH)
public class AddUniformNoiseIntegerType<I extends IntegerType<I>> extends
	AbstractUnaryComputerOp<I, I> implements Ops.Filter.AddUniformNoise
{

	/**
	 * The greatest that an input value can be decreased.
	 */
	@Parameter
	private long rangeMin;

	/**
	 * The greatest that an input value can be <b> increased </b>
	 */
	@Parameter
	private long rangeMax;

	/**
	 * If false, the Op will wrap outputs that are outside of the type bounds,
	 * instead of clamping them
	 */
	@Parameter(required = false)
	private boolean clampOutput = true;

	@Parameter(required = false)
	private Long seed;
	private long range;

	private MersenneTwisterFast rng;

	@Override
	public void initialize() {
		// setup rng with seed only if one was provided.
		if (rng == null) rng = seed == null ? new MersenneTwisterFast()
			: new MersenneTwisterFast(seed);
		if (rangeMax < rangeMin) {
			long temp = rangeMax;
			rangeMax = rangeMin;
			rangeMin = temp;
		}
		// MersenneTwister can only generate numbers that can fit into a long.
		range = Math.subtractExact(rangeMax + 1, rangeMin);
	}

	@Override
	public void compute(I input, I output) {
		final double newVal = rng.nextLong(range) + rangeMin + input
			.getRealDouble();

		AddUniformNoiseRealType.setOutput(newVal, clampOutput, output);
	}

}
