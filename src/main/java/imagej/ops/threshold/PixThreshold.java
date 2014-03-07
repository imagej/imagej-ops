
package imagej.ops.threshold;

import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import net.imglib2.type.logic.BitType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "applyThreshold")
public class PixThreshold<T extends Comparable<T>> extends
	UnaryFunction<T, BitType>
{

	@Parameter
	private T threshold;

	@Override
	public BitType compute(final T input, final BitType output) {
		output.set(input.compareTo(threshold) > 0);
		return output;
	}

	@Override
	public UnaryFunction<T, BitType> copy() {
		final PixThreshold<T> func = new PixThreshold<T>();
		func.threshold = threshold;
		return func;
	}
}
