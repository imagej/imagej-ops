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

import net.imagej.ops.special.UnaryOutputFactory;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;

/**
 * A hybrid unary operation which can be used as a {@link UnaryComputerOp} or
 * {@link UnaryFunctionOp}.
 * <p>
 * To populate a preallocated output object, call
 * {@link UnaryComputerOp#compute}; to compute a new output object, call
 * {@link UnaryFunctionOp#calculate}. To do any of these things as appropriate,
 * call {@link #run(Object, Object)}.
 * </p>
 * 
 * @author Curtis Rueden
 * @author Christian Dietz (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 * @see UnaryHybridCFI
 */
public interface UnaryHybridCF<I, O> extends UnaryComputerOp<I, O>,
	UnaryFunctionOp<I, O>, UnaryOutputFactory<I, O>, NullaryHybridCF<O>
{

	// -- UnaryFunctionOp methods --

	@Override
	default O calculate(final I input) {
		final O output = createOutput(input);
		compute(input, output);
		return output;
	}

	// -- UnaryOp methods --

	@Override
	default O run(final I input, final O output) {
		if (output == null) {
			// run as a function
			return calculate(input);
		}

		// run as a computer
		compute(input, output);
		return output;
	}

	// -- NullaryFunctionOp methods --

	@Override
	default O calculate() {
		return calculate(in());
	}

	// -- NullaryOutputFactory methods --

	@Override
	default O createOutput() {
		return createOutput(in());
	}

	// -- Runnable methods --

	@Override
	default void run() {
		setOutput(run(in(), out()));
	}

	// -- Threadable methods --

	@Override
	default UnaryHybridCF<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
