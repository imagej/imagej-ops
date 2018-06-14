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

package net.imagej.ops.filter;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.fft.FFTMethodsOpC;
import net.imagej.ops.filter.ifft.IFFTMethodsOpC;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
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
@Plugin(type = Ops.Filter.LinearFilter.class, priority = Priority.LOW)
public class FFTMethodsLinearFFTFilterC<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFFTFilterC<I, O, K, C> implements Ops.Filter.LinearFilter
{

	// TODO: should this be a parameter? figure out best way to override
	// frequencyOp
	@Parameter
	private BinaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> frequencyOp;

	private UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<C>> fftIn;

	private UnaryComputerOp<RandomAccessibleInterval<K>, RandomAccessibleInterval<C>> fftKernel;

	private UnaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<O>> ifft;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		fftIn = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsOpC.class,
			getFFTInput(), RandomAccessibleInterval.class);

		fftKernel = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsOpC.class,
			getFFTKernel(), RandomAccessibleInterval.class);

		ifft = (UnaryComputerOp) Computers.unary(ops(), IFFTMethodsOpC.class,
			RandomAccessibleInterval.class, getFFTKernel());

	}

	/**
	 * Perform convolution by multiplying the FFTs in the frequency domain
	 */
	@Override
	public void compute(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{
		// create FFT input memory if needed
		if (getFFTInput() == null) {
			setFFTInput(getCreateOp().calculate(in));
		}

		// create FFT kernel memory if needed
		if (getFFTKernel() == null) {
			setFFTKernel(getCreateOp().calculate(in));
		}
		
		// perform input FFT if needed
		if (getPerformInputFFT()) {
			fftIn.compute(in, getFFTInput());
		}

		// perform kernel FFT if needed
		if (getPerformKernelFFT()) {
			fftKernel.compute(kernel, getFFTKernel());
		}

		// perform the operation in frequency domain (ie multiplication for
		// convolution, complex conjugate multiplication for correlation,
		// Wiener Filter, etc.)
		frequencyOp.compute(getFFTInput(), getFFTKernel(), getFFTInput());

		// perform inverse fft
		ifft.compute(getFFTInput(), out);
		// linearFilter.compute(in, kernel, out);
	}
}
