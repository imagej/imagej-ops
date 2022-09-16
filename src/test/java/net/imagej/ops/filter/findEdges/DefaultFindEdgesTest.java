/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 ImageJ developers.
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

package net.imagej.ops.filter.findEdges;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Test;

public class DefaultFindEdgesTest extends AbstractOpTest {

	@Test
	public void testRegression() {

		long[] dims = { 314, 60 };
		Img<UnsignedByteType> input = (Img<UnsignedByteType>) ops.run(Ops.Create.Img.class,
			dims, new UnsignedByteType());
		RandomAccess<UnsignedByteType> ra = input.randomAccess();

		// add sin curve and cosine curve
		for (int i = 0; i < input.dimension(0); i++) {
			ra.setPosition(i, 0);
			double cos = 30 * Math.cos(((double) i )/ 10);
			double sin = 30 * Math.sin(((double) i )/ 10);
			int top = (int) (cos < sin ? cos : sin);
			int bottom = (int) (cos < sin ? sin : cos);
			
			for (int j = 30 + top; j < 30 + bottom; j++) {
				ra.setPosition(j, 1);
				ra.get().setReal(105);
			}
		}
		
		//get output
		Img<UnsignedByteType> output = (Img<UnsignedByteType>) ops.run(Ops.Filter.FindEdges.class, input);

		//load in expected output
		Img<UnsignedByteType> expected = (Img<UnsignedByteType>) openUnsignedByteType(getClass(), "DefaultFindEdgesExpected.png");
		Cursor<UnsignedByteType> expectedCursor = expected.cursor();
		Cursor<UnsignedByteType> actualCursor = output.cursor();
		
		//loop through images assert equal to expect
		while(actualCursor.hasNext()) {
			assertEquals(expectedCursor.next().get(), actualCursor.next().get());
			
		}

	}
}
