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

package net.imagej.ops.image.invert;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Test;

/**
 * @author Martin Horn (University of Konstanz)
 */
public class InvertTest extends AbstractOpTest {

	@Test
	public void testInvertSigned() {

		// signed type test
		Img<ByteType> in = generateByteArrayTestImg(true, 5, 5);
		Img<ByteType> out = in.factory().create(in, new ByteType());

		ops.image().invert(out, in);

		ByteType firstIn = in.firstElement();
		ByteType firstOut = out.firstElement();

		assertEquals(firstIn.get() * -1 - 1, firstOut.get());

	}

	@Test
	public void testInvertUnsigned() {

		// unsigned type test
		Img<UnsignedByteType> in = generateUnsignedByteArrayTestImg(true, 5, 5);
		Img<UnsignedByteType> out = in.factory().create(in, new UnsignedByteType());

		ops.image().invert(out, in);

		UnsignedByteType firstIn = in.firstElement();
		UnsignedByteType firstOut = out.firstElement();

		assertEquals((int) firstIn.getMaxValue() - firstIn.getInteger(), firstOut
			.getInteger());

	}
}
