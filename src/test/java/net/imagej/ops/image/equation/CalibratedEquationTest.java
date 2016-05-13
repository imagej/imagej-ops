/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.image.equation;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

public class CalibratedEquationTest extends AbstractOpTest {

	long[] size = new long[] { 100, 100 };

	// define start and end
	double[] start = new double[] { -1., -1. };
	double[] end = new double[] { 1., 1. };

	// calculate spacing based on size and starting point
	double[] spacing = new double[] { (end[0] - start[0]) / (size[0] - 1),
		(end[1] - start[1]) / (size[1] - 1) };

	Dimensions dimensions = new FinalDimensions(size[0], size[1]);

	@Test
	public void testEquationXY() {
		IterableInterval<DoubleType> image = ops.create().img(dimensions,
			new DoubleType());

		ops.image().equation(image, (x, y) -> 10 * (Math.cos(0.3 * x) + Math.sin(
			0.3 * y)));
		
		// assert against value determined by running string version of equation in jython
		assertEquals(ops.stats().sum(image).getRealDouble(), 446.1755977585166, 0.000000001);

	}

	@Test
	public void testCalibratedEquationXY() {

		IterableInterval<DoubleType> image = ops.create().img(dimensions,
			new DoubleType());

		ops.image().equation(image, (x, y) -> Math.pow(x, 2) + Math.pow(y, 2),
			start, spacing);

		assertEquals(6801.346801346799, ops.stats().sum(image).getRealDouble(),
			0.0000001);

	}

	@Test
	public void testCalibratedEquation() {

		IterableInterval<DoubleType> image = ops.create().img(dimensions,
			new DoubleType());

		// implement x^2+y^2
		UnaryFunctionOp<double[], Double> op =
			new AbstractUnaryFunctionOp<double[], Double>()
		{

				@Override
				public Double compute1(double[] coords) {
					double result = 0;
					for (double d : coords) {
						result += Math.pow(d, 2);
					}

					return result;
				}
			};

		ops.image().equation(image, op, start, spacing);

		assertEquals(6801.346801346799, ops.stats().sum(image).getRealDouble(),
			0.0000001);

	}

}
