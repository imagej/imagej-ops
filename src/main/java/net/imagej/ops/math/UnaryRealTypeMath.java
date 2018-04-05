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

package net.imagej.ops.math;

import java.util.Random;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Ops of the {@code math} namespace which operate on {@link RealType}s.
 *
 * @author Barry DeZonia
 * @author Jonathan Hale (University of Konstanz)
 * @author Curtis Rueden
 */
public final class UnaryRealTypeMath {

	private UnaryRealTypeMath() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Sets the real component of an output real number to the absolute value of
	 * the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Abs.class)
	public static class Abs<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Abs
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.abs(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse cosine of
	 * the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arccos.class)
	public static class Arccos<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arccos
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.acos(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * cosine of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arccosh.class)
	public static class Arccosh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arccosh
	{

		@Override
		public void compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			double delta = Math.sqrt(xt * xt - 1);
			if (xt <= -1) delta = -delta;
			output.setReal(Math.log(xt + delta));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse cotangent
	 * of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arccot.class)
	public static class Arccot<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arccot
	{

		@Override
		public void compute(final I input, final O output) {
			double value = Math.atan(1.0 / input.getRealDouble());
			if (input.getRealDouble() < 0) value += Math.PI;
			output.setReal(value);
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * cotangent of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arccoth.class)
	public static class Arccoth<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arccoth
	{

		@Override
		public void compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			output.setReal(0.5 * Math.log((xt + 1) / (xt - 1)));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse cosecant of
	 * the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arccsc.class)
	public static class Arccsc<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arccsc
	{

		private final static Arcsin<DoubleType, DoubleType> asin = new Arcsin<>();

		private final DoubleType angle = new DoubleType();

		private final DoubleType tmp = new DoubleType();

		@Override
		public void compute(final I input, final O output) {
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
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * cosecant of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arccsch.class)
	public static class Arccsch<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arccsch
	{

		@Override
		public void compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			final double delta = Math.sqrt(1 + (1 / (xt * xt)));
			output.setReal(Math.log((1 / xt) + delta));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse secant of
	 * the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arcsec.class)
	public static class Arcsec<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arcsec
	{

		private final static Arcsin<DoubleType, DoubleType> asin = new Arcsin<>();

		private final DoubleType angle = new DoubleType();

		private final DoubleType tmp = new DoubleType();

		@Override
		public void compute(final I input, final O output) {
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
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * secant of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arcsech.class)
	public static class Arcsech<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arcsech
	{

		@Override
		public void compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			final double numer = 1 + Math.sqrt(1 - xt * xt);
			output.setReal(Math.log(numer / xt));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse sine of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arcsin.class)
	public static class Arcsin<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arcsin
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.asin(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * sine of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arcsinh.class)
	public static class Arcsinh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arcsinh
	{

		@Override
		public void compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			final double delta = Math.sqrt(xt * xt + 1);
			output.setReal(Math.log(xt + delta));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse tangent of
	 * the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arctan.class)
	public static class Arctan<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arctan
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.atan(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse hyperbolic
	 * tangent of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arctanh.class)
	public static class Arctanh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Arctanh
	{

		@Override
		public void compute(final I input, final O output) {
			final double xt = input.getRealDouble();
			output.setReal(0.5 * Math.log((1 + xt) / (1 - xt)));
		}
	}

	/**
	 * Sets the real component of an output real number to the ceiling of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Ceil.class)
	public static class Ceil<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Ceil
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.ceil(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the cosine of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Cos.class)
	public static class Cos<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Cos
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.cos(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic cosine
	 * of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Cosh.class)
	public static class Cosh<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Cosh
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.cosh(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the cotangent of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Cot.class)
	public static class Cot<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Cot
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(1.0 / Math.tan(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic
	 * cotangent of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Coth.class)
	public static class Coth<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Coth
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(1.0 / Math.tanh(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the cosecant of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Csc.class)
	public static class Csc<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Csc
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(1.0 / Math.sin(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic cosecant
	 * of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Csch.class)
	public static class Csch<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Csch
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(1.0 / Math.sinh(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the cube root of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.CubeRoot.class)
	public static class CubeRoot<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.CubeRoot
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.cbrt(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the exponentiation of
	 * the real component of an input real number. (e raised to a power)
	 */
	@Plugin(type = Ops.Math.Exp.class)
	public static class Exp<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Exp
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.exp(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to e^x - 1. x is the input
	 * argument to the operation.
	 */
	@Plugin(type = Ops.Math.ExpMinusOne.class)
	public static class ExpMinusOne<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.ExpMinusOne
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.exp(input.getRealDouble()) - 1);
		}
	}

	/**
	 * Sets the real component of an output real number to the floor of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Floor.class)
	public static class Floor<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Floor
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.floor(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the gamma value of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Gamma.class)
	public static class GammaConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Gamma
	{

		@Parameter
		private double constant;

		@Override
		public void compute(final I input, final O output) {
			final double inputVal = input.getRealDouble();
			if (inputVal <= 0) output.setReal(0);
			else {
				output.setReal(Math.exp(this.constant * Math.log(inputVal)));
			}
		}
	}

	/**
	 * Sets the real component of an output real number to the inversion of the
	 * real component of an input real number about a range.
	 */
	@Plugin(type = Ops.Math.Invert.class)
	public static class Invert<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Invert
	{

		@Parameter
		private double specifiedMin;
		@Parameter
		private double specifiedMax;

		@Override
		public void compute(final I input, final O output) {
			output.setReal(specifiedMax - (input.getRealDouble() - specifiedMin));
		}
	}

	/**
	 * Sets the real component of an output real number to the natural log of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Log.class)
	public static class Log<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Log
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.log(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the 10-based log of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Log10.class)
	public static class Log10<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Log10
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.log10(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the base 2 log of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Log2.class)
	public static class Log2<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Log2
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.log(input.getRealDouble()) / Math.log(2));
		}
	}

	/**
	 * Sets the real component of an output real number to the natural logarithm
	 * of the sum of the argument and 1. This calculation is more accurate than
	 * explicitly calling log(1.0 + x).
	 */
	@Plugin(type = Ops.Math.LogOnePlusX.class)
	public static class LogOnePlusX<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.LogOnePlusX
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.log1p(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the real component of
	 * an input real number unless it exceeds a maximum value. If it exceeds the
	 * maximum value then it sets the output real component to that maximum value.
	 */
	@Plugin(type = Ops.Math.Max.class)
	public static class MaxConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Max
	{

		@Parameter
		private double constant;

		@Override
		public void compute(final I input, final O output) {
			final double value = input.getRealDouble();
			if (value < constant) output.setReal(value);
			else output.setReal(constant);
		}
	}

	/**
	 * Sets the real component of an output real number to the real component of
	 * an input real number unless it is less then a minimum value. If it is less
	 * than the minimum value then it sets the output real component to that
	 * minimum value.
	 */
	@Plugin(type = Ops.Math.Min.class)
	public static class MinConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Min
	{

		@Parameter
		private double constant;

		@Override
		public void compute(final I input, final O output) {
			final double value = input.getRealDouble();
			if (value > constant) output.setReal(value);
			else output.setReal(constant);
		}
	}

	/**
	 * Sets the real component of an output real number to the nearest integral
	 * value of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.NearestInt.class)
	public static class NearestInt<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.NearestInt
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.rint(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the negation of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Negate.class)
	public static class Negate<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Negate
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(-input.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the raising of the real
	 * component of an input real number to a constant value.
	 */
	@Plugin(type = Ops.Math.Power.class)
	public static class PowerConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Power
	{

		@Parameter
		private double constant;

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.pow(input.getRealDouble(), constant));
		}
	}

	/**
	 * Sets the real component of an output real number to a random value using a
	 * gaussian distribution. The input value is considered the standard deviation
	 * of the desired distribution and must be positive. The output value has mean
	 * value 0.
	 */
	@Plugin(type = Ops.Math.RandomGaussian.class)
	public static class RandomGaussian<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.RandomGaussian
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
		public void compute(final I input, final O output) {
			if (rng == null) rng = new Random(seed);
			output.setReal(rng.nextGaussian() * Math.abs(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to a random value between
	 * 0 and (input real number).
	 */
	@Plugin(type = Ops.Math.RandomUniform.class)
	public static class RandomUniform<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.RandomUniform
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
		public void compute(final I input, final O output) {
			if (rng == null) rng = new Random(seed);
			final double r = rng.nextDouble();
			output.setReal(r * input.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the reciprocal of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Reciprocal.class)
	public static class Reciprocal<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Reciprocal
	{

		@Parameter
		private double dbzVal;

		@Override
		public void compute(final I input, final O output) {
			final double inputVal = input.getRealDouble();
			if (inputVal == 0) output.setReal(dbzVal);
			else output.setReal(1.0 / inputVal);
		}
	}

	/**
	 * Sets the real component of an output real number to the rounding of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Round.class)
	public static class Round<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Round
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal((double) Math.round(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the secant of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Sec.class)
	public static class Sec<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Sec
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(1.0 / Math.cos(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic secant
	 * of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Sech.class)
	public static class Sech<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Sech
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(1.0 / Math.cosh(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the signum of the real
	 * component of an input real number. It equals -1 if the input number is less
	 * than 0, it equals 1 if the input number is greater than 0, and it equals 0
	 * if the input number equals 0.
	 */
	@Plugin(type = Ops.Math.Signum.class)
	public static class Signum<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Signum
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.signum(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the sine of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Sin.class)
	public static class Sin<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Sin
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.sin(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the sinc value of the
	 * real component of an input real number. The sinc function is defined as
	 * sin(x) / x.
	 */
	@Plugin(type = Ops.Math.Sinc.class)
	public static class Sinc<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Sinc
	{

		@Override
		public void compute(final I input, final O output) {
			final double x = input.getRealDouble();
			double value;
			if (x == 0) value = 1;
			else value = Math.sin(x) / x;
			output.setReal(value);
		}
	}

	/**
	 * Sets the real component of an output real number to the sinc (pi version)
	 * of the real component of an input real number. The pi version of sinc is
	 * defined as sin(x*pi) / (x*pi).
	 */
	@Plugin(type = Ops.Math.SincPi.class)
	public static class SincPi<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.SincPi
	{

		@Override
		public void compute(final I input, final O output) {
			final double x = input.getRealDouble();
			double value;
			if (x == 0) value = 1;
			else value = Math.sin(Math.PI * x) / (Math.PI * x);
			output.setReal(value);
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic sine of
	 * the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Sinh.class)
	public static class Sinh<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Sinh
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.sinh(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the square of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Sqr.class)
	public static class Sqr<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Sqr
	{

		@Override
		public void compute(final I input, final O output) {
			final double value = input.getRealDouble();
			output.setReal(value * value);
		}
	}

	/**
	 * Sets the real component of an output real number to the square root of the
	 * real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Sqrt.class)
	public static class Sqrt<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Sqrt
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.sqrt(input.getRealDouble()));
		}
	}

	/**
	 * Sets an output real number to 0 if the input real number is less than 0.
	 * Otherwise sets the output real number to 1. This implements a step function
	 * similar to Mathematica's unitstep function. It is a Heaviside step function
	 * with h(0) = 1 rather than 0.5.
	 */
	@Plugin(type = Ops.Math.Step.class)
	public static class Step<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Step
	{

		@Override
		public void compute(final I input, final O output) {
			if (input.getRealDouble() < 0) output.setZero();
			else output.setOne();
		}
	}

	/**
	 * Sets the real component of an output real number to the tangent of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Tan.class)
	public static class Tan<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Tan
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.tan(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the hyperbolic tangent
	 * of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Tanh.class)
	public static class Tanh<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Tanh
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.tanh(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the size of the ulp of
	 * an input real number. An ulp of a floating point value is the positive
	 * distance between an input floating-point value and the floating point value
	 * next larger in magnitude. Note that for non-NaN x, ulp(-x) == ulp(x).
	 */
	@Plugin(type = Ops.Math.Ulp.class)
	public static class Ulp<I extends RealType<I>, O extends RealType<O>> extends
		AbstractUnaryComputerOp<I, O> implements Ops.Math.Ulp
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.ulp(input.getRealDouble()));
		}
	}

}
