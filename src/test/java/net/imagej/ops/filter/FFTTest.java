/* #%L
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

package net.imagej.ops.filter;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.filter.fft.FFTMethodsOpF;
import net.imagej.ops.filter.fftSize.ComputeFFTSize;
import net.imagej.ops.filter.ifft.IFFTMethodsOpC;
import net.imagej.ops.filter.pad.PadShiftKernel;
import net.imagej.ops.filter.pad.PadShiftKernelFFTMethods;
import net.imglib2.Cursor;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Test FFT implementations
 * 
 * @author Brian Northan
 */
public class FFTTest extends AbstractOpTest {

	private final boolean expensiveTestsEnabled = "enabled".equals(System
		.getProperty("imagej.ops.expensive.tests"));

	/**
	 * test that a forward transform followed by an inverse transform gives us
	 * back the original image
	 */
	@Test
	public void testFFT3DOp() {
		final int min = expensiveTestsEnabled ? 115 : 9;
		final int max = expensiveTestsEnabled ? 120 : 11;
		for (int i = min; i < max; i++) {

			final long[] dimensions = new long[] { i, i, i };

			// create an input with a small sphere at the center
			final Img<FloatType> in = generateFloatArrayTestImg(false, dimensions);
			placeSphereInCenter(in);

			final Img<FloatType> inverse = generateFloatArrayTestImg(false,
				dimensions);

			@SuppressWarnings("unchecked")
			final Img<ComplexFloatType> out = (Img<ComplexFloatType>) ops.run(
				FFTMethodsOpF.class, in);
			ops.run(IFFTMethodsOpC.class, inverse, out);

			assertImagesEqual(in, inverse, .00005f);
		}

	}

