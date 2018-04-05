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

package net.imagej.ops.special.computer;

import net.imagej.ops.special.UnaryOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;

/**
 * A unary <em>computer</em> calculates a result from the given input, storing
 * it into the specified output reference. The contents of the input are
 * unaffected.
 * <p>
 * A unary computer may be treated as a {@link NullaryComputerOp} by holding the
 * input constant.
 * </p>
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 * @author Curtis Rueden
 * @param <I> type of input
 * @param <O> type of output
 * @see UnaryFunctionOp
 * @see UnaryInplaceOp
 */
public interface UnaryComputerOp<I, O> extends UnaryOp<I, O>,
	NullaryComputerOp<O>
{

	/**
	 * Computes the output given some input.
	 * 
	 * @param input Argument to the computation, which <em>must be non-null</em>
	 * @param output Object where the computation's result will be stored, which
	 * <em>must be non-null and a different object than {@code input}</em>
	 */
	void compute(I input, O output);

	// -- UnaryOp methods --

	@Override
	default O run(final I input, final O output) {
		// check computer preconditions
		if (input == null) throw new NullPointerException("input is null");
		if (output == null) throw new NullPointerException("output is null");
		if (input == output) {
			throw new IllegalArgumentException("Computer expects input != output");
		}
		// compute the result
		compute(input, output);
		return output;
	}

	// -- NullaryComputerOp methods --

	@Override
	default void compute(final O output) {
		compute(in(), output);
	}

	// -- Runnable methods --

	@Override
	default void run() {
		setOutput(run(in(), out()));
	}

	// -- Threadable methods --

	@Override
	default UnaryComputerOp<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
