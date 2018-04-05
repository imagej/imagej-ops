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

package net.imagej.ops.math.multiply;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Math.ComplexConjugateMultiply.class,
	priority = Priority.LOW)
public class ComplexConjugateMultiplyOp<T extends ComplexType<T>> extends
	AbstractBinaryComputerOp<T, T, T> implements
	Ops.Math.ComplexConjugateMultiply, Contingent
{

	// TODO: extend common abstract base class which implements Contingent
	// for dimensionality checking.
	// TODO: code generate this and all add ops to generalize them to other
	// operators.

	@Override
	public boolean conforms() {
		return true;
		/*	if (!Intervals.equalDimensions(in1(), in2())) return false;
			if (!in1().iterationOrder().equals(in2().iterationOrder())) return false;
			if (out() == null) return true;
			return Intervals.equalDimensions(in1(), out()) && in1().iterationOrder()
				.equals(out().iterationOrder());*/
	}

	@Override
	public void compute(final T input1, final T input2, final T output) {
		T temp = input2.copy();
		temp.complexConjugate();
		temp.mul(input1);
		output.set(temp);

	}

}
