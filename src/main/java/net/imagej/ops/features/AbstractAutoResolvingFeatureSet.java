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
public abstract class AbstractAutoResolvingFeatureSet<I, O> extends
		AbstractFeatureSet<I, O> implements FeatureSet<I, O>,
		LabeledFeatures<I, O> {

	@Parameter
	private OpResolverService oobs;

	@Parameter
	private PluginService ps;

	/* internal stuff */

	/*
	 * function representing the compiled feature set
	 */
	private ResolvedOpSet<I> modulSet;

	/*
	 * Map used to store OutputOps and avoid duplicate castings
	 */
	private Map<OpRef<?>, OutputOp<O>> outputOpMap;

	/*
	 * Keep names of ops
	 */
	private Map<OpRef<?>, String> names;

	// @SuppressWarnings("rawtypes")
	// public <OP extends OutputOp> void addOutputOp(final Class<OP> op,
	// final Object... args) {
	// final OpRef<OP> opRef = new OpRef<OP>(op, args);
	// this.outputOps.add(opRef);
	// addHiddenOp(opRef);
	// }

	// public void addOutputOp(final OpRef<?> opRef) {
	// this.outputOps.add(opRef);
	// addHiddenOp(opRef);
	// }

	protected <OP extends Op> OpRef<OP> createOpRef(final Class<OP> op,
			final Object... args) {
		return new OpRef<OP>(op, args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		// compile
		if (this.modulSet == null) {
			final Set<OpRef<?>> outputOps = getOutputOps();
			final Set<OpRef<?>> pool = new HashSet<OpRef<?>>();
			pool.addAll(outputOps);
			pool.addAll(getHiddenOps());

			this.modulSet = this.oobs.resolve(getInput(), pool);
			this.outputOpMap = new HashMap<OpRef<?>, OutputOp<O>>();

			this.names = new HashMap<OpRef<?>, String>();

			// avoid duplicate castings
			for (final OpRef<?> ref : outputOps) {
				this.outputOpMap.put(ref, ((OutputOp<O>) this.modulSet
						.getOutput().get(ref)));
				this.names.put(
						ref,
						this.ps.getPlugin(
								this.modulSet.get().get(ref).getClass())
								.getName());
			}

			setOutput(new HashMap<OpRef<?>, O>());
		} else {

		}

		this.modulSet.setInput(getInput());
		this.modulSet.run();

		getOutput().clear();
		for (final Entry<OpRef<?>, OutputOp<O>> entry : this.outputOpMap
				.entrySet()) {
			getOutput().put(entry.getKey(), entry.getValue().getOutput());
		}
	}

	@Override
	public List<Pair<String, O>> getFeatureList(final I input) {
		final Map<OpRef<? extends Op>, O> map = compute(input);
		final List<Pair<String, O>> features = new ArrayList<Pair<String, O>>();

		for (final Entry<OpRef<?>, String> entry : this.names.entrySet()) {
			features.add(new ValuePair<String, O>(entry.getValue(), map
					.get(entry.getKey())));
		}

		return features;
	}

	@Override
	public Map<OpRef<? extends Op>, O> getFeaturesByRef(final I input) {
		return compute(input);
	}

	public abstract Set<OpRef<?>> getOutputOps();

	public abstract Set<OpRef<?>> getHiddenOps();

}
