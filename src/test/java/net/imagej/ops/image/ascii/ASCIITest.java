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

package net.imagej.ops.image.ascii;

import static org.junit.Assert.assertTrue;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.Ops.Image.ASCII}.
 * 
 * @author Leon Yang
 */
public class ASCIITest extends AbstractOpTest {

	@Test
	public void testDefaultASCII() {
		// character set used in DefaultASCII, could be updated if necessary
		final String CHARS = "#O*o+-,. ";
		final int len = CHARS.length();
		final int width = 10;
		final int offset = 47;
		final byte[] array = new byte[width * len];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < width; j++) {
				array[i * width + j] = (byte) (offset + i * width + j);
			}
		}
		final Img<UnsignedByteType> img = ArrayImgs.unsignedBytes(array, width,
			len);
		final String ascii = (String) ops.run(DefaultASCII.class, img);
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < width; j++) {
				assertTrue(ascii.charAt(i * (width + 1) + j) == CHARS.charAt(i));
			}
			assertTrue(ascii.charAt(i * (width + 1) + width) == '\n');
		}
	}
}
