/*
 * #%L
 * A framework for reusable algorithms.
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
package imagej.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.ShortArray;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.ShortType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;


public class OneTheFlyTest {
	private Context context;
	private OpService ops;

	@Before
	public void setUp() {
		context = new Context(OpService.class);
		ops = context.getService(OpService.class);
		assertTrue(ops != null);
	}

	@After
	public synchronized void cleanUp() {
		if (context != null) {
			context.dispose();
			context = null;
		}
	}

	@Test
	public void testByte() {
		final byte[] array = new byte[65536];
		final ArrayImg<ByteType, ByteArray> img = ArrayImgs.bytes(array, new long[] { 256, 256 });
		final byte[] array2 = new byte[65536];
		final ArrayImg<ByteType, ByteArray> img2 = ArrayImgs.bytes(array2, new long[] { 256, 256 });
		final byte[] result = new byte[65536];
		final ArrayImg<ByteType, ByteArray> resultImg = ArrayImgs.bytes(result, new long[] { 256, 256 });

		for (int i = 0; i < array.length; i++) {
			array[i] = (byte) i;
			array2[i] = (byte) (5 + ((3 * i) % 7));
			assertEquals((byte) 0, result[i]);
		}

		ops.add(img, img2, resultImg);

		for (int i = 0; i < array.length; i++) {
			assertEquals("index " + i, (byte) (i + (5 + ((3 * i) % 7))), result[i]);
		}
	}

	@Test
	public void testShort() {
		final short[] array = new short[65536];
		final ArrayImg<ShortType, ShortArray> img = ArrayImgs.shorts(array, new long[] { 256, 256 });
		final short[] array2 = new short[65536];
		final ArrayImg<ShortType, ShortArray> img2 = ArrayImgs.shorts(array2, new long[] { 256, 256 });
		final short[] result = new short[65536];
		final ArrayImg<ShortType, ShortArray> resultImg = ArrayImgs.shorts(result, new long[] { 256, 256 });

		for (int i = 0; i < array.length; i++) {
			array[i] = (short) i;
			array2[i] = (short) (5 + ((3 * i) % 7));
			assertEquals((short) 0, result[i]);
		}

		ops.add(img, img2, resultImg);

		for (int i = 0; i < array.length; i++) {
			assertEquals("index " + i, (short) (i + (5 + ((3 * i) % 7))), result[i]);
		}
	}
}
