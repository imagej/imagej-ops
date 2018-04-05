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

package net.imagej.ops.loop;

import net.imagej.ops.Ops;
import net.imagej.ops.special.UnaryOutputFactory;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Applies a {@link UnaryComputerOp} multiple times to an image.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Loop.class)
public class DefaultLoopComputer<I> extends AbstractUnaryComputerOp<I, I>
	implements LoopComputer<I>
{

	@Parameter
	private UnaryComputerOp<I, I> op;

	@Parameter
	private UnaryOutputFactory<I, I> outputFactory;

	@Parameter
	private int n;

	// -- LoopComputer methods --

	@Override
	public UnaryOutputFactory<I, I> getOutputFactory() {
		return outputFactory;
	}

	@Override
	public void setOutputFactory(final UnaryOutputFactory<I, I> outputFactory) {
		this.outputFactory = outputFactory;
	}

	// -- LoopOp methods --

	@Override
	public UnaryComputerOp<I, I> getOp() {
		return op;
	}

	@Override
	public void setOp(final UnaryComputerOp<I, I> op) {
		this.op = op;
	}

	@Override
	public int getLoopCount() {
		return n;
	}

	@Override
	public void setLoopCount(final int n) {
		this.n = n;
	}

	// -- Threadable methods --

	@Override
	public DefaultLoopComputer<I> getIndependentInstance() {
		final DefaultLoopComputer<I> looper = new DefaultLoopComputer<>();

		looper.setOp(getOp().getIndependentInstance());
		looper.setOutputFactory(getOutputFactory());

		return looper;
	}

}
