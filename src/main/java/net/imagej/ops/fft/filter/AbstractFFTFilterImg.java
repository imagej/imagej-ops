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

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for FFT based filters that operate on Img.
 * 
 * @author bnorthan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractFFTFilterImg<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFilterImg<I, O, K>
{

	/**
	 * FFT type
	 */
	@Parameter(required = false)
	protected ComplexType<C> fftType;

	/**
	 * Factory to create ffts Imgs
	 */
	@Parameter(required = false)
	protected ImgFactory<C> fftFactory;

	/**
	 * 
	 */
	@Override
	public Img<O> safeCompute(Img<I> input, Img<O> output) {

		// run the op that extends the input and kernel and creates the Imgs
		// required for the fft algorithm
		CreateFFTFilterMemory<I, O, K, C> createMemory =
			ops.op(CreateFFTFilterMemory.class, input, kernel, borderSize);

		createMemory.run();

		// run the filter, pass in the memory created above
		runFilter(createMemory.getRAIExtendedInput(), createMemory
			.getRAIExtendedKernel(), createMemory.getFFTImg(), createMemory
			.getFFTKernel(), output, createMemory.getImgConvolutionInterval());

		return output;
	}

	/**
	 * This function is called after the rais and ffts are set up and implements a
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
		RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftImg,
		Img<C> fftKernel, Img<O> output, Interval imgConvolutionInterval);

}
