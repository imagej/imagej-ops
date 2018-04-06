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

import java.util.HashSet;
import java.util.Set;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.labeling.ConnectedComponents.StructuringElement;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Regions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;
import org.scijava.util.MersenneTwisterFast;

/**
 * Test for the seeded watershed op.
 * 
 * @author Simon Schmid (University of Konstanz)
 **/
public class WatershedSeededTest extends AbstractOpTest {

	private static final long SEED = 0x12345678;

	@Test
	public void test() {
		long[] dims = { 15, 30 };
		// create input image
		Img<FloatType> input = ArrayImgs.floats(dims);
		MersenneTwisterFast random = new MersenneTwisterFast(SEED);
		for (FloatType b : input) {
			b.setReal(random.nextDouble());
		}

		// create 3 seeds
		Img<BitType> bits = ArrayImgs.bits(dims);
		RandomAccess<BitType> ra = bits.randomAccess();
		ra.setPosition(new int[] { 0, 0 });
		ra.get().set(true);
		ra.setPosition(new int[] { 4, 6 });
		ra.get().set(true);
		ra.setPosition(new int[] { 10, 20 });
		ra.get().set(true);

		// compute labeled seeds
		final ImgLabeling<Integer, IntType> labeledSeeds = ops.labeling().cca(bits, StructuringElement.EIGHT_CONNECTED);

		testWithoutMask(input, labeledSeeds);

		testWithMask(input, labeledSeeds);
	}

	@SuppressWarnings("unchecked")
	private void testWithoutMask(final RandomAccessibleInterval<FloatType> in,
			final ImgLabeling<Integer, IntType> seeds) {
		// create mask which is 1 everywhere
		long[] dims = new long[in.numDimensions()];
		in.dimensions(dims);
		Img<BitType> mask = ArrayImgs.bits(dims);
		for (BitType b : mask) {
			b.setOne();
		}

		/*
		 * use 8-connected neighborhood
		 */
		// compute result without watersheds
		ImgLabeling<Integer, IntType> out = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null, in,
				seeds, true, false);

		assertResults(in, out, seeds, mask, false, false);

		// compute result with watersheds
		ImgLabeling<Integer, IntType> out2 = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null, in,
				seeds, true, true);

		assertResults(in, out2, seeds, mask, true, false);

		/*
		 * use 4-connected neighborhood
		 */
		// compute result without watersheds
		ImgLabeling<Integer, IntType> out3 = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null, in,
				seeds, false, false);

		assertResults(in, out3, seeds, mask, false, false);

		// compute result with watersheds
		ImgLabeling<Integer, IntType> out4 = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null, in,
				seeds, false, true);

		assertResults(in, out4, seeds, mask, true, false);
	}

	@SuppressWarnings("unchecked")
	private void testWithMask(final RandomAccessibleInterval<FloatType> in, final ImgLabeling<Integer, IntType> seeds) {
		// create mask which is 1 everywhere
		long[] dims = new long[in.numDimensions()];
		in.dimensions(dims);
		Img<BitType> mask = ArrayImgs.bits(dims);
		RandomAccess<BitType> raMask = mask.randomAccess();
		for (BitType b : mask) {
			b.setZero();
		}
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				raMask.setPosition(new int[] { x, y });
				raMask.get().setOne();
			}
		}

		/*
		 * use 8-connected neighborhood
		 */
		// compute result without watersheds
		ImgLabeling<Integer, IntType> out = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null, in,
				seeds, true, false, mask);

		assertResults(in, out, seeds, mask, false, true);

		// compute result with watersheds
		ImgLabeling<Integer, IntType> out2 = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null, in,
				seeds, true, true, mask);

		assertResults(in, out2, seeds, mask, true, true);

		/*
		 * use 4-connected neighborhood
		 */
		// compute result without watersheds
		ImgLabeling<Integer, IntType> out3 = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null, in,
				seeds, false, false, mask);

		assertResults(in, out3, seeds, mask, false, true);

		// compute result with watersheds
		ImgLabeling<Integer, IntType> out4 = (ImgLabeling<Integer, IntType>) ops.run(WatershedSeeded.class, null, in,
				seeds, false, true, mask);

		assertResults(in, out4, seeds, mask, true, true);
	}

	private void assertResults(final RandomAccessibleInterval<FloatType> in, final ImgLabeling<Integer, IntType> out,
			final ImgLabeling<Integer, IntType> seeds, final RandomAccessibleInterval<BitType> mask,
			final boolean withWatersheds, final boolean smallMask) {

		final Cursor<LabelingType<Integer>> curOut = out.cursor();
		final RandomAccess<BitType> raMask = mask.randomAccess();
		while (curOut.hasNext()) {
			curOut.fwd();
			raMask.setPosition(curOut);
			if (raMask.get().get()) {
				assertEquals(1, curOut.get().size());
			} else {
				assertEquals(true, curOut.get().isEmpty());
			}
		}
		// Sample the output image based on the mask
		IterableRegion<BitType> regions = Regions.iterable(mask);

		// count labels
		Set<Integer> labelSet = new HashSet<>();
		for (LabelingType<Integer> pixel : Regions.sample(regions, out)) {
			labelSet.addAll(pixel);
		}

		// assert equals
		assertEquals(in.numDimensions(), out.numDimensions());
		assertEquals(in.dimension(0), out.dimension(0));
		assertEquals(in.dimension(1), out.dimension(1));
		assertEquals(3 + (withWatersheds ? 1 : 0), labelSet.size() + (smallMask ? 1 : 0));
	}

}
