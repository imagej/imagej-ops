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

import net.imagej.ops.AbstractNamespaceTest;
import net.imagej.ops.MathOps.Abs;
import net.imagej.ops.MathOps.Add;
import net.imagej.ops.MathOps.AddNoise;
import net.imagej.ops.MathOps.And;
import net.imagej.ops.MathOps.Arccos;
import net.imagej.ops.MathOps.Arccosh;
import net.imagej.ops.MathOps.Arccot;
import net.imagej.ops.MathOps.Arccoth;
import net.imagej.ops.MathOps.Arccsc;
import net.imagej.ops.MathOps.Arccsch;
import net.imagej.ops.MathOps.Arcsec;
import net.imagej.ops.MathOps.Arcsech;
import net.imagej.ops.MathOps.Arcsin;
import net.imagej.ops.MathOps.Arcsinh;
import net.imagej.ops.MathOps.Arctan;
import net.imagej.ops.MathOps.Arctanh;
import net.imagej.ops.MathOps.Ceil;
import net.imagej.ops.MathOps.Complement;
import net.imagej.ops.MathOps.Copy;
import net.imagej.ops.MathOps.Cos;
import net.imagej.ops.MathOps.Cosh;
import net.imagej.ops.MathOps.Cot;
import net.imagej.ops.MathOps.Coth;
import net.imagej.ops.MathOps.Csc;
import net.imagej.ops.MathOps.Csch;
import net.imagej.ops.MathOps.CubeRoot;
import net.imagej.ops.MathOps.Divide;
import net.imagej.ops.MathOps.Exp;
import net.imagej.ops.MathOps.ExpMinusOne;
import net.imagej.ops.MathOps.Floor;
import net.imagej.ops.MathOps.Gamma;
import net.imagej.ops.MathOps.Invert;
import net.imagej.ops.MathOps.LeftShift;
import net.imagej.ops.MathOps.Log;
import net.imagej.ops.MathOps.Log10;
import net.imagej.ops.MathOps.Log2;
import net.imagej.ops.MathOps.LogOnePlusX;
import net.imagej.ops.MathOps.Max;
import net.imagej.ops.MathOps.Min;
import net.imagej.ops.MathOps.Multiply;
import net.imagej.ops.MathOps.NearestInt;
import net.imagej.ops.MathOps.Negate;
import net.imagej.ops.MathOps.Or;
import net.imagej.ops.MathOps.Power;
import net.imagej.ops.MathOps.RandomGaussian;
import net.imagej.ops.MathOps.RandomUniform;
import net.imagej.ops.MathOps.Reciprocal;
import net.imagej.ops.MathOps.Remainder;
import net.imagej.ops.MathOps.RightShift;
import net.imagej.ops.MathOps.Round;
import net.imagej.ops.MathOps.Sec;
import net.imagej.ops.MathOps.Sech;
import net.imagej.ops.MathOps.Signum;
import net.imagej.ops.MathOps.Sin;
import net.imagej.ops.MathOps.Sinc;
import net.imagej.ops.MathOps.SincPi;
import net.imagej.ops.MathOps.Sinh;
import net.imagej.ops.MathOps.Sqr;
import net.imagej.ops.MathOps.Sqrt;
import net.imagej.ops.MathOps.Step;
import net.imagej.ops.MathOps.Subtract;
import net.imagej.ops.MathOps.Tan;
import net.imagej.ops.MathOps.Tanh;
import net.imagej.ops.MathOps.Ulp;
import net.imagej.ops.MathOps.UnsignedRightShift;
import net.imagej.ops.MathOps.Xor;
import net.imagej.ops.MathOps.Zero;

import org.junit.Test;

/**
 * Tests that the ops of the {@code math} namespace have corresponding type-safe
 * Java method signatures declared in the {@link MathNamespace} class.
 *
 * @author Alison Walter
 */
public class MathNamespaceTest extends AbstractNamespaceTest {

	/** Tests for {@link Abs} method convergence. */
	@Test
	public void testAbs() {
		assertComplete(MathNamespace.class, Abs.NAME);
	}

	/** Tests for {@link Add} method convergence. */
	@Test
	public void testAdd() {
		assertComplete(MathNamespace.class, Add.NAME);
	}

	/** Tests for {@link AddNoise} method convergence. */
	@Test
	public void testAddNoise() {
		assertComplete(MathNamespace.class, AddNoise.NAME);
	}

	/** Tests for {@link And} method convergence. */
	@Test
	public void testAnd() {
		assertComplete(MathNamespace.class, And.NAME);
	}

	/** Tests for {@link Arccos} method convergence. */
	@Test
	public void testArccos() {
		assertComplete(MathNamespace.class, Arccos.NAME);
	}

	/** Tests for {@link Arccosh} method convergence. */
	@Test
	public void testArccosh() {
		assertComplete(MathNamespace.class, Arccosh.NAME);
	}

	/** Tests for {@link Arccot} method convergence. */
	@Test
	public void testArccot() {
		assertComplete(MathNamespace.class, Arccot.NAME);
	}

