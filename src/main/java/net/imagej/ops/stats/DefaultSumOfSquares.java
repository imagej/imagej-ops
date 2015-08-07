
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.SumOfSquares;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = SumOfSquares.NAME,
	label = "Statistics: Sum Of Squares")
public class DefaultSumOfSquares<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements SumOfSquares
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double res = 0.0;
		for (final T in : input) {
			final double tmp = in.getRealDouble();
			res += tmp * tmp;
		}
		output.setReal(res);
	}
}
