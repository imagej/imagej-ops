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

package imagej.ops.map.parallel;

import imagej.ops.Contingent;
import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.Parallel;
import imagej.ops.map.AbstractFunctionMap;
import imagej.ops.map.Map;
import imagej.ops.threading.ChunkExecutor;
import imagej.ops.threading.CursorBasedChunkExecutable;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Parallelized {@link FunctionalMap}, which is specialized for the case, that
 * the two incoming {@link IterableInterval}s have the same IterationOrder.
 * 
 * @author Christian Dietz
 * @param <A> mapped on <B>
 * @param <B> mapped from <A>
 */

@Plugin(type = Op.class, name = Map.NAME, priority = Priority.LOW_PRIORITY + 3)
public class FunctionMapIIP<A, B> extends
	AbstractFunctionMap<A, B, IterableInterval<A>, IterableInterval<B>> implements
	Contingent, Parallel
{

	@Parameter
	private OpService opService;

	@Override
	public boolean conforms() {
		return getInput().iterationOrder().equals(getOutput().iterationOrder());
	}

	@Override
	public IterableInterval<B> compute(final IterableInterval<A> input,
		final IterableInterval<B> output)
	{
		opService.run(ChunkExecutor.class, new CursorBasedChunkExecutable() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				final Cursor<A> inCursor = input.cursor();
				final Cursor<B> outCursor = output.cursor();

				setToStart(inCursor, startIndex);
				setToStart(outCursor, startIndex);

				int ctr = 0;
				while (ctr < numSteps) {
					func.compute(inCursor.get(), outCursor.get());
					inCursor.jumpFwd(stepSize);
					outCursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, input.size());

		return output;
	}
}
