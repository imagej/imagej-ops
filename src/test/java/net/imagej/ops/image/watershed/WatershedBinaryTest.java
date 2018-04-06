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
package net.imagej.ops.image.watershed;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Test for the binary watershed op.
 * 
 * @author Simon Schmid (University of Konstanz)
 **/
public class WatershedBinaryTest extends AbstractOpTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		// load test image
		Img<FloatType> watershedTestImg = openFloatImg(WatershedTest.class, "watershed_test_image.png");

		// threshold it
		RandomAccessibleInterval<BitType> thresholdedImg = ops.create().img(watershedTestImg, new BitType());
		ops.threshold().apply(Views.flatIterable(thresholdedImg), Views.flatIterable(watershedTestImg),
				new FloatType(1));

		// compute inverted distance transform and smooth it with gaussian
		// filtering
		final RandomAccessibleInterval<FloatType> distMap = ops.image().distancetransform(thresholdedImg);
		final RandomAccessibleInterval<FloatType> invertedDistMap = ops.create().img(distMap, new FloatType());
		ops.image().invert(Views.iterable(invertedDistMap), Views.iterable(distMap));

		double[] sigma = { 3.0, 3.0, 0.0 };
		final RandomAccessibleInterval<FloatType> gauss = ops.filter().gauss(invertedDistMap, sigma[0], sigma[1]);

		// compute result
		final ImgLabeling<Integer, IntType> out1 = (ImgLabeling<Integer, IntType>) ops.run(WatershedBinary.class,
			null, thresholdedImg, true, false, sigma, thresholdedImg);

		final ImgLabeling<Integer, IntType> expOut1 = (ImgLabeling<Integer, IntType>) ops.run(Watershed.class, null,
			gauss, true, false, thresholdedImg);

		assertResults(expOut1, out1);

		final ImgLabeling<Integer, IntType> out2 = (ImgLabeling<Integer, IntType>) ops.run(WatershedBinary.class,
			null, thresholdedImg, true, false, sigma);

		final ImgLabeling<Integer, IntType> expOut2 = (ImgLabeling<Integer, IntType>) ops.run(Watershed.class, null,
			gauss, true, false);

		assertResults(expOut2, out2);

		// compute result
		final ImgLabeling<Integer, IntType> out3 = (ImgLabeling<Integer, IntType>) ops.run(WatershedBinary.class,
			null, thresholdedImg, true, true, sigma, thresholdedImg);

		final ImgLabeling<Integer, IntType> expOut3 = (ImgLabeling<Integer, IntType>) ops.run(Watershed.class, null,
			gauss, true, true, thresholdedImg);

		assertResults(expOut3, out3);

		final ImgLabeling<Integer, IntType> out4 = (ImgLabeling<Integer, IntType>) ops.run(WatershedBinary.class,
			null, thresholdedImg, true, true, sigma);

		final ImgLabeling<Integer, IntType> expOut4 = (ImgLabeling<Integer, IntType>) ops.run(Watershed.class, null,
			gauss, true, true);

		assertResults(expOut4, out4);

		// compute result
		final ImgLabeling<Integer, IntType> out5 = (ImgLabeling<Integer, IntType>) ops.run(WatershedBinary.class,
			null, thresholdedImg, false, true, sigma, thresholdedImg);

		final ImgLabeling<Integer, IntType> expOut5 = (ImgLabeling<Integer, IntType>) ops.run(Watershed.class, null,
			gauss, false, true, thresholdedImg);

		assertResults(expOut5, out5);

		final ImgLabeling<Integer, IntType> out6 = (ImgLabeling<Integer, IntType>) ops.run(WatershedBinary.class,
			null, thresholdedImg, false, true, sigma);

		final ImgLabeling<Integer, IntType> expOut6 = (ImgLabeling<Integer, IntType>) ops.run(Watershed.class, null,
			gauss, false, true);

		assertResults(expOut6, out6);
	}

	private void assertResults(final ImgLabeling<Integer, IntType> expOut, final ImgLabeling<Integer, IntType> out) {
		Cursor<LabelingType<Integer>> expOutCursor = expOut.cursor();
		RandomAccess<LabelingType<Integer>> raOut = out.randomAccess();

		while (expOutCursor.hasNext()) {
			expOutCursor.fwd();
			raOut.setPosition(expOutCursor);
			assertEquals(expOutCursor.get().size(), raOut.get().size());
			if (expOutCursor.get().size() > 0)
				assertEquals(expOutCursor.get().iterator().next(), raOut.get().iterator().next());
		}

	}
}
