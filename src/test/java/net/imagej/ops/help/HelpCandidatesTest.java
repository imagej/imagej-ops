/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;

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
		final String actual = ops.help();
		assertTrue(actual.startsWith("Available operations:\n"));
		assertTrue(actual.length() > 50000); // lots of ops!
	}

	@Test
	public void testName() {
		final String expectedApple = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyApple()\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyApple()";
		final String apple = ops.help("test.apple");
		assertEquals(expectedApple, apple);

		final String expectedOrange = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyOrange()\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyOrange()";
		final String orange = ops.help("test.orange");
		assertEquals(expectedOrange, orange);
	}

	@Test
	public void testType() {
		final String expectedYummy = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyApple()\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyOrange()";
		final String actualYummy = ops.help(null, Yummy.class);
		assertEquals(expectedYummy, actualYummy);

		final String expectedYucky = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyOrange()\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyApple()";
		final String yucky = ops.help(null, Yucky.class);
		assertEquals(expectedYucky, yucky);
	}

	@Test
	public void testNameAndType() {
		final String expectedYummyApple = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyApple()"; //
		final String actualYummyApple = ops.help("test.apple", Yummy.class);
		assertEquals(expectedYummyApple, actualYummyApple);

		final String expectedYuckyApple = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyApple()"; //
		final String actualYuckyApple = ops.help("test.apple", Yucky.class);
		assertEquals(expectedYuckyApple, actualYuckyApple);

		final String expectedYummyOrange = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YummyOrange()"; //
		final String actualYummyOrange = ops.help("test.orange", Yummy.class);
		assertEquals(expectedYummyOrange, actualYummyOrange);

		final String expectedYuckyOrange = "" + //
			"Available operations:\n" + //
			"\tnet.imagej.ops.help.HelpCandidatesTest$YuckyOrange()"; //
		final String actualYuckyOrange = ops.help("test.orange", Yucky.class);
		assertEquals(expectedYuckyOrange, actualYuckyOrange);
		
		final String expectedEmpty = ops.help("test.apple", Ops.Help.class);
		assertEquals("No such operation.", expectedEmpty);
	}

	// -- Helper classes --

	public interface Yummy extends Op {
		// NB: Marker interface.
	}

	public interface Yucky extends Op {
		// NB: Marker interface.
	}

	@Plugin(type = Yummy.class, name = "test.apple",
		priority = Priority.VERY_HIGH_PRIORITY)
	public static class YummyApple extends NoOp implements Yummy {
		// NB: No implementation needed.
	}

	@Plugin(type = Yucky.class, name = "test.apple",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class YuckyApple extends NoOp implements Yucky {
		// NB: No implementation needed.
	}

	@Plugin(type = Yummy.class, name = "test.orange",
		priority = Priority.HIGH_PRIORITY)
	public static class YummyOrange extends NoOp implements Yummy {
		// NB: No implementation needed.
	}

	@Plugin(type = Yucky.class, name = "test.orange",
		priority = Priority.LOW_PRIORITY)
	public static class YuckyOrange extends NoOp implements Yucky {
		// NB: No implementation needed.
	}

}
