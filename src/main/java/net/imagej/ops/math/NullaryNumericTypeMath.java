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
import net.imagej.ops.special.computer.AbstractNullaryComputerOp;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.NumericType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Nullary Ops of the {@code math} namespace which operate on
 * {@link NumericType}s.
 * 
 * @author Leon Yang
 */
public class NullaryNumericTypeMath {

	private NullaryNumericTypeMath() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Sets the output to a constant.
	 */
	@Plugin(type = Ops.Math.Assign.class)
	public static class Assign<T extends Type<T>> extends
		AbstractNullaryComputerOp<T> implements Ops.Math.Assign
	{
		// NB: This class does not require output to be NumericType. Should consider
		// move to else where.
		@Parameter
		private T constant;

		@Override
		public void compute(final T output) {
			output.set(constant);
		}
	}

	/**
	 * Sets the output to zero.
	 */
	@Plugin(type = Ops.Math.Zero.class)
	public static class Zero<T extends NumericType<T>> extends
		AbstractNullaryComputerOp<T> implements Ops.Math.Zero
	{

		@Override
		public void compute(final T output) {
			output.setZero();
		}

		@Override
		public Zero<T> getIndependentInstance() {
			return this;
		}
	}
}
