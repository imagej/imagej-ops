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

package net.imagej.ops.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops;
import net.imagej.ops.convert.ConvertTypes.ComplexToFloat32;
import net.imagej.ops.convert.ConvertTypes.ComplexToUint8;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;

import org.junit.Test;

/**
 * Tests that the {@code convert} ops work on {@link Img} objects via
 * {@code ops.map}.
 *
 * @author Alison Walter
 */
public class ConvertMapTest extends AbstractOpTest {

	private final static long[] dims = { 3, 3 };

	@Test
	public void testLossless() {

		final byte[] inArray = { 12, 122, 9, -6, 56, 34, 108, 1, 73 };
		final Img<UnsignedByteType> in = generateUnsignedByteImg(inArray);

		final float[] outArray =
			{ 134.7f, -13089.3208f, 209.3f, 0.6f, 84.0f, -543.1f, 0f, 34.908f,
				592087.0957f };
		final Img<FloatType> out = generateFloatImg(outArray);

		final Cursor<UnsignedByteType> inC1 = in.cursor();
		final Cursor<FloatType> outC1 = out.cursor();

		while (inC1.hasNext()) {
			assertNotEquals(inC1.next().getRealDouble(),
				outC1.next().getRealDouble(), 0d);
		}

		ops.run(Ops.Map.class, out, in, new ComplexToFloat32<UnsignedByteType>());

		final Cursor<UnsignedByteType> inC2 = in.cursor();
		final Cursor<FloatType> outC2 = out.cursor();

		while (inC2.hasNext()) {
			assertEquals(inC2.next().getRealDouble(), outC2.next().getRealDouble(), 0);
		}
	}

	@Test
	public void testLossy() {

		final float[] inArray =
			{ 12.7f, -13089.3208f, 78.023f, 0.04f, 12.01f, -1208.90f, 109432.109f,
				1204.88f, 87.6f };
		final Img<FloatType> in = generateFloatImg(inArray);

		final byte[] outArray = { 4, 123, 18, 64, 90, 120, 12, 17, 73 };
		final Img<UnsignedByteType> out = generateUnsignedByteImg(outArray);

		ops.run(Ops.Map.class, out, in, new ComplexToUint8<FloatType>());

		final Cursor<FloatType> inC = in.cursor();
		final Cursor<UnsignedByteType> outC = out.cursor();

		while (inC.hasNext()) {

			final double inV = inC.next().getRealDouble();
			final double outV = outC.next().getRealDouble();

			// values won't be equal because the conversion is lossy
			assertNotEquals(inV, outV, 0);

			// uint8 masks float values to be 8 bits
			assertEquals(Types.uint8(inV), outV, 0);

		}
	}

	// -- Helper methods --
	private static Img<FloatType> generateFloatImg(final float[] values) {

		final float[] array =
			new float[(int) Intervals.numElements(new FinalInterval(dims))];

		if (array.length != values.length) {
			throw new RuntimeException("Number of values doesn't match dimmensions");
		}

		for (int i = 0; i < array.length; i++) {
			array[i] = values[i];
		}

		return ArrayImgs.floats(array, dims);
	}

	private static Img<UnsignedByteType> generateUnsignedByteImg(
		final byte[] values)
	{

		final byte[] array =
			new byte[(int) Intervals.numElements(new FinalInterval(dims))];

		if (array.length != values.length) {
			throw new RuntimeException("Number of values doesn't match dimmensions");
		}

		for (int i = 0; i < array.length; i++) {
			array[i] = values[i];
		}

		return ArrayImgs.unsignedBytes(array, dims);
	}

}
