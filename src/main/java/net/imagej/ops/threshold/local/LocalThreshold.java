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

package net.imagej.ops.threshold.local;

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.threshold.local.LocalThresholdMethod.Pair;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn
 */
@Plugin(type = Op.class, name = Ops.Threshold.NAME)
public class LocalThreshold<T extends RealType<T>>
	extends
	AbstractStrictFunction<RandomAccessibleInterval<T>, RandomAccessibleInterval<BitType>>
	implements Ops.Threshold
{

	@Parameter
	private LocalThresholdMethod<T> method;

	@Parameter
	private Shape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds;

	@Override
	public RandomAccessibleInterval<BitType>
		compute(RandomAccessibleInterval<T> input,
			RandomAccessibleInterval<BitType> output)
	{
		// TODO: provide threaded implementation and specialized ones for
		// rectangular neighborhoods (using integral images)
		RandomAccessibleInterval<T> extInput =
			Views.interval(Views.extend(input, outOfBounds), input);
		Iterable<Neighborhood<T>> neighborhoods = shape.neighborhoodsSafe(extInput);
		final Cursor<T> inCursor = Views.flatIterable(input).cursor();
		final Cursor<BitType> outCursor = Views.flatIterable(output).cursor();
		Pair<T> pair = new Pair<T>();
		for (final Neighborhood<T> neighborhood : neighborhoods) {
			pair.neighborhood = neighborhood;
			pair.pixel = inCursor.next();
			method.compute(pair, outCursor.next());
		}
		return output;
	}
}
