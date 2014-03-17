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

package imagej.ops.descriptors;

import imagej.ops.Op;
import imagej.ops.descriptors.DefaultDescriptorService.InputUpdateListeners;

import java.util.List;

/**
 * Descriptor for which all dependencies were resolved by the
 * {@link DefaultDescriptorService}. On update, all dependent {@link Op}s will
 * be recalculated.
 * 
 * @author Christian Dietz
 * 
 * @param <O> type of resulting op
 * @param <I> type of input
 */
public class ResolvedDescriptor<O extends Op, I> {

	private final O op;
	private final List<InputUpdateListeners> listeners;

	/**
	 * @param op The {@link Op} with all dependencies resolved.
	 * @param listeners
	 */
	protected ResolvedDescriptor(final O op,
		final List<InputUpdateListeners> listeners)
	{
		this.listeners = listeners;
		this.op = op;
	}

	/**
	 * Update descriptor and all dependent {@link Op}s.
	 * 
	 * @param input
	 * @return
	 */
	public O update(final I input) {

		for (final InputUpdateListeners listener : listeners) {
			listener.update(input);
		}

		op.run();

		return op;
	}
}
