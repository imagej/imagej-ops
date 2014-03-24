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

package imagej.ops.benchmark;

import imagej.ops.Op;
import imagej.ops.map.MapI;
import imagej.ops.map.MapII2II;
import imagej.ops.map.MapII2RAI;
import imagej.ops.map.ParallelMap;
import imagej.ops.map.ParallelMapI2I;
import imagej.ops.map.ParallelMapI2R;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * Benchmarking various implementations of mappers. Benchmarked since now:
 * {@link MapII2RAI}, {@link MapII2II},
 * {@link ParallelMapI2R}, {@link ParallelMapI2I}
 * 
 * @author Christian Dietz
 */
@BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 1)
public class MappersBenchmarkTest extends AbstractOpBenchmark {

	private Img<ByteType> in;
	private Img<ByteType> out;
	private Op addConstant;
	private Op addConstantInplace;

	/** Needed for JUnit-Benchmarks */
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	@Before
	public void initImg() {
		in = generateByteTestImg(true, 1000, 1000);
		out = generateByteTestImg(false, 1000, 1000);

		addConstant = ops.op("add", null, NumericType.class, new ByteType((byte) 5));
		addConstantInplace = ops.op(AddConstantInplace.class, NumericType.class, new ByteType((byte) 5));
	}

	@Test
	public void pixelWiseTestMapper() {
		ops.module(new MapII2RAI<ByteType, ByteType>(), out, in,
			addConstant).run();
	}

	@Test
	public void pixelWiseTestMapperII() {
		ops.module(new MapII2II<ByteType, ByteType>(), out, in,
			addConstant).run();
	}

	@Test
	public void pixelWiseTestThreadedMapper() {
		ops.module(new ParallelMapI2R<ByteType, ByteType>(), out, in,
			addConstant).run();
	}

	@Test
	public void pixelWiseTestThreadedMapperII() {
		ops.module(new ParallelMapI2I<ByteType, ByteType>(), out, in,
			addConstant, out).run();
	}

	@Test
	public void pixelWiseTestMapperInplace() {
		ops.module(new MapI<ByteType>(), in, addConstantInplace).run();
	}

	@Test
	public void pixelWiseTestThreadedMapperInplace() {
		ops.module(new ParallelMap<ByteType>(), in.copy(), addConstantInplace).run();
	}
}
