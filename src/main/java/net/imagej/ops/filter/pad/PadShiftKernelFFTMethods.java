
package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.fftSize.ComputeFFTMethodsSize;
import net.imagej.ops.special.function.Functions;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;

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
public class PadShiftKernelFFTMethods<T extends ComplexType<T>, I extends RandomAccessibleInterval<T>, O extends RandomAccessibleInterval<T>>
	extends PadShiftKernel<T, I, O>
{

	@Parameter(required = false)
	private boolean fast = true;

	@Override
	public void initialize() {
		super.initialize();

		setFFTSizeOp(Functions.unary(ops(), ComputeFFTMethodsSize.class,
			long[][].class, Dimensions.class, true, fast));
	}

}
