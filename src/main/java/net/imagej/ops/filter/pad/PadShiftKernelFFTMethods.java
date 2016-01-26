
package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.fft.FFTMethodsUtility;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op used to pad a kernel to a size that is compatible with FFTMethods and
 * shift the center of the kernel to the origin
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PadShiftFFTKernel.class,
	priority = Priority.HIGH_PRIORITY)
public class PadShiftKernelFFTMethods<T extends RealType<T>, I extends RandomAccessibleInterval<T>, O extends RandomAccessibleInterval<T>>
	extends AbstractBinaryFunctionOp<I, Dimensions, O> implements
	Ops.Filter.PadShiftFFTKernel
{

	@Parameter(required = false)
	private Boolean fast = true;

	@Override
	@SuppressWarnings("unchecked")
	public O compute2(final I kernel, final Dimensions paddedDimensions) {

		Dimensions paddedFFTMethodsInputDimensions = FFTMethodsUtility
			.getPaddedInputDimensionsRealToComplex(fast, paddedDimensions);

		// compute where to place the final Interval for the kernel so that the
		// coordinate in the center
		// of the kernel is shifted to position (0,0).

		final Interval kernelConvolutionInterval = ops().filter()
			.paddingIntervalCentered(kernel, paddedFFTMethodsInputDimensions);

		final Interval kernelConvolutionIntervalOrigin = ops().filter()
			.paddingIntervalOrigin(kernel, kernelConvolutionInterval);

		// assemble the kernel (size of the input + extended periodic +
		// top left at center of input kernel)
		return (O) Views.interval(Views.extendPeriodic(Views.interval(Views
			.extendValue(kernel, Util.getTypeFromInterval(kernel).createVariable()),
			kernelConvolutionInterval)), kernelConvolutionIntervalOrigin);

	}
}
