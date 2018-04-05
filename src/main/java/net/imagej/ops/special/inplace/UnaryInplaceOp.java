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

package net.imagej.ops.special.inplace;

import net.imagej.ops.special.UnaryOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.UnaryHybridCI;

/**
 * A unary <em>inplace</em> operation is an op which mutates a parameter.
 * <p>
 * Note that the {@code <I>} and {@code <O>} type parameters are kept distinct
 * for special hybrid ops, which may <em>allow</em> inplace mutation without
 * <em>requiring</em> it; see e.g. {@link UnaryHybridCI}.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I> type of input
 * @param <O> type of output
 * @see UnaryComputerOp
 * @see UnaryFunctionOp
 */
public interface UnaryInplaceOp<I, O extends I> extends UnaryOp<I, O> {

	/**
	 * Mutates the given input argument in-place.
	 * 
	 * @param arg of the {@link UnaryInplaceOp}
	 */
	void mutate(O arg);

	// -- UnaryOp methods --

	@Override
	default O run(final I input, final O output) {
		// check inplace preconditions
		if (input == null) throw new NullPointerException("input is null");
		if (input != output) {
			throw new IllegalArgumentException("Inplace expects input == output");
		}

		// compute the result
		mutate(output);
		return output;
	}

	// -- NullaryOp methods --

	@Override
	default O run(final O output) {
		// check inplace preconditions
		if (output == null) throw new NullPointerException("output is null");

		// compute the result
		mutate(output);
		return output;
	}

	// -- UnaryInput methods --

	@Override
	default I in() {
		return out();
	}

	// -- Threadable methods --

	@Override
	default UnaryInplaceOp<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
