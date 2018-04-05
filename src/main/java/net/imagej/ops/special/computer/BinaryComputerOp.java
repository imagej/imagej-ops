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

import net.imagej.ops.special.BinaryOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.inplace.BinaryInplaceOp;

/**
 * A binary <em>computer</em> computes a result from two given inputs, storing
 * it into the specified output reference. The contents of the inputs are not
 * affected.
 * <p>
 * A binary computer may be treated as a {@link UnaryComputerOp} by holding the
 * second input constant, or treated as a {@link NullaryComputerOp} by holding
 * both inputs constant.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I1> type of first input
 * @param <I2> type of second input
 * @param <O> type of output
 * @see BinaryFunctionOp
 * @see BinaryInplaceOp
 */
public interface BinaryComputerOp<I1, I2, O> extends BinaryOp<I1, I2, O>,
	UnaryComputerOp<I1, O>
{

	/**
	 * Computes the output given two inputs.
	 * 
	 * @param input1 first argument to the computation, which
	 *          <em>must be non-null</em>
	 * @param input2 second argument to the computation, which
	 *          <em>must be non-null</em>
	 * @param output object where the computation's result will be stored, which
	 *          <em>must be non-null and a different object than
	 *          {@code input1} and {@code input2}</em>
	 */
	void compute(I1 input1, I2 input2, O output);

	// -- BinaryOp methods --

	@Override
	default O run(final I1 input1, final I2 input2, final O output) {
		// check computer preconditions
		if (input1 == null) throw new NullPointerException("input1 is null");
		if (input2 == null) throw new NullPointerException("input2 is null");
		if (output == null) throw new NullPointerException("output is null");
		if (input1 == output) {
			throw new IllegalArgumentException("Computer expects input1 != output");
		}
		if (input2 == output) {
			throw new IllegalArgumentException("Computer expects input2 != output");
		}
		// compute the result
		compute(input1, input2, output);
		return output;
	}

	// -- UnaryComputerOp methods --

	@Override
	default void compute(final I1 input, final O output) {
		compute(input, in2(), output);
	}

	// -- Runnable methods --

	@Override
	default void run() {
		setOutput(run(in1(), in2(), out()));
	}

	// -- Threadable methods --

	@Override
	default BinaryComputerOp<I1, I2, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
