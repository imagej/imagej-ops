/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Abstract class for linear filters that operate on RAIs
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Ops.Filter.LinearFilter.class, priority = Priority.LOW_PRIORITY)
public class DefaultLinearFFTFilterC<I, O, K, C> extends
	AbstractFFTFilterC<I, O, K, C> implements Ops.Filter.LinearFilter
{
	
	// TODO: some of these should not be parameters

	// FFT Op for input
	@Parameter
	private UnaryComputerOp<I, C> fftInputOp;

	// FFT Op for kernel
	@Parameter
	private UnaryComputerOp<K, C> fftKernelOp;

	// Op that performs filter in frequency domain
	@Parameter
	private BinaryComputerOp<C, C, C> frequencyOp;

	// Inverse fft op
	@Parameter
	private UnaryComputerOp<C, O> ifftOp;

	@Override
	public void compute2(I in, K kernel, O out) {

		// perform input FFT if needed
		if (getPerformInputFFT()) {
			fftInputOp.compute1(in, getFFTInput());
		}

		// perform kernel FFT if needed
		if (getPerformKernelFFT()) {
			fftKernelOp.compute1(kernel, getFFTKernel());
		}

		// perform the operation in frequency domain (ie multiplication for
		// convolution, complex conjugate multiplication for correlation,
		// Wiener Filter, etc.)
		frequencyOp.compute2(getFFTInput(), getFFTKernel(), getFFTInput());

		// perform inverse fft
		ifftOp.compute1(getFFTInput(), out);
	}
}
