
package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op used to translate the center of an interval the origin. This is needed for
 * FFT operations
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PaddingIntervalOrigin.class,
	name = Ops.Filter.PaddingIntervalOrigin.NAME,
	priority = Priority.HIGH_PRIORITY)
public class PaddingIntervalOrigin<T extends RealType<T>, I extends RandomAccessibleInterval<T>, O extends Interval>
	extends AbstractUnaryFunctionOp<I, O> implements
	Ops.Filter.PaddingIntervalOrigin
{

	@Parameter
	Interval centeredInterval;

	@Override
	@SuppressWarnings("unchecked")
	public O compute1(final I input) {

		int numDimensions = input.numDimensions();

		// compute where to place the final Interval for the input so that the
		// coordinate in the center
		// of the input is at position (0,0).
		final long[] min = new long[numDimensions];
		final long[] max = new long[numDimensions];

		for (int d = 0; d < numDimensions; ++d) {
			min[d] = input.min(d) + input.dimension(d) / 2;
			max[d] = min[d] + centeredInterval.dimension(d) - 1;
		}

		return (O) new FinalInterval(min, max);
	}
}
