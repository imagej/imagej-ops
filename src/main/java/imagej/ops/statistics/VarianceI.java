
package imagej.ops.statistics;

import imagej.ops.AbstractFunction;
import imagej.ops.OpService;
import imagej.ops.statistics.moments.Moment2AboutMean;

import org.scijava.plugin.Parameter;

/**
 * @author Christian Dietz
 * @param <T>
 * @param <V>
 */
public class VarianceI<T, V> extends AbstractFunction<Iterable<? extends T>, V>
	implements Variance<Iterable<? extends T>, V>
{

	@Parameter(required = false)
	private Moment2AboutMean<Iterable<? extends T>, V> moment2;

	@Parameter
	private OpService ops;

	@Override
	public V compute(final Iterable<? extends T> input, final V output) {

		if (moment2 == null) {
			initFunctions(input, output);
		}

		return moment2.compute(input, output);
	}

	@SuppressWarnings("unchecked")
	private void initFunctions(final Iterable<? extends T> input, final V output)
	{
		moment2 =
			(Moment2AboutMean<Iterable<? extends T>, V>) ops.op(
				Moment2AboutMean.class, output, input);
	}
}
