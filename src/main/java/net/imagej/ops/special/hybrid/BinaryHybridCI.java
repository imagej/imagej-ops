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
import net.imagej.ops.special.inplace.BinaryInplaceOp;

/**
 * A hybrid binary operation which can be used as a {@link BinaryComputerOp} or
 * {@link BinaryInplaceOp}.
 * <p>
 * To populate a preallocated output object, call
 * {@link BinaryComputerOp#compute}; to compute inplace, call
 * {@link BinaryInplaceOp#mutate1} or {@link BinaryInplaceOp#mutate2}. To do any
 * of these things as appropriate, call {@link #run(Object, Object, Object)}.
 * </p>
 * 
 * @author Leon Yang
 * @param <I> type of first + second input
 * @param <O> type of output
 * @see BinaryHybridCI1
 */
public interface BinaryHybridCI<I, O extends I> extends
	BinaryHybridCI1<I, I, O>, BinaryInplaceOp<I, O>
{

	// -- BinaryOp methods --

	@Override
	default O run(final I input1, final I input2, final O output) {
		if (output == input1)
			// mutate first input
			mutate1(output, input2);
		else if (output == input2)
			// mutate second input
			mutate2(input1, output);
		else
			// run as computer
			compute(input1, input2, output);
		return output;
	}

	// -- Threadable methods --

	@Override
	default BinaryHybridCI<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
