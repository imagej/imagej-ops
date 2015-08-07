
package net.imagej.ops.stats;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Size;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Size.NAME, label = "Statistics: Size",
	priority = Priority.FIRST_PRIORITY)
public class IterableIntervalSize<T extends RealType<T>, O extends RealType<O>>
	extends AbstractStatOp<IterableInterval<T>, O>implements Size
{

	@Override
	public void compute(IterableInterval<T> input, O output) {
		output.setReal(input.size());
	}

}
