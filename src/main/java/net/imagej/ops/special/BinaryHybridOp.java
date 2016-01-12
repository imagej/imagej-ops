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

package net.imagej.ops.special;

/**
 * A <em>hybrid</em> binary operation can be used as either a
 * {@link BinaryFunctionOp} or as a {@link BinaryComputerOp}.
 * <p>
 * To compute a new output object, call {@link BinaryFunctionOp#compute2}; to
 * populate an already-existing output object, call
 * {@link BinaryComputerOp#compute2}.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I1> type of first input
 * @param <I2> type of second input
 * @param <O> type of output
 * @see BinaryComputerOp
 * @see BinaryFunctionOp
 */
public interface BinaryHybridOp<I1, I2, O> extends BinaryComputerOp<I1, I2, O>,
	BinaryFunctionOp<I1, I2, O>, BinaryOutputFactory<I1, I2, O>,
	UnaryHybridOp<I1, O>
{

	// -- BinaryFunctionOp methods --

	@Override
	default O compute2(final I1 input1, final I2 input2) {
		final O output = createOutput(input1, input2);
		compute2(input1, input2, output);
		return output;
	}

	// -- UnaryFunctionOp methods --

	@Override
	default O compute1(final I1 input) {
		return compute2(input, in2());
	}

	// -- UnaryOutputFactory methods --

	@Override
	default O createOutput(final I1 input) {
		return createOutput(input, in2());
	}

	// -- Threadable methods --

	@Override
	default BinaryHybridOp<I1, I2, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
