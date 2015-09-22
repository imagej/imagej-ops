
package net.imagej.ops.filter;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.ComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;

public abstract class AbstractNeighborhoodBasedFilter<I, O> extends
	AbstractComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
{

	@Parameter
	private Shape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory;

	@Override
	public void compute(RandomAccessibleInterval<I> input,
		RandomAccessibleInterval<O> output)
	{
		// optionally extend input if outOfBoundsFactory is set
		RandomAccessibleInterval<I> extInput = input;
		if (outOfBoundsFactory != null) {
			extInput = Views.interval(Views.extend(input, outOfBoundsFactory), input);
		}

		I in = input.randomAccess().get();
		O out = output.randomAccess().get();

		// map computer to neighborhoods
		ops().map(output, extInput, getComputer(in.getClass(), out.getClass()),
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
	 * @param inClass Class of the type in the input
	 *          {@link RandomAccessibleInterval}
	 * @param outClass Class of the type in the output
	 *          {@link RandomAccessibleInterval}
	 * @return the Computer to map to all neighborhoods of input to output.
	 */
	protected abstract ComputerOp<Iterable<I>, O> getComputer(final Class<?> inClass,
		final Class<?> outClass);

}
