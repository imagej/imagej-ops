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

package net.imagej.ops.filter;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.fft.FFTMethodsComputerOp;
import net.imagej.ops.filter.ifft.IFFTComputerOp;
import net.imagej.ops.special.BinaryComputerOp;
import net.imagej.ops.special.BinaryHybridOp;
import net.imagej.ops.special.Computers;
import net.imagej.ops.special.UnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Convolve op for (@link RandomAccessibleInterval)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Ops.Filter.Convolve.class, priority = Priority.LOW_PRIORITY)
public class FFTMethodsLinearFFTFilter<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends
	AbstractFFTFilterComputer<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>, RandomAccessibleInterval<K>, RandomAccessibleInterval<C>>
	implements Ops.Filter.Convolve
{

	@Parameter
	BinaryHybridOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> frequencyOp;

	UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<C>> fftIn;

	UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<C>> fftKernel;

	UnaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<O>> ifft;

	BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> linearFilter;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		fftIn = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsComputerOp.class,
			getFFTInput(), RandomAccessibleInterval.class);

		fftKernel = (UnaryComputerOp) Computers.unary(ops(),
			FFTMethodsComputerOp.class, getFFTKernel(),
			RandomAccessibleInterval.class);

		ifft = (UnaryComputerOp) Computers.unary(ops(), IFFTComputerOp.class,
			RandomAccessibleInterval.class, getFFTKernel());

	}

	/**
	 * Perform convolution by multiplying the FFTs in the frequency domain
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void compute2(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{
		// TODO: create this op in initialize... for some reason when I tried it I
		// got an error
		// need to investigate...
		linearFilter = (BinaryComputerOp) Computers.binary(ops(),
			DefaultLinearFFTFilter.class, out, in, kernel, getFFTInput(),
			getFFTKernel(), getPerformInputFFT(), getPerformKernelFFT(), fftIn,
			fftKernel, frequencyOp, ifft);

		linearFilter.compute2(in, kernel, out);

	}
}
