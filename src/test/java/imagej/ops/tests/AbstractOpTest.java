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

package imagej.ops.tests;

import static org.junit.Assert.assertTrue;
import imagej.ops.Op;
import imagej.ops.OpService;
import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.util.Intervals;

import org.junit.After;
import org.junit.Before;
import org.scijava.Context;

/**
 * Base class for {@link Op} unit testing.
 * <p>
 * <i>All</i> {@link Op} unit tests need to have an {@link OpService} instance.
 * Following the DRY principle, we should implement it only once. Here.
 * </p>
 * 
 * @author Johannes Schindelin
 */
public abstract class AbstractOpTest {

	protected Context context;
	protected OpService ops;

	/**
	 * Sets up an {@link OpService}.
	 */
	@Before
	public void setUp() {
		context = new Context(OpService.class);
		ops = context.getService(OpService.class);
		assertTrue(ops != null);
	}

	/**
	 * Disposes of the {@link OpService} that was initialized in {@link #setUp()}.
	 */
	@After
	public synchronized void cleanUp() {
		if (context != null) {
			context.dispose();
			context = null;
		}
	}

	private int seed;

	private int pseudoRandom() {
		return seed = 3170425 * seed + 132102;
	}

	public Img<ByteType> generateByteTestImg(final boolean fill,
		final long... dims)
	{
		final byte[] array =
			new byte[(int) Intervals.numElements(new FinalInterval(dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (byte) pseudoRandom();
			}
		}

		return ArrayImgs.bytes(array, dims);
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
}
