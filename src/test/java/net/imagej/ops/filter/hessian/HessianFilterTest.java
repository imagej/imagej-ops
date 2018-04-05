/* #%L
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

package net.imagej.ops.filter.hessian;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.RealComposite;

import org.junit.Test;

/**
 * Test for Hessian matrice op.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 */

public class HessianFilterTest extends AbstractOpTest {

	@Test
	public void test() {
		Img<FloatType> img = generateFloatArrayTestImg(false, new long[] { 50, 50 });

		Cursor<FloatType> cursorImg = img.cursor();
		int counterX = 0;
		int counterY = 0;
		while (cursorImg.hasNext()) {
			if (counterX > 20 && counterX < 30 || counterY > 20 && counterY < 30) {
				cursorImg.next().setOne();
			} else {
				cursorImg.next().setZero();
			}
			counterX++;
			if (counterX % 50 == 0) {
				counterY++;
			}
			if (counterX == 50) {
				counterX = 0;
			}
			if (counterY == 50) {
				counterY = 0;
			}
		}

		CompositeIntervalView<FloatType, RealComposite<FloatType>> out = ops.filter().hessian(img);
		
		
		Cursor<RealComposite<FloatType>> outCursor = Views.iterable(out).cursor();
		
		while (outCursor.hasNext()) {
			RealComposite<FloatType> values = outCursor.next();
			assertEquals(values.get(1), values.get(2));
		}

		CompositeView<FloatType, RealComposite<FloatType>>.CompositeRandomAccess outRA = out.randomAccess();
		
		// two numbers represent a coordinate: 20|0 ; 21|0 ...
		int[] positions = new int[] { 20, 0, 21, 0, 19, 31, 19, 30 };
		float[] valuesXX = new float[] { 16.0f, -16.0f, 15.0f, 11.0f };
		float[] valuesXY = new float[] { 0.0f, 0.0f, 1.0f, 3.0f };
		float[] valuesYY = new float[] { 0.0f, 0.0f, 15.0f, 15.0f };

		FloatType type = Util.getTypeFromInterval(img).createVariable();
		int i = 0;
		int j = 0;
		while (i < positions.length - 1) {
			int[] pos = new int[2];
			pos[0] = positions[i];
			pos[1] = positions[i + 1];

			outRA.setPosition(pos);
			type.set(valuesXX[j]);
			assertEquals(type, outRA.get().get(0));

			outRA.setPosition(pos);
			type.set(valuesXY[j]);
			assertEquals(type, outRA.get().get(1));

			outRA.setPosition(pos);
			type.set(valuesYY[j]);
			assertEquals(type, outRA.get().get(3));

			i += 2;
			j++;
		}
	}

}
