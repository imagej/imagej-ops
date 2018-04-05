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

package net.imagej.ops.special.function;

import net.imagej.ops.special.AbstractUnaryOp;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Abstract superclass for {@link UnaryFunctionOp} implementations.
 * 
 * @author Curtis Rueden
 */
public abstract class AbstractUnaryFunctionOp<I, O> extends AbstractUnaryOp<I, O>
	implements UnaryFunctionOp<I, O>
{

	// -- Parameters --

	@Parameter(type = ItemIO.OUTPUT)
	private O out;

	@Parameter
	private I in;

	// -- Runnable methods --

	@Override
	public void run() {
		out = run(in(), null);
	}

	// -- UnaryInput methods --

	@Override
	public I in() {
		return in;
	}

	@Override
	public void setInput(final I input) {
		in = input;
	}

	// -- Output methods --

	@Override
	public O out() {
		return out;
	}

}
