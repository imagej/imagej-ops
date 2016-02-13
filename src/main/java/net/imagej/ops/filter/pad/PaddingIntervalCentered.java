
package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op used to calculate and return a centered padding interval given an input
 * RAI and the desired padded dimensions
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PaddingIntervalCentered.class,
	name = Ops.Filter.PaddingIntervalCentered.NAME,
	priority = Priority.HIGH_PRIORITY)
public class PaddingIntervalCentered<T extends RealType<T>, I extends RandomAccessibleInterval<T>, O extends Interval>
	extends AbstractUnaryFunctionOp<I, O> implements
	Ops.Filter.PaddingIntervalCentered
{

	@Parameter
	Dimensions paddedDimensions;

	@Override
	@SuppressWarnings("unchecked")
	public O compute1(final I input) {

		final long[] paddedSize = new long[paddedDimensions.numDimensions()];
		paddedDimensions.dimensions(paddedSize);

		O inputInterval = (O) FFTMethods.paddingIntervalCentered(input,
			FinalDimensions.wrap(paddedSize));

		return inputInterval;
	}
}
