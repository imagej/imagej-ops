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

package net.imagej.ops.threshold.localSauvola;

import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.CenterAwareIntegralComputerOp;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.stats.IntegralMean;
import net.imagej.ops.stats.IntegralVariance;
import net.imagej.ops.threshold.apply.LocalThresholdIntegral;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.composite.Composite;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * <p>
 * Local thresholding algorithm as proposed by Sauvola et al.
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
 * @see LocalSauvolaThreshold
 * @see LocalThresholdIntegral
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalSauvolaThreshold.class,
	priority = Priority.LOW - 1)
public class LocalSauvolaThresholdIntegral<T extends RealType<T>> extends
	LocalThresholdIntegral<T> implements Ops.Threshold.LocalSauvolaThreshold
{

	@Parameter(required = false)
	private double k = 0.5d;

	@Parameter(required = false)
	private double r = 0.5d;

	@SuppressWarnings("unchecked")
	@Override
	protected CenterAwareIntegralComputerOp<T, BitType> unaryComputer() {
		final CenterAwareIntegralComputerOp<T, BitType> op =
			new LocalSauvolaThresholdComputer<>(ops().op(IntegralMean.class,
				DoubleType.class, RectangleNeighborhood.class), ops()
					.op(IntegralVariance.class, DoubleType.class,
						RectangleNeighborhood.class));

		op.setEnvironment(ops());
		return op;
	}

	private class LocalSauvolaThresholdComputer<I extends RealType<I>> extends
		AbstractBinaryComputerOp<I, RectangleNeighborhood<Composite<DoubleType>>, BitType>
		implements CenterAwareIntegralComputerOp<I, BitType>
	{

		private final IntegralMean<DoubleType> integralMean;
		private final IntegralVariance<DoubleType> integralVariance;

		public LocalSauvolaThresholdComputer(
			final IntegralMean<DoubleType> integralMean,
			final IntegralVariance<DoubleType> integralVariance)
		{
			super();
			this.integralMean = integralMean;
			this.integralVariance = integralVariance;
		}

		@Override
		public void compute(final I center,
			final RectangleNeighborhood<Composite<DoubleType>> neighborhood,
			final BitType output)
		{

			final DoubleType mean = new DoubleType();
			integralMean.compute(neighborhood, mean);

			final DoubleType variance = new DoubleType();
			integralVariance.compute(neighborhood, variance);

			final DoubleType stdDev = new DoubleType(Math.sqrt(variance.get()));

			final DoubleType threshold = new DoubleType(mean.getRealDouble() * (1.0d +
				k * ((Math.sqrt(stdDev.getRealDouble()) / r) - 1.0)));

			// Set value
			final Converter<I, DoubleType> conv = new RealDoubleConverter<>();
			final DoubleType centerPixelAsDoubleType = variance; // NB: Reuse
			// DoubleType
			conv.convert(center, centerPixelAsDoubleType);

			output.set(centerPixelAsDoubleType.compareTo(threshold) > 0);
		}

	}

	@Override
	protected int[] requiredIntegralImages() {
		return new int[] { 1, 2 };
	}

}
