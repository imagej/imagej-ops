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

import net.imagej.ops.AbstractComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for FFT based filters that operate on RAI
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractFFTFilterRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
{

	/**
	 * kernel rai. Needs to be the same size as the input rai
	 */
	@Parameter
	private RandomAccessibleInterval<K> raiExtendedKernel;

	/**
	 * Buffer to be used to store FFTs for input. Size of fftInput must correspond to
	 * the fft size of raiExtendedInput
	 */
	@Parameter
	private RandomAccessibleInterval<C> fftInput;

	/**
	 * Buffer to be used to store FFTs for kernel. Size of fftKernel must correspond
	 * to the fft size of raiExtendedKernel
	 */
	@Parameter
	private RandomAccessibleInterval<C> fftKernel;

	/**
	 * Boolean indicating that the input FFT has already been calculated
	 */
	@Parameter(required = false)
	private boolean performInputFFT = true;

	/**
	 * Boolean indicating that the kernel FFT has already been calculated 
	 */
	@Parameter(required = false)
	private boolean performKernelFFT = true;


	protected RandomAccessibleInterval<K> getRAIExtendedKernel() {
		return raiExtendedKernel;
	}

	protected RandomAccessibleInterval<C> getFFTInput() {
		return fftInput;
	}

	protected RandomAccessibleInterval<C> getFFTKernel() {
		return fftKernel;
	}

	protected boolean getPerformInputFFT() {
		return performInputFFT;
	}

	protected boolean getPerformKernelFFT() {
		return performKernelFFT;
	}

}
