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

package net.imagej.ops.filter.fft;

import net.imagej.ops.Ops;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Forward FFT that operates on Img
 * 
 * @author Brian Northan
 * @param <T>
 * @param <I>
 */
@Plugin(type = Ops.Filter.FFT.class, priority = Priority.HIGH_PRIORITY)
public class FFTImg<T extends RealType<T>, I extends Img<T>> extends
	AbstractFFTImg<T, I, ComplexFloatType, Img<ComplexFloatType>>
{

	private long[] paddedSize;

	private long[] fftSize;

	@Override
	protected void computeFFTFastSize(long[] inputSize) {

		paddedSize = new long[inputSize.length];
		fftSize = new long[inputSize.length];

		ops().filter().fftSize(inputSize, paddedSize, fftSize, true, true);

	}

	@Override
	protected void computeFFTSmallSize(long[] inputSize) {

		paddedSize = new long[inputSize.length];
		fftSize = new long[inputSize.length];

		ops().filter().fftSize(inputSize, paddedSize, fftSize, true, false);

	}

	@Override
	protected Img<ComplexFloatType> createFFTImg(ImgFactory<T> factory) {

		try {
			return factory.imgFactory(new ComplexFloatType()).create(fftSize,
				new ComplexFloatType());
		}
		// TODO: error handling?
		catch (IncompatibleTypeException e) {
			return null;
		}
	}

	@Override
	public void compute(final I input, final Img<ComplexFloatType> output) {
		ops().filter().fft(output, input, getOBF(), paddedSize);
	}
}
