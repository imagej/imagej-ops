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

package net.imagej.ops.statistics;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.statistics.FirstOrderOps.Max;
import net.imagej.ops.statistics.FirstOrderOps.Mean;
import net.imagej.ops.statistics.FirstOrderOps.Min;
import net.imagej.ops.statistics.FirstOrderOps.StdDeviation;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests statistics operations using the following general pattern. 
 * 
 * 1. Generate a random test image. 
 * 2. Get a reference to the raw data pointer. 
 * 3. Calculate the statistic by directly using the raw data. 
 * 4. Calculate the statistic by calling the op. 
 * 5. Assert that the two values are the same.
 * 
 * @author Brian Northan
 */
public class StatisticsTest extends AbstractOpTest {

	double delta = 0.001;

	ArrayImg<FloatType, FloatArray> img;
	float array[];
	long arraySize;

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
		ops.run(Min.class, min2, img);

		// calculate max using ops
		FloatType max2 = new FloatType();
		max2.setReal(Float.MIN_VALUE);
		ops.run(Max.class, max2, img);

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
		DoubleType mean2 = new DoubleType();
		mean2 = (DoubleType) ops.run(Mean.class, DoubleType.class, img);

		// check that the ratio between mean1 and mean2 is 1.0
		Assert.assertEquals(1.0, mean1 / mean2.getRealFloat(), delta);

		// calculate standard deviation using ops
		DoubleType std2 = new DoubleType();
		ops.run(StdDeviation.class, std2, img);

		// check that the ratio between std1 and std2 is 1.0
		Assert.assertEquals(1.0, std1 / std2.getRealFloat(), delta);
	}
}
