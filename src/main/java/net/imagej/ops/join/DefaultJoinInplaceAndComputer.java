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
 * Joins an {@link UnaryInplaceOp} with a {@link UnaryComputerOp}.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Join.class)
public class DefaultJoinInplaceAndComputer<A, B> extends
	AbstractUnaryComputerOp<A, B> implements JoinInplaceAndComputer<A, A, B>
{

	@Parameter
	private UnaryInplaceOp<A, A> first;

	@Parameter
	private UnaryComputerOp<A, B> second;

	// -- Join2Ops methods --

	@Override
	public UnaryInplaceOp<A, A> getFirst() {
		return first;
	}

	@Override
	public void setFirst(final UnaryInplaceOp<A, A> first) {
		this.first = first;
	}

	@Override
	public UnaryComputerOp<A,B> getSecond() {
		return second;
	}

	@Override
	public void setSecond(final UnaryComputerOp<A,B> second) {
		this.second = second;
	}

	// -- Threadable methods --

	@Override
	public DefaultJoinInplaceAndComputer<A, B> getIndependentInstance() {
		final DefaultJoinInplaceAndComputer<A, B> joiner =
			new DefaultJoinInplaceAndComputer<>();

		joiner.setFirst(getFirst().getIndependentInstance());
		joiner.setSecond(getSecond().getIndependentInstance());

		return joiner;
	}

}
