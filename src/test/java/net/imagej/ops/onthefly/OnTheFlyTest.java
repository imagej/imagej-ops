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
package net.imagej.ops.onthefly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.MathOps;
import net.imagej.ops.Op;
import net.imagej.ops.onthefly.ArithmeticOp.AddOp;
import net.imagej.ops.onthefly.ArithmeticOp.DivideOp;
import net.imagej.ops.onthefly.ArithmeticOp.SubtractOp;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.ShortArray;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.img.planar.PlanarImgs;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.ShortType;

import org.junit.Test;

/**
 * Tests the {@link Op} generator.
 * 
 * @author Johannes Schindelin
 */
public class OnTheFlyTest extends AbstractOpTest {
	private final int pixelCount = 256 * 256;
	private final long[] dimensions = new long[] { 256, 256 };

	/** Tests the "add" op on byte typed images. */
	@Test
	public void testByte() {
		final byte[] array = new byte[pixelCount];
		final ArrayImg<ByteType, ByteArray> img = ArrayImgs.bytes(array, dimensions);
		final byte[] array2 = new byte[pixelCount];
		final ArrayImg<ByteType, ByteArray> img2 = ArrayImgs.bytes(array2, dimensions);
		final byte[] result = new byte[pixelCount];
		final ArrayImg<ByteType, ByteArray> resultImg = ArrayImgs.bytes(result, dimensions);

		for (int i = 0; i < array.length; i++) {
			array[i] = (byte) i;
			array2[i] = (byte) (5 + ((3 * i) % 7));
			assertEquals((byte) 0, result[i]);
		}

		ops.run(AddOp.class, resultImg, img, img2);

		for (int i = 0; i < array.length; i++) {
			assertEquals("index " + i, (byte) (i + (5 + ((3 * i) % 7))), result[i]);
		}
	}

	/** Tests the "add" op on short typed images. */
	@Test
	public void testShort() {
		final short[] array = new short[pixelCount];
		final ArrayImg<ShortType, ShortArray> img = ArrayImgs.shorts(array, dimensions);
		final short[] array2 = new short[pixelCount];
		final ArrayImg<ShortType, ShortArray> img2 = ArrayImgs.shorts(array2, dimensions);
		final short[] result = new short[pixelCount];
		final ArrayImg<ShortType, ShortArray> resultImg = ArrayImgs.shorts(result, dimensions);

		for (int i = 0; i < array.length; i++) {
			array[i] = (short) i;
			array2[i] = (short) (5 + ((3 * i) % 7));
			assertEquals((short) 0, result[i]);
		}

		ops.run(AddOp.class, resultImg, img, img2);

		for (int i = 0; i < array.length; i++) {
			assertEquals("index " + i, (short) (i + (5 + ((3 * i) % 7))), result[i]);
		}
	}


	/** Tests the "divide" op on byte typed images. */
	@Test
	public void testDivide() {
		final short[] array = new short[512];
		final ArrayImg<ShortType, ShortArray> img = ArrayImgs.shorts(array, dimensions);

		for (int i = 0; i < array.length; i++) {
			array[i] = (short) (i + 1);
		}

		ops.run(DivideOp.class, img, img, img);

		for (int i = 0; i < array.length; i++) {
			assertEquals("index " + i, (short) 1, array[i]);
		}
	}

	/** Tests the "subtract" op on int typed {@link PlanarImg}s. */
	@Test
	public void testPlanar() {
		final PlanarImg<IntType, ?> a = PlanarImgs.ints(dimensions);
		final PlanarImg<IntType, ?> b = PlanarImgs.ints(dimensions);
		final PlanarImg<IntType, ?> result = PlanarImgs.ints(dimensions);
		int i = 0;
		for (final IntType t : a) {
			t.set(i++);
		}
		for (final IntType t : b) {
			t.set(i-- / 2);
		}

		ops.run(SubtractOp.class, result, a, b);

		i = 0;
		for (final IntType t : result) {
			assertEquals("index " + i, i - (pixelCount - i) / 2, t.get());
			i++;
		}
	}

	/** Tests the "add" op on a byte typed image and a constant. */
	@Test
	public void testByteImgPlusConstant() {
		final byte[] array = new byte[pixelCount];
		final ArrayImg<ByteType, ByteArray> img = ArrayImgs.bytes(array, dimensions);

		for (int i = 0; i < array.length; i++) {
			array[i] = (byte) i;
		}

		ops.run(AddOp.class, img, img, new ByteType((byte) 17));

		for (int i = 0; i < array.length; i++) {
			assertEquals("index " + i, (byte) (i + 17), array[i]);
		}
	}

	/** Verifies that the OpMatcher gets a chance to do things. */
	@Test
	public void testOpMatcher() {
		final Object result = generateByteTestImg(false, 256, 256);
		final Object a = generateByteTestImg(false, 256, 256);
		final Object b = generateByteTestImg(false, 256, 256);
		final Op op = ops.op(MathOps.Add.class, result, a, b);
		assertTrue("Not a subclass of " + ArithmeticOp.class + ": " + op.getClass(), op instanceof ArithmeticOp);
		assertTrue("Not optimized: " + op.getClass(), op.getClass() != AddOp.class);
	}
}
