package net.imagej.ops.filter.sigma;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Stats.Variance;
import net.imagej.ops.filter.AbstractCenterAwareNeighborhoodBasedFilter;
import net.imagej.ops.map.neighborhood.AbstractCenterAwareComputerOp;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link SigmaFilterOp}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <T>
 *            type
 */
@Plugin(type = Ops.Filter.Sigma.class, name = Ops.Filter.Sigma.NAME, priority = Priority.LOW_PRIORITY)
public class DefaultSigmaFilter<T extends RealType<T>> extends
		AbstractCenterAwareNeighborhoodBasedFilter<T, T> implements
		SigmaFilterOp<RandomAccessibleInterval<T>>, Contingent {

	@Parameter
	private Double range;

	@Parameter
	private Double minPixelFraction;

	@Override
	protected CenterAwareComputerOp<T, T> getComputer(Class<?> inClass,
			Class<?> outClass) {
		final AbstractCenterAwareComputerOp<T, T> op =
			new AbstractCenterAwareComputerOp<T, T>() {

			private ComputerOp<Iterable<T>, DoubleType> variance;

			@Override
			public void compute(Pair<T, Iterable<T>> input, T output) {
				if (variance == null) {
					variance = (ComputerOp<Iterable<T>, DoubleType>) ops().op(
							Variance.class, DoubleType.class, input.getB());
				}

				DoubleType varianceResult = new DoubleType();
				variance.compute(input.getB(), varianceResult);
				double varianceValue = varianceResult.getRealDouble() * range;

				final double centerValue = input.getA().getRealDouble();
				double sumAll = 0;
				double sumWithin = 0;
				long countAll = 0;
				long countWithin = 0;

				for (T neighbor : input.getB()) {
					final double pixelValue = neighbor.getRealDouble();
					final double diff = centerValue - pixelValue;

					sumAll += pixelValue;
					++countAll;

					if (diff > varianceValue || diff < -varianceValue) {
						continue;
					}

					// pixel within variance range
					sumWithin += pixelValue;
					++countWithin;
				}

				if (countWithin < (int) (minPixelFraction * countAll)) {
					output.setReal(sumAll / countAll); // simply mean
				} else {
					// mean over pixels in variance range only
					output.setReal(sumWithin / countWithin);
				}
			}

		};
		op.setEnvironment(ops());
		return op;
	}

	@Override
	public boolean conforms() {
		return range > 0.0;
	}
}