	/**
	 * test the fast FFT
	 */
	@Test
	public void testFastFFT3DOp() {

		final int min = expensiveTestsEnabled ? 120 : 9;
		final int max = expensiveTestsEnabled ? 130 : 11;
		final int size = expensiveTestsEnabled ? 129 : 10;
		for (int i = min; i < max; i++) {

			// define the original dimensions
			final long[] originalDimensions = new long[] { i, size, size };

			// arrays for the fast dimensions
			final long[] fastDimensions = new long[3];
			final long[] fftDimensions = new long[3];

			// compute the dimensions that will result in the fastest FFT time
			ops.run(ComputeFFTSize.class, originalDimensions, fastDimensions,
				fftDimensions, true, true);

			// create an input with a small sphere at the center
			final Img<FloatType> inOriginal = generateFloatArrayTestImg(false,
				originalDimensions);
			placeSphereInCenter(inOriginal);

			// create a similar input using the fast size
			final Img<FloatType> inFast = generateFloatArrayTestImg(false,
				fastDimensions);
			placeSphereInCenter(inFast);

			// call FFT passing false for "fast" (in order to pass the optional
			// parameter we have to pass null for the
			// output parameter).
			@SuppressWarnings("unchecked")
			final RandomAccessibleInterval<ComplexFloatType> fft1 =
				(RandomAccessibleInterval<ComplexFloatType>) ops.run(
					FFTMethodsOpF.class, inOriginal, null, false);

			// call FFT passing true for "fast" The FFT op will pad the input to the
			// fast
			// size.
			@SuppressWarnings("unchecked")
			final RandomAccessibleInterval<ComplexFloatType> fft2 =
				(RandomAccessibleInterval<ComplexFloatType>) ops.run(
					FFTMethodsOpF.class, inOriginal, null, true);

			// call fft using the img that was created with the fast size
			@SuppressWarnings("unchecked")
			final RandomAccessibleInterval<ComplexFloatType> fft3 =
				(RandomAccessibleInterval<ComplexFloatType>) ops.run(
					FFTMethodsOpF.class, inFast);

			// create an image to be used for the inverse, using the original
			// size
			final Img<FloatType> inverseOriginalSmall = generateFloatArrayTestImg(
				false, originalDimensions);

			// create an inverse image to be used for the inverse, using the
			// original
			// size
			final Img<FloatType> inverseOriginalFast = generateFloatArrayTestImg(
				false, originalDimensions);

			// create an inverse image to be used for the inverse, using the
			// fast size
			final Img<FloatType> inverseFast = generateFloatArrayTestImg(false,
				fastDimensions);

			// invert the "small" FFT
			ops.run(IFFTMethodsOpC.class, inverseOriginalSmall, fft1);

			// invert the "fast" FFT. The inverse will should be the original
			// size.
			ops.run(IFFTMethodsOpC.class, inverseOriginalFast, fft2);

			// invert the "fast" FFT that was acheived by explicitly using an
			// image
			// that had "fast" dimensions. The inverse will be the fast size
			// this
			// time.
			ops.run(IFFTMethodsOpC.class, inverseFast, fft3);

			// assert that the inverse images are equal to the original
			assertImagesEqual(inverseOriginalSmall, inOriginal, .0001f);
			assertImagesEqual(inverseOriginalFast, inOriginal, .00001f);
			assertImagesEqual(inverseFast, inFast, 0.00001f);
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPadShiftKernel() {
		long[] dims = new long[] { 1024, 1024 };
		Img<ComplexDoubleType> test = ops.create().img(new FinalDimensions(dims),
			new ComplexDoubleType());

		RandomAccessibleInterval<ComplexDoubleType> shift =
			(RandomAccessibleInterval<ComplexDoubleType>) ops.run(
				PadShiftKernel.class, test, new FinalDimensions(dims));

		RandomAccessibleInterval<ComplexDoubleType> shift2 =
			(RandomAccessibleInterval<ComplexDoubleType>) ops.run(
				PadShiftKernelFFTMethods.class, test, new FinalDimensions(dims));

		// assert there was no additional padding done by PadShiftKernel
		assertEquals(1024, shift.dimension(0));
		// assert that PadShiftKernelFFTMethods padded to the FFTMethods fast size
		assertEquals(1120, shift2.dimension(0));

	}

	/**
	 * utility that places a sphere in the center of the image
	 * 
	 * @param img
	 */
	private void placeSphereInCenter(final Img<FloatType> img) {

		final Point center = new Point(img.numDimensions());

		for (int d = 0; d < img.numDimensions(); d++)
			center.setPosition(img.dimension(d) / 2, d);

		final HyperSphere<FloatType> hyperSphere = new HyperSphere<>(img, center,
			2);

		for (final FloatType value : hyperSphere) {
			value.setReal(1);
		}
	}

	/**
	 * a utility to assert that two images are equal
	 * 
	 * @param img1
	 * @param img2
	 * @param delta
	 */
	protected void assertImagesEqual(final Img<FloatType> img1,
		final Img<FloatType> img2, final float delta)
	{
		final Cursor<FloatType> c1 = img1.cursor();
		final Cursor<FloatType> c2 = img2.cursor();

		while (c1.hasNext()) {
			c1.fwd();
			c2.fwd();

			// assert that the inverse = the input within the error delta
			assertEquals(c1.get().getRealFloat(), c2.get().getRealFloat(), delta);
		}

	}

	// a utility to assert that two rais are equal
	protected void assertRAIsEqual(final RandomAccessibleInterval<FloatType> rai1,
		final RandomAccessibleInterval<FloatType> rai2, final float delta)
	{
		final IterableInterval<FloatType> rai1Iterator = Views.iterable(rai1);
		final IterableInterval<FloatType> rai2Iterator = Views.iterable(rai2);

		final Cursor<FloatType> c1 = rai1Iterator.cursor();
		final Cursor<FloatType> c2 = rai2Iterator.cursor();

		while (c1.hasNext()) {
			c1.fwd();
			c2.fwd();

			// assert that the inverse = the input within the error delta
			assertEquals(c1.get().getRealFloat(), c2.get().getRealFloat(), delta);
		}
	}

	// a utility to assert that two images are equal
	protected void assertComplexImagesEqual(final Img<ComplexFloatType> img1,
		final Img<ComplexFloatType> img2, final float delta)
	{
		final Cursor<ComplexFloatType> c1 = img1.cursor();
		final Cursor<ComplexFloatType> c2 = img2.cursor();

		while (c1.hasNext()) {
			c1.fwd();
			c2.fwd();

			// assert that the inverse = the input within the error delta
			assertEquals(c1.get().getRealFloat(), c2.get().getRealFloat(), delta);
			// assert that the inverse = the input within the error delta
			assertEquals(c1.get().getImaginaryFloat(), c2.get().getImaginaryFloat(),
				delta);
		}

	}

}
