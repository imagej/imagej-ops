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

import net.imagej.ops.special.BinaryOutputFactory;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;

/**
 * A hybrid binary operation which can be used as a {@link BinaryComputerOp} or
 * {@link BinaryFunctionOp}.
 * <p>
 * To populate a preallocated output object, call
 * {@link BinaryComputerOp#compute}; to compute a new output object, call
 * {@link BinaryFunctionOp#calculate}. To do any of these things as appropriate,
 * call {@link #run(Object, Object, Object)}.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I1> type of first input
 * @param <I2> type of second input
 * @param <O> type of output
 * @see BinaryHybridCFI
 * @see BinaryHybridCFI1
 */
public interface BinaryHybridCF<I1, I2, O> extends BinaryComputerOp<I1, I2, O>,
	BinaryFunctionOp<I1, I2, O>, BinaryOutputFactory<I1, I2, O>,
	UnaryHybridCF<I1, O>
{

	// -- BinaryFunctionOp methods --

	@Override
	default O calculate(final I1 input1, final I2 input2) {
		final O output = createOutput(input1, input2);
		compute(input1, input2, output);
		return output;
	}

	// -- BinaryOp methods --

	@Override
	default O run(final I1 input1, final I2 input2, final O output) {
		if (output == null) {
			// run as a function
			return calculate(input1, input2);
		}

		// run as a computer
		compute(input1, input2, output);
		return output;
	}

	// -- UnaryFunctionOp methods --

	@Override
	default O calculate(final I1 input) {
		return calculate(input, in2());
	}

	// -- UnaryOutputFactory methods --

	@Override
	default O createOutput(final I1 input) {
		return createOutput(input, in2());
	}

	// -- Runnable methods --

	@Override
	default void run() {
		setOutput(run(in1(), in2(), out()));
	}

	// -- Threadable methods --

	@Override
	default BinaryHybridCF<I1, I2, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
