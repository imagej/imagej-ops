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

import java.util.ArrayList;

import net.imagej.ops.join.DefaultJoinNComputers;
import net.imagej.ops.special.UnaryOutputFactory;
import net.imagej.ops.special.computer.UnaryComputerOp;

/**
 * Loops over an injected {@link UnaryComputerOp}. A {@link LoopComputer}
 * applies a {@link UnaryComputerOp} n-times to an input.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public interface LoopComputer<I> extends UnaryComputerOp<I, I>,
	LoopOp<UnaryComputerOp<I, I>>
{

	UnaryOutputFactory<I, I> getOutputFactory();

	void setOutputFactory(UnaryOutputFactory<I, I> outputFactory);

	// -- UnaryComputerOp methods --

	@Override
	default void compute(final I input, final I output) {
		final int n = getLoopCount();

		final ArrayList<UnaryComputerOp<I, I>> ops = new ArrayList<>(n);
		for (int i = 0; i < n; i++)
			ops.add(getOp());

		final DefaultJoinNComputers<I> joiner = new DefaultJoinNComputers<>();
		joiner.setOps(ops);
		joiner.setOutputFactory(getOutputFactory());

		joiner.compute(input, output);
	}

}
