
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Skewness;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Skewness.NAME,
	label = "Statistics: Skewness")
public class DefaultSkewness<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Skewness
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		final double moment3 = this.ops.stats().moment3AboutMean(input).getRealDouble();
		final double std = this.ops.stats().stdDev(input).getRealDouble();

		output.setReal(Double.NaN);
		if (std != 0) {
			output.setReal((moment3) / (std * std * std));
		}
	}
}
