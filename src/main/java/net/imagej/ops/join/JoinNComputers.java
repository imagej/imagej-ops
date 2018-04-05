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

import java.util.Iterator;
import java.util.List;

import net.imagej.ops.special.computer.UnaryComputerOp;

/**
 * A join operation which joins a list of {@link UnaryComputerOp}s.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 */
public interface JoinNComputers<A> extends UnaryComputerOp<A, A>,
	JoinNOps<UnaryComputerOp<A, A>>, BufferFactory<A, A>
{

	// -- UnaryComputerOp methods --

	@Override
	default void compute(final A input, final A output) {
		final List<? extends UnaryComputerOp<A, A>> ops = getOps();
		final Iterator<? extends UnaryComputerOp<A, A>> it = ops.iterator();
		final UnaryComputerOp<A, A> first = it.next();

		if (ops.size() == 1) {
			first.compute(input, output);
			return;
		}

		final A buffer = getBuffer(input);

		A tmpOutput = output;
		A tmpInput = buffer;
		A tmp;

		if (ops.size() % 2 == 0) {
			tmpOutput = buffer;
			tmpInput = output;
		}

		first.compute(input, tmpOutput);

		while (it.hasNext()) {
			tmp = tmpInput;
			tmpInput = tmpOutput;
			tmpOutput = tmp;
			it.next().compute(tmpInput, tmpOutput);
		}
	}

}
