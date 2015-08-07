
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Min;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Min.NAME, label = "Statistics: Min")
public class DefaultMin<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Min
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double min = Double.MAX_VALUE;
		for (final T in : input) {
			final double n = in.getRealDouble();
			if (min > n) {
				min = n;
			}
		}

		output.setReal(min);
	}
}
