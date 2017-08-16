/*
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

package net.imagej.ops.filter.vesselness;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.scijava.Context;
import org.scijava.cache.CacheService;
import org.scijava.script.ScriptService;

import net.imagej.ImgPlus;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

/**
 * Tests the Frangi Vesselness operation.
 * 
 * @author Gabe Selzer
 */
public class FrangiVesselnessTest extends AbstractOpTest {

	@Test
	public void regressionTest() throws Exception {

		// load in input image and expected output image.
		Img<FloatType> inputImg = (Img<FloatType>) ops.run(net.imagej.ops.image.equation.DefaultEquation.class,
				"Math.tan(0.3*p[0]) + Math.tan(0.1*p[1])");
		Img<FloatType> expectedOutput = ((Img<FloatType>) openFloatImg("Result.tif"));

		// create ouput image
		long[] dims = new long[inputImg.numDimensions()];
		inputImg.dimensions(dims);
		Img<FloatType> actualOutput = ArrayImgs.floats(dims);

		// scale over which the filter operates (sensitivity)
		double[] scale = { 1 };

		// physical spacing between data points (1,1 since I got it from the computer)
		double[] spacing = { 1, 1 };

		// run the op
		ops.run(net.imagej.ops.filter.vesselness.DefaultFrangi.class, actualOutput, inputImg, spacing, scale);

		// compare the output image data to that stored in the file.
		Cursor<FloatType> cursor = Views.iterable(actualOutput).localizingCursor();
		RandomAccess<FloatType> actualRA = actualOutput.randomAccess();
		RandomAccess<FloatType> expectedRA = expectedOutput.randomAccess();

		while (cursor.hasNext()) {
			cursor.fwd();
			actualRA.setPosition(cursor);
			expectedRA.setPosition(cursor);
			assertEquals(expectedRA.get().get(), actualRA.get().get(), 0);
		}
	}

	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class, CacheService.class, ScriptService.class);
	}

}