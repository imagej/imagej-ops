/*-
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
package net.imagej.ops.filter;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.filter.bilateral.DefaultBilateral;
import net.imagej.ops.filter.gauss.GaussRAISingleSigma;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Test;

public class DefaultBilateralTest extends AbstractOpTest {

	@Test
	public void testBigImage() {
		final byte[] data = { 7, 8, 9, 1, 2, 3, 7, 9, 8, 1, 3, 2, 8, 7, 9, 2, 1, 3, 8, 9, 7, 2, 3, 1, 9, 7, 8, 3, 1, 2,
				9, 8, 7, 3, 2, 1 };
		final Img<ByteType> in = ArrayImgs.bytes(data, 6, 6);
		final Img<ByteType> out = generateByteArrayTestImg(false, 6, 6);

		ops.run(DefaultBilateral.class, out, in, 15, 5, 2);

		final byte[] expected = { 8, 7, 6, 4, 3, 2, 8, 7, 6, 4, 3, 2, 8, 7, 6, 4, 3, 2, 8, 7, 6, 4, 3, 2, 8, 7, 6, 4, 3,
				2, 8, 7, 6, 4, 3, 2 };

		Cursor<ByteType> cout = out.cursor();
		for (int i = 0; i < expected.length; i++) {
			assertEquals(cout.next().get(), expected[i]);
		}
	}

	@Test
	public void testMath() {
		final byte[] data = { 7, 4, 9, 1 };
		final Img<ByteType> in = ArrayImgs.bytes(data, 2, 2);
		final Img<ByteType> out = generateByteArrayTestImg(false, 2, 2);

		ops.run(DefaultBilateral.class, out, in, 15, 5, 1);

		Cursor<ByteType> cout = out.cursor();
		final byte[] expected = { 5, 5, 5, 5 };
		int counter = 0;
		while (cout.hasNext()) {
			byte actual = cout.next().get();
			assertEquals(expected[counter++], actual);
		}
	}

	@Test
	public void testArrayToCellImg() {

		final byte[] data = { 7, 8, 9, 1, 2, 3, 7, 9, 8, 1, 3, 2, 8, 7, 9, 2, 1, 3, 8, 9, 7, 2, 3, 1, 9, 7, 8, 3, 1, 2,
				9, 8, 7, 3, 2, 1 };
		final Img<ByteType> in = ArrayImgs.bytes(data, 6, 6);
		final Img<ByteType> out = generateByteArrayTestImg(false, 6, 6);
		final Img<ByteType> cellOut = generateByteTestCellImg(false, 6, 6);

		ops.run(DefaultBilateral.class, out, in, 15, 5, 2);
		ops.run(DefaultBilateral.class, cellOut, in, 15, 5, 2);

		Cursor<ByteType> cout = out.cursor();
		Cursor<ByteType> cCellOut = cellOut.cursor();
		while (cout.hasNext()) {
			byte expected = cout.next().get();
			byte actual = cCellOut.next().get();
			assertEquals(expected, actual);
		}
	}

	@Test
	public void testGaussianVsBilateral() {
		final byte[] data = { 7, 8, 9, 1, 2, 3, 7, 9, 8, 1, 3, 2, 8, 7, 9, 2, 1, 3, 8, 9, 7, 2, 3, 1, 9, 7, 8, 3, 1, 2,
				9, 8, 7, 3, 2, 1 };
		final Img<ByteType> in = ArrayImgs.bytes(data, 6, 6);
		final Img<ByteType> gaussOut = generateByteArrayTestImg(false, 6, 6);
		final Img<ByteType> bilateralOut = generateByteTestCellImg(false, 6, 6);

		ops.run(DefaultBilateral.class, bilateralOut, in, 15, 5, 2);
		final double sigma = 5;
		ops.run(GaussRAISingleSigma.class, gaussOut, in, sigma);
		assertEquals(areCongruent(gaussOut, bilateralOut, 0), false);
	}

	@Test
	public void testZeroes() {
		final byte[] data = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0 };
		final Img<ByteType> in = ArrayImgs.bytes(data, 6, 6);
		final Img<ByteType> out = generateByteArrayTestImg(false, 6, 6);

		ops.run(DefaultBilateral.class, out, in, 15, 5, 2);

		Cursor<ByteType> cout = out.cursor();
		while (cout.hasNext()) {
			byte expected = cout.next().get();
			assertEquals(expected, 0);
		}
	}

	@Test
	public void testNegatives() {
		final byte[] data = { -7, -8, -9, -1, -2, -3, -7, -9, -8, -1, -3, -2, -8, -7, -9, -2, -1, -3, -8, -9, -7, -2,
				-3, -1, -9, -7, -8, -3, -1, -2, -9, -8, -7, -3, -2, -1 };
		final Img<ByteType> in = ArrayImgs.bytes(data, 6, 6);
		final Img<ByteType> out = generateByteArrayTestImg(false, 6, 6);

		ops.run(DefaultBilateral.class, out, in, 15, 5, 2);

		final byte[] expected = { -8, -7, -6, -4, -3, -2, -8, -7, -6, -4, -3, -2, -8, -7, -6, -4, -3, -2, -8, -7, -6,
				-4, -3, -2, -8, -7, -6, -4, -3, -2, -8, -7, -6, -4, -3, -2 };

		Cursor<ByteType> cout = out.cursor();
		for (int i = 0; i < expected.length; i++) {
			assertEquals(cout.next().get(), expected[i]);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTooManyDimensions() {
		final byte[] data = { 2, 2, 2, 2, 2, 2, 2, 2 };
		final Img<ByteType> in = ArrayImgs.bytes(data, 2, 2);
		final Img<ByteType> out = generateByteArrayTestImg(false, 2, 2, 2);

		ops.run(DefaultBilateral.class, out, in, 15, 5, 2);

		final byte[] expected = { 2, 2, 2, 2, 2, 2, 2, 2 };

		Cursor<ByteType> cout = out.cursor();
		for (int i = 0; i < expected.length; i++) {
			assertEquals(cout.next().get(), expected[i]);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMismatchedDimensions() {
		final byte[] data = { 1, 1, 1, 1, 1, 1 };
		final Img<ByteType> in = ArrayImgs.bytes(data, 2, 3);
		final Img<ByteType> out = generateByteArrayTestImg(false, 3, 2);

		ops.run(DefaultBilateral.class, out, in, 15, 5, 2);

		final byte[] expected = { 1, 1, 1, 1, 1, 1 };
		Cursor<ByteType> cout = out.cursor();
		for (int i = 0; i < expected.length; i++) {
			assertEquals(cout.next().get(), expected[i]);
		}
	}

}
