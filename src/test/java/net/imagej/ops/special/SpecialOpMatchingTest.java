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

package net.imagej.ops.special;

import static org.junit.Assert.assertSame;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.computer.AbstractNullaryComputerOp;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.NullaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.AbstractNullaryFunctionOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.NullaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCFI;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCFI1;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCI;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCI1;
import net.imagej.ops.special.hybrid.AbstractNullaryHybridCF;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCFI;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCI;
import net.imagej.ops.special.hybrid.BinaryHybridCF;
import net.imagej.ops.special.hybrid.BinaryHybridCFI;
import net.imagej.ops.special.hybrid.BinaryHybridCFI1;
import net.imagej.ops.special.hybrid.BinaryHybridCI;
import net.imagej.ops.special.hybrid.BinaryHybridCI1;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imagej.ops.special.hybrid.NullaryHybridCF;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imagej.ops.special.hybrid.UnaryHybridCFI;
import net.imagej.ops.special.hybrid.UnaryHybridCI;
import net.imagej.ops.special.inplace.AbstractBinaryInplace1Op;
import net.imagej.ops.special.inplace.AbstractBinaryInplaceOp;
import net.imagej.ops.special.inplace.AbstractUnaryInplaceOp;
import net.imagej.ops.special.inplace.BinaryInplace1Op;
import net.imagej.ops.special.inplace.BinaryInplaceOp;
import net.imagej.ops.special.inplace.Inplaces;
import net.imagej.ops.special.inplace.UnaryInplaceOp;

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
	 * Tests {@link Hybrids#nullaryCF(OpEnvironment, Class, Class, Object...)}
	 * (i.e.: without the output specified).
	 */
	@Test
	public void testNullaryHybrid() {
		final NullaryHybridCF<Apple> nullaryHybridA = Hybrids.nullaryCF(ops,
			FruitOp.class, Apple.class);
		assertSame(nullaryHybridA.getClass(), NullaryHybridA.class);

		final NullaryHybridCF<Orange> nullaryHybridO = Hybrids.nullaryCF(ops,
			FruitOp.class, Orange.class);
		assertSame(nullaryHybridO.getClass(), NullaryHybridO.class);
	}

	/**
	 * Tests {@link Hybrids#nullaryCF(OpEnvironment, Class, Object, Object...)}
	 * (i.e.: with the output specified).
	 */
	@Test
	public void testNullaryHybridOut() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final NullaryHybridCF<Apple> nullaryHybridA = Hybrids.nullaryCF(ops,
			FruitOp.class, a);
		assertSame(nullaryHybridA.getClass(), NullaryHybridA.class);

		final NullaryHybridCF<Orange> nullaryHybridO = Hybrids.nullaryCF(ops,
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
	 * Tests {@link Hybrids#unaryCF(OpEnvironment, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor input specified).
	 */
	@Test
	public void testUnaryHybrid() {
		final UnaryHybridCF<Apple, Apple> unaryHybridAA = Hybrids.unaryCF(ops,
			FruitOp.class, Apple.class, Apple.class);
		assertSame(unaryHybridAA.getClass(), UnaryHybridAA.class);

		final UnaryHybridCF<Apple, Orange> unaryHybridAO = Hybrids.unaryCF(ops,
			FruitOp.class, Orange.class, Apple.class);
		assertSame(unaryHybridAO.getClass(), UnaryHybridAO.class);

		final UnaryHybridCF<Orange, Apple> unaryHybridOA = Hybrids.unaryCF(ops,
			FruitOp.class, Apple.class, Orange.class);
		assertSame(unaryHybridOA.getClass(), UnaryHybridOA.class);

		final UnaryHybridCF<Orange, Orange> unaryHybridOO = Hybrids.unaryCF(ops,
			FruitOp.class, Orange.class, Orange.class);
		assertSame(unaryHybridOO.getClass(), UnaryHybridOO.class);
	}

	/**
	 * Tests {@link Hybrids#unaryCF(OpEnvironment, Class, Class, Object, Object...)}
	 * (i.e.: with the input specified).
	 */
	@Test
	public void testUnaryHybridIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final UnaryHybridCF<Apple, Apple> unaryHybridAA = Hybrids.unaryCF(ops,
			FruitOp.class, Apple.class, a);
		assertSame(unaryHybridAA.getClass(), UnaryHybridAA.class);

		final UnaryHybridCF<Apple, Orange> unaryHybridAO = Hybrids.unaryCF(ops,
			FruitOp.class, Orange.class, a);
		assertSame(unaryHybridAO.getClass(), UnaryHybridAO.class);

		final UnaryHybridCF<Orange, Apple> unaryHybridOA = Hybrids.unaryCF(ops,
			FruitOp.class, Apple.class, o);
		assertSame(unaryHybridOA.getClass(), UnaryHybridOA.class);

		final UnaryHybridCF<Orange, Orange> unaryHybridOO = Hybrids.unaryCF(ops,
			FruitOp.class, Orange.class, o);
		assertSame(unaryHybridOO.getClass(), UnaryHybridOO.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#unaryCF(OpEnvironment, Class, Object, Object, Object...)}
	 * (i.e.: with the output and input specified).
	 */
	@Test
	public void testUnaryHybridOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final UnaryHybridCF<Apple, Apple> binaryHybridAA = Hybrids.unaryCF(ops,
			FruitOp.class, a, a);
		assertSame(binaryHybridAA.getClass(), UnaryHybridAA.class);

		final UnaryHybridCF<Apple, Orange> binaryHybridAO = Hybrids.unaryCF(ops,
			FruitOp.class, o, a);
		assertSame(binaryHybridAO.getClass(), UnaryHybridAO.class);

		final UnaryHybridCF<Orange, Apple> binaryHybridOA = Hybrids.unaryCF(ops,
			FruitOp.class, a, o);
		assertSame(binaryHybridOA.getClass(), UnaryHybridOA.class);

		final UnaryHybridCF<Orange, Orange> binaryHybridOO = Hybrids.unaryCF(ops,
			FruitOp.class, o, o);
		assertSame(binaryHybridOO.getClass(), UnaryHybridOO.class);
	}

	/**
	 * Tests {@link Hybrids#unaryCI(OpEnvironment, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor input specified).
	 */
	@Test
	public void testUnaryHybridCI() {
		final UnaryHybridCI<Apple, Apple> unaryHybridCIAA = Hybrids.unaryCI(ops,
			FruitOp.class, Apple.class, Apple.class);
		assertSame(unaryHybridCIAA.getClass(), UnaryHybridCIAA.class);

		final UnaryHybridCI<Orange, Orange> unaryHybridCIOO = Hybrids.unaryCI(ops,
			FruitOp.class, Orange.class, Orange.class);
		assertSame(unaryHybridCIOO.getClass(), UnaryHybridCIOO.class);

		final UnaryHybridCI<Lemon, Lemon> unaryHybridCILL = Hybrids.unaryCI(ops,
			FruitOp.class, Lemon.class, Lemon.class);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(unaryHybridCILL.getClass(), UnaryHybridCFILL.class);
	}

	/**
	 * Tests {@link Hybrids#unaryCI(OpEnvironment, Class, Class, Object, Object...)}
	 * (i.e.: with the input specified).
	 */
	@Test
	public void testUnaryHybridCIIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final UnaryHybridCI<Apple, Apple> unaryHybridCIAA = Hybrids.unaryCI(ops,
			FruitOp.class, Apple.class, a);
		assertSame(unaryHybridCIAA.getClass(), UnaryHybridCIAA.class);

		final UnaryHybridCI<Orange, Orange> unaryHybridCIOO = Hybrids.unaryCI(ops,
			FruitOp.class, Orange.class, o);
		assertSame(unaryHybridCIOO.getClass(), UnaryHybridCIOO.class);

		final UnaryHybridCI<Lemon, Lemon> unaryHybridCILL = Hybrids.unaryCI(ops,
			FruitOp.class, Lemon.class, l);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(unaryHybridCILL.getClass(), UnaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#unaryCI(OpEnvironment, Class, Object, Object, Object...)}
	 * (i.e.: with the output and input specified).
	 */
	@Test
	public void testUnaryHybridCIOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final UnaryHybridCI<Apple, Apple> unaryHybridCIAA = Hybrids.unaryCI(ops,
			FruitOp.class, a, a);
		assertSame(unaryHybridCIAA.getClass(), UnaryHybridCIAA.class);

		final UnaryHybridCI<Orange, Orange> unaryHybridCIOO = Hybrids.unaryCI(ops,
			FruitOp.class, o, o);
		assertSame(unaryHybridCIOO.getClass(), UnaryHybridCIOO.class);

		final UnaryHybridCI<Lemon, Lemon> unaryHybridCILL = Hybrids.unaryCI(ops,
			FruitOp.class, l, l);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(unaryHybridCILL.getClass(), UnaryHybridCFILL.class);
	}

	/**
	 * Tests {@link Hybrids#unaryCFI(OpEnvironment, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor input speCFIfied).
	 */
	@Test
	public void testUnaryHybridCFI() {
		final UnaryHybridCFI<Apple, Apple> unaryHybridCFIAA = Hybrids.unaryCFI(ops,
			FruitOp.class, Apple.class, Apple.class);
		assertSame(unaryHybridCFIAA.getClass(), UnaryHybridCFIAA.class);

		final UnaryHybridCFI<Orange, Orange> unaryHybridCFIOO = Hybrids.unaryCFI(ops,
			FruitOp.class, Orange.class, Orange.class);
		assertSame(unaryHybridCFIOO.getClass(), UnaryHybridCFIOO.class);

		final UnaryHybridCFI<Lemon, Lemon> unaryHybridCFILL = Hybrids.unaryCFI(ops,
			FruitOp.class, Lemon.class, Lemon.class);
		assertSame(unaryHybridCFILL.getClass(), UnaryHybridCFILL.class);
	}

	/**
	 * Tests {@link Hybrids#unaryCFI(OpEnvironment, Class, Class, Object, Object...)}
	 * (i.e.: with the input speCFIfied).
	 */
	@Test
	public void testUnaryHybridCFIIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final UnaryHybridCFI<Apple, Apple> unaryHybridCFIAA = Hybrids.unaryCFI(ops,
			FruitOp.class, Apple.class, a);
		assertSame(unaryHybridCFIAA.getClass(), UnaryHybridCFIAA.class);

		final UnaryHybridCFI<Orange, Orange> unaryHybridCFIOO = Hybrids.unaryCFI(ops,
			FruitOp.class, Orange.class, o);
		assertSame(unaryHybridCFIOO.getClass(), UnaryHybridCFIOO.class);

		final UnaryHybridCFI<Lemon, Lemon> unaryHybridCFILL = Hybrids.unaryCFI(ops,
			FruitOp.class, Lemon.class, l);
		assertSame(unaryHybridCFILL.getClass(), UnaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#unaryCFI(OpEnvironment, Class, Object, Object, Object...)}
	 * (i.e.: with the output and input speCFIfied).
	 */
	@Test
	public void testUnaryHybridCFIOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final UnaryHybridCFI<Apple, Apple> unaryHybridCFIAA = Hybrids.unaryCFI(ops,
			FruitOp.class, a, a);
		assertSame(unaryHybridCFIAA.getClass(), UnaryHybridCFIAA.class);

		final UnaryHybridCFI<Orange, Orange> unaryHybridCFIOO = Hybrids.unaryCFI(ops,
			FruitOp.class, o, o);
		assertSame(unaryHybridCFIOO.getClass(), UnaryHybridCFIOO.class);

		final UnaryHybridCFI<Lemon, Lemon> unaryHybridCFILL = Hybrids.unaryCFI(ops,
			FruitOp.class, l, l);
		assertSame(unaryHybridCFILL.getClass(), UnaryHybridCFILL.class);
	}

	/**
	 * Tests {@link Inplaces#unary(OpEnvironment, Class, Class, Object...)} (i.e.:
	 * without the argument specified). UnaryHybridCI/CFI should be matched if
	 * types are matched and it has higher priority. 
	 */
	@Test
	public void testInplace() {
		final UnaryInplaceOp<? super Apple, Apple> inplaceA = Inplaces.unary(ops, FruitOp.class,
			Apple.class, String.class);
		assertSame(inplaceA.getClass(), InplaceA.class);

		final UnaryInplaceOp<? super Orange, Orange> inplaceO = Inplaces.unary(ops, FruitOp.class,
			Orange.class, String.class);
		assertSame(inplaceO.getClass(), UnaryHybridCIOO.class);

		final UnaryInplaceOp<? super Lemon, Lemon> inplaceL = Inplaces.unary(ops, FruitOp.class,
			Lemon.class, String.class);
		assertSame(inplaceL.getClass(), UnaryHybridCFILL.class);
	}

	/**
	 * Tests {@link Inplaces#unary(OpEnvironment, Class, Object, Object...)}
	 * (i.e.: with the argument specified). UnaryHybridCI/CFI should be matched if
	 * types are matched and it has higher priority.
	 */
	@Test
	public void testInplaceIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();
		final String foo = "optional parameter placeholder";

		final UnaryInplaceOp<? super Apple, Apple> inplaceA = Inplaces.unary(ops,
			FruitOp.class, a, foo);
		assertSame(inplaceA.getClass(), InplaceA.class);

		final UnaryInplaceOp<? super Orange, Orange> inplaceO = Inplaces.unary(ops,
			FruitOp.class, o, foo);
		assertSame(inplaceO.getClass(), UnaryHybridCIOO.class);

		final UnaryInplaceOp<? super Lemon, Lemon> inplaceL = Inplaces.unary(ops,
			FruitOp.class, l, foo);
		assertSame(inplaceL.getClass(), UnaryHybridCFILL.class);
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
	 * {@link Hybrids#binaryCF(OpEnvironment, Class, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor inputs specified).
	 */
	@Test
	public void testBinaryHybrid() {
		final BinaryHybridCF<Apple, Apple, Lemon> binaryHybridAAL = Hybrids.binaryCF(
			ops, FruitOp.class, Lemon.class, Apple.class, Apple.class);
		assertSame(binaryHybridAAL.getClass(), BinaryHybridAAL.class);

		final BinaryHybridCF<Apple, Orange, Lemon> binaryHybridAOL = Hybrids.binaryCF(
			ops, FruitOp.class, Lemon.class, Apple.class, Orange.class);
		assertSame(binaryHybridAOL.getClass(), BinaryHybridAOL.class);

		final BinaryHybridCF<Orange, Apple, Lemon> binaryHybridOAL = Hybrids.binaryCF(
			ops, FruitOp.class, Lemon.class, Orange.class, Apple.class);
		assertSame(binaryHybridOAL.getClass(), BinaryHybridOAL.class);

		final BinaryHybridCF<Orange, Orange, Lemon> binaryHybridOOL = Hybrids
			.binaryCF(ops, FruitOp.class, Lemon.class, Orange.class, Orange.class);
		assertSame(binaryHybridOOL.getClass(), BinaryHybridOOL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCF(OpEnvironment, Class, Class, Object, Object, Object...)}
	 * (i.e.: with the inputs specified).
	 */
	@Test
	public void testBinaryHybridIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final BinaryHybridCF<Apple, Apple, Lemon> binaryHybridAAL = Hybrids.binaryCF(
			ops, FruitOp.class, Lemon.class, a, a);
		assertSame(binaryHybridAAL.getClass(), BinaryHybridAAL.class);

		final BinaryHybridCF<Apple, Orange, Lemon> binaryHybridAOL = Hybrids.binaryCF(
			ops, FruitOp.class, Lemon.class, a, o);
		assertSame(binaryHybridAOL.getClass(), BinaryHybridAOL.class);

		final BinaryHybridCF<Orange, Apple, Lemon> binaryHybridOAL = Hybrids.binaryCF(
			ops, FruitOp.class, Lemon.class, o, a);
		assertSame(binaryHybridOAL.getClass(), BinaryHybridOAL.class);

		final BinaryHybridCF<Orange, Orange, Lemon> binaryHybridOOL = Hybrids
			.binaryCF(ops, FruitOp.class, Lemon.class, o, o);
		assertSame(binaryHybridOOL.getClass(), BinaryHybridOOL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCF(OpEnvironment, Class, Object, Object, Object, Object...)}
	 * (i.e.: with the output and inputs specified).
	 */
	@Test
	public void testBinaryHybridOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCF<Apple, Apple, Lemon> binaryHybridAAL = Hybrids.binaryCF(ops,
			FruitOp.class, l, a, a);
		assertSame(binaryHybridAAL.getClass(), BinaryHybridAAL.class);

		final BinaryHybridCF<Apple, Orange, Lemon> binaryHybridAOL = Hybrids.binaryCF(ops,
			FruitOp.class, l, a, o);
		assertSame(binaryHybridAOL.getClass(), BinaryHybridAOL.class);

		final BinaryHybridCF<Orange, Apple, Lemon> binaryHybridOAL = Hybrids.binaryCF(ops,
			FruitOp.class, l, o, a);
		assertSame(binaryHybridOAL.getClass(), BinaryHybridOAL.class);

		final BinaryHybridCF<Orange, Orange, Lemon> binaryHybridOOL = Hybrids.binaryCF(ops,
			FruitOp.class, l, o, o);
		assertSame(binaryHybridOOL.getClass(), BinaryHybridOOL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCI1(OpEnvironment, Class, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor inputs specified).
	 */
	@Test
	public void testBinaryHybridCI1() {
		final BinaryHybridCI1<Apple, Orange, Apple> binaryHybridCI1AOA = Hybrids
			.binaryCI1(ops, FruitOp.class, Apple.class, Apple.class, Orange.class);
		assertSame(binaryHybridCI1AOA.getClass(), BinaryHybridCI1AOA.class);

		final BinaryHybridCI1<Orange, Lemon, Orange> binaryHybridCI1OLO = Hybrids
			.binaryCI1(ops, FruitOp.class, Orange.class, Orange.class, Lemon.class);
		assertSame(binaryHybridCI1OLO.getClass(), BinaryHybridCI1OLO.class);

		final BinaryHybridCI1<Lemon, Apple, Lemon> binaryHybridCI1LAL = Hybrids
			.binaryCI1(ops, FruitOp.class, Lemon.class, Lemon.class, Apple.class);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridCI1LAL.getClass(), BinaryHybridCFI1LAL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCI1(OpEnvironment, Class, Class, Object, Object, Object...)}
	 * (i.e.: with the inputs specified).
	 */
	@Test
	public void testBinaryHybridCI1In() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCI1<Apple, Orange, Apple> binaryHybridCI1AOA = Hybrids
			.binaryCI1(ops, FruitOp.class, Apple.class, a, o);
		assertSame(binaryHybridCI1AOA.getClass(), BinaryHybridCI1AOA.class);

		final BinaryHybridCI1<Orange, Lemon, Orange> binaryHybridCI1OLO = Hybrids
			.binaryCI1(ops, FruitOp.class, Orange.class, o, l);
		assertSame(binaryHybridCI1OLO.getClass(), BinaryHybridCI1OLO.class);

		final BinaryHybridCI1<Lemon, Apple, Lemon> binaryHybridCI1LAL = Hybrids
			.binaryCI1(ops, FruitOp.class, Lemon.class, l, a);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridCI1LAL.getClass(), BinaryHybridCFI1LAL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCI1(OpEnvironment, Class, Object, Object, Object, Object...)}
	 * (i.e.: with the output and inputs specified).
	 */
	@Test
	public void testBinaryHybridCI1OutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCI1<Apple, Orange, Apple> binaryHybridAOA = Hybrids
			.binaryCI1(ops, FruitOp.class, a, a, o);
		assertSame(binaryHybridAOA.getClass(), BinaryHybridCI1AOA.class);

		final BinaryHybridCI1<Orange, Lemon, Orange> binaryHybridOLO = Hybrids
			.binaryCI1(ops, FruitOp.class, o, o, l);
		assertSame(binaryHybridOLO.getClass(), BinaryHybridCI1OLO.class);

		final BinaryHybridCI1<Lemon, Apple, Lemon> binaryHybridOAL = Hybrids
			.binaryCI1(ops, FruitOp.class, l, l, a);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridOAL.getClass(), BinaryHybridCFI1LAL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCFI1(OpEnvironment, Class, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor inputs speCFIfied).
	 */
	@Test
	public void testBinaryHybridCFI1() {
		final BinaryHybridCFI1<Apple, Orange, Apple> binaryHybridCFI1AOA = Hybrids
			.binaryCFI1(ops, FruitOp.class, Apple.class, Apple.class, Orange.class);
		assertSame(binaryHybridCFI1AOA.getClass(), BinaryHybridCFI1AOA.class);

		final BinaryHybridCFI1<Orange, Lemon, Orange> binaryHybridCFI1OLO = Hybrids
			.binaryCFI1(ops, FruitOp.class, Orange.class, Orange.class, Lemon.class);
		assertSame(binaryHybridCFI1OLO.getClass(), BinaryHybridCFI1OLO.class);

		final BinaryHybridCFI1<Lemon, Apple, Lemon> binaryHybridCFI1LAL = Hybrids
			.binaryCFI1(ops, FruitOp.class, Lemon.class, Lemon.class, Apple.class);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridCFI1LAL.getClass(), BinaryHybridCFI1LAL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCFI1(OpEnvironment, Class, Class, Object, Object, Object...)}
	 * (i.e.: with the inputs speCFIfied).
	 */
	@Test
	public void testBinaryHybridCFI1In() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCFI1<Apple, Orange, Apple> binaryHybridCFI1AOA = Hybrids
			.binaryCFI1(ops, FruitOp.class, Apple.class, a, o);
		assertSame(binaryHybridCFI1AOA.getClass(), BinaryHybridCFI1AOA.class);

		final BinaryHybridCFI1<Orange, Lemon, Orange> binaryHybridCFI1OLO = Hybrids
			.binaryCFI1(ops, FruitOp.class, Orange.class, o, l);
		assertSame(binaryHybridCFI1OLO.getClass(), BinaryHybridCFI1OLO.class);

		final BinaryHybridCFI1<Lemon, Apple, Lemon> binaryHybridCFI1LAL = Hybrids
			.binaryCFI1(ops, FruitOp.class, Lemon.class, l, a);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridCFI1LAL.getClass(), BinaryHybridCFI1LAL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCFI1(OpEnvironment, Class, Object, Object, Object, Object...)}
	 * (i.e.: with the output and inputs speCFIfied).
	 */
	@Test
	public void testBinaryHybridCFI1OutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCFI1<Apple, Orange, Apple> binaryHybridAOA = Hybrids
			.binaryCFI1(ops, FruitOp.class, a, a, o);
		assertSame(binaryHybridAOA.getClass(), BinaryHybridCFI1AOA.class);

		final BinaryHybridCFI1<Orange, Lemon, Orange> binaryHybridOLO = Hybrids
			.binaryCFI1(ops, FruitOp.class, o, o, l);
		assertSame(binaryHybridOLO.getClass(), BinaryHybridCFI1OLO.class);

		final BinaryHybridCFI1<Lemon, Apple, Lemon> binaryHybridOAL = Hybrids
			.binaryCFI1(ops, FruitOp.class, l, l, a);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridOAL.getClass(), BinaryHybridCFI1LAL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCI(OpEnvironment, Class, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor inputs specified).
	 */
	@Test
	public void testBinaryHybridCI() {
		final BinaryHybridCI<Apple, Apple> binaryHybridCIAA = Hybrids
			.binaryCI(ops, FruitOp.class, Apple.class, Apple.class);
		assertSame(binaryHybridCIAA.getClass(), BinaryHybridCIAA.class);

		final BinaryHybridCI<Orange, Orange> binaryHybridCIOO = Hybrids
			.binaryCI(ops, FruitOp.class, Orange.class, Orange.class);
		assertSame(binaryHybridCIOO.getClass(), BinaryHybridCIOO.class);

		final BinaryHybridCI<Lemon, Lemon> binaryHybridCILL = Hybrids
			.binaryCI(ops, FruitOp.class, Lemon.class, Lemon.class);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridCILL.getClass(), BinaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCI(OpEnvironment, Class, Class, Object, Object, Object...)}
	 * (i.e.: with the inputs specified).
	 */
	@Test
	public void testBinaryHybridCIIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCI<Apple, Apple> binaryHybridCIAA = Hybrids
			.binaryCI(ops, FruitOp.class, Apple.class, a, a);
		assertSame(binaryHybridCIAA.getClass(), BinaryHybridCIAA.class);

		final BinaryHybridCI<Orange, Orange> binaryHybridCIOO = Hybrids
			.binaryCI(ops, FruitOp.class, Orange.class, o, o);
		assertSame(binaryHybridCIOO.getClass(), BinaryHybridCIOO.class);

		final BinaryHybridCI<Lemon, Lemon> binaryHybridCILL = Hybrids
			.binaryCI(ops, FruitOp.class, Lemon.class, l, l);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridCILL.getClass(), BinaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCI(OpEnvironment, Class, Object, Object, Object, Object...)}
	 * (i.e.: with the output and inputs specified).
	 */
	@Test
	public void testBinaryHybridCIOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCI<Apple, Apple> binaryHybridAA = Hybrids
			.binaryCI(ops, FruitOp.class, a, a, a);
		assertSame(binaryHybridAA.getClass(), BinaryHybridCIAA.class);

		final BinaryHybridCI<Orange, Orange> binaryHybridOO = Hybrids
			.binaryCI(ops, FruitOp.class, o, o, o);
		assertSame(binaryHybridOO.getClass(), BinaryHybridCIOO.class);

		final BinaryHybridCI<Lemon, Lemon> binaryHybridOAL = Hybrids
			.binaryCI(ops, FruitOp.class, l, l, l);
		// CFI is expected because of its higher priority, which is for inplace test
		assertSame(binaryHybridOAL.getClass(), BinaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCFI(OpEnvironment, Class, Class, Class, Class, Object...)}
	 * (i.e.: with neither output nor inputs speCFIfied).
	 */
	@Test
	public void testBinaryHybridCFI() {
		final BinaryHybridCFI<Apple, Apple> binaryHybridCFIAA = Hybrids
			.binaryCFI(ops, FruitOp.class, Apple.class, Apple.class);
		assertSame(binaryHybridCFIAA.getClass(), BinaryHybridCFIAA.class);

		final BinaryHybridCFI<Orange, Orange> binaryHybridCFIOO = Hybrids
			.binaryCFI(ops, FruitOp.class, Orange.class, Orange.class);
		assertSame(binaryHybridCFIOO.getClass(), BinaryHybridCFIOO.class);

		final BinaryHybridCFI<Lemon, Lemon> binaryHybridCFILL = Hybrids
			.binaryCFI(ops, FruitOp.class, Lemon.class, Lemon.class);
		assertSame(binaryHybridCFILL.getClass(), BinaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCFI(OpEnvironment, Class, Class, Object, Object, Object...)}
	 * (i.e.: with the inputs speCFIfied).
	 */
	@Test
	public void testBinaryHybridCFIIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCFI<Apple, Apple> binaryHybridCFIAA = Hybrids
			.binaryCFI(ops, FruitOp.class, Apple.class, a, a);
		assertSame(binaryHybridCFIAA.getClass(), BinaryHybridCFIAA.class);

		final BinaryHybridCFI<Orange, Orange> binaryHybridCFIOO = Hybrids
			.binaryCFI(ops, FruitOp.class, Orange.class, o, o);
		assertSame(binaryHybridCFIOO.getClass(), BinaryHybridCFIOO.class);

		final BinaryHybridCFI<Lemon, Lemon> binaryHybridCFILL = Hybrids
			.binaryCFI(ops, FruitOp.class, Lemon.class, l, l);
		assertSame(binaryHybridCFILL.getClass(), BinaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Hybrids#binaryCFI(OpEnvironment, Class, Object, Object, Object, Object...)}
	 * (i.e.: with the output and inputs speCFIfied).
	 */
	@Test
	public void testBinaryHybridCFIOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		final BinaryHybridCFI<Apple, Apple> binaryHybridAA = Hybrids
			.binaryCFI(ops, FruitOp.class, a, a, a);
		assertSame(binaryHybridAA.getClass(), BinaryHybridCFIAA.class);

		final BinaryHybridCFI<Orange, Orange> binaryHybridOO = Hybrids
			.binaryCFI(ops, FruitOp.class, o, o, o);
		assertSame(binaryHybridOO.getClass(), BinaryHybridCFIOO.class);

		final BinaryHybridCFI<Lemon, Lemon> binaryHybridOAL = Hybrids
			.binaryCFI(ops, FruitOp.class, l, l, l);
		assertSame(binaryHybridOAL.getClass(), BinaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Inplaces#binary1(OpEnvironment, Class, Class, Class, Object...)}
	 * (i.e.: without the argument specified).
	 */
	@Test
	public void testBinaryInplace1() {
		// NB: Need "? super Apple" instead of "?" to make javac happy.
		final BinaryInplace1Op<? super Apple, Orange, Apple> binaryInplace1AOA = Inplaces
			.binary1(ops, FruitOp.class, Apple.class, Orange.class);
		assertSame(binaryInplace1AOA.getClass(), BinaryInplace1AO.class);

		// NB: Need "? super Orange" instead of "?" to make javac happy.
		final BinaryInplace1Op<? super Orange, Lemon, Orange> binaryInplace1OLO = Inplaces
			.binary1(ops, FruitOp.class, Orange.class, Lemon.class);
		assertSame(binaryInplace1OLO.getClass(), BinaryHybridCI1OLO.class);

		// NB: Need "? super Lemon" instead of "?" to make javac happy.
		final BinaryInplace1Op<? super Lemon, Apple, Lemon> binaryInplace1LAL = Inplaces
			.binary1(ops, FruitOp.class, Lemon.class, Apple.class);
		assertSame(binaryInplace1LAL.getClass(), BinaryHybridCFI1LAL.class);
	}

	/**
	 * Tests
	 * {@link Inplaces#binary1(OpEnvironment, Class, Object, Object, Object...)}
	 * (i.e.: with the argument specified).
	 */
	@Test
	public void testBinaryInplace1In() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		// NB: Need "? super Apple" instead of "?" to make javac happy.
		final BinaryInplace1Op<? super Apple, Orange, Apple> binaryInplace1AOA =
			Inplaces.binary1(ops, FruitOp.class, a, o);
		assertSame(binaryInplace1AOA.getClass(), BinaryInplace1AO.class);

		// NB: Need "? super Orange" instead of "?" to make javac happy.
		final BinaryInplace1Op<? super Orange, Lemon, Orange> binaryInplace1OLO =
			Inplaces.binary1(ops, FruitOp.class, o, l);
		assertSame(binaryInplace1OLO.getClass(), BinaryHybridCI1OLO.class);

		// NB: Need "? super Lemon" instead of "?" to make javac happy.
		final BinaryInplace1Op<? super Lemon, Apple, Lemon> binaryInplace1LAL =
			Inplaces.binary1(ops, FruitOp.class, l, a);
		assertSame(binaryInplace1LAL.getClass(), BinaryHybridCFI1LAL.class);
	}

	/**
	 * Tests
	 * {@link Inplaces#binary(OpEnvironment, Class, Class, Object...)}
	 * (i.e.: without the argument specified).
	 */
	@Test
	public void testBinaryInplace() {
		// NB: Need "? super Apple" instead of "?" to make javac happy.
		final BinaryInplaceOp<? super Apple, Apple> binaryInplaceA = //
			Inplaces.binary(ops, FruitOp.class, Apple.class);
		assertSame(binaryInplaceA.getClass(), BinaryInplaceA.class);

		// NB: Need "? super Orange" instead of "?" to make javac happy.
		final BinaryInplaceOp<? super Orange, Orange> binaryInplaceO = //
			Inplaces.binary(ops, FruitOp.class, Orange.class);
		assertSame(binaryInplaceO.getClass(), BinaryHybridCIOO.class);

		// NB: Need "? super Lemon" instead of "?" to make javac happy.
		final BinaryInplaceOp<? super Lemon, Lemon> binaryInplaceL = //
			Inplaces.binary(ops, FruitOp.class, Lemon.class);
		assertSame(binaryInplaceL.getClass(), BinaryHybridCFILL.class);
	}

	/**
	 * Tests
	 * {@link Inplaces#binary(OpEnvironment, Class, Object, Object, Object...)}
	 * (i.e.: with the argument specified).
	 */
	@Test
	public void testBinaryInplaceIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();
		final Lemon l = new Lemon();

		// NB: Need "? super Apple" instead of "?" to make javac happy.
		final BinaryInplaceOp<? super Apple, Apple> binaryInplaceA = //
			Inplaces.binary(ops, FruitOp.class, a, a);
		assertSame(binaryInplaceA.getClass(), BinaryInplaceA.class);

		// NB: Need "? super Orange" instead of "?" to make javac happy.
		final BinaryInplaceOp<? super Orange, Orange> binaryInplaceO = //
			Inplaces.binary(ops, FruitOp.class, o, o);
		assertSame(binaryInplaceO.getClass(), BinaryHybridCIOO.class);

		// NB: Need "? super Lemon" instead of "?" to make javac happy.
		final BinaryInplaceOp<? super Lemon, Lemon> binaryInplaceL = //
			Inplaces.binary(ops, FruitOp.class, l, l);
		assertSame(binaryInplaceL.getClass(), BinaryHybridCFILL.class);
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
		public void compute(final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class NullaryFruitFunction<O> extends
		AbstractNullaryFunctionOp<O> implements FruitOp
	{

		@Override
		public O calculate() {
			return null;
		}
	}

	public abstract static class NullaryFruitHybrid<O> extends
		AbstractNullaryHybridCF<O> implements FruitOp
	{

		@Override
		public O createOutput() {
			return null;
		}

		@Override
		public void compute(final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class UnaryFruitComputer<I, O> extends
		AbstractUnaryComputerOp<I, O> implements FruitOp
	{

		@Override
		public void compute(final I in, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class UnaryFruitFunction<I, O> extends
		AbstractUnaryFunctionOp<I, O> implements FruitOp
	{

		@Override
		public O calculate(final I in) {
			return null;
		}
	}

	public abstract static class UnaryFruitHybrid<I, O> extends
		AbstractUnaryHybridCF<I, O> implements FruitOp
	{

		@Override
		public O createOutput(final I input) {
			return null;
		}

		@Override
		public void compute(final I in, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class UnaryFruitHybridCI<I, O extends I> extends
		AbstractUnaryHybridCI<I, O> implements FruitOp
	{

		@Parameter(required = false)
		private String foo;

		@Override
		public void compute(final I in, final O out) {
		// NB: No implementation needed.
		}
	}

	public abstract static class UnaryFruitHybridCFI<I, O extends I> extends
		AbstractUnaryHybridCFI<I, O> implements FruitOp
	{

		@Parameter(required = false)
		private String foo;

		@Override
		public O createOutput(final I input) {
			return null;
		}

		@Override
		public void compute(final I in, final O out) {
		// NB: No implementation needed.
		}
	}

	public abstract static class FruitInplace<A> extends AbstractUnaryInplaceOp<A>
		implements FruitOp
	{

		@Parameter(required = false)
		private String foo;

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
		priority = Priority.HIGH)
	public static class NullaryFunctionA extends NullaryFruitFunction<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryFunctionO")
	public static class NullaryFunctionO extends NullaryFruitFunction<Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryHybridA",
		priority = Priority.LOW + 1)
	public static class NullaryHybridA extends NullaryFruitHybrid<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.nullaryHybridO",
		priority = Priority.LOW + 1)
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
		priority = Priority.HIGH)
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
		priority = Priority.HIGH)
	public static class UnaryFunctionOO extends UnaryFruitFunction<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridAA",
		priority = Priority.LOW)
	public static class UnaryHybridAA extends UnaryFruitHybrid<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridAO",
		priority = Priority.LOW)
	public static class UnaryHybridAO extends UnaryFruitHybrid<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridOA",
		priority = Priority.LOW)
	public static class UnaryHybridOA extends UnaryFruitHybrid<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridOO",
		priority = Priority.LOW)
	public static class UnaryHybridOO extends UnaryFruitHybrid<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridCIAA",
		priority = Priority.VERY_LOW + 20)
	public static class UnaryHybridCIAA extends UnaryFruitHybridCI<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridCIOO",
		priority = Priority.VERY_LOW + 21)
	public static class UnaryHybridCIOO extends UnaryFruitHybridCI<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridCILL",
		priority = Priority.VERY_LOW + 20)
	public static class UnaryHybridCILL extends UnaryFruitHybridCI<Lemon, Lemon> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridCFIAA",
		priority = Priority.VERY_LOW + 15)
	public static class UnaryHybridCFIAA extends UnaryFruitHybridCFI<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridCFIOO",
		priority = Priority.VERY_LOW + 15)
	public static class UnaryHybridCFIOO extends UnaryFruitHybridCFI<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.unaryHybridCFILL",
		priority = Priority.VERY_LOW + 21)
	public static class UnaryHybridCFILL extends UnaryFruitHybridCFI<Lemon, Lemon> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.inplaceA",
		priority = Priority.VERY_LOW + 21)
	public static class InplaceA extends FruitInplace<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.inplaceO",
		priority = Priority.VERY_LOW + 20)
	public static class InplaceO extends FruitInplace<Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.inplaceL",
		priority = Priority.VERY_LOW + 20)
	public static class InplaceL extends FruitInplace<Lemon> {
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
		public void compute(final I1 in1, final I2 in2, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class BinaryFruitFunction<I1, I2, O> extends
		AbstractBinaryFunctionOp<I1, I2, O> implements FruitOp
	{

		@Override
		public O calculate(final I1 in1, final I2 in2) {
			return null;
		}
	}

	public abstract static class BinaryFruitHybrid<I1, I2, O> extends
		AbstractBinaryHybridCF<I1, I2, O> implements FruitOp
	{

		@Override
		public O createOutput(final I1 in1, final I2 in2) {
			return null;
		}

		@Override
		public void compute(final I1 in1, final I2 in2, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class BinaryFruitHybridCI1<I1, I2, O extends I1>
		extends AbstractBinaryHybridCI1<I1, I2, O> implements FruitOp
	{

		@Override
		public void compute(final I1 in1, final I2 in2, final O out) {
			// NB: No implementation needed.
		}

		@Override
		public void mutate1(final O arg, final I2 in) {
			// NB: No implementation needed.
		}
	}

	public abstract static class BinaryFruitHybridCFI1<I1, I2, O extends I1>
		extends AbstractBinaryHybridCFI1<I1, I2, O> implements FruitOp
	{

		@Override
		public O createOutput(final I1 in1, final I2 in2) {
			return null;
		}

		@Override
		public void compute(final I1 in1, final I2 in2, final O out) {
			// NB: No implementation needed.
		}

		@Override
		public void mutate1(final O arg, final I2 in) {
			// NB: No implementation needed.
		}

	}

	public abstract static class BinaryFruitHybridCI<I, O extends I> extends
		AbstractBinaryHybridCI<I, O> implements FruitOp
	{

		@Override
		public void compute(final I in1, final I in2, final O out) {
			// NB: No implementation needed.
		}

		@Override
		public void mutate1(final O arg, final I in) {
			// NB: No implementation needed.
		}

		@Override
		public void mutate2(final I in, final O arg) {
			// NB: No implementation needed.
		}
	}

	public abstract static class BinaryFruitHybridCFI<I, O extends I> extends
		AbstractBinaryHybridCFI<I, O> implements FruitOp
	{

		@Override
		public O createOutput(final I in1, final I in2) {
			return null;
		}

		@Override
		public void compute(final I in1, final I in2, final O out) {
			// NB: No implementation needed.
		}

		@Override
		public void mutate1(final O arg, final I in) {
			// NB: No implementation needed.
		}

		@Override
		public void mutate2(final I in, final O arg) {
			// NB: No implementation needed.
		}
	}

	public abstract static class BinaryFruitInplace1<A, I> extends
		AbstractBinaryInplace1Op<A, I> implements FruitOp
	{

		@Override
		public void mutate1(final A arg, final I in) {
			// NB: No implementation needed.
		}
	}

	public abstract static class BinaryFruitInplace<A> extends
		AbstractBinaryInplaceOp<A> implements FruitOp
	{

		@Override
		public void mutate1(final A arg, final A in) {
			// NB: No implementation needed.
		}

		@Override
		public void mutate2(final A in, final A arg) {
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
		priority = Priority.HIGH)
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
		priority = Priority.HIGH)
	public static class BinaryFunctionOOL extends
		BinaryFruitFunction<Orange, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridAAL",
		priority = Priority.VERY_LOW)
	public static class BinaryHybridAAL extends
		BinaryFruitHybrid<Apple, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridAOL",
		priority = Priority.VERY_LOW)
	public static class BinaryHybridAOL extends
		BinaryFruitHybrid<Apple, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridOAL",
		priority = Priority.VERY_LOW)
	public static class BinaryHybridOAL extends
		BinaryFruitHybrid<Orange, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridOOL",
		priority = Priority.VERY_LOW)
	public static class BinaryHybridOOL extends
		BinaryFruitHybrid<Orange, Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCI1AOA",
		priority = Priority.VERY_LOW + 10)
	public static class BinaryHybridCI1AOA extends
		BinaryFruitHybridCI1<Apple, Orange, Apple>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCI1OLO",
		priority = Priority.VERY_LOW + 11)
	public static class BinaryHybridCI1OLO extends
		BinaryFruitHybridCI1<Orange, Lemon, Orange>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCI1LAL",
		priority = Priority.VERY_LOW + 10)
	public static class BinaryHybridCI1LAL extends
		BinaryFruitHybridCI1<Lemon, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCFI1AOA",
		priority = Priority.VERY_LOW + 5)
	public static class BinaryHybridCFI1AOA extends
		BinaryFruitHybridCFI1<Apple, Orange, Apple>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCFI1OLO",
		priority = Priority.VERY_LOW + 5)
	public static class BinaryHybridCFI1OLO extends
		BinaryFruitHybridCFI1<Orange, Lemon, Orange>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCFI1LAL",
		priority = Priority.VERY_LOW + 11)
	public static class BinaryHybridCFI1LAL extends
		BinaryFruitHybridCFI1<Lemon, Apple, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCIAA",
		priority = Priority.VERY_LOW + 5)
	public static class BinaryHybridCIAA extends
		BinaryFruitHybridCI<Apple, Apple>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCIOO",
		priority = Priority.VERY_LOW + 11)
	public static class BinaryHybridCIOO extends
		BinaryFruitHybridCI<Orange, Orange>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCILL",
		priority = Priority.VERY_LOW + 5)
	public static class BinaryHybridCILL extends
		BinaryFruitHybridCI<Lemon, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCFIAA",
		priority = Priority.VERY_LOW)
	public static class BinaryHybridCFIAA extends
		BinaryFruitHybridCFI<Apple, Apple>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCFIOO",
		priority = Priority.VERY_LOW)
	public static class BinaryHybridCFIOO extends
		BinaryFruitHybridCFI<Orange, Orange>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryHybridCFILL",
		priority = Priority.VERY_LOW + 11)
	public static class BinaryHybridCFILL extends
		BinaryFruitHybridCFI<Lemon, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryInplace1AO",
		priority = Priority.VERY_LOW + 11)
	public static class BinaryInplace1AO extends
		BinaryFruitInplace1<Apple, Orange>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryInplace1OL",
		priority = Priority.VERY_LOW + 5)
	public static class BinaryInplace1OL extends
		BinaryFruitInplace1<Orange, Lemon>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryInplace1LA",
		priority = Priority.VERY_LOW + 5)
	public static class BinaryInplace1LA extends
		BinaryFruitInplace1<Lemon, Apple>
	{
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryInplaceA",
		priority = Priority.VERY_LOW + 11)
	public static class BinaryInplaceA extends BinaryFruitInplace<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryInplaceO",
		priority = Priority.VERY_LOW)
	public static class BinaryInplaceO extends BinaryFruitInplace<Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.binaryInplaceL",
		priority = Priority.VERY_LOW)
	public static class BinaryInplaceL extends BinaryFruitInplace<Lemon> {
		// NB: No implementation needed.
	}

}
