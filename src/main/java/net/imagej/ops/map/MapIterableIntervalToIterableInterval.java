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
import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * {@link MapOp} from {@link IterableInterval} to {@link IterableInterval}.
 * Conforms if the {@link IterableInterval}s have the same IterationOrder.
 * 
 * @author Martin Horn (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Map.class, name = Ops.Map.NAME, priority = Priority.LOW_PRIORITY + 1)
public class MapIterableIntervalToIterableInterval<A, B> extends
	AbstractMapComputer<A, B, IterableInterval<A>, IterableInterval<B>> implements
	Contingent
{

	@Override
	public boolean conforms() {
		return getOutput() == null || isValid(getInput(), getOutput());
	}

	private boolean isValid(final IterableInterval<A> input,
		final IterableInterval<B> output)
	{
		return input.iterationOrder().equals(output.iterationOrder());
	}

	@Override
	public void compute(final IterableInterval<A> input,
		final IterableInterval<B> output)
	{
		if (!isValid(input, output)) {
			throw new IllegalArgumentException(
				"Input and Output don't have the same iteration order!");
		}

		final Cursor<A> inCursor = input.cursor();
		final Cursor<B> outCursor = output.cursor();

		while (inCursor.hasNext()) {
			getOp().compute(inCursor.next(), outCursor.next());
		}
	}
}
