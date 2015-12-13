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
			ops.computer(SpecialOp.class, Apple.class, Apple.class);
		assertSame(computerAA.getClass(), ComputerAA.class);

		final ComputerOp<Apple, Orange> computerAO =
			ops.computer(SpecialOp.class, Orange.class, Apple.class);
		assertSame(computerAO.getClass(), ComputerAO.class);

		final ComputerOp<Orange, Apple> computerOA =
			ops.computer(SpecialOp.class, Apple.class, Orange.class);
		assertSame(computerOA.getClass(), ComputerOA.class);

		final ComputerOp<Orange, Orange> computerOO =
			ops.computer(SpecialOp.class, Orange.class, Orange.class);
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
			ops.computer(SpecialOp.class, Apple.class, a);
		assertSame(computerAA.getClass(), ComputerAA.class);

		final ComputerOp<Apple, Orange> computerAO =
			ops.computer(SpecialOp.class, Orange.class, a);
		assertSame(computerAO.getClass(), ComputerAO.class);

		final ComputerOp<Orange, Apple> computerOA =
			ops.computer(SpecialOp.class, Apple.class, o);
		assertSame(computerOA.getClass(), ComputerOA.class);

		final ComputerOp<Orange, Orange> computerOO =
			ops.computer(SpecialOp.class, Orange.class, o);
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
			ops.computer(SpecialOp.class, a, a);
		assertSame(computerAA.getClass(), ComputerAA.class);

		final ComputerOp<Apple, Orange> computerAO =
			ops.computer(SpecialOp.class, o, a);
		assertSame(computerAO.getClass(), ComputerAO.class);

		final ComputerOp<Orange, Apple> computerOA =
			ops.computer(SpecialOp.class, a, o);
		assertSame(computerOA.getClass(), ComputerOA.class);

		final ComputerOp<Orange, Orange> computerOO =
			ops.computer(SpecialOp.class, o, o);
		assertSame(computerOO.getClass(), ComputerOO.class);
	}

	/**
	 * Tests {@link OpService#function(Class, Class, Class, Object...)} (i.e.:
	 * without the input specified).
	 */
	@Test
	public void testFunction() {
		final FunctionOp<Apple, Apple> functionAA =
			ops.function(SpecialOp.class, Apple.class, Apple.class);
		assertSame(functionAA.getClass(), FunctionAA.class);

		final FunctionOp<Apple, Orange> functionAO =
			ops.function(SpecialOp.class, Orange.class, Apple.class);
		assertSame(functionAO.getClass(), FunctionAO.class);

		final FunctionOp<Orange, Apple> functionOA =
			ops.function(SpecialOp.class, Apple.class, Orange.class);
		assertSame(functionOA.getClass(), FunctionOA.class);

		final FunctionOp<Orange, Orange> functionOO =
			ops.function(SpecialOp.class, Orange.class, Orange.class);
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
			ops.function(SpecialOp.class, Apple.class, a);
		assertSame(functionAA.getClass(), FunctionAA.class);

		final FunctionOp<Apple, Orange> functionAO =
			ops.function(SpecialOp.class, Orange.class, a);
		assertSame(functionAO.getClass(), FunctionAO.class);

		final FunctionOp<Orange, Apple> functionOA =
			ops.function(SpecialOp.class, Apple.class, o);
		assertSame(functionOA.getClass(), FunctionOA.class);

		final FunctionOp<Orange, Orange> functionOO =
			ops.function(SpecialOp.class, Orange.class, o);
		assertSame(functionOO.getClass(), FunctionOO.class);
	}

	/**
	 * Tests {@link OpService#hybrid(Class, Class, Class, Object...)} (i.e.: with
	 * neither output nor input specified).
	 */
	@Test
	public void testHybrid() {
		final ComputerOp<Apple, Apple> computerAA =
			ops.computer(SpecialOp.class, Apple.class, Apple.class);
		assertSame(computerAA.getClass(), ComputerAA.class);

		final ComputerOp<Apple, Orange> computerAO =
			ops.computer(SpecialOp.class, Orange.class, Apple.class);
		assertSame(computerAO.getClass(), ComputerAO.class);

		final ComputerOp<Orange, Apple> computerOA =
			ops.computer(SpecialOp.class, Apple.class, Orange.class);
		assertSame(computerOA.getClass(), ComputerOA.class);

		final ComputerOp<Orange, Orange> computerOO =
			ops.computer(SpecialOp.class, Orange.class, Orange.class);
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
			ops.hybrid(SpecialOp.class, Apple.class, a);
		assertSame(hybridAA.getClass(), HybridAA.class);

		final HybridOp<Apple, Orange> hybridAO =
			ops.hybrid(SpecialOp.class, Orange.class, a);
		assertSame(hybridAO.getClass(), HybridAO.class);

		final HybridOp<Orange, Apple> hybridOA =
			ops.hybrid(SpecialOp.class, Apple.class, o);
		assertSame(hybridOA.getClass(), HybridOA.class);

		final HybridOp<Orange, Orange> hybridOO =
			ops.hybrid(SpecialOp.class, Orange.class, o);
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
			ops.hybrid(SpecialOp.class, a, a);
		assertSame(hybridAA.getClass(), HybridAA.class);

		final HybridOp<Apple, Orange> hybridAO =
			ops.hybrid(SpecialOp.class, o, a);
		assertSame(hybridAO.getClass(), HybridAO.class);

		final HybridOp<Orange, Apple> hybridOA =
			ops.hybrid(SpecialOp.class, a, o);
		assertSame(hybridOA.getClass(), HybridOA.class);

		final HybridOp<Orange, Orange> hybridOO =
			ops.hybrid(SpecialOp.class, o, o);
		assertSame(hybridOO.getClass(), HybridOO.class);
	}

	/**
	 * Tests {@link OpService#inplace(Class, Class, Object...)} (i.e.: without the
	 * argument specified).
	 */
	@Test
	public void testInplace() {
		final InplaceOp<Apple> inplaceA = ops.inplace(SpecialOp.class, Apple.class);
		assertSame(inplaceA.getClass(), InplaceA.class);

		final InplaceOp<Orange> inplaceO =
			ops.inplace(SpecialOp.class, Orange.class);
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

		final InplaceOp<Apple> inplaceA = ops.inplace(SpecialOp.class, a);
		assertSame(inplaceA.getClass(), InplaceA.class);

		final InplaceOp<Orange> inplaceO = ops.inplace(SpecialOp.class, o);
		assertSame(inplaceO.getClass(), InplaceO.class);
	}

	// -- Helper classes --

	public static class Apple {
		// NB: No implementation needed.
	}

	public static class Orange {
		// NB: No implementation needed.
	}

	public static interface SpecialOp extends Op {
		// NB: Marker interface.
	}

	public abstract static class SpecialComputer<I, O> extends
		AbstractComputerOp<I, O> implements SpecialOp
	{

		@Override
		public void compute(final I in, final O out) {
			// NB: No implementation needed.
		}
	}

	public abstract static class SpecialFunction<I, O> extends
		AbstractFunctionOp<I, O> implements SpecialOp
	{

		@Override
		public O compute(final I in) {
			return null;
		}
	}

	public abstract static class SpecialHybrid<I, O> extends
		AbstractHybridOp<I, O> implements SpecialOp
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

	public abstract static class SpecialInplace<A> extends AbstractInplaceOp<A>
		implements SpecialOp
	{

		@Override
		public void compute(final A arg) {
			// NB: No implementation needed.
		}
	}

	public static class AbstractSpecialOp extends NoOp implements SpecialOp {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.computerAA")
	public static class ComputerAA extends SpecialComputer<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.computerAO")
	public static class ComputerAO extends SpecialComputer<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.computerOA")
	public static class ComputerOA extends SpecialComputer<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.computerOO")
	public static class ComputerOO extends SpecialComputer<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.functionAA",
		priority = Priority.HIGH_PRIORITY)
	public static class FunctionAA extends SpecialFunction<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.functionAO")
	public static class FunctionAO extends SpecialFunction<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.functionOA")
	public static class FunctionOA extends SpecialFunction<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.functionOO",
		priority = Priority.HIGH_PRIORITY)
	public static class FunctionOO extends SpecialFunction<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.hybridAA",
		priority = Priority.LOW_PRIORITY)
	public static class HybridAA extends SpecialHybrid<Apple, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.hybridAO",
		priority = Priority.LOW_PRIORITY)
	public static class HybridAO extends SpecialHybrid<Apple, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.hybridOA",
		priority = Priority.LOW_PRIORITY)
	public static class HybridOA extends SpecialHybrid<Orange, Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.hybridOO",
		priority = Priority.LOW_PRIORITY)
	public static class HybridOO extends SpecialHybrid<Orange, Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.inplaceA",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class InplaceA extends SpecialInplace<Apple> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.inplaceO",
		priority = Priority.VERY_LOW_PRIORITY)
	public static class InplaceO extends SpecialInplace<Orange> {
		// NB: No implementation needed.
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeComputerAA")
	public static class FakeComputerAA extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH)
		private Apple o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeComputerAO")
	public static class FakeComputerAO extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH)
		private Orange o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeComputerOA")
	public static class FakeComputerOA extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH)
		private Apple o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeComputerOO")
	public static class FakeComputerOO extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH)
		private Orange o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeFunctionAA")
	public static class FakeFunctionAA extends AbstractSpecialOp {

		@Parameter(type = ItemIO.OUTPUT)
		private Apple o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeFunctionAO")
	public static class FakeFunctionAO extends AbstractSpecialOp {

		@Parameter(type = ItemIO.OUTPUT)
		private Orange o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeFunctionOA")
	public static class FakeFunctionOA extends AbstractSpecialOp {

		@Parameter(type = ItemIO.OUTPUT)
		private Apple o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeFunctionOO")
	public static class FakeFunctionOO extends AbstractSpecialOp {

		@Parameter(type = ItemIO.OUTPUT)
		private Orange o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeHybridAA")
	public static class FakeHybridAA extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH, required = false)
		private Apple o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeHybridAO")
	public static class FakeHybridAO extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH, required = false)
		private Orange o;
		@Parameter
		private Apple i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeHybridOA")
	public static class FakeHybridOA extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH, required = false)
		private Apple o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeHybridOO")
	public static class FakeHybridOO extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH, required = false)
		private Orange o;
		@Parameter
		private Orange i;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeInplaceD")
	public static class FakeInplaceA extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH)
		private Apple a;
	}

	@Plugin(type = SpecialOp.class, name = "test.fakeInplaceS")
	public static class FakeInplaceO extends AbstractSpecialOp {

		@Parameter(type = ItemIO.BOTH)
		private Orange a;
	}

}
