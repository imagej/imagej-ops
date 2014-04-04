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

package imagej.ops.join;

import imagej.ops.AbstractFunction;
import imagej.ops.Function;
import imagej.ops.OutputFactory;

import org.scijava.plugin.Parameter;

/**
 * Abstract superclass of {@link JoinFunctionFunction} implementations.
 * 
 * @author Christian Dietz
 */
public abstract class AbstractJoinFunctionAndFunction<A, B, C, F1 extends Function<A, B>, F2 extends Function<B, C>>
	extends AbstractFunction<A, C> implements
	JoinFunctionFunction<A, B, C, F1, F2>
{

	@Parameter
	protected F1 first;

	@Parameter
	protected F2 second;

	@Parameter
	private OutputFactory<A, B> bufferFactory;

	private B buffer;

	public B getBuffer(final A input) {
		if (buffer == null) buffer = bufferFactory.create(input);
		return buffer;
	}

	public void setBufferFactory(final OutputFactory<A, B> bufferFactory) {
		this.bufferFactory = bufferFactory;
	}

	@Override
	public F1 getFirst() {
		return first;
	}

	@Override
	public void setFirst(final F1 first) {
		this.first = first;
	}

	@Override
	public F2 getSecond() {
		return second;
	}

	@Override
	public void setSecond(final F2 second) {
		this.second = second;
	}

}
