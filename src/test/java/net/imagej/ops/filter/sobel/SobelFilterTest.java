/* #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.filter.sobel;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

import org.junit.Test;

/**
 * Test for sobel op using separated kernel.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 */

public class SobelFilterTest extends AbstractOpTest {

	@Test
	public void test() {

		Img<FloatType> img = generateFloatArrayTestImg(false, new long[] { 20, 20 });

		Cursor<FloatType> cursorImg = img.cursor();
		int counterX = 0;
		int counterY = 0;
		while (cursorImg.hasNext()) {
			if (counterX > 8 && counterX < 12 || counterY > 8 && counterY < 12) {
				cursorImg.next().setOne();
			} else {
				cursorImg.next().setZero();
			}
			counterX++;
			if (counterX % 20 == 0) {
				counterY++;
			}
			if (counterX == 20) {
				counterX = 0;
			}
			if (counterY == 20) {
				counterY = 0;
			}
		}
		RandomAccessibleInterval<FloatType> out = ops.filter().sobel(img);

		RandomAccess<FloatType> outRA = out.randomAccess();
		outRA.setPosition(new int[] { 0, 8 });
		FloatType type = Util.getTypeFromInterval(out).createVariable();
		type.set(4.0f);
		assertEquals(type, outRA.get());
		type.setZero();
		outRA.setPosition(new int[] { 0, 10 });
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] { 10, 8 });
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] { 10, 10 });
		assertEquals(type, outRA.get());

		outRA.setPosition(new int[] { 10, 12 });
		type.set(0.0f);
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] { 12, 10 });
		assertEquals(type, outRA.get());
	}

}
