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

package net.imagej.ops.filter.dog;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Contingent;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Filter.DoG;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.dog.DifferenceOfGaussian;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Default implementation of {@link DifferenceOfGaussian}.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = DoG.class, name = DoG.NAME)
public class DefaultDoG<T extends NumericType<T> & NativeType<T>>
	extends
	AbstractOutputFunction<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements DoG, Contingent
{

	@Parameter
	private ThreadService ts;

	@Parameter
	private OpService ops;

	@Parameter
	private double[] sigmas1;

	@Parameter
	private double[] sigmas2;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> fac;

	@SuppressWarnings("unchecked")
	@Override
	public RandomAccessibleInterval<T> createOutput(
		final RandomAccessibleInterval<T> input)
	{
		return (RandomAccessibleInterval<T>) ops.create().img(input);
	}

	@Override
	protected void safeCompute(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<T> output)
	{

		if (fac == null) {
			fac =
				new OutOfBoundsMirrorFactory<T, RandomAccessibleInterval<T>>(
					Boundary.SINGLE);
		}

		// input may potentially be translated
		final long[] translation = new long[input.numDimensions()];
		input.min(translation);

		final IntervalView<T> tmpInterval =
			Views.interval(Views.translate((RandomAccessible<T>) ops.create().img(
				input, Util.getTypeFromInterval(input)), translation), output);

		// TODO: How can I enforce that a certain gauss implementation is used
		// here? I don't want to pass the Gauss class in the DoG, I rather
		// would love to have something that allows me to enforce
		// that certain Ops are used, even if they have lower priority. This is a
		// common use-case if you want to let the user
		// select in a GUI if (for example) he wants to use CUDA or CPU
		// see issue https://github.com/imagej/imagej-ops/issues/154)

		ops.filter().gauss(tmpInterval, input, sigmas1);
		ops.filter().gauss(output, input, sigmas2);

		// TODO: Use SUbtractOp as soon as available (see issue
		// https://github.com/imagej/imagej-ops/issues/161).
		final Cursor<T> tmpCursor = Views.flatIterable(tmpInterval).cursor();
		final Cursor<T> outputCursor = Views.flatIterable(output).cursor();

		while (outputCursor.hasNext()) {
			outputCursor.next().sub(tmpCursor.next());
		}

	}

	@Override
	public boolean conforms() {
		return sigmas1.length == sigmas2.length &&
			sigmas1.length == getInput().numDimensions();
	}
}
