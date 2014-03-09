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

package imagej.ops.tests.benchmark;

import imagej.module.Module;
import imagej.ops.Op;
import imagej.ops.map.DefaultInplaceMapper;
import imagej.ops.map.DefaultFunctionalMapper;
import imagej.ops.map.IterableIntervalMapper;
import imagej.ops.map.parallel.DefaultInplaceMapperP;
import imagej.ops.map.parallel.DefaultMapperP;
import imagej.ops.map.parallel.IterableIntervalMapperP;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;

/**
 * Benchmarking various implementations of mappers. Benchmarked since now:
 * {@link DefaultFunctionalMapper}, {@link IterableIntervalMapper}, {@link DefaultMapperP},
 * {@link IterableIntervalMapperP}
 * 
 * @author Christian Dietz
 */
public class MappersBenchmark extends AbstractOpBenchmark {

	private Img<ByteType> in;
	private int numRuns;
	private Img<ByteType> out;
	private Op addConstant;

	// run the benchmarks
	public static void main(final String[] args) {
		final MappersBenchmark mappersBenchmark = new MappersBenchmark();

		mappersBenchmark.setUp();
		mappersBenchmark.initImg();

		// run the benchmarks
		mappersBenchmark.pixelWiseTestMapper();
		mappersBenchmark.pixelWiseTestMapperII();
		mappersBenchmark.pixelWiseTestMapperInplace();
		mappersBenchmark.pixelWiseTestThreadedMapper();
		mappersBenchmark.pixelWiseTestThreadedMapperII();
		mappersBenchmark.pixelWiseTestThreadedMapperInplace();

		mappersBenchmark.cleanUp();
	}

	@Before
	public void initImg() {
		in = generateByteTestImg(true, 1000, 1000);
		out = generateByteTestImg(false, 1000, 1000);
		addConstant = ops.op("add", (byte) 5);
		numRuns = 10;
	}

	public void pixelWiseTestMapper() {
		final Module module =
			ops.module(new DefaultFunctionalMapper<ByteType, ByteType>(), out, in, addConstant);

		benchmarkAndPrint(DefaultFunctionalMapper.class.getSimpleName(), module, numRuns);
	}

	public void pixelWiseTestMapperII() {
		final Module module =
			ops.module(new IterableIntervalMapper<ByteType, ByteType>(), out, in, addConstant);

		benchmarkAndPrint(IterableIntervalMapper.class.getSimpleName(), module, numRuns);
	}

	public void pixelWiseTestThreadedMapper() {
		final Module module =
			ops
				.module(new DefaultMapperP<ByteType, ByteType>(), out, in, addConstant);

		benchmarkAndPrint(DefaultMapperP.class.getSimpleName(), module, numRuns);
	}

	public void pixelWiseTestThreadedMapperII() {
		final Module module =
			ops.module(new IterableIntervalMapperP<ByteType, ByteType>(), out, in,
				addConstant, out);

		benchmarkAndPrint(IterableIntervalMapperP.class.getSimpleName(), module, numRuns);
	}

	public void pixelWiseTestMapperInplace() {
		final Module module =
			ops.module(new DefaultInplaceMapper<ByteType>(), in, addConstant);

		benchmarkAndPrint(DefaultInplaceMapper.class.getSimpleName(), module, numRuns);
	}

	public void pixelWiseTestThreadedMapperInplace() {
		final Module module =
			ops.module(new DefaultInplaceMapperP<ByteType>(), in.copy(),
				addConstant);

		benchmarkAndPrint(DefaultInplaceMapperP.class.getSimpleName(), module,
			numRuns);
	}
}
