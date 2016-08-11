/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.map.neighborhood;

import net.imagej.ops.OpEnvironment;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Map;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Evaluates a {@link CenterAwareComputerOp} for each {@link Neighborhood} on
 * the input {@link RandomAccessibleInterval} and sets the value of the
 * corresponding pixel on the output {@link IterableInterval}. Similar
 * to {@link DefaultMapNeighborhood}, but passes the center pixel to the op as well.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @author Stefan Helfrich (University of Konstanz)
 * @see OpEnvironment#map(IterableInterval, RandomAccessibleInterval, Shape,
 *      CenterAwareComputerOp)
 * @see CenterAwareComputerOp
 */
@Plugin(type = Ops.Map.class, priority = Priority.LOW_PRIORITY + 21)
public class MapNeighborhoodWithCenter<I, O> extends
	AbstractMapNeighborhood<I, O, RandomAccessibleInterval<I>, IterableInterval<O>, Shape, CenterAwareComputerOp<I, O>>
{

	@Parameter(required = false)
	private OutOfBoundsFactory<I, RandomAccessibleInterval<I>> oobFactory;

	private BinaryComputerOp<IterableInterval<Neighborhood<I>>, RandomAccessibleInterval<I>, IterableInterval<O>> map;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		map = (BinaryComputerOp) Computers.binary(ops(), Map.class, IterableInterval.class,
			IterableInterval.class, RandomAccessibleInterval.class, getOp());
	}

	@Override
	public void compute2(final RandomAccessibleInterval<I> in1, final Shape in2,
		final IterableInterval<O> out)
	{
		final RandomAccessibleInterval<I> extended = oobFactory == null ? in1
			: Views.interval(Views.extend(in1, oobFactory), in1);
		map.compute2(in2.neighborhoodsSafe(extended), in1, out);
	}

}
