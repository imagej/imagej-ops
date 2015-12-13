/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

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
