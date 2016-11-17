/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.threshold;

import net.imagej.ops.Ops;
import net.imagej.ops.pixml.DefaultHardClusterer;
import net.imagej.ops.pixml.HardClusterer;
import net.imagej.ops.pixml.UnsupervisedLearner;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.View;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.util.Pair;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;

/**
 * Abstract superclass for {@link LocalThresholder} implementations.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 */
public abstract class AbstractLocalThresholder<I, O extends BooleanType<O>>
	extends AbstractUnaryHybridCF<RandomAccessibleInterval<I>, IterableInterval<O>>
	implements LocalThresholder<I, O>
{

	@Parameter
	public Shape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory =
		new OutOfBoundsBorderFactory<>();

	/** Op that is used to learn and apply to the whole input */
	public HardClusterer<Pair<Neighborhood<I>, I>, O> hardClusterer;

	/** Op that is used for creating the output image */
	protected UnaryFunctionOp<RandomAccessibleInterval<I>, Img<BitType>> imgCreator;

	private LocalThresholder<I, O> localThresholder;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), new BitType());
		localThresholder = new LocalThresholder<>(getLearner());
		hardClusterer = ops().op(DefaultHardClusterer.class, in(),
			new LazyUnsupervisedLearner<>(localThresholder), new BitType());
	}

	@Override
	public void compute1(final RandomAccessibleInterval<I> input,
		final IterableInterval<O> output)
	{
		final RandomAccessible<Neighborhood<I>> safe = shape
			.neighborhoodsRandomAccessibleSafe(Views.extend(input,
				outOfBoundsFactory));
		final RandomAccessibleInterval<Pair<Neighborhood<I>, I>> pair = Views
			.interval(Views.pair(safe, Views.extend(input,
				outOfBoundsFactory)), input);
		final IterableInterval<O> functionOut = hardClusterer.compute1(Views.iterable(pair));

		// Temporary burnIn
		if (functionOut instanceof View) {
			ops().copy().iterableInterval(output, functionOut);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IterableInterval<O> createOutput(RandomAccessibleInterval<I> input) {
		return (IterableInterval<O>) imgCreator.compute1(input);
	}

	protected abstract ThresholdLearner<I, O> getLearner();

	/**
	 * Wrapper for a
	 * {@link net.imagej.ops.threshold.AbstractLocalThresholder.LocalThresholder}
	 * that learns and applies lazily.
	 *
	 * @param <I> type of input
	 * @param <O> type of output
	 */
	static class LazyUnsupervisedLearner<I, O extends BooleanType<O>> extends
		AbstractUnaryFunctionOp<IterableInterval<Pair<Neighborhood<I>, I>>, UnaryComputerOp<Pair<Neighborhood<I>, I>, O>>
		implements UnsupervisedLearner<Pair<Neighborhood<I>, I>, O>
	{

		private LocalThresholder<I, O> localThresholder;

		public LazyUnsupervisedLearner(
			final LocalThresholder<I, O> localThresholder)
		{
			this.localThresholder = localThresholder;
		}

		@Override
		public UnaryComputerOp<Pair<Neighborhood<I>, I>, O> compute1(
			final IterableInterval<Pair<Neighborhood<I>, I>> input)
		{
			return localThresholder;
		}

	}

	/**
	 * Uses a provided {@link ThresholdLearner} to implement a local thresholding.
	 *
	 * @param <I> type of input
	 * @param <O> type of output
	 */
	static class LocalThresholder<I, O extends BooleanType<O>> extends
		AbstractUnaryComputerOp<Pair<Neighborhood<I>, I>, O>
	{

		private ThresholdLearner<I, O> learner;

		public LocalThresholder(final ThresholdLearner<I, O> learner) {
			this.learner = learner;
		}

		@Override
		public void compute1(Pair<Neighborhood<I>, I> input, O output) {
			learner.compute1(input.getA()).compute1(input.getB(), output);
		}

	}

}
