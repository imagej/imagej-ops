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

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.Ops.IFFT;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Inverse fft that operates on an RAI and wraps FFTMethods.
 * 
 * @author Brian Northan
 * @param <C>
 * @param <T>
 */
@Plugin(type = IFFT.class, name = IFFT.NAME)
public class IFFTRAI<C extends ComplexType<C>, T extends RealType<T>>
	extends
	AbstractStrictFunction<RandomAccessibleInterval<C>, RandomAccessibleInterval<T>>
{

	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<C> input,
		RandomAccessibleInterval<T> output)
	{
		// TODO: proper use of Executor service
		final int numThreads = Runtime.getRuntime().availableProcessors();
		final ExecutorService service = Executors.newFixedThreadPool(numThreads);

		for (int d = input.numDimensions() - 1; d > 0; d--)
			FFTMethods.complexToComplex(input, d, false, true, service);

		FFTMethods.complexToReal(input, output, FFTMethods
			.unpaddingIntervalCentered(input, output), 0, true, service);

		return output;
	}
}
