/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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
 * {@link BinaryComputerOp#compute2}; to compute a new output object, call
 * {@link BinaryFunctionOp#compute2}; to mutate the first input inplace, call
 * {@link BinaryInplace1Op#mutate1}. To do any of these things as appropriate,
 * call {@link #run(Object, Object, Object)}.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <A> type of first input + output
 * @param <I> type of second input
 * @see BinaryHybridCF
 * @see BinaryHybridCFI
 */
public interface BinaryHybridCFI1<A, I> extends BinaryHybridCF<A, I, A>,
	BinaryInplace1Op<A, I>, UnaryHybridCFI<A>
{

	// -- BinaryOp methods --

	@Override
	default A run(final A input1, final I input2, final A output) {
		if (input1 == output) {
			// run as an inplace (1st input)
			return BinaryInplace1Op.super.run(input1, input2, output);
		}
		// run as a hybrid CF
		return BinaryHybridCF.super.run(input1, input2, output);
	}

	// -- Runnable methods --

	@Override
	default void run() {
		setOutput(run(in1(), in2(), out()));
	}

	// -- Threadable methods --

	@Override
	default BinaryHybridCFI1<A, I> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
