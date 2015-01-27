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

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Function;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.functionbuilder.ModuleBuilderService;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginService;

/**
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <I>
 */
public class GenericFeatureSet<I, O> extends
		AbstractOutputFunction<I, Map<Class<? extends Op>, O>> implements
		FeatureSet<I, O> {

	@Parameter
	private PluginService ps;

	@Parameter
	private OpService ops;

	@Parameter
	private ModuleBuilderService fb;

	/* internal stuff */

	/*
	 * Set containing all visible features, i.e. features will will be available
	 * as FeatureResults
	 */
	private HashSet<OpRef> visible;

	/*
	 * Set containing all invisible features, i.e. features which are required
	 * by visible features.
	 */
	private HashSet<OpRef> opPool;

	/*
	 * function representing the compiled feature set
	 */
	private Function<I, Map<Class<? extends Op>, O>> func;

	/*
	 * Defines the output type of the ops added as visible
	 */
	private final O outType;

	public GenericFeatureSet(final O outType) {
		this.outType = outType;
		this.visible = new HashSet<OpRef>();
		this.opPool = new HashSet<OpRef>();
	}

	protected Map<Class<? extends Op>, O> safeCompute(final I input,
			final Map<Class<? extends Op>, O> output) {
		output.clear();

		if (func == null) {
			func = fb.build(visible, outType, input,
					opPool.toArray(new OpRef[opPool.size()]));
		}

		return func.compute(input, output);
	}

	@Override
	public Map<Class<? extends Op>, O> createOutput(I input) {
		return new HashMap<Class<? extends Op>, O>();
	};

	/**
	 * TODO docu
	 * 
	 * Add an invisible feature, this means a feature which will not be
	 * available as a {@link FeatureResult}, but is required by some visible
	 * {@link Feature}
	 * 
	 * @param op
	 * @param parameters
	 */
	public void addOp(final OpRef ref, boolean visibleAsOutput) {
		opPool.add(ref);

		if (visibleAsOutput)
			visible.add(ref);
	}
}
