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

package net.imagej.ops.image.integral;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops;
import net.imagej.ops.threshold.apply.LocalThresholdTest;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Stefan Helfrich (University of Konstanz)
 */
public class IntegralImgTest extends AbstractOpTest {

	Img<ByteType> in;
	RandomAccessibleInterval<DoubleType> out;

	/**
	 * Initialize images.
	 *
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
		in = generateByteArrayTestImg(true, new long[] { 10, 10 });
	}

	/**
	 * @see DefaultIntegralImg
	 * @see SquareIntegralImg
	 */
	@SuppressWarnings({ "unchecked" })
	@Test
	public void testIntegralImageCreation() {
		out = (RandomAccessibleInterval<DoubleType>) ops.run(
			Ops.Image.Integral.class, in);
		out = (RandomAccessibleInterval<DoubleType>) ops.run(
			Ops.Image.SquareIntegral.class, in);
	}

	/**
	 * @see DefaultIntegralImg
	 * @see SquareIntegralImg
	 */
	@SuppressWarnings({ "unchecked" })
	@Test
	public void testIntegralImageSimilarity() {
		RandomAccessibleInterval<LongType> out1 =
			(RandomAccessibleInterval<LongType>) ops.run(DefaultIntegralImg.class,
				in);
		RandomAccessibleInterval<DoubleType> out2 =
			(RandomAccessibleInterval<DoubleType>) ops.run(WrappedIntegralImg.class,
				in);

		// Remove 0s from integralImg by shifting its interval by +1
		final long[] min = new long[out2.numDimensions()];
		final long[] max = new long[out2.numDimensions()];

		for (int d = 0; d < out2.numDimensions(); ++d) {
			min[d] = out2.min(d) + 1;
			max[d] = out2.max(d);
		}

		// Define the Interval on the infinite random accessibles
		final FinalInterval interval = new FinalInterval(min, max);

		LocalThresholdTest.testIterableIntervalSimilarity(Views.iterable(out1),
			Views.iterable(Views.offsetInterval(out2, interval)));
	}

	public ArrayImg<ByteType, ByteArray> generateKnownByteArrayTestImgLarge() {
		final long[] dims = new long[] { 3, 3 };
		final byte[] array = new byte[9];

		array[0] = (byte) 40;
		array[1] = (byte) 40;
		array[2] = (byte) 20;

		array[3] = (byte) 40;
		array[4] = (byte) 40;
		array[5] = (byte) 20;

		array[6] = (byte) 20;
		array[7] = (byte) 20;
		array[8] = (byte) 100;

		return ArrayImgs.bytes(array, dims);
	}

}
