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

package net.imagej.ops.filter.fft;

import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.algorithm.fft2.FFTMethods;

/**
 * Utility class that interacts with FFTMethods
 *
 * @author bnorthan
 */
public class FFTMethodsUtility {

	/**
	 * Calculates padding size and complex FFT size for real to complex FFT
	 * 
	 * @param fast if true calculate size for fast FFT
	 * @param inputDimensions original real dimensions
	 * @param paddedDimensions padded real dimensions
	 * @param fftDimensions complex FFT dimensions
	 */
	public static void dimensionsRealToComplex(final boolean fast,
		final Dimensions inputDimensions, final long[] paddedDimensions,
		final long[] fftDimensions)
	{
		if (fast) {
			FFTMethods.dimensionsRealToComplexFast(inputDimensions, paddedDimensions,
				fftDimensions);
		}
		else {
			FFTMethods.dimensionsRealToComplexSmall(inputDimensions, paddedDimensions,
				fftDimensions);
		}
	}

	/**
	 * Calculates padding size size for real to complex FFT
	 * 
	 * @param fast if true calculate size for fast FFT
	 * @param inputDimensions original real dimensions
	 * @return padded real dimensions
	 */
	public static Dimensions getPaddedInputDimensionsRealToComplex(
		final boolean fast, final Dimensions inputDimensions)
	{
		final long[] paddedSize = new long[inputDimensions.numDimensions()];
		final long[] fftSize = new long[inputDimensions.numDimensions()];

		dimensionsRealToComplex(fast, inputDimensions, paddedSize, fftSize);

		return new FinalDimensions(paddedSize);

	}

	/**
	 * Calculates complex FFT size for real to complex FFT
	 * 
	 * @param fast if true calculate size for fast FFT
	 * @param inputDimensions original real dimensions
	 * @return complex FFT dimensions
	 */
	public static Dimensions getFFTDimensionsRealToComplex(final boolean fast,
		final Dimensions inputDimensions)
	{
		final long[] paddedSize = new long[inputDimensions.numDimensions()];
		final long[] fftSize = new long[inputDimensions.numDimensions()];

		dimensionsRealToComplex(fast, inputDimensions, paddedSize, fftSize);

		return new FinalDimensions(fftSize);

	}

}
