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

import net.imagej.ops.special.BinaryOp;
import net.imagej.ops.special.InplaceOp;
import net.imagej.ops.thread.chunker.ChunkerOp;
import net.imagej.ops.thread.chunker.CursorBasedChunk;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

/**
 * {@link MapBinaryInplace} over 2 {@link IterableInterval}s
 * 
 * @author Leon Yang
 * @param <EI1> element type of first inputs
 * @param <EI2> element type of second inputs
 * @param <EO> element type of outputs
 */
public class MapIIAndIIInplaceParallel<EI1, EI2, EO> extends
	AbstractMapBinaryInplace<EI1, EI2, EO, IterableInterval<EI1>, IterableInterval<EI2>, IterableInterval<EO>>
{

	@Override
	public boolean conforms() {
		if (!super.conforms()) return false;
		return in1().iterationOrder().equals(in2().iterationOrder());
	}

	@Override
	public void mutate(IterableInterval<EO> arg) {
		ops().run(ChunkerOp.class, new CursorBasedChunk() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				final BinaryOp<EI1, EI2, EO> safe = getOp().getIndependentInstance();
				@SuppressWarnings("unchecked")
				final InplaceOp<EO> inplace = (InplaceOp<EO>) safe;
				final EI1 tmpIn1 = safe.in1();
				final EI2 tmpIn2 = safe.in2();
				final Cursor<EI1> in1Cursor = in1().cursor();
				final Cursor<EI2> in2Cursor = in2().cursor();
				final Cursor<EO> argCursor = arg().cursor();

				setToStart(in1Cursor, startIndex);
				setToStart(in2Cursor, startIndex);
				setToStart(argCursor, startIndex);

				int ctr = 0;
				while (ctr < numSteps) {
					final EI1 i1 = in1Cursor.get();
					final EI2 i2 = in2Cursor.get();
					final EO a = argCursor.get();
					safe.setInput1(i1);
					safe.setInput2(i2);
					inplace.mutate(a);
					in1Cursor.jumpFwd(stepSize);
					in2Cursor.jumpFwd(stepSize);
					argCursor.jumpFwd(stepSize);
					ctr++;
				}
				safe.setInput1(tmpIn1);
				safe.setInput2(tmpIn2);
			}
		}, arg.size());
	}

}
