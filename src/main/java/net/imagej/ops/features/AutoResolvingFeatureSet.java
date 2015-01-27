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
package net.imagej.ops.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;
import net.imagej.ops.functionbuilder.OutputOpBuilderService;
import net.imagej.ops.functionbuilder.OutputOpRef;
import net.imagej.ops.functionbuilder.UpdatableOutputOpSet;

import org.scijava.plugin.Parameter;

/**
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <I>
 */
public class AutoResolvingFeatureSet<I, O> extends AbstractFeatureSet<I, O> implements FeatureSet<I, O> {

	@Parameter
	private OutputOpBuilderService oobs;

	/* internal stuff */

	/*
	 * Set containing all visible features, i.e. features will will be available
	 * as FeatureResults
	 */
	private HashSet<OutputOpRef<O>> outputOps;

	/*
	 * Set containing all invisible features, i.e. features which are required
	 * by visible features.
	 */
	private HashSet<OpRef> pool;

	/*
	 * function representing the compiled feature set
	 */
	private UpdatableOutputOpSet<I, O> moduleSet;

	/*
	 * Defines the output type of the ops added as visible
	 */
	private final O outType;

	public AutoResolvingFeatureSet(final O outType) {
		this.outType = outType;
		this.outputOps = new HashSet<OutputOpRef<O>>();
		this.pool = new HashSet<OpRef>();
	}

	public void addOutputOp(final OutputOpRef<O> ref) {
		outputOps.add(ref);
		addHiddenOp(ref);
	}

	public void addHiddenOp(final OpRef ref) {
		pool.add(ref);
	}

	@Override
	public void run() {

		if (moduleSet == null) {
			moduleSet = oobs.build(outputOps, outType, getInput(),
					pool.toArray(new OpRef[pool.size()]));
		}

		moduleSet.setInput(getInput());
		moduleSet.run();

		final Map<OutputOpRef<O>, O> output = new HashMap<OutputOpRef<O>, O>();
		for (final Entry<OutputOpRef<O>, OutputOp<O>> entry : moduleSet.get()
				.entrySet()) {
			output.put(entry.getKey(), entry.getValue().getOutput());
		}

		setOutput(output);
	}
}
