
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.SumOfInverses;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = SumOfInverses.NAME,
	label = "Statistics: Sum Of Inverses")
public class DefaultSumOfInverses<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements SumOfInverses
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double res = 0.0;
		for (final T in : input) {
			res += (1.0d / in.getRealDouble());
		}
		output.setReal(res);
	}
}
