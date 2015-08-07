
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Max;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Max.NAME, label = "Statistics: Max")
public class DefaultMax<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Max
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double max = Double.MIN_VALUE;
		for (final T in : input) {
			final double n = in.getRealDouble();
			if (max < n) {
				max = n;
			}
		}

		output.setReal(max);
	}
}
