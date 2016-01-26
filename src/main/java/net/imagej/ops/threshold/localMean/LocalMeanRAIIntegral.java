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

package net.imagej.ops.threshold.localMean;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.stats.IntegralSum;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.integral.IntegralImg;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * LocalThresholdMethod using mean.
 *
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalMean.class, priority = Priority.HIGH_PRIORITY)
public class LocalMeanRAIIntegral<T extends RealType<T> & NativeType<T>> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<BitType>>
	implements Ops.Threshold.LocalMean
{

	@Parameter
	private RectangleShape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds;

	@Parameter(required = false)
	private IntegralImg<T, DoubleType> integralImg;

	@Parameter
	private double c;

	private IntegralSum<DoubleType> integralSum;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		integralSum = ops().op(IntegralSum.class, DoubleType.class, Views.iterable(
			in()));

		// FIXME Increase span of RectangleShape
//		if (!(shape instanceof IntegralRectangleShape)) {
			shape = new RectangleShape(shape.getSpan() + 1, false);
//		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute1(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<BitType> output)
	{
		// Create IntegralImg from input
		integralImg = new IntegralImg<>(input, new DoubleType(),
			new RealDoubleConverter<T>());

		// integralImg will be larger by one pixel in each dimension than input due
		// to the computation of the integral image
		RandomAccessibleInterval<DoubleType> img = null;
		if (integralImg.process()) {
			img = integralImg.getResult();
		}

		// Remove 0s from integralImg by shifting its interval by +1
		final long[] min = new long[input.numDimensions()];
		final long[] max = new long[input.numDimensions()];

		for (int d = 0; d < input.numDimensions(); ++d) {
			min[d] = input.min(d) + 1;
			max[d] = input.max(d) + 1;
		}

		// Define the Interval on the infinite random accessibles
		final FinalInterval interval = new FinalInterval(min, max);

		final RandomAccessibleInterval<DoubleType> extendedImg = Views
			.offsetInterval(Views.extendBorder(img), interval);

		// Random access for input and output
		final RandomAccess<BitType> outputRandomAccess = output.randomAccess();
		final RandomAccess<T> inputRandomAccess = input.randomAccess();

		// Cast is safe due to the initialization of the Op
		final Cursor<Neighborhood<DoubleType>> cursor = shape.neighborhoods(extendedImg).cursor();

		// Iterate neighborhoods
		while (cursor.hasNext()) {
			final Neighborhood<DoubleType> neighborhood = cursor.next();
			
			final DoubleType sum = new DoubleType();
			if (neighborhood instanceof RectangleNeighborhood) {
				integralSum.compute1((RectangleNeighborhood<DoubleType>) neighborhood, sum);
			}

			final long[] neighborhoodPosition = new long[neighborhood
				.numDimensions()];
			neighborhood.localize(neighborhoodPosition);

			// Absolute difference between minima
			final long[] neighborhoodMinimum = new long[neighborhood.numDimensions()];
			neighborhood.min(neighborhoodMinimum);
			neighborhoodMinimum[0] = neighborhoodMinimum[0] - 1;
			neighborhoodMinimum[1] = neighborhoodMinimum[1] - 1;

			final long[] neighborhoodMaximum = new long[neighborhood.numDimensions()];
			neighborhood.max(neighborhoodMaximum);
			neighborhoodMaximum[0] = neighborhoodMaximum[0] + 1;
			neighborhoodMaximum[1] = neighborhoodMaximum[1] + 1;

			final long[] inputMinimum = new long[extendedImg.numDimensions()];
			extendedImg.min(inputMinimum);

			final long[] inputMaximum = new long[extendedImg.numDimensions()];
			extendedImg.max(inputMaximum);

			int area = 1;
			for (int d = 0; d < input.numDimensions(); d++) {
				if (neighborhoodMinimum[d] <= inputMinimum[d] &&
					neighborhoodMaximum[d] <= inputMaximum[d])
				{
					area *= Math.abs((neighborhoodMaximum[d] + 1) - inputMinimum[d]);
					continue;
				}

				if (neighborhoodMinimum[d] >= inputMinimum[d] &&
					neighborhoodMaximum[d] <= inputMaximum[d])
				{
					area *= Math.abs((neighborhoodMaximum[d] + 1) -
						neighborhoodMinimum[d]);
					continue;
				}

				if (neighborhoodMinimum[d] >= inputMinimum[d] &&
					neighborhoodMaximum[d] >= inputMaximum[d])
				{
					area *= Math.abs((inputMaximum[d] + 1) - neighborhoodMinimum[d]);
					continue;
				}

				// FIXME Missing case that implies that the RectangleShape is bigger
				// than the image itself
			}

			// Compute mean by dividing the sum divided by the number of elements
			sum.div(new DoubleType(area));

			// Subtract the contrast
			sum.sub(new DoubleType(c));

			// Set value
			inputRandomAccess.setPosition(neighborhoodPosition);
			final T inputPixel = inputRandomAccess.get();

			final Converter<T, DoubleType> conv = new RealDoubleConverter<>();
			final DoubleType inputPixelAsDoubleType = new DoubleType();
			conv.convert(inputPixel, inputPixelAsDoubleType);

			outputRandomAccess.setPosition(neighborhood);
			outputRandomAccess.get().set(inputPixelAsDoubleType.compareTo(sum) > 0);
		}
	}

}
