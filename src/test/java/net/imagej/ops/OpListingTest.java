/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2023 ImageJ2 developers.
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
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Test;
import org.scijava.ItemIO;
import org.scijava.module.Module;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.Types;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link OpListing}.
 *
 * @author Gabriel Selzer
 */
public class OpListingTest extends AbstractOpTest{

	/**
	 * A very useful Op.
	 *
	 * @author Gabriel Selzer
	 */
	@Plugin(type = Op.class, name = "test.opListing")
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

		assertEquals("test.opListing", sig.getName());
	}

	@Test
	public void testFunctionalType() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		assertEquals(info.getType(), sig.getFunctionalType());
	}

	@Test
	public void testInputTypes() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		final List<Type> expectedTypes = Arrays.asList(Types.parameterize(Img.class,
			UnsignedByteType.class), UnsignedByteType.class);
		assertEquals(expectedTypes, sig.getInputTypes());
	}

	@Test
	public void testInputNames() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		// NB that the img's name is in because it is a UnaryFunctionOp
		final List<String> expectedNames = Arrays.asList("in", "unnecessary");
		assertEquals(expectedNames, sig.getInputNames());
	}

	@Test
	public void testOutputTypes() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		List<Type> expected = Collections.singletonList(String.class);
		assertEquals(expected, sig.getReturnTypes());
	}

	@Test
	public void testOutputNames() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();

		// NB that the img's name is in because it is a UnaryFunctionOp
		final List<String> expected = Collections.singletonList("out");
		assertEquals(expected, sig.getReturnNames());
	}

	@Test
	public void testToString() {
		final OpInfo info = new OpInfo(FooOp.class);
		final OpListing sig = info.listing();
		final String expected =
			"test.opListing(img \"in\", unsignedByteType \"unnecessary\") -> (string \"out\")";
		assertEquals(expected, sig.toString());
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
		assertEquals(expectedIns, sig.getInputTypes());
		final List<Type> expectedOut = Collections.singletonList(List.class);
		assertEquals(expectedOut, sig.getReturnTypes());
		// test string
		final String expected =
			"test.opListing(randomAccessibleInterval \"in\", number \"unnecessary\") -> (list \"out\")";
		assertEquals(expected, sig.toString());
	}

	/**
	 * An Op that, given {@link Double}s {@code a} and {@code b}, divides
	 * {@code a} by {@code b}, but <em>also</em> divides {@code b} by {@code a}.
	 *
	 * @author Gabriel Selzer
	 */
	@Plugin(type = Op.class, name = "test.opListingMultiple")
	public static class ADividedByB extends
		AbstractBinaryFunctionOp<Double, Double, Double>
	{

		@Parameter(type = ItemIO.OUTPUT)
		public Double bDividedByA;

		@Override
		public Double calculate(final Double a, final Double b) {
			bDividedByA = b / a;
			return a / b;
		}
	}

	@Test
	public void testMultipleOutputs() {
		final OpInfo info = new OpInfo(ADividedByB.class);
		final OpListing listing = info.listing();
		final OpListingInfo listingInfo = new OpListingInfo(context.getService(OpService.class), listing);
		final Module module = listingInfo.createModule();
		module.setInput("in1", 1.0);
		module.setInput("in2", 2.0);
		module.run();
		assertEquals(0.5, module.getOutput("out"));
		assertEquals(2.0, module.getOutput("bDividedByA"));
	}
	
	/**
	 * A test Computer
	 * 
	 * @author Gabriel Selzer
	 */
	@Plugin(type = Op.class, name = "test.opListingComputer")
	public static class ComputerOp extends
		AbstractUnaryComputerOp<Img<UnsignedByteType>, Img<UnsignedByteType>>
	{

		@Override
		public void compute(final Img<UnsignedByteType> in,
			final Img<UnsignedByteType> out)
		{}
	}

	@Test
	public void testComputerReduction() {
		final OpInfo info = new OpInfo(ComputerOp.class);
		final OpListing sig = info.listing().reduce(exampleReducer);
		// test input types
		final List<Type> expectedIns = Arrays.asList(RandomAccessibleInterval.class,
				RandomAccessibleInterval.class);
		assertEquals(expectedIns, sig.getInputTypes());
		final List<Type> expectedOut = Collections.singletonList(RandomAccessibleInterval.class);
		assertEquals(expectedOut, sig.getReturnTypes());
		// test string
		final String expected =
				"test.opListingComputer(randomAccessibleInterval \"out\", randomAccessibleInterval \"in\") -> (randomAccessibleInterval \"out\")";
		assertEquals(expected, sig.toString());
		
	}



}
