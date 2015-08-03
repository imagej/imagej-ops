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
package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.TransformView;
import net.imglib2.view.Views;

/**
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * This test only checks if the op call works with all parameters and that the 
 * result is equal to the Views.method() call. 
 * This is not a correctness test of {@linkplain net.imglib2.view.Views}.
 */
public class ShearTest extends AbstractOpTest {

	@Test
	public void defaultShearTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 2, 2 }, new DoubleType());
		Cursor<DoubleType> imgC = img.cursor();
		while (imgC.hasNext()) {
			imgC.next().set(1);
		}

		TransformView<DoubleType> il2 = Views.shear(Views.extendZero(img), 0, 1);
		TransformView<DoubleType> opr = ops.view().shear(Views.extendZero(img), 0, 1);
		Cursor<DoubleType> il2C = Views.interval(il2, new FinalInterval(new long[] { 0, 0 }, new long[] { 3, 3 }))
				.cursor();
		RandomAccess<DoubleType> oprRA = Views
				.interval(opr, new FinalInterval(new long[] { 0, 0 }, new long[] { 3, 3 })).randomAccess();

		while (il2C.hasNext()) {
			il2C.next();
			oprRA.setPosition(il2C);
			assertEquals(il2C.get().get(), oprRA.get().get(), 1e-10);
		}
	}

	@Test
	public void ShearIntervalTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 2, 2 }, new DoubleType());
		Cursor<DoubleType> imgC = img.cursor();
		while (imgC.hasNext()) {
			imgC.next().set(1);
		}

		Cursor<DoubleType> il2 = Views
				.shear(Views.extendZero(img), new FinalInterval(new long[] { 0, 0 }, new long[] { 3, 3 }), 0, 1)
				.cursor();
		RandomAccess<DoubleType> opr = ops.view()
				.shear(Views.extendZero(img), new FinalInterval(new long[] { 0, 0 }, new long[] { 3, 3 }), 0, 1)
				.randomAccess();

		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
	}
}
