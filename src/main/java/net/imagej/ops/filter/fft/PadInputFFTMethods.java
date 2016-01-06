
package net.imagej.ops.filter.fft;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractBinaryFunctionOp;
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

/**
 * Op used to pad the image by extending the borders
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PadInput.class, name = "createoutputfftmethods",
	priority = Priority.HIGH_PRIORITY)
public class PadInputFFTMethods<T extends RealType<T>, I extends RandomAccessibleInterval<T>, O extends RandomAccessibleInterval<T>>
	extends AbstractBinaryFunctionOp<I, Dimensions, O> implements
	Ops.Filter.PadInput
{

	@Parameter(required = false)
	private Boolean fast = true;

	/**
	 * The OutOfBoundsFactory used to extend the image
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf;

	@Override
	@SuppressWarnings("unchecked")
	public O compute2(final I input, final Dimensions paddedDimensions) {

		long[] paddedSize = new long[input.numDimensions()];
		long[] fftSize = new long[input.numDimensions()];

		if (fast) {
			FFTMethods.dimensionsRealToComplexFast(paddedDimensions, paddedSize,
				fftSize);
		}
		else {
			FFTMethods.dimensionsRealToComplexSmall(paddedDimensions, paddedSize,
				fftSize);
		}

		Dimensions paddedFFTMethodsInputDimensions = new FinalDimensions(
			paddedSize);

		if (obf == null) {
			obf = new OutOfBoundsConstantValueFactory<T, RandomAccessibleInterval<T>>(
				Util.getTypeFromInterval(input).createVariable());
		}

		Interval inputInterval = ops().filter().paddingIntervalCentered(input,
			paddedFFTMethodsInputDimensions);

		return (O) Views.interval(Views.extend(input, obf), inputInterval);
	}
}
