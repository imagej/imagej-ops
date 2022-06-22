/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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
package net.imagej.ops.transform.addDimensionView;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.Ops.Transform.AddDimensionView} ops.
 * <p>
 * This test only checks if the op call works with all parameters and that the
 * result is equal to that of the {@link Views} method call. It is not a
 * correctness test of {@link Views} itself.
 * </p>
 *
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
public class AddDimensionViewTest extends AbstractOpTest {

	@Test
	public void addDimensionTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());
		
		MixedTransformView<DoubleType> il2 = Views.addDimension((RandomAccessible<DoubleType>)img);
		
		MixedTransformView<DoubleType> opr = ops.transform().addDimensionView((RandomAccessible<DoubleType>)img);
		
		assertEquals(il2.numDimensions(), opr.numDimensions());
		boolean[] il2Transform = new boolean[3];
		boolean[] oprTransform = new boolean[3];
		il2.getTransformToSource().getComponentZero(il2Transform);
		opr.getTransformToSource().getComponentZero(oprTransform);
		for (int i = 0; i < il2Transform.length; i++) {
			assertEquals(il2Transform[i], oprTransform[i]);
		}
	}

	@Test
	public void addDimensionMinMaxTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());
		int max = 20;
		int min = 0;
		
		IntervalView<DoubleType> il2 = Views.addDimension(img, min, max);
		
		IntervalView<DoubleType> opr = ops.transform().addDimensionView(img, min, max);
		
		assertEquals(il2.numDimensions(), opr.numDimensions(), 0.0);
		for (int i = 0; i < il2.numDimensions(); i++) {
			assertEquals(il2.dimension(i), opr.dimension(i), 0.0);
		}
	}
}
