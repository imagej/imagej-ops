
package imagej.ops.misc;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;

import java.util.Iterator;

import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "max", priority = Priority.LOW_PRIORITY)
public class MaxRealType<T extends RealType<T>> extends
	AbstractFunction<Iterable<T>, T> implements Max<T, T>
{

	@Override
	public T compute(final Iterable<T> input, final T output) {

		final Iterator<T> it = input.iterator();
		T max = it.next();

		while (it.hasNext()) {
			final T next = it.next();
			if (max.compareTo(next) < 0) {
				max = next;
			}
		}
		output.set(max);
		return output;
	}
}
