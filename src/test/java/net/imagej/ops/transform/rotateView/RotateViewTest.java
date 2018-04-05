/*
 * #%L
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
package net.imagej.ops.transform.rotateView;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.Ops.Transform.RotateView} ops.
 * <p>
 * This test only checks if the op call works with all parameters and that the
 * result is equal to that of the {@link Views} method call. It is not a
 * correctness test of {@link Views} itself.
 * </p>
 *
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @author Gabe Selzer
 */
public class RotateViewTest extends AbstractOpTest {

	@Test
	public void testDefaultRotate() {
		final Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 20, 10 }, new DoubleType());

		final MixedTransformView<DoubleType> il2 = Views.rotate((RandomAccessible<DoubleType>) img, 1, 0);
		final MixedTransformView<DoubleType> opr = ops.transform().rotateView(deinterval(img), 1, 0);

		for (int i = 0; i < il2.getTransformToSource().getMatrix().length; i++) {
			for (int j = 0; j < il2.getTransformToSource().getMatrix()[i].length; j++) {
				assertEquals(il2.getTransformToSource().getMatrix()[i][j], opr.getTransformToSource().getMatrix()[i][j],
						1e-10);
			}
		}
	}

	@Test
	public void testIntervalRotate() {
		final Img<DoubleType> img = ArrayImgs.doubles(20,10);

		final IntervalView<DoubleType> il2 = Views.rotate((RandomAccessibleInterval<DoubleType>) img, 1, 0);
		final IntervalView<DoubleType> opr = (IntervalView<DoubleType>) ops.transform().rotateView((RandomAccessibleInterval<DoubleType>) img, 1, 0);

		for (int i = 0; i < ((MixedTransformView<DoubleType>) il2.getSource()).getTransformToSource()
				.getMatrix().length; i++) {
			for (int j = 0; j < ((MixedTransformView<DoubleType>) il2.getSource()).getTransformToSource()
					.getMatrix()[i].length; j++) {
				assertEquals(
						((MixedTransformView<DoubleType>) il2.getSource()).getTransformToSource().getMatrix()[i][j],
						((MixedTransformView<DoubleType>) opr.getSource()).getTransformToSource().getMatrix()[i][j],
						1e-10);
			}
		}
	}

	@Test
	public void testIntervalRotateInterval() {
		final Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 20, 10 }, new DoubleType());

		final IntervalView<DoubleType> il2 = Views.rotate((RandomAccessibleInterval<DoubleType>) img, 1, 0);
		final IntervalView<DoubleType> opr = ops.transform().rotateView((RandomAccessibleInterval<DoubleType>) img, 1, 0);

		assertEquals(img.min(1), il2.min(0));
		assertEquals(img.max(1), il2.max(0));
		assertEquals(img.min(0), -il2.max(1));
		assertEquals(img.max(0), -il2.min(1));

		for (int i = 0; i < il2.numDimensions(); i++) {
			assertEquals(il2.max(i), opr.max(i));
			assertEquals(il2.min(i), opr.min(i));
		}

	}
}
