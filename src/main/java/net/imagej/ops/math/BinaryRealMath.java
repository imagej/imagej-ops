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

package net.imagej.ops.math;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractBinaryComputerOp;
import net.imglib2.type.numeric.RealType;

/**
 * Ops of the {@code math} namespace which operate on {@link RealType}s
 * using a BinaryOp approach (vs RealMath which use a UnaryOp + Param approach).
 *
 * @author Barry DeZonia
 * @author Jonathan Hale (University of Konstanz)
 * @author Curtis Rueden
 * @author Jay Warrick
 */
public final class BinaryRealMath {

	private BinaryRealMath() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Sets the real component of an output real number to the addition of the
	 * real component of an input real number with the provided output value.
	 */
	@Plugin(type = Ops.Math.Add.class)
	public static class Add<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>> extends
		AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Add
	{
		@Override
		public void compute2(final I1 input1, final I2 input2, final O output) {
			input2.setReal(input1.getRealDouble() + input2.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the subtraction from
	 * the real component of an input real number by the provided output value.
	 */
	@Plugin(type = Ops.Math.Subtract.class)
	public static class Subtract<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Subtract
	{
		@Override
		public void compute2(final I1 input1, final I2 input2, final O output) {
			output.setReal(input1.getRealDouble() - input2.getRealDouble());
		}
	}
	
	/**
	 * Sets the real component of an output real number to the multiplication of
	 * the real component of an input real number with the provided output value.
	 */
	@Plugin(type = Ops.Math.Multiply.class)
	public static class Multiply<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Multiply
	{
		@Override
		public void compute2(final I1 input1, final I2 input2, O output) {
			output.setReal(input1.getRealDouble() * input2.getRealDouble());
		}
	}

	/**
	 * Sets the real component of an output real number to the division of the
	 * real component of an input real number by the provided output value.
	 */
	@Plugin(type = Ops.Math.Divide.class)
	public static class Divide<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Divide
	{
		@Parameter
		private double dbzVal;

		@Override
		public void compute2(final I1 input1, final I2 input2, O output) {
			if (input2.getRealDouble() == 0) {
				output.setReal(dbzVal);
			}
			else {
				output.setReal(input1.getRealDouble() / input2.getRealDouble());
			}
		}
	}
	
	/**
	 * Sets the real component of an output real number to the raising of the real
	 * component of an input real number to the provided output value.
	 */
	@Plugin(type = Ops.Math.Power.class)
	public static class Power<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Power
	{
		@Override
		public void compute2(final I1 input1, final I2 input2, O output) {
			output.setReal(Math.pow(input1.getRealDouble(), input2.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the real component of
	 * the output or input, whichever is greater.
	 */
	@Plugin(type = Ops.Math.Max.class)
	public static class Max<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Max
	{
		@Override
		public void compute2(final I1 input1, final I2 input2, O output) {
			output.setReal(Math.max(input1.getRealDouble(), input2.getRealDouble()));
		}
	}

	/**
	 * Sets the real component of an output real number to the real component of
	 * the output or input, whichever is lesser.
	 */
	@Plugin(type = Ops.Math.Min.class)
	public static class Min<I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>>
		extends AbstractBinaryComputerOp<I1, I2, O> implements Ops.Math.Min
	{
		@Override
		public void compute2(final I1 input1, final I2 input2, O output) {
			output.setReal(Math.min(input1.getRealDouble(), input2.getRealDouble()));
		}
	}

}
