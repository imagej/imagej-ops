/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

import net.imagej.ops.Ops;
import net.imagej.ops.special.UnaryOutputFactory;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Joins two {@link UnaryComputerOp}s.
 *
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Join.class)
public class DefaultJoin2Computers<A, B, C> extends
	AbstractUnaryComputerOp<A, C> implements Join2Computers<A, B, C>
{

	@Parameter
	private UnaryComputerOp<A, B> first;

	@Parameter
	private UnaryComputerOp<B, C> second;

	@Parameter
	private UnaryOutputFactory<A, B> bufferFactory;

	private B buffer;

	// -- Join2Ops methods --

	@Override
	public UnaryComputerOp<A, B> getFirst() {
		return first;
	}

	@Override
	public void setFirst(final UnaryComputerOp<A, B> first) {
		this.first = first;
	}

	@Override
	public UnaryComputerOp<B, C> getSecond() {
		return second;
	}

	@Override
	public void setSecond(final UnaryComputerOp<B, C> second) {
		this.second = second;
	}

	// -- BufferFactory methods --

	@Override
	public UnaryOutputFactory<A, B> getOutputFactory() {
		return bufferFactory;
	}

	@Override
	public void setOutputFactory(final UnaryOutputFactory<A, B> bufferFactory) {
		this.bufferFactory = bufferFactory;
	}

	@Override
	public B getBuffer(final A input) {
		if (buffer == null) buffer = bufferFactory.createOutput(input);
		return buffer;
	}

	// -- Threadable methods --

	@Override
	public DefaultJoin2Computers<A, B, C> getIndependentInstance() {
		final DefaultJoin2Computers<A, B, C> joiner =
			new DefaultJoin2Computers<>();

		joiner.setFirst(getFirst().getIndependentInstance());
		joiner.setSecond(getSecond().getIndependentInstance());
		joiner.setOutputFactory(getOutputFactory());

		return joiner;
	}

}
