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
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Joins a {@link UnaryComputerOp} with an {@link UnaryInplaceOp}.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Join.class)
public class DefaultJoinComputerAndInplace<A, B> extends
	AbstractUnaryComputerOp<A, B> implements JoinComputerAndInplace<A, B, B>
{

	@Parameter
	private UnaryComputerOp<A, B> first;

	@Parameter
	private UnaryInplaceOp<B, B> second;

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
	public UnaryInplaceOp<B, B> getSecond() {
		return second;
	}

	@Override
	public void setSecond(final UnaryInplaceOp<B, B> second) {
		this.second = second;
	}

	// -- Threadable methods --

	@Override
	public DefaultJoinComputerAndInplace<A, B> getIndependentInstance() {

		final DefaultJoinComputerAndInplace<A, B> joiner =
			new DefaultJoinComputerAndInplace<>();

		joiner.setFirst(getFirst().getIndependentInstance());
		joiner.setSecond(getSecond().getIndependentInstance());

		return joiner;
	}

}
