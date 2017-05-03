/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Low-level difference of Gaussians (DoG) implementation which leans on other
 * ops to do the work.
 *
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 * @param <T>
 */
@Plugin(type = Ops.Filter.DoG.class)
public class DefaultDoG<T extends NumericType<T> & NativeType<T>> extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Filter.DoG
{

	@Parameter
	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss1;

	@Parameter
	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss2;

	@Parameter
	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> outputCreator;

	@Parameter
	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> tmpCreator;

	@Override
	public RandomAccessibleInterval<T> createOutput(
		final RandomAccessibleInterval<T> input)
	{
		return outputCreator.calculate(input);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<T> output)
	{
		// input may potentially be translated
		final long[] translation = new long[input.numDimensions()];
		input.min(translation);

		final IntervalView<T> tmpInterval = Views.interval(Views.translate(
			(RandomAccessible<T>) tmpCreator.calculate(input), translation), output);

		gauss1.compute(input, tmpInterval);
		gauss2.compute(input, output);

		// TODO: Match the Subtract Op in initialize() once we have BinaryOp
		ops().run(Ops.Math.Subtract.class, output, output, tmpInterval);
	}

}
