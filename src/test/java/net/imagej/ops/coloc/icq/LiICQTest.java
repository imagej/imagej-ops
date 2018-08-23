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

package net.imagej.ops.coloc.icq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.imagej.ops.coloc.ColocalisationTest;
import net.imagej.ops.coloc.pValue.DefaultPValue;
import net.imagej.ops.coloc.pValue.PValueResult;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.Ops.Coloc.ICQ}.
 *
 * @author Curtis Rueden
 */
public class LiICQTest extends ColocalisationTest {

	@Test
	public void testICQ() {
		final Img<ByteType> img1 = generateByteArrayTestImg(true, 10, 15, 20);
		final Img<ByteType> img2 = generateByteArrayTestImg(true, 10, 15, 20);

		final Object icqValue = ops.coloc().icq(img1, img2);

		assertTrue(icqValue instanceof Double);
		assertEquals(0.5, (Double) icqValue, 0.0);
	}

	/**
	 * Checks Li's ICQ value for positive correlated images.
	 */
	@Test
	public void liPositiveCorrTest() {
		final Object icqValue = ops.run(LiICQ.class, positiveCorrelationImageCh1,
			positiveCorrelationImageCh2);

		assertTrue(icqValue instanceof Double);
		final double icq = (Double) icqValue;
		assertTrue(icq > 0.34 && icq < 0.35);
	}

	/**
	 * Checks Li's ICQ value for zero correlated images. The ICQ value should be
	 * about zero.
	 */
	@Test
	public void liZeroCorrTest() {
		final Object icqValue = ops.coloc().icq(zeroCorrelationImageCh1,
			zeroCorrelationImageCh2);

		assertTrue(icqValue instanceof Double);
		final double icq = (Double) icqValue;
		assertTrue(Math.abs(icq) < 0.01);
	}

	/**
	 * Checks calculated pValue for Li's ICQ.
	 */
	@Test
	public void testPValue() {
		final double mean = 0.2;
		final double spread = 0.1;
		final double[] sigma = new double[] { 3.0, 3.0 };
		Img<FloatType> ch1 = ColocalisationTest.produceMeanBasedNoiseImage(new FloatType(), 24, 24,
			mean, spread, sigma, 0x01234567);
		Img<FloatType> ch2 = ColocalisationTest.produceMeanBasedNoiseImage(new FloatType(), 24, 24,
			mean, spread, sigma, 0x98765432);
		BinaryFunctionOp<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<FloatType>, Double> op =
			Functions.binary(ops, LiICQ.class, Double.class, ch1, ch2);
		PValueResult value = (PValueResult) ops.run(DefaultPValue.class, new PValueResult(), ch1, ch2, op);
		assertEquals(0.82, value.getPValue(), 0.0);
	}

}
