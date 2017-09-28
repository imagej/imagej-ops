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

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.labeling.ConnectedComponents.StructuringElement;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.LongArray;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Regions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

/**
 * Test for the seeded watershed op.
 * 
 * @author Simon Schmid (University of Konstanz)
 **/
public class WatershedSeededTest extends AbstractOpTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {

		// load test image and seeds
		Img<FloatType> watershedTestImg = openFloatImg(WatershedTest.class, "WatershedTestImage.png");
		Img<FloatType> seeds = openFloatImg(WatershedBinaryTest.class, "Seeds.png");

		// threshold them
		RandomAccessibleInterval<BitType> thresholdedImg = ops.create().img(watershedTestImg, new BitType());
		ops.threshold().apply(Views.flatIterable(thresholdedImg), Views.flatIterable(watershedTestImg),
				new FloatType(1));
		RandomAccessibleInterval<BitType> thresholdedSeeds = ops.create().img(seeds, new BitType());
		ops.threshold().apply(Views.flatIterable(thresholdedSeeds), Views.flatIterable(seeds), new FloatType(1));

		// compute labeled seeds
		final ImgLabeling<Integer, IntType> labeledSeeds = ops.labeling().cca(thresholdedSeeds,
				StructuringElement.EIGHT_CONNECTED);

		// FIXME Using the input image as mask?!
		// compute result
		ImgLabeling<Integer, IntType> out = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null,
				thresholdedImg, labeledSeeds, true, false, thresholdedImg);

		// Sample the output image based on the mask
		// FIXME The dummy label is set somewhere outside of the mask
		IterableRegion<BitType> regions = Regions.iterable(thresholdedImg);

		// count labels
		Set<Integer> labelSet = new HashSet<>();
		for (LabelingType<Integer> pixel : Regions.sample(regions, out)) {
			labelSet.addAll(pixel);
		}

		// count seeds
		Set<Integer> seedSet = new HashSet<>();
		for (LabelingType<Integer> pixel : Regions.sample(regions, labeledSeeds)) {
			seedSet.addAll(pixel);
		}

		// assert equals
		assertEquals(watershedTestImg.numDimensions(), out.numDimensions());
		assertEquals(watershedTestImg.dimension(0), out.dimension(0));
		assertEquals(watershedTestImg.dimension(1), out.dimension(1));
		assertEquals(seedSet.size(), labelSet.size());

	}

	@Test
	public void testSmall() {
		// create input image
		Img<BitType> input = ArrayImgs.bits(5, 3);
		for (BitType b : input) {
			b.not();
		}

		// create seeds
		Img<BitType> bits = ArrayImgs.bits(5, 3);
		RandomAccess<BitType> ra = bits.randomAccess();
		ra.setPosition(new int[] {1, 1});
		ra.get().set(true);
		ra.setPosition(new int[] {3, 1});
		ra.get().set(true);

		// compute labeled seeds
		final ImgLabeling<Integer, IntType> labeledSeeds = ops.labeling().cca(bits, StructuringElement.EIGHT_CONNECTED);

		// FIXME Using the input image as mask?!
		// compute result
		ImgLabeling<Integer, IntType> out = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null,
				input, labeledSeeds, true, false, input);

		// Sample the output image based on the mask
		// FIXME The dummy label is set somewhere outside of the mask
		IterableRegion<BitType> regions = Regions.iterable(input);

		// count labels
		Set<Integer> labelSet = new HashSet<>();
		for (LabelingType<Integer> pixel : Regions.sample(regions, out)) {
			labelSet.addAll(pixel);
		}

		// count seeds
		Set<Integer> seedSet = new HashSet<>();
		for (LabelingType<Integer> pixel : Regions.sample(regions, labeledSeeds)) {
			seedSet.addAll(pixel);
		}

		// assert equals
		assertEquals(input.numDimensions(), out.numDimensions());
		assertEquals(input.dimension(0), out.dimension(0));
		assertEquals(input.dimension(1), out.dimension(1));
		assertEquals(seedSet.size(), labelSet.size());
	}

}
