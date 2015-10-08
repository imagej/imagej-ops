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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Forward FFT computer that operates on an RAI and wraps FFTMethods.
 * 
 * @author Brian Northan
 * @param <T>
 * @param <C>
 */
@Plugin(type = Ops.Filter.FFT.class, priority = Priority.HIGH_PRIORITY)
public class FFTComputerOp<T extends RealType<T>, C extends ComplexType<C>>
	extends
	AbstractComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<C>>
	implements Ops.Filter.FFT, Contingent
{

	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<C> output)
	{

		// TODO: proper use of Executor service
		final int numThreads = Runtime.getRuntime().availableProcessors();
		final ExecutorService service = Executors.newFixedThreadPool(numThreads);

		// perform a real to complex FFT in the first dimension
		FFTMethods.realToComplex(input, output, 0, false, service);

		// loop and perform complex to complex FFT in the remaining dimensions
		for (int d = 1; d < input.numDimensions(); d++)
			FFTMethods.complexToComplex(output, d, true, false, service);
	}
	
	/**
	 * Make sure that the input and output size conforms to a supported FFT size.
	 */
	@Override
	public boolean conforms() {

		long[] paddedDimensions = new long[in().numDimensions()];
		long[] fftDimensions = new long[in().numDimensions()];

		boolean fastSizeConforms = false;

		FFTMethods.dimensionsRealToComplexFast(in(), paddedDimensions,
			fftDimensions);

		if ((FFTMethods.dimensionsEqual(in(), paddedDimensions) == true) &&
			(FFTMethods.dimensionsEqual(out(), fftDimensions) == true))
		{
			fastSizeConforms = true;
		}

		boolean smallSizeConforms = false;

		FFTMethods.dimensionsRealToComplexSmall(in(), paddedDimensions,
			fftDimensions);

		if ((FFTMethods.dimensionsEqual(in(), paddedDimensions) == true) &&
			(FFTMethods.dimensionsEqual(out(), fftDimensions) == true))
		{
			smallSizeConforms = true;
		}

		return fastSizeConforms || smallSizeConforms;

	}

}
