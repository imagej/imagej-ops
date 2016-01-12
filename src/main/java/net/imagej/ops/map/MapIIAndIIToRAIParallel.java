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
import net.imagej.ops.special.BinaryComputerOp;
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
 * {@link RandomAccessibleInterval} outputs. The {@link IterableInterval}s must
 * have the same iteration order, and the inputs and outputs must have the same
 * dimensions.
 * 
 * @author Leon Yang
 * @param <EI1> element type of first inputs
 * @param <EI2> element type of second inputs
 * @param <EO> element type of outputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.LOW_PRIORITY + 4)
public class MapIIAndIIToRAIParallel<EI1, EI2, EO> extends
	AbstractMapBinaryComputer<EI1, EI2, EO, IterableInterval<EI1>, IterableInterval<EI2>, RandomAccessibleInterval<EO>>
	implements Contingent
{

	@Override
	public boolean conforms() {
		if (!in1().iterationOrder().equals(in2().iterationOrder())) return false;

		if (out() == null) return true;
		return Intervals.equalDimensions(in1(), out());
	}

	@Override
	public void compute2(final IterableInterval<EI1> input1,
		final IterableInterval<EI2> input2,
		final RandomAccessibleInterval<EO> output)
	{
		ops().run(ChunkerOp.class, new CursorBasedChunk() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				final BinaryComputerOp<EI1, EI2, EO> safe = getOp()
					.getIndependentInstance();

				final Cursor<EI1> in1Cursor = input1.localizingCursor();
				final Cursor<EI2> in2Cursor = input2.cursor();
				final RandomAccess<EO> outAccess = output.randomAccess();

				setToStart(in1Cursor, startIndex);
				setToStart(in2Cursor, startIndex);

				int ctr = 0;
				while (ctr < numSteps) {
					outAccess.setPosition(in1Cursor);
					safe.compute2(in1Cursor.get(), in2Cursor.get(), outAccess.get());
					in1Cursor.jumpFwd(stepSize);
					in2Cursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, input1.size());
	}
}
