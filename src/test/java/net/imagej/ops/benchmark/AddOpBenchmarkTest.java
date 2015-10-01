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

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import net.imagej.ops.map.MapIterableToIterableParallel;
import net.imagej.ops.map.MapIterableToRAIParallel;
import net.imagej.ops.map.MapParallel;
import net.imagej.ops.math.ConstantToArrayImage;
import net.imagej.ops.math.ConstantToArrayImageP;
import net.imagej.ops.math.ConstantToImageFunctional;
import net.imagej.ops.math.ConstantToImageInPlace;
import net.imagej.ops.math.NumericTypeBinaryMath;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

/**
 * Benchmarks the pixel-wise add operation.
 * 
 * @author Christian Dietz (University of Konstanz)
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
		ops.run(MapIterableToIterableParallel.class, out, in, ops.op(
			NumericTypeBinaryMath.Add.class, null, NumericType.class, new ByteType((byte) 10)));
	}

	@Test
	public void fTestDefaultMapperP() {
		ops.run(MapIterableToRAIParallel.class, out, in, ops.op(
			NumericTypeBinaryMath.Add.class, null, NumericType.class, new ByteType((byte) 10)));
	}

	@Test
	public void fTtestAddConstantToImage() {
		ops.run(new ConstantToImageFunctional.Add<ByteType>(), out, in,
			new ByteType((byte) 10));
	}

	@Test
	public void inTestDefaultInplaceMapperP() {
		ops.run(MapParallel.class, in, ops.op(
			AddConstantInplace.class, NumericType.class, new ByteType((byte) 10)));
	}

	@Test
	public void inTestAddConstantToImageInPlace() {
		ops.run(new ConstantToImageInPlace.Add<ByteType>(), in, new ByteType(
			(byte) 10));
	}

	@Test
	public void inTestAddConstantToArrayByteImage() {
		ops.run(new ConstantToArrayImage.AddByte(), in, (byte) 10);
	}

	@Test
	public void inTestAddConstantToArrayByteImageP() {
		ops.run(new ConstantToArrayImageP.AddByte(), in, (byte) 10);
	}
}
