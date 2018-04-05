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
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCFI;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Adds Gaussian noise to a real number.
 * <p>
 * This op is a hybrid computer/function/inplace, since input and output types
 * are the same. For input and output of different types, see
 * {@link AddNoiseRealType}.
 * </p>
 */
@Plugin(type = Ops.Filter.AddNoise.class, priority = Priority.HIGH)
public class AddNoiseRealTypeCFI<T extends RealType<T>> extends
	AbstractUnaryHybridCFI<T, T> implements Ops.Filter.AddNoise
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
	public void compute(final T input, final T output) {
		if (rng == null) rng = new Random(seed);
		AddNoiseRealType.addNoise(input, output, rangeMin, rangeMax, rangeStdDev,
			rng);
	}

	// -- UnaryInplaceOp methods --

	@Override
	public void mutate(final T arg) {
		if (rng == null) rng = new Random(seed);
		AddNoiseRealType.addNoise(arg, arg, rangeMin, rangeMax, rangeStdDev, rng);
	}

	// -- UnaryOutputFactory methods --

	@Override
	public T createOutput(final T input) {
		return input.createVariable();
	}

}
