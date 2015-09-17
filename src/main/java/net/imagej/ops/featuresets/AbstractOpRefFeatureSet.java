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

package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.scijava.plugin.Parameter;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;

/**
 * {@link OpRef} based {@link AbstractCachedFeatureSet}.
 * 
 * @author Christian Dietz, University of Konstanz.
 * @param <I> type of the input
 * @param <O> type of the output
 */
public abstract class AbstractOpRefFeatureSet<I, O> extends
	AbstractCachedFeatureSet<I, O>implements FeatureSet<I, O>,
	ConfigurableFeatureSet<I, O>
{

	@Parameter
	private Class<? extends O> outType;

	// active ops
	private Set<NamedFeature> active;

	// all opRefs
	private Map<NamedFeature, FunctionOp<I, ? extends O>> namedFeatureMap;

	@Override
	public void initialize() {
		super.initialize();

		if (namedFeatureMap == null) {
			namedFeatureMap = new HashMap<NamedFeature, FunctionOp<I, ? extends O>>();
			for (final OpRef<?> ref : initOpRefs()) {
				if (active == null || active.contains(ref)) namedFeatureMap.put(
					new OpRefFeatureInfo(ref), ops().function(ref.getType(), outType,
						in(), ref.getArgs()));
			}
		}

	}

	@Override
	public Map<NamedFeature, O> compute(final I input) {
		final Map<NamedFeature, O> res = new HashMap<NamedFeature, O>();

		for (final Entry<NamedFeature, FunctionOp<I, ? extends O>> entry : namedFeatureMap
			.entrySet())
		{
			res.put(entry.getKey(), entry.getValue().compute(input));
		}

		return res;
	}

	@Override
	public void setFeatureStatus(final NamedFeature info, boolean enabled) {
		if (active == null && enabled) {
			active = new HashSet<NamedFeature>();
		}
		if (enabled) {
			active.add(info);
		}
		else if (active != null) {
			active.remove(info);
		}
	}

	/**
	 * @return all active {@link OpRef}s.
	 */
	@Override
	public Collection<NamedFeature> getEnabledFeatures() {
		return active != null ? active : namedFeatureMap.keySet();
	}

	/**
	 * @return all {@link OpRef}s.
	 */
	@Override
	public Collection<NamedFeature> getFeatures() {
		return namedFeatureMap.keySet();
	}

	/**
	 * Helper method: Creates a simple {@link OpRef} given the additional args.
	 * NB: Input/Output are not type of the args in this particular case.
	 * 
	 * @param type of the {@link Op}
	 * @param args arguments without input and output
	 * @return {@link OpRef}
	 */
	protected <OP extends Op> OpRef<OP> ref(final Class<OP> type,
		final Object... args)
	{
		return new OpRef<OP>(type, args);
	}

	/**
	 * @return {@link Collection} of {@link OpRef}s which will be calculated by
	 *         the {@link FeatureSet} (if activated).
	 */
	protected abstract Collection<? extends OpRef<?>> initOpRefs();

}
