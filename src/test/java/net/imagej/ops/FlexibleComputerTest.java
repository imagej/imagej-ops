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
import static org.junit.Assert.assertTrue;

import net.imagej.ops.special.AbstractBinaryComputerOp;
import net.imagej.ops.special.AbstractBinaryHybridOp;
import net.imagej.ops.special.AbstractUnaryComputerOp;
import net.imagej.ops.special.AbstractUnaryHybridOp;
import net.imagej.ops.special.BinaryComputerOp;
import net.imagej.ops.special.Computers;
import net.imagej.ops.special.InplaceOp;
import net.imagej.ops.special.UnaryComputerOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

/**
 * Test {@link Computers#compute(UnaryComputerOp, Object, Object)} and
 * {@link Computers#compute(BinaryComputerOp, Object, Object, Object)}
 * 
 * @author Leon Yang
 */
public class FlexibleComputerTest extends AbstractOpTest {

	// --------------------- Unary Computer Tests ---------------------

	@Test
	public void testUnaryAsFunction() {
		final UnaryComputerOp<DoubleType, DoubleType> op = new UnaryAddHybrid();
		final DoubleType input = new DoubleType(10.0);
		final DoubleType result = Computers.compute(op, input, null);
		assertEquals(11.0, result.get(), 0);
	}

