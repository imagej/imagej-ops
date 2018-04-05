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

package net.imagej.ops.threshold.apply;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Map;
import net.imagej.ops.map.neighborhood.CenterAwareIntegralComputerOp;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imagej.ops.stats.IntegralMean;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.RectangleShape.NeighborhoodsIterableInterval;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;
import net.imglib2.view.composite.Composite;

import org.scijava.plugin.Parameter;

/**
 * Apply a local thresholding method to an image using integral images for speed
 * up, optionally using a out of bounds strategy.
 *
 * @author Stefan Helfrich (University of Konstanz)
 */
public abstract class LocalThresholdIntegral<I extends RealType<I>> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<I>, IterableInterval<BitType>>
{

	@Parameter
	protected RectangleShape shape;
	
	@Parameter(required = false)
	private OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory =
		new OutOfBoundsBorderFactory<>();

	private CenterAwareIntegralComputerOp<I, BitType> filterOp;
	private AbstractUnaryHybridCF<RandomAccessibleInterval<I>, RandomAccessibleInterval<RealType<?>>> integralImgOp;
	private AbstractUnaryHybridCF<RandomAccessibleInterval<I>, RandomAccessibleInterval<RealType<?>>> squareIntegralImgOp;

	@SuppressWarnings("rawtypes")
	private BinaryComputerOp<RandomAccessibleInterval<I>, NeighborhoodsIterableInterval<? extends Composite<RealType>>, IterableInterval<BitType>> map;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		// Increase span of shape by 1 to return correct values together with
		// the integralSum operation
		shape = new RectangleShape(shape.getSpan() + 1, false);

		filterOp = unaryComputer();

		integralImgOp = (AbstractUnaryHybridCF) ops().op(Ops.Image.Integral.class,
			in());
		squareIntegralImgOp = (AbstractUnaryHybridCF) ops().op(
			Ops.Image.SquareIntegral.class, in());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void compute(final RandomAccessibleInterval<I> input,
		final IterableInterval<BitType> output)
	{

		final List<RandomAccessibleInterval<RealType>> listOfIntegralImages =
			new ArrayList<>();
		for (final int order : requiredIntegralImages()) {
			final RandomAccessibleInterval<RealType> requiredIntegralImg =
				getIntegralImage(input, order);
			listOfIntegralImages.add(requiredIntegralImg);
		}

		// Composite image of integral images of order 1 and 2
		final RandomAccessibleInterval<RealType> stacked = Views.stack(
			listOfIntegralImages);
		final RandomAccessibleInterval<? extends Composite<RealType>> compositeRAI =
			Views.collapse(stacked);
		final RandomAccessibleInterval<? extends Composite<RealType>> extendedCompositeRAI =
			removeLeadingZeros(compositeRAI);

		final NeighborhoodsIterableInterval<? extends Composite<RealType>> neighborhoods =
			shape.neighborhoodsSafe(extendedCompositeRAI);

		if (map == null) {
			map = (BinaryComputerOp) ops().op(Map.class, out(), in(), neighborhoods,
				filterOp);
		}

		map.compute(input, neighborhoods, output);
	}

	/**
	 * Computes integral images of a given order and extends them such that
	 * {@link IntegralMean} et al work with them.
	 *
	 * @param input The RAI for which an integral image is computed
	 * @param order
	 * @return An extended integral image for the input RAI
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private RandomAccessibleInterval<RealType> getIntegralImage(
		final RandomAccessibleInterval<I> input, final int order)
	{
		ExtendedRandomAccessibleInterval<I, RandomAccessibleInterval<I>> extendedInput =
			Views.extend(input, outOfBoundsFactory);
		FinalInterval expandedInterval = Intervals.expand(input, shape.getSpan()-1);
		IntervalView<I> offsetInterval2 = Views.offsetInterval(extendedInput, expandedInterval);
		
		RandomAccessibleInterval<RealType> img = null;
		switch (order) {
			case 1:
				img = (RandomAccessibleInterval) integralImgOp.calculate(offsetInterval2);
				break;
			case 2:
				img = (RandomAccessibleInterval) squareIntegralImgOp.calculate(offsetInterval2);
				break;
		}

		img = addLeadingZeros(img);

		return img;
	}

	/**
	 * Add 0s before axis minimum.
	 * 
	 * @param input Input RAI
	 * @return An extended and cropped version of input
	 */
	private <T extends RealType<T>> RandomAccessibleInterval<T> addLeadingZeros(
		RandomAccessibleInterval<T> input)
	{
		final long[] min = Intervals.minAsLongArray(input);
		final long[] max = Intervals.maxAsLongArray(input);

		for (int i = 0; i < max.length; i++) {
			min[i]--;
		}

		final T realZero = Util.getTypeFromInterval(input).copy();
		realZero.setZero();

		final ExtendedRandomAccessibleInterval<T, RandomAccessibleInterval<T>> extendedImg = Views.extendValue(input,
			realZero);
		final IntervalView<T> offsetInterval = Views.interval(extendedImg,
			min, max);
		
		return Views.zeroMin(offsetInterval);
	}

	/**
	 * Removes leading 0s from integral image after composite creation.
	 *
	 * @param input Input RAI (can be a RAI of Composite)
	 * @return An extended and cropped version of input
	 */
	private <T> RandomAccessibleInterval<T> removeLeadingZeros(
		final RandomAccessibleInterval<T> input)
	{
		// Remove 0s from integralImg by shifting its interval by +1
		final long[] min = Intervals.minAsLongArray(input);
		final long[] max = Intervals.maxAsLongArray(input);

		for (int d = 0; d < input.numDimensions(); ++d) {
			int correctedSpan = getShape().getSpan() - 1;
			min[d] += (1 + correctedSpan);
			max[d] -= correctedSpan;
		}

		// Define the Interval on the infinite random accessibles
		final FinalInterval interval = new FinalInterval(min, max);

		final RandomAccessibleInterval<T> extendedImg = Views.offsetInterval(Views
			.extendBorder(input), interval);
		return extendedImg;
	}

	/**
	 * Get the shape (structuring element) used by this filter.
	 *
	 * @return the shape
	 */
	public RectangleShape getShape() {
		return shape;
	}

	/**
	 * @return the Computer to map to all neighborhoods of input to output.
	 */
	protected abstract CenterAwareIntegralComputerOp<I, BitType> unaryComputer();

	/**
	 * @return the orders of integral images that are required for a local
	 *         threshold method. For example [1,2] for the default as well as the
	 *         squared integral image.
	 */
	protected abstract int[] requiredIntegralImages();

}
