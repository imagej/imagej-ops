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

package net.imagej.ops.map.neighborhood;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.util.ValuePair;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Evaluates a {@link CenterAwareComputerOp} for each {@link Neighborhood} on
 * the input {@link RandomAccessibleInterval} and sets the value of the
 * corresponding pixel on the output {@link RandomAccessibleInterval}. Similar
 * to {@link MapNeighborhood}, but passes the center pixel to the op aswell.
 * 
 * @author Jonathan Hale
 * @see OpService#map(RandomAccessibleInterval, RandomAccessibleInterval,
 *      CenterAwareComputerOp, Shape)
 * @see CenterAwareComputerOp
 */
@Plugin(type = Op.class, name = Ops.Map.NAME, priority = Priority.LOW_PRIORITY)
public class MapNeighborhoodWithCenter<I, O>
	extends
	AbstractMapCenterAwareComputer<I, O, RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
{

	@Parameter
	private Shape shape;

	@Parameter
	private OpService ops;

	@Override
	public void compute(final RandomAccessibleInterval<I> input,
		final RandomAccessibleInterval<O> output)
	{
		final IterableInterval<Neighborhood<I>> neighborhoods =
			shape.neighborhoods(input);

		final Cursor<Neighborhood<I>> cNeigh = neighborhoods.localizingCursor();

		final RandomAccess<I> raIn = input.randomAccess();
		final RandomAccess<O> raOut = output.randomAccess();

		final CenterAwareComputerOp<I, O> op = getOp();

		while (cNeigh.hasNext()) {
			Neighborhood<I> neigh = cNeigh.next();

			raIn.setPosition(cNeigh);
			raOut.setPosition(cNeigh);

			op.compute(new ValuePair<I, Iterable<I>>(raIn.get(), neigh), raOut.get());
		}

		// TODO: threaded map neighborhood
		// TODO: optimization with integral images, if there is a rectangular
		// neighborhood
		// TODO: provide threaded implementation and specialized ones for
		// rectangular neighborhoods (using integral images)
	}

}
