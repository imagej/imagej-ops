/*-
 * #%L
 * Fiji's plugin for colocalization analysis.
 * %%
 * Copyright (C) 2009 - 2017 Fiji developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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
