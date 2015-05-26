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

package net.imagej.ops.map;

import net.imagej.ops.Contingent;
import net.imagej.ops.Function;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * {@link Map} using a {@link Function} on {@link RandomAccessibleInterval} and
 * {@link IterableInterval}
 * 
 * @author Martin Horn
 * @author Christian Dietz
 * @author Tim-Oliver Buchholz
 * 
 * @param <A>
 *            mapped on <B>
 * @param <B>
 *            mapped from <A>
 */
@Plugin(type = Op.class, name = Ops.Map.NAME, priority = Priority.LOW_PRIORITY)
public class MapRAIToIterableInterval<A, B>
		extends
		AbstractMapFunction<A, B, RandomAccessibleInterval<A>, IterableInterval<B>>
		implements Contingent {

	@Override
	public IterableInterval<B> compute(final RandomAccessibleInterval<A> input,
			final IterableInterval<B> output) {
		final Cursor<B> cursor = output.localizingCursor();
		final RandomAccess<A> rndAccess = input.randomAccess();

		while (cursor.hasNext()) {
			cursor.fwd();
			rndAccess.setPosition(cursor);
			func.compute(rndAccess.get(), cursor.get());
		}

		return output;
	}

	@Override
	public boolean conforms() {
		return Intervals.equalDimensions(getOutput(), getInput());
	}
}
