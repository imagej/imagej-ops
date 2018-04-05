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

import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imagej.ops.special.hybrid.BinaryHybridCF;

/**
 * Base class for {@link BinaryHybridCF}s that delegate to
 * {@link BinaryHybridCF}s.
 * 
 * @author Curtis Rueden
 * @param <I1> type of first input
 * @param <I2> type of second input
 * @param <O> type of output (for both the op and its worker)
 * @param <DI1> type of first input accepted by the worker op
 * @param <DI2> type of second input accepted by the worker op
 */
public abstract class BHCFViaBHCF<I1 extends DI1, I2 extends DI2, O, DI1, DI2>
	extends AbstractBinaryHybridCF<I1, I2, O> implements
	DelegatingBinaryOp<I1, I2, O, DI1, DI2, O, BinaryHybridCF<DI1, DI2, O>>
{

	private BinaryHybridCF<DI1, DI2, O> worker;

	@Override
	public O createOutput(final I1 input1, final I2 input2) {
		return worker.createOutput(input1, input2);
	}

	@Override
	public void initialize() {
		worker = createWorker(in1(), in2());
	}

	@Override
	public void compute(final I1 input1, final I2 input2, final O output) {
		worker.compute(input1, input2, output);
	}

}
