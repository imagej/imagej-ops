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

package imagej.ops.loop;

import imagej.ops.Function;
import imagej.ops.Op;

import org.scijava.plugin.Plugin;

/**
 * Default implementation of a {@link AbstractLoopInplace}
 * 
 * @author Christian Dietz
 */
@Plugin(type = Op.class, name = Loop.NAME)
public class DefaultLoopInplace<I> extends AbstractLoopInplace<I> {

	@Override
	public I compute(final I arg) {
		final int n = getLoopCount();
		final Function<I, I> func = getFunction();
		for (int i = 0; i < n; i++) {
			func.compute(arg, arg);
		}
		return arg;
	}

	@Override
	public DefaultLoopInplace<I> getIndependentInstance() {
		final DefaultLoopInplace<I> looper = new DefaultLoopInplace<I>();
		looper.setFunction(getFunction().getIndependentInstance());
		looper.setLoopCount(getLoopCount());
		return looper;
	}
}
