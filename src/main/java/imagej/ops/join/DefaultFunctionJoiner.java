/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package imagej.ops.join;

import imagej.ops.AbstractFunction;
import imagej.ops.Function;

import java.util.Iterator;
import java.util.List;

import org.scijava.plugin.Parameter;

/**
 * Joins {@link Function}s.
 * 
 * @author Christian Dietz
 */
public class DefaultFunctionJoiner<A> extends AbstractFunction<A, A> {

	/** List of functions to be joined. */
	private List<Function<A, A>> functions;

	@Parameter
	private A buffer;

	public A getBuffer() {
		return buffer;
	}

	public void setBuffer(final A buffer) {
		this.buffer = buffer;
	}

	public void setFunctions(final List<Function<A, A>> functions) {
		this.functions = functions;
	}

	@Override
	public A compute(final A input, final A output) {
		final Iterator<Function<A, A>> it = functions.iterator();
		final Function<A, A> first = it.next();

		if (functions.size() == 1) {
			return first.compute(input, output);
		}

		A tmpOutput = output;
		A tmpInput = buffer;
		A tmp;

		if (functions.size() % 2 == 0) {
			tmpOutput = buffer;
			tmpInput = output;
		}

		first.compute(input, tmpOutput);

		while (it.hasNext()) {
			tmp = tmpInput;
			tmpInput = tmpOutput;
			tmpOutput = tmp;
			it.next().compute(tmpInput, tmpOutput);
		}

		return output;
	}

}
