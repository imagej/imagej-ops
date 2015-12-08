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

package net.imagej.ops.join;

import net.imagej.ops.AbstractUnaryComputerOp;
import net.imagej.ops.UnaryOutputFactory;
import net.imagej.ops.UnaryComputerOp;

import org.scijava.plugin.Parameter;

/**
 * Abstract superclass of {@link JoinComputerAndComputer} implementations.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public abstract class AbstractJoinComputerAndComputer<A, B, C, C1 extends UnaryComputerOp<A, B>, C2 extends UnaryComputerOp<B, C>>
	extends AbstractUnaryComputerOp<A, C> implements
	JoinComputerAndComputer<A, B, C, C1, C2>
{

	@Parameter
	private C1 first;

	@Parameter
	private C2 second;

	@Parameter(required = false)
	private UnaryOutputFactory<A, B> outputFactory;

	private B buffer;

	public B getBuffer(final A input) {
		if (buffer == null) buffer = outputFactory.createOutput(input);
		return buffer;
	}

	public UnaryOutputFactory<A, B> getOutputFactory() {
		return outputFactory;
	}

	public void setOutputFactory(final UnaryOutputFactory<A, B> outputFactory) {
		this.outputFactory = outputFactory;
	}

	@Override
	public C1 getFirst() {
		return first;
	}

	@Override
	public void setFirst(final C1 first) {
		this.first = first;
	}

	@Override
	public C2 getSecond() {
		return second;
	}

	@Override
	public void setSecond(final C2 second) {
		this.second = second;
	}

}
