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

import net.imagej.ops.filter.fft.CreateOutputFFTMethods;
import net.imagej.ops.filter.pad.PadInputFFTMethods;
import net.imagej.ops.filter.pad.PadShiftKernelFFTMethods;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for binary filter that performs operations using an image and
 * kernel in the frequency domain.
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractFFTFilterF<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFilterF<I, O, K>
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
	private BinaryFunctionOp<RandomAccessibleInterval<I>, Dimensions, RandomAccessibleInterval<I>> padOp;

	/**
	 * Op used to pad the kernel
	 */
	private BinaryFunctionOp<RandomAccessibleInterval<K>, Dimensions, RandomAccessibleInterval<K>> padKernelOp;

	/**
	 * Op used to create the complex FFTs
	 */
	private UnaryFunctionOp<Dimensions, RandomAccessibleInterval<C>> createOp;

	/**
	 * Filter Op
	 */
	private BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> filter;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		// if fftType, and/or fftFactory do not exist, create them using defaults
		if (fftType == null) {
			fftType = (ComplexType) (new ComplexFloatType().createVariable());
		}

		if (this.getOBFInput() == null) {
			setOBFInput(new OutOfBoundsMirrorFactory<>(Boundary.SINGLE));
		}

		/**
		 * Op used to pad the input
		 */
		padOp = (BinaryFunctionOp) Functions.binary(ops(), PadInputFFTMethods.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			Dimensions.class, true, getOBFInput());

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
	}

	/**
	 * compute output by extending the input(s) and running the filter
	 */
	@Override
	public RandomAccessibleInterval<O> compute2(
		final RandomAccessibleInterval<I> input,
		final RandomAccessibleInterval<K> kernel)
	{

		RandomAccessibleInterval<O> output = createOutput(input, kernel);

		final int numDimensions = input.numDimensions();

		// 1. Calculate desired extended size of the image

		final long[] paddedSize = new long[numDimensions];

		if (getBorderSize() == null) {
			// if no border size was passed in, then extend based on kernel size
			for (int d = 0; d < numDimensions; ++d) {
				paddedSize[d] = (int) input.dimension(d) + (int) kernel.dimension(d) -
					1;
			}

		}
		else {
			// if getBorderSize() was passed in
			for (int d = 0; d < numDimensions; ++d) {

				paddedSize[d] = Math.max(kernel.dimension(d) + 2 * getBorderSize()[d],
					input.dimension(d) + 2 * getBorderSize()[d]);
			}
		}

		RandomAccessibleInterval<I> paddedInput = padOp.compute2(input,
			new FinalDimensions(paddedSize));

		RandomAccessibleInterval<K> paddedKernel = padKernelOp.compute2(kernel,
			new FinalDimensions(paddedSize));

		RandomAccessibleInterval<C> fftImage = createOp.compute1(
			new FinalDimensions(paddedSize));

		RandomAccessibleInterval<C> fftKernel = createOp.compute1(
			new FinalDimensions(paddedSize));

		// TODO: in this case it is difficult to match the filter op in the
		// 'initialize' as we don't know the size yet, thus we can't create memory for the FFTs
		filter = createFilter(paddedInput, paddedKernel, fftImage, fftKernel,
			output, paddedInput);

		filter.compute2(paddedInput, paddedKernel, output);

		return output;

	}

	/**
	 * This function is called after the RAIs and FFTs are set up and create the
	 * frequency filter.
	 * 
	 * @param raiExtendedInput
	 * @param raiExtendedKernel
	 * @param fftImg
	 * @param fftKernel
	 * @param output
	 * @param imgConvolutionInterval
	 */
	abstract public
		BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>>
		createFilter(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel,
			RandomAccessibleInterval<C> fftImg, RandomAccessibleInterval<C> fftKernel,
			RandomAccessibleInterval<O> output, Interval imgConvolutionInterval);

}
