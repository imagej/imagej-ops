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

package net.imagej.ops.special;

import static org.junit.Assert.assertSame;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;

import org.junit.Test;
import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Tests that the special op matching methods of {@link OpEnvironment} work.
 *
 * @author Curtis Rueden
 */
public class SpecialOpMatchingTest extends AbstractOpTest {

	/**
	 * Tests
	 * {@link Computers#nullary(OpEnvironment, Class, Class, Object...)}
	 * (i.e.: without the output specified).
	 */
	@Test
	public void testNullaryComputer() {
		final NullaryComputerOp<Apple> nullaryComputerA = Computers.nullary(ops,
			FruitOp.class, Apple.class);
		assertSame(nullaryComputerA.getClass(), NullaryComputerA.class);

		final NullaryComputerOp<Orange> nullaryComputerO = Computers.nullary(ops,
			FruitOp.class, Orange.class);
		assertSame(nullaryComputerO.getClass(), NullaryComputerO.class);
	}

	/**
	 * Tests
	 * {@link Computers#nullary(OpEnvironment, Class, Object, Object...)}
	 * (i.e.: with the output specified).
	 */
	@Test
	public void testNullaryComputerOut() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final NullaryComputerOp<Apple> nullaryComputerA = Computers.nullary(ops,
			FruitOp.class, a);
		assertSame(nullaryComputerA.getClass(), NullaryComputerA.class);

