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

package net.imagej.ops.special.function;

import net.imagej.ops.special.UnaryOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;

/**
 * A unary <em>function</em> calculates a result from the given input, returning
 * it as a new object. The contents of the input are not affected.
 * <p>
 * A unary function may be treated as a {@link NullaryFunctionOp} by holding the
 * input constant.
 * </p>
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 * @param <I> type of input
 * @param <O> type of output
 * @see UnaryComputerOp
 * @see UnaryInplaceOp
 */
public interface UnaryFunctionOp<I, O> extends UnaryOp<I, O>,
	NullaryFunctionOp<O>
{

	/**
	 * Calculates the output given some input.
	 * 
	 * @param input Argument to the function
	 * @return output Result of the function
	 */
	O calculate(I input);

	// -- UnaryOp methods --

	@Override
	default O run(final I input, final O output) {
		// check function preconditions
		if (input == null) throw new NullPointerException("input is null");
		if (output != null) {
			throw new IllegalArgumentException(
				"Function expects a null output reference");
		}
		// calculate the result
		return calculate(input);
	}

	// -- NullaryFunctionOp methods --

	@Override
	default O calculate() {
		return calculate(in());
	}

	// -- Threadable methods --

	@Override
	default UnaryFunctionOp<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
