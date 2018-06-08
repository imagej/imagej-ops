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

package net.imagej.ops.image.equation;

import static org.junit.Assert.assertEquals;

import java.util.function.DoubleBinaryOperator;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

/**
 * Test XY, and coordinate based equations that is equations of the form f(x,y)
 * or f(c[0], [1], ... c[n]) where c represents pixel coordinates
 * 
 * @author bnorthan
 */
public class CoordinateEquationTest extends AbstractOpTest {

	long[] size = new long[] { 100, 100 };

	// define start and end
	double[] start = new double[] { -1., -1. };
	double[] end = new double[] { 1., 1. };

	// calculate spacing based on size and starting point
	double[] spacing = new double[] { (end[0] - start[0]) / (size[0] - 1),
		(end[1] - start[1]) / (size[1] - 1) };

	Dimensions dimensions = new FinalDimensions(size[0], size[1]);

	/**
	 * Test the lambda version of the XY equation
	 */
	@Test
	public void testEquationXY() {

		// create a double image
		final IterableInterval<DoubleType> image1 = ops.create().img(dimensions,
			new DoubleType());

		ops.image().equation(image1, (x, y) -> 10 * (Math.cos(0.3 * x) + Math.sin(
			0.3 * y)));

		// assert against value determined by running string version of equation in
		// jython
		assertEquals(ops.stats().sum(image1).getRealDouble(), 446.1755977585166,
			0.001);

		// create an integer type image
		final IterableInterval<IntType> image2 = ops.create().img(dimensions,
			new IntType());

		// implement x^2+y^2
		ops.image().equation(image2, (x, y) -> Math.pow(x, 2) + Math.pow(y, 2));

		assertEquals(ops.stats().sum(image2).getRealDouble(), 6.567e7, 0.001);

		// create a float image
		final IterableInterval<FloatType> image3 = ops.create().img(dimensions,
			new FloatType());

		// implement x^2+y^2 taking into account the calibration (start and spacing)
		DoubleBinaryOperator equation = (x, y) -> Math.pow(start[0] + spacing[0] *
			x, 2) + Math.pow(start[1] + spacing[1] * y, 2);

		ops.run(net.imagej.ops.image.equation.DefaultXYEquation.class, image3,
			equation);

		assertEquals(6801.346801346799, ops.stats().sum(image3).getRealDouble(),
			0.00001);
	}

	/**
	 * Test the coordinate op version of the equation using 2 dimensions
	 */
	@Test
	public void testEquation2DOp() {

		final IterableInterval<DoubleType> image = ops.create().img(dimensions,
			new DoubleType());

		// implement x^2+y^2 taking into account the calibration
		final UnaryFunctionOp<long[], Double> op =
			new AbstractUnaryFunctionOp<long[], Double>()
			{

				@Override
				public Double calculate(final long[] coords) {
					final double result = Math.pow(start[0] + coords[0] * spacing[0], 2) +
						Math.pow(start[1] + coords[1] * spacing[1], 2);

					return result;
				}
			};

		ops.run(DefaultCoordinatesEquation.class, image, op);

		assertEquals(6801.346801346799, ops.stats().sum(image).getRealDouble(),
			0.00001);

	}

	/**
	 * Test the coordinate op version of the equation using 4 dimensions
	 */
	@Test
	public void testEquation4DOp() {

		final long[] size4D = new long[] { 5, 5, 5, 5 };

		final Dimensions dimensions4D = new FinalDimensions(size4D);

		final Img<ShortType> image = ops.create().img(dimensions4D,
			new ShortType());

		// implement c[0]+10*c[1]+100*c[3]+1000*c[4]
		final UnaryFunctionOp<long[], Double> op =
			new AbstractUnaryFunctionOp<long[], Double>()
			{

				@Override
				public Double calculate(final long[] coords) {
					final double result = coords[0] + 10 * coords[1] + 100 * coords[2] +
						1000 * coords[3];

					return result;
				}
			};

		ops.run(DefaultCoordinatesEquation.class, image, op);

		final RandomAccess<ShortType> ra = image.randomAccess();

		ra.setPosition(new long[] { 1, 2, 2, 3 });

		assertEquals(1 + 10 * 2 + 100 * 2 + 1000 * 3, ra.get().getRealFloat(),
			0.000001);

	}

}
