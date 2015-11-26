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

import static org.junit.Assert.assertSame;

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
	 * Tests {@link OpService#computer(Class, Class, Class, Object...)} (i.e.:
	 * with neither output nor input specified).
	 */
	@Test
	public void testComputer() {
		final ComputerOp<Apple, Apple> computerAA =
			ops.computer(FruitOp.class, Apple.class, Apple.class);
		assertSame(computerAA.getClass(), ComputerAA.class);

		final ComputerOp<Apple, Orange> computerAO =
			ops.computer(FruitOp.class, Orange.class, Apple.class);
		assertSame(computerAO.getClass(), ComputerAO.class);

		final ComputerOp<Orange, Apple> computerOA =
			ops.computer(FruitOp.class, Apple.class, Orange.class);
		assertSame(computerOA.getClass(), ComputerOA.class);

		final ComputerOp<Orange, Orange> computerOO =
			ops.computer(FruitOp.class, Orange.class, Orange.class);
		assertSame(computerOO.getClass(), ComputerOO.class);
	}

	/**
	 * Tests {@link OpService#computer(Class, Class, Object, Object...)} (i.e.:
	 * with the input specified).
	 */
	@Test
	public void testComputerIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final ComputerOp<Apple, Apple> computerAA =
			ops.computer(FruitOp.class, Apple.class, a);
		assertSame(computerAA.getClass(), ComputerAA.class);

		final ComputerOp<Apple, Orange> computerAO =
			ops.computer(FruitOp.class, Orange.class, a);
		assertSame(computerAO.getClass(), ComputerAO.class);

		final ComputerOp<Orange, Apple> computerOA =
			ops.computer(FruitOp.class, Apple.class, o);
		assertSame(computerOA.getClass(), ComputerOA.class);

		final ComputerOp<Orange, Orange> computerOO =
			ops.computer(FruitOp.class, Orange.class, o);
		assertSame(computerOO.getClass(), ComputerOO.class);
	}

	/**
	 * Tests {@link OpService#computer(Class, Object, Object, Object...)} (i.e.:
	 * with the output and input specified).
	 */
	@Test
	public void testComputerOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final ComputerOp<Apple, Apple> computerAA =
			ops.computer(FruitOp.class, a, a);
		assertSame(computerAA.getClass(), ComputerAA.class);

		final ComputerOp<Apple, Orange> computerAO =
			ops.computer(FruitOp.class, o, a);
		assertSame(computerAO.getClass(), ComputerAO.class);

		final ComputerOp<Orange, Apple> computerOA =
			ops.computer(FruitOp.class, a, o);
		assertSame(computerOA.getClass(), ComputerOA.class);

		final ComputerOp<Orange, Orange> computerOO =
			ops.computer(FruitOp.class, o, o);
		assertSame(computerOO.getClass(), ComputerOO.class);
	}

	/**
	 * Tests {@link OpService#function(Class, Class, Class, Object...)} (i.e.:
	 * without the input specified).
	 */
	@Test
	public void testFunction() {
		final FunctionOp<Apple, Apple> functionAA =
			ops.function(FruitOp.class, Apple.class, Apple.class);
		assertSame(functionAA.getClass(), FunctionAA.class);

		final FunctionOp<Apple, Orange> functionAO =
			ops.function(FruitOp.class, Orange.class, Apple.class);
		assertSame(functionAO.getClass(), FunctionAO.class);

		final FunctionOp<Orange, Apple> functionOA =
			ops.function(FruitOp.class, Apple.class, Orange.class);
		assertSame(functionOA.getClass(), FunctionOA.class);

		final FunctionOp<Orange, Orange> functionOO =
			ops.function(FruitOp.class, Orange.class, Orange.class);
		assertSame(functionOO.getClass(), FunctionOO.class);
	}

	/**
	 * Tests {@link OpService#function(Class, Class, Object, Object...)} (i.e.:
	 * with the input specified).
	 */
	@Test
	public void testFunctionIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final FunctionOp<Apple, Apple> functionAA =
			ops.function(FruitOp.class, Apple.class, a);
		assertSame(functionAA.getClass(), FunctionAA.class);

		final FunctionOp<Apple, Orange> functionAO =
			ops.function(FruitOp.class, Orange.class, a);
		assertSame(functionAO.getClass(), FunctionAO.class);

		final FunctionOp<Orange, Apple> functionOA =
			ops.function(FruitOp.class, Apple.class, o);
		assertSame(functionOA.getClass(), FunctionOA.class);

		final FunctionOp<Orange, Orange> functionOO =
			ops.function(FruitOp.class, Orange.class, o);
		assertSame(functionOO.getClass(), FunctionOO.class);
	}

	/**
	 * Tests {@link OpService#hybrid(Class, Class, Class, Object...)} (i.e.: with
	 * neither output nor input specified).
	 */
	@Test
	public void testHybrid() {
		final ComputerOp<Apple, Apple> computerAA =
			ops.computer(FruitOp.class, Apple.class, Apple.class);
		assertSame(computerAA.getClass(), ComputerAA.class);

		final ComputerOp<Apple, Orange> computerAO =
			ops.computer(FruitOp.class, Orange.class, Apple.class);
		assertSame(computerAO.getClass(), ComputerAO.class);

		final ComputerOp<Orange, Apple> computerOA =
			ops.computer(FruitOp.class, Apple.class, Orange.class);
		assertSame(computerOA.getClass(), ComputerOA.class);

		final ComputerOp<Orange, Orange> computerOO =
			ops.computer(FruitOp.class, Orange.class, Orange.class);
		assertSame(computerOO.getClass(), ComputerOO.class);
	}

	/**
	 * Tests {@link OpService#hybrid(Class, Class, Object, Object...)} (i.e.: with
	 * the input specified).
	 */
	@Test
	public void testHybridIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final HybridOp<Apple, Apple> hybridAA =
			ops.hybrid(FruitOp.class, Apple.class, a);
		assertSame(hybridAA.getClass(), HybridAA.class);

		final HybridOp<Apple, Orange> hybridAO =
			ops.hybrid(FruitOp.class, Orange.class, a);
		assertSame(hybridAO.getClass(), HybridAO.class);

		final HybridOp<Orange, Apple> hybridOA =
			ops.hybrid(FruitOp.class, Apple.class, o);
		assertSame(hybridOA.getClass(), HybridOA.class);

		final HybridOp<Orange, Orange> hybridOO =
			ops.hybrid(FruitOp.class, Orange.class, o);
		assertSame(hybridOO.getClass(), HybridOO.class);
	}

	/**
	 * Tests {@link OpService#hybrid(Class, Object, Object, Object...)} (i.e.:
	 * with the output and input specified).
	 */
	@Test
	public void testHybridOutIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final HybridOp<Apple, Apple> hybridAA =
			ops.hybrid(FruitOp.class, a, a);
		assertSame(hybridAA.getClass(), HybridAA.class);

		final HybridOp<Apple, Orange> hybridAO =
			ops.hybrid(FruitOp.class, o, a);
		assertSame(hybridAO.getClass(), HybridAO.class);

		final HybridOp<Orange, Apple> hybridOA =
			ops.hybrid(FruitOp.class, a, o);
		assertSame(hybridOA.getClass(), HybridOA.class);

		final HybridOp<Orange, Orange> hybridOO =
			ops.hybrid(FruitOp.class, o, o);
		assertSame(hybridOO.getClass(), HybridOO.class);
	}

	/**
	 * Tests {@link OpService#inplace(Class, Class, Object...)} (i.e.: without the
	 * argument specified).
	 */
	@Test
	public void testInplace() {
		final InplaceOp<Apple> inplaceA = ops.inplace(FruitOp.class, Apple.class);
		assertSame(inplaceA.getClass(), InplaceA.class);

		final InplaceOp<Orange> inplaceO =
			ops.inplace(FruitOp.class, Orange.class);
		assertSame(inplaceO.getClass(), InplaceO.class);
	}

	/**
	 * Tests {@link OpService#inplace(Class, Object, Object...)} (i.e.: with the
	 * argument specified).
	 */
	@Test
	public void testInplaceIn() {
		final Apple a = new Apple();
		final Orange o = new Orange();

		final InplaceOp<Apple> inplaceA = ops.inplace(FruitOp.class, a);
		assertSame(inplaceA.getClass(), InplaceA.class);

		final InplaceOp<Orange> inplaceO = ops.inplace(FruitOp.class, o);
		assertSame(inplaceO.getClass(), InplaceO.class);
	}

	// -- Helper classes --

	public static class Apple {
		// NB: No implementation needed.
	}

	public static class Orange {
		// NB: No implementation needed.
	}

	public static interface FruitOp extends Op {
		// NB: Marker interface.
	}

	public abstract static class FruitComputer<I, O> extends
		AbstractComputerOp<I, O> implements FruitOp
	{

		@Override
		public void compute(final I in, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class FruitFunction<I, O> extends
		AbstractFunctionOp<I, O> implements FruitOp
	{

		@Override
		public O compute(final I in) {
			return null;
		}
	}

	public abstract static class FruitHybrid<I, O> extends
		AbstractHybridOp<I, O> implements FruitOp
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

	public abstract static class FruitInplace<A> extends AbstractInplaceOp<A>
		implements FruitOp
	{

		@Override
		public void compute(final A arg) {
			// NB: No implementation needed.
		}
	}

	public static class AbstractFruitOp extends NoOp implements FruitOp {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.computerAA")
	public static class ComputerAA extends FruitComputer<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.computerAO")
	public static class ComputerAO extends FruitComputer<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.computerOA")
	public static class ComputerOA extends FruitComputer<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.computerOO")
	public static class ComputerOO extends FruitComputer<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.functionAA",
		priority = Priority.HIGH_PRIORITY)
	public static class FunctionAA extends FruitFunction<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.functionAO")
	public static class FunctionAO extends FruitFunction<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.functionOA")
	public static class FunctionOA extends FruitFunction<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.functionOO",
		priority = Priority.HIGH_PRIORITY)
	public static class FunctionOO extends FruitFunction<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.hybridAA",
		priority = Priority.LOW_PRIORITY)
	public static class HybridAA extends FruitHybrid<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.hybridAO",
		priority = Priority.LOW_PRIORITY)
	public static class HybridAO extends FruitHybrid<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.hybridOA",
		priority = Priority.LOW_PRIORITY)
	public static class HybridOA extends FruitHybrid<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = FruitOp.class, name = "test.hybridOO",
		priority = Priority.LOW_PRIORITY)
	public static class HybridOO extends FruitHybrid<Orange, Orange> {
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

}
