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

package net.imagej.ops.special.inplace;

import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;

/**
 * A binary <em>inplace</em> operation is an op which computes a result from two
 * given arguments, storing it either the first <em>or</em> second argument
 * (i.e., mutating it).
 * <p>
 * It is a special case of {@link BinaryInplace1Op}, which can mutate
 * <em>only</em> the first input.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <A> type of inputs + output
 * @see BinaryComputerOp
 * @see BinaryFunctionOp
 */
public interface BinaryInplaceOp<A> extends BinaryInplace1Op<A, A> {

	/**
	 * Mutates the second argument in-place.
	 * 
	 * @param in First argument of the {@link BinaryInplaceOp}, which will
	 *          <em>not</em> be mutated.
	 * @param arg Second argument of the {@link BinaryInplaceOp}, which
	 *          <em>will</em> be mutated.
	 */
	void mutate2(A in, A arg);

	// -- BinaryOp methods --

	@Override
	default A run(final A input1, final A input2, final A output) {
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
		if (input1 == output) mutate1(input1, input2);
		else mutate2(input1, input2);
		return output;
	}

	// -- Threadable methods --

	@Override
	default BinaryInplaceOp<A> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
