/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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
 * A unary <em>hybrid</em> operation can be used as either a
 * {@link UnaryFunctionOp} or as a {@link UnaryComputerOp}.
 * <p>
 * To compute a new output object, call {@link UnaryFunctionOp#compute1}; to
 * populate an already-existing output object, call
 * {@link UnaryComputerOp#compute1}.
 * </p>
 * 
 * @author Curtis Rueden
 * @author Christian Dietz (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 * @see UnaryComputerOp
 * @see UnaryFunctionOp
 */
public interface UnaryHybridOp<I, O> extends UnaryComputerOp<I, O>,
	UnaryFunctionOp<I, O>, UnaryOutputFactory<I, O>, NullaryHybridOp<O>
{

	// -- UnaryFunctionOp methods --

	@Override
	default O compute1(final I input) {
		final O output = createOutput(input);
		compute1(input, output);
		return output;
	}

	// -- NullaryFunctionOp methods --

	@Override
	default O compute0() {
		return compute1(in());
	}

	// -- NullaryOutputFactory methods --

	@Override
	default O createOutput() {
		return createOutput(in());
	}

	// -- Threadable methods --

	@Override
	default UnaryHybridOp<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
