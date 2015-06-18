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

package net.imagej.ops.arithmetic.real;

import static org.junit.Assert.assertEquals;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

/**
 * Tests {@link RealUniformRandom}.
 *
 * @author Alison Walter
 * @author Curtis Rueden
 */
public class RealUniformRandomTest extends AbstractOpTest {

	@Test
	public void testUniformRandom() {
		assertUniformRandom(23, 14.278690684728433);
		assertUniformRandom(27, 5.940945158572171, 0xfeeddeadbeefbeefL);
		assertUniformRandom(123, 52.3081016051914, 124, 95.52110798318904);
	}

	private void assertUniformRandom(final double i, final double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = ops.math().uniformrandom(in.createVariable(), in);
		assertEquals(o, out.get(), 0);
	}

	private void assertUniformRandom(final double i, final double o,
		final long seed)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out =
			ops.math().uniformrandom(in.createVariable(), in, seed);
		assertEquals(o, out.get(), 0);
	}

	private void assertUniformRandom(final double i, final double o,
		final double i2, final double o2)
	{
		final DoubleType in = new DoubleType(i);
		final DoubleType out = new DoubleType();
		final long seed = 0xcafebabe12345678L;
		@SuppressWarnings("unchecked")
		final RealUniformRandom<DoubleType, DoubleType> op =
			ops.op(RealUniformRandom.class, in.createVariable(), in, seed);
		op.compute(in, out);
		assertEquals(o, out.get(), 0);
		in.set(i2);
		op.compute(in, out);
		assertEquals(o2, out.get(), 0);
	}
}
