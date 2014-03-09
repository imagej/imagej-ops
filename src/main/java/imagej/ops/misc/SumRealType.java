
package imagej.ops.misc;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "sum", priority = Priority.LOW_PRIORITY)
public class SumRealType<T extends RealType<T>> extends
	AbstractFunction<Iterable<T>, T> implements Sum<T, T>
{

	@Override
	public T compute(final Iterable<T> input, final T output) {

		for (final T t : input) {
			output.add(t);
		}

		return output;
	}
}
