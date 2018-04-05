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

package net.imagej.ops.math;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Binary Ops of the {@code math} namespace which operate on {@link RealType}s.
 *
 * @author Leon Yang
 */
public class BinaryRealTypeMath {

	private BinaryRealTypeMath() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Sets the real component of an output real number to the addition of the
	 * real components of two input real numbers.
	 */
	@Plugin(type = Ops.Math.Add.class)
	public static class Add<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Add
	{

		@Override
		public void compute(final I1 input1, final I2 input2, final O output) {
			output.setReal(input1.getRealDouble() + input2.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the logical AND of the
	 * real component of two input real numbers.
	 */
	@Plugin(type = Ops.Math.And.class)
	public static class And<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.And
	{

		@Override
		public void compute(final I1 input1, final I2 input2, final O output) {
			output.setReal((long) input1.getRealDouble() & (long) input2
				.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the division of the
	 * real component of two input real numbers.
	 */
	@Plugin(type = Ops.Math.Divide.class)
	public static class Divide<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Divide
	{

		@Parameter
		private double dbzVal;

		@Override
		public void compute(final I1 input1, final I2 input2, final O output) {
			if (input2.getRealDouble() == 0) {
				output.setReal(dbzVal);
			}
			else {
				output.setReal(input1.getRealDouble() / input2.getRealDouble());
			}
		}
	}

	/**
	 * Sets the real component of an output real number to the multiplication of
	 * the real component of two input real numbers.
	 */
	@Plugin(type = Ops.Math.Multiply.class)
	public static class Multiply<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Multiply
	{

		@Override
		public void compute(final I1 input1, final I2 input2, final O output) {
			output.setReal(input1.getRealDouble() * input2.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the logical OR of the
	 * real component of two input real numbers.
	 */
	@Plugin(type = Ops.Math.Or.class)
	public static class Or<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Or
	{

		@Override
		public void compute(final I1 input1, final I2 input2, final O output) {
			output.setReal((long) input1.getRealDouble() | (long) input2
				.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the subtraction between
	 * the real component of two input real numbers.
	 */
	@Plugin(type = Ops.Math.Subtract.class)
	public static class Subtract<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Subtract
	{

		@Override
		public void compute(final I1 input1, final I2 input2, final O output) {
			output.setReal(input1.getRealDouble() - input2.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the logical XOR of the
	 * real component of two input real numbers.
	 */
	@Plugin(type = Ops.Math.Xor.class)
	public static class Xor<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Xor
	{

		@Override
		public void compute(final I1 input1, final I2 input2, final O output) {
			output.setReal((long) input1.getRealDouble() ^ (long) input2
				.getRealDouble());
		}
	}

}
