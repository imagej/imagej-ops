
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.SumOfLogs;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = SumOfLogs.NAME,
	label = "Statistics: Sum Of Logs")
public class DefaultSumOfLogs<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements SumOfLogs
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double res = 0.0;
		for (final T in : input) {
			res += Math.log(in.getRealDouble());
		}
		output.setReal(res);
	}
}
