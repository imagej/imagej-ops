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

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.IterablePair;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * A class that represents the mean calculation of the two source images in the
 * data container. It implements the FAST calculation for Pearson's Correlation.
 *
 * @author Ellen T Arena
 */
@Plugin(type = Ops.Coloc.Pearsons.class)
public class DefaultPearsons<T extends RealType<T>, U extends RealType<U>> extends
	AbstractBinaryFunctionOp<Iterable<T>, Iterable<U>, Double> implements
	Ops.Coloc.Pearsons
{

	@Override
	public Double calculate(final Iterable<T> image1,
		final Iterable<U> image2)
	{
		return fastPearsons(new IterablePair<>(image1, image2));
	}

	/**
	 * Calculates Person's R value by using a fast implementation of the
	 * algorithm taken from Coloc 2.
	 *
	 * @return Person's R value
	 * @throws IllegalArgumentException If input data is statistically unsound.
	 */
	private double fastPearsons(final Iterable<Pair<T, U>> samples) {
		// the actual accumulation of the image values is done in a separate object
		Accumulator acc = new Accumulator(samples);

		// for faster computation, have the inverse of N available
		final int count = acc.getCount();
		final double invCount = 1.0 / count;

		final double pearsons1 = acc.getXY() - (acc.getX() * acc.getY() * invCount);
		final double pearsons2 = acc.getXX() - (acc.getX() * acc.getX() * invCount);
		final double pearsons3 = acc.getYY() - (acc.getY() * acc.getY() * invCount);
		final double pearsonsR = pearsons1 / (Math.sqrt(pearsons2 * pearsons3));

		checkForSanity(pearsonsR, count);

		return pearsonsR;
	}

	/**
	 * Does a sanity check for calculated Pearsons values. Wrong values can happen
	 * for fast and classic implementation.
	 *
	 * @param value The value to check.
	 * @throws IllegalArgumentException
	 */
	private static void checkForSanity(final double value, final int iterations) {
		if (Double.isNaN(value) || Double.isInfinite(value)) {
			/* For the _fast_ implementation this could happen:
			 *   Infinity could happen if only the numerator is 0, i.e.:
			 *     sum1squared == sum1 * sum1 * invN
			 *   and
			 *     sum2squared == sum2 * sum2 * invN
			 *   If the denominator is also zero, one will get NaN, i.e:
			 *     sumProduct1_2 == sum1 * sum2 * invN
			 *
			 * For the classic implementation it could happen, too:
			 *   Infinity happens if one channels sum of value-mean-differences
			 *   is zero. If it is negative for one image you will get NaN.
			 *   Additionally, if is zero for both channels at once you
			 *   could get NaN. NaN
			 */
			throw new IllegalArgumentException(
				"A numerical problem occured: the input data is unsuitable for this algorithm. Possibly too few pixels (in range were: " +
					iterations + ").");
		}
	}

	// -- Helper classes --

	/**
	 * A class allowing an easy accumulation of values visited by an
	 * IterablePair. After instantiation the sum of channel one,
	 * channel two, products with them self and a product of both of
	 * them will be available. It additionally provides the possibility
	 * to subtract values from the data before adding them to the sum.:
	 * 
	 * @author Johannes Schindelin
	 * @author Tom Kazimiers
	 * @author Ellen T Arena
	 */
	private class Accumulator {

		private double x, y, xx, xy, yy;
		private int count;

		/**
		 * The two values x and y from each iteration to get
		 * summed up as single values and their combinations.
		 */
		public Accumulator(final Iterable<Pair<T, U>> samples) {
			this(samples, false, 0.0d, 0.0d);
		}

		protected Accumulator(final Iterable<Pair<T, U>> samples, boolean substract, double xDiff, double yDiff) {

			for (Pair<T, U> sample : samples) {

				double value1 = sample.getA().getRealDouble();
				double value2 = sample.getB().getRealDouble();

				if (substract) {
					value1 -= xDiff;
					value2 -= yDiff;
				}

				x += value1;
				y += value2;
				xx += value1 * value1;
				xy += value1 * value2;
				yy += value2 * value2;
				count++;
			}
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getXX() {
			return xx;
		}

		public double getXY() {
			return xy;
		}

		public double getYY() {
			return yy;
		}

		public int getCount() {
			return count;
		}
	}
}
