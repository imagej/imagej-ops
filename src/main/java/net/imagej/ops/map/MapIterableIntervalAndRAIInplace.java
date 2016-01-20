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
import net.imagej.ops.special.inplace.BinaryInplace1Op;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * {@link MapBinaryInplace1} over {@link IterableInterval} and
 * {@link RandomAccessibleInterval}
 * 
 * @author Leon Yang
 * @param <EA> element type of first inputs + outputs
 * @param <EI> element type of second inputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.HIGH_PRIORITY)
public class MapIterableIntervalAndRAIInplace<EA, EI> extends
	AbstractMapBinaryInplace1<EA, EI, IterableInterval<EA>, RandomAccessibleInterval<EI>>
	implements Contingent
{

	@Override
	public boolean conforms() {
		return Intervals.equalDimensions(in1(), in2());
	}

	@Override
	public void mutate1(final IterableInterval<EA> arg,
		final RandomAccessibleInterval<EI> in)
	{
		final RandomAccess<EI> inAccess = in.randomAccess();
		final Cursor<EA> argCursor = arg.localizingCursor();
		final BinaryInplace1Op<EA, EI> op = getOp();

		while (argCursor.hasNext()) {
			argCursor.fwd();
			inAccess.setPosition(argCursor);
			op.mutate1(argCursor.get(), inAccess.get());
		}
	}

}
