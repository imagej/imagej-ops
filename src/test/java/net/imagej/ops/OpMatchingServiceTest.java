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

	/** Tests matching with a mix of BOTH and IN optional parameters. */
	@Test
	public void testMixedOptionalParams() {
		Module m;

		try {
			m = optionalMixedParams("abcdefghijklm");
			fail("Expected IllegalArgumentException for 13 args");
		}
		catch (final IllegalArgumentException exc) {
			// NB: Expected.
		}

		// no defaults
		m = optionalMixedParams("abcdefghijkl");
		assertMixedValues(m,
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l");

		// defaults: bothOpt3
		m = optionalMixedParams("abcdefghijk");
		assertMixedValues(m,
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "bo3");

		// defaults: bothOpt3, bothOpt2
		m = optionalMixedParams("abcdefghij");
		assertMixedValues(m,
			"a", "b", "c", "d", "e", "f", "g", "bo2", "h", "i", "j", "bo3");

		// defaults: bothOpt3, bothOpt2, bothOpt1
		m = optionalMixedParams("abcdefghi");
		assertMixedValues(m,
			"a", "b", "c", "bo1", "d", "e", "f", "bo2", "g", "h", "i", "bo3");

		// defaults: bothOpt3, bothOpt2, bothOpt1, inOpt3
		m = optionalMixedParams("abcdefgh");
		assertMixedValues(m,
			"a", "b", "c", "bo1", "d", "e", "f", "bo2", "g", "io3", "h", "bo3");

		// defaults: bothOpt3, bothOpt2, bothOpt1, inOpt3, inOpt2
		m = optionalMixedParams("abcdefg");
		assertMixedValues(m,
			"a", "b", "c", "bo1", "d", "io2", "e", "bo2", "f", "io3", "g", "bo3");

		// defaults: bothOpt3, bothOpt2, bothOpt1, inOpt3, inOpt2, inOpt1
		m = optionalMixedParams("abcdef");
		assertMixedValues(m,
			"a", "io1", "b", "bo1", "c", "io2", "d", "bo2", "e", "io3", "f", "bo3");

		try {
			m = optionalMixedParams("abcde");
			fail("Expected IllegalArgumentException for 5 args");
		}
		catch (final IllegalArgumentException exc) {
			// NB: Expected.
		}
	}

	// -- Helper methods --

	private Module optionalMixedParams(final String s) {
		final Object[] params = s.split("(?!^)");
		return matcher.findModule(null, OptionalMixedParams.class, params);
	}

	private void assertMixedValues(final Module m,
		final String i1, final String io1, final String b1, final String bo1,
		final String i2, final String io2, final String b2, final String bo2,
		final String i3, final String io3, final String b3, final String bo3)
	{
		assertEquals(i1, m.getInput("in1"));
		assertEquals(io1, m.getInput("inOpt1"));
		assertEquals(b1, m.getInput("both1"));
		assertEquals(b1, m.getOutput("both1"));
		assertEquals(bo1, m.getInput("bothOpt1"));
		assertEquals(bo1, m.getOutput("bothOpt1"));
		assertEquals("o1", m.getOutput("out1"));

		assertEquals(i2, m.getInput("in2"));
		assertEquals(io2, m.getInput("inOpt2"));
		assertEquals(b2, m.getInput("both2"));
		assertEquals(b2, m.getOutput("both2"));
		assertEquals(bo2, m.getInput("bothOpt2"));
		assertEquals(bo2, m.getOutput("bothOpt2"));
		assertEquals("o2", m.getOutput("out2"));

		assertEquals(i3, m.getInput("in3"));
		assertEquals(io3, m.getInput("inOpt3"));
		assertEquals(b3, m.getInput("both3"));
		assertEquals(b3, m.getOutput("both3"));
		assertEquals(bo3, m.getInput("bothOpt3"));
		assertEquals(bo3, m.getOutput("bothOpt3"));
		assertEquals("o3", m.getOutput("out3"));
	}

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

	@Plugin(type = Op.class)
	public static class OptionalMixedParams implements Op {

		@Parameter
		private String in1 = "i1";
		@Parameter(required = false)
		private String inOpt1 = "io1";
		@Parameter(type = ItemIO.BOTH)
		private String both1 = "b1";
		@Parameter(type = ItemIO.BOTH, required = false)
		private String bothOpt1 = "bo1";
		@Parameter(type = ItemIO.OUTPUT)
		private String out1 = "o1";
		@Parameter
		private String in2 = "i2";
		@Parameter(required = false)
		private String inOpt2 = "io2";
		@Parameter(type = ItemIO.BOTH)
		private String both2 = "b2";
		@Parameter(type = ItemIO.BOTH, required = false)
		private String bothOpt2 = "bo2";
		@Parameter(type = ItemIO.OUTPUT)
		private String out2 = "o2";
		@Parameter
		private String in3 = "i3";
		@Parameter(required = false)
		private String inOpt3 = "io3";
		@Parameter(type = ItemIO.BOTH)
		private String both3 = "b3";
		@Parameter(type = ItemIO.BOTH, required = false)
		private String bothOpt3 = "bo3";
		@Parameter(type = ItemIO.OUTPUT)
		private String out3 = "o3";

		@Override
		public void run() {
			// NB: No action needed.
		}

	}

}
