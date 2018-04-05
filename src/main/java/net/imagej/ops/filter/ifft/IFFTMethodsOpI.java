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

package net.imagej.ops.filter.ifft;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.inplace.AbstractUnaryInplaceOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Inverse FFT inplace operator -- complex to complex only, output size must
 * conform to supported FFT size. Use
 * {@link net.imagej.ops.filter.fftSize.ComputeFFTSize} to calculate the
 * supported FFT size.
 * 
 * @author Brian Northan
 */
@Plugin(type = Ops.Filter.IFFT.class)
public class IFFTMethodsOpI<C extends ComplexType<C>> extends
	AbstractUnaryInplaceOp<RandomAccessibleInterval<C>> implements
	Ops.Filter.IFFT, Contingent
{

	@Parameter
	ThreadService ts;

	/**
	 * Compute an ND inverse FFT
	 */
	@Override
	public void mutate(final RandomAccessibleInterval<C> inout) {
		for (int d = inout.numDimensions() - 1; d >= 0; d--)
			FFTMethods.complexToComplex(inout, d, false, true, ts
				.getExecutorService());
	}

	/**
	 * Make sure that the input size conforms to a supported FFT size.
	 */
	@Override
	public boolean conforms() {

		long[] paddedDimensions = new long[in().numDimensions()];

		boolean fastSizeConforms = false;

		FFTMethods.dimensionsComplexToComplexFast(in(), paddedDimensions);

		if (FFTMethods.dimensionsEqual(in(), paddedDimensions) == true) {
			fastSizeConforms = true;
		}

		boolean smallSizeConforms = false;

		FFTMethods.dimensionsComplexToComplexSmall(in(), paddedDimensions);

		if ((FFTMethods.dimensionsEqual(in(), paddedDimensions) == true)) {
			smallSizeConforms = true;
		}

		return fastSizeConforms || smallSizeConforms;

	}

}
