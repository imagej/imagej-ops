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

package net.imagej.ops.deconvolve;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.filter.convolve.ConvolveFFTF;
import net.imglib2.Cursor;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Tests involving convolvers.
 */
public class DeconvolveTest extends AbstractOpTest {

	@Test
	public void testDeconvolve() {
		int[] size = new int[] { 225, 167 };
		int[] kernelSize = new int[] { 27, 39 };

		// create an input with a small sphere at the center
		Img<FloatType> in = new ArrayImgFactory<FloatType>().create(size,
			new FloatType());
		placeSphereInCenter(in);

		// create a kernel with a small sphere in the center
		Img<FloatType> kernel = new ArrayImgFactory<FloatType>().create(kernelSize,
			new FloatType());
		placeSphereInCenter(kernel);

		// convolve and calculate the sum of output
		@SuppressWarnings("unchecked")
		final Img<FloatType> convolved = (Img<FloatType>) ops.run(
			ConvolveFFTF.class, in, kernel);

		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<FloatType> deconvolved2 =
			(RandomAccessibleInterval<FloatType>) ops.run(RichardsonLucyF.class,
				convolved, kernel, null, new OutOfBoundsConstantValueFactory<>(Util
					.getTypeFromInterval(in).createVariable()), 10);

		assertEquals(size[0], deconvolved2.dimension(0));
		assertEquals(size[1], deconvolved2.dimension(1));
		final Cursor<FloatType> deconvolved2Cursor = Views.iterable(deconvolved2)
			.cursor();
		float[] deconvolved2Values = { 1.0936068E-14f, 2.9685445E-14f,
			4.280788E-15f, 3.032084E-18f, 1.1261E-39f, 0.0f, -8.7E-44f, -8.11881E-31f,
			-2.821192E-18f, 1.8687104E-20f, -2.927517E-23f, 1.2815774E-29f,
			-1.0611375E-19f, -5.2774515E-21f, -6.154334E-20f };
		for (int i = 0; i < deconvolved2Values.length; i++) {
			assertEquals(deconvolved2Values[i], deconvolved2Cursor.next().get(),
				0.0f);
		}
	}

	// utility to place a small sphere at the center of the image
	private void placeSphereInCenter(Img<FloatType> img) {

		final Point center = new Point(img.numDimensions());

		for (int d = 0; d < img.numDimensions(); d++)
			center.setPosition(img.dimension(d) / 2, d);

		HyperSphere<FloatType> hyperSphere = new HyperSphere<>(img, center, 2);

		for (final FloatType value : hyperSphere) {
			value.setReal(1);
		}
	}
}
