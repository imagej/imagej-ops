
package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Op used to pad a kernel and shift the center of the kernel to the origin
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PadShiftFFTKernel.class,
	priority = Priority.HIGH_PRIORITY + 1)
public class PadShiftKernel<T extends ComplexType<T>, I extends RandomAccessibleInterval<T>, O extends RandomAccessibleInterval<T>>
	extends AbstractBinaryFunctionOp<I, Dimensions, O> implements
	Ops.Filter.PadShiftFFTKernel
{

	private BinaryFunctionOp<I, Dimensions, O> paddingIntervalCentered;

	private BinaryFunctionOp<I, Interval, O> paddingIntervalOrigin;

	private UnaryFunctionOp<Dimensions, long[][]> fftSizeOp;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		paddingIntervalCentered = (BinaryFunctionOp) Functions.unary(ops(),
			PaddingIntervalCentered.class, Interval.class,
			RandomAccessibleInterval.class, Dimensions.class);

		paddingIntervalOrigin = (BinaryFunctionOp) Functions.unary(ops(),
			PaddingIntervalOrigin.class, Interval.class,
			RandomAccessibleInterval.class, Interval.class);

		fftSizeOp = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public O compute2(final I kernel, final Dimensions paddedDimensions) {

		Dimensions paddedFFTInputDimensions;

		// if an fftsize op has been set recompute padded size
		if (fftSizeOp != null) {
			long[][] sizes = fftSizeOp.compute1(paddedDimensions);

			paddedFFTInputDimensions = new FinalDimensions(sizes[0]);
		}
		else {
			paddedFFTInputDimensions = paddedDimensions;

		}

		// compute where to place the final Interval for the kernel so that the
		// coordinate in the center
		// of the kernel is shifted to position (0,0).

		final Interval kernelConvolutionInterval = paddingIntervalCentered.compute2(
			kernel, paddedFFTInputDimensions);

		final Interval kernelConvolutionIntervalOrigin = paddingIntervalOrigin
			.compute2(kernel, kernelConvolutionInterval);

		return (O) Views.interval(Views.extendPeriodic(Views.interval(Views
			.extendValue(kernel, Util.getTypeFromInterval(kernel).createVariable()),
			kernelConvolutionInterval)), kernelConvolutionIntervalOrigin);

	}

	protected void setFFTSizeOp(UnaryFunctionOp<Dimensions, long[][]> fftSizeOp) {
		this.fftSizeOp = fftSizeOp;
	}

}
