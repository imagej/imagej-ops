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
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

public class DefaultOffsetTest extends AbstractOpTest {

	@Test
	public void defaultOffsetTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		MixedTransformView<DoubleType> il2 = Views.offset((RandomAccessible<DoubleType>) img, new long[] { 2, 2 });
		MixedTransformView<DoubleType> opr = ops.view().offset((RandomAccessible<DoubleType>) img, new long[] { 2, 2 });

		for (int i = 0; i < il2.getTransformToSource().getMatrix().length; i++) {
			for (int j = 0; j < il2.getTransformToSource().getMatrix()[i].length; j++) {
				assertEquals(il2.getTransformToSource().getMatrix()[i][j], opr.getTransformToSource().getMatrix()[i][j],
						1e-10);
			}
		}
	}

	@Test
	public void defaultOffsetIntervalTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		IntervalView<DoubleType> il2 = Views.offsetInterval(img,
				new FinalInterval(new long[] { 2, 2 }, new long[] { 9, 9 }));
		IntervalView<DoubleType> opr = ops.view().offset(img,
				new FinalInterval(new long[] { 2, 2 }, new long[] { 9, 9 }));

		assertEquals(il2.realMax(0), opr.realMax(0), 1e-10);
		assertEquals(il2.realMin(0), opr.realMin(0), 1e-10);
		assertEquals(il2.realMax(1), opr.realMax(1), 1e-10);
		assertEquals(il2.realMin(1), opr.realMin(1), 1e-10);
	}

	@Test
	public void defaultOffsetStartEndTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		IntervalView<DoubleType> il2 = Views.offsetInterval(img, new long[] { 2, 2 }, new long[] { 9, 9 });
		IntervalView<DoubleType> opr = ops.view().offset(img, new long[] { 2, 2 }, new long[] { 9, 9 });

		assertEquals(il2.realMax(0), opr.realMax(0), 1e-10);
		assertEquals(il2.realMin(0), opr.realMin(0), 1e-10);
		assertEquals(il2.realMax(1), opr.realMax(1), 1e-10);
		assertEquals(il2.realMin(1), opr.realMin(1), 1e-10);
	}
}
