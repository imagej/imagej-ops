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
import imagej.ops.UnaryFunction;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz
 * @author Martin Horn
 * @param <A>
 * @param <B>
 */
@Plugin(type = Op.class, name = "map", priority = Priority.LOW_PRIORITY + 3)
public class ThreadedMapperII<A, B> extends AbstractThreadedMapper implements
	Contingent
{

	@Parameter
	private IterableInterval<A> in;

	@Parameter
	private UnaryFunction<A, B> func;

	@Parameter(type = ItemIO.BOTH)
	private IterableInterval<B> out;

	@Override
	public void run() {
		runThreading(in.size());
	}

	@Override
	public boolean conforms() {
		return in.iterationOrder().equals(out.iterationOrder());
	}

	@Override
	protected void runThread(final int firstElement, final int lastElement) {
		final Cursor<A> inCursor = in.cursor();
		inCursor.jumpFwd(firstElement - 1);

		final Cursor<B> outCursor = out.cursor();
		final UnaryFunction<A, B> copy = func.copy();

		int ctr = 0;
		while (inCursor.hasNext() && ctr < lastElement + 1) {
			inCursor.fwd();
			outCursor.fwd();
			copy.compute(inCursor.get(), outCursor.get());
			ctr++;
		}
	}
}
