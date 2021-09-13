/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2021 ImageJ2 developers.
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
import net.imagej.ops.filter.convolve.PadAndConvolveFFT;
import net.imglib2.Cursor;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Tests involving convolvers.
 */
public class DeconvolveTest extends AbstractOpTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testDeconvolve() {
		int[] size = new int[] { 225, 167 };

		// create an input with a small sphere at the center
		Img<FloatType> in = new ArrayImgFactory<FloatType>().create(size,
			new FloatType());
		placeSphereInCenter(in);

		// crop the image so the sphere is truncated at the corner
		// (this is useful for testing non-circulant mode)
		IntervalView<FloatType> incropped = Views.interval(in, new long[] {
			size[0] / 2, size[1] / 2 }, new long[] { size[0] - 1, size[1] - 1 });

		incropped = Views.zeroMin(incropped);

		RandomAccessibleInterval<FloatType> kernel = ops.create().kernelGauss(
			new double[] { 4.0, 4.0 }, new FloatType());

		RandomAccessibleInterval<FloatType> convolved = ops.create().img(incropped,
			new FloatType());
		RandomAccessibleInterval<FloatType> deconvolved = ops.create().img(
			incropped, new FloatType());
		RandomAccessibleInterval<FloatType> deconvolved2 = ops.create().img(
			incropped, new FloatType());

		// convolve
		convolved = (Img<FloatType>) ops.run(PadAndConvolveFFT.class, convolved,
			incropped, kernel);

		// deconvolve with standard Richardson Lucy
		deconvolved = (RandomAccessibleInterval<FloatType>) ops.run(
			PadAndRichardsonLucy.class, deconvolved, convolved, kernel, null,
			new OutOfBoundsConstantValueFactory<>(Util.getTypeFromInterval(in)
				.createVariable()), 10);

		// deconvolve with accelerated non-circulant Richardson Lucy
		deconvolved2 = (RandomAccessibleInterval<FloatType>) ops.run(
			PadAndRichardsonLucy.class, deconvolved2, convolved, kernel, null,
			new OutOfBoundsConstantValueFactory<>(Util.getTypeFromInterval(in)
				.createVariable()), null, null, null, 10, true, true);

		assertEquals(incropped.dimension(0), deconvolved.dimension(0));
		assertEquals(incropped.dimension(1), deconvolved.dimension(1));

		assertEquals(incropped.dimension(0), deconvolved2.dimension(0));
		assertEquals(incropped.dimension(1), deconvolved2.dimension(1));

		final Cursor<FloatType> deconvolvedCursor = Views.iterable(deconvolved)
			.cursor();

		final Cursor<FloatType> deconvolvedCursor2 = Views.iterable(deconvolved2)
			.cursor();

		float[] deconvolvedValues = { 3.6045982E-4f, 0.0016963598f, 0.0053468645f,
			0.011868152f, 0.019616995f, 0.025637051f, 0.028158935f, 0.027555753f,
			0.025289025f, 0.02266813f, 0.020409783f, 0.018752098f, 0.017683199f,
			0.016951872f, 0.016685976f };

		float[] deconvolvedValues2 = { 0.2630342f, 0.3163991f, 0.37503138f,
			0.43603566f, 0.49504462f, 0.54681045f, 0.5863713f, 0.6105026f, 0.6186579f,
			0.6129602f, 0.5972553f, 0.57583135f, 0.55244136f, 0.53075385f, 0.5109135f };

		for (int i = 0; i < deconvolvedValues.length; i++) {
			assertEquals(deconvolvedValues[i], deconvolvedCursor.next().get(), 0.0f);
			assertEquals(deconvolvedValues2[i], deconvolvedCursor2.next().get(),
				0.0f);
		}
	}

	// utility to place a small sphere at the center of the image
	private void placeSphereInCenter(Img<FloatType> img) {

		final Point center = new Point(img.numDimensions());

		for (int d = 0; d < img.numDimensions(); d++)
			center.setPosition(img.dimension(d) / 2, d);

		HyperSphere<FloatType> hyperSphere = new HyperSphere<>(img, center, 30);

		for (final FloatType value : hyperSphere) {
			value.setReal(1);
		}
	}
}
