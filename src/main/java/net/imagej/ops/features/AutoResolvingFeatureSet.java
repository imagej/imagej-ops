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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginService;

/**
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <I>
 */
public class AutoResolvingFeatureSet<I, O> extends AbstractFeatureSet<I, O>
		implements FeatureSet<I, O> {

	@Parameter
	private OpResolverService oobs;

	@Parameter
	private PluginService ps;

	/* internal stuff */

	/*
	 * Set containing all visible features, i.e. features will will be available
	 * as FeatureResults
	 */
	private Set<OpRef<?>> outputOps;

	/*
	 * Set containing all invisible features, i.e. features which are required
	 * by visible features.
	 */
	private Set<OpRef<?>> pool;

	/*
	 * function representing the compiled feature set
	 */
	private ResolvedOpSet<I> modulSet;

	private Map<OpRef<?>, String> names;

	public AutoResolvingFeatureSet() {
		this.outputOps = new HashSet<OpRef<?>>();
		this.pool = new HashSet<OpRef<?>>();
	}

	@SuppressWarnings("rawtypes")
	public <OP extends OutputOp> void addOutputOp(final Class<OP> op,
			final Object... args) {
		final OpRef<OP> opRef = new OpRef<OP>(op, args);
		outputOps.add(opRef);
		addHiddenOp(opRef);
	}

	public void addOutputOp(final OpRef<?> opRef) {
		outputOps.add(opRef);
		addHiddenOp(opRef);
	}

	public <OP extends Op> void addHiddenOp(final Class<OP> op,
			final Object... args) {
		pool.add(new OpRef<OP>(op, args));
	}

	public void addHiddenOp(final OpRef<?> ref) {
		pool.add(ref);
	}

	@Override
	public void run() {

		if (modulSet == null) {
			names = new HashMap<OpRef<?>, String>();

			modulSet = oobs.resolve(getInput(), pool);

			for (final OpRef<?> ref : outputOps) {
				names.put(ref, ps.getPlugin(modulSet.get().get(ref).getClass())
						.getLabel());
			}

		}

		modulSet.setInput(getInput());
		modulSet.run();

		final Map<OpRef<?>, Op> output = new HashMap<OpRef<?>, Op>();
		for (OpRef<?> ref : outputOps) {
			output.put(ref, modulSet.getOutput().get(ref));
		}

		setOutput(output);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pair<String, O>> getFeatures(I input) {
		final Map<OpRef<? extends Op>, Op> map = compute(input);
		final List<Pair<String, O>> features = new ArrayList<Pair<String, O>>();

		for (final Entry<OpRef<?>, String> entry : names.entrySet()) {
			features.add(new ValuePair<String, O>(entry.getValue(),
					((OutputOp<O>) map.get(entry.getKey())).getOutput()));
		}

		return features;
	}

	public Set<OpRef<?>> getOutputOps() {
		return outputOps;
	}

	public void setOutputOps(Set<OpRef<?>> opRefs) {
		this.outputOps = opRefs;
	}
}
