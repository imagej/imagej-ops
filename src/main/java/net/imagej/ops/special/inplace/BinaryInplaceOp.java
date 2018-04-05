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

import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.hybrid.BinaryHybridCFI;

/**
 * A binary <em>inplace</em> operation is an op which computes a result from two
 * given arguments, storing it either the first <em>or</em> second argument
 * (i.e., mutating it).
 * <p>
 * It is a special case of {@link BinaryInplace1Op}, which can mutate
 * <em>only</em> the first input.
 * </p>
 * <p>
 * Note that the {@code <I>} and {@code <O>} type parameters are kept distinct
 * for special hybrid ops, which may <em>allow</em> inplace mutation without
 * <em>requiring</em> it; see e.g. {@link BinaryHybridCFI}. However, due to
 * limitations of Java generics, the {@code <I1>} and {@code <I2>} parameters
 * <em>must</em> be collapsed into a single {@code <I>}, since it is not legal
 * to write {@code <I1, I2, O extends I1 & I2>} (&quot;Cannot specify any
 * additional bound I2 when first bound is a type parameter&quot;).
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I> type of inputs
 * @param <O> type of output
 * @see BinaryComputerOp
 * @see BinaryFunctionOp
 */
public interface BinaryInplaceOp<I, O extends I> extends
	BinaryInplace1Op<I, I, O>
{

	/**
	 * Mutates the second argument in-place.
	 * 
	 * @param in First argument of the {@link BinaryInplaceOp}, which will
	 *          <em>not</em> be mutated.
	 * @param arg Second argument of the {@link BinaryInplaceOp}, which
	 *          <em>will</em> be mutated.
	 */
	void mutate2(I in, O arg);

	// -- BinaryOp methods --

	@Override
	default O run(final I input1, final I input2, final O output) {
		// check inplace preconditions
		if (input1 == null) throw new NullPointerException("input1 is null");
		if (input2 == null) throw new NullPointerException("input2 is null");
		if (input1 != output && input2 != output) {
			throw new IllegalArgumentException(
				"Inplace expects input1 == output || input2 == output");
		}
		if (input1 == input2) {
			throw new IllegalArgumentException("Inplace expects input1 != input2");
		}

		// compute the result
		if (input1 == output) mutate1(output, input2);
		else mutate2(input1, output);
		return output;
	}

	// -- Threadable methods --

	@Override
	default BinaryInplaceOp<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
