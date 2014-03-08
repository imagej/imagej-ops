
package imagej.ops.tests.benchmark;

import imagej.module.Module;
import imagej.ops.tests.AbstractOpTest;

/**
 * @author Christian Dietz
 */
public class AbstractOpBenchmark extends AbstractOpTest {

	public long bestOf(final Runnable runnable, final int n) {
		long best = Long.MAX_VALUE;

		for (int i = 0; i < n; i++) {
			long time = System.nanoTime();
			runnable.run();
			time = System.nanoTime() - time;

			if (time < best) {
				best = time;
			}
		}

		return best;
	}

	public double asMilliSeconds(final long nanoTime) {
		return nanoTime / 1000.0d / 1000.d;
	}

	public void benchmarkAndPrint(final String name, final Module module,
		final int numRuns)
	{
		System.out.println("[" + name + "]: " +
			asMilliSeconds(bestOf(module, numRuns)) + "ms !");
	}
}
