
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Kurtosis;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Kurtosis.NAME,
	label = "Statistics: Kurtosis")
public class DefaultKurtosis<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Kurtosis
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		output.setReal(Double.NaN);

		final double std = this.ops.stats().stdDev(input).getRealDouble();
		final double moment4 = this.ops.stats().moment4AboutMean(input).getRealDouble();

		if (std != 0) {
			output.setReal((moment4) / (std * std * std * std));
		}
	}
}
