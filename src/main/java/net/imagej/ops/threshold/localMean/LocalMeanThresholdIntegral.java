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

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.stats.IntegralMean;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imagej.ops.threshold.apply.LocalThreshold;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.integral.IntegralImg;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

/**
 * LocalThresholdMethod using mean.
 *
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalMeanThreshold.class, priority = Priority.HIGH_PRIORITY)
public class LocalMeanThresholdIntegral<T extends RealType<T>> extends
	LocalThreshold<T>
	implements Ops.Threshold.LocalMeanThreshold
{
	
	private IntegralImg<T, DoubleType> integralImg;

	@Parameter
	private double c;
	
	@Override
	public void initialize() {
		// Increase span of shape by 1 to return correct values together with
		// the integralSum operation
		shape = new RectangleShape(((RectangleShape) shape).getSpan() + 1, false); // FIXME
		
		super.initialize();
	}

	@Override
	public void compute1(RandomAccessibleInterval<T> input,
		IterableInterval<BitType> output)
	{

		// Create IntegralImg from input
		integralImg = new IntegralImg<>(input, new DoubleType(),
			new RealDoubleConverter<T>());

		// integralImg will be larger by one pixel in each dimension than input
		// due to the computation of the integral image
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

		super.compute1((RandomAccessibleInterval<T>) extendedImg, output); // FIXME
	}
	
	protected CenterAwareComputerOp<T, BitType> unaryComputer(
		final BitType outClass)
	{
		final LocalThresholdMethod<T> op = new LocalThresholdMethod<T>() {

			private IntegralMean<DoubleType> integralMean;

			@SuppressWarnings("unchecked")
			@Override
			public void compute2(T center, Iterable<T> neighborhood, BitType output) {

				if (integralMean == null) {
					integralMean = ops().op(IntegralMean.class, DoubleType.class,
						RectangleNeighborhood.class, Interval.class);
				}

				final DoubleType sum = new DoubleType();
				integralMean.compute2((RectangleNeighborhood<DoubleType>) neighborhood, new FinalInterval(10, 10), sum); // FIXME

				// Subtract the contrast
				sum.sub(new DoubleType(c));

				// Set value
				final Converter<T, DoubleType> conv = new RealDoubleConverter<>();
				final DoubleType centerPixelAsDoubleType = new DoubleType();
				conv.convert(center, centerPixelAsDoubleType);

				output.set(centerPixelAsDoubleType.compareTo(sum) > 0);
			}
		};

		op.setEnvironment(ops());
		return op;
	}
	
}
