
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Sum;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Sum.NAME, label = "Statistics: Sum")
public class DefaultSum<I extends RealType<I>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<I>, O>implements Sum
{

	@Override
	public void compute(final Iterable<I> input, final O output) {
		double sum = 0;
		for (final I in : input) {
			sum += in.getRealDouble();
		}

		output.setReal(sum);
	}
}
