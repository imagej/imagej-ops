/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package imagej.ops.map;

import imagej.Cancelable;
import imagej.ops.Op;
import imagej.ops.Function;

import java.util.ArrayList;
import java.util.concurrent.Future;

import org.scijava.plugin.Parameter;
import org.scijava.thread.ThreadService;

/**
 * Abstract Threader for MultiThreading of mappings of {@link Function}s
 * 
 * @author Christian Dietz
 */
public abstract class AbstractThreadedMapper implements Op, Cancelable {

	@Parameter
	private ThreadService threadService;

	private String cancelationMessage;

	protected abstract void runThread(final int firstElement,
		final int numElements);

	protected void runThreading(final long numElements) {

		// TODO: is there a better way to determine the optimal chunk size?
		final int chunkSize =
			(int) (numElements / Runtime.getRuntime().availableProcessors());

		final int numChunks = (int) (numElements / chunkSize);

		final ArrayList<Future<?>> futures = new ArrayList<Future<?>>(numChunks);

		for (int i = 0; i < numChunks - 1; i++) {
			futures.add(threadService.run(new ChunkedUnaryFunctionTask(i * chunkSize,
				(i * chunkSize + chunkSize) - 1)));
		}

		// last chunk gets the rest
		futures.add(threadService.run(new ChunkedUnaryFunctionTask((numChunks - 1) *
			chunkSize, (int) (chunkSize + (numElements % chunkSize)))));

		for (final Future<?> future : futures) {
			try {
				future.get();
			}
			catch (final Exception e) {
				cancelationMessage = e.getMessage();
				break;
			}
		}
	}

	@Override
	public String getCancelReason() {
		return cancelationMessage;
	}

	@Override
	public boolean isCanceled() {
		return cancelationMessage != null;
	}

	private class ChunkedUnaryFunctionTask implements Runnable {

		private final int firstElement;

		private final int numElements;

		public ChunkedUnaryFunctionTask(final int firstElement,
			final int numElements)
		{
			this.firstElement = firstElement;
			this.numElements = numElements;
		}

		@Override
		public void run() {
			runThread(firstElement, numElements);
		}
	}
}
