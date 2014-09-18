/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.gauss;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.Op;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.algorithm.gauss3.SeparableSymmetricConvolution;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Plugin(type = Op.class, name = "gauss")
public class GaussRAI2RAI<T extends RealType<T>> extends
	AbstractStrictFunction<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Gauss
{

	@Parameter
	private double sigma;

	// TODO: make that selectable by the user/programmer
	private final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds =
		new OutOfBoundsMirrorFactory(Boundary.SINGLE);

	@Override
	public RandomAccessibleInterval<T> compute(
		final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<T> output)
	{
		final RandomAccessible<FloatType> eIn =
			(RandomAccessible) Views.extend(input, outOfBounds);

		final double[] sigmas = new double[input.numDimensions()];
		Arrays.fill(sigmas, sigma);

		try {
			final int numThreads = Runtime.getRuntime().availableProcessors();
			final ExecutorService executorService =
				Executors.newFixedThreadPool(numThreads);
			SeparableSymmetricConvolution.convolve(Gauss3.halfkernels(sigmas), eIn,
				output, executorService);
		}
		catch (final IncompatibleTypeException e) {
			// TODO: better error handling
			throw new RuntimeException(e);
		}

		return output;
	}

}