		final NullaryComputerOp<Orange> nullaryComputerO = Computers.nullary(ops,
			FruitOp.class, o);
		assertSame(nullaryComputerO.getClass(), NullaryComputerO.class);
	}

	/**
	 * Tests {@link Functions#nullary(OpEnvironment, Class, Class, Object...)}.
	 */
	@Test
	public void testNullaryFunction() {
		final NullaryFunctionOp<Apple> nullaryFunctionA = Functions.nullary(ops,
			FruitOp.class, Apple.class);
		assertSame(nullaryFunctionA.getClass(), NullaryFunctionA.class);

		final NullaryFunctionOp<Orange> nullaryFunctionO = Functions.nullary(ops,
			FruitOp.class, Orange.class);
		assertSame(nullaryFunctionO.getClass(), NullaryFunctionO.class);
	}

	/**
	 * Tests {@link Hybrids#nullary(OpEnvironment, Class, Class, Object...)}
	 * (i.e.: without the output specified).
	 */
	@Test
	public void testNullaryHybrid() {
		final NullaryHybridOp<Apple> nullaryHybridA = Hybrids.nullary(ops,
			FruitOp.class, Apple.class);
		assertSame(nullaryHybridA.getClass(), NullaryHybridA.class);

		final NullaryHybridOp<Orange> nullaryHybridO = Hybrids.nullary(ops,
			FruitOp.class, Orange.class);
		assertSame(nullaryHybridO.getClass(), NullaryHybridO.class);
	}

	/**
	 * Tests {@link Hybrids#nullary(OpEnvironment, Class, Object, Object...)}
	 * (i.e.: with the output specified).
	 */
	@Test
	public void testNullaryHybridOut() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final NullaryHybridOp<Apple> nullaryHybridA = Hybrids.nullary(ops,
			FruitOp.class, a);
		assertSame(nullaryHybridA.getClass(), NullaryHybridA.class);

		final NullaryHybridOp<Orange> nullaryHybridO = Hybrids.nullary(ops,
			FruitOp.class, o);
		assertSame(nullaryHybridO.getClass(), NullaryHybridO.class);
	}

	/**
	 * Tests
	 * {@link Computers#unary(OpEnvironment, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor input specified).
	 */
	@Test
	public void testUnaryComputer() {
		final UnaryComputerOp<Apple, Apple> unaryComputerAA = Computers.unary(ops,
			FruitOp.class, Apple.class, Apple.class);
		assertSame(unaryComputerAA.getClass(), UnaryComputerAA.class);

		final UnaryComputerOp<Apple, Orange> unaryComputerAO = Computers.unary(ops,
			FruitOp.class, Orange.class, Apple.class);
		assertSame(unaryComputerAO.getClass(), UnaryComputerAO.class);

		final UnaryComputerOp<Orange, Apple> unaryComputerOA = Computers.unary(ops,
			FruitOp.class, Apple.class, Orange.class);
		assertSame(unaryComputerOA.getClass(), UnaryComputerOA.class);

		final UnaryComputerOp<Orange, Orange> unaryComputerOO = Computers.unary(ops,
			FruitOp.class, Orange.class, Orange.class);
		assertSame(unaryComputerOO.getClass(), UnaryComputerOO.class);
	}

	/**
	 * Tests
	 * {@link Computers#unary(OpEnvironment, Class, Class, Object, Object...)}
	 * (i.e.: with the input specified).
	 */
	@Test
	public void testUnaryComputerIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final UnaryComputerOp<Apple, Apple> unaryComputerAA = Computers.unary(ops,
			FruitOp.class, Apple.class, a);
		assertSame(unaryComputerAA.getClass(), UnaryComputerAA.class);

		final UnaryComputerOp<Apple, Orange> unaryComputerAO = Computers.unary(ops,
			FruitOp.class, Orange.class, a);
		assertSame(unaryComputerAO.getClass(), UnaryComputerAO.class);

		final UnaryComputerOp<Orange, Apple> unaryComputerOA = Computers.unary(ops,
			FruitOp.class, Apple.class, o);
		assertSame(unaryComputerOA.getClass(), UnaryComputerOA.class);

		final UnaryComputerOp<Orange, Orange> unaryComputerOO = Computers.unary(ops,
			FruitOp.class, Orange.class, o);
		assertSame(unaryComputerOO.getClass(), UnaryComputerOO.class);
	}

	/**
	 * Tests
	 * {@link Computers#unary(OpEnvironment, Class, Object, Object, Object...)}
	 * (i.e.: with the output and input specified).
	 */
	@Test
	public void testUnaryComputerOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final UnaryComputerOp<Apple, Apple> unaryComputerAA = Computers.unary(ops,
			FruitOp.class, a, a);
		assertSame(unaryComputerAA.getClass(), UnaryComputerAA.class);

		final UnaryComputerOp<Apple, Orange> unaryComputerAO = Computers.unary(ops,
			FruitOp.class, o, a);
		assertSame(unaryComputerAO.getClass(), UnaryComputerAO.class);

		final UnaryComputerOp<Orange, Apple> unaryComputerOA = Computers.unary(ops,
			FruitOp.class, a, o);
		assertSame(unaryComputerOA.getClass(), UnaryComputerOA.class);

		final UnaryComputerOp<Orange, Orange> unaryComputerOO = Computers.unary(ops,
			FruitOp.class, o, o);
		assertSame(unaryComputerOO.getClass(), UnaryComputerOO.class);
	}

	/**
	 * Tests
	 * {@link Functions#unary(OpEnvironment, Class, Class, Class, Object...)}
	 * (i.e.: without the input specified).
	 */
	@Test
	public void testUnaryFunction() {
		final UnaryFunctionOp<Apple, Apple> unaryFunctionAA = Functions.unary(ops,
			FruitOp.class, Apple.class, Apple.class);
		assertSame(unaryFunctionAA.getClass(), UnaryFunctionAA.class);

		final UnaryFunctionOp<Apple, Orange> unaryFunctionAO = Functions.unary(ops,
			FruitOp.class, Orange.class, Apple.class);
		assertSame(unaryFunctionAO.getClass(), UnaryFunctionAO.class);

		final UnaryFunctionOp<Orange, Apple> unaryFunctionOA = Functions.unary(ops,
			FruitOp.class, Apple.class, Orange.class);
		assertSame(unaryFunctionOA.getClass(), UnaryFunctionOA.class);

		final UnaryFunctionOp<Orange, Orange> unaryFunctionOO = Functions.unary(ops,
			FruitOp.class, Orange.class, Orange.class);
		assertSame(unaryFunctionOO.getClass(), UnaryFunctionOO.class);
	}

	/**
	 * Tests
	 * {@link Functions#unary(OpEnvironment, Class, Class, Object, Object...)}
	 * (i.e.: with the input specified).
	 */
	@Test
	public void testUnaryFunctionIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final UnaryFunctionOp<Apple, Apple> unaryFunctionAA = Functions.unary(ops,
			FruitOp.class, Apple.class, a);
		assertSame(unaryFunctionAA.getClass(), UnaryFunctionAA.class);

		final UnaryFunctionOp<Apple, Orange> unaryFunctionAO = Functions.unary(ops,
			FruitOp.class, Orange.class, a);
		assertSame(unaryFunctionAO.getClass(), UnaryFunctionAO.class);

		final UnaryFunctionOp<Orange, Apple> unaryFunctionOA = Functions.unary(ops,
			FruitOp.class, Apple.class, o);
		assertSame(unaryFunctionOA.getClass(), UnaryFunctionOA.class);

		final UnaryFunctionOp<Orange, Orange> unaryFunctionOO = Functions.unary(ops,
			FruitOp.class, Orange.class, o);
		assertSame(unaryFunctionOO.getClass(), UnaryFunctionOO.class);
	}

	/**
	 * Tests {@link Hybrids#unary(OpEnvironment, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor input specified).
	 */
	@Test
	public void testUnaryHybrid() {
		final UnaryHybridOp<Apple, Apple> unaryHybridAA = Hybrids.unary(ops,
			FruitOp.class, Apple.class, Apple.class);
		assertSame(unaryHybridAA.getClass(), UnaryHybridAA.class);

		final UnaryHybridOp<Apple, Orange> unaryHybridAO = Hybrids.unary(ops,
			FruitOp.class, Orange.class, Apple.class);
		assertSame(unaryHybridAO.getClass(), UnaryHybridAO.class);

		final UnaryHybridOp<Orange, Apple> unaryHybridOA = Hybrids.unary(ops,
			FruitOp.class, Apple.class, Orange.class);
		assertSame(unaryHybridOA.getClass(), UnaryHybridOA.class);

		final UnaryHybridOp<Orange, Orange> unaryHybridOO = Hybrids.unary(ops,
			FruitOp.class, Orange.class, Orange.class);
		assertSame(unaryHybridOO.getClass(), UnaryHybridOO.class);
	}

	/**
	 * Tests {@link Hybrids#unary(OpEnvironment, Class, Class, Object, Object...)}
	 * (i.e.: with the input specified).
	 */
	@Test
	public void testUnaryHybridIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final UnaryHybridOp<Apple, Apple> unaryHybridAA = Hybrids.unary(ops,
			FruitOp.class, Apple.class, a);
		assertSame(unaryHybridAA.getClass(), UnaryHybridAA.class);

		final UnaryHybridOp<Apple, Orange> unaryHybridAO = Hybrids.unary(ops,
			FruitOp.class, Orange.class, a);
		assertSame(unaryHybridAO.getClass(), UnaryHybridAO.class);

		final UnaryHybridOp<Orange, Apple> unaryHybridOA = Hybrids.unary(ops,
			FruitOp.class, Apple.class, o);
		assertSame(unaryHybridOA.getClass(), UnaryHybridOA.class);

		final UnaryHybridOp<Orange, Orange> unaryHybridOO = Hybrids.unary(ops,
			FruitOp.class, Orange.class, o);
		assertSame(unaryHybridOO.getClass(), UnaryHybridOO.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#unary(OpEnvironment, Class, Object, Object, Object...)}
	 * (i.e.: with the output and input specified).
	 */
	@Test
	public void testUnaryHybridOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final UnaryHybridOp<Apple, Apple> binaryHybridAA = Hybrids.unary(ops,
			FruitOp.class, a, a);
		assertSame(binaryHybridAA.getClass(), UnaryHybridAA.class);

		final UnaryHybridOp<Apple, Orange> binaryHybridAO = Hybrids.unary(ops,
			FruitOp.class, o, a);
		assertSame(binaryHybridAO.getClass(), UnaryHybridAO.class);

		final UnaryHybridOp<Orange, Apple> binaryHybridOA = Hybrids.unary(ops,
			FruitOp.class, a, o);
		assertSame(binaryHybridOA.getClass(), UnaryHybridOA.class);

		final UnaryHybridOp<Orange, Orange> binaryHybridOO = Hybrids.unary(ops,
			FruitOp.class, o, o);
		assertSame(binaryHybridOO.getClass(), UnaryHybridOO.class);
	}

	/**
	 * Tests {@link Inplaces#unary(OpEnvironment, Class, Class, Object...)} (i.e.:
	 * without the argument specified).
	 */
	@Test
	public void testInplace() {
		final InplaceOp<Apple> inplaceA = Inplaces.unary(ops, FruitOp.class,
			Apple.class);
		assertSame(inplaceA.getClass(), InplaceA.class);

		final InplaceOp<Orange> inplaceO = Inplaces.unary(ops, FruitOp.class,
			Orange.class);
		assertSame(inplaceO.getClass(), InplaceO.class);
	}

	/**
	 * Tests {@link Inplaces#unary(OpEnvironment, Class, Object, Object...)}
	 * (i.e.: with the argument specified).
	 */
	@Test
	public void testInplaceIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final InplaceOp<Apple> inplaceA = Inplaces.unary(ops, FruitOp.class, a);
		assertSame(inplaceA.getClass(), InplaceA.class);

		final InplaceOp<Orange> inplaceO = Inplaces.unary(ops, FruitOp.class, o);
		assertSame(inplaceO.getClass(), InplaceO.class);
	}

	/**
	 * Tests
	 * {@link Computers#binary(OpEnvironment, Class, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor inputs specified).
	 */
	@Test
	public void testBinaryComputer() {
		final BinaryComputerOp<Apple, Apple, Lemon> binaryComputerAAL = Computers
			.binary(ops, FruitOp.class, Lemon.class, Apple.class, Apple.class);
		assertSame(binaryComputerAAL.getClass(), BinaryComputerAAL.class);

		final BinaryComputerOp<Apple, Orange, Lemon> binaryComputerAOL = Computers
			.binary(ops, FruitOp.class, Lemon.class, Apple.class, Orange.class);
		assertSame(binaryComputerAOL.getClass(), BinaryComputerAOL.class);

		final BinaryComputerOp<Orange, Apple, Lemon> binaryComputerOAL = Computers
			.binary(ops, FruitOp.class, Lemon.class, Orange.class, Apple.class);
		assertSame(binaryComputerOAL.getClass(), BinaryComputerOAL.class);

		final BinaryComputerOp<Orange, Orange, Lemon> binaryComputerOOL = Computers
			.binary(ops, FruitOp.class, Lemon.class, Orange.class, Orange.class);
		assertSame(binaryComputerOOL.getClass(), BinaryComputerOOL.class);
	}

	/**
	 * Tests
	 * {@link Computers#binary(OpEnvironment, Class, Class, Object, Object, Object...)}
	 * (i.e.: with the inputs specified).
	 */
	@Test
	public void testBinaryComputerIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final BinaryComputerOp<Apple, Apple, Lemon> binaryComputerAAL = Computers
			.binary(ops, FruitOp.class, Lemon.class, a, a);
		assertSame(binaryComputerAAL.getClass(), BinaryComputerAAL.class);

		final BinaryComputerOp<Apple, Orange, Lemon> binaryComputerAOL = Computers
			.binary(ops, FruitOp.class, Lemon.class, a, o);
		assertSame(binaryComputerAOL.getClass(), BinaryComputerAOL.class);

		final BinaryComputerOp<Orange, Apple, Lemon> binaryComputerOAL = Computers
			.binary(ops, FruitOp.class, Lemon.class, o, a);
		assertSame(binaryComputerOAL.getClass(), BinaryComputerOAL.class);

		final BinaryComputerOp<Orange, Orange, Lemon> binaryComputerOOL = Computers
			.binary(ops, FruitOp.class, Lemon.class, o, o);
		assertSame(binaryComputerOOL.getClass(), BinaryComputerOOL.class);
	}

	/**
	 * Tests
	 * {@link Computers#binary(OpEnvironment, Class, Object, Object, Object, Object...)}
	 * (i.e.: with the output and inputs specified).
	 */
	@Test
	public void testBinaryComputerOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryComputerOp<Apple, Apple, Lemon> binaryComputerAAL = Computers
			.binary(ops, FruitOp.class, l, a, a);
		assertSame(binaryComputerAAL.getClass(), BinaryComputerAAL.class);

		final BinaryComputerOp<Apple, Orange, Lemon> binaryComputerAOL = Computers
			.binary(ops, FruitOp.class, l, a, o);
		assertSame(binaryComputerAOL.getClass(), BinaryComputerAOL.class);

		final BinaryComputerOp<Orange, Apple, Lemon> binaryComputerOAL = Computers
			.binary(ops, FruitOp.class, l, o, a);
		assertSame(binaryComputerOAL.getClass(), BinaryComputerOAL.class);

		final BinaryComputerOp<Orange, Orange, Lemon> binaryComputerOOL = Computers
			.binary(ops, FruitOp.class, l, o, o);
		assertSame(binaryComputerOOL.getClass(), BinaryComputerOOL.class);
	}

	/**
	 * Tests
	 * {@link Functions#binary(OpEnvironment, Class, Class, Class, Class, Object...)}
	 * (i.e.: without the inputs specified).
	 */
	@Test
	public void testBinaryFunction() {
		final BinaryFunctionOp<Apple, Apple, Lemon> binaryFunctionAAL = Functions
			.binary(ops, FruitOp.class, Lemon.class, Apple.class, Apple.class);
		assertSame(binaryFunctionAAL.getClass(), BinaryFunctionAAL.class);

		final BinaryFunctionOp<Apple, Orange, Lemon> binaryFunctionAOL = Functions
			.binary(ops, FruitOp.class, Lemon.class, Apple.class, Orange.class);
		assertSame(binaryFunctionAOL.getClass(), BinaryFunctionAOL.class);

		final BinaryFunctionOp<Orange, Apple, Lemon> binaryFunctionOAL = Functions
			.binary(ops, FruitOp.class, Lemon.class, Orange.class, Apple.class);
		assertSame(binaryFunctionOAL.getClass(), BinaryFunctionOAL.class);

		final BinaryFunctionOp<Orange, Orange, Lemon> binaryFunctionOOL = Functions
			.binary(ops, FruitOp.class, Lemon.class, Orange.class, Orange.class);
		assertSame(binaryFunctionOOL.getClass(), BinaryFunctionOOL.class);
	}

	/**
	 * Tests
	 * {@link Functions#binary(OpEnvironment, Class, Class, Object, Object, Object...)}
	 * (i.e.: with the inputs specified).
	 */
	@Test
	public void testBinaryFunctionIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final BinaryFunctionOp<Apple, Apple, Lemon> binaryFunctionAAL = Functions
			.binary(ops, FruitOp.class, Lemon.class, a, a);
		assertSame(binaryFunctionAAL.getClass(), BinaryFunctionAAL.class);

		final BinaryFunctionOp<Apple, Orange, Lemon> binaryFunctionAOL = Functions
			.binary(ops, FruitOp.class, Lemon.class, a, o);
		assertSame(binaryFunctionAOL.getClass(), BinaryFunctionAOL.class);

		final BinaryFunctionOp<Orange, Apple, Lemon> binaryFunctionOAL = Functions
			.binary(ops, FruitOp.class, Lemon.class, o, a);
		assertSame(binaryFunctionOAL.getClass(), BinaryFunctionOAL.class);

		final BinaryFunctionOp<Orange, Orange, Lemon> binaryFunctionOOL = Functions
			.binary(ops, FruitOp.class, Lemon.class, o, o);
		assertSame(binaryFunctionOOL.getClass(), BinaryFunctionOOL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binary(OpEnvironment, Class, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor inputs specified).
	 */
	@Test
	public void testBinaryHybrid() {
		final BinaryHybridOp<Apple, Apple, Lemon> binaryHybridAAL = Hybrids.binary(
			ops, FruitOp.class, Lemon.class, Apple.class, Apple.class);
		assertSame(binaryHybridAAL.getClass(), BinaryHybridAAL.class);

		final BinaryHybridOp<Apple, Orange, Lemon> binaryHybridAOL = Hybrids.binary(
			ops, FruitOp.class, Lemon.class, Apple.class, Orange.class);
		assertSame(binaryHybridAOL.getClass(), BinaryHybridAOL.class);

		final BinaryHybridOp<Orange, Apple, Lemon> binaryHybridOAL = Hybrids.binary(
			ops, FruitOp.class, Lemon.class, Orange.class, Apple.class);
		assertSame(binaryHybridOAL.getClass(), BinaryHybridOAL.class);

		final BinaryHybridOp<Orange, Orange, Lemon> binaryHybridOOL = Hybrids
			.binary(ops, FruitOp.class, Lemon.class, Orange.class, Orange.class);
		assertSame(binaryHybridOOL.getClass(), BinaryHybridOOL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binary(OpEnvironment, Class, Class, Object, Object, Object...)}
	 * (i.e.: with the inputs specified).
	 */
	@Test
	public void testBinaryHybridIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final BinaryHybridOp<Apple, Apple, Lemon> binaryHybridAAL = Hybrids.binary(
			ops, FruitOp.class, Lemon.class, a, a);
		assertSame(binaryHybridAAL.getClass(), BinaryHybridAAL.class);

		final BinaryHybridOp<Apple, Orange, Lemon> binaryHybridAOL = Hybrids.binary(
			ops, FruitOp.class, Lemon.class, a, o);
		assertSame(binaryHybridAOL.getClass(), BinaryHybridAOL.class);

		final BinaryHybridOp<Orange, Apple, Lemon> binaryHybridOAL = Hybrids.binary(
			ops, FruitOp.class, Lemon.class, o, a);
		assertSame(binaryHybridOAL.getClass(), BinaryHybridOAL.class);

		final BinaryHybridOp<Orange, Orange, Lemon> binaryHybridOOL = Hybrids
			.binary(ops, FruitOp.class, Lemon.class, o, o);
		assertSame(binaryHybridOOL.getClass(), BinaryHybridOOL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binary(OpEnvironment, Class, Object, Object, Object, Object...)}
	 * (i.e.: with the output and inputs specified).
	 */
	@Test
	public void testBinaryHybridOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridOp<Apple, Apple, Lemon> binaryHybridAAL = Hybrids.binary(ops,
			FruitOp.class, l, a, a);
		assertSame(binaryHybridAAL.getClass(), BinaryHybridAAL.class);

		final BinaryHybridOp<Apple, Orange, Lemon> binaryHybridAOL = Hybrids.binary(ops,
			FruitOp.class, l, a, o);
		assertSame(binaryHybridAOL.getClass(), BinaryHybridAOL.class);

		final BinaryHybridOp<Orange, Apple, Lemon> binaryHybridOAL = Hybrids.binary(ops,
			FruitOp.class, l, o, a);
		assertSame(binaryHybridOAL.getClass(), BinaryHybridOAL.class);

		final BinaryHybridOp<Orange, Orange, Lemon> binaryHybridOOL = Hybrids.binary(ops,
			FruitOp.class, l, o, o);
		assertSame(binaryHybridOOL.getClass(), BinaryHybridOOL.class);
	}

	// -- Helper classes --

	public static class Apple {
		// NB: No implementation needed.
	}

	public static class Orange {
		// NB: No implementation needed.
	}

	public static class Lemon {
		// NB: No implementation needed.
	}

	public static interface FruitOp extends Op {
		// NB: Marker interface.
	}

	public abstract static class NullaryFruitComputer<O> extends
		AbstractNullaryComputerOp<O> implements FruitOp
	{

		@Override
		public void compute0(final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class NullaryFruitFunction<O> extends
		AbstractNullaryFunctionOp<O> implements FruitOp
	{

		@Override
		public O compute0() {
			return null;
		}
	}

	public abstract static class NullaryFruitHybrid<O> extends
		AbstractNullaryHybridOp<O> implements FruitOp
	{

		@Override
		public O createOutput() {
			return null;
		}

		@Override
		public void compute0(final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class UnaryFruitComputer<I, O> extends
		AbstractUnaryComputerOp<I, O> implements FruitOp
	{

		@Override
		public void compute1(final I in, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class UnaryFruitFunction<I, O> extends
		AbstractUnaryFunctionOp<I, O> implements FruitOp
	{

		@Override
		public O compute1(final I in) {
			return null;
		}
	}

	public abstract static class UnaryFruitHybrid<I, O> extends
		AbstractUnaryHybridOp<I, O> implements FruitOp
	{

		@Override
		public O createOutput(final I input) {
			return null;
		}

		@Override
		public void compute1(final I in, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class FruitInplace<A> extends AbstractInplaceOp<A>
		implements FruitOp
	{

		@Override
		public void mutate(final A arg) {
			// NB: No implementation needed.
		}
	}

	public abstract static class AbstractFruitOp extends NoOp implements FruitOp {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryComputerA")
	public static class NullaryComputerA extends NullaryFruitComputer<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryComputerO")
	public static class NullaryComputerO extends NullaryFruitComputer<Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryFunctionA",
		priority = Priority.HIGH_PRIORITY)
	public static class NullaryFunctionA extends NullaryFruitFunction<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryFunctionO")
	public static class NullaryFunctionO extends NullaryFruitFunction<Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryHybridA",
		priority = Priority.LOW_PRIORITY + 1)
	public static class NullaryHybridA extends NullaryFruitHybrid<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryHybridO",
		priority = Priority.LOW_PRIORITY + 1)
	public static class NullaryHybridO extends NullaryFruitHybrid<Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryComputerAA")
	public static class UnaryComputerAA extends UnaryFruitComputer<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryComputerAO")
	public static class UnaryComputerAO extends UnaryFruitComputer<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryComputerOA")
	public static class UnaryComputerOA extends UnaryFruitComputer<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryComputerOO")
	public static class UnaryComputerOO extends UnaryFruitComputer<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryFunctionAA",
		priority = Priority.HIGH_PRIORITY)
	public static class UnaryFunctionAA extends UnaryFruitFunction<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryFunctionAO")
	public static class UnaryFunctionAO extends UnaryFruitFunction<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryFunctionOA")
	public static class UnaryFunctionOA extends UnaryFruitFunction<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryFunctionOO",
		priority = Priority.HIGH_PRIORITY)
	public static class UnaryFunctionOO extends UnaryFruitFunction<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridAA",
		priority = Priority.LOW_PRIORITY)
	public static class UnaryHybridAA extends UnaryFruitHybrid<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridAO",
		priority = Priority.LOW_PRIORITY)
	public static class UnaryHybridAO extends UnaryFruitHybrid<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridOA",
		priority = Priority.LOW_PRIORITY)
	public static class UnaryHybridOA extends UnaryFruitHybrid<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridOO",
		priority = Priority.LOW_PRIORITY)
	public static class UnaryHybridOO extends UnaryFruitHybrid<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.inplaceA",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class InplaceA extends FruitInplace<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.inplaceO",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class InplaceO extends FruitInplace<Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.fakeComputerAA")
	public static class FakeComputerAA extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH)
		private Apple o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeComputerAO")
	public static class FakeComputerAO extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH)
		private Orange o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeComputerOA")
	public static class FakeComputerOA extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH)
		private Apple o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeComputerOO")
	public static class FakeComputerOO extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH)
		private Orange o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeFunctionAA")
	public static class FakeFunctionAA extends AbstractFruitOp {

		@Parameter(type = ItemIO.OUTPUT)
		private Apple o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeFunctionAO")
	public static class FakeFunctionAO extends AbstractFruitOp {

		@Parameter(type = ItemIO.OUTPUT)
		private Orange o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeFunctionOA")
	public static class FakeFunctionOA extends AbstractFruitOp {

		@Parameter(type = ItemIO.OUTPUT)
		private Apple o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeFunctionOO")
	public static class FakeFunctionOO extends AbstractFruitOp {

		@Parameter(type = ItemIO.OUTPUT)
		private Orange o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeHybridAA")
	public static class FakeHybridAA extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH, required = false)
		private Apple o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeHybridAO")
	public static class FakeHybridAO extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH, required = false)
		private Orange o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeHybridOA")
	public static class FakeHybridOA extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH, required = false)
		private Apple o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeHybridOO")
	public static class FakeHybridOO extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH, required = false)
		private Orange o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeInplaceD")
	public static class FakeInplaceA extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH)
		private Apple a;
	}

	@Plugin(type = FruitOp.class, name = "test.fakeInplaceS")
	public static class FakeInplaceO extends AbstractFruitOp {

		@Parameter(type = ItemIO.BOTH)
		private Orange a;
	}

	// -- Binary ops --

	public abstract static class BinaryFruitComputer<I1, I2, O> extends
		AbstractBinaryComputerOp<I1, I2, O> implements FruitOp
	{

		@Override
		public void compute2(final I1 in1, final I2 in2, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class BinaryFruitFunction<I1, I2, O> extends
		AbstractBinaryFunctionOp<I1, I2, O> implements FruitOp
	{

		@Override
		public O compute2(final I1 in1, final I2 in2) {
			return null;
		}
	}

	public abstract static class BinaryFruitHybrid<I1, I2, O> extends
		AbstractBinaryHybridOp<I1, I2, O> implements FruitOp
	{

		@Override
		public O createOutput(final I1 in1, final I2 in2) {
			return null;
		}

		@Override
		public void compute2(final I1 in1, final I2 in2, final O out) {
			// NB: No implementation needed.
		}
	}

	@Plugin(type = FruitOp.class, name = "test.binaryComputerAAL")
	public static class BinaryComputerAAL extends
		BinaryFruitComputer<Apple, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryComputerAOL")
	public static class BinaryComputerAOL extends
		BinaryFruitComputer<Apple, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryComputerOAL")
	public static class BinaryComputerOAL extends
		BinaryFruitComputer<Orange, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryComputerOOL")
	public static class BinaryComputerOOL extends
		BinaryFruitComputer<Orange, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryFunctionAAL",
		priority = Priority.HIGH_PRIORITY)
	public static class BinaryFunctionAAL extends
		BinaryFruitFunction<Apple, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryFunctionAOL")
	public static class BinaryFunctionAOL extends
		BinaryFruitFunction<Apple, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryFunctionOAL")
	public static class BinaryFunctionOAL extends
		BinaryFruitFunction<Orange, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryFunctionOOL",
		priority = Priority.HIGH_PRIORITY)
	public static class BinaryFunctionOOL extends
		BinaryFruitFunction<Orange, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridAAL",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class BinaryHybridAAL extends
		BinaryFruitHybrid<Apple, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridAOL",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class BinaryHybridAOL extends
		BinaryFruitHybrid<Apple, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridOAL",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class BinaryHybridOAL extends
		BinaryFruitHybrid<Orange, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridOOL",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class BinaryHybridOOL extends
		BinaryFruitHybrid<Orange, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

}
