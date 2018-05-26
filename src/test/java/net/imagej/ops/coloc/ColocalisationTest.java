/*-
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
package net.imagej.ops.coloc;

import io.scif.SCIFIOService;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import java.util.Arrays;
import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.Point;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss.Gauss;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.junit.Before;
import org.scijava.Context;
import org.scijava.app.StatusService;
import org.scijava.cache.CacheService;

/** Abstract base class for coloc op unit tests. */
public abstract class ColocalisationTest extends AbstractOpTest {

	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
			CacheService.class, StatusService.class, SCIFIOService.class);
	}

	// images and meta data for zero correlation
	protected Img<UnsignedByteType> zeroCorrelationImageCh1;
	protected Img<UnsignedByteType> zeroCorrelationImageCh2;

	// images and meta data for positive correlation test
	// and real noisy image Manders' coeff with mask test
	protected Img<UnsignedByteType> positiveCorrelationImageCh1;
	protected Img<UnsignedByteType> positiveCorrelationImageCh2;

	/**
	 * This method is run before every single test is run and is meant to set up
	 * the images and meta data needed for testing image colocalisation.
	 */
	@Before
	public void setup() {
		zeroCorrelationImageCh1 = loadTiffFromJar("greenZstack.tif");
		zeroCorrelationImageCh2 = loadTiffFromJar("redZstack.tif");

		positiveCorrelationImageCh1 = loadTiffFromJar("colocsample1b-green.tif");
		positiveCorrelationImageCh2 = loadTiffFromJar("colocsample1b-red.tif");
	}

	/**
	 * Loads a Tiff file from within the jar to use as a mask Cursor.
	 * So we use Img<T> which has a cursor() method. 
	 * The given path is treated
	 * as relative to this tests-package (i.e. "Data/test.tiff" refers
	 * to the test.tiff in sub-folder Data).
	 *
	 * @param <T> The wanted output type.
	 * @param relPath The relative path to the Tiff file.
	 * @return The file as ImgLib image.
	 */
	private <T extends RealType<T> & NativeType<T>> Img<T> loadTiffFromJar(
		final String relPath)
	{
//		InputStream is = TestImageAccessor.class.getResourceAsStream(relPath);
//		BufferedInputStream bis = new BufferedInputStream(is);

		final ImgOpener opener = new ImgOpener(context);

		// HACK: Read data from file system for now.
		// Until this is fixed, the test will not pass when run from a JAR file.
		String source = "src/test/resources/net/imagej/ops/coloc/" + relPath;
		try {
			return (Img) opener.openImgs(source).get(0);
		}
		catch (final ImgIOException exc) {
			throw new IllegalStateException("File " + relPath +
				" is unexpectedly inaccessible?");
		}
	}

	/**
	 * This method creates a noise image that has a specified mean. Every pixel
	 * has a value uniformly distributed around mean with the maximum spread
	 * specified.
	 *
	 * @return IllegalArgumentException if specified means and spreads are not
	 *         valid
	 */
	public static <T extends RealType<T> & NativeType<T>>
		Img<T> produceMeanBasedNoiseImage(T type, int width,
			int height, double mean, double spread, double[] smoothingSigma,
			long seed) throws IllegalArgumentException
	{
		if (mean < spread || (mean + spread) > type.getMaxValue()) {
			throw new IllegalArgumentException(
				"Mean must be larger than spread, and mean plus spread must be smaller than max of the type");
		}
		// create the new image
		ImgFactory<T> imgFactory = new ArrayImgFactory<>(type);
		Img<T> noiseImage = imgFactory.create(width, height);

		Random r = new Random(seed);
		for (T value : Views.iterable(noiseImage)) {
			value.setReal(mean + ((r.nextDouble() - 0.5) * spread));
		}

		// TODO: call Ops filter.gauss instead
		return gaussianSmooth(noiseImage, smoothingSigma);
	}

	/**
	 * Gaussian Smooth of the input image using intermediate float format.
	 * 
	 * @param <T>
	 * @param img
	 * @param sigma
	 * @return
	 */
	public static <T extends RealType<T> & NativeType<T>>
		Img<T> gaussianSmooth(RandomAccessibleInterval<T> img,
			double[] sigma)
	{
		Interval interval = Views.iterable(img);

		ImgFactory<T> outputFactory = new ArrayImgFactory<>(Util.getTypeFromInterval(img));
		final long[] dim = new long[img.numDimensions()];
		img.dimensions(dim);
		Img<T> output = outputFactory.create(dim);

		final long[] pos = new long[img.numDimensions()];
		Arrays.fill(pos, 0);
		Localizable origin = new Point(pos);

		ImgFactory<FloatType> tempFactory = new ArrayImgFactory<>(new FloatType());
		RandomAccessible<T> input = Views.extendMirrorSingle(img);
		Gauss.inFloat(sigma, input, interval, output, origin, tempFactory);

		return output;
	}
}
