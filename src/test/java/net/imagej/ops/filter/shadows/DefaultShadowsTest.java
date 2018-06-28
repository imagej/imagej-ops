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

package net.imagej.ops.filter.shadows;

import io.scif.SCIFIOService;
import io.scif.services.DatasetIOService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import net.imagej.Dataset;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
import net.imagej.ops.filter.shadow.DefaultShadows;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.app.StatusService;
import org.scijava.download.DiskLocationCache;
import org.scijava.download.Download;
import org.scijava.download.DownloadService;
import org.scijava.io.IOService;
import org.scijava.io.http.HTTPLocation;
import org.scijava.io.location.FileLocation;

/**
 * Tests for {@link DefaultShadows}
 *
 * @author Gabe Selzer
 */
public class DefaultShadowsTest extends AbstractOpTest {

	private Dataset input;

	@Override
	protected Context createContext() {
		return new Context(OpService.class, IOService.class, DownloadService.class,
			SCIFIOService.class, StatusService.class);
	}

	@Before
	public void init() throws IOException, InterruptedException,
		ExecutionException, URISyntaxException
	{
		// URL of test image
		final URL imageSrc = new URL("http://imagej.net/images/abe.tif");

		// FileLocation that we want to store this image in.
		final File destFile = File.createTempFile(getClass().getName(), ".tif");
		final FileLocation inputDest = new FileLocation(destFile);

		downloadOnce(imageSrc.toURI(), inputDest);

		final String srcPath = inputDest.getFile().getAbsolutePath();
		final DatasetIOService dio = context.service(DatasetIOService.class);
		input = dio.open(srcPath);
	}

	// move to DownloadService
	public void downloadOnce(final URI srcURI, final FileLocation inputDest)
		throws URISyntaxException, InterruptedException, ExecutionException
	{
		final DownloadService ds = context.service(DownloadService.class);
		final DiskLocationCache cache = new DiskLocationCache();
		cache.getBaseDirectory().mkdirs();

		final HTTPLocation inputSrc = new HTTPLocation(srcURI);

		// download the data from the URI to inputDest. Once we are done the method
		// calling downloadOnce can access the File from the FileLocation passed
		// through.
		final Download download = ds.download(inputSrc, inputDest, cache);
		download.task().waitFor();
	}

	@Test
	public void testRegression() throws IOException {
		final IOService io = context.getService(IOService.class);
		final double north = Math.PI / 2;
		final Img<FloatType> actualOutput = (Img<FloatType>) ops.run(
			"filter.shadows", input, north);
		final Img<FloatType> expectedOutput = (Img<FloatType>) io.open(
			"src/test/resources/net/imagej/ops/filter/shadows/ExpectedShadowsNorth.tif");
		assertIterationsEqual(actualOutput, expectedOutput);
	}

}
