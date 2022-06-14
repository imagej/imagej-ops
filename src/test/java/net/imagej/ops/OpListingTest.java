/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Assert;
import org.junit.Test;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.Types;

/**
 * Tests {@link OpListing}.
 *
 * @author Gabriel Selzer
 */
public class OpListingTest {

	/**
	 * A very useful Op.
	 *
	 * @author Gabriel Selzer
	 */
	@Plugin(type = Op.class, name = "test.opSignature")
	public static class FooOp extends
		AbstractUnaryFunctionOp<Img<UnsignedByteType>, String>
	{

		@Parameter
		public UnsignedByteType unnecessary;

		@Override
		public String calculate(final Img<UnsignedByteType> weDontUseThis) {
			return "Foooooooooooo";
		}
	}

	@Test
	public void testName() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		Assert.assertEquals("test.opSignature", sig.getName());
	}

	@Test
	public void testFunctionalType() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		Assert.assertEquals(info.getType(), sig.getFunctionalType());
	}

	@Test
	public void testInputTypes() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		final List<Type> expectedTypes = Arrays.asList(Types.parameterize(Img.class,
			UnsignedByteType.class), UnsignedByteType.class);
		Assert.assertEquals(expectedTypes, sig.getInputTypes());
	}

	@Test
	public void testInputNames() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		// NB that the img's name is in because it is a UnaryFunctionOp
		final List<String> expectedNames = Arrays.asList("in", "unnecessary");
		Assert.assertEquals(expectedNames, sig.getInputNames());
	}

	@Test
	public void testOutputTypes() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		final Optional<Type> expected = Optional.of(String.class);
		Assert.assertEquals(expected, sig.getReturnType());
	}

	@Test
	public void testOutputNames() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		// NB that the img's name is in because it is a UnaryFunctionOp
		final Optional<String> expected = Optional.of("out");
		Assert.assertEquals(expected, sig.getReturnName());
	}

	@Test
	public void testToString() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();
		final String expected =
			"test.opSignature(img, unsignedByteType \"unnecessary\") -> string";
		Assert.assertEquals(expected, sig.toString());
	}

	private final Function<Type, Type> exampleReducer = (t) -> {
		final Class<?> raw = Types.raw(t);
		if (Img.class.isAssignableFrom(raw) || raw.isAssignableFrom(Img.class)) {
			return RandomAccessibleInterval.class;
		}
		if (RealType.class.isAssignableFrom(raw)) return Number.class;
		if (String.class.isAssignableFrom(raw)) return List.class;
		return raw;
	};

	@Test
	public void testReduction() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing().reduce(exampleReducer);
		// test input types
		final List<Type> expectedIns = Arrays.asList(RandomAccessibleInterval.class,
			Number.class);
		Assert.assertEquals(expectedIns, sig.getInputTypes());
		final Optional<Type> expectedOut = Optional.of(List.class);
		Assert.assertEquals(expectedOut, sig.getReturnType());
		// test string
		final String expected =
			"test.opSignature(randomAccessibleInterval, number \"unnecessary\") -> list";
		Assert.assertEquals(expected, sig.toString());
	}
}
