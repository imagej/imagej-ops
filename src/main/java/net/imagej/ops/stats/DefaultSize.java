
package net.imagej.ops.stats;

import java.util.Iterator;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Size;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Size.NAME, label = "Statistics: Size")
public class DefaultSize<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Size
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double size = 0;

		final Iterator<T> it = input.iterator();
		while (it.hasNext()) {
			it.next();
			size++;
		}

		output.setReal(size);
	}
}
