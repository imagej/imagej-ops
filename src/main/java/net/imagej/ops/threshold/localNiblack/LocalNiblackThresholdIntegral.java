package net.imagej.ops.threshold.localNiblack;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.CenterAwareIntegralComputerOp;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.stats.IntegralMean;
import net.imagej.ops.stats.IntegralVariance;
import net.imagej.ops.threshold.apply.LocalThresholdIntegral;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.composite.Composite;

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
 * @see LocalNiblackThreshold
 * @see LocalThresholdIntegral
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalNiblackThreshold.class,
	priority = Priority.LOW_PRIORITY - 1)
public class LocalNiblackThresholdIntegral<T extends RealType<T>> extends
	LocalThresholdIntegral<T> implements Ops.Threshold.LocalNiblackThreshold
{

	@Parameter
	private double c;

	@Parameter
	private double k;

	@SuppressWarnings("unchecked")
	@Override
	protected CenterAwareIntegralComputerOp<T, BitType> unaryComputer() {
		final CenterAwareIntegralComputerOp<T, BitType> op =
			new LocalNiblackThresholdComputer<>(in(), ops().op(IntegralMean.class,
				DoubleType.class, RectangleNeighborhood.class, Interval.class), ops()
					.op(IntegralVariance.class, DoubleType.class,
						RectangleNeighborhood.class, Interval.class));

		op.setEnvironment(ops());
		return op;
	}

	private class LocalNiblackThresholdComputer<I extends RealType<I>> extends
		AbstractBinaryComputerOp<I, RectangleNeighborhood<Composite<DoubleType>>, BitType>
		implements CenterAwareIntegralComputerOp<I, BitType>
	{

		RandomAccessibleInterval<I> source;
		private IntegralMean<DoubleType> integralMean;
		private IntegralVariance<DoubleType> integralVariance;

		public LocalNiblackThresholdComputer(RandomAccessibleInterval<I> source,
			IntegralMean<DoubleType> integralMean,
			IntegralVariance<DoubleType> integralVariance)
		{
			super();
			this.source = source;
			this.integralMean = integralMean;
			this.integralVariance = integralVariance;
		}

		@Override
		public void compute2(I center,
			RectangleNeighborhood<Composite<DoubleType>> neighborhood,
			BitType output)
		{

			final DoubleType threshold = new DoubleType(0.0d);

			final DoubleType mean = new DoubleType();
			integralMean.compute2(neighborhood, source, mean);

			threshold.add(mean);

			final DoubleType variance = new DoubleType();
			integralVariance.compute2(neighborhood, source, variance);

			final DoubleType stdDev = new DoubleType(Math.sqrt(variance.get()));
			stdDev.mul(k);

			threshold.add(stdDev);

			// Subtract the contrast
			threshold.sub(new DoubleType(c));

			// Set value
			final Converter<I, DoubleType> conv = new RealDoubleConverter<>();
			final DoubleType centerPixelAsDoubleType = new DoubleType();
			conv.convert(center, centerPixelAsDoubleType);

			output.set(centerPixelAsDoubleType.compareTo(threshold) > 0);
		}

	}

	@Override
	protected int[] requiredIntegralImages() {
		return new int[] { 1, 2 };
	}

}
