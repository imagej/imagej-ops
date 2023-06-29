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

package net.imagej.ops.image.quality;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for (P)SNR, RMSE, and MAE.
 *
 * @author Stefan Helfrich (University of Konstanz)
 */
public class QualityTest extends AbstractOpTest {

	Img<ByteType> reference;
	Img<ByteType> test;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		reference = ArrayImgs.bytes(new byte[] { 9, 5, 8, 0, 9, 5, 2, 3, 7 }, 3, 3);
		test = ArrayImgs.bytes(new byte[] { 8, 4, 9, 3, 2, 7, 1, 3, 7 }, 3, 3);
	}

	@Test
	public void testSNR() {
		final DoubleType snr = new DoubleType();
		ops.run(net.imagej.ops.image.quality.DefaultSNR.class, snr, reference, test);

		assertEquals(7.0937, snr.getRealDouble(), 0.0001);
	}

	@Test
	public void testPSNR() {
		final DoubleType psnr = new DoubleType();
		ops.run(net.imagej.ops.image.quality.DefaultPSNR.class, psnr, reference, test);

		assertEquals(10.4319, psnr.getRealDouble(), 0.0001);
	}

	@Test
	public void testRMSE() {
		final DoubleType rmse = new DoubleType();
		ops.run(net.imagej.ops.image.quality.DefaultRMSE.class, rmse, reference, test);

		assertEquals(2.7080, rmse.getRealDouble(), 0.0001);
	}

	@Test
	public void testMAE() {
		final DoubleType mae = new DoubleType();
		ops.run(net.imagej.ops.image.quality.DefaultMAE.class, mae, reference, test);

		assertEquals(1.7777, mae.getRealDouble(), 0.0001);
	}

}
