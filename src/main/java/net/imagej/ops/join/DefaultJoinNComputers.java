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

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.special.UnaryOutputFactory;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Joins a list of {@link UnaryComputerOp}s.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 */
@Plugin(type = Ops.Join.class)
public class DefaultJoinNComputers<A> extends AbstractUnaryComputerOp<A, A>
	implements JoinNComputers<A>
{

	@Parameter
	private List<? extends UnaryComputerOp<A, A>> ops;

	@Parameter
	private UnaryOutputFactory<A, A> outputFactory;

	private A buffer;

	// -- JoinNOps methods --

	@Override
	public List<? extends UnaryComputerOp<A,A>> getOps() {
		return ops;
	}

	@Override
	public void setOps(final List<? extends UnaryComputerOp<A,A>> ops) {
		this.ops = ops;
	}

	// -- BufferFactory methods --

	@Override
	public UnaryOutputFactory<A, A> getOutputFactory() {
		return outputFactory;
	}

	@Override
	public void setOutputFactory(final UnaryOutputFactory<A, A> outputFactory) {
		this.outputFactory = outputFactory;
	}

	@Override
	public A getBuffer(final A input) {
		if (buffer == null) {
			buffer = outputFactory.createOutput(input);
		}
		return buffer;
	}

	// -- Threadable methods --

	@Override
	public DefaultJoinNComputers<A> getIndependentInstance() {
		final DefaultJoinNComputers<A> joiner = new DefaultJoinNComputers<>();

		final ArrayList<UnaryComputerOp<A, A>> opsCopy = new ArrayList<>();
		for (final UnaryComputerOp<A, A> op : getOps()) {
			opsCopy.add(op.getIndependentInstance());
		}

		joiner.setOps(opsCopy);
		joiner.setOutputFactory(getOutputFactory());

		return joiner;
	}

}
