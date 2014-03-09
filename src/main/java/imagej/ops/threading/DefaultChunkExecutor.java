
package imagej.ops.threading;

import imagej.ops.Op;

import java.util.ArrayList;
import java.util.concurrent.Future;

import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Very simple default implementation.
 * 
 * @author Christian Dietz
 */
@Plugin(type = Op.class, name = "chunker")
public class DefaultChunkExecutor extends AbstractChunkExecutor {

	// we jump 1 forward
	private final int STEP_SIZE = 1;

	@Parameter
	public LogService logService;

	// cancellation
	private String cancellationMsg;

	@Override
	public void run() {

		// TODO: is there a better way to determine the optimal chunk size?
		final int numSteps =
			(int) (totalSize / Runtime.getRuntime().availableProcessors());

		final int numChunks = (int) (totalSize / numSteps);

		final ArrayList<Future<?>> futures = new ArrayList<Future<?>>(numChunks);

		for (int i = 0; i < numChunks - 1; i++) {
			final int j = i;
			// TODO Benchmark if this makes it slow vs. other class/object option
			final class MyRunnable implements Runnable {

				@Override
				public void run() {
					chunkable.execute(j * numSteps, STEP_SIZE, numSteps);
				}
			};
			futures.add(threadService.run(new MyRunnable()));
		}

		// last chunk gets the rest
		futures.add(threadService.run(new Runnable() {

			@Override
			public void run() {
				chunkable.execute((numChunks - 1) * numSteps, STEP_SIZE,
					(int) (numSteps + (totalSize % numSteps)));
			}
		}));

		for (final Future<?> future : futures) {
			try {
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
