/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

import net.imagej.ops.Ops.Map;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;

public abstract class AbstractNeighborhoodBasedFilter<I, O> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<I>, IterableInterval<O>>
{

	@Parameter
	private Shape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory =
		new OutOfBoundsBorderFactory<>();

	private UnaryComputerOp<Iterable<I>, O> filterOp;

	private UnaryComputerOp<RandomAccessibleInterval<I>, IterableInterval<O>> map;

	@Override
	public void initialize() {
		filterOp = unaryComputer(out().firstElement());
		map = Computers.unary(ops(), Map.class, out(), in(), shape, filterOp);
	}

	@Override
	public void compute(RandomAccessibleInterval<I> input,
		IterableInterval<O> output)
	{
		map.compute(Views.interval(Views.extend(input, outOfBoundsFactory), input),
			output);
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
	 * @return the Computer to map to all neighborhoods of input to output.
	 */
	protected abstract UnaryComputerOp<Iterable<I>, O> unaryComputer(
		final O outType);

}
