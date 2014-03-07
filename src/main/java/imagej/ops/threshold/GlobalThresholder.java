
package imagej.ops.threshold;

import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.UnaryFunction;
import net.imglib2.IterableInterval;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "threshold")
public class GlobalThresholder<T extends RealType<T>> extends
	UnaryFunction<IterableInterval<T>, IterableInterval<BitType>> implements Op
{

	@Parameter
	private IterableInterval<T> in;

	@Parameter(type = ItemIO.BOTH)
	private IterableInterval<BitType> out;

	@Parameter
	private ThresholdMethod<T> method;

	@Parameter
	private OpService opService;

	/**
	 * Sets the thresholding method to use
	 */
	public void setMethod(final ThresholdMethod<T> method) {
		this.method = method;
	}

	@Override
	public IterableInterval<T> getInput() {
		return in;
	}

	@Override
	public IterableInterval<BitType> getOutput() {
		return out;
	}

	@Override
	public void setInput(final IterableInterval<T> input) {
		in = input;
	}

	@Override
	public void setOutput(final IterableInterval<BitType> output) {
		out = output;

	}

	@Override
	public IterableInterval<BitType> compute(final IterableInterval<T> input,
		final IterableInterval<BitType> output)
	{
		final T threshold = (T) opService.run(method, input);
		final PixThreshold<T> apply = new PixThreshold<T>();
		return (IterableInterval<BitType>) opService.run("map", input, apply,
			output);
	}

	@Override
	public UnaryFunction<IterableInterval<T>, IterableInterval<BitType>> copy() {
		final GlobalThresholder<T> func = new GlobalThresholder<T>();
		func.method = method.copy();
		return func;
	}
}
