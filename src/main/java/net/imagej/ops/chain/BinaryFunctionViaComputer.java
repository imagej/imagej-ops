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

package net.imagej.ops.chain;

import net.imagej.ops.special.AbstractBinaryFunctionOp;
import net.imagej.ops.special.BinaryComputerOp;
import net.imagej.ops.special.BinaryHybridOp;
import net.imagej.ops.special.BinaryOutputFactory;
import net.imagej.ops.special.UnaryComputerOp;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imagej.ops.special.UnaryHybridOp;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Base class for {@link BinaryHybridOp} implementations that delegate to
 * {@link BinaryComputerOp} implementations.
 * <p>
 * This is mostly useful when the {@link BinaryComputerOp} in question has a
 * generic type as output, which needs to be narrowed to a concrete type for the
 * purposes of the {@link UnaryFunctionOp} portion's return type. In this scenario, a
 * {@link BinaryHybridOp} cannot be used directly with type-safe generics.
 * </p>
 * <p>
 * For example, a {@link UnaryComputerOp} whose output variable is a
 * {@code T extends RealType<T>} cannot be a {@link UnaryHybridOp} because we do not
 * know at runtime which sort of {@link RealType} matches the caller's {@code T}
 * parameter. However, a separate {@link UnaryFunctionOp} can be created whose output
 * is typed on e.g. {@link DoubleType}, with the computation delegating to the
 * wrapped {@link UnaryComputerOp}.
 * </p>
 */
public abstract class BinaryFunctionViaComputer<I1, I2, O> extends
	AbstractBinaryFunctionOp<I1, I2, O> implements
	DelegatingBinaryOp<BinaryComputerOp<I1, I2, O>, I1, I2, O>,
	BinaryOutputFactory<I1, I2, O>
{

	private BinaryComputerOp<I1, I2, O> worker;

	// -- Initializable methods --

	@Override
	public void initialize() {
		worker = createWorker(in1(), in2());
	}

	// -- BinaryFunctionOp methods --

	@Override
	public O compute2(final I1 input1, final I2 input2) {
		final O output = createOutput(input1, input2);
		worker.compute2(input1, input2, output);
		return output;
	}

}
