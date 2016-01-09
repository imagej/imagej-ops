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

import net.imagej.ops.Op;
import net.imagej.ops.special.BinaryHybridOp;
import net.imagej.ops.special.UnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Abstract class for linear filters that operate on RAIs
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Op.class, priority = Priority.LOW_PRIORITY)
public class DefaultLinearFFTFilter<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFFTFilterRAI<I, O, K, C>
{

	// FFT Op for input
	@Parameter
	UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<C>> fftInputOp;

	// FFT Op for kernel
	@Parameter
	UnaryComputerOp<RandomAccessibleInterval<K>, RandomAccessibleInterval<C>> fftKernelOp;

	// Op that performs filter in frequency domain
	@Parameter
	BinaryHybridOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> frequencyOp;
	
	// TODO: inverse fft op as parameter?
	@Parameter
	UnaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<O>> ifftOp;

	@Override
	public void compute1(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<O> out)
	{

		// perform input FFT if needed
		if (getPerformInputFFT()) {
			fftInputOp.compute1(in(), getFFTInput());
		}

		// perform kernel FFT if needed
		if (getPerformKernelFFT()) {
			fftKernelOp.compute1(getRAIExtendedKernel(), getFFTKernel());
		}

		// perform the operation in frequency domain (ie multiplication for
		// convolution, complex conjugate multiplication for correlation,
		// Wiener Filter, etc.)
		frequencyOp.compute2(getFFTInput(), getFFTKernel(), getFFTInput());

		// perform inverse fft
		ifftOp.compute1(getFFTInput(),out());
	}
}
