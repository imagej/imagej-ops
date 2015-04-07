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

package net.imagej.ops.fft.size;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.FFTSize;
import net.imglib2.FinalDimensions;

import net.imglib2.algorithm.fft2.FFTMethods;

/**
 * Op to calculate JTransform FFT sizes.
 * 
 * @author Brian Northan
 */
@Plugin(type = FFTSize.class, name = FFTSize.NAME)
public class ComputeFFTSize extends AbstractFFTSize {

	public void run() {
		FinalDimensions dim = new FinalDimensions(inputSize);

		if (fast && forward) {

			FFTMethods.dimensionsRealToComplexFast(dim, paddedSize, fftSize);

		}
		else if (!fast && forward) {
			FFTMethods.dimensionsRealToComplexSmall(dim, paddedSize, fftSize);

		}
		if (fast && !forward) {

			FFTMethods.dimensionsComplexToRealFast(dim, paddedSize, fftSize);

		}
		else if (!fast && !forward) {

			FFTMethods.dimensionsComplexToRealSmall(dim, paddedSize, fftSize);

		}
	}

}
