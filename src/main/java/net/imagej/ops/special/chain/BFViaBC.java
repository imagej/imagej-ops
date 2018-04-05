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

package net.imagej.ops.special.chain;

import net.imagej.ops.special.BinaryOutputFactory;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.BinaryHybridCF;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Base class for {@link BinaryFunctionOp}s that delegate to
 * {@link BinaryComputerOp}s.
 * <p>
 * This is mostly useful when the {@link BinaryComputerOp} in question has a
 * generic type as output, which needs to be narrowed to a concrete type for the
 * purposes of the {@link UnaryFunctionOp} portion's return type. In this
 * scenario, a {@link BinaryHybridCF} cannot be used directly with type-safe
 * generics.
 * </p>
 * <p>
 * For example, a {@link UnaryComputerOp} whose output variable is a
 * {@code T extends RealType<T>} cannot be a {@link UnaryHybridCF} because we do
 * not know at runtime which sort of {@link RealType} matches the caller's
 * {@code T} parameter. However, a separate {@link UnaryFunctionOp} can be
 * created whose output is typed on e.g. {@link DoubleType}, with the
 * computation delegating to the wrapped {@link UnaryComputerOp}.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I1> type of first input
 * @param <I2> type of second input
 * @param <O> type of output
 * @param <DI1> type of first input accepted by the worker op
 * @param <DI2> type of second input accepted by the worker op
 * @param <DO> type of output accepted by the worker op
 */
public abstract class BFViaBC<I1 extends DI1, I2 extends DI2, O extends DO, DI1, DI2, DO>
	extends AbstractBinaryFunctionOp<I1, I2, O> implements
	DelegatingBinaryOp<I1, I2, O, DI1, DI2, DO, BinaryComputerOp<DI1, DI2, DO>>,
	BinaryOutputFactory<I1, I2, O>
{

	private BinaryComputerOp<DI1, DI2, DO> worker;

	@Override
	public void initialize() {
		worker = createWorker(in1(), in2());
	}

	@Override
	public O calculate(final I1 input1, final I2 input2) {
		final O output = createOutput(input1, input2);
		worker.compute(input1, input2, output);
		return output;
	}

}
