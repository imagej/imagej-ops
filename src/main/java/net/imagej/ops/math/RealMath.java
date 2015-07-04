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

package net.imagej.ops.math;

import java.util.Random;

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.MathOps;
import net.imagej.ops.Op;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Ops of the {@code math} namespace which operate on {@link RealType}s.
 *
 * @author Barry DeZonia
 * @author Jonathan Hale
 * @author Curtis Rueden
 */
public final class RealMath {

	private RealMath() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Sets the real component of an output real number to the absolute value of
	 * the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Abs.NAME)
	public static class Abs<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Abs
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.abs(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the addition of the
	 * real component of an input real number with a constant value.
	 */
	@Plugin(type = Op.class, name = MathOps.Add.NAME)
	public static class Add<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Add
	{

		@Parameter
		private double constant;

		@Override
		public O compute(final I input, final O output) {
			output.setReal(input.getRealDouble() + constant);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the addition of the
	 * real component of an input real number with an amount of Gaussian noise.
	 */
	@Plugin(type = Op.class, name = MathOps.AddNoise.NAME)
	public static class AddNoise<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.AddNoise
	{

		@Parameter
		private double rangeMin;
		@Parameter
		private double rangeMax;
		@Parameter
		private double rangeStdDev;
		@Parameter
		private Random rng;

		@Override
		public O compute(final I input, final O output) {
			if (rng == null) {
				rng = new Random(System.currentTimeMillis());
			}
			int i = 0;
			do {
				final double newVal =
					input.getRealDouble() + (rng.nextGaussian() * rangeStdDev);
				if ((rangeMin <= newVal) && (newVal <= rangeMax)) {
					output.setReal(newVal);
					return output;
				}
				if (i++ > 100) throw new IllegalArgumentException(
					"noise function failing to terminate. probably misconfigured.");
			}
			while (true);
		}
	}

	/**
	 * Sets the real component of an output real number to the logical AND of the
	 * real component of an input real number with a constant value.
	 */
	@Plugin(type = Op.class, name = MathOps.And.NAME)
	public static class AndConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.And
	{

		@Parameter
		private long constant;

		@Override
		public O compute(final I input, final O output) {
			output.setReal(constant & (long) input.getRealDouble());
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse cosine of
	 * the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arccos.NAME)
	public static class Arccos<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arccos
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.acos(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * cosine of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arccosh.NAME)
	public static class Arccosh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arccosh
	{

		@Override
		public O compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			double delta = Math.sqrt(xt * xt - 1);
			if (xt <= -1) delta = -delta;
			output.setReal(Math.log(xt + delta));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse cotangent
	 * of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arccot.NAME)
	public static class Arccot<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arccot
	{

		@Override
		public O compute(final I input, final O output) {
			double value = Math.atan(1.0 / input.getRealDouble());
			if (input.getRealDouble() < 0) value += Math.PI;
			output.setReal(value);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * cotangent of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arccoth.NAME)
	public static class Arccoth<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arccoth
	{

		@Override
		public O compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			output.setReal(0.5 * Math.log((xt + 1) / (xt - 1)));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse cosecant of
	 * the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arccsc.NAME)
	public static class Arccsc<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arccsc
	{

		private final static Arcsin<DoubleType, DoubleType> asin =
			new Arcsin<DoubleType, DoubleType>();

		private final DoubleType angle = new DoubleType();

		private final DoubleType tmp = new DoubleType();

		@Override
		public O compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			if ((xt > -1) && (xt < 1)) throw new IllegalArgumentException(
				"arccsc(x) : x out of range");
			else if (xt == -1) output.setReal(-Math.PI / 2);
			else if (xt == 1) output.setReal(Math.PI / 2);
			else {
				tmp.setReal(1 / xt);
				asin.compute(tmp, angle);
				output.setReal(angle.getRealDouble());
			}
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * cosecant of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arccsch.NAME)
	public static class Arccsch<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arccsch
	{

		@Override
		public O compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			final double delta = Math.sqrt(1 + (1 / (xt * xt)));
			output.setReal(Math.log((1 / xt) + delta));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse secant of
	 * the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arcsec.NAME)
	public static class Arcsec<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arcsec
	{

		private final static Arcsin<DoubleType, DoubleType> asin =
			new Arcsin<DoubleType, DoubleType>();

		private final DoubleType angle = new DoubleType();

		private final DoubleType tmp = new DoubleType();

		@Override
		public O compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			if ((xt > -1) && (xt < 1)) throw new IllegalArgumentException(
				"arcsec(x) : x out of range");
			else if (xt == -1) output.setReal(Math.PI);
			else if (xt == 1) output.setReal(0);
			else {
				tmp.setReal(Math.sqrt(xt * xt - 1) / xt);
				asin.compute(tmp, angle);
				double value = angle.getRealDouble();
				if (xt < -1) value += Math.PI;
				output.setReal(value);
			}
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * secant of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arcsech.NAME)
	public static class Arcsech<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arcsech
	{

		@Override
		public O compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			final double numer = 1 + Math.sqrt(1 - xt * xt);
			output.setReal(Math.log(numer / xt));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse sine of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arcsin.NAME)
	public static class Arcsin<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arcsin
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.asin(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * sine of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arcsinh.NAME)
	public static class Arcsinh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arcsinh
	{

		@Override
		public O compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			final double delta = Math.sqrt(xt * xt + 1);
			output.setReal(Math.log(xt + delta));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse tangent of
	 * the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arctan.NAME)
	public static class Arctan<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arctan
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.atan(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * tangent of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Arctanh.NAME)
	public static class Arctanh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Arctanh
	{

		@Override
		public O compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			output.setReal(0.5 * Math.log((1 + xt) / (1 - xt)));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the ceiling of the real
	 * component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Ceil.NAME)
	public static class Ceil<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Ceil
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.ceil(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the real component of
	 * an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Copy.NAME)
	public static class Copy<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Copy
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(input.getRealDouble());
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the cosine of the real
	 * component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Cos.NAME)
	public static class Cos<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Cos
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.cos(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic cosine
	 * of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Cosh.NAME)
	public static class Cosh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Cosh
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.cosh(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the cotangent of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Cot.NAME)
	public static class Cot<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Cot
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(1.0 / Math.tan(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic
	 * cotangent of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Coth.NAME)
	public static class Coth<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Coth
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(1.0 / Math.tanh(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the cosecant of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Csc.NAME)
	public static class Csc<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Csc
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(1.0 / Math.sin(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic cosecant
	 * of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Csch.NAME)
	public static class Csch<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Csch
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(1.0 / Math.sinh(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the cube root of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.CubeRoot.NAME)
	public static class CubeRoot<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.CubeRoot
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.cbrt(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the division of the
	 * real component of an input real number by a constant value.
	 */
	@Plugin(type = Op.class, name = MathOps.Divide.NAME)
	public static class Divide<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Divide
	{

		@Parameter
		private double constant;
		@Parameter
		private double dbzVal;

		@Override
		public O compute(final I input, final O output) {
			if (constant == 0) {
				output.setReal(dbzVal);
			}
			else {
				output.setReal(input.getRealDouble() / constant);
			}
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the exponentiation of
	 * the real component of an input real number. (e raised to a power)
	 */
	@Plugin(type = Op.class, name = MathOps.Exp.NAME)
	public static class Exp<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Exp
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.exp(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to e^x - 1. x is the input
	 * argument to the operation.
	 */
	@Plugin(type = Op.class, name = MathOps.ExpMinusOne.NAME)
	public static class ExpMinusOne<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.ExpMinusOne
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.exp(input.getRealDouble()) - 1);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the floor of the real
	 * component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Floor.NAME)
	public static class Floor<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Floor
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.floor(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the gamma value of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Gamma.NAME)
	public static class GammaConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Gamma
	{

		@Parameter
		private double constant;

		@Override
		public O compute(final I input, final O output) {
			final double inputVal = input.getRealDouble();
			if (inputVal <= 0) output.setReal(0);
			else {
				output.setReal(Math.exp(this.constant * Math.log(inputVal)));
			}
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the inversion of the
	 * real component of an input real number about a range.
	 */
	@Plugin(type = Op.class, name = MathOps.Invert.NAME)
	public static class Invert<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Invert
	{

		@Parameter
		private double specifiedMin;
		@Parameter
		private double specifiedMax;

		@Override
		public O compute(final I input, final O output) {
			output.setReal(specifiedMax - (input.getRealDouble() - specifiedMin));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the natural log of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Log.NAME)
	public static class Log<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Log
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.log(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the 10-based log of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Log10.NAME)
	public static class Log10<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Log10
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.log10(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the base 2 log of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Log2.NAME)
	public static class Log2<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Log2
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.log(input.getRealDouble()) / Math.log(2));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the natural logarithm
	 * of the sum of the argument and 1. This calculation is more accurate than
	 * explicitly calling log(1.0 + x).
	 */
	@Plugin(type = Op.class, name = MathOps.LogOnePlusX.NAME)
	public static class LogOnePlusX<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.LogOnePlusX
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.log1p(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the real component of
	 * an input real number unless it exceeds a maximum value. If it exceeds the
	 * maximum value then it sets the output real component to that maximum value.
	 */
	@Plugin(type = Op.class, name = MathOps.Max.NAME)
	public static class MaxConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Max
	{

		@Parameter
		private double constant;

		@Override
		public O compute(final I input, final O output) {
			final double value = input.getRealDouble();
			if (value < constant) output.setReal(value);
			else output.setReal(constant);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the real component of
	 * an input real number unless it is less then a minimum value. If it is less
	 * than the minimum value then it sets the output real component to that
	 * minimum value.
	 */
	@Plugin(type = Op.class, name = MathOps.Min.NAME)
	public static class MinConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Min
	{

		@Parameter
		private double constant;

		@Override
		public O compute(final I input, final O output) {
			final double value = input.getRealDouble();
			if (value > constant) output.setReal(value);
			else output.setReal(constant);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the multiplication of
	 * the real component of an input real number with a constant value.
	 */
	@Plugin(type = Op.class, name = MathOps.Multiply.NAME)
	public static class Multiply<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Multiply
	{

		@Parameter
		private double constant;

		@Override
		public O compute(final I input, final O output) {
			output.setReal(input.getRealDouble() * constant);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the nearest integral
	 * value of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.NearestInt.NAME)
	public static class NearestInt<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.NearestInt
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.rint(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the negation of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Negate.NAME)
	public static class Negate<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Negate
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(-input.getRealDouble());
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the logical OR of the
	 * real component of an input real number with a constant value.
	 */
	@Plugin(type = Op.class, name = MathOps.Or.NAME)
	public static class OrConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Or
	{

		@Parameter
		private long constant;

		@Override
		public O compute(final I input, final O output) {
			output.setReal(constant | (long) input.getRealDouble());
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the raising of the real
	 * component of an input real number to a constant value.
	 */
	@Plugin(type = Op.class, name = MathOps.Power.NAME)
	public static class PowerConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Power
	{

		@Parameter
		private double constant;

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.pow(input.getRealDouble(), constant));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to a random value using a
	 * gaussian distribution. The input value is considered the standard deviation
	 * of the desired distribution and must be positive. The output value has mean
	 * value 0.
	 */
	@Plugin(type = Op.class, name = MathOps.RandomGaussian.NAME)
	public static class RandomGaussian<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.RandomGaussian
	{

		@Parameter(required = false)
		private long seed = 0xabcdef1234567890L;

		private Random rng;

		public long getSeed() {
			return seed;
		}

		public void setSeed(final long seed) {
			this.seed = seed;
		}

		@Override
		public O compute(final I input, final O output) {
			if (rng == null) rng = new Random(seed);
			output.setReal(rng.nextGaussian() * Math.abs(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to a random value between
	 * 0 and (input real number).
	 */
	@Plugin(type = Op.class, name = MathOps.RandomUniform.NAME)
	public static class RandomUniform<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.RandomUniform
	{

		@Parameter(required = false)
		private long seed = 0xabcdef1234567890L;

		private Random rng;

		public long getSeed() {
			return seed;
		}

		public void setSeed(final long seed) {
			this.seed = seed;
		}

		@Override
		public O compute(final I input, final O output) {
			if (rng == null) rng = new Random(seed);
			final double r = rng.nextDouble();
			output.setReal(r * input.getRealDouble());
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the reciprocal of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Reciprocal.NAME)
	public static class Reciprocal<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Reciprocal
	{

		@Parameter
		private double dbzVal;

		@Override
		public O compute(final I input, final O output) {
			final double inputVal = input.getRealDouble();
			if (inputVal == 0) output.setReal(dbzVal);
			else output.setReal(1.0 / inputVal);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the rounding of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Round.NAME)
	public static class Round<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Round
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.round(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the secant of the real
	 * component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Sec.NAME)
	public static class Sec<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Sec
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(1.0 / Math.cos(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic secant
	 * of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Sech.NAME)
	public static class Sech<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Sech
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(1.0 / Math.cosh(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the signum of the real
	 * component of an input real number. It equals -1 if the input number is less
	 * than 0, it equals 1 if the input number is greater than 0, and it equals 0
	 * if the input number equals 0.
	 */
	@Plugin(type = Op.class, name = MathOps.Signum.NAME)
	public static class Signum<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Signum
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.signum(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the sine of the real
	 * component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Sin.NAME)
	public static class Sin<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Sin
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.sin(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the sinc value of the
	 * real component of an input real number. The sinc function is defined as
	 * sin(x) / x.
	 */
	@Plugin(type = Op.class, name = MathOps.Sinc.NAME)
	public static class Sinc<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Sinc
	{

		@Override
		public O compute(final I input, final O output) {
			final double x = input.getRealDouble();
			double value;
			if (x == 0) value = 1;
			else value = Math.sin(x) / x;
			output.setReal(value);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the sinc (pi version)
	 * of the real component of an input real number. The pi version of sinc is
	 * defined as sin(x*pi) / (x*pi).
	 */
	@Plugin(type = Op.class, name = MathOps.SincPi.NAME)
	public static class SincPi<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.SincPi
	{

		@Override
		public O compute(final I input, final O output) {
			final double x = input.getRealDouble();
			double value;
			if (x == 0) value = 1;
			else value = Math.sin(Math.PI * x) / (Math.PI * x);
			output.setReal(value);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic sine of
	 * the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Sinh.NAME)
	public static class Sinh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Sinh
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.sinh(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the square of the real
	 * component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Sqr.NAME)
	public static class Sqr<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Sqr
	{

		@Override
		public O compute(final I input, final O output) {
			final double value = input.getRealDouble();
			output.setReal(value * value);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the square root of the
	 * real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Sqrt.NAME)
	public static class Sqrt<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Sqrt
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.sqrt(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets an output real number to 0 if the input real number is less than 0.
	 * Otherwise sets the output real number to 1. This implements a step function
	 * similar to Mathematica's unitstep function. It is a Heaviside step function
	 * with h(0) = 1 rather than 0.5.
	 */
	@Plugin(type = Op.class, name = MathOps.Step.NAME)
	public static class Step<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Step
	{

		@Override
		public O compute(final I input, final O output) {
			if (input.getRealDouble() < 0) output.setZero();
			else output.setOne();
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the subtraction from
	 * the real component of an input real number a constant value.
	 */
	@Plugin(type = Op.class, name = MathOps.Subtract.NAME)
	public static class Subtract<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Subtract
	{

		@Parameter
		private double constant;

		@Override
		public O compute(final I input, final O output) {
			output.setReal(input.getRealDouble() - constant);
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the tangent of the real
	 * component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Tan.NAME)
	public static class Tan<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Tan
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.tan(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic tangent
	 * of the real component of an input real number.
	 */
	@Plugin(type = Op.class, name = MathOps.Tanh.NAME)
	public static class Tanh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Tanh
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.tanh(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the size of the ulp of
	 * an input real number. An ulp of a floating point value is the positive
	 * distance between an input floating-point value and the floating point value
	 * next larger in magnitude. Note that for non-NaN x, ulp(-x) == ulp(x).
	 */
	@Plugin(type = Op.class, name = MathOps.Ulp.NAME)
	public static class Ulp<I extends RealType<I>, O extends RealType<O>> extends
		AbstractStrictFunction<I, O> implements MathOps.Ulp
	{

		@Override
		public O compute(final I input, final O output) {
			output.setReal(Math.ulp(input.getRealDouble()));
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to the logical XOR of the
	 * real component of an input real number with a constant value.
	 */
	@Plugin(type = Op.class, name = MathOps.Xor.NAME)
	public static class XorConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Xor
	{

		@Parameter
		private long constant;

		@Override
		public O compute(final I input, final O output) {
			output.setReal(constant ^ (long) input.getRealDouble());
			return output;
		}
	}

	/**
	 * Sets the real component of an output real number to zero.
	 */
	@Plugin(type = Op.class, name = MathOps.Zero.NAME)
	public static class Zero<I extends RealType<I>, O extends RealType<O>>
		extends AbstractStrictFunction<I, O> implements MathOps.Zero
	{

		@Override
		public O compute(final I input, final O output) {
			output.setZero();
			return output;
		}
	}

}
