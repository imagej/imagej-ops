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

import imagej.ops.AbstractFunction;
import imagej.ops.Function;
import imagej.ops.OutputFactory;

import org.scijava.plugin.Parameter;

/**
 * Abstract implementation of a {@link LoopFunction}.
 * 
 * @author Christian Dietz
 */
public abstract class AbstractLoopFunction<F extends Function<I, I>, I> extends
	AbstractFunction<I, I> implements LoopFunction<I>
{

	/** Function to loop. */
	@Parameter
	private Function<I, I> function;

	/** Buffer for intermediate results. */
	@Parameter
	private OutputFactory<I, I> bufferFactory;

	/** Number of iterations. */
	@Parameter
	private int n;

	public OutputFactory<I, I> getBufferFactory() {
		return bufferFactory;
	}

	public void setBufferFactory(final OutputFactory<I, I> bufferFactory) {
		this.bufferFactory = bufferFactory;
	}

	@Override
	public Function<I, I> getFunction() {
		return function;
	}

	@Override
	public void setFunction(final Function<I, I> function) {
		this.function = function;
	}
	
	@Override
	public int getLoopCount() {
		return n;
	}

	@Override
	public void setLoopCount(final int n) {
		this.n = n;
	}
}
