
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Moment1AboutMean;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Moment1AboutMean.NAME,
	label = "Statistics: Moment1AboutMean")
public class DefaultMoment1AboutMean<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Moment1AboutMean
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		final double mean = this.ops.stats().mean(input).getRealDouble();
		final double size = this.ops.stats().size(input).getRealDouble();

		double res = 0;
		for (final T in : input) {
			final double val = in.getRealDouble() - mean;
			res += val;
		}

		output.setReal(res / size);
	}
}
