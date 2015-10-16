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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.scijava.plugin.Parameter;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * {@link OpRef} based {@link AbstractCachedFeatureSet}.
 * 
 * @author Christian Dietz, University of Konstanz.
 * @param <I>
 *            type of the input
 * @param <O>
 *            type of the output
 */
public abstract class AbstractOpRefFeatureSet<I, O extends RealType<O>> extends AbstractCachedFeatureSet<I, O> {

	@Parameter(required = false)
	private Class<? extends O> outType;

	// all opRefs
	private Map<NamedFeature, FunctionOp<Object, ? extends O>> namedFeatureMap;

	private List<NamedFeature> featureNames;

	private final LinkedHashSet<OpRef<?>> opRefs;

	public AbstractOpRefFeatureSet() {
		opRefs = new LinkedHashSet<OpRef<?>>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		super.initialize();

		if (namedFeatureMap == null) {
			namedFeatureMap = new HashMap<NamedFeature, FunctionOp<Object, ? extends O>>();
			for (final OpRef<?> ref : opRefs) {
				final NamedFeature feature = new NamedFeature(ref.getLabel());
				namedFeatureMap.put(feature, (FunctionOp<Object, ? extends O>) ops().function(ref.getType(),
						outType == null ? DoubleType.class : outType, in(), ref.getArgs()));
			}
		}

		getFeatureNames();
	}

	private List<NamedFeature> getFeatureNames() {
		if (featureNames == null) {
			final ArrayList<NamedFeature> list = new ArrayList<NamedFeature>();
			for (final OpRef<?> ref : opRefs) {
				list.add(new NamedFeature(ref.getLabel()));
			}

			return list;
		}
		return featureNames;
	}

	@Override
	public Map<NamedFeature, O> compute(final I input) {
		final Map<NamedFeature, O> res = new HashMap<NamedFeature, O>();

		for (final Entry<NamedFeature, FunctionOp<Object, ? extends O>> entry : namedFeatureMap.entrySet()) {
			res.put(entry.getKey(), evalFunction(entry.getValue(), input));
		}

		return res;
	}

	/**
	 * Can be overriden by implementors to provide specialized implementations
	 * for certain functions.
	 * 
	 * @param func
	 *            function used to compute output. Will be any function added as
	 *            OpRef.
	 * @param input
	 *            input object
	 * @return
	 */
	protected O evalFunction(final FunctionOp<Object, ? extends O> func, final I input) {
		return func.compute(input);
	}

	/**
	 * @return all {@link OpRef}s which are active!!!
	 */
	@Override
	public List<NamedFeature> getFeatures() {
		return getFeatureNames();
	}

	/**
	 * Adds an {@link OpRef} from the list of active features.
	 * 
	 * @param type
	 *            of the {@link Op}
	 * @param args
	 *            arguments without input and output
	 */
	private <OP extends Op> void activateFeature(final Class<OP> type, final Object... args) {
		activateFeature(new OpRef<OP>(type, args));
	}

	/**
	 * Adds an {@link OpRef} from the list of active features.
	 * 
	 * @param ref
	 *            {@link OpRef}
	 */
	private <OP extends Op> void activateFeature(final OpRef<OP> ref) {
		opRefs.add(ref);
	}

	/**
	 * Removes an {@link OpRef} from the list of active features.
	 * 
	 * @param type
	 *            of the {@link Op}
	 * 
	 * @param args
	 *            arguments without input and output
	 */
	private <OP extends Op> void deactivateFeature(final Class<OP> type, final Object... args) {
		deactivateFeature(new OpRef<OP>(type, args));
	}

	/**
	 * Removes an {@link OpRef} from the list of active features.
	 * 
	 * @param ref
	 *            of the {@link OpRef}
	 * 
	 * @param args
	 *            arguments without input and output
	 */
	private <OP extends Op> void deactivateFeature(final OpRef<OP> ref) {
		opRefs.remove(ref);
	}

	protected <OP extends Op> void handleStatus(final boolean active, final Class<OP> type, final Object... args) {
		if (active) {
			activateFeature(type, args);
		} else {
			deactivateFeature(type, args);
		}
	}

	/**
	 * @return {@link Collection} of {@link OpRef}s which will be calculated by
	 *         the {@link FeatureSet} (if activated).
	 */
	protected abstract void initOpRefs();

}
