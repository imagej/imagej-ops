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
import net.imagej.ops.special.UnaryComputerOp;
import net.imagej.ops.thread.chunker.ChunkerOp;
import net.imagej.ops.thread.chunker.CursorBasedChunk;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Parallelized {@link MapComputer} from {@link IterableInterval} inputs to
 * {@link RandomAccessibleInterval} outputs.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <EI> element type of inputs
 * @param <EO> element type of outputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.LOW_PRIORITY + 2)
public class MapIterableIntervalToRAIParallel<EI, EO> extends
	AbstractMapComputer<EI, EO, IterableInterval<EI>, RandomAccessibleInterval<EO>>
	implements Contingent, Parallel
{

	@Override
	public void compute1(final IterableInterval<EI> input,
		final RandomAccessibleInterval<EO> output)
	{
		ops().run(ChunkerOp.class, new CursorBasedChunk() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				final UnaryComputerOp<EI, EO> safe = getOp().getIndependentInstance();
				final Cursor<EI> cursor = input.localizingCursor();

				setToStart(cursor, startIndex);

				final RandomAccess<EO> rndAccess = output.randomAccess();

				int ctr = 0;
				while (ctr < numSteps) {
					rndAccess.setPosition(cursor);
					safe.compute1(cursor.get(), rndAccess.get());
					cursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, input.size());
	}

	@Override
	public boolean conforms() {
		return out() == null || Intervals.equalDimensions(out(), in());
	}

}
