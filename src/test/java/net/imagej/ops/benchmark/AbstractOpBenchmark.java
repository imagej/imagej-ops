/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.benchmark;

import static org.junit.Assume.assumeTrue;

import net.imagej.ops.AbstractOpTest;

import org.junit.Before;
import org.scijava.module.Module;

/**
 * @author Christian Dietz (University of Konstanz)
 */
public class AbstractOpBenchmark extends AbstractOpTest {

	private boolean benchmarkTestsEnabled = "enabled".equals(System.getProperty("imagej.ops.benchmark.tests"));

	@Before
	public void skipBenchmarksByDefault() {
		assumeTrue(benchmarkTestsEnabled);
	}

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
