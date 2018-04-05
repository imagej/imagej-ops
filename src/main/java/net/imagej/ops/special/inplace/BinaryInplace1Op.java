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

import net.imagej.ops.special.BinaryOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.hybrid.BinaryHybridCFI1;

/**
 * A binary <em>inplace</em> operation which computes a result from two given
 * arguments, storing it back into the <em>first</em> input (i.e., mutating it).
 * <p>
 * It is a less powerful version of {@link BinaryInplaceOp}, which can mutate
 * <em>either</em> of its inputs.
 * </p>
 * <p>
 * Note that the {@code <I1>} and {@code <O>} type parameters are kept distinct
 * for special hybrid ops, which may <em>allow</em> inplace mutation without
 * <em>requiring</em> it; see e.g. {@link BinaryHybridCFI1}.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I1> type of first input
 * @param <I2> type of second input
 * @param <O> type of output
 * @see BinaryComputerOp
 * @see BinaryFunctionOp
 */
public interface BinaryInplace1Op<I1, I2, O extends I1> extends
	BinaryOp<I1, I2, O>, UnaryInplaceOp<I1, O>
{

	/**
	 * Mutates the first argument in-place.
	 * 
	 * @param arg First argument of the {@link BinaryInplace1Op}, which
	 *          <em>will</em> be mutated.
	 * @param in Second argument of the {@link BinaryInplace1Op}, which will
	 *          <em>not</em> be mutated.
	 */
	void mutate1(O arg, I2 in);

	// -- BinaryOp methods --

	@Override
	default O run(final I1 input1, final I2 input2, final O output) {
		// check inplace preconditions
		if (input1 == null) throw new NullPointerException("input1 is null");
		if (input2 == null) throw new NullPointerException("input2 is null");
		if (input1 != output) {
			throw new IllegalArgumentException("Inplace expects input1 == output");
		}
		if (input1 == input2) {
			throw new IllegalArgumentException("Inplace expects input1 != input2");
		}

		// compute the result
		mutate1(output, input2);
		return output;
	}

	// -- BinaryInput methods --

	@Override
	default I1 in1() {
		return out();
	}

	// -- UnaryInplaceOp methods --

	@Override
	default void mutate(final O arg) {
		mutate1(arg, in2());
	}

	// -- UnaryInput methods --

	@Override
	default I1 in() {
		return BinaryOp.super.in();
	}

	// -- Runnable methods --

	@Override
	default void run() {
		run(in1(), in2(), out());
	}

	// -- Threadable methods --

	@Override
	default BinaryInplace1Op<I1, I2, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
