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

package net.imagej.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;
import org.scijava.ItemIO;
import org.scijava.module.Module;
import org.scijava.plugin.Attr;
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

		final Module moduleByName =
			matcher.findModule(ops, OpRef.create("test.nan", value));
		assertSame(value, moduleByName.getInput("arg"));

		assertFalse(Double.isNaN(value.get()));
		moduleByName.run();
		assertTrue(Double.isNaN(value.get()));

		value.set(987.654);
		final Module moduleByType =
			matcher.findModule(ops, OpRef.create(NaNOp.class, value));
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
			m = optionalParamsModule(1, 2, 3, 4, 5, 6, 7);
			fail("Expected IllegalArgumentException for 7 args");
		}
		catch (final IllegalArgumentException exc) {
			// NB: Expected.
		}

		m = optionalParamsModule(1, 2, 3, 4, 5, 6);
		assertValues(m, 1, 2, 3, 4, 5, 6, -7);

		m = optionalParamsModule(1, 2, 3, 4, 5);
		assertValues(m, 1, 2, 3, 4, -5, 5, -7);

		m = optionalParamsModule(1, 2, 3, 4);
		assertValues(m, 1, 2, 3, -4, -5, 4, -7);

		m = optionalParamsModule(1, 2, 3);
		assertValues(m, 1, -2, 2, -4, -5, 3, -7);

		try {
			m = optionalParamsModule(1, 2);
			fail("Expected IllegalArgumentException for 2 args");
		}
		catch (final IllegalArgumentException exc) {
			// NB: Expected.
		}
	}

	@Test
	public void testNameViaInterface() {
		assertMatches("dessert.iceCream", FlavorlessIceCream.class);
		assertMatches("dessert.vanillaIceCream", VanillaIceCream.class);
		assertMatches("dessert.chocolateIceCream", ChocolateIceCream.class);
		assertMatches("dessert.sorbet", YummySorbet.class);
		assertMatches("dessert.sherbet", GenericSherbet.class, RainbowSherbet.class);
		assertMatches("dessert.americanSherbet", GenericSherbet.class);
		assertMatches("dessert.rainbowSherbet", RainbowSherbet.class);
		assertMatches("dessert.gelato", RichGelato.class);
		assertMatches("dessert.gelati", RichGelato.class);
		assertMatches("dessert.italianIceCream", RichGelato.class);
	}

	// -- Helper methods --

	private Module optionalParamsModule(Object... args) {
		return matcher.findModule(ops, OpRef.create(OptionalParams.class, args));
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

	private void assertMatches(final String name, Class<?>... opTypes) {
		final List<OpCandidate<Op>> candidates =
			matcher.findCandidates(ops, OpRef.create(name));
		final List<Module> matches = matcher.findMatches(candidates);
		assertEquals(opTypes.length, matches.size());
		for (int i=0; i<opTypes.length; i++) {
			assertSame(opTypes[i], matches.get(i).getDelegateObject().getClass());
		}
	}

	// -- Helper classes --

	/** A test {@link Op}. */
	@Plugin(type = Op.class, name = "test.nan")
	public static class NaNOp extends AbstractOp {

		@Parameter(type = ItemIO.BOTH)
		private DoubleType arg;

		@Override
		public void run() {
			arg.set(Double.NaN);
		}
	}

	@Plugin(type = Op.class)
	public static class OptionalParams extends AbstractOp {

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

	public static interface Dessert extends Op {
		// NB: Marker interface.
	}

	public static interface IceCream extends Dessert {
		String NAME = "dessert.iceCream";
	}

	public static interface Sorbet extends IceCream {
		String NAME = "dessert.sorbet";
	}

	public static interface Sherbet extends IceCream {
		String NAME = "dessert.sherbet";
		String ALIAS = "dessert.americanSherbet";
	}

	public static interface Gelato extends IceCream {
		String NAME = "dessert.gelato";
		String ALIASES = "dessert.gelati, dessert.italianIceCream";
	}

	@Plugin(type = IceCream.class)
	public static class FlavorlessIceCream extends NoOp implements IceCream {
		// NB: No implementation needed.
	}

	@Plugin(type = IceCream.class, name = "dessert.vanillaIceCream")
	public static class VanillaIceCream extends NoOp implements IceCream {
		// NB: No implementation needed.
	}

	@Plugin(type = IceCream.class, name = "dessert.chocolateIceCream")
	public static class ChocolateIceCream extends NoOp implements IceCream {
		// NB: No implementation needed.
	}

	@Plugin(type = Sorbet.class)
	public static class YummySorbet extends NoOp implements Sorbet {
		// NB: No implementation needed.
	}

	@Plugin(type = Sherbet.class)
	public static class GenericSherbet extends NoOp implements Sherbet {
		// NB: No implementation needed.
	}

	@Plugin(type = Sherbet.class, //
		attrs = { @Attr(name = "alias", value = "dessert.rainbowSherbet") })
	public static class RainbowSherbet extends NoOp implements Sherbet {
		// NB: No implementation needed.
	}

	@Plugin(type = Gelato.class)
	public static class RichGelato extends NoOp implements Gelato {
		// NB: No implementation needed.
	}

}
