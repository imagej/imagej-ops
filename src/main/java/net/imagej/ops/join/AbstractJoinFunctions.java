/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
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

package net.imagej.ops.join;

import java.util.List;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Function;
import net.imagej.ops.OutputFactory;

import org.scijava.plugin.Parameter;

/**
 * Abstract superclass of {@link JoinFunctions}s.
 * 
 * @author Christian Dietz
 * @author Curtis Rueden
 */
public abstract class AbstractJoinFunctions<A, F extends Function<A, A>>
	extends AbstractFunction<A, A> implements JoinFunctions<A, F>
{

	/** List of functions to be joined. */
	@Parameter
	private List<? extends F> functions;

	@Parameter
	private OutputFactory<A, A> bufferFactory;

	private A buffer;

	@Override
	public OutputFactory<A, A> getBufferFactory() {
		return bufferFactory;
	}

	@Override
	public void setBufferFactory(final OutputFactory<A, A> bufferFactory) {
		this.bufferFactory = bufferFactory;
	}

	@Override
	public List<? extends F> getFunctions() {
		return functions;
	}

	@Override
	public void setFunctions(final List<? extends F> functions) {
		this.functions = functions;
	}

	/**
	 * @param input helping to create the buffer
	 * @return the buffer which can be used for the join.
	 */
	protected A getBuffer(final A input) {
		if (buffer == null) {
			buffer = bufferFactory.create(input);
		}
		return buffer;
	}

}
