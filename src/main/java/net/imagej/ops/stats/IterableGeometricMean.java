
package net.imagej.ops.stats;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.GeometricMean;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = GeometricMean.NAME,
	label = "Statistics: Geometric Mean", priority = Priority.FIRST_PRIORITY)
public class IterableGeometricMean<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements GeometricMean
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double size = 0;
		double sumOfLogs = 0;
		for (final T in : input) {
			size++;
			sumOfLogs += Math.log(in.getRealDouble());
		}

		if (size != 0) {
			output.setReal(Math.exp(sumOfLogs / size));
		}
		else {
			output.setReal(0);
		}

	}
}
