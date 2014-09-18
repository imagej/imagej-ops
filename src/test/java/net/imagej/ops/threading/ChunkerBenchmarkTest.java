/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
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
package net.imagej.ops.threading;

import static org.junit.Assume.assumeTrue;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import net.imagej.ops.Ops;
import net.imagej.ops.benchmark.AbstractOpBenchmark;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

/**
 * Tests the {@link Chunker}.
 *
 * @author Christian Dietz
 */
// make sure that the data structure initialisation is not benchmarked
@BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 1)
public class ChunkerBenchmarkTest extends AbstractOpBenchmark {

	private boolean expensiveTestsEnabled = "enabled".equals(System.getProperty("imagej.ops.expensive.tests"));

	/** Needed for JUnit-Benchmarks */
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	private Img<ByteType> in, out;

	private Byte[] in2, out2;

	@Test
	public void run100MbTest() {
		generateByteTestImgs(10240);
		ops.module(RunDefaultChunker.class, out, in).run();
	}

	@Test
	public void run100MbInterleaved() {
		generateByteTestImgs(10240);
		ops.module(RunInterleavedChunker.class, out, in).run();
	}

	@Test
	public void run1MbTest() {
		generateByteTestImgs(1024);
		ops.module(RunDefaultChunker.class, out, in).run();
	}

	@Test
	public void run1MbInterleaved() {
		generateByteTestImgs(1024);
		ops.module(RunInterleavedChunker.class, out, in).run();
	}

	//with arrays

	@Test
	public void run100MbArrayTest() {
		generateByteArrays(10240);
		ops.module(RunDefaultChunkerArray.class, out2, in2).run();
	}

	@Test
	public void run100MbArrayInterleavedTest() {
		generateByteArrays(10240);
		ops.module(RunInterleavedChunkerArray.class, out2, in2).run();
	}

	@Test
	public void run1MbArrayTest() {
		generateByteArrays(1024);
		ops.module(RunDefaultChunkerArray.class, out2, in2).run();
	}

	@Test
	public void run1MbArrayInterleavedTest() {
		generateByteArrays(1024);
		ops.module(RunInterleavedChunkerArray.class, out2, in2).run();
	}

	private void generateByteTestImgs(int size) {
		if (in != null && in.dimension(0) == size) return;
		if (size > 1024) {
			assumeTrue(expensiveTestsEnabled);
		}
		in = generateByteTestImg(true, size, size);
		out = generateByteTestImg(false, size, size);
	}

	private void generateByteArrays(int size) {
		if (in2 != null && size * size == in2.length) return;
		if (size > 1024) {
			assumeTrue(expensiveTestsEnabled);
		}
		in2 = new Byte[size * size];
		out2 = new Byte[size * size];
	}
}
