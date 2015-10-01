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

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link RealMath}.
 *
 * @author Alison Walter
 * @author Curtis Rueden
 */
public class RealMathTest extends AbstractOpTest {

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
		final DoubleType out = ops.math().arccsc(in.createVariable(), in);
		assertEquals(o, out.get(), 1e-15);
	}

	private void assertArcsec(final double i, final double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = ops.math().arcsec(in.createVariable(), in);
		assertEquals(o, out.get(), 1e-15);
	}

	private void assertRandomGaussian(final double i, final double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = ops.math().randomGaussian(in.createVariable(), in);
		assertEquals(o, out.get(), 0);
	}

	private void assertRandomGaussian(final double i, final double o,
		final long seed)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out =
			ops.math().randomGaussian(in.createVariable(), in, seed);
		assertEquals(o, out.get(), 0);
	}

	private void assertRandomGaussian(final double i, final double o,
		final double i2, final double o2)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out = new DoubleType();
		final long seed = 0xcafebabe12345678L;
		@SuppressWarnings("unchecked")
		final RealMath.RandomGaussian<DoubleType, DoubleType> op =
			ops.op(RealMath.RandomGaussian.class, in.createVariable(), in, seed);
		op.compute(in, out);
		assertEquals(o, out.get(), 0);
		in.set(i2);
		op.compute(in, out);
		assertEquals(o2, out.get(), 0);
	}

	private void assertRandomUniform(final double i, final double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = ops.math().randomUniform(in.createVariable(), in);
		assertEquals(o, out.get(), 0);
	}

	private void assertRandomUniform(final double i, final double o,
		final long seed)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out =
			ops.math().randomUniform(in.createVariable(), in, seed);
		assertEquals(o, out.get(), 0);
	}

	private void assertRandomUniform(final double i, final double o,
		final double i2, final double o2)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out = new DoubleType();
		final long seed = 0xcafebabe12345678L;
		@SuppressWarnings("unchecked")
		final RealMath.RandomUniform<DoubleType, DoubleType> op =
			ops.op(RealMath.RandomUniform.class, in.createVariable(), in, seed);
		op.compute(in, out);
		assertEquals(o, out.get(), 0);
		in.set(i2);
		op.compute(in, out);
		assertEquals(o2, out.get(), 0);
	}
}
