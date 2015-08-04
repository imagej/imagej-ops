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

package net.imagej.ops.resolver;

import net.imagej.ops.Computer;
import net.imagej.ops.InputOp;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * A {@link ResolvedOp} is an {@link Op} which has been resolved by the
 * {@link OpResolverService}. It is therefore part of an {@link ResolvedOpSet}.
 * The {@link ResolvedOp} takes care, that all {@link Op}s, which are required
 * by the {@link ResolvedOp} are executed before the {@link ResolvedOp} itself
 * is executed, i.e. all parameters are available.
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <I> type of the input
 * @param <O> type of the output
 */
class ResolvedOp<I, O> implements InputOp<I>, OutputOp<O>, Computer<I, O> {

	@Parameter(type = ItemIO.OUTPUT)
	private O output;

	@Parameter
	private I input;

	private OutputOp<O> outputOp;

	private ResolvedOpSet<I> resolvedSet;

	@SuppressWarnings("unchecked")
	public ResolvedOp(final ResolvedOpSet<I> build, final OpRef<?> ref) {
		this.outputOp = (OutputOp<O>) build.getOutput().get(ref);
		this.resolvedSet = build;
	}

	@Override
	public void run() {
		resolvedSet.compute(getInput());
		resolvedSet.run();
		output = outputOp.getOutput();
	}

	@Override
	public I getInput() {
		return input;
	}

	@Override
	public void setInput(final I input) {
		this.input = input;
	}

	@Override
	public O compute(I input) {
		setInput(input);
		run();
		return output;
	}

	@Override
	public O getOutput() {
		return output;
	}

	@Override
	public void setOutput(O output) {
		this.output = output;
	}

}
