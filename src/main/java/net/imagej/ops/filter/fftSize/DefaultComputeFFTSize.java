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

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op that calculates FFT fast sizes according to the following logic.
 * 
 * If powerOfTwo=true compute next power of 2
 * 
 * If powerOfTwo=false compute next smooth size
 * 
 * @author Brian Northan
 */
@Plugin(type = Ops.Filter.FFTSize.class)
public class DefaultComputeFFTSize extends AbstractUnaryFunctionOp<Dimensions, long[][]> implements Ops.Filter.FFTSize {

	@Parameter
	private boolean powerOfTwo;

	@Override
	public long[][] calculate(Dimensions inputDimensions) {

		long[][] size = new long[2][];
		size[0] = new long[inputDimensions.numDimensions()];
		size[1] = new long[inputDimensions.numDimensions()];

		for (int i = 0; i < inputDimensions.numDimensions(); i++) {
			// real size
			if (powerOfTwo) {
				size[0][i] = NextPowerOfTwo.nextPowerOfTwo(inputDimensions.dimension(i));
			} else {
				size[0][i] = (long) NextSmoothNumber.nextSmooth((int) inputDimensions.dimension(i));
			}
			// complex size
			if (i == 0) {
				size[1][i] = (size[0][i] / 2 + 1);
			} else {
				size[1][i] = size[0][i];
			}
		}

		return size;

	}

}
