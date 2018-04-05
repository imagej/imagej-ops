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
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Inverse FFT computer that operates on an RAI and wraps FFTMethods. The input
 * and output size must conform to supported FFT size. Use
 * {@link net.imagej.ops.filter.fftSize.ComputeFFTSize} to calculate the
 * supported FFT size.
 * 
 * @author Brian Northan
 * @param <C>
 * @param <T>
 */
@Plugin(type = Ops.Filter.IFFT.class)
public class IFFTMethodsOpC<C extends ComplexType<C>, T extends RealType<T>>
	extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<T>>
	implements Ops.Filter.IFFT, Contingent
{

	@Parameter
	ThreadService ts;

	private UnaryFunctionOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> copyOp;

	@Override
	public void initialize() {
		super.initialize();
		copyOp = RAIs.function(ops(), Ops.Copy.RAI.class, in());
	}

	/**
	 * Compute an ND inverse FFT
	 */
	@Override
	public void compute(final RandomAccessibleInterval<C> input,
		final RandomAccessibleInterval<T> output)
	{
		final RandomAccessibleInterval<C> temp = copyOp.calculate(input);

		for (int d = input.numDimensions() - 1; d > 0; d--)
			FFTMethods.complexToComplex(temp, d, false, true, ts
				.getExecutorService());

		FFTMethods.complexToReal(temp, output, FFTMethods.unpaddingIntervalCentered(
			temp, output), 0, true, ts.getExecutorService());
	}

	/**
	 * Make sure that the input size conforms to a supported FFT size.
	 */
	@Override
	public boolean conforms() {

		long[] paddedDimensions = new long[in().numDimensions()];
		long[] realDimensions = new long[in().numDimensions()];

		boolean fastSizeConforms = false;

		FFTMethods.dimensionsComplexToRealFast(in(), paddedDimensions,
			realDimensions);

		if (FFTMethods.dimensionsEqual(in(), paddedDimensions) == true) {
			fastSizeConforms = true;
		}

		boolean smallSizeConforms = false;

		FFTMethods.dimensionsComplexToRealSmall(in(), paddedDimensions,
			realDimensions);

		if ((FFTMethods.dimensionsEqual(in(), paddedDimensions) == true)) {
			smallSizeConforms = true;
		}

		return fastSizeConforms || smallSizeConforms;

	}

}
