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
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.Sampler;
import net.imglib2.iterator.IntervalIterator;

/**
 * @author Christian Dietz
 */
public class SliceCursor extends IntervalIterator implements
		Cursor<RandomAccessibleInterval<?>> {

	private final long[] tmpPosition;
	private final OpService opService;
	private final RandomAccessibleInterval<?> src;
	private final long[] hyperSliceMax;

	public SliceCursor(final RandomAccessibleInterval<?> src,
			final OpService service, final Interval fixed,
			final Interval hyperSlice) {
		super(fixed);

		this.opService = service;
		this.src = src;

		this.hyperSliceMax = new long[hyperSlice.numDimensions()];
		hyperSlice.max(hyperSliceMax);

		this.tmpPosition = new long[fixed.numDimensions()];
	}

	@Override
	public RandomAccessibleInterval<?> get() {
		return null;
	}

	@Override
	public Sampler<RandomAccessibleInterval<?>> copy() {
		return null;
	}

	@Override
	public RandomAccessibleInterval<?> next() {
		fwd();
		localize(tmpPosition);
		return (RandomAccessibleInterval<?>) opService.run("hyperslicer", src,
				new FinalInterval(tmpPosition, hyperSliceMax));
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public Cursor<RandomAccessibleInterval<?>> copyCursor() {
		return null;
	}
}
