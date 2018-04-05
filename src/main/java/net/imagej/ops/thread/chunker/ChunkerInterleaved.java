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
import java.util.concurrent.Future;

import net.imagej.ops.Ops;

import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Implementation of a {@link ChunkerOp} that interleaves the chunks. In a
 * element enumeration from 1..n with <b>k</b> {@link Chunk}s the
 * first one will process the elements 1, k+1, 2k+1, ... the second chunk
 * executable 2, k+2, 2k+2 and so on.
 * 
 * @author Michael Zinsmaier (University of Konstanz)
 */
@Plugin(type = Ops.Thread.Chunker.class, priority = Priority.VERY_LOW)
public class ChunkerInterleaved extends AbstractChunker {

	@Parameter
	public LogService logService;

	private String cancellationMsg;

	@Override
	public void run() {

		final int numThreads = Runtime.getRuntime().availableProcessors();
		final int numStepsFloor = (int) (numberOfElements / numThreads);
		final int remainder = (int) numberOfElements - (numStepsFloor * numThreads);

		final ArrayList<Future<?>> futures = new ArrayList<>(numThreads);

		for (int i = 0; i < numThreads; i++) {
			final int j = i;

			futures.add(threadService.run(new Runnable() {

				@Override
				public void run() {
					if (j < remainder) {
						chunkable.execute(j, numThreads, (numStepsFloor + 1));
					}
					else {
						chunkable.execute(j, numThreads, numStepsFloor);
					}
				}
			}));
		}

		for (final Future<?> future : futures) {
			try {
				if (isCanceled()) {
					break;
				}
				future.get();
			}
			catch (final Exception e) {
				logService.error(e);
				cancellationMsg = e.getMessage();
				break;
			}
		}
	}

	@Override
	public boolean isCanceled() {
		return cancellationMsg != null;
	}

	@Override
	public String getCancelReason() {
		return cancellationMsg;
	}
}
