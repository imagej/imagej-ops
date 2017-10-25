/*-
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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
package net.imagej.ops.coloc;

import io.scif.SCIFIOService;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

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
}
