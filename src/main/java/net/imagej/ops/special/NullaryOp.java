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

package net.imagej.ops.special;

import net.imagej.ops.special.computer.NullaryComputerOp;
import net.imagej.ops.special.function.NullaryFunctionOp;
import net.imagej.ops.special.hybrid.NullaryHybridCF;

/**
 * A <em>nullary</em> operation computes a result in a vacuum, without any input
 * values.
 * <p>
 * Nullary ops come in two major flavors: {@link NullaryComputerOp} and
 * {@link NullaryFunctionOp}. An additional hybrid type {@link NullaryHybridCF}
 * unions both flavors.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <O> type of output
 */
public interface NullaryOp<O> extends SpecialOp, Output<O> {

	/**
	 * Executes the operation in a type-safe but flexible way.
	 * <p>
	 * The exact behavior depends on the type of special op.
	 * </p>
	 * @param output reference where the operation's result will be stored
	 * @return result of the operation
	 * @see NullaryComputerOp#run(Object)
	 * @see NullaryFunctionOp#run(Object)
	 * @see NullaryHybridCF#run(Object)
	 */
	O run(O output);

	// -- SpecialOp methods --

	@Override
	default int getArity() {
		return 0;
	}

	// -- Runnable methods --

	@Override
	default void run() {
		run(out());
	}

	// -- Threadable methods --

	@Override
	default NullaryOp<O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
