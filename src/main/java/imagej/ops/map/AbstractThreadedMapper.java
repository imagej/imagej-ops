package imagej.ops.map;

import imagej.Cancelable;
import imagej.ops.Op;
import imagej.ops.UnaryFunction;

import java.util.ArrayList;
import java.util.concurrent.Future;

import org.scijava.plugin.Parameter;
import org.scijava.thread.ThreadService;

/**
 * Abstract Threader for MultiThreading of mappings of {@link UnaryFunction}s
 * 
 * @author Christian Dietz
 * 
 */
public abstract class AbstractThreadedMapper implements Op, Cancelable {

	@Parameter
	private ThreadService threadService;

	@Parameter
	private String cancelationMessage;

	protected abstract void runThread(final int firstElement,
			final int lastElement);

	protected void runThreading(final long l) {

		// TODO: is there a better way to determine the optimal chunk size?
		final int numChunks = (int) (l / Runtime.getRuntime()
				.availableProcessors());

		final int chunkSize = (int) (l / numChunks);

		final ArrayList<Future<?>> futures = new ArrayList<Future<?>>(numChunks);

		for (int i = 0; i < numChunks - 1; i++) {
			futures.add(threadService.run(new ChunkedUnaryFunctionTask(i
					* chunkSize, (i * chunkSize + chunkSize) - 1)));
		}

		// last chunk gets the rest
		futures.add(threadService.run(new ChunkedUnaryFunctionTask(
				(numChunks - 1) * chunkSize,
				(int) (chunkSize + (l % chunkSize)))));

		for (final Future<?> future : futures) {
			try {
				future.get();
			} catch (final Exception e) {
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

		private final int lastElement;

		public ChunkedUnaryFunctionTask(final int firstElement,
				final int lastElement) {
			this.firstElement = firstElement;
			this.lastElement = lastElement;
		}

		@Override
		public void run() {
			runThread(firstElement, lastElement);
		}
	}
}
