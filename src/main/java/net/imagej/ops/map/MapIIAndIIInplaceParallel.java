/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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
import net.imagej.ops.Ops;
import net.imagej.ops.Parallel;
import net.imagej.ops.special.inplace.BinaryInplaceOp;
import net.imagej.ops.thread.chunker.ChunkerOp;
import net.imagej.ops.thread.chunker.CursorBasedChunk;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * {@link MapBinaryInplace} over 2 {@link IterableInterval}s
 * 
 * @author Leon Yang
 * @param <EA> element type of inputs + outputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.HIGH_PRIORITY + 3)
public class MapIIAndIIInplaceParallel<EA> extends
	AbstractMapBinaryInplace<EA, IterableInterval<EA>> implements Contingent,
	Parallel
{

	@Override
	public boolean conforms() {
		return in1().iterationOrder().equals(in2().iterationOrder());
	}

	@Override
	public void mutate1(final IterableInterval<EA> arg,
		final IterableInterval<EA> in)
	{
		ops().run(ChunkerOp.class, new CursorBasedChunk() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				final Cursor<EA> inCursor = in.cursor();
				final Cursor<EA> argCursor = arg.cursor();
				final BinaryInplaceOp<EA> op = getOp();

				setToStart(inCursor, startIndex);
				setToStart(argCursor, startIndex);

				int ctr = 0;
				while (ctr < numSteps) {
					op.mutate1(argCursor.get(), inCursor.get());
					inCursor.jumpFwd(stepSize);
					argCursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, arg.size());
	}

	@Override
	public void mutate2(final IterableInterval<EA> in,
		final IterableInterval<EA> arg)
	{
		ops().run(ChunkerOp.class, new CursorBasedChunk() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				final Cursor<EA> inCursor = in.cursor();
				final Cursor<EA> argCursor = arg.cursor();
				final BinaryInplaceOp<EA> op = getOp();

				setToStart(inCursor, startIndex);
				setToStart(argCursor, startIndex);

				int ctr = 0;
				while (ctr < numSteps) {
					op.mutate2(inCursor.get(), argCursor.get());
					inCursor.jumpFwd(stepSize);
					argCursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, in.size());
	}

	/*
	 * TODO: this will not work until Java 8 is fully supported by SciJava.
	 * See https://github.com/scijava/scijava-common/pull/218
	
	@Override
	public void mutate1(final IterableInterval<EA> arg,
		final IterableInterval<EA> in)
	{
		final BinaryInplaceOp<EA> safe = getOp().getIndependentInstance();
		mutateDispatch(arg, in, safe::mutate1);
	}
	
	@Override
	public void mutate2(final IterableInterval<EA> in,
		final IterableInterval<EA> arg)
	{
		final BinaryInplaceOp<EA> safe = getOp().getIndependentInstance();
		mutateDispatch(in, arg, safe::mutate2);
	}
	
	private void mutateDispatch(final IterableInterval<EA> first,
		final IterableInterval<EA> second, final BiConsumer<EA, EA> c)
	{
		ops().run(ChunkerOp.class, new CursorBasedChunk() {
	
			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				final Cursor<EA> firstCursor = first.cursor();
				final Cursor<EA> secondCursor = second.cursor();
	
				setToStart(firstCursor, startIndex);
				setToStart(secondCursor, startIndex);
	
				int ctr = 0;
				while (ctr < numSteps) {
					c.accept(firstCursor.get(), secondCursor.get());
					firstCursor.jumpFwd(stepSize);
					secondCursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, first.size());
	
	}
	*/
}
