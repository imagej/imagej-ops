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
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.outofbounds.OutOfBounds;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

/**
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * This test only checks if the op call works with all parameters and that the 
 * result is equal to the Views.method() call. 
 * This is not a correctness test of {@linkplain net.imglib2.view.Views}.
 */
public class ExtendTests extends AbstractOpTest {

	@Test
	public void defaultExtendTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views
				.extend(img, new OutOfBoundsBorderFactory<DoubleType, RandomAccessibleInterval<DoubleType>>())
				.randomAccess();

		OutOfBounds<DoubleType> opr = ops.view()
				.extend(img, new OutOfBoundsBorderFactory<DoubleType, RandomAccessibleInterval<DoubleType>>())
				.randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendBorderTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendBorder(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendBorder(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendMirrorDoubleTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendMirrorDouble(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendMirrorDouble(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendMirrorSingleTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendMirrorSingle(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendMirrorSingle(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendPeriodicTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendPeriodic(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendPeriodic(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendRandomTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendRandom(img, 0, 0).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendRandom(img, 0, 0).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendValueTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendValue(img, new DoubleType(0)).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendValue(img, new DoubleType(0)).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendZeroTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendZero(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendZero(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
}
