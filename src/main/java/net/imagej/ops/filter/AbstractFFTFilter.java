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

import org.scijava.plugin.Parameter;

import net.imagej.ops.filter.fft.CreateOutputFFTMethods;
import net.imagej.ops.filter.fft.PadInputFFTMethods;
import net.imagej.ops.filter.fft.PadShiftKernelFFTMethods;
import net.imagej.ops.special.BinaryFunctionOp;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

/**
 * Abstract class for FFT based filters that operate on Img.
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractFFTFilter<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFilter<I, O, K>
{

	/**
	 * FFT type
	 */
	@Parameter(required = false)
	private ComplexType<C> fftType;

	/**
	 * Factory to create FFT Imgs
	 */
	@Parameter(required = false)
	private ImgFactory<C> fftFactory;

	/**
	 * Op used to pad the input
	 */
	@Parameter(required = false)
	private BinaryFunctionOp<RandomAccessibleInterval<I>, Dimensions, RandomAccessibleInterval<I>> padOp;

	/**
	 * Op used to pad the kernel
	 */
	@Parameter(required = false)
	private BinaryFunctionOp<RandomAccessibleInterval<K>, Dimensions, RandomAccessibleInterval<K>> padKernelOp;

	/**
	 * Op used to create the complex FFTs
	 */
	@Parameter(required = false)
	private UnaryFunctionOp<Dimensions, RandomAccessibleInterval<C>> createOp;

	/**
	 * compute output by extending the input(s) and running the filter
	 */
	@Override
	public RandomAccessibleInterval<O> compute1(
		final RandomAccessibleInterval<I> input)
	{

		RandomAccessibleInterval<O> output = createOutput(input);

		final int numDimensions = input.numDimensions();

		// 1. Calculate desired extended size of the image

		final long[] paddedSize = new long[numDimensions];

		if (getBorderSize() == null) {
			// if no getBorderSize() was passed in, then extend based on kernel size
			for (int d = 0; d < numDimensions; ++d) {
				paddedSize[d] = (int) input.dimension(d) + (int) getKernel().dimension(
					d) - 1;
			}

		}
		else {
			// if getBorderSize() was passed in
			for (int d = 0; d < numDimensions; ++d) {

				paddedSize[d] = Math.max(getKernel().dimension(d) + 2 *
					getBorderSize()[d], input.dimension(d) + 2 * getBorderSize()[d]);
			}
		}

		// if fftType, and/or fftFactory do not exist, create them using defaults
		if (fftType == null) {
			fftType = (ComplexType) (new ComplexFloatType().createVariable());
		}

		/**
		 * Op used to pad the input
		 */
		padOp = (BinaryFunctionOp) Functions.binary(ops(), PadInputFFTMethods.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			Dimensions.class, true);

		/**
		 * Op used to pad the kernel
		 */
		padKernelOp = (BinaryFunctionOp) Functions.binary(ops(),
			PadShiftKernelFFTMethods.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, Dimensions.class, true);

		/**
		 * Op used to create the complex FFTs
		 */
		createOp = (UnaryFunctionOp) Functions.unary(ops(),
			CreateOutputFFTMethods.class, RandomAccessibleInterval.class,
			Dimensions.class, fftType, true);

		RandomAccessibleInterval<I> paddedInput = padOp.compute2(input,
			new FinalDimensions(paddedSize));

		RandomAccessibleInterval<K> paddedKernel = padKernelOp.compute2(getKernel(),
			new FinalDimensions(paddedSize));

		RandomAccessibleInterval<C> fftImage = createOp.compute1(
			new FinalDimensions(paddedSize));

		RandomAccessibleInterval<C> fftKernel = createOp.compute1(
			new FinalDimensions(paddedSize));

		// TODO: this should be an op??
		runFilter(paddedInput, paddedKernel, fftImage, fftKernel, output,
			paddedInput);

		return output;

	}

	/**
	 * This function is called after the RAIs and FFTs are set up and implements a
	 * frequency filter.
	 * 
	 * @param raiExtendedInput
	 * @param raiExtendedKernel
	 * @param fftImg
	 * @param fftKernel
	 * @param output
	 * @param imgConvolutionInterval
	 */
	abstract public void runFilter(RandomAccessibleInterval<I> raiExtendedInput,
		RandomAccessibleInterval<K> raiExtendedKernel,
		RandomAccessibleInterval<C> fftImg, RandomAccessibleInterval<C> fftKernel,
		RandomAccessibleInterval<O> output, Interval imgConvolutionInterval);

}
