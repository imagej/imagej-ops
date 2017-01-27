/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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
package net.imagej.ops.image.watershed;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.junit.Test;

import ij.io.Opener;

/**
 * Test for the watershed op.
 * 
 * @author Simon Schmid (University of Konstanz)
 **/
public class WatershedTest extends AbstractOpTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		// load test image
		Img<FloatType> watershedTestImg = ImageJFunctions.convertFloat(
				new Opener().openImage(WatershedBinaryTest.class.getResource("WatershedTestImage.png").getPath()));

		// threshold it
		RandomAccessibleInterval<BitType> thresholdedImg = ops.create().img(watershedTestImg, new BitType());
		ops.threshold().apply(Views.flatIterable(thresholdedImg), Views.flatIterable(watershedTestImg),
				new FloatType(1));

		// compute inverted distance transform and smooth it with gaussian
		// filtering
		final RandomAccessibleInterval<FloatType> distMap = ops.image().distancetransform(thresholdedImg);
		final RandomAccessibleInterval<FloatType> invertedDistMap = ops.create().img(distMap, new FloatType());
		ops.image().invert(Views.iterable(invertedDistMap), Views.iterable(distMap));
		final RandomAccessibleInterval<FloatType> gauss = ops.filter().gauss(invertedDistMap, 3);

		// compute result
		ImgLabeling<Integer, IntType> out = (ImgLabeling<Integer, IntType>) ops.run(Watershed.class, null, gauss, true,
				thresholdedImg);

		// count labels
		Set<Integer> labels = new HashSet<>();
		for (LabelingType<Integer> pixel : Views.iterable(out)) {
			labels.addAll(pixel);
		}

		// assert equals
		assertEquals(out.numDimensions(), watershedTestImg.numDimensions());
		assertEquals(out.dimension(0), watershedTestImg.dimension(0));
		assertEquals(out.dimension(1), watershedTestImg.dimension(1));
		assertEquals(labels.size(), 42);
	}

}
