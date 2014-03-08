/*
 * #%L
 * A framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package imagej.ops.slicer;

import imagej.ops.OpService;

import java.util.Arrays;
import java.util.Iterator;

import net.imglib2.AbstractInterval;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.FlatIterationOrder;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.IterableRealInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;

/**
 * @author Christian Dietz
 */
public class SliceIterableInterval extends AbstractInterval implements
	IterableInterval<RandomAccessibleInterval<?>>
{

	private final Interval hyperSlice;

	private final OpService opService;

	private final RandomAccessibleInterval<?> source;

	public SliceIterableInterval(final OpService opService,
		final RandomAccessibleInterval<?> source, final int[] axesOfInterest)
	{
		super(initIntervals(source, axesOfInterest));

		final long[] hyperSliceDims = new long[source.numDimensions()];

		for (int d = 0; d < source.numDimensions(); d++) {
			if (dimension(d) == 1) {
				hyperSliceDims[d] = source.dimension(d);
			}
			else {
				hyperSliceDims[d] = 1;
			}
		}

		this.hyperSlice = new FinalInterval(hyperSliceDims);
		this.opService = opService;
		this.source = source;
	}

	// init method
	private static Interval initIntervals(final RandomAccessibleInterval<?> src,
		final int[] axesOfInterest)
	{

		final long[] dimensionsToIterate = new long[src.numDimensions()];

		Arrays.fill(dimensionsToIterate, 1l);

		// determine axis to iterate
		int k = 0;
		for (int i = 0; i < src.numDimensions(); i++) {
			boolean selected = false;
			for (int j = 0; j < axesOfInterest.length; j++) {

				if (axesOfInterest[j] == i) {
					selected = true;
				}

				if (!selected) {
					dimensionsToIterate[k] = src.dimension(i);
					k++;
				}
			}
		}

		return new FinalInterval(dimensionsToIterate);
	}

	@Override
	public Cursor<RandomAccessibleInterval<?>> cursor() {
		return new SliceCursor(source, opService, this, hyperSlice);
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
	public boolean equalIterationOrder(final IterableRealInterval<?> f) {
		return iterationOrder().equals(f);
	}

	@Override
	public Iterator<RandomAccessibleInterval<?>> iterator() {
		return cursor();
	}

}
