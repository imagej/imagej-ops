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

import net.imagej.ops.filter.fft.CreateOutputFFTMethods;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for FFT based filter computers
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O> gene
 * @param <K>
 * @param <C>
 */
public abstract class AbstractFFTFilterC<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends
	AbstractBinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>>
{

	/**
	 * Buffer to be used to store FFTs for input. Size of fftInput must correspond
	 * to the fft size of raiExtendedInput
	 */
	@Parameter(required = false)
	private RandomAccessibleInterval<C> fftInput;

	/**
	 * Buffer to be used to store FFTs for kernel. Size of fftKernel must
	 * correspond to the fft size of raiExtendedKernel
	 */
	@Parameter(required = false)
	private RandomAccessibleInterval<C> fftKernel;

	/**
	 * boolean indicating that the input FFT has already been calculated
	 */
	@Parameter(required = false)
	private boolean performInputFFT = true;

	/**
	 * boolean indicating that the kernel FFT has already been calculated
	 */
	@Parameter(required = false)
	private boolean performKernelFFT = true;

	/**
	 * FFT type
	 */
	private ComplexType<C> fftType;

	/**
	 * Op used to create the complex FFTs
	 */
	private UnaryFunctionOp<Dimensions, RandomAccessibleInterval<C>> createOp;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		if (fftType == null) {
			fftType = (ComplexType<C>) ops().create().nativeType(
				ComplexFloatType.class);
		}

		/**
		 * Op used to create the complex FFTs
		 */
		createOp = (UnaryFunctionOp) Functions.unary(ops(),
			CreateOutputFFTMethods.class, RandomAccessibleInterval.class,
			Dimensions.class, fftType, true);
	}

	protected RandomAccessibleInterval<C> getFFTInput() {
		return fftInput;
	}

	public void setFFTInput(RandomAccessibleInterval<C> fftInput) {
		this.fftInput = fftInput;
	}

	protected RandomAccessibleInterval<C> getFFTKernel() {
		return fftKernel;
	}

	public void setFFTKernel(RandomAccessibleInterval<C> fftKernel) {
		this.fftKernel = fftKernel;
	}

	protected boolean getPerformInputFFT() {
		return performInputFFT;
	}

	protected boolean getPerformKernelFFT() {
		return performKernelFFT;
	}

	public UnaryFunctionOp<Dimensions, RandomAccessibleInterval<C>>
		getCreateOp()
	{
		return createOp;
	}

}
