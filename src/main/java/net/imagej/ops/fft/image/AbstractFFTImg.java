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

package net.imagej.ops.fft.image;

import net.imagej.ops.Ops.FFT;
import net.imagej.ops.fft.AbstractFFTIterable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;

import org.scijava.plugin.Parameter;

/**
 * Abstract superclass for forward FFT implementations that operate on Img<T>.
 * 
 * @author Brian Northan
 */
public abstract class AbstractFFTImg<T, I extends Img<T>, C, O extends Img<C>>
	extends AbstractFFTIterable<T, C, I, O> implements FFT
{

	/**
	 * size of border to apply in each dimension
	 */
	@Parameter(required = false)
	long[] borderSize = null;

	/**
	 * set to true to compute the FFT as fast as possible. The input will be
	 * extended to the next fast FFT size. If false the input will be computed
	 * using the original input dimensions (if possible) If the input dimensions
	 * are not supported by the underlying FFT implementation the input will be
	 * extended to the nearest size that is supported.
	 */
	@Parameter(required = false)
	Boolean fast = true;

	/**
	 * generates the out of bounds strategy for the extended area
	 */
	@Parameter(required = false)
	protected OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf;

	protected long[] paddedSize;
	protected long[] fftSize;

	/**
	 * create the output based on the input. If fast=true the size is determined
	 * such that the underlying FFT implementation will run as fast as possible.
	 * If fast=false the size is determined such that the underlying FFT
	 * implementation will use the smallest amount of memory possible.
	 */
	@Override
	public O createOutput(I input) {

		long[] inputSize = new long[input.numDimensions()];

		for (int d = 0; d < input.numDimensions(); d++) {
			inputSize[d] = input.dimension(d);

			if (borderSize != null) {
				inputSize[d] += borderSize[d];
			}
		}

		if (fast) {
			computeFFTFastSize(inputSize);
		}
		else {
			computeFFTSmallSize(inputSize);
		}

		return createFFTImg(input.factory(), fftSize);

	}

	/**
	 * returns fastest FFT size possible for the input size
	 * 
	 * @param inputSize
	 * @return
	 */
	protected abstract void computeFFTFastSize(long[] inputSize);

	/**
	 * returns smallest FFT size possible for the input size
	 * 
	 * @param inputSize
	 */
	protected abstract void computeFFTSmallSize(long[] inputSize);

	/**
	 * creates the output Img
	 * 
	 * @param factory
	 * @param size
	 * @return
	 */
	protected abstract O createFFTImg(ImgFactory<T> factory, long[] size);

}
