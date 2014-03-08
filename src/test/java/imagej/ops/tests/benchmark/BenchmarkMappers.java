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
import imagej.ops.Function;
import imagej.ops.map.Mapper;
import imagej.ops.map.MapperII;
import imagej.ops.map.ThreadedMapper;
import imagej.ops.map.ThreadedMapperII;
import imagej.ops.tests.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Benchmarking various implementations of mappers. Benchmarked since now:
 * {@link Mapper}, {@link MapperII}, {@link ThreadedMapper},
 * {@link ThreadedMapperII}
 * 
 * @author Christian Dietz
 */
public class BenchmarkMappers extends AbstractOpTest {

	private Img<ByteType> in;
	private int numRuns;
	private Img<ByteType> out;

	@Before
	public void initImg() {
		in = generateByteTestImg(true, 10000, 10000);
		out = generateByteTestImg(false, 10000, 10000);
		numRuns = 10;
	}

	@Test
	public void pixelWiseTestMapper() {
		final Module asModule = ops.module(new Mapper<ByteType, ByteType>());

		asModule.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		asModule.setInput("in", in);
		asModule.setInput("out", out);

		System.out.println("[Mapper] Runtime " +
			asMilliSeconds(bestOf(asModule, numRuns)) + "!");
	}

	@Test
	public void pixelWiseTestMapperII() {
		final Module asModule = ops.module(new MapperII<ByteType, ByteType>());

		asModule.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		asModule.setInput("in", in);
		asModule.setInput("out", out);

		System.out.println("[MapperII] Runtime " +
			asMilliSeconds(bestOf(asModule, numRuns)) + "!");
	}

	@Test
	public void pixelWiseTestThreadedMapper() {
		final Module asModule =
			ops.module(new ThreadedMapper<ByteType, ByteType>());

		asModule.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		asModule.setInput("in", in);
		asModule.setInput("out", out);

		System.out.println("[ThreadedMapper] Runtime " +
			asMilliSeconds(bestOf(asModule, numRuns)) + "!");
	}

	@Test
	public void pixelWiseTestThreadedMapperII() {
		final Module asModule =
			ops.module(new ThreadedMapperII<ByteType, ByteType>());

		asModule.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		asModule.setInput("in", in);
		asModule.setInput("out", out);

		System.out.println("[ThreadedMapperII] Runtime " +
			asMilliSeconds(bestOf(asModule, numRuns)) + "!");
	}

	private double asMilliSeconds(final long nanoTime) {
		return nanoTime / 1000.0d / 1000.d;
	}

	public class DummyPixelOp<T extends RealType<T>, V extends RealType<V>>
		extends Function<T, V>
	{

		double constant = -5;

		@Override
		public V compute(final T input, final V output) {
			output.setReal(input.getRealDouble() + constant);
			return output;
		}

		@Override
		public Function<T, V> copy() {
			return new DummyPixelOp<T, V>();
		}
	}
}
