/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops.convolve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imglib2.Point;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.ShortType;

import org.junit.Test;

/**
 * Tests involving convolvers.
 */
public class ConvolveTest extends AbstractOpTest {

	/** Tests that the correct convolver is selected. */
	@Test
	public void testConvolveMethodSelection() {

		final Img<ByteType> in =
			new ArrayImgFactory<ByteType>().create(new int[] { 20, 20 },
				new ByteType());
		final Img<ByteType> out = in.copy();

		// testing for a small kernel
		Img<ByteType> kernel =
			new ArrayImgFactory<ByteType>()
				.create(new int[] { 3, 3 }, new ByteType());
		Op op = ops.op("convolve", out, in, kernel);
		assertSame(ConvolveNaive.class, op.getClass());

		// testing for a 'bigger' kernel 
		kernel =
			new ArrayImgFactory<ByteType>().create(new int[] { 10, 10 },
				new ByteType());
		op = ops.op("convolve", in, out, kernel);
		assertSame(ConvolveFourier.class, op.getClass());

	}

	@Test
	public void testConvolve() {

		int[] size = new int[] { 225, 167 };
		int[] kernelSize = new int[] { 27, 39 };

		// create an input with a small sphere at the center
		Img<ShortType> in =
			new ArrayImgFactory<ShortType>().create(size, new ShortType());
		placeSphereInCenter(in);

		// create an output
		Img<ShortType> out =
			new ArrayImgFactory<ShortType>().create(size, new ShortType());

		// create a kernel with a small sphere in the center
		Img<ShortType> kernel =
			new ArrayImgFactory<ShortType>().create(kernelSize, new ShortType());
		placeSphereInCenter(kernel);

		// create variables to hold the image sums
		ShortType inSum = new ShortType();
		ShortType kernelSum = new ShortType();
		ShortType outSum = new ShortType();

		// calculate sum of input and kernel
		ops.run("sum", inSum, in);
		ops.run("sum", kernelSum, kernel);

		// convolve and calculate sum of output
		ops.run("convolve", out, in, kernel);
		ops.run("sum", outSum, out);

		// multiply input sum by kernelSum and assert it is the same as outSum
		inSum.mul(kernelSum);
		assertEquals(inSum, outSum);
	}

	// utility to place a small sphere at the center of the image
	private void placeSphereInCenter(Img<ShortType> img) {

		final Point center = new Point(img.numDimensions());

		for (int d = 0; d < img.numDimensions(); d++)
			center.setPosition(img.dimension(d) / 2, d);

		HyperSphere<ShortType> hyperSphere =
			new HyperSphere<ShortType>(img, center, 2);

		for (final ShortType value : hyperSphere) {
			value.setReal(1);
		}
	}
}
