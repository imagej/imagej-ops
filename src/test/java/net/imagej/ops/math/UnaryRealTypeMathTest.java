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

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.math.UnaryRealTypeMath.Abs;
import net.imagej.ops.math.UnaryRealTypeMath.Arccos;
import net.imagej.ops.math.UnaryRealTypeMath.Arccosh;
import net.imagej.ops.math.UnaryRealTypeMath.Arccot;
import net.imagej.ops.math.UnaryRealTypeMath.Arccoth;
import net.imagej.ops.math.UnaryRealTypeMath.Arccsc;
import net.imagej.ops.math.UnaryRealTypeMath.Arccsch;
import net.imagej.ops.math.UnaryRealTypeMath.Arcsec;
import net.imagej.ops.math.UnaryRealTypeMath.Arcsech;
import net.imagej.ops.math.UnaryRealTypeMath.Arcsin;
import net.imagej.ops.math.UnaryRealTypeMath.Arcsinh;
import net.imagej.ops.math.UnaryRealTypeMath.Arctan;
import net.imagej.ops.math.UnaryRealTypeMath.Arctanh;
import net.imagej.ops.math.UnaryRealTypeMath.Ceil;
import net.imagej.ops.math.UnaryRealTypeMath.Cos;
import net.imagej.ops.math.UnaryRealTypeMath.Cosh;
import net.imagej.ops.math.UnaryRealTypeMath.Cot;
import net.imagej.ops.math.UnaryRealTypeMath.Coth;
import net.imagej.ops.math.UnaryRealTypeMath.Csc;
import net.imagej.ops.math.UnaryRealTypeMath.Csch;
import net.imagej.ops.math.UnaryRealTypeMath.CubeRoot;
import net.imagej.ops.math.UnaryRealTypeMath.Exp;
import net.imagej.ops.math.UnaryRealTypeMath.ExpMinusOne;
import net.imagej.ops.math.UnaryRealTypeMath.Floor;
import net.imagej.ops.math.UnaryRealTypeMath.Invert;
import net.imagej.ops.math.UnaryRealTypeMath.Log;
import net.imagej.ops.math.UnaryRealTypeMath.Log10;
import net.imagej.ops.math.UnaryRealTypeMath.Log2;
import net.imagej.ops.math.UnaryRealTypeMath.LogOnePlusX;
import net.imagej.ops.math.UnaryRealTypeMath.MaxConstant;
import net.imagej.ops.math.UnaryRealTypeMath.MinConstant;
import net.imagej.ops.math.UnaryRealTypeMath.NearestInt;
import net.imagej.ops.math.UnaryRealTypeMath.Negate;
import net.imagej.ops.math.UnaryRealTypeMath.PowerConstant;
import net.imagej.ops.math.UnaryRealTypeMath.RandomGaussian;
import net.imagej.ops.math.UnaryRealTypeMath.RandomUniform;
import net.imagej.ops.math.UnaryRealTypeMath.Reciprocal;
import net.imagej.ops.math.UnaryRealTypeMath.Round;
import net.imagej.ops.math.UnaryRealTypeMath.Sec;
import net.imagej.ops.math.UnaryRealTypeMath.Sech;
import net.imagej.ops.math.UnaryRealTypeMath.Signum;
import net.imagej.ops.math.UnaryRealTypeMath.Sin;
import net.imagej.ops.math.UnaryRealTypeMath.Sinc;
import net.imagej.ops.math.UnaryRealTypeMath.SincPi;
import net.imagej.ops.math.UnaryRealTypeMath.Sinh;
import net.imagej.ops.math.UnaryRealTypeMath.Sqr;
import net.imagej.ops.math.UnaryRealTypeMath.Sqrt;
import net.imagej.ops.math.UnaryRealTypeMath.Step;
import net.imagej.ops.math.UnaryRealTypeMath.Tan;
import net.imagej.ops.math.UnaryRealTypeMath.Tanh;
import net.imagej.ops.math.UnaryRealTypeMath.Ulp;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link UnaryRealTypeMath}.
 *
 * @author Leon Yang
 * @author Alison Walter
 * @author Curtis Rueden
 */
public class UnaryRealTypeMathTest extends AbstractOpTest {

	// NB: long number LARGE_NUM is rounded to double 9007199254740992.0.
	final static private long LARGE_NUM = 9007199254740993L;

