/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

package net.imagej.ops.math;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import net.imagej.ops.Ops;
import net.imagej.ops.benchmark.AbstractOpBenchmark;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

@BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 20)
public class MathBenchmarkTest extends AbstractOpBenchmark {

	ArrayImg<FloatType, ?> img1;
	ArrayImg<FloatType, ?> img2;
	ArrayImg<FloatType, ?> img3;
	ArrayImg<FloatType, ?> imgzero;

	float[] float1;
	float[] float2;
	float[] float3;
	float[] float4;

	Img<ByteType> byteimg;

	long x = 5000;
	long y = 5000;
	long size = x * y;

	/** Needed for JUnit-Benchmarks */
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	private ArrayImg<FloatType, ?> createConstantImg(long[] dims,
		float constant)
	{
		// create an input
		ArrayImg<FloatType, ?> img = new ArrayImgFactory<FloatType>().create(dims,
			new FloatType());

		for (final FloatType value : img) {
			value.setReal(constant);
		}

		return img;

	}

	@Before
	public void setUpImgs() {
		// create an input
		img1 = createConstantImg(new long[] { x, y }, 1.0f);
		img2 = createConstantImg(new long[] { x, y }, 2.0f);
		img3 = createConstantImg(new long[] { x, y }, 3.0f);
		imgzero = createConstantImg(new long[] { x, y }, 0.0f);

		byteimg = new ArrayImgFactory<ByteType>().create(new long[] { 20000,
			20000 }, new ByteType());

		float1 = new float[(int) size];
		float2 = new float[(int) size];
		float3 = new float[(int) size];
		float4 = new float[(int) size];

		for (int i = 0; i < size; i++) {
			float1[i] = 1.0f;
			float2[i] = 2.0f;
			float3[i] = 3.0f;
			float4[i] = 0.0f;

		}

	}

	@Test
	public void testDivide() {
		ops.run(Ops.Math.Divide.class, img3, img2, img1);
	}

	@Test
	public void testDivideIterableIntervalToImg() {
		ops.run(IIToIIOutputII.Divide.class, img3, img2, img1);
	}

	@Test
	public void testDivideExplicit() {
		for (int i = 0; i < size; i++) {
			float3[i] = float2[i] / float1[i];
		}
	}

	@Test
	public void testDivideWithCursorExplicit() {
		final Cursor<FloatType> cursor = img1.cursor();
		final Cursor<FloatType> cursorI = img2.cursor();
		final Cursor<FloatType> cursorO = img3.cursor();

		while (cursor.hasNext()) {
			cursor.fwd();
			cursorI.fwd();
			cursorO.fwd();

			cursorO.get().set(cursorI.get());
			cursorO.get().div(cursor.get());
		}

	}

}
