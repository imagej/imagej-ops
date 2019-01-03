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

package net.imagej.ops.thread.chunker;

import net.imagej.ops.Parallel;

/**
 * A {@link Chunk} of code which can be executed by a {@link ChunkerOp}.
 * <p>
 * A {@link Chunk} processes a subset of a bigger problem and can be executed in
 * parallel with other {@link Chunk}s. The elements of the subproblem are
 * identified by enumerating the original problem.
 * </p>
 * 
 * @author Christian Dietz (University of Konstanz)
 * @see ChunkerOp
 * @see Parallel
 */
public interface Chunk {
	
	/**
	 * Solve the subproblem for the element at startIndex, increase the index by
	 * the given stepSize and repeat numSteps.
	 * 
	 * @param startIndex zero based index that identifies the first element of
	 *          this subproblem (w.r.t. the global problem enumeration)
	 * @param stepSize the step-size between two consecutive elements
	 * @param numSteps how many steps shall be taken
	 */
	void execute(long startIndex, long stepSize, long numSteps);

}
