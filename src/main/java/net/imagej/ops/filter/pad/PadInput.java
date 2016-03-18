
package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
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
 * Op used to pad the image by extending the borders
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PadInput.class, name = Ops.Filter.PadInput.NAME,
	priority = Priority.HIGH_PRIORITY)
public class PadInput<T extends RealType<T>, I extends RandomAccessibleInterval<T>, O extends RandomAccessibleInterval<T>>
	extends AbstractUnaryFunctionOp<I, O> implements Ops.Filter.PadInput
{

	@Parameter
	private Dimensions paddedDimensions;

	private BinaryFunctionOp<I, Dimensions, O> paddingIntervalCentered;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		paddingIntervalCentered = (BinaryFunctionOp) Functions.unary(ops(),
			PaddingIntervalCentered.class, Interval.class,
			RandomAccessibleInterval.class, paddedDimensions.getClass());
	}

	/**
	 * The OutOfBoundsFactory used to extend the image
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf;

	@Override
	@SuppressWarnings("unchecked")
	public O compute1(final I input) {

		if (obf == null) {
			obf = new OutOfBoundsConstantValueFactory<T, RandomAccessibleInterval<T>>(
				Util.getTypeFromInterval(input).createVariable());
		}

		Interval inputInterval = paddingIntervalCentered.compute2(input,
			paddedDimensions);

		return (O) Views.interval(Views.extend(input, obf), inputInterval);
	}
}
