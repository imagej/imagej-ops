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
import net.imagej.ops.special.inplace.AbstractUnaryInplaceOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Joins a list of {@link UnaryInplaceOp}s.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 */
@Plugin(type = Ops.Join.class)
public class DefaultJoinNInplaces<A> extends AbstractUnaryInplaceOp<A> implements
	JoinNInplaces<A, A>
{

	@Parameter
	private List<? extends UnaryInplaceOp<A, A>> ops;

	// -- UnaryInplaceOp methods --

	@Override
	public void mutate(final A input) {
		for (final UnaryInplaceOp<A, A> inplace : getOps()) {
			inplace.mutate(input);
		}
	}

	// -- JoinNOps methods --

	@Override
	public void setOps(final List<? extends UnaryInplaceOp<A, A>> ops) {
		this.ops = ops;
	}

	@Override
	public List<? extends UnaryInplaceOp<A, A>> getOps() {
		return ops;
	}

	// -- Threadable methods --

	@Override
	public DefaultJoinNInplaces<A> getIndependentInstance() {
		final DefaultJoinNInplaces<A> joiner = new DefaultJoinNInplaces<>();

		final ArrayList<UnaryInplaceOp<A, A>> opsCopy = new ArrayList<>();
		for (final UnaryInplaceOp<A, A> func : getOps()) {
			opsCopy.add(func.getIndependentInstance());
		}

		joiner.setOps(opsCopy);

		return joiner;
	}

}
