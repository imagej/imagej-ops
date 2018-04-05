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

import net.imagej.ops.special.UnaryOutputFactory;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Base class for {@link UnaryFunctionOp}s that delegate to
 * {@link UnaryComputerOp}s.
 * <p>
 * This is mostly useful when the {@link UnaryComputerOp} in question has a
 * generic type as output, which needs to be narrowed to a concrete type for the
 * purposes of the {@link UnaryFunctionOp} portion's return type. In this
 * scenario, a {@link UnaryHybridCF} cannot be used directly with type-safe
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
 * @param <I> type of input
 * @param <O> type of output
 * @param <DI> type of input accepted by the worker op
 * @param <DO> type of output accepted by the worker op
 */
public abstract class UFViaUC<I extends DI, O extends DO, DI, DO> extends
	AbstractUnaryFunctionOp<I, O> implements
	DelegatingUnaryOp<I, O, DI, DO, UnaryComputerOp<DI, DO>>,
	UnaryOutputFactory<I, O>
{

	private UnaryComputerOp<DI, DO> worker;

	// -- UnaryFunctionOp methods --

	@Override
	public O calculate(final I input) {
		final O output = createOutput(input);
		worker.compute(input, output);
		return output;
	}

	// -- Initializable methods --

	@Override
	public void initialize() {
		worker = createWorker(in());
	}

}
