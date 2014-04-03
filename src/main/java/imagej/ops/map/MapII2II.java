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

package imagej.ops.map;

import imagej.ops.Contingent;
import imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * {@link Map} from {@link IterableInterval} to {@link IterableInterval}.
 * Conforms if the {@link IterableInterval}s have the same IterationOrder.
 * 
 * @author Martin Horn
 * @author Christian Dietz
 */
@Plugin(type = Op.class, name = Map.NAME, priority = Priority.LOW_PRIORITY + 1)
public class MapII2II<A, B> extends
	AbstractFunctionMap<A, B, IterableInterval<A>, IterableInterval<B>> implements
	Contingent
{

	@Override
	public boolean conforms() {
		return getOutput() == null || isValid(getInput(), getOutput());
	}

	private boolean isValid(final IterableInterval<A> input,
		final IterableInterval<B> output)
	{
		return input.iterationOrder().equals(getOutput().iterationOrder());
	}

	@Override
	public IterableInterval<B> compute(final IterableInterval<A> input,
		final IterableInterval<B> output)
	{
		if (!isValid(input, output)) {
			throw new IllegalArgumentException(
				"Input and Output don't have the same iteration order!");
		}

		final Cursor<A> inCursor = input.cursor();
		final Cursor<B> outCursor = output.cursor();

		while (inCursor.hasNext()) {
			func.compute(inCursor.next(), outCursor.next());
		}

		return output;
	}
}
