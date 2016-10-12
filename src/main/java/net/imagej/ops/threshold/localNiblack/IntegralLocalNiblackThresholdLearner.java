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

package net.imagej.ops.threshold.localNiblack;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.CenterAwareIntegralComputerOp;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.stats.IntegralMean;
import net.imagej.ops.stats.IntegralVariance;
import net.imagej.ops.threshold.IntegralThresholdLearner;
import net.imagej.ops.threshold.apply.LocalThresholdIntegral;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.composite.Composite;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * <p>
 * Niblack's local thresholding algorithm.
 * </p>
 * <p>
 * This implementation improves execution speed by using integral images for the
 * computations of mean and standard deviation in the local windows. A
 * significant improvement can be observed for increased window sizes (
 * {@code span > 10}). It operates on {@link RandomAccessibleInterval}s of
 * {@link RealType}, i.e. explicit conversion to an integral image is <b>not</b>
 * required.
 * </p>
 *
 * @author Jonathan Hale
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 * @see IntegralLocalNiblack
 */
@Plugin(type = Op.class)
public class IntegralLocalNiblackThresholdLearner<I extends RealType<I>, O extends BooleanType<O>>
	extends
	AbstractUnaryFunctionOp<RectangleNeighborhood<Composite<DoubleType>>, UnaryComputerOp<I, O>>
	implements IntegralThresholdLearner<I, O>
{

	@Parameter
	private double c;

	@Parameter
	private double k;

	private IntegralMean<DoubleType> integralMean;
	private IntegralVariance<DoubleType> integralVariance;

	@SuppressWarnings("unchecked")
	@Override
	public UnaryComputerOp<I, O> compute1(final RectangleNeighborhood<Composite<DoubleType>> neighborhood)
	{
		if (integralMean == null) {
			integralMean = ops().op(IntegralMean.class, DoubleType.class,
				RectangleNeighborhood.class);
		}

		if (integralVariance == null) {
			integralVariance = ops().op(IntegralVariance.class, DoubleType.class,
				RectangleNeighborhood.class);
		}

		final DoubleType threshold = new DoubleType(0.0d);

		final DoubleType mean = new DoubleType();
		integralMean.compute1(neighborhood, mean);

		threshold.add(mean);

		final DoubleType variance = new DoubleType();
		integralVariance.compute1(neighborhood, variance);

		final DoubleType stdDev = new DoubleType(Math.sqrt(variance.get()));
		stdDev.mul(k);

		threshold.add(stdDev);

		// Subtract the contrast
		threshold.sub(new DoubleType(c));

		UnaryComputerOp<I, O> predictorOp = new AbstractUnaryComputerOp<I, O>() {

			@Override
			public void compute1(I in, O out) {
				// Set value
				final Converter<I, DoubleType> conv = new RealDoubleConverter<>();
				final DoubleType centerPixelAsDoubleType = variance; // NB: Reuse
				// DoubleType
				conv.convert(in, centerPixelAsDoubleType);

				out.set(centerPixelAsDoubleType.compareTo(threshold) > 0);
			}
		};
		predictorOp.setEnvironment(ops());
		predictorOp.initialize();

		return predictorOp;
	}

	@Override
	public int[] requiredIntegralImages() {
		return new int[] { 1, 2 };
	}

}
