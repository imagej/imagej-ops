
package net.imagej.ops.filter;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;

/**
 * Abstract superclass of all non-linear filters.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <I> type of the input {@link RandomAccessibleInterval}
 * @param <O> type of the output {@link RandomAccessibleInterval}
 */
public abstract class AbstractNonLinearFilter<I, O> extends
	AbstractComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
{

	@Parameter
	protected OpService ops;

	@Parameter
	private Shape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory;

	@Override
	public void compute(final RandomAccessibleInterval<I> input,
		final RandomAccessibleInterval<O> output)
	{
		// optionally extend input if outOfBoundsFactory is set
		RandomAccessibleInterval<I> extInput = input;
		if (outOfBoundsFactory != null) {
			extInput = Views.interval(Views.extend(input, outOfBoundsFactory), input);
		}

		I in = input.randomAccess().get();
		O out = output.randomAccess().get();

		// map computer to neighborhoods
		ops.map(output, extInput, getComputer(in.getClass(), out.getClass()),
			getShape());
	}

	/**
	 * Get the shape (structuring element) used by this filter.
	 * 
	 * @return the shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Set the shape (structuring element) to be used by this filter.
	 * 
	 * @param s the shape
	 */
	public void setShape(Shape s) {
		shape = s;
	}

	/**
	 * @return the {@link ComputerOp} to evaluate for every neighborhood of every
	 *         pixel
	 */
	protected abstract ComputerOp<Iterable<I>, O> getComputer(
		final Class<?> inClass, final Class<?> outClass);

}
