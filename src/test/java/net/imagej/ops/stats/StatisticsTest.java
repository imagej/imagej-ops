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

package net.imagej.ops.stats;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests statistics operations using the following general pattern.
 * <ol>
 * <li>Generate a random test image.</li>
 * <li>Get a reference to the raw data pointer.</li>
 * <li>Calculate the statistic by directly using the raw data.</li>
 * <li>Calculate the statistic by calling the op.</li>
 * <li>Assert that the two values are the same.</li>
 * </ol>
 * 
 * @author Brian Northan
 */
public class StatisticsTest extends AbstractOpTest {

	double delta = 0.001;

	ArrayImg<FloatType, FloatArray> img;
	float array[];
	long arraySize;

	private Img<UnsignedByteType> randomlyFilledImg;

	@Override
	@Before
	public void setUp() {
		super.setUp();

		// make a random float array image
		img = generateFloatArrayTestImg(true, 100, 100);

		// get direct access to the float array
		array = img.update(null).getCurrentStorageArray();

		arraySize = 1;
		for (int d = 0; d < img.numDimensions(); d++)
			arraySize *= img.dimension(d);

		randomlyFilledImg = generateRandomlyFilledUnsignedByteTestImgWithSeed(
			new long[] { 100, 100 }, 1234567890L);
	}

	@Test
	public void MinMaxTest() {
		float min1 = Float.MAX_VALUE;
		float max1 = Float.MIN_VALUE;

		// loop through the array calculating min and max
		for (int i = 0; i < arraySize; i++) {
			if (array[i] < min1) min1 = array[i];
			if (array[i] > max1) max1 = array[i];
		}

		// calculate min using ops
		FloatType min2 = new FloatType();
		min2.setReal(Float.MAX_VALUE);
		ops.stats().min(min2, img);

		// calculate max using ops
		FloatType max2 = new FloatType();
		max2.setReal(Float.MIN_VALUE);
		ops.stats().max(max2, img);

		// check to see if everything matches
		Assert.assertEquals(min1, min2.getRealFloat(), delta);
		Assert.assertEquals(max1, max2.getRealFloat(), delta);
	}

	@Test
	public void MeanStdTest() {
		float sum = 0.0f;

		for (int i = 0; i < arraySize; i++) {

			sum += array[i];
		}

		float variance = 0.0f;

		float mean1 = sum / (arraySize);

		// use the mean to calculate the variance
		for (int i = 0; i < arraySize; i++) {
			float temp = array[i] - mean1;
			variance += temp * temp;
		}

		variance = variance / arraySize;
		float std1 = (float) Math.sqrt(variance);

		// calculate mean using ops
		final DoubleType mean2 = new DoubleType();
		ops.stats().mean(mean2, img);

		// check that the ratio between mean1 and mean2 is 1.0
		Assert.assertEquals(1.0, mean1 / mean2.getRealFloat(), delta);

		// calculate standard deviation using ops
		final DoubleType std2 = new DoubleType();
		ops.stats().stdDev(std2, img);

		// check that the ratio between std1 and std2 is 1.0
		Assert.assertEquals(1.0, std1 / std2.getRealFloat(), delta);
	}

	@Test
	public void testMax() {
		Assert.assertEquals("Max", 254d, ops.stats().max(randomlyFilledImg)
			.getRealDouble(), 0.00001d);
	}

	@Test
	public void testMean() {
		Assert.assertEquals("Mean", 127.7534, ops.stats().mean(randomlyFilledImg)
			.getRealDouble(), 0.00001d);
	}

	@Test
	public void testMedian() {
		Assert.assertEquals("Median", 128d, ops.stats().median(randomlyFilledImg)
			.getRealDouble(), 0.00001d);
	}

	@Test
	public void testMin() {
		Assert.assertEquals("Min", 0, ops.stats().min(randomlyFilledImg)
			.getRealDouble(), 0.00001d);
	}

	@Test
	public void testStdDev() {
		Assert.assertEquals("StdDev", 73.7460374274008, ops.stats().stdDev(
			randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testSum() {
		Assert.assertEquals("Sum", 1277534.0, ops.stats().sum(randomlyFilledImg)
			.getRealDouble(), 0.00001d);
	}

	@Test
	public void testVariance() {
		Assert.assertEquals("Variance", 5438.4780362436, ops.stats().variance(
			randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testGeometricMean() {
		Assert.assertEquals("Geometric Mean", 0, ops.stats().geometricMean(
			randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testHarmonicMean() {
		Assert.assertEquals("Harmonic Mean", 0, ops.stats().harmonicMean(
			randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testKurtosis() {
		Assert.assertEquals("Kurtosis", 1.794289587623922, ops.stats().kurtosis(
			randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testMoment1AboutMean() {
		Assert.assertEquals("Moment 1 About Mean", 0, ops.stats().moment1AboutMean(
			randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testMoment2AboutMean() {
		Assert.assertEquals("Moment 2 About Mean", 5437.93418843998, ops.stats()
			.moment2AboutMean(randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testMoment3AboutMean() {
		Assert.assertEquals("Moment 3 About Mean", -507.810691261427, ops.stats()
			.moment3AboutMean(randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testMoment4AboutMean() {
		Assert.assertEquals("Moment 4 About Mean", 53069780.9168701, ops.stats()
			.moment4AboutMean(randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testPercentile() {
		Assert.assertEquals("50-th Percentile", 128d, ops.stats().percentile(randomlyFilledImg, 50d)
				.getRealDouble(), 0.00001d);
	}

	@Test
	public void testSkewness() {
		Assert.assertEquals("Skewness", -0.0012661517853476312, ops.stats()
			.skewness(randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testSumOfInverses() {
		Assert.assertEquals("Sum Of Inverses", Double.POSITIVE_INFINITY, ops.stats()
			.sumOfInverses(randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testSumOfLogs() {
		Assert.assertEquals("Sum Of Logs", Double.NEGATIVE_INFINITY, ops.stats()
			.sumOfLogs(randomlyFilledImg).getRealDouble(), 0.00001d);
	}

	@Test
	public void testSumOfSquares() {
		Assert.assertEquals("Sum Of Squares", 217588654, ops.stats().sumOfSquares(
			randomlyFilledImg).getRealDouble(), 0.00001d);
	}
}
