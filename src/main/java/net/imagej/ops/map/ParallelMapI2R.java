/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
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

package net.imagej.ops.map;

import net.imagej.ops.Function;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Parallel;
import net.imagej.ops.chunker.Chunker;
import net.imagej.ops.chunker.CursorBasedChunk;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Parallelized {@link Map}.
 * 
 * @author Christian Dietz
 * @param <A> mapped on {@code <B>}
 * @param <B> mapped from {@code <A>}
 */
@Plugin(type = Op.class, name = Map.NAME, priority = Priority.LOW_PRIORITY + 2)
public class ParallelMapI2R<A, B> extends
	AbstractFunctionMap<A, B, IterableInterval<A>, RandomAccessibleInterval<B>>
	implements Parallel
{

	@Parameter
	private OpService opService;

	@Override
	public RandomAccessibleInterval<B> compute(final IterableInterval<A> input,
		final RandomAccessibleInterval<B> output)
	{
		opService.run(Chunker.class, new CursorBasedChunk() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				final Function<A, B> safe = func.getIndependentInstance();
				final Cursor<A> cursor = input.localizingCursor();

				setToStart(cursor, startIndex);

				final RandomAccess<B> rndAccess = output.randomAccess();

				int ctr = 0;
				while (ctr < numSteps) {
					rndAccess.setPosition(cursor);
					safe.compute(cursor.get(), rndAccess.get());
					cursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, input.size());

		return output;
	}
}
