/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.threshold;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ij.ImagePlus;
import ij.process.AutoThresholder.Method;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.img.display.imagej.ImageJFunctions;

/**
 * Tests for Global Threshold ops
 * 
 * @author bnorthan
 */
public class GlobalThresholdTest extends AbstractOpTest {

	int xSize = 10;
	int ySize = 10;

	Img<UnsignedShortType> in;

	@Before
	public void initialize() {

		long[] dimensions = new long[] { xSize, ySize };

		// create image and output
		in = new ArrayImgFactory<UnsignedShortType>().create(dimensions,
				new UnsignedShortType());
	}

	@Test
	public void testManualThreshold() throws IncompatibleTypeException {

		Img<BitType> manual = in.factory().imgFactory(new BitType())
				.create(in, new BitType());

		UnsignedShortType threshold = new UnsignedShortType();
		threshold.set(1009);

		ops.module(
				"threshold",
				manual,
				in,
				threshold).run();
	}

	/**
	 * This test generates a 2D ramp image then thresholds. After thresholding
	 * the number of foreground pixels are counted. The image is then wrapped as
	 * an ImagePlus and setAutoThreshold is called from the ImageProcessor. We
	 * verify that the op and setAutoThreshold are producing the same result.
	 * 
	 * @throws IncompatibleTypeException
	 */
	@Test
	public void testOtsuThreshold() throws IncompatibleTypeException {

		Img<BitType> out = in.factory().imgFactory(new BitType())
				.create(in, new BitType());

		RandomAccess<UnsignedShortType> ra = in.randomAccess();

		// populate pixel values with a ramp function + a constant
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				ra.setPosition(new int[] { x, y });
				ra.get().setReal(x + y + 1000);
			}
		}

		// apply Otsu segmentation algorithm
		// TODO: allow things like ops.run(name, parameters, delegate)?
		// TODO: Example here ops.module("threshold", res, in, Otsu.class). We
		// can do that as soon as otsu knows how to create its output.

		// TODO what do you expect here? a ready to go (everything injected) op
		// or just like a pointer to an op.
		Op otsuOp = ops.op(ComputeOtsuThreshold.class, in.firstElement().createVariable()
				.getClass(), IterableInterval.class);

		ops.run("threshold", out, in, otsuOp);

		// Ideally, we instead want this to be simpler:
		// out = ops.op("otsu", inputImgPlus);

		// loop through the output pixels and count
		// the number that are above zero
		long count = 0;
		for (BitType b : out) {
			if (b.getRealFloat() > 0) {
				count++;
			}
		}

		// // convert to IJ1 ImagePlus
		// ImagePlus imp = ImageJFunctions.wrapUnsignedShort(in, "gradient16");
		//
		// // run IJ1 Otsu Auto threshold
		// imp.getProcessor().setAutoThreshold(Method.Otsu, true);
		// double minthreshold = imp.getProcessor().getMinThreshold();
		//
		// short count2 = 0;
		//
		// // loop through all the pixels counting the foreground
		// (>minthreshold)
		// // pixels
		// for (int x = 0; x < xSize; x++) {
		// for (int y = 0; y < ySize; y++) {
		// if (imp.getPixel(y, x)[0] >= minthreshold)
		// count2 += 1;
		// }
		// }
		//
		// // verify the count of foreground pixels from the op and imagej1 test
		// // are
		// // equal
		assertEquals(count, 45);
	}

}
