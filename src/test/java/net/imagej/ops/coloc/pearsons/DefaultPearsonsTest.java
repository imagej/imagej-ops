/*-
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
package net.imagej.ops.coloc.pearsons;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.Ops;
import net.imagej.ops.coloc.ColocalisationTest;
import net.imagej.ops.coloc.pValue.PValueResult;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

/**
 * Tests {@link DefaultPearsons}.
 *
 * @author Ellen T Arena
 */
public class DefaultPearsonsTest extends ColocalisationTest {
	
	/**
	 * Tests if the fast implementation of Pearson's correlation with two
	 * zero correlated images produce a Pearson's R value of about zero.
	 */
	@Test
	public void fastPearsonsZeroCorrTest(){
		double result = (Double) ops.run(DefaultPearsons.class, zeroCorrelationImageCh1, zeroCorrelationImageCh2);
		assertEquals(0.0, result, 0.05);
	}
	
	/**
	 * Tests if the fast implementation of Pearson's correlation with two
	 * positive correlated images produce a Pearson's R value of about 0.75.
	 */
	@Test
	public void fastPearsonsPositiveCorrTest() {
		double result = (Double) ops.run(DefaultPearsons.class, positiveCorrelationImageCh1, positiveCorrelationImageCh2);
		assertEquals(0.75, result, 0.01);
	}
	
	/**
	 * Tests Pearson's correlation stays close to zero for image pairs with the same mean and spread
	 * of randomized pixel values around that mean.
	 */
	@Test
	public void differentMeansTest()  {
		final double initialMean = 0.2;
		final double spread = 0.1;
		final double[] sigma = new double[] {3.0, 3.0};

		for (double mean = initialMean; mean < 1; mean += spread) {
			RandomAccessibleInterval<FloatType> ch1 = produceMeanBasedNoiseImage(new FloatType(),
					512, 512, mean, spread, sigma, 0x01234567);
			RandomAccessibleInterval<FloatType> ch2 = produceMeanBasedNoiseImage(new FloatType(),
					512, 512, mean, spread, sigma, 0x98765432);
			double resultFast = (Double) ops.run(DefaultPearsons.class, ch1, ch2);
			assertEquals(0.0, resultFast, 0.1);

			/* If the means are the same, it causes a numerical problem in the classic implementation of Pearson's
			 * double resultClassic = PearsonsCorrelation.classicPearsons(cursor, mean, mean);
			 * assertTrue(Math.abs(resultClassic) < 0.1);
			 */
		}
	}

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
			Functions.binary(ops, Ops.Coloc.Pearsons.class, Double.class, ch1, ch2);
		PValueResult value = (PValueResult) ops.run(Ops.Coloc.PValue.class, new PValueResult(), ch1, ch2, op);
		assertEquals(0.8, value.getPValue(), 0.0);
	}

}
