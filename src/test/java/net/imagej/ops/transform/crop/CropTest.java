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

package net.imagej.ops.transform.crop;

import static org.junit.Assert.assertTrue;

import net.imagej.ImgPlus;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.Ops.Transform.Crop}.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public class CropTest extends AbstractOpTest {

	private Img<ByteType> in;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		in = ArrayImgs.bytes(20, 20, 20);
	}

	/** Verifies that the types of the objects returned are correct. */
	@Test
	public void testCropTypes() {
		// Set-up interval
		final Interval defInterval = new FinalInterval(new long[] { 0, 0, 0 },
			new long[] { 19, 19, 19 });

		final Interval smallerInterval = new FinalInterval(new long[] { 0, 0, 0 },
			new long[] { 19, 19, 18 });

		// check if result is ImgView
		assertTrue(ops.run(CropRAI.class, in, defInterval) instanceof Img);

		// check if result is ImgPlus
		final Object imgPlus = ops.run(CropImgPlus.class, new ImgPlus<>(in),
			defInterval);
		assertTrue(imgPlus instanceof ImgPlus);

		// check if result is RandomAccessibleInterval
		final Object run = ops.run(CropRAI.class, Views.interval(in,
			smallerInterval), smallerInterval);
		assertTrue(run instanceof RandomAccessibleInterval &&
			!(run instanceof Img));
	}

	/** Tests the result of the slicing. */
	@Test
	public void testCropResults() {

		// Case 1: fix one dimension
		long[] min = { 0, 0, 5 };
		long[] max = { 19, 19, 5 };
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<ByteType> res1 =
			(RandomAccessibleInterval<ByteType>) ops.run(CropRAI.class, in,
				new FinalInterval(min, max));

		assertTrue(res1.numDimensions() == 2);
		assertTrue(res1.min(0) == 0);
		assertTrue(res1.max(0) == 19);

		// Case 2: Fix one dimension and don't start at zero
		max = new long[] { 19, 0, 10 };
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<ByteType> res2 =
			(RandomAccessibleInterval<ByteType>) ops.run(CropRAI.class, in,
				new FinalInterval(min, max));

		assertTrue(res2.numDimensions() == 2);
		assertTrue(res2.min(0) == 0);
		assertTrue(res2.max(1) == 5);

		// Case 3: fix two dimensions
		min = new long[] { 0, 0, 0 };
		max = new long[] { 0, 15, 0 };
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<ByteType> res3 =
			(RandomAccessibleInterval<ByteType>) ops.run(CropRAI.class, in,
				new FinalInterval(min, max));

		assertTrue(res3.numDimensions() == 1);
		assertTrue(res3.max(0) == 15);
	}
}
