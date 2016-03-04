package net.imagej.ops.create.integralImg;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converter;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

/**
 * <p>
 * <i>n</i>-dimensional integral image that stores sums using type {@code O}.
 * Care must be taken that sums do not overflow the capacity of type {@code O}.
 * </p>
 * <p>
 * The integral image will be one pixel larger in each dimension as for easy
 * computation of sums it has to contain "zeros" at the beginning of each
 * dimension
 * </p>
 * <p>
 * The {@link Converter} defines how to convert from Type {@code I} to
 * {@code O}.
 * </p>
 * <p>
 * Sums are done with the precision of {@code I} and then set to the integral
 * image type, which may crop the values according to the type's capabilities.
 * </p>
 * 
 * @param <I> The type of the input image.
 * @param <O> The type of the integral image.
 * @author Stephan Preibisch
 * @author Albert Cardona
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Create.IntegralImg.class, priority = Priority.LOW_PRIORITY)
public class CreateIntegralImgFromImg<I extends RealType<I>, O> extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
	implements Ops.Create.IntegralImg
{
	
	@Parameter(required=false)
	int order = 1;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RandomAccessibleInterval<O> compute1(final RandomAccessibleInterval<I> input)
	{
		// Extend input in each dimension and fill with zeros
		final RandomAccessibleInterval<I> extendedInput = Views.offsetInterval(Views
			.extendZero(input), extendInterval(input));

		// Create integral image
		RandomAccessibleInterval<O> output = (RandomAccessibleInterval) ops().create().img(extendInterval(
			input), DoubleType.class);

		// not enough RAM or disc space
		if (output == null) {
			// TODO Handle this case?
		}

		// Op that sums up values along the computation
		UnaryComputerOp<IterableInterval<I>, IterableInterval<O>> add =
			(UnaryComputerOp) ops().op(Ops.Create.IntegralAdd.class,
				IterableInterval.class, IterableInterval.class);

		// Slicewise addition in one direction
		output = (Img) ops().slicewise(output, extendedInput, add, new int[] { 0 });

		// FIXME Currently, we need a new image per dimension
		RandomAccessibleInterval<O> output2 = (RandomAccessibleInterval) ops().create().img(extendInterval(
			input), DoubleType.class);
		
		// Slicewise addition in the other direction
		output2 = (Img) ops().slicewise(output2, output, add, new int[] { 1 });
		
		return output2;
	}
	
	/**
	 * Extend an interval by one in each dimension (only the minimum)
	 * 
	 * @param interval {@code Interval} that is to be extended and later converted
	 *          to an integral image
	 * @return {@code Interval} extended by one in each dimension
	 */
	private Interval extendInterval(Interval interval) {	
		final long[] imgMinimum = new long[interval.numDimensions()];
		interval.min(imgMinimum);
		final long[] imgMaximum = new long[interval.numDimensions()];
		interval.max(imgMaximum);
		
		for (int d = 0; d < interval.numDimensions(); d++)
		{
			imgMinimum[d] = imgMinimum[d] - 1;		
		}
		
		return new FinalInterval(imgMinimum, imgMaximum);
	}
	
}
