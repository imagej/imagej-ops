
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.GeometricMean;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = GeometricMean.NAME,
	label = "Statistics: GeometricMean")
public class DefaultGeometricMean<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements GeometricMean
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		final double size = this.ops.stats().size(input).getRealDouble();
		final double sumOfLogs = this.ops.stats().sumOfLogs(input).getRealDouble();

		if (size != 0) {
			output.setReal(Math.exp(sumOfLogs / size));
		}
		else {
			output.setReal(0);
		}

	}
}