	@Test
	public void testUnaryAsInplaceNullOut() {
		final UnaryComputerOp<DoubleType, DoubleType> op = new UnaryAddInplace();
		final DoubleType input = new DoubleType(10.0);
		final DoubleType result = Computers.compute(op, input, null);
		assertTrue(result == input);
		assertEquals(11.0, result.get(), 0.000001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnaryNullOut() {
		final UnaryComputerOp<DoubleType, DoubleType> op = new UnaryAddComputer();
		final DoubleType input = new DoubleType(10.0);
		Computers.compute(op, input, null);
	}

	@Test
	public void testUnaryAsInplaceInEqualsOut() {
		final UnaryComputerOp<DoubleType, DoubleType> op = new UnaryAddInplace();
		final DoubleType input = new DoubleType(10.0);
		final DoubleType result = Computers.compute(op, input, input);
		assertTrue(result == input);
		assertEquals(11.0, result.get(), 0.000001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnaryInEqualsOut() {
		final UnaryComputerOp<DoubleType, DoubleType> op = new UnaryAddComputer();
		final DoubleType input = new DoubleType(10.0);
		Computers.compute(op, input, input);
	}

	@Test
	public void testUnaryNormal() {
		final UnaryComputerOp<DoubleType, DoubleType> op = new UnaryAddComputer();
		final DoubleType input = new DoubleType(10.0);
		final DoubleType output = new DoubleType();
		final DoubleType result = Computers.compute(op, input, output);
		assertTrue(output == result);
		assertEquals(11.0, result.get(), 0.000001);
	}

	// --------------------- Binary Computer Tests ---------------------

	@Test
	public void testBinaryAsUnary() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddComputer();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		op.setInput1(in1);
		op.setInput2(in2);
		final DoubleType output = new DoubleType();
		final DoubleType result = Computers.compute(op, in1, null, output);
		assertTrue(result == output);
		assertEquals(11.0, result.get(), 0.000001);
	}

	@Test
	public void testBinaryAsFunction() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddHybrid();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		final DoubleType result = Computers.compute(op, in1, in2, null);
		assertEquals(11.0, result.get(), 0.000001);
	}

	@Test
	public void testBinaryAsInplaceNullOut() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddInplace();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		final DoubleType result = Computers.compute(op, in1, in2, null);
		assertTrue(result == in1);
		assertEquals(11.0, result.get(), 0.000001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBinaryNullOut() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddComputer();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		Computers.compute(op, in1, in2, null);
	}

	@Test
	public void testBinaryAsInplaceIn1EqualsOut() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddInplace();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		final DoubleType result = Computers.compute(op, in1, in2, in1);
		assertTrue(result == in1);
		assertEquals(11.0, result.get(), 0.000001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBinaryIn1EqualsOut() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddComputer();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		Computers.compute(op, in1, in2, in1);
	}

	@Test
	public void testBinaryAsInplaceIn2EqualsOut() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddInplace();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		final DoubleType result = Computers.compute(op, in1, in2, in2);
		assertTrue(result == in2);
		assertEquals(11.0, result.get(), 0.000001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBinaryIn2EqualsOut() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddComputer();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		Computers.compute(op, in1, in2, in2);
	}

	@Test
	public void testBianryNormal() {
		final BinaryComputerOp<DoubleType, DoubleType, DoubleType> op =
			new BinaryAddComputer();
		final DoubleType in1 = new DoubleType(10.0);
		final DoubleType in2 = new DoubleType(1.0);
		final DoubleType out = new DoubleType();
		final DoubleType result = Computers.compute(op, in1, in2, out);
		assertTrue(out == result);
		assertEquals(11.0, result.get(), 0.000001);
	}

	// --------------------- Unary Helper Classes ---------------------

	public static class UnaryAddHybrid extends
		AbstractUnaryHybridOp<DoubleType, DoubleType>
	{

		@Override
		public void compute1(final DoubleType input, final DoubleType output) {
			output.setReal(input.get() + 1.0);
		}

		@Override
		public DoubleType createOutput(final DoubleType input) {
			return new DoubleType();
		}

	}

	public static class UnaryAddInplace extends
		AbstractUnaryComputerOp<DoubleType, DoubleType> implements
		InplaceOp<DoubleType>
	{

		@Override
		public void run() {
			if (out() == null) mutate(arg());
			else compute1(in(), out());
		}

		@Override
		public void compute1(final DoubleType input, final DoubleType output) {
			output.setReal(input.getRealDouble() + 1.0);
		}

		@Override
		public void mutate(final DoubleType arg) {
			compute1(arg, arg);
		}

		@Override
		public DoubleType arg() {
			return in();
		}

		@Override
		public void setArg(final DoubleType arg) {
			setInput(arg);
		}

		@Override
		public UnaryAddInplace getIndependentInstance() {
			return this;
		}

	}

	public static class UnaryAddComputer extends
		AbstractUnaryComputerOp<DoubleType, DoubleType>
	{

		@Override
		public void compute1(final DoubleType input, final DoubleType output) {
			output.setReal(input.getRealDouble() + 1.0);
		}

	}

	// --------------------- Binary Helper Classes ---------------------

	public static class BinaryAddComputer extends
		AbstractBinaryComputerOp<DoubleType, DoubleType, DoubleType>
	{

		@Override
		public void compute2(final DoubleType input1, final DoubleType input2,
			final DoubleType output)
		{
			output.set(input1.getRealDouble() + input2.getRealDouble());
		}

	}

	public static class BinaryAddHybrid extends
		AbstractBinaryHybridOp<DoubleType, DoubleType, DoubleType>
	{

		@Override
		public void compute2(final DoubleType input1, final DoubleType input2,
			final DoubleType output)
		{
			output.set(input1.getRealDouble() + input2.getRealDouble());
		}

		@Override
		public DoubleType createOutput(final DoubleType input1,
			final DoubleType input2)
		{
			return new DoubleType();
		}

	}

	public static class BinaryAddInplace extends
		AbstractBinaryComputerOp<DoubleType, DoubleType, DoubleType> implements
		InplaceOp<DoubleType>
	{

		@Override
		public void run() {
			InplaceOp.super.run();
		}

		@Override
		public void compute2(final DoubleType input1, final DoubleType input2,
			final DoubleType output)
		{
			output.set(input1.getRealDouble() + input2.getRealDouble());
		}

		@Override
		public void mutate(final DoubleType arg) {
			compute2(in1(), in2(), arg);
		}

		@Override
		public DoubleType arg() {
			return out() == null ? in1() : out();
		}

		@Override
		public void setArg(final DoubleType arg) {
			setOutput(arg);
		}

		@Override
		public BinaryAddInplace getIndependentInstance() {
			return this;
		}

	}
}
