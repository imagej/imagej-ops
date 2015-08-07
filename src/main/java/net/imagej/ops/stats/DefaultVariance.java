
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Variance;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Variance.NAME,
	label = "Statistics: Variance")
public class DefaultVariance<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Variance
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double sum = 0;
		double sumSqr = 0;
		int n = 0;

		for (final T in : input) {
			final double px = in.getRealDouble();
			++n;
			sum += px;
			sumSqr += px * px;
		}

		output.setReal((sumSqr - (sum * sum / n)) / (n - 1));
	}

}
