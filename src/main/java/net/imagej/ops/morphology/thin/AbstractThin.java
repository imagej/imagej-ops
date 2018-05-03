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

package net.imagej.ops.morphology.thin;

import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.view.Views;

/**
 * Thinning Operation
 *
 * @author Andreas Burger, University of Konstanz
 * @author Kyle Harrington, Beth Israel Deaconess Medical Center
 */
public abstract class AbstractThin extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<BitType>, RandomAccessibleInterval<BitType>>
{

	protected ThinningStrategy m_strategy;

	@Override
	public RandomAccessibleInterval<BitType> createOutput(
		final RandomAccessibleInterval<BitType> input)
	{
		final long[] dims = new long[input.numDimensions()];
		input.dimensions(dims);
		final FinalDimensions dimensions = new FinalDimensions(dims);
		return ops().create().img(dimensions, new BitType());
	}

	private void copy(final RandomAccessibleInterval<BitType> source,
		final RandomAccessibleInterval<BitType> target)
	{
		final IterableInterval<BitType> targetIt = Views.iterable(target);
		final IterableInterval<BitType> sourceIt = Views.iterable(source);

		if (sourceIt.iterationOrder().equals(targetIt.iterationOrder())) {
			final Cursor<BitType> targetCursor = targetIt.cursor();
			final Cursor<BitType> sourceCursor = sourceIt.cursor();
			while (sourceCursor.hasNext()) {
				targetCursor.fwd();
				sourceCursor.fwd();
				targetCursor.get().set(sourceCursor.get().get());
			}
		}
		else { // Fallback to random access
			final RandomAccess<BitType> targetRA = target.randomAccess();
			final Cursor<BitType> sourceCursor = sourceIt.localizingCursor();
			while (sourceCursor.hasNext()) {
				sourceCursor.fwd();
				targetRA.setPosition(sourceCursor);
				targetRA.get().set(sourceCursor.get().get());
			}
		}
	}

	@Override
	public void compute(final RandomAccessibleInterval<BitType> input,
		final RandomAccessibleInterval<BitType> output)
	{
		// Create a new image as a buffer to store the thinning image in each
		// iteration.
		// This image and output are swapped each iteration since we need to work on
		// the image
		// without changing it.

		final Img<BitType> buffer = ops().create().img(input, new BitType());

		final IterableInterval<BitType> it1 = Views.iterable(buffer);
		final IterableInterval<BitType> it2 = Views.iterable(output);

		// Extend the buffer in order to be able to iterate care-free later.
		final RandomAccessible<BitType> ra1 = Views.extendBorder(buffer);
		final RandomAccessible<BitType> ra2 = Views.extendBorder(output);

		// Used only in first iteration.
		RandomAccessible<BitType> currRa = Views.extendBorder(input);

		// Create cursors.
		final Cursor<BitType> firstCursor = it1.localizingCursor();
		Cursor<BitType> currentCursor = Views.iterable(input).localizingCursor();
		final Cursor<BitType> secondCursor = it2.localizingCursor();

		// Create pointers to the current and next cursor and set them to Buffer and
		// output respectively.
		Cursor<BitType> nextCursor;
		nextCursor = secondCursor;

		// The main loop.
		boolean changes = true;
		int i = 0;
		// Until no more changes, do:
		final long[] coordinates = new long[currentCursor.numDimensions()];
		while (changes) {
			changes = false;
			// This For-Loop makes sure, that iterations only end on full cycles (as
			// defined by the strategies).
			for (int j = 0; j < m_strategy.getIterationsPerCycle(); ++j) {
				// For each pixel in the image.
				while (currentCursor.hasNext()) {
					// Move both cursors
					currentCursor.fwd();
					nextCursor.fwd();
					// Get the position of the current cursor.
					currentCursor.localize(coordinates);

					// Copy the value of the image currently operated upon.
					final boolean curr = currentCursor.get().get();
					nextCursor.get().set(curr);

					// Only foreground pixels may be thinned
					if (curr) {

						// Ask the strategy whether to flip the foreground pixel or not.
						final boolean flip = m_strategy.removePixel(coordinates, currRa, j);

						// If yes - change and keep track of the change.
						if (flip) {
							nextCursor.get().set(false);
							changes = true;
						}
					}
				}
				// One step of the cycle is finished, notify the strategy.
				m_strategy.afterCycle();

				// Reset the cursors to the beginning and assign pointers for the next
				// iteration.
				currentCursor.reset();
				nextCursor.reset();

				// Keep track of the most recent image. Needed for output.
				if (currRa == ra2) {
					currRa = ra1;
					currentCursor = firstCursor;
					nextCursor = secondCursor;
				}
				else {
					currRa = ra2;
					currentCursor = secondCursor;
					nextCursor = firstCursor;
				}

				// Keep track of iterations.
				++i;
			}
		}

		// Depending on the iteration count, the final image is either in ra1 or
		// ra2. Copy it to output.
		if (i % 2 == 0) {
			// Ra1 points to img1, ra2 points to output.
			copy(buffer, output);
		}
	}

}
