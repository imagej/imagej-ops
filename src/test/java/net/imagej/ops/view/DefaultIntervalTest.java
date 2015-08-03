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

import java.util.Random;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

/**
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * This test only checks if the op call works with all parameters and that the 
 * result is equal to the Views.method() call. 
 * This is not a correctness test of {@linkplain net.imglib2.view.Views}.
 */
public class DefaultIntervalTest extends AbstractOpTest {

	@Test
	public void defaultIntervalTest() {
		
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}
		
		Cursor<DoubleType> il2 = Views.interval(img, img).localizingCursor();
		RandomAccess<DoubleType> opr = ops.view().interval(img, img).randomAccess();

		
		while (il2.hasNext()) {
			DoubleType e = il2.next();
			opr.setPosition(il2);
			
			assertEquals(e.get(), opr.get().get(), 1e-10);
		}
	}
	
	@Test
	public void intervalMinMaxTest() {
		
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}
		
		Cursor<DoubleType> il2 = Views.interval(img, new long[]{1, 1}, new long[]{8,9}).localizingCursor();
		RandomAccess<DoubleType> opr = ops.view().interval(img, new long[]{1, 1}, new long[]{8,9}).randomAccess();
		
		while (il2.hasNext()) {
			DoubleType e = il2.next();
			opr.setPosition(il2);
			
			assertEquals(e.get(), opr.get().get(), 1e-10);
		}
	}
}
