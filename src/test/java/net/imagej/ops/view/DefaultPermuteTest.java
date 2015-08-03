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
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

/**
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * This test only checks if the op call works with all parameters and that the 
 * result is equal to the Views.method() call. 
 * This is not a correctness test of {@linkplain net.imglib2.view.Views}.
 */
public class DefaultPermuteTest extends AbstractOpTest {

	@Test
	public void defaultPermuteTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		
		MixedTransformView<DoubleType> il2 = Views.permute((RandomAccessible<DoubleType>)img, 1, 0);
		MixedTransformView<DoubleType> opr = ops.view().permute(img, 1, 0);
		
		for (int i = 0; i < il2.getTransformToSource().getMatrix().length; i++) {
			for (int j = 0; j < il2.getTransformToSource().getMatrix()[i].length; j++) {
				assertEquals(il2.getTransformToSource().getMatrix()[i][j], opr.getTransformToSource().getMatrix()[i][j],
						1e-10);
			}
		}
	}
	
	@Test
	public void defaultPermuteCoordinatesTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{2, 2}, new DoubleType());
		Cursor<DoubleType> c = img.cursor();
		Random r = new Random();
		while (c.hasNext()) {
			c.next().set(r.nextDouble());
		}
		Cursor<DoubleType> il2 = Views.permuteCoordinates(img, new int[]{0, 1}).cursor();
		RandomAccess<DoubleType> opr = ops.view().permuteCoordinates(img, new int[]{0, 1}).randomAccess();
		
		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
		
	}
	
	@Test
	public void permuteCoordinatesOfDimensionTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{2, 2}, new DoubleType());
		Cursor<DoubleType> c = img.cursor();
		Random r = new Random();
		while (c.hasNext()) {
			c.next().set(r.nextDouble());
		}
		Cursor<DoubleType> il2 = Views.permuteCoordinates(img, new int[]{0, 1}, 1).cursor();
		RandomAccess<DoubleType> opr = ops.view().permuteCoordinates(img, new int[]{0, 1}, 1).randomAccess();
		
		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
		
	}
	
	@Test
	public void defaultPermuteCoordinatesInverseTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{2, 2}, new DoubleType());
		Cursor<DoubleType> c = img.cursor();
		Random r = new Random();
		while (c.hasNext()) {
			c.next().set(r.nextDouble());
		}
		Cursor<DoubleType> il2 = Views.permuteCoordinatesInverse(img, new int[]{0, 1}).cursor();
		RandomAccess<DoubleType> opr = ops.view().permuteCoordinatesInverse(img, new int[]{0, 1}).randomAccess();
		
		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
	}
	
	@Test
	public void permuteCoordinatesInverseOfDimensionTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{2, 2}, new DoubleType());
		Cursor<DoubleType> c = img.cursor();
		Random r = new Random();
		while (c.hasNext()) {
			c.next().set(r.nextDouble());
		}
		
		IntervalView<DoubleType> out = Views.permuteCoordinateInverse(img, new int[]{0, 1}, 1);
		
		if(out == null)
			System.out.println("fuck");
		Cursor<DoubleType> il2 = out.cursor();
		RandomAccess<DoubleType> opr = ops.view().permuteCoordinatesInverse(img, new int[]{0, 1}, 1).randomAccess();
		
		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
	}
}
