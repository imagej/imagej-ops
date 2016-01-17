/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.OpCandidate;
import net.imagej.ops.OpRef;
import net.imagej.ops.Ops;
import net.imagej.ops.thread.chunker.Chunk;
import net.imagej.ops.thread.chunker.ChunkerInterleaved;
import net.imagej.ops.thread.chunker.DefaultChunker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.scijava.InstantiableException;
import org.scijava.module.Module;

/**
 * Benchmark op matching.
 * 
 * @author Curtis Rueden
 */
@BenchmarkOptions(benchmarkRounds = 20000, warmupRounds = 1)
public class MatchingBenchmarkTest extends AbstractOpBenchmark {

	/** Needed for JUnit-Benchmarks */
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	@Test
	public void allCandidates() {
		final List<OpCandidate<Op>> candidates = matcher.findCandidates(ops,
			new OpRef<>(null, null, null, null));
		assertTrue(candidates.size() > 500);
	}

	@Test
	public void candidates() throws InstantiableException {
		final List<OpCandidate<Ops.Thread.Chunker>> candidates = matcher
			.findCandidates(ops, new OpRef<>(null, Ops.Thread.Chunker.class, null,
				null));
		assertEquals(2, candidates.size());
		assertSame(DefaultChunker.class, candidates.get(0).cInfo().loadClass());
		assertSame(ChunkerInterleaved.class, candidates.get(1).cInfo().loadClass());
	}

	@Test
	public void matches() throws ClassNotFoundException {
		final Module match = matcher.findModule(ops, OpRef.create(
			Ops.Thread.Chunker.class, Chunk.class, 1));
		assertSame(DefaultChunker.class, match.getInfo().loadDelegateClass());
	}

}
