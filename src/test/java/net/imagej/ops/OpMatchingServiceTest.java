/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;
import org.scijava.ItemIO;
import org.scijava.module.Module;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Tests {@link OpMatchingService}.
 * 
 * @author Curtis Rueden
 */
public class OpMatchingServiceTest extends AbstractOpTest {

	/** Tests {@link OpMatchingService#findModule}. */
	@Test
	public void testFindModule() {
		final DoubleType value = new DoubleType(123.456);

		final Module moduleByName = matcher.findModule("nan", null, value);
		assertSame(value, moduleByName.getInput("arg"));

		assertFalse(Double.isNaN(value.get()));
		moduleByName.run();
		assertTrue(Double.isNaN(value.get()));

		value.set(987.654);
		final Module moduleByType = matcher.findModule(null, NaNOp.class, value);
		assertSame(value, moduleByType.getInput("arg"));

		assertFalse(Double.isNaN(value.get()));
		moduleByType.run();
		assertTrue(Double.isNaN(value.get()));
	}

	/** Tests support for matching when there are optional parameters. */
	@Test
	public void testOptionalParams() {
		Module m;

		try {
			m = matcher.findModule(null, OptionalParams.class, 1, 2, 3, 4, 5, 6, 7);
			fail("Expected IllegalArgumentException for 7 args");
		}
		catch (final IllegalArgumentException exc) {
			// NB: Expected.
		}

		m = matcher.findModule(null, OptionalParams.class, 1, 2, 3, 4, 5, 6);
		assertValues(m, 1, 2, 3, 4, 5, 6, -7);

		m = matcher.findModule(null, OptionalParams.class, 1, 2, 3, 4, 5);
		assertValues(m, 1, 2, 3, 4, -5, 5, -7);

		m = matcher.findModule(null, OptionalParams.class, 1, 2, 3, 4);
		assertValues(m, 1, 2, 3, -4, -5, 4, -7);

		m = matcher.findModule(null, OptionalParams.class, 1, 2, 3);
		assertValues(m, 1, -2, 2, -4, -5, 3, -7);

		try {
			m = matcher.findModule(null, OptionalParams.class, 1, 2);
			fail("Expected IllegalArgumentException for 2 args");
		}
		catch (final IllegalArgumentException exc) {
			// NB: Expected.
		}
	}

	// -- Helper methods --

	private void assertValues(final Module m, final int a, final int b,
		final int c, final int d, final int e, final int f, final int result)
	{
		assertEquals(a, num(m.getInput("a")));
		assertEquals(b, num(m.getInput("b")));
		assertEquals(c, num(m.getInput("c")));
		assertEquals(d, num(m.getInput("d")));
		assertEquals(e, num(m.getInput("e")));
		assertEquals(f, num(m.getInput("f")));
		assertEquals(result, num(m.getOutput("result")));
	}

	private int num(final Object o) {
		return (Integer) o;
	}

	// -- Helper classes --

	/** A test {@link Op}. */
	@Plugin(type = Op.class, name = "nan")
	public static class NaNOp extends AbstractInplaceFunction<DoubleType> {

		@Override
		public DoubleType compute(final DoubleType argument) {
			argument.set(Double.NaN);
			return argument;
		}

	}

	@Plugin(type = Op.class)
	public static class OptionalParams implements Op {

		@Parameter
		private int a = -1;
		@Parameter(required = false)
		private int b = -2;
		@Parameter
		private int c = -3;
		@Parameter(required = false)
		private int d = -4;
		@Parameter(required = false)
		private int e = -5;
		@Parameter
		private int f = -6;
		@Parameter(type = ItemIO.OUTPUT)
		private int result = -7;

		@Override
		public void run() {
			// NB: No action needed.
		}

	}

}
