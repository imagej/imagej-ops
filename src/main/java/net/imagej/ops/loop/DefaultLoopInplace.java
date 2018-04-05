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
import net.imagej.ops.special.inplace.AbstractUnaryInplaceOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of a {@link LoopInplace}.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 */
@Plugin(type = Ops.Loop.class)
public class DefaultLoopInplace<A> extends AbstractUnaryInplaceOp<A> implements
	LoopInplace<A, A>
{

	@Parameter
	private UnaryInplaceOp<A, A> op;

	@Parameter
	private int n;

	// -- LoopOp methods --

	@Override
	public UnaryInplaceOp<A, A> getOp() {
		return op;
	}

	@Override
	public void setOp(final UnaryInplaceOp<A, A> op) {
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
	public DefaultLoopInplace<A> getIndependentInstance() {
		final DefaultLoopInplace<A> looper = new DefaultLoopInplace<>();
		looper.setOp(getOp().getIndependentInstance());
		looper.setLoopCount(getLoopCount());
		return looper;
	}

}
