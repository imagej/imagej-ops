
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.StdDev;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = StdDev.NAME,
	label = "Statistics: Standard Deviation")
public class DefaultStandardDeviation<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements StdDev
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		output.setReal(Math.sqrt(this.ops.stats().variance(input).getRealDouble()));
	}

}
