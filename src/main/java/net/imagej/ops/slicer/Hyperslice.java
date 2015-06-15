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

package net.imagej.ops.slicer;

import java.util.Iterator;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
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

/**
 * Helper class to iterate through subsets of {@link RandomAccessibleInterval}s
 * (e.g. {@link Img}s)
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class Hyperslice extends AbstractInterval implements
	IterableInterval<RandomAccessibleInterval<?>>
{

	private final Interval slice;

	private final OpService opService;

	private final RandomAccessibleInterval<?> source;

	private final boolean dropSingleDimensions;

	/**
	 * @param opService {@link OpService} used
	 * @param source {@link RandomAccessibleInterval} which will be virtually
	 *          cropped
	 * @param axesOfInterest axes which define a plane, cube, hypercube, ...! All
	 *          other axes will be iterated.
	 * @param dropSingleDimensions if true, dimensions of size one will be
	 *          discarded in the hyper-sliced images
	 */
	public Hyperslice(final OpService opService,
		final RandomAccessibleInterval<?> source, final int[] axesOfInterest,
		final boolean dropSingleDimensions)
	{
		super(initIntervals(source, axesOfInterest));

		final long[] sliceMin = new long[source.numDimensions()];
		final long[] sliceMax = new long[source.numDimensions()];

		for (int d = 0; d < source.numDimensions(); d++) {
			if (dimension(d) == 1) {
				sliceMin[d] = source.min(d);
				sliceMax[d] = source.max(d);
			}
		}

		this.slice = new FinalInterval(sliceMin, sliceMax);
		this.opService = opService;
		this.source = source;
		this.dropSingleDimensions = dropSingleDimensions;
	}

	/**
	 * @param opService {@link OpService} used
	 * @param source {@link RandomAccessibleInterval} which will be virtually
	 *          cropped
	 * @param axesOfInterest axes which define a plane, cube, hypercube, ...! All
	 *          other axes will be iterated.
	 */
	public Hyperslice(final OpService opService,
		final RandomAccessibleInterval<?> source, final int[] axesOfInterest)
	{
		this(opService, source, axesOfInterest, true);
	}

	// init method
	private static Interval initIntervals(final RandomAccessibleInterval<?> src,
		final int[] axesOfInterest)
	{

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
	public Cursor<RandomAccessibleInterval<?>> cursor() {
		return new HyperSliceCursor(source, opService, this, slice);
	}

	@Override
	public Cursor<RandomAccessibleInterval<?>> localizingCursor() {
		return cursor();
	}

	@Override
	public long size() {
		return Intervals.numElements(this);
	}

	@Override
	public RandomAccessibleInterval<?> firstElement() {
		return cursor().next();
	}

	@Override
	public Object iterationOrder() {
		return new FlatIterationOrder(this);
	}

	@Override
	public Iterator<RandomAccessibleInterval<?>> iterator() {
		return cursor();
	}

	/**
	 * Help class.
	 * 
	 * @author Christian Dietz
	 */
	private class HyperSliceCursor extends IntervalIterator implements
		Cursor<RandomAccessibleInterval<?>>
	{

		private final long[] tmpPosition;
		private final OpService opService;
		private final RandomAccessibleInterval<?> src;
		private final long[] sliceMax;
		private final long[] sliceMin;

		public HyperSliceCursor(final RandomAccessibleInterval<?> src,
			final OpService service, final Interval fixedAxes, final Interval slice)
		{
			super(fixedAxes);

			this.opService = service;
			this.src = src;
			this.tmpPosition = new long[fixedAxes.numDimensions()];
			this.sliceMax = new long[slice.numDimensions()];
			this.sliceMin = new long[slice.numDimensions()];

			slice.max(sliceMax);
			slice.min(sliceMin);
		}

		private HyperSliceCursor(final HyperSliceCursor cursor) {
			super(cursor);

			this.opService = cursor.opService;
			this.src = cursor.src;
			this.sliceMax = cursor.sliceMax;
			this.sliceMin = cursor.sliceMin;
			this.tmpPosition = cursor.tmpPosition;

			// set to the current position
			jumpFwd(cursor.index);
		}

		@Override
		public RandomAccessibleInterval<?> get() {
			localize(tmpPosition);

			final long[] max = tmpPosition.clone();
			final long[] min = tmpPosition.clone();
			for (int d = 0; d < max.length; d++) {
				max[d] += sliceMax[d];
				min[d] += sliceMin[d];
			}

			return (RandomAccessibleInterval<?>) opService.run(Ops.Crop.class, src,
				new FinalInterval(min, max), dropSingleDimensions);
		}

		@Override
		public Sampler<RandomAccessibleInterval<?>> copy() {
			return copyCursor();
		}

		@Override
		public RandomAccessibleInterval<?> next() {
			fwd();
			return get();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supported");
		}

		@Override
		public Cursor<RandomAccessibleInterval<?>> copyCursor() {
			return new HyperSliceCursor(this);
		}
	}

}
