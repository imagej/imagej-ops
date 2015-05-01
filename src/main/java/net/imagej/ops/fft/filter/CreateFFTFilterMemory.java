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

package net.imagej.ops.fft.filter;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imglib2.FinalDimensions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This class is used to extend the image and kernel for an FFT filter and
 * create the corresponding (@Link Img) buffers to store the FFTs
 * 
 * @author bnorthan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Op.class)
public class CreateFFTFilterMemory<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	implements Op
{

	@Parameter
	protected OpService ops;

	@Parameter
	protected RandomAccessibleInterval<I> input;

	@Parameter
	protected RandomAccessibleInterval<K> kernel;

	@Parameter(required = false)
	protected long[] borderSize = null;

	/**
	 * generates the out of bounds strategy for the extended area
	 */
	@Parameter(required = false)
	protected OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput;

	/**
	 * generates the out of bounds strategy for the extended area
	 */
	@Parameter(required = false)
	protected OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel;

	@Parameter(required = false)
	protected ComplexType<C> fftType;

	@Parameter(required = false)
	protected ImgFactory<C> fftFactory;

	Img<C> fftImg;

	Img<C> fftKernel;

	RandomAccessibleInterval<I> raiExtendedInput;

	RandomAccessibleInterval<K> raiExtendedKernel;

	protected Interval imgConvolutionInterval;

	@Override
	public void run() {

		createFFTMemory();
	}

	/**
	 * Extend input and kernel and create the FFT (@link Img} buffers.
	 */
	protected void createFFTMemory() {

		if (obfInput == null) {
			obfInput =
				new OutOfBoundsConstantValueFactory<I, RandomAccessibleInterval<I>>(
					Util.getTypeFromInterval(input).createVariable());
		}

		if ((obfKernel == null) && (kernel != null)) {
			obfKernel =
				new OutOfBoundsConstantValueFactory<K, RandomAccessibleInterval<K>>(
					Util.getTypeFromInterval(kernel).createVariable());
		}

		// //////////////////////////////////////////////////////////////////////
		// the first few lines of code here calculate the extended sizes and
		// corresponding intervals of the input image and the kernel.
		// Extension is needed to avoid edge artifacts
		// Also, since the FFT is faster at certain sizes, we want to extend to
		// an
		// efficient FFT size
		// //////////////////////////////////////////////////////////////////////
		final int numDimensions = input.numDimensions();

		// 1. Calculate desired extended size of the image
		final long[] newDimensions = new long[numDimensions];

		if (borderSize == null) {
			// if no borderSize was passed in extend based on kernel size
			for (int d = 0; d < numDimensions; ++d) {
				newDimensions[d] =
					(int) input.dimension(d) + (int) kernel.dimension(d) - 1;
			}

		}
		else {
			// if borderSize was passed in
			for (int d = 0; d < numDimensions; ++d) {

				newDimensions[d] =
					Math.max(kernel.dimension(d) + 2 * borderSize[d], input.dimension(d) +
						2 * borderSize[d]);
			}
		}

		// 2. compute the size of the complex-valued output and the required
		// padding based on the prior extended input image
		// (The image size is recalculated again so that it is a "fast" fft
		// size. FFTs are much faster for certain sizes.)

		final long[] paddedDimensions = new long[numDimensions];
		final long[] fftDimensions = new long[numDimensions];

		FFTMethods.dimensionsRealToComplexFast(FinalDimensions.wrap(newDimensions),
			paddedDimensions, fftDimensions);

		// 3. Using the size calculated above compute the new interval for
		// the input image
		imgConvolutionInterval =
			FFTMethods.paddingIntervalCentered(input, FinalDimensions
				.wrap(paddedDimensions));

		// 4. compute the new interval for the kernel image
		final Interval kernelConvolutionInterval =
			FFTMethods.paddingIntervalCentered(kernel, FinalDimensions
				.wrap(paddedDimensions));

		// compute where to place the final Interval for the kernel so that the
		// coordinate in the center
		// of the kernel is at position (0,0).
		final long[] min = new long[numDimensions];
		final long[] max = new long[numDimensions];

		for (int d = 0; d < numDimensions; ++d) {
			min[d] = kernel.min(d) + kernel.dimension(d) / 2;
			max[d] = min[d] + kernelConvolutionInterval.dimension(d) - 1;
		}

		// assemble the kernel (size of the input + extended periodic +
		// top left at center of input kernel)
		raiExtendedKernel =
			Views.interval(Views.extendPeriodic(Views.interval(Views.extendValue(
				kernel, Util.getTypeFromInterval(kernel).createVariable()),
				kernelConvolutionInterval)), new FinalInterval(min, max));

		// assemble the extended view of the image
		raiExtendedInput =
			Views.interval(Views.extend(input, obfInput), imgConvolutionInterval);

		if (fftType == null) {
			fftType = (ComplexType) (new ComplexFloatType().createVariable());
		}

		if (fftFactory == null) {
			try {
				fftFactory = new PlanarImgFactory().imgFactory(fftType);
			}
			catch (IncompatibleTypeException e) {
				// TODO handle exception??
			}
		}

		fftImg = ((ImgFactory) fftFactory).create(fftDimensions, fftType);

		fftKernel = ((ImgFactory) fftFactory).create(fftDimensions, fftType);

	}

	public Img<C> getFFTImg() {
		return fftImg;
	}

	public Img<C> getFFTKernel() {
		return fftKernel;
	}

	public RandomAccessibleInterval<I> getRAIExtendedInput() {
		return raiExtendedInput;
	}

	public RandomAccessibleInterval<K> getRAIExtendedKernel() {
		return raiExtendedKernel;
	}

	public Interval getImgConvolutionInterval() {
		return imgConvolutionInterval;
	}

}
