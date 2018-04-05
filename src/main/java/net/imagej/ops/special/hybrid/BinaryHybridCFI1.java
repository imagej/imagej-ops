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

import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.inplace.BinaryInplace1Op;

/**
 * A hybrid binary operation which can be used as a {@link BinaryComputerOp},
 * {@link BinaryFunctionOp} or {@link BinaryInplace1Op}.
 * <p>
 * To populate a preallocated output object, call
 * {@link BinaryComputerOp#compute}; to compute a new output object, call
 * {@link BinaryFunctionOp#calculate}; to mutate the first input inplace, call
 * {@link BinaryInplace1Op#mutate1}. To do any of these things as appropriate,
 * call {@link #run(Object, Object, Object)}.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I1> type of first input
 * @param <I2> type of second input
 * @param <O> type of output
 * @see BinaryHybridCF
 * @see BinaryHybridCFI
 */
public interface BinaryHybridCFI1<I1, I2, O extends I1> extends
	BinaryHybridCF<I1, I2, O>, BinaryHybridCI1<I1, I2, O>,
	UnaryHybridCFI<I1, O>
{

	// -- BinaryOp methods --

	@Override
	default O run(final I1 input1, final I2 input2, final O output) {
		if (input1 == output) {
			// run as an inplace (1st input)
			return BinaryHybridCI1.super.run(input1, input2, output);
		}
		// run as a hybrid CF
		return BinaryHybridCF.super.run(input1, input2, output);
	}

	// -- UnaryInplaceOp methods --

	@Override
	default void mutate(final O arg) {
		BinaryHybridCI1.super.mutate(arg);
	}

	// -- Runnable methods --

	@Override
	default void run() {
		setOutput(run(in1(), in2(), out()));
	}

	// -- Threadable methods --

	@Override
	default BinaryHybridCFI1<I1, I2, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
