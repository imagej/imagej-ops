
package net.imagej.ops.stats;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Mean;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Mean.NAME, label = "Statistics: Mean",
	priority = Priority.LOW_PRIORITY)
public class DefaultMean<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Mean
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		output.setReal(this.ops.stats().sum(input).getRealDouble() / this.ops.stats().size(input)
			.getRealDouble());
	}
}
