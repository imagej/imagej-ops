
package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.fft.FFTMethodsUtility;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op used to pad the image to a size that is compatible with FFTMethods
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PadFFTInput.class, priority = Priority.HIGH_PRIORITY)
public class PadInputFFTMethods<T extends RealType<T>, I extends RandomAccessibleInterval<T>, O extends RandomAccessibleInterval<T>>
	extends AbstractBinaryFunctionOp<I, Dimensions, O> implements
	Ops.Filter.PadFFTInput
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

		Dimensions paddedFFTMethodsInputDimensions = FFTMethodsUtility
			.getPaddedInputDimensionsRealToComplex(fast, paddedDimensions);

		if (obf == null) {
			obf = new OutOfBoundsConstantValueFactory<T, RandomAccessibleInterval<T>>(
				Util.getTypeFromInterval(input).createVariable());
		}

		Interval inputInterval = ops().filter().paddingIntervalCentered(input,
			paddedFFTMethodsInputDimensions);

		return (O) Views.interval(Views.extend(input, obf), inputInterval);
	}
}