	@Test
	public void testAbs() {
		final LongType in = new LongType(-LARGE_NUM);
		final LongType out = (LongType) ops.run(Abs.class, in.createVariable(), in);
		assertEquals(out.get(), LARGE_NUM - 1);
	}

	@Test
	public void testArccos() {
		final FloatType in = new FloatType(0.5f);
		final DoubleType out = new DoubleType();
		ops.run(Arccos.class, out, in);
		assertEquals(out.get(), Math.acos(0.5), 0.0);
	}

	@Test
	public void testArccosh() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arccosh.class, out, in);
		final double delta = Math.sqrt(1234567890.0 * 1234567890.0 - 1);
		assertEquals(out.get(), Math.log(1234567890 + delta), 0.0);
	}

	@Test
	public void testArccot() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arccot.class, out, in);
		assertEquals(out.get(), Math.atan(1.0 / 1234567890), 0.0);
	}

	@Test
	public void testArccoth() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arccoth.class, out, in);
		final double result = 0.5 * Math.log(1234567891.0 / 1234567889.0);
		assertEquals(out.get(), result, 0.0);
	}

	@Test
	public void testArccsch() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arccsch.class, out, in);
		final double delta = Math.sqrt(1 + 1 / (1234567890.0 * 1234567890.0));
		assertEquals(out.get(), Math.log(1 / 1234567890.0 + delta), 0.0);
	}

	@Test
	public void testArcsech() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arcsech.class, out, in);
		final double numer = 1 + Math.sqrt(1 - 1234567890.0 * 1234567890.0);
		assertEquals(out.get(), Math.log(numer / 1234567890.0), 0.0);
	}

	@Test
	public void testArcsin() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arcsin.class, out, in);
		assertEquals(out.get(), Math.asin(1234567890), 0.0);
	}

	@Test
	public void testArcsinh() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arcsinh.class, out, in);
		final double delta = Math.sqrt(1234567890.0 * 1234567890.0 + 1);
		assertEquals(out.get(), Math.log(1234567890 + delta), 0.0);
	}

	@Test
	public void testArctan() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arctan.class, out, in);
		assertEquals(out.get(), Math.atan(1234567890), 0.0);
	}

	@Test
	public void testArctanh() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Arctanh.class, out, in);
		assertEquals(out.get(), 0.5 * Math.log(1234567891.0 / -1234567889.0), 0.0);
	}

	@Test
	public void testCeil() {
		final LongType in = new LongType(LARGE_NUM);
		final LongType out = (LongType) ops.run(Ceil.class, in.createVariable(),
			in);
		assertEquals(out.get(), LARGE_NUM - 1);
	}

	@Test
	public void testCos() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Cos.class, out, in);
		assertEquals(out.get(), Math.cos(1234567890), 0.0);
	}

	@Test
	public void testCosh() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Cosh.class, out, in);
		assertEquals(out.get(), Math.cosh(1234567890), 0.0);
	}

	@Test
	public void testCot() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Cot.class, out, in);
		assertEquals(out.get(), 1 / Math.tan(1234567890), 0.0);
	}

	@Test
	public void testCoth() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Coth.class, out, in);
		assertEquals(out.get(), 1 / Math.tanh(1234567890), 0.0);
	}

	@Test
	public void testCsc() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Csc.class, out, in);
		assertEquals(out.get(), 1 / Math.sin(1234567890), 0.0);
	}

	@Test
	public void testCsch() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Csch.class, out, in);
		assertEquals(out.get(), 1 / Math.sinh(1234567890), 0.0);
	}

	@Test
	public void testCubeRoot() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(CubeRoot.class, out, in);
		assertEquals(out.get(), Math.cbrt(1234567890), 0.0);
	}

	@Test
	public void testExp() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Exp.class, out, in);
		assertEquals(out.get(), Math.exp(1234567890), 0.0);
	}

	@Test
	public void testExpMinusOne() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(ExpMinusOne.class, out, in);
		assertEquals(out.get(), Math.exp(1234567890) - 1, 0.0);
	}

	@Test
	public void testFloor() {
		final LongType in = new LongType(LARGE_NUM);
		final LongType out = (LongType) ops.run(Floor.class, in.createVariable(),
			in);
		assertEquals(out.get(), LARGE_NUM - 1);
	}

	@Test
	public void testInvert() {
		final LongType in = new LongType(LARGE_NUM);
		final LongType out = (LongType) ops.run(Invert.class, in.createVariable(),
			in, 9007199254740992.0, 9007199254740994.0);
		assertEquals(out.get(), LARGE_NUM + 1);
	}

	@Test
	public void testLog() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Log.class, out, in);
		assertEquals(out.get(), Math.log(1234567890), 0.0);
	}

	@Test
	public void testLog10() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Log10.class, out, in);
		assertEquals(out.get(), Math.log10(1234567890), 0.0);
	}

	@Test
	public void testLog2() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Log2.class, out, in);
		assertEquals(out.get(), Math.log(1234567890) / Math.log(2), 0.0);
	}

	@Test
	public void testLogOnePlusX() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(LogOnePlusX.class, out, in);
		assertEquals(out.get(), Math.log1p(1234567890), 0.0);
	}

	@Test
	public void testMax() {
		final LongType in = new LongType(LARGE_NUM);
		final LongType out = (LongType) ops.run(MaxConstant.class, in
			.createVariable(), in, LARGE_NUM + 1.0);
		assertEquals(out.get(), LARGE_NUM - 1);
	}

	@Test
	public void testMin() {
		final LongType in = new LongType(LARGE_NUM);
		final LongType out = (LongType) ops.run(MinConstant.class, in
			.createVariable(), in, LARGE_NUM - 1.0);
		assertEquals(out.get(), LARGE_NUM - 1);
	}

	@Test
	public void testNearestInt() {
		final LongType in = new LongType(LARGE_NUM);
		final LongType out = (LongType) ops.run(NearestInt.class, in
			.createVariable(), in);
		assertEquals(out.get(), LARGE_NUM - 1);
	}

	@Test
	public void testNegate() {
		final LongType in = new LongType(-LARGE_NUM);
		final LongType out = (LongType) ops.run(Negate.class, in.createVariable(),
			in);
		assertEquals(out.get(), LARGE_NUM - 1);
	}

	@Test
	public void testPower() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(PowerConstant.class, out, in, 1.5);
		assertEquals(out.get(), Math.pow(1234567890, 1.5), 0.0);
	}

	@Test
	public void testReciprocal() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Reciprocal.class, out, in, 0.0);
		assertEquals(out.get(), 1.0 / 1234567890, 0.0);
	}

	@Test
	public void testRound() {
		final LongType in = new LongType(LARGE_NUM);
		final LongType out = (LongType) ops.run(Round.class, in.createVariable(), in);
		assertEquals(out.get(), LARGE_NUM - 1);
	}

	@Test
	public void testSec() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Sec.class, out, in);
		assertEquals(out.get(), 1 / Math.cos(1234567890), 0.0);
	}

	@Test
	public void testSech() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Sech.class, out, in);
		assertEquals(out.get(), 1 / Math.cosh(1234567890), 0.0);
	}

	@Test
	public void testSignum() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Signum.class, out, in);
		assertEquals(out.get(), 1.0, 0.0);
	}

	@Test
	public void testSin() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Sin.class, out, in);
		assertEquals(out.get(), Math.sin(1234567890), 0.0);
	}

	@Test
	public void testSinc() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Sinc.class, out, in);
		assertEquals(out.get(), Math.sin(1234567890) / 1234567890, 0.0);
	}

	@Test
	public void testSincPi() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(SincPi.class, out, in);
		final double PI = Math.PI;
		assertEquals(out.get(), Math.sin(PI * 1234567890) / (PI * 1234567890), 0.0);
	}

	@Test
	public void testSinh() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Sinh.class, out, in);
		assertEquals(out.get(), Math.sinh(1234567890), 0.0);
	}

	@Test
	public void testSqr() {
		final LongType in = new LongType(94906267L);
		final LongType out = (LongType) ops.run(Sqr.class, in.createVariable(), in);
		// NB: for any odd number greater than LARGE_NUM - 1, its double
		// representation is not exact.
		assertEquals(out.get(), 94906267L * 94906267L - 1);
	}

	@Test
	public void testSqrt() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Sqrt.class, out, in);
		assertEquals(out.get(), Math.sqrt(1234567890), 0.0);
	}

	@Test
	public void testStep() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Step.class, out, in);
		assertEquals(out.get(), 1.0, 0.0);
	}

	@Test
	public void testTan() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Tan.class, out, in);
		assertEquals(out.get(), Math.tan(1234567890), 0.0);
	}

	@Test
	public void testTanh() {
		final LongType in = new LongType(1234567890);
		final DoubleType out = new DoubleType();
		ops.run(Tanh.class, out, in);
		assertEquals(out.get(), Math.tanh(1234567890), 0.0);
	}

	@Test
	public void testUlp() {
		final LongType in = new LongType(LARGE_NUM);
		final DoubleType out = new DoubleType();
		ops.run(Ulp.class, out, in);
		assertEquals(out.get(), 2.0, 0.0);
	}

	// -- complex tests --

	@Test
	public void testArccsc() {
		assertArccsc(-1, -Math.PI / 2);
		assertArccsc(1, Math.PI / 2);
		assertArccsc(2, Math.PI / 6);
		assertArccsc(-2, -Math.PI / 6);
		assertArccsc((2 * Math.sqrt(3)) / 3, Math.PI / 3);
		assertArccsc(-(2 * Math.sqrt(3)) / 3, -Math.PI / 3);
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testArccscIllegalArgument() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("arccsc(x) : x out of range");
		assertArccsc(0, 0);
	}

	@Test
	public void testArcsec() {
		assertArcsec(-1, Math.PI);
		assertArcsec(1, 0);
		assertArcsec(Math.sqrt(2), Math.PI / 4);
		assertArcsec(-Math.sqrt(2), (3 * Math.PI) / 4);
		assertArcsec(2, Math.PI / 3);
		assertArcsec(-2, (2 * Math.PI) / 3);
	}

	@Test
	public void testArcsecIllegalArgument() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("arcsec(x) : x out of range");
		assertArcsec(0, 0);
	}

	@Test
	public void testRandomGaussian() {
		assertRandomGaussian(23, 16.53373419964066);
		assertRandomGaussian(27, -15.542815799078497, 0xfeeddeadbeefbeefL);
		assertRandomGaussian(123, -49.838353142718006, 124, 181.75101003563117);
	}

	@Test
	public void testRandomUniform() {
		assertRandomUniform(23, 14.278690684728433);
		assertRandomUniform(27, 5.940945158572171, 0xfeeddeadbeefbeefL);
		assertRandomUniform(123, 52.3081016051914, 124, 95.52110798318904);
	}

	// -- Helper methods --

	private void assertArccsc(final double i, final double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = (DoubleType) ops.run(Arccsc.class, in
			.createVariable(), in);
		assertEquals(o, out.get(), 1e-15);
	}

	private void assertArcsec(final double i, final double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = (DoubleType) ops.run(Arcsec.class, in
			.createVariable(), in);
		assertEquals(o, out.get(), 1e-15);
	}

	private void assertRandomGaussian(final double i, final double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = (DoubleType) ops.run(RandomGaussian.class, in
			.createVariable(), in);
		assertEquals(o, out.get(), 0);
	}

	private void assertRandomGaussian(final double i, final double o,
		final long seed)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out = (DoubleType) ops.run(RandomGaussian.class, in
			.createVariable(), in, seed);
		assertEquals(o, out.get(), 0);
	}

	private void assertRandomGaussian(final double i, final double o,
		final double i2, final double o2)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out = new DoubleType();
		final long seed = 0xcafebabe12345678L;
		@SuppressWarnings("unchecked")
		final UnaryRealTypeMath.RandomGaussian<DoubleType, DoubleType> op = ops.op(
			UnaryRealTypeMath.RandomGaussian.class, in.createVariable(), in, seed);
		op.compute(in, out);
		assertEquals(o, out.get(), 0);
		in.set(i2);
		op.compute(in, out);
		assertEquals(o2, out.get(), 0);
	}

	private void assertRandomUniform(final double i, final double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = (DoubleType) ops.run(RandomUniform.class, in
			.createVariable(), in);
		assertEquals(o, out.get(), 0);
	}

	private void assertRandomUniform(final double i, final double o,
		final long seed)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out = (DoubleType) ops.run(RandomUniform.class, in
			.createVariable(), in, seed);
		assertEquals(o, out.get(), 0);
	}

	private void assertRandomUniform(final double i, final double o,
		final double i2, final double o2)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out = new DoubleType();
		final long seed = 0xcafebabe12345678L;
		@SuppressWarnings("unchecked")
		final UnaryRealTypeMath.RandomUniform<DoubleType, DoubleType> op = ops.op(
			UnaryRealTypeMath.RandomUniform.class, in.createVariable(), in, seed);
		op.compute(in, out);
		assertEquals(o, out.get(), 0);
		in.set(i2);
		op.compute(in, out);
		assertEquals(o2, out.get(), 0);
	}
}
