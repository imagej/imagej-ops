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

import imagej.ops.Function;
import imagej.ops.Op;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.scijava.plugin.Plugin;

/**
 * Joins a list of {@link Function}s.
 * 
 * @author Christian Dietz
 * @author Curtis Rueden
 */
@Plugin(type = Op.class, name = Join.NAME)
public class DefaultJoinFunctions<A> extends
	AbstractJoinFunctions<A, Function<A, A>>
{

	@Override
	public A compute(final A input, final A output) {
		final List<? extends Function<A, A>> functions = getFunctions();
		final Iterator<? extends Function<A, A>> it = functions.iterator();
		final Function<A, A> first = it.next();

		if (functions.size() == 1) {
			return first.compute(input, output);
		}

		final A buffer = getBuffer(input);

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

	@Override
	public DefaultJoinFunctions<A> getIndependentInstance() {

		final DefaultJoinFunctions<A> joiner = new DefaultJoinFunctions<A>();

		final ArrayList<Function<A, A>> funcs = new ArrayList<Function<A, A>>();
		for (final Function<A, A> func : getFunctions()) {
			funcs.add(func.getIndependentInstance());
		}

		joiner.setFunctions(funcs);
		joiner.setBufferFactory(getBufferFactory());

		return joiner;
	}
}
