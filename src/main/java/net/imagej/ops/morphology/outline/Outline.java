/*-
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

package net.imagej.ops.morphology.outline;

import java.util.Arrays;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBounds;
import net.imglib2.roi.labeling.BoundingBox;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.util.Util;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

/**
 * The Op creates an output interval where the objects are hollow versions from
 * the input. Rectangles become outlines, solid cubes become surfaces etc.
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Ops.Morphology.Outline.class)
public class Outline<B extends BooleanType<B>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<B>, Boolean, RandomAccessibleInterval<BitType>>
	implements Ops.Morphology.Outline
{

	@Override
	public RandomAccessibleInterval<BitType> createOutput(
		final RandomAccessibleInterval<B> input, final Boolean input2)
	{
		final long[] dims = new long[input.numDimensions()];
		input.dimensions(dims);
		final FinalDimensions dimensions = new FinalDimensions(dims);
		return ops().create().img(dimensions, new BitType());
	}

	/**
	 * Copies the outlines of the objects in the input interval into the output
	 *
	 * @param input a binary interval
	 * @param excludeEdges are elements on stack edges outline or not
	 *          <p>
	 *          For example, a 2D square:<br>
	 *          0 0 0 0<br>
	 *          1 1 1 0<br>
	 *          E 1 1 0<br>
	 *          1 1 1 0<br>
	 *          0 0 0 0<br>
	 *          Element E is removed if parameter true, kept if false
	 *          </p>
	 * @param output outlines of the objects in interval
	 */
	@Override
	public void compute(final RandomAccessibleInterval<B> input,
		final Boolean excludeEdges,
		final RandomAccessibleInterval<BitType> output)
	{
		final Cursor<B> inputCursor = Views.iterable(input).localizingCursor();
		final long[] coordinates = new long[input.numDimensions()];
		final ExtendedRandomAccessibleInterval<B, RandomAccessibleInterval<B>> extendedInput =
			extendInterval(input);
		final RandomAccess<BitType> outputAccess = output.randomAccess();
		while (inputCursor.hasNext()) {
			inputCursor.fwd();
			inputCursor.localize(coordinates);
			if (isOutline(extendedInput, coordinates)) {
				outputAccess.setPosition(coordinates);
				outputAccess.get().set(inputCursor.get().get());
			}
		}
	}

	// region -- Helper methods --
	private ExtendedRandomAccessibleInterval<B, RandomAccessibleInterval<B>>
		extendInterval(RandomAccessibleInterval<B> interval)
	{
		final B type = Util.getTypeFromInterval(interval).createVariable();
		type.set(in2());
		return Views.extendValue(interval, type);
	}

	/**
	 * Creates a view that spans from (x-1, y-1, ... i-1) to (x+1, y+1, ... i+1)
	 * around the given coordinates
	 *
	 * @param interval the space of the coordinates
	 * @param coordinates coordinates (x, y, ... i)
	 * @return a view of a neighbourhood in the space
	 */
	private IntervalView<B> neighbourhoodInterval(
		final ExtendedRandomAccessibleInterval<B, RandomAccessibleInterval<B>> interval,
		final long[] coordinates)
	{
		final int dimensions = interval.numDimensions();
		final BoundingBox box = new BoundingBox(dimensions);
		final long[] minBounds = Arrays.stream(coordinates).map(c -> c - 1)
			.toArray();
		final long[] maxBounds = Arrays.stream(coordinates).map(c -> c + 1)
			.toArray();
		box.update(minBounds);
		box.update(maxBounds);
		return Views.offsetInterval(interval, box);
	}

	/** Checks if any element in the neighbourhood is background */
	private boolean isAnyBackground(final IntervalView<B> neighbourhood) {
		final Cursor<B> cursor = neighbourhood.cursor();
		while (cursor.hasNext()) {
			cursor.fwd();
			if (!cursor.get().get()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if an element is part of the outline of an object
	 *
	 * @param source the location of the element
	 * @param coordinates coordinates of the element
	 * @return true if element is foreground and has at least one background
	 *         neighbour
	 */
	private boolean isOutline(
		final ExtendedRandomAccessibleInterval<B, RandomAccessibleInterval<B>> source,
		final long[] coordinates)
	{
		final OutOfBounds<B> access = source.randomAccess();
		access.setPosition(coordinates);
		if (!access.get().get()) {
			return false;
		}

		final IntervalView<B> neighbourhood = neighbourhoodInterval(source,
			coordinates);
		return isAnyBackground(neighbourhood);
	}
	// endregion
}
