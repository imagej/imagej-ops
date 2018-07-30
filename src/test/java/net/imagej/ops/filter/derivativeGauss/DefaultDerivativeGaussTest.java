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

package net.imagej.ops.filter.derivativeGauss;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

/**
 * Contains tests for {@link DefaultDerivativeGauss}.
 *
 * @author Gabe Selzer
 */
public class DefaultDerivativeGaussTest extends AbstractOpTest {

	@Test(expected = IllegalArgumentException.class)
	public void testImgParamDimensionsMismatch() {
		final Img<DoubleType> input = ops.convert().float64(
			generateFloatArrayTestImg(false, 30, 30, 30));

		final Img<DoubleType> output = ops.create().img(input);

		final int[] derivatives = new int[] { 1, 0 };
		final double[] sigmas = new double[] { 1, 1 };
		ops.filter().derivativeGauss(output, input, derivatives, sigmas);
	}

	@Test
	public void regressionTest() {
		final int width = 10;
		final Img<DoubleType> input = ops.convert().float64(
			generateFloatArrayTestImg(false, width, width));

		final Img<DoubleType> output = ops.create().img(input);

		// Draw a line on the image
		final RandomAccess<DoubleType> inputRA = input.randomAccess();
		inputRA.setPosition(5, 0);
		for (int i = 0; i < 10; i++) {
			inputRA.setPosition(i, 1);
			inputRA.get().set(255);
		}

		// filter the image
		final int[] derivatives = new int[] { 1, 0 };
		final double[] sigmas = new double[] { 0.5, 0.5 };
		ops.filter().derivativeGauss(output, input, derivatives, sigmas);

		final Cursor<DoubleType> cursor = output.localizingCursor();
		int currentPixel = 0;
		while (cursor.hasNext()) {
			cursor.fwd();
			assertEquals(cursor.get().getRealDouble(),
				regressionRowValues[currentPixel % width], 0);
			currentPixel++;
		}
	}

	double[] regressionRowValues = { 0.0, 0.0, 0.0, 2.1876502452391353,
		117.25400606437196, 0.0, -117.25400606437196, -2.1876502452391353, 0.0,
		0.0 };

}
