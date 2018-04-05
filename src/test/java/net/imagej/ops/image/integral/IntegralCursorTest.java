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

package net.imagej.ops.image.integral;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link IntegralCursor} implementation.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
public class IntegralCursorTest {

	protected ArrayImg<ByteType, ByteArray> img;

	@Before
	public void setUp() throws Exception {
		this.img = generateKnownByteArrayTestImg();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testIntegralCursor() {
		Shape rectangleShape = new RectangleShape(1, false);
		IterableInterval<Neighborhood<ByteType>> ii = rectangleShape
			.neighborhoodsSafe(img);
		Cursor<Neighborhood<ByteType>> cursor = ii.cursor();

		// Manually select the neighborhood that is centered on the image
		cursor.fwd();
		cursor.fwd();
		cursor.fwd();
		cursor.fwd();
		cursor.fwd();

		IntegralCursor<ByteType> integralCursor = new IntegralCursor<>(
			(RectangleNeighborhood) cursor.get());

		assertEquals(integralCursor.next(), new ByteType((byte) 1));
		assertEquals(integralCursor.next(), new ByteType((byte) 2));
		assertEquals(integralCursor.next(), new ByteType((byte) 5));
		assertEquals(integralCursor.next(), new ByteType((byte) 4));
		assertFalse(integralCursor.hasNext());

		integralCursor.reset();

		assertEquals(integralCursor.next(), new ByteType((byte) 1));
		assertEquals(integralCursor.next(), new ByteType((byte) 2));
		assertEquals(integralCursor.next(), new ByteType((byte) 5));
		assertEquals(integralCursor.next(), new ByteType((byte) 4));
		assertFalse(integralCursor.hasNext());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testIntegralCursorCopyConstructor() {
		Shape rectangleShape = new RectangleShape(1, false);
		IterableInterval<Neighborhood<ByteType>> ii = rectangleShape
			.neighborhoodsSafe(img);
		Cursor<Neighborhood<ByteType>> cursor = ii.cursor();

		// Manually select the neighborhood that is centered on the image
		cursor.fwd();
		cursor.fwd();
		cursor.fwd();
		cursor.fwd();
		cursor.fwd();

		IntegralCursor<ByteType> integralCursor = new IntegralCursor<>(
			(RectangleNeighborhood) cursor.get());
		assertEquals(integralCursor.next(), new ByteType((byte) 1));
		assertEquals(integralCursor.next(), new ByteType((byte) 2));

		IntegralCursor<ByteType> integralCursor2 = new IntegralCursor<>(
			integralCursor);
		assertEquals(integralCursor2.next(), new ByteType((byte) 5));
		assertEquals(integralCursor2.next(), new ByteType((byte) 4));

		assertFalse(integralCursor2.hasNext());
	}

	public ArrayImg<ByteType, ByteArray> generateKnownByteArrayTestImg() {
		final long[] dims = new long[] { 3, 3 };
		final byte[] array = new byte[9];

		array[0] = (byte) 1;
		array[1] = (byte) 2;
		array[2] = (byte) 3;

		array[3] = (byte) 4;
		array[4] = (byte) 5;
		array[5] = (byte) 6;

		array[6] = (byte) 7;
		array[7] = (byte) 8;
		array[8] = (byte) 9;

		return ArrayImgs.bytes(array, dims);
	}

}