	/** Tests for {@link Arccoth} method convergence. */
	@Test
	public void testArccoth() {
		assertComplete(MathNamespace.class, Arccoth.NAME);
	}

	/** Tests for {@link Arccsc} method convergence. */
	@Test
	public void testArccsc() {
		assertComplete(MathNamespace.class, Arccsc.NAME);
	}

	/** Tests for {@link Arccsch} method convergence. */
	@Test
	public void testArccsch() {
		assertComplete(MathNamespace.class, Arccsch.NAME);
	}

	/** Tests for {@link Arcsec} method convergence. */
	@Test
	public void testArcsec() {
		assertComplete(MathNamespace.class, Arcsec.NAME);
	}

	/** Tests for {@link Arcsech} method convergence. */
	@Test
	public void testArcsech() {
		assertComplete(MathNamespace.class, Arcsech.NAME);
	}

	/** Tests for {@link Arcsin} method convergence. */
	@Test
	public void testArcsin() {
		assertComplete(MathNamespace.class, Arcsin.NAME);
	}

	/** Tests for {@link Arcsinh} method convergence. */
	@Test
	public void testArcsinh() {
		assertComplete(MathNamespace.class, Arcsinh.NAME);
	}

	/** Tests for {@link Arctan} method convergence. */
	@Test
	public void testArctan() {
		assertComplete(MathNamespace.class, Arctan.NAME);
	}

	/** Tests for {@link Arctanh} method convergence. */
	@Test
	public void testArctanh() {
		assertComplete(MathNamespace.class, Arctanh.NAME);
	}

	/** Tests for {@link Ceil} method convergence. */
	@Test
	public void testCeil() {
		assertComplete(MathNamespace.class, Ceil.NAME);
	}

	/** Tests for {@link Complement} method convergence. */
	@Test
	public void testComplement() {
		assertComplete(MathNamespace.class, Complement.NAME);
	}

	/** Tests for {@link Copy} method convergence. */
	@Test
	public void testCopy() {
		assertComplete(MathNamespace.class, Copy.NAME);
	}

	/** Tests for {@link Cos} method convergence. */
	@Test
	public void testCos() {
		assertComplete(MathNamespace.class, Cos.NAME);
	}

	/** Tests for {@link Cosh} method convergence. */
	@Test
	public void testCosh() {
		assertComplete(MathNamespace.class, Cosh.NAME);
	}

	/** Tests for {@link Cot} method convergence. */
	@Test
	public void testCot() {
		assertComplete(MathNamespace.class, Cot.NAME);
	}

	/** Tests for {@link Coth} method convergence. */
	@Test
	public void testCoth() {
		assertComplete(MathNamespace.class, Coth.NAME);
	}

	/** Tests for {@link Csc} method convergence. */
	@Test
	public void testCsc() {
		assertComplete(MathNamespace.class, Csc.NAME);
	}

	/** Tests for {@link Csch} method convergence. */
	@Test
	public void testCsch() {
		assertComplete(MathNamespace.class, Csch.NAME);
	}

	/** Tests for {@link CubeRoot} method convergence. */
	@Test
	public void testCubeRoot() {
		assertComplete(MathNamespace.class, CubeRoot.NAME);
	}

	/** Tests for {@link Divide} method convergence. */
	@Test
	public void testDivide() {
		assertComplete(MathNamespace.class, Divide.NAME);
	}

	/** Tests for {@link Exp} method convergence. */
	@Test
	public void testExp() {
		assertComplete(MathNamespace.class, Exp.NAME);
	}

	/** Tests for {@link ExpMinusOne} method convergence. */
	@Test
	public void testExpMinusOne() {
		assertComplete(MathNamespace.class, ExpMinusOne.NAME);
	}

	/** Tests for {@link Floor} method convergence. */
	@Test
	public void testFloor() {
		assertComplete(MathNamespace.class, Floor.NAME);
	}

	/** Tests for {@link Gamma} method convergence. */
	@Test
	public void testGamma() {
		assertComplete(MathNamespace.class, Gamma.NAME);
	}

	/** Tests for {@link Invert} method convergence. */
	@Test
	public void testInvert() {
		assertComplete(MathNamespace.class, Invert.NAME);
	}

	/** Tests for {@link LeftShift} method convergence. */
	@Test
	public void testLeftShift() {
		assertComplete(MathNamespace.class, LeftShift.NAME);
	}

	/** Tests for {@link Log} method convergence. */
	@Test
	public void testLog() {
		assertComplete(MathNamespace.class, Log.NAME);
	}

	/** Tests for {@link Log2} method convergence. */
	@Test
	public void testLog2() {
		assertComplete(MathNamespace.class, Log2.NAME);
	}

	/** Tests for {@link Log10} method convergence. */
	@Test
	public void testLog10() {
		assertComplete(MathNamespace.class, Log10.NAME);
	}

	/** Tests for {@link LogOnePlusX} method convergence. */
	@Test
	public void testLogOnePlusX() {
		assertComplete(MathNamespace.class, LogOnePlusX.NAME);
	}

