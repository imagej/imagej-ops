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

package net.imagej.ops.filter.dog;

import java.util.Arrays;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Difference of Gaussians (DoG) implementation where all sigmas are the same in
 * each dimension. Internally running DoG on arrays.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Filter.DoG.class, priority = 1.0)
public class DoGSingleSigmas<T extends NumericType<T> & NativeType<T>> extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Filter.DoG
{

	@Parameter
	private ThreadService ts;

	@Parameter
	private double sigma1;

	@Parameter
	private double sigma2;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> op;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> fac;

	@Override
	public void initialize() {
		final double[] sigmas1 = new double[in().numDimensions()];
		final double[] sigmas2 = new double[in().numDimensions()];

		Arrays.fill(sigmas1, sigma1);
		Arrays.fill(sigmas2, sigma2);

		op = Computers.unary(ops(), Ops.Filter.DoG.class, out(), in(), sigmas1, sigmas2,
			fac);
	}

	@Override
	public RandomAccessibleInterval<T> createOutput(
		final RandomAccessibleInterval<T> input)
	{
		// HACK: Make Java 6 javac compiler happy.
		return ops().create().<T> img(input);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<T> output)
	{
		op.compute(input, output);
	}
}
