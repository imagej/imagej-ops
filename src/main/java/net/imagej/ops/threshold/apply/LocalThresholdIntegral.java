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

package net.imagej.ops.threshold.apply;

import org.scijava.plugin.Parameter;

import net.imagej.ops.Ops.Map;
import net.imagej.ops.map.neighborhood.CenterAwareIntegralComputerOp;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.integral.IntegralImg;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.RealComposite;

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

	private BinaryComputerOp<RandomAccessibleInterval<I>, IterableInterval<Neighborhood<RealComposite<DoubleType>>>, IterableInterval<BitType>> map;

	private IntegralImg<I, DoubleType> integralImg;
	
	@Override
	public void initialize() {
		// Increase span of shape by 1 to return correct values together with
		// the integralSum operation
		shape = new RectangleShape(shape.getSpan() + 1, false);

		filterOp = unaryComputer();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void compute1(RandomAccessibleInterval<I> input,
		IterableInterval<BitType> output)
	{
		
		// TODO Let the implementor determine the order of integral images required
		final RandomAccessibleInterval<DoubleType> extendedImg = getIntegralImage(
			input, 1);
		
		final RandomAccessibleInterval<DoubleType> extendedImg2 = getIntegralImage(
			input, 2);
		
		// Composite image of integral images of order 1 and 2
		RandomAccessibleInterval<DoubleType> stacked = Views.stack(extendedImg,
			extendedImg2);
		RandomAccessibleInterval<RealComposite<DoubleType>> compositeRAI =
			Views.collapseReal(stacked);
		RandomAccessibleInterval<RealComposite<DoubleType>> extendedCompositeRAI =
			removeLeadingZeros(compositeRAI);
		
		IterableInterval<Neighborhood<RealComposite<DoubleType>>> neighborhoods =
			shape.neighborhoodsSafe(extendedCompositeRAI);
		
		if (map == null) {
			map = (BinaryComputerOp) ops().op(Map.class, out(), in(), neighborhoods,
				filterOp);
		}
		
		map.compute2(input, neighborhoods, output);
	}

	/**
	 * TODO Documentation
	 * 
	 * @param input
	 * @param order
	 * @return
	 */
	private RandomAccessibleInterval<DoubleType> getIntegralImage(
		RandomAccessibleInterval<I> input, int order)
	{
		// Create IntegralImg from input
		integralImg = new IntegralImg<>(input, new DoubleType(),
			new RealDoubleConverter<I>(), order);

		// integralImg will be larger by one pixel in each dimension than input due
		// to the computation of the integral image
		RandomAccessibleInterval<DoubleType> img = null;
		if (integralImg.process()) {
			img = integralImg.getResult();
		}

		return img;
	}

	/**
	 * TODO Documentation
	 * 
	 * @param input
	 * @return
	 */
	private <T> RandomAccessibleInterval<T> removeLeadingZeros(
		RandomAccessibleInterval<T> input)
	{
		// Remove 0s from integralImg by shifting its interval by +1
		final long[] min = new long[input.numDimensions()];
		final long[] max = new long[input.numDimensions()];

		for (int d = 0; d < input.numDimensions(); ++d) {
			min[d] = input.min(d) + 1;
			max[d] = input.max(d);
		}

		// Define the Interval on the infinite random accessibles
		final FinalInterval interval = new FinalInterval(min, max);
		
		final RandomAccessibleInterval<T> extendedImg = Views
			.offsetInterval(Views.extendBorder(input), interval);
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

	
}
