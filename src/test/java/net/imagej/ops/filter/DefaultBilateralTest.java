package net.imagej.ops.filter;

import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.filter.bilateral.DefaultBilateral;
import net.imagej.ops.filter.gauss.GaussRAISingleSigma;

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
