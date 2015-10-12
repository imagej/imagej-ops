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

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.Ops;
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
public final class RealMath {

	private RealMath() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Sets the real component of an output real number to the absolute value of
	 * the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Abs.class, name = Ops.Math.Abs.NAME)
	public static class Abs<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Abs
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.abs(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the addition of the
	 * real component of an input real number with a constant value.
	 */
	@Plugin(type = Ops.Math.Add.class, name = Ops.Math.Add.NAME)
	public static class Add<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Add
	{

		@Parameter
		private double constant;

		@Override
		public void compute(final I input, final O output) {
			output.setReal(input.getRealDouble() + constant);
		}
	}

	/**
	 * Sets the real component of an output real number to the logical AND of the
	 * real component of an input real number with a constant value.
	 */
	@Plugin(type = Ops.Math.And.class, name = Ops.Math.And.NAME)
	public static class AndConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.And
	{

		@Parameter
		private long constant;

		@Override
		public void compute(final I input, final O output) {
			output.setReal(constant & (long) input.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the inverse cosine of
	 * the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.Arccos.class, name = Ops.Math.Arccos.NAME)
	public static class Arccos<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arccos
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
	@Plugin(type = Ops.Math.Arccosh.class, name = Ops.Math.Arccosh.NAME)
	public static class Arccosh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arccosh
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
	@Plugin(type = Ops.Math.Arccot.class, name = Ops.Math.Arccot.NAME)
	public static class Arccot<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arccot
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
	@Plugin(type = Ops.Math.Arccoth.class, name = Ops.Math.Arccoth.NAME)
	public static class Arccoth<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arccoth
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
	@Plugin(type = Ops.Math.Arccsc.class, name = Ops.Math.Arccsc.NAME)
	public static class Arccsc<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arccsc
	{

		private final static Arcsin<DoubleType, DoubleType> asin =
			new Arcsin<DoubleType, DoubleType>();

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
	@Plugin(type = Ops.Math.Arccsch.class, name = Ops.Math.Arccsch.NAME)
	public static class Arccsch<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arccsch
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
	@Plugin(type = Ops.Math.Arcsec.class, name = Ops.Math.Arcsec.NAME)
	public static class Arcsec<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arcsec
	{

		private final static Arcsin<DoubleType, DoubleType> asin =
			new Arcsin<DoubleType, DoubleType>();

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
	@Plugin(type = Ops.Math.Arcsech.class, name = Ops.Math.Arcsech.NAME)
	public static class Arcsech<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arcsech
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
	@Plugin(type = Ops.Math.Arcsin.class, name = Ops.Math.Arcsin.NAME)
	public static class Arcsin<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arcsin
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
	@Plugin(type = Ops.Math.Arcsinh.class, name = Ops.Math.Arcsinh.NAME)
	public static class Arcsinh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arcsinh
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
	@Plugin(type = Ops.Math.Arctan.class, name = Ops.Math.Arctan.NAME)
	public static class Arctan<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arctan
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
	@Plugin(type = Ops.Math.Arctanh.class, name = Ops.Math.Arctanh.NAME)
	public static class Arctanh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Arctanh
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
	@Plugin(type = Ops.Math.Ceil.class, name = Ops.Math.Ceil.NAME)
	public static class Ceil<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Ceil
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
	@Plugin(type = Ops.Math.Cos.class, name = Ops.Math.Cos.NAME)
	public static class Cos<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Cos
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
	@Plugin(type = Ops.Math.Cosh.class, name = Ops.Math.Cosh.NAME)
	public static class Cosh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Cosh
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
	@Plugin(type = Ops.Math.Cot.class, name = Ops.Math.Cot.NAME)
	public static class Cot<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Cot
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
	@Plugin(type = Ops.Math.Coth.class, name = Ops.Math.Coth.NAME)
	public static class Coth<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Coth
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
	@Plugin(type = Ops.Math.Csc.class, name = Ops.Math.Csc.NAME)
	public static class Csc<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Csc
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
	@Plugin(type = Ops.Math.Csch.class, name = Ops.Math.Csch.NAME)
	public static class Csch<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Csch
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
	@Plugin(type = Ops.Math.CubeRoot.class, name = Ops.Math.CubeRoot.NAME)
	public static class CubeRoot<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.CubeRoot
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.cbrt(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the division of the
	 * real component of an input real number by a constant value.
	 */
	@Plugin(type = Ops.Math.Divide.class, name = Ops.Math.Divide.NAME)
	public static class Divide<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Divide
	{

		@Parameter
		private double constant;
		@Parameter
		private double dbzVal;

		@Override
		public void compute(final I input, final O output) {
			if (constant == 0) {
				output.setReal(dbzVal);
			}
			else {
				output.setReal(input.getRealDouble() / constant);
			}
		}
	}

	/**
	 * Sets the real component of an output real number to the exponentiation of
	 * the real component of an input real number. (e raised to a power)
	 */
	@Plugin(type = Ops.Math.Exp.class, name = Ops.Math.Exp.NAME)
	public static class Exp<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Exp
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
	@Plugin(type = Ops.Math.ExpMinusOne.class, name = Ops.Math.ExpMinusOne.NAME)
	public static class ExpMinusOne<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.ExpMinusOne
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
	@Plugin(type = Ops.Math.Floor.class, name = Ops.Math.Floor.NAME)
	public static class Floor<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Floor
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
	@Plugin(type = Ops.Math.Gamma.class, name = Ops.Math.Gamma.NAME)
	public static class GammaConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Gamma
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
	@Plugin(type = Ops.Math.Invert.class, name = Ops.Math.Invert.NAME)
	public static class Invert<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Invert
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
	@Plugin(type = Ops.Math.Log.class, name = Ops.Math.Log.NAME)
	public static class Log<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Log
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
	@Plugin(type = Ops.Math.Log10.class, name = Ops.Math.Log10.NAME)
	public static class Log10<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Log10
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
	@Plugin(type = Ops.Math.Log2.class, name = Ops.Math.Log2.NAME)
	public static class Log2<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Log2
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
	@Plugin(type = Ops.Math.LogOnePlusX.class, name = Ops.Math.LogOnePlusX.NAME)
	public static class LogOnePlusX<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.LogOnePlusX
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
	@Plugin(type = Ops.Math.Max.class, name = Ops.Math.Max.NAME)
	public static class MaxConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Max
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
	@Plugin(type = Ops.Math.Min.class, name = Ops.Math.Min.NAME)
	public static class MinConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Min
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
	 * Sets the real component of an output real number to the multiplication of
	 * the real component of an input real number with a constant value.
	 */
	@Plugin(type = Ops.Math.Multiply.class, name = Ops.Math.Multiply.NAME)
	public static class Multiply<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Multiply
	{

		@Parameter
		private double constant;

		@Override
		public void compute(final I input, final O output) {
			output.setReal(input.getRealDouble() * constant);
		}
	}

	/**
	 * Sets the real component of an output real number to the nearest integral
	 * value of the real component of an input real number.
	 */
	@Plugin(type = Ops.Math.NearestInt.class, name = Ops.Math.NearestInt.NAME)
	public static class NearestInt<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.NearestInt
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
	@Plugin(type = Ops.Math.Negate.class, name = Ops.Math.Negate.NAME)
	public static class Negate<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Negate
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(-input.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the logical OR of the
	 * real component of an input real number with a constant value.
	 */
	@Plugin(type = Ops.Math.Or.class, name = Ops.Math.Or.NAME)
	public static class OrConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Or
	{

		@Parameter
		private long constant;

		@Override
		public void compute(final I input, final O output) {
			output.setReal(constant | (long) input.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the raising of the real
	 * component of an input real number to a constant value.
	 */
	@Plugin(type = Ops.Math.Power.class, name = Ops.Math.Power.NAME)
	public static class PowerConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Power
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
	@Plugin(type = Ops.Math.RandomGaussian.class, name = Ops.Math.RandomGaussian.NAME)
	public static class RandomGaussian<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.RandomGaussian
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
	@Plugin(type = Ops.Math.RandomUniform.class, name = Ops.Math.RandomUniform.NAME)
	public static class RandomUniform<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.RandomUniform
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
	@Plugin(type = Ops.Math.Reciprocal.class, name = Ops.Math.Reciprocal.NAME)
	public static class Reciprocal<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Reciprocal
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
	@Plugin(type = Ops.Math.Round.class, name = Ops.Math.Round.NAME)
	public static class Round<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Round
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.round(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the secant of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Sec.class, name = Ops.Math.Sec.NAME)
	public static class Sec<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Sec
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
	@Plugin(type = Ops.Math.Sech.class, name = Ops.Math.Sech.NAME)
	public static class Sech<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Sech
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
	@Plugin(type = Ops.Math.Signum.class, name = Ops.Math.Signum.NAME)
	public static class Signum<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Signum
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
	@Plugin(type = Ops.Math.Sin.class, name = Ops.Math.Sin.NAME)
	public static class Sin<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Sin
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
	@Plugin(type = Ops.Math.Sinc.class, name = Ops.Math.Sinc.NAME)
	public static class Sinc<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Sinc
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
	@Plugin(type = Ops.Math.SincPi.class, name = Ops.Math.SincPi.NAME)
	public static class SincPi<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.SincPi
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
	@Plugin(type = Ops.Math.Sinh.class, name = Ops.Math.Sinh.NAME)
	public static class Sinh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Sinh
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
	@Plugin(type = Ops.Math.Sqr.class, name = Ops.Math.Sqr.NAME)
	public static class Sqr<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Sqr
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
	@Plugin(type = Ops.Math.Sqrt.class, name = Ops.Math.Sqrt.NAME)
	public static class Sqrt<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Sqrt
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
	@Plugin(type = Ops.Math.Step.class, name = Ops.Math.Step.NAME)
	public static class Step<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Step
	{

		@Override
		public void compute(final I input, final O output) {
			if (input.getRealDouble() < 0) output.setZero();
			else output.setOne();
		}
	}

	/**
	 * Sets the real component of an output real number to the subtraction from
	 * the real component of an input real number a constant value.
	 */
	@Plugin(type = Ops.Math.Subtract.class, name = Ops.Math.Subtract.NAME)
	public static class Subtract<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Subtract
	{

		@Parameter
		private double constant;

		@Override
		public void compute(final I input, final O output) {
			output.setReal(input.getRealDouble() - constant);
		}
	}

	/**
	 * Sets the real component of an output real number to the tangent of the real
	 * component of an input real number.
	 */
	@Plugin(type = Ops.Math.Tan.class, name = Ops.Math.Tan.NAME)
	public static class Tan<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Tan
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
	@Plugin(type = Ops.Math.Tanh.class, name = Ops.Math.Tanh.NAME)
	public static class Tanh<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Tanh
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
	@Plugin(type = Ops.Math.Ulp.class, name = Ops.Math.Ulp.NAME)
	public static class Ulp<I extends RealType<I>, O extends RealType<O>> extends
		AbstractComputerOp<I, O> implements Ops.Math.Ulp
	{

		@Override
		public void compute(final I input, final O output) {
			output.setReal(Math.ulp(input.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the logical XOR of the
	 * real component of an input real number with a constant value.
	 */
	@Plugin(type = Ops.Math.Xor.class, name = Ops.Math.Xor.NAME)
	public static class XorConstant<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Xor
	{

		@Parameter
		private long constant;

		@Override
		public void compute(final I input, final O output) {
			output.setReal(constant ^ (long) input.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to zero.
	 */
	@Plugin(type = Ops.Math.Zero.class, name = Ops.Math.Zero.NAME)
	public static class Zero<I extends RealType<I>, O extends RealType<O>>
		extends AbstractComputerOp<I, O> implements Ops.Math.Zero
	{

		@Override
		public void compute(final I input, final O output) {
			output.setZero();
		}
	}

}
