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

package net.imagej.ops;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Abstract superclass for {@link ComputerOp} implementations.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 * @author Curtis Rueden
 */
public abstract class AbstractComputerOp<I, O> extends AbstractSpecialOp<I, O>
	implements ComputerOp<I, O>
{

	// -- Parameters --

	@Parameter(type = ItemIO.BOTH)
	private O out;

	@Parameter
	private I in;

	// -- Runnable methods --

	@Override
	public void run() {
		compute(in(), out());
	}

	// -- Input methods --

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

	// -- Threadable methods --

	@Override
	public ComputerOp<I, O> getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

}
