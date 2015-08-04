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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.imagej.ops.Computer;
import net.imagej.ops.InputOp;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;

import org.scijava.ItemIO;
import org.scijava.module.Module;
import org.scijava.plugin.Parameter;

/**
 * A {@link ResolvedOpSet} represents a set of operations, which have been
 * resolved by the {@link OpResolverService}. The resolved {@link Op}s can be
 * used by updating the {@link ResolvedOpSet} and access the {@link Op}s via the
 * {@link OpRef}s which were passed to the {@link OpResolverService}.
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <I> type of input
 */
class ResolvedOpSet<I> implements InputOp<I>, OutputOp<Map<OpRef<?>, Op>>,
	Computer<I, Map<OpRef<?>, Op>>
{

	@Parameter
	private I input;

	@Parameter(type = ItemIO.OUTPUT)
	private Map<OpRef<?>, Op> output;

	private InputOp<I> source;
	private Map<OpRef<?>, ? extends Module> globalSet;

	public ResolvedOpSet(final InputOp<I> source,
		final Map<OpRef<?>, ? extends Module> set, final Set<OpRef<?>> outputOpRefs)
	{
		this.source = source;

		this.globalSet = set;
		this.output = new HashMap<OpRef<?>, Op>();

		for (final OpRef<?> ref : outputOpRefs) {
			output.put(ref, (Op) set.get(ref).getDelegateObject());
		}
	}

	public Map<OpRef<?>, Op> get() {
		return output;
	}

	@Override
	public void run() {
		source.run();
		for (final OpRef<?> op : output.keySet()) {
			globalSet.get(op).run();
		}
	}

	@Override
	public I getInput() {
		return input;
	}

	@Override
	public void setInput(final I input) {
		this.input = input;
		source.setInput(input);
	}

	@Override
	public Map<OpRef<?>, Op> compute(final I input) {
		setInput(input);
		run();
		return getOutput();
	}

	@Override
	public Map<OpRef<?>, Op> getOutput() {
		return output;
	}

	@Override
	public void setOutput(final Map<OpRef<?>, Op> output) {
		throw new UnsupportedOperationException("Not supported");
	}
}
