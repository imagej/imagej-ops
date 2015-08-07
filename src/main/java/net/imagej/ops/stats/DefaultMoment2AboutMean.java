
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Moment2AboutMean;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Moment2AboutMean.NAME,
	label = "Statistics: Moment2AboutMean")
public class DefaultMoment2AboutMean<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Moment2AboutMean
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		final double mean = this.ops.stats().mean(input).getRealDouble();
		final double size = this.ops.stats().size(input).getRealDouble();

		double res = 0;
		for (final T in : input) {
			final double val = in.getRealDouble() - mean;
			res += val * val;
		}

		output.setReal(res / size);
	}
}
