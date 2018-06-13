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

package net.imagej.ops.filter.convolve;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractFFTFilterC;
import net.imagej.ops.filter.FFTMethodsLinearFFTFilterC;
import net.imagej.ops.math.IIToIIOutputII;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.hybrid.BinaryHybridCF;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Convolve op for (@link RandomAccessibleInterval)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Ops.Filter.Convolve.class, priority = Priority.LOW)
public class ConvolveFFTC<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends
	AbstractFFTFilterC<I, O, K, C>
	implements Ops.Filter.Convolve
{

	private BinaryHybridCF<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> mul;

	private BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> linearFilter;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		mul = Hybrids.binaryCF(ops(), IIToIIOutputII.Multiply.class, getFFTInput(),
			getFFTKernel(), getFFTInput());

		// create a convolver by creating a linear filter and passing the multiplier as
		// the frequency operation
		linearFilter = (BinaryComputerOp) Computers.binary(ops(),
			FFTMethodsLinearFFTFilterC.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			getFFTInput(), getFFTKernel(), getPerformInputFFT(),
			getPerformKernelFFT(), mul);

	}

	/**
	 * Call the linear filter that is set up to perform convolution
	 */
	@Override
	public void compute(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{
		linearFilter.compute(in, kernel, out);
	}
}
