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

import net.imagej.ops.arithmetic.add.AddConstantToArrayByteImage;
import net.imagej.ops.arithmetic.add.AddConstantToImageFunctional;
import net.imagej.ops.arithmetic.add.AddConstantToImageInPlace;
import net.imagej.ops.arithmetic.add.AddConstantToNumericType;
import net.imagej.ops.arithmetic.add.parallel.AddConstantToArrayByteImageP;
import net.imagej.ops.map.ParallelMap;
import net.imagej.ops.map.ParallelMapI2I;
import net.imagej.ops.map.ParallelMapI2R;
import net.imagej.ops.onthefly.ArithmeticOp;
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
 * Benchmarks the pixel-wise add operation.
 * 
 * @author Christian Dietz
 */
@BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 1)
public class AddOpBenchmarkTest extends AbstractOpBenchmark {

	private Img<ByteType> in;
	private Img<ByteType> out;

	/** Needed for JUnit-Benchmarks */
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	/** Sets up test images */
	@Before
	public void initImg() {
		in = generateByteTestImg(true, 5000, 5000);
		out = generateByteTestImg(false, 5000, 5000);
	}

	@Test
	public void fTestIterableIntervalMapperP() {
		ops.module(ParallelMapI2I.class, out, in, ops.op(
			AddConstantToNumericType.class, null, NumericType.class, new ByteType((byte) 10))).run();
	}

	@Test
	public void fTestDefaultMapperP() {
		ops.module(ParallelMapI2R.class, out, in, ops.op(
			AddConstantToNumericType.class, null, NumericType.class, new ByteType((byte) 10))).run();
	}

	@Test
	public void fTtestAddConstantToImage() {
		ops.module(new AddConstantToImageFunctional<ByteType>(), out, in,
			new ByteType((byte) 10)).run();
	}

	@Test
	public void inTestDefaultInplaceMapperP() {
		ops.module(ParallelMap.class, in, ops.op(
			AddConstantInplace.class, NumericType.class, new ByteType((byte) 10))).run();
	}

	@Test
	public void inTestJavaAssist() {
		ops.module(new ArithmeticOp.AddOp(), in, in, new ByteType((byte) 10)).run();
	}

	@Test
	public void inTestAddConstantToImageInPlace() {
		ops.module(new AddConstantToImageInPlace<ByteType>(), in, new ByteType(
			(byte) 10)).run();
	}

	@Test
	public void inTestAddConstantToArrayByteImage() {
		ops.module(new AddConstantToArrayByteImage(), in, (byte) 10).run();
	}

	@Test
	public void inTestAddConstantToArrayByteImageP() {
		ops.module(new AddConstantToArrayByteImageP(), in, (byte) 10).run();
	}
}
