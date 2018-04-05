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

package net.imagej.ops.filter.fftSize;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.algorithm.fft2.FFTMethods;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op that calculates FFT sizes.
 * 
 * @author Brian Northan
 */
@Plugin(type = Ops.Filter.FFTSize.class)
public class ComputeFFTMethodsSize extends
	AbstractUnaryFunctionOp<Dimensions, long[][]> implements Ops.Filter.FFTSize
{

	@Parameter
	private boolean forward;

	@Parameter
	private boolean fast;

	@Override
	public long[][] calculate(Dimensions inputDimensions) {

		long[][] size = new long[2][];
		size[0] = new long[inputDimensions.numDimensions()];
		size[1] = new long[inputDimensions.numDimensions()];

		if (fast && forward) {

			FFTMethods.dimensionsRealToComplexFast(inputDimensions, size[0], size[1]);

		}
		else if (!fast && forward) {
			FFTMethods.dimensionsRealToComplexSmall(inputDimensions, size[0],
				size[1]);

		}
		if (fast && !forward) {

			FFTMethods.dimensionsComplexToRealFast(inputDimensions, size[0], size[1]);

		}
		else if (!fast && !forward) {

			FFTMethods.dimensionsComplexToRealSmall(inputDimensions, size[0],
				size[1]);
		}

		return size;

	}

}
