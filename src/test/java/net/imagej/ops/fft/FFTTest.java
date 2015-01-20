package net.imagej.ops.fft;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.benchmark.AbstractOpBenchmark;
import net.imagej.ops.create.CreateImgDefault;
import net.imagej.ops.fft.size.ComputeFFTSize;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

/**
 * Test FFT implementations
 * 
 * @author Brian Northan
 */
public class FFTTest extends AbstractOpBenchmark {

	/**
	 * test that a forward transform followed by an inverse transform gives us
	 * back the original image
	 */
	@Test
	public void testFFT3DOp() {
		for (int i = 115; i < 120; i++) {

			Dimensions dimensions = new FinalDimensions(new long[] { i, i, i });

			// create an input with a small sphere at the center
			Img<FloatType> in = new ArrayImgFactory<FloatType>().create(
					dimensions, new FloatType());
			placeSphereInCenter(in);

			Img<FloatType> inverse = new ArrayImgFactory<FloatType>().create(
					dimensions, new FloatType());

			Img<ComplexFloatType> out = (Img<ComplexFloatType>) ops.run("fft",
					in);
			ops.run("ifft", inverse, out);

			assertImagesEqual(in, inverse, .00005f);
		}

	}

	/**
	 * test the fast FFT
	 */
	@Test
	public void testFastFFT3DOp() {

		for (int i = 115; i < 135; i++) {

			// define the original dimensions
			long[] originalDimensions = new long[] { i, 129, 129 };

			// arrays for the fast dimensions
			long[] fastDimensions = new long[3];
			long[] fftDimensions = new long[3];

			// compute the dimensions that will result in the fastest FFT time
			ops.run(ComputeFFTSize.class, originalDimensions, fastDimensions,
					fftDimensions, true, true);

			// create an input with a small sphere at the center
			Img<FloatType> inOriginal = (Img<FloatType>) ops.run(
					CreateImgDefault.class, new ArrayImgFactory<FloatType>(),
					new FloatType(), originalDimensions);
			placeSphereInCenter(inOriginal);

			// create a similar input using the fast size
			Img<FloatType> inFast = (Img<FloatType>) ops.run(
					CreateImgDefault.class, new ArrayImgFactory<FloatType>(),
					new FloatType(), fastDimensions);
			placeSphereInCenter(inFast);

			// call FFT passing false for "fast" (in order to pass the optional
			// parameter we have to pass null for the
			// output parameter).
			Img<ComplexFloatType> fft1 = (Img<ComplexFloatType>) ops.run("fft",
					null, inOriginal, null, false);

			// call FFT passing true for "fast" (in order to pass the optional
			// parameter we have to pass null for the
			// output parameter). The FFT op will pad the input to the fast
			// size.
			Img<ComplexFloatType> fft2 = (Img<ComplexFloatType>) ops.run("fft",
					null, inOriginal, null, true);

			// call fft using the img that was created with the fast size
			Img<ComplexFloatType> fft3 = (Img<ComplexFloatType>) ops.run("fft",
					inFast);

			// create an image to be used for the inverse, using the original
			// size
			Img<FloatType> inverseOriginalSmall = (Img<FloatType>) ops.run(
					CreateImgDefault.class, new ArrayImgFactory<FloatType>(),
					new FloatType(), originalDimensions);

			// create an inverse image to be used for the inverse, using the
			// original
			// size
			Img<FloatType> inverseOriginalFast = (Img<FloatType>) ops.run(
					CreateImgDefault.class, new ArrayImgFactory<FloatType>(),
					new FloatType(), originalDimensions);

			// create an inverse image to be used for the inverse, using the
			// fast size
			Img<FloatType> inverseFast = (Img<FloatType>) ops.run(
					CreateImgDefault.class, new ArrayImgFactory<FloatType>(),
					new FloatType(), fastDimensions);

			// invert the "small" FFT
			ops.run("ifft", inverseOriginalSmall, fft1);

			// invert the "fast" FFT. The inverse will should be the original
			// size.
			ops.run("ifft", inverseOriginalFast, fft2);

			// invert the "fast" FFT that was acheived by explicitly using an
			// image
			// that had "fast" dimensions. The inverse will be the fast size
			// this
			// time.
			ops.run("ifft", inverseFast, fft3);

			// assert that the inverse images are equal to the original
			assertImagesEqual(inverseOriginalSmall, inOriginal, .0001f);
			assertImagesEqual(inverseOriginalFast, inOriginal, .00001f);
			assertImagesEqual(inverseFast, inFast, 0.00001f);
		}
	}

	/**
	 * utility that places a sphere in the center of the image
	 * 
	 * @param img
	 */
	private void placeSphereInCenter(Img<FloatType> img) {

		final Point center = new Point(img.numDimensions());

		for (int d = 0; d < img.numDimensions(); d++)
			center.setPosition(img.dimension(d) / 2, d);

		HyperSphere<FloatType> hyperSphere = new HyperSphere<FloatType>(img,
				center, 2);

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
	protected void assertImagesEqual(Img<FloatType> img1, Img<FloatType> img2,
			float delta) {
		Cursor<FloatType> c1 = img1.cursor();
		Cursor<FloatType> c2 = img2.cursor();

		int i = 0;
		while (c1.hasNext()) {

			c1.fwd();
			c2.fwd();

			i++;

			// assert that the inverse = the input within the error delta
			assertEquals(c1.get().getRealFloat(), c2.get().getRealFloat(),
					delta);
		}

	}

	// a utility to assert that two rais are equal
	protected void assertRAIsEqual(RandomAccessibleInterval<FloatType> rai1,
			RandomAccessibleInterval<FloatType> rai2, float delta) {
		IterableInterval<FloatType> rai1Iterator = Views.iterable(rai1);
		IterableInterval<FloatType> rai2Iterator = Views.iterable(rai2);

		Cursor<FloatType> c1 = rai1Iterator.cursor();
		Cursor<FloatType> c2 = rai2Iterator.cursor();

		int i = 0;
		while (c1.hasNext()) {

			c1.fwd();
			c2.fwd();

			i++;

			// assert that the inverse = the input within the error delta
			assertEquals(c1.get().getRealFloat(), c2.get().getRealFloat(),
					delta);
		}

	}

	// a utility to assert that two images are equal
	protected void assertComplexImagesEqual(Img<ComplexFloatType> img1,
			Img<ComplexFloatType> img2, float delta) {
		Cursor<ComplexFloatType> c1 = img1.cursor();
		Cursor<ComplexFloatType> c2 = img2.cursor();

		int i = 0;
		while (c1.hasNext()) {

			c1.fwd();
			c2.fwd();

			i++;

			// assert that the inverse = the input within the error delta
			assertEquals(c1.get().getRealFloat(), c2.get().getRealFloat(),
					delta);
			// assert that the inverse = the input within the error delta
			assertEquals(c1.get().getImaginaryFloat(), c2.get()
					.getImaginaryFloat(), delta);
		}

	}

}