	/** Tests for {@link Max} method convergence. */
	@Test
	public void testMax() {
		assertComplete(MathNamespace.class, Max.NAME);
	}

	/** Tests for {@link Min} method convergence. */
	@Test
	public void testMin() {
		assertComplete(MathNamespace.class, Min.NAME);
	}

	/** Tests for {@link Multiply} method convergence. */
	@Test
	public void testMultiply() {
		assertComplete(MathNamespace.class, Multiply.NAME);
	}

	/** Tests for {@link NearestInt} method convergence. */
	@Test
	public void testNearestInt() {
		assertComplete(MathNamespace.class, NearestInt.NAME);
	}

	/** Tests for {@link Negate} method convergence. */
	@Test
	public void testNegate() {
		assertComplete(MathNamespace.class, Negate.NAME);
	}

	/** Tests for {@link Or} method convergence. */
	@Test
	public void testOr() {
		assertComplete(MathNamespace.class, Or.NAME);
	}

	/** Tests for {@link Power} method convergence. */
	@Test
	public void testPower() {
		assertComplete(MathNamespace.class, Power.NAME);
	}

	/** Tests for {@link RandomGaussian} method convergence. */
	@Test
	public void testRandomGaussian() {
		assertComplete(MathNamespace.class, RandomGaussian.NAME);
	}

	/** Tests for {@link RandomUniform} method convergence. */
	@Test
	public void testRandomUniform() {
		assertComplete(MathNamespace.class, RandomUniform.NAME);
	}

	/** Tests for {@link Reciprocal} method convergence. */
	@Test
	public void testReciprocal() {
		assertComplete(MathNamespace.class, Reciprocal.NAME);
	}

	/** Tests for {@link Remainder} method convergence. */
	@Test
	public void testRemainder() {
		assertComplete(MathNamespace.class, Remainder.NAME);
	}

	/** Tests for {@link RightShift} method convergence. */
	@Test
	public void testRightShift() {
		assertComplete(MathNamespace.class, RightShift.NAME);
	}

	/** Tests for {@link Round} method convergence. */
	@Test
	public void testRound() {
		assertComplete(MathNamespace.class, Round.NAME);
	}

	/** Tests for {@link Sec} method convergence. */
	@Test
	public void testSec() {
		assertComplete(MathNamespace.class, Sec.NAME);
	}

	/** Tests for {@link Sech} method convergence. */
	@Test
	public void testSech() {
		assertComplete(MathNamespace.class, Sech.NAME);
	}

	/** Tests for {@link Signum} method convergence. */
	@Test
	public void testSignum() {
		assertComplete(MathNamespace.class, Signum.NAME);
	}

	/** Tests for {@link Sin} method convergence. */
	@Test
	public void testSin() {
		assertComplete(MathNamespace.class, Sin.NAME);
	}

	/** Tests for {@link Sinc} method convergence. */
	@Test
	public void testSinc() {
		assertComplete(MathNamespace.class, Sinc.NAME);
	}

	/** Tests for {@link SincPi} method convergence. */
	@Test
	public void testSincPi() {
		assertComplete(MathNamespace.class, SincPi.NAME);
	}

	/** Tests for {@link Sinh} method convergence. */
	@Test
	public void testSinh() {
		assertComplete(MathNamespace.class, Sinh.NAME);
	}

	/** Tests for {@link Sqr} method convergence. */
	@Test
	public void testSqr() {
		assertComplete(MathNamespace.class, Sqr.NAME);
	}

	/** Tests for {@link Sqrt} method convergence. */
	@Test
	public void testSqrt() {
		assertComplete(MathNamespace.class, Sqrt.NAME);
	}

	/** Tests for {@link Step} method convergence. */
	@Test
	public void testStep() {
		assertComplete(MathNamespace.class, Step.NAME);
	}

	/** Tests for {@link Subtract} method convergence. */
	@Test
	public void testSubtract() {
		assertComplete(MathNamespace.class, Subtract.NAME);
	}

	/** Tests for {@link Tan} method convergence. */
	@Test
	public void testTan() {
		assertComplete(MathNamespace.class, Tan.NAME);
	}

	/** Tests for {@link Tanh} method convergence. */
	@Test
	public void testTanh() {
		assertComplete(MathNamespace.class, Tanh.NAME);
	}

	/** Tests for {@link Ulp} method convergence. */
	@Test
	public void testUlp() {
		assertComplete(MathNamespace.class, Ulp.NAME);
	}

	/** Tests for {@link UnsignedRightShift} method convergence. */
	@Test
	public void testUnsignedRightShift() {
		assertComplete(MathNamespace.class, UnsignedRightShift.NAME);
	}

	/** Tests for {@link Xor} method convergence. */
	@Test
	public void testXor() {
		assertComplete(MathNamespace.class, Xor.NAME);
	}

	/** Tests for {@link Zero} method convergence. */
	@Test
	public void testZero() {
		assertComplete(MathNamespace.class, Zero.NAME);
	}
}
