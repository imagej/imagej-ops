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

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.OpMethod;
import net.imglib2.type.numeric.RealType;

/**
 * The math namespace contains arithmetic operations.
 * 
 * @author Curtis Rueden
 */
public class MathNamespace extends AbstractNamespace {

	// -- Math namespace ops --

	@OpMethod(op = net.imagej.ops.MathOps.Abs.class)
	public Object abs(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Abs.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerAbs.class)
	public int abs(final int a) {
		final int result =
			(Integer) ops().run(net.imagej.ops.math.PrimitiveMath.IntegerAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongAbs.class)
	public long abs(final long a) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatAbs.class)
	public float abs(final float a) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleAbs.class)
	public double abs(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.arithmetic.real.RealAbs.class)
	public <I extends RealType<I>, O extends RealType<O>> O abs(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.arithmetic.real.RealAbs.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.MathOps.Add.class)
	public Object add(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Add.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.AddNoise.class)
	public Object addnoise(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.AddNoise.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.And.class)
	public Object and(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.And.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arccos.class)
	public Object arccos(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arccos.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arccosh.class)
	public Object arccosh(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arccosh.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arccot.class)
	public Object arccot(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arccot.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arccoth.class)
	public Object arccoth(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arccoth.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arccsc.class)
	public Object arccsc(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arccsc.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arccsch.class)
	public Object arccsch(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arccsch.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arcsec.class)
	public Object arcsec(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arcsec.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arcsech.class)
	public Object arcsech(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arcsech.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arcsin.class)
	public Object arcsin(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arcsin.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arcsinh.class)
	public Object arcsinh(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arcsinh.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arctan.class)
	public Object arctan(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arctan.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Arctanh.class)
	public Object arctanh(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Arctanh.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Ceil.class)
	public Object ceil(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Ceil.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Complement.class)
	public Object complement(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Complement.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Copy.class)
	public Object copy(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Copy.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Cos.class)
	public Object cos(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Cos.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Cosh.class)
	public Object cosh(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Cosh.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Cot.class)
	public Object cot(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Cot.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Coth.class)
	public Object coth(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Coth.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Csc.class)
	public Object csc(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Csc.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Csch.class)
	public Object csch(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Csch.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.CubeRoot.class)
	public Object cuberoot(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.CubeRoot.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Divide.class)
	public Object divide(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Divide.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Exp.class)
	public Object exp(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Exp.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.ExpMinusOne.class)
	public Object expminusone(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.ExpMinusOne.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Floor.class)
	public Object floor(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Floor.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Gamma.class)
	public Object gamma(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Gamma.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.GaussianRandom.class)
	public Object gaussianrandom(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.GaussianRandom.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Invert.class)
	public Object invert(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Invert.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.LeftShift.class)
	public Object leftshift(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.LeftShift.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Log.class)
	public Object log(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Log.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Log2.class)
	public Object log2(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Log2.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Log10.class)
	public Object log10(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Log10.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.LogOnePlusX.class)
	public Object logoneplusx(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.LogOnePlusX.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Max.class)
	public Object max(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Max.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Min.class)
	public Object min(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Min.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Multiply.class)
	public Object multiply(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Multiply.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.NearestInt.class)
	public Object nearestint(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.NearestInt.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Negate.class)
	public Object negate(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Negate.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Or.class)
	public Object or(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Or.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Power.class)
	public Object power(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Power.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Reciprocal.class)
	public Object reciprocal(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Reciprocal.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Remainder.class)
	public Object remainder(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Remainder.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.RightShift.class)
	public Object rightshift(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.RightShift.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Round.class)
	public Object round(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Round.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Sec.class)
	public Object sec(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Sec.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Sech.class)
	public Object sech(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Sech.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Signum.class)
	public Object signum(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Signum.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Sin.class)
	public Object sin(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Sin.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Sinc.class)
	public Object sinc(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Sinc.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.SincPi.class)
	public Object sincpi(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.SincPi.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Sinh.class)
	public Object sinh(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Sinh.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Sqr.class)
	public Object sqr(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Sqr.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Sqrt.class)
	public Object sqrt(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Sqrt.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Step.class)
	public Object step(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Step.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Subtract.class)
	public Object subtract(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Subtract.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Tan.class)
	public Object tan(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Tan.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Tanh.class)
	public Object tanh(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Tanh.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Ulp.class)
	public Object ulp(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Ulp.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.UniformRandom.class)
	public Object uniformrandom(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.UniformRandom.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.UnsignedRightShift.class)
	public Object unsignedrightshift(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.UnsignedRightShift.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Xor.class)
	public Object xor(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Xor.class, args);
	}

	@OpMethod(op = net.imagej.ops.MathOps.Zero.class)
	public Object zero(final Object... args) {
		return ops().run(net.imagej.ops.MathOps.Zero.class, args);
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "math";
	}

}
