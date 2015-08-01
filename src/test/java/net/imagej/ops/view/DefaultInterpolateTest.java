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
import net.imglib2.RealRandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.interpolation.randomaccess.FloorInterpolatorFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class DefaultInterpolateTest extends AbstractOpTest {

	@Test
	public void defaultInterpolateTest() {
		
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}
		
		RealRandomAccess<DoubleType> il2 = Views.interpolate(img, new FloorInterpolatorFactory<DoubleType>()).realRandomAccess();
		RealRandomAccess<DoubleType> opr = ops.view().interpolate(img, new FloorInterpolatorFactory<DoubleType>()).realRandomAccess();
		
		il2.setPosition(new double[]{1.75, 5.34});
		opr.setPosition(new double[]{1.75, 5.34});
		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		
		il2.setPosition(new double[]{3, 7});
		opr.setPosition(new double[]{3, 7});
		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		
		il2.setPosition(new double[]{8.37, 3.97});
		opr.setPosition(new double[]{8.37, 3.97});
		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
}
