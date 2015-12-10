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
 * A unary <em>computer</em> calculates a result from the given input, storing
 * it into the specified output reference. The contents of the input are
 * unaffected.
 * <p>
 * A unary computer may be treated as a {@link NullaryComputerOp} by holding the
 * input constant.
 * </p>
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 * @author Curtis Rueden
 * @param <I> type of input
 * @param <O> type of output
 * @see UnaryFunctionOp
 * @see UnaryHybridOp
 */
public interface UnaryComputerOp<I, O> extends UnaryOp<I, O>,
	NullaryComputerOp<O>
{

	/**
	 * Computes the output given some input.
	 * 
	 * @param input Argument to the computation, which <em>must be non-null</em>
	 * @param output Object where the computation's result will be stored, which
	 * <em>must be non-null and a different object than {@code input}</em>
	 */
	void compute1(I input, O output);

	// -- NullaryComputerOp methods --

	@Override
	default void compute0(final O output) {
		compute1(in(), output);
	}

	// -- Runnable methods --

	@Override
	default void run() {
		compute1(in(), out());
	}

	// -- Threadable methods --

	@Override
	default UnaryComputerOp<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
