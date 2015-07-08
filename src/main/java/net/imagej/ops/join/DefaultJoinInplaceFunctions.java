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

package net.imagej.ops.join;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.AbstractInplaceFunction;
import net.imagej.ops.InplaceFunction;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Joins a list of {@link InplaceFunction}s.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Op.class, name = Ops.Join.NAME)
public class DefaultJoinInplaceFunctions<A> extends AbstractInplaceFunction<A>
	implements Ops.Join
{

	// list of functions to be joined
	@Parameter
	private List<InplaceFunction<A>> functions;

	public void setFunctions(final List<InplaceFunction<A>> functions) {
		this.functions = functions;
	}

	public List<InplaceFunction<A>> getFunctions() {
		return functions;
	}

	@Override
	public A compute(final A input) {

		for (final InplaceFunction<A> inplace : functions) {
			inplace.compute(input);
		}

		return input;
	}

	@Override
	public DefaultJoinInplaceFunctions<A> getIndependentInstance() {

		final DefaultJoinInplaceFunctions<A> joiner =
			new DefaultJoinInplaceFunctions<A>();

		final ArrayList<InplaceFunction<A>> funcs =
			new ArrayList<InplaceFunction<A>>();
		for (final InplaceFunction<A> func : getFunctions()) {
			funcs.add(func.getIndependentInstance());
		}

		joiner.setFunctions(funcs);

		return joiner;
	}

}
