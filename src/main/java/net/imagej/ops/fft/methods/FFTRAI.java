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

package net.imagej.ops.fft.methods;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.Ops.FFT;

import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.imglib2.algorithm.fft2.FFTMethods;

/**
 * Forward FFT that operates on an RAI and wraps FFTMethods.
 * 
 * @author Brian Northan
 * @param <T>
 * @param <C>
 */
@Plugin(type = FFT.class, name = FFT.NAME, priority = Priority.HIGH_PRIORITY)
public class FFTRAI<T extends RealType<T>, C extends ComplexType<C>>
	extends
	AbstractStrictFunction<RandomAccessibleInterval<T>, RandomAccessibleInterval<C>>
{

	/**
	 * generates the out of bounds strategy for the extended area
	 */
	@Parameter(required = false)
	protected OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf;

	@Parameter(required = false)
	protected long[] paddedSize;

	public RandomAccessibleInterval<C> compute(RandomAccessibleInterval<T> input,
		RandomAccessibleInterval<C> output)
	{

		RandomAccessibleInterval<T> inputRAI;

		if (paddedSize == null) {

			paddedSize = new long[input.numDimensions()];

			for (int d = 0; d < input.numDimensions(); d++) {
				paddedSize[d] = input.dimension(d);
			}
		}

		// Extend input to padded size using a View
		if (!FFTMethods.dimensionsEqual(input, paddedSize)) {

			if (obf == null) {
				obf =
					new OutOfBoundsConstantValueFactory<T, RandomAccessibleInterval<T>>(
						Util.getTypeFromInterval(input).createVariable());
			}

			Interval inputInterval =
				FFTMethods.paddingIntervalCentered(input, FinalDimensions
					.wrap(paddedSize));

			inputRAI = Views.interval(Views.extend(input, obf), inputInterval);

		}
		else {
			inputRAI = input;
		}

		// TODO: proper use of Executor service
		final int numThreads = Runtime.getRuntime().availableProcessors();
		final ExecutorService service = Executors.newFixedThreadPool(numThreads);

		FFTMethods.realToComplex(inputRAI, output, 0, false, service);

		for (int d = 1; d < input.numDimensions(); d++)
			FFTMethods.complexToComplex(output, d, true, false, service);

		return output;
	}
}
