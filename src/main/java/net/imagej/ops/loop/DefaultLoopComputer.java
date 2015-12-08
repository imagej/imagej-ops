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

package net.imagej.ops.loop;

import java.util.ArrayList;

import net.imagej.ops.UnaryComputerOp;
import net.imagej.ops.Ops;
import net.imagej.ops.join.DefaultJoinComputers;

import org.scijava.plugin.Plugin;

/**
 * Applies a {@link UnaryComputerOp} multiple times to an image.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Loop.class)
public class DefaultLoopComputer<A> extends
	AbstractLoopComputer<UnaryComputerOp<A, A>, A>
{

	@Override
	public void compute1(final A input, final A output) {
		final int n = getLoopCount();

		final ArrayList<UnaryComputerOp<A, A>> ops = new ArrayList<UnaryComputerOp<A, A>>(n);
		for (int i = 0; i < n; i++)
			ops.add(getOp());

		final DefaultJoinComputers<A> joiner = new DefaultJoinComputers<A>();
		joiner.setOps(ops);
		joiner.setOutputFactory(getOutputFactory());

		joiner.compute1(input, output);
	}

	@Override
	public DefaultLoopComputer<A> getIndependentInstance() {
		final DefaultLoopComputer<A> looper = new DefaultLoopComputer<A>();

		looper.setOp(getOp().getIndependentInstance());
		looper.setOutputFactory(getOutputFactory());

		return looper;
	}
}
