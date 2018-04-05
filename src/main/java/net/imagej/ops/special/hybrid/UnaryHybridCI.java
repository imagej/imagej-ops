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

package net.imagej.ops.special.hybrid;

import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;

/**
 * A hybrid unary operation which can be used as a {@link UnaryComputerOp} or
 * {@link UnaryInplaceOp}.
 * <p>
 * To populate a preallocated output object, call
 * {@link UnaryComputerOp#compute}; to compute inplace, call
 * {@link UnaryInplaceOp#mutate}. To do any of these things as appropriate, call
 * {@link #run(Object, Object)}.
 * </p>
 * 
 * @author Curtis Rueden
 * @author Christian Dietz (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 * @see UnaryHybridCI
 */
public interface UnaryHybridCI<I, O extends I> extends UnaryComputerOp<I, O>,
	UnaryInplaceOp<I, O>
{

	// -- UnaryInplaceOp methods --

	@Override
	default void mutate(final O arg) {
		compute(arg, arg);
	}

	// -- UnaryOp methods --

	@Override
	default O run(final I input, final O output) {
		if (output == input) {
			// run inplace
			mutate(output);
			return output;
		}

		// run as a computer
		compute(input, output);
		return output;
	}

	// -- NullaryOp methods --

	@Override
	default O run(final O output) {
		return UnaryInplaceOp.super.run(output);
	}

	// -- Runnable methods --

	@Override
	default void run() {
		setOutput(run(in(), out()));
	}

	// -- Threadable methods --

	@Override
	default UnaryHybridCI<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
