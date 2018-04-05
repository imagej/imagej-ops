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

package net.imagej.ops.slice;

import java.util.Iterator;

import net.imglib2.AbstractInterval;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.FlatIterationOrder;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.Sampler;
import net.imglib2.img.Img;
import net.imglib2.iterator.IntervalIterator;
import net.imglib2.util.Intervals;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

/**
 * Helper class to iterate through subsets of {@link RandomAccessibleInterval}s
 * (e.g. {@link Img}s)
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public class SlicesII<T> extends AbstractInterval implements IterableInterval<RandomAccessibleInterval<T>> {

	private final Interval slice;

	private final RandomAccessibleInterval<T> source;

	private boolean dropSingltonDimensions;

	/**
	 * @param source
	 *            {@link RandomAccessibleInterval} which will be virtually
	 *            cropped
	 * @param axesOfInterest
	 *            axes which define a plane, cube, hypercube, ...! All other
	 *            axes will be iterated.
	 * @param dropSingletonDimensions
	 *            if true, dimensions of size one will be discarded in the
	 *            sliced images
	 */
	public SlicesII(final RandomAccessibleInterval<T> source, final int[] axesOfInterest,
			final boolean dropSingletonDimensions) {
		super(initIntervals(source, axesOfInterest));

		final long[] sliceMin = new long[source.numDimensions()];
		final long[] sliceMax = new long[source.numDimensions()];

		for (int d = 0; d < source.numDimensions(); d++) {
			if (dimension(d) == 1) {
				sliceMin[d] = source.min(d);
				sliceMax[d] = source.max(d);
			}
		}

		this.dropSingltonDimensions = dropSingletonDimensions;
		this.slice = new FinalInterval(sliceMin, sliceMax);
		this.source = source;
	}

	/**
	 * @param source
	 *            {@link RandomAccessibleInterval} which will be virtually
	 *            cropped
	 * @param axesOfInterest
	 *            axes which define a plane, cube, hypercube, ...! All other
	 *            axes will be iterated.
	 */
	public SlicesII(final RandomAccessibleInterval<T> source, final int[] axesOfInterest) {
		this(source, axesOfInterest, true);
	}

	// init method
	private static Interval initIntervals(final Interval src, final int[] axesOfInterest) {

		final long[] dimensionsToIterate = new long[src.numDimensions()];
		src.dimensions(dimensionsToIterate);

		// determine axis to iterate
		for (int i = 0; i < src.numDimensions(); i++) {
			for (int j = 0; j < axesOfInterest.length; j++) {

				if (axesOfInterest[j] == i) {
					dimensionsToIterate[i] = 1;
					break;
				}
			}
		}

		return new FinalInterval(dimensionsToIterate);
	}

	@Override
	public Cursor<RandomAccessibleInterval<T>> cursor() {
		return new SlicesIICursor(source, this, slice);
	}

	@Override
	public Cursor<RandomAccessibleInterval<T>> localizingCursor() {
		return cursor();
	}

	@Override
	public long size() {
		return Intervals.numElements(this);
	}

	@Override
	public RandomAccessibleInterval<T> firstElement() {
		return cursor().next();
	}

	@Override
	public Object iterationOrder() {
		return new FlatIterationOrder(this);
	}

	@Override
	public Iterator<RandomAccessibleInterval<T>> iterator() {
		return cursor();
	}

	/**
	 * Help class.
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	private class SlicesIICursor extends IntervalIterator implements Cursor<RandomAccessibleInterval<T>> {

		private final long[] tmpPosition;
		private final RandomAccessibleInterval<T> src;
		private final long[] sliceDims;
		private final long[] sliceOffset;

		public SlicesIICursor(final RandomAccessibleInterval<T> src, final Interval fixedAxes, final Interval slice) {
			super(fixedAxes);

			this.src = src;
			this.tmpPosition = new long[fixedAxes.numDimensions()];
			this.sliceDims = new long[slice.numDimensions()];
			this.sliceOffset = new long[slice.numDimensions()];

			slice.dimensions(sliceDims);
			slice.min(sliceOffset);
		}

		private SlicesIICursor(final SlicesIICursor cursor) {
			super(cursor);

			this.src = cursor.src;
			this.sliceDims = cursor.sliceDims;
			this.sliceOffset = cursor.sliceOffset;
			this.tmpPosition = cursor.tmpPosition;

			// set to the current position
			jumpFwd(cursor.index);
		}

		@Override
		public RandomAccessibleInterval<T> get() {
			localize(tmpPosition);

			final long[] offset = tmpPosition.clone();
			for (int d = 0; d < max.length; d++) {
				offset[d] += sliceOffset[d];
			}

			final IntervalView<T> res = Views.offsetInterval(src, offset, sliceDims);

			return dropSingltonDimensions ? Views.dropSingletonDimensions(res) : res;
		}

		@Override
		public Sampler<RandomAccessibleInterval<T>> copy() {
			return copyCursor();
		}

		@Override
		public RandomAccessibleInterval<T> next() {
			fwd();
			return get();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supported");
		}

		@Override
		public Cursor<RandomAccessibleInterval<T>> copyCursor() {
			return new SlicesIICursor(this);
		}
	}

}
