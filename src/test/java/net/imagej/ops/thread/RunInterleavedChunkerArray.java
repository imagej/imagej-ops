/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2024 ImageJ2 developers.
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
package net.imagej.ops.thread;

import net.imagej.ops.Op;
import net.imagej.ops.Parallel;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.thread.chunker.Chunk;
import net.imagej.ops.thread.chunker.ChunkerInterleaved;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import org.scijava.util.ArrayUtils;

@Plugin(type = Op.class, name = "test.chunker",
	priority = Priority.LOW)
public class RunInterleavedChunkerArray<A> extends
	AbstractUnaryComputerOp<A[], A[]> implements Parallel
{
	
	@Override
	public void compute(final A[] input, final A[] output) {
		ops().run(ChunkerInterleaved.class, new Chunk() {

			@Override
			public void
				execute(long startIndex, final long stepSize, final long numSteps)
			{
				int i = startIndex==0 ? 0 : ArrayUtils.safeMultiply32(startIndex);
				int stepSizeSafe32 = stepSize == 0 ? 0 : ArrayUtils.safeMultiply32(stepSize);
				
				int ctr = 0;
				while (ctr < numSteps) {
					output[i] = input[i];
					i += stepSizeSafe32;
					ctr++;
				}
			}
		}, input.length);
	}
}
