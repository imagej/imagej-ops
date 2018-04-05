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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.imagej.ops.Ops;

import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Simple default implementation of a {@link ChunkerOp}. The list of
 * elements is chunked into equally sized (besides the last one), disjoint
 * chunks, which are processed in parallel. The stepSize is set to one, i.e.
 * each chunk consists of consecutive elements.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Thread.Chunker.class)
public class DefaultChunker extends AbstractChunker {

	private final int STEP_SIZE = 1;

	@Parameter
	public LogService logService;

	@Override
	public void run() {

		// TODO: is there a better way to determine the optimal chunk size?
		
		final int numSteps = Math.max(1, 
			(int) (numberOfElements / Runtime.getRuntime().availableProcessors())) ;
		
		final int numChunks = (int) (numberOfElements / numSteps);

		final ArrayList<Future<?>> futures = new ArrayList<>(numChunks);

		for (int i = 0; i < numChunks - 1; i++) {
			final int j = i;

			futures.add(threadService.run(new Runnable() {

				@Override
				public void run() {
					chunkable.execute(j * numSteps, STEP_SIZE, numSteps);
				}
			}));
		}

		// last chunk additionally add the rest of elements
		futures.add(threadService.run(new Runnable() {

			@Override
			public void run() {
				chunkable.execute((numChunks - 1) * numSteps, STEP_SIZE,
					(int) (numSteps + (numberOfElements % numSteps)));
			}
		}));

		for (final Future<?> future : futures) {
			try {
				if (isCanceled()) {
					break;
				}
				future.get();
			}
			catch (final InterruptedException exc) {
				throw new RuntimeException(exc);
			}
			catch (final ExecutionException exc) {
				throw new RuntimeException(exc);
			}
		}
	}

}
