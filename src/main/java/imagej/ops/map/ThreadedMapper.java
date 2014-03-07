package imagej.ops.map;

import imagej.Cancelable;
import imagej.ops.Op;
import imagej.ops.UnaryFunction;

import java.util.ArrayList;
import java.util.concurrent.Future;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * @author Christian Dietz
 * 
 * @param <A>
 * @param <B>
 */
@Plugin(type = Op.class, name = "map", priority = Priority.LOW_PRIORITY + 1)
public class ThreadedMapper<A, B> implements Op, Cancelable {

	@Parameter
	private ThreadService threadService;

	@Parameter
	private IterableInterval<A> in;

	@Parameter
	private UnaryFunction<A, B> func;

	@Parameter(type = ItemIO.BOTH)
	private RandomAccessibleInterval<B> out;

	private String cancelationMessage;

	@Override
	public void run() {

		final long numElements = in.size();

		// TODO: is there a better way to determine the optimal chunk size?
		final int numChunks = (int) in.size()
				/ Runtime.getRuntime().availableProcessors();

		final int chunkSize = (int) (numElements / numChunks);

		final ArrayList<Future<?>> futures = new ArrayList<Future<?>>(numChunks);

		for (int i = 0; i < numChunks - 1; i++) {
			futures.add(threadService.run(new ChunkedUnaryFunctionTask(i
					* chunkSize, i * chunkSize + chunkSize)));
		}

		// last chunk gets the rest
		futures.add(threadService.run(new ChunkedUnaryFunctionTask(
				(numChunks - 1) * chunkSize,
				(int) (chunkSize + (numElements % chunkSize)))));

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
			final Cursor<A> cursor = in.cursor();
			cursor.jumpFwd(firstElement - 1);

			final RandomAccess<B> rndAccess = out.randomAccess();
			final UnaryFunction<A, B> copy = func.copy();

			int ctr = 0;
			while (cursor.hasNext() && ctr < lastElement + 1) {
				cursor.fwd();
				rndAccess.setPosition(cursor);
				copy.compute(cursor.get(), rndAccess.get());
				ctr++;
			}
		}
	}

}
