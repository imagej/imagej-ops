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

package net.imagej.ops.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.special.SpecialOp;
import net.imagej.ops.special.SpecialOpMatchingTest.Apple;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.AbstractNullaryFunctionOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;

import org.junit.Test;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Tests {@link HelpCandidates}.
 *
 * @author Curtis Rueden
 */
public class HelpCandidatesTest extends AbstractOpTest {

	@Test
	public void testAll() {
		final String actual = (String) ops.run(HelpCandidates.class);
		assertTrue(actual.startsWith("Available operations:\n"));
		assertTrue(actual.length() > 50000); // lots of ops!
	}

	@Test
	public void testName() {
		final String expectedApple = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyApple()\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyApple()";
		final String apple = (String) ops.run(HelpCandidates.class, "test.apple");
		assertEquals(expectedApple, apple);

		final String expectedOrange = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyOrange()\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyOrange()";
		final String orange = (String) ops.run(HelpCandidates.class, "test.orange");
		assertEquals(expectedOrange, orange);
	}

	@Test
	public void testType() {
		final String expectedYummy = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyApple()\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyOrange()";
		final String actualYummy = (String) ops.run(HelpCandidates.class, null,
			Yummy.class);
		assertEquals(expectedYummy, actualYummy);

		final String expectedYucky = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyOrange()\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyApple()";
		final String yucky = (String) ops.run(HelpCandidates.class, null,
			Yucky.class);
		assertEquals(expectedYucky, yucky);
	}

	@Test
	public void testNameAndType() {
		final String expectedYummyApple = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyApple()"; //
		final String actualYummyApple = (String) ops.run(HelpCandidates.class,
			"test.apple", Yummy.class);
		assertEquals(expectedYummyApple, actualYummyApple);

		final String expectedYuckyApple = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyApple()"; //
		final String actualYuckyApple = (String) ops.run(HelpCandidates.class,
			"test.apple", Yucky.class);
		assertEquals(expectedYuckyApple, actualYuckyApple);

		final String expectedYummyOrange = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyOrange()"; //
		final String actualYummyOrange = (String) ops.run(HelpCandidates.class,
			"test.orange", Yummy.class);
		assertEquals(expectedYummyOrange, actualYummyOrange);

		final String expectedYuckyOrange = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyOrange()"; //
		final String actualYuckyOrange = (String) ops.run(HelpCandidates.class,
			"test.orange", Yucky.class);
		assertEquals(expectedYuckyOrange, actualYuckyOrange);

		final String actualEmpty = (String) ops.run(HelpCandidates.class,
			"test.apple", Ops.Help.class);
		assertEquals("No such operation.", actualEmpty);
	}

	@Test
	public void testSpecial() {
		final String expectedF0 = "" + //
			"Available operations:\n" + //
			"\t(Apple out) =\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$AppleF0()";
		final String actualF0 = (String) ops.run(HelpCandidates.class,
			"test.special", null, 0, SpecialOp.Flavor.FUNCTION);
		assertEquals(expectedF0, actualF0);

		final String expectedF1 = "" + //
			"Available operations:\n" + //
			"\t(Apple out) =\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$AppleF1(\n" + //
			"\t\tApple in)";
		final String actualF1 = (String) ops.run(HelpCandidates.class,
			"test.special", null, 1, SpecialOp.Flavor.FUNCTION);
		assertEquals(expectedF1, actualF1);

		final String expectedF2 = "Available operations:\n" + "\t(Apple out) =\n" +
			"\tnet.imagej.ops.help.HelpCandidatesTest$AppleF2(\n" +
			"\t\tApple in1,\n" + "\t\tApple in2)";
		final String actualF2 = (String) ops.run(HelpCandidates.class,
			"test.special", null, 2, SpecialOp.Flavor.FUNCTION);
		assertEquals(expectedF2, actualF2);

		final String expectedC0 = "No such operation.";
		final String actualC0 = (String) ops.run(HelpCandidates.class,
			"test.special", null, 0, SpecialOp.Flavor.COMPUTER);
		assertEquals(expectedC0, actualC0);

		final String expectedC1 = "" + //
			"Available operations:\n" + //
			"\t(Apple out) =\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$AppleC1(\n" + //
			"\t\tApple out,\n" + //
			"\t\tApple in)";
		final String actualC1 = (String) ops.run(HelpCandidates.class,
			"test.special", null, 1, SpecialOp.Flavor.COMPUTER);
		assertEquals(expectedC1, actualC1);

		final String expectedC2 = "No such operation.";
		final String actualC2 = (String) ops.run(HelpCandidates.class,
			"test.special", null, 2, SpecialOp.Flavor.COMPUTER);
		assertEquals(expectedC2, actualC2);
	}

	// -- Helper classes --

	public interface Yummy extends Op {
		// NB: Marker interface.
	}

	public interface Yucky extends Op {
		// NB: Marker interface.
	}

	@Plugin(type = Yummy.class, name = "test.apple",
		priority = Priority.VERY_HIGH)
	public static class YummyApple extends NoOp implements Yummy {
		// NB: No implementation needed.
	}

	@Plugin(type = Yucky.class, name = "test.apple",
		priority = Priority.VERY_LOW)
	public static class YuckyApple extends NoOp implements Yucky {
		// NB: No implementation needed.
	}

	@Plugin(type = Yummy.class, name = "test.orange",
		priority = Priority.HIGH)
	public static class YummyOrange extends NoOp implements Yummy {
		// NB: No implementation needed.
	}

	@Plugin(type = Yucky.class, name = "test.orange",
		priority = Priority.LOW)
	public static class YuckyOrange extends NoOp implements Yucky {
		// NB: No implementation needed.
	}

	@Plugin(type = Op.class, name = "test.special")
	public static class AppleF0 extends AbstractNullaryFunctionOp<Apple> {

		@Override
		public Apple calculate() {
			return new Apple();
		}
	}

	@Plugin(type = Op.class, name = "test.special")
	public static class AppleF1 extends AbstractUnaryFunctionOp<Apple, Apple> {

		@Override
		public Apple calculate(final Apple in) {
			return new Apple();
		}
	}

	@Plugin(type = Op.class, name = "test.special")
	public static class AppleF2 extends
		AbstractBinaryFunctionOp<Apple, Apple, Apple>
	{

		@Override
		public Apple calculate(final Apple in1, final Apple in2) {
			return new Apple();
		}
	}

	@Plugin(type = Op.class, name = "test.special")
	public static class AppleC1 extends AbstractUnaryComputerOp<Apple, Apple> {

		@Override
		public void compute(final Apple out, final Apple in) {
			// NB: No implementation needed.
		}
	}
}
