
package net.imagej.ops.filter.fft;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op used to pad the image by extending the borders
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.FFT.class, name = Ops.Filter.PadInput.NAME,
	priority = Priority.HIGH_PRIORITY)
public class PadFFTInput<T extends RealType<T>, I extends RandomAccessibleInterval<T>, O extends RandomAccessibleInterval<T>>
	extends AbstractFunctionOp<I, O>
{

	@Parameter
	Dimensions paddedDimensions;

	/**
	 * The OutOfBoundsFactory used to extend the image
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf;

	@Override
	@SuppressWarnings("unchecked")
	public O compute(final I input) {

		if (obf == null) {
			obf =
				new OutOfBoundsConstantValueFactory<T, RandomAccessibleInterval<T>>(
					Util.getTypeFromInterval(input).createVariable());
		}

		final long[] paddedSize = new long[paddedDimensions.numDimensions()];
		paddedDimensions.dimensions(paddedSize);

		Interval inputInterval =
			FFTMethods.paddingIntervalCentered(input, FinalDimensions
				.wrap(paddedSize));

		return (O) Views.interval(Views.extend(input, obf), inputInterval);
	}
}
