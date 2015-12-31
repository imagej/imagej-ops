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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.scijava.command.CommandService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;

import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.type.numeric.RealType;

/**
 * {@link OpRef} based {@link AbstractCachedFeatureSet}.
 * 
 * @author Christian Dietz, University of Konstanz.
 * @param <I>
 *            type of the input
 * @param <O>
 *            type of the output
 */
public abstract class AbstractOpRefFeatureSet<I, O extends RealType<O>> extends AbstractFeatureSet<I, O> {

	protected final static String ATTR_FEATURE = "feature";

	protected final static String ATTR_TYPE = "feature_type";

	protected final static String ATTR_PARAMS = "feature_params";

	@Parameter
	private CommandService cs;

	@Parameter(required = false)
	private Class<? extends O> outType;

	// all features
	private Map<NamedFeature, UnaryFunctionOp<Object, ? extends O>> namedFeatureMap;

	@Override
	public List<NamedFeature> getFeatures() {

		final List<NamedFeature> features = new ArrayList<NamedFeature>();
		if (namedFeatureMap == null) {
			final Module self = cs.getCommand(this.getClass()).createModule(this);

			for (final ModuleItem<?> item : self.getInfo().inputs()) {
				// we found a feature. lets create a named feature!
				if (item.get(ATTR_FEATURE) != null && ((Boolean) item.getValue(self))) {
					features.add(new NamedFeature(item.getLabel()));
				}
			}
		} else {
			features.addAll(namedFeatureMap.keySet());
		}

		return features;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		super.initialize();

		namedFeatureMap = new LinkedHashMap<NamedFeature, UnaryFunctionOp<Object, ? extends O>>();

		final Module self = cs.getCommand(this.getClass()).createModule(this);
		try {
			for (final ModuleItem<?> item : self.getInfo().inputs()) {
				// we found a feature. lets create a named feature!
				if (item.get(ATTR_FEATURE) != null && ((Boolean) item.getValue(self))) {
					final String[] params;
					if(item.get(ATTR_PARAMS) != null)
					{
						params = item.get(ATTR_PARAMS).split(",");
					}
					else
					{
						params = new String[0];
					}
					
					final Object[] args = new Object[params.length];

					int i = 0;
					for (final String param : params) {
						args[i++] = self.getInput(param);
					}
					// make sure we have an outtype
					@SuppressWarnings("rawtypes") // workaround for class cast exception in maven
					final Class<? extends O> outType = this.outType == null ? (Class) RealType.class
							: this.outType;

					final OpRef<? extends Op> ref = OpRef.create((Class<? extends Op>) Class.forName((String) item.get(ATTR_TYPE)), args);

					namedFeatureMap.put(new NamedFeature(ref), (UnaryFunctionOp<Object, ? extends O>) Functions.unary(ops(), ref.getType(), outType, in(), ref.getArgs()));
				}
			}
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<NamedFeature, O> compute1(final I input) {
		final Map<NamedFeature, O> res = new HashMap<NamedFeature, O>();

		for (final Entry<NamedFeature, UnaryFunctionOp<Object, ? extends O>> entry : namedFeatureMap.entrySet()) {
			res.put(entry.getKey(), evalFunction(entry.getValue(), input));
		}

		return res;
	}

	/**
	 * Can be overriden by implementors to provide specialized implementations
	 * for certain functions. For example, this method calls func.compute1
	 * directly which bypasses input automatic input conversion. Thus, ops
	 * that are part of the feature set that require input conversion can produce
	 * a class cast exception.
	 * 
	 * Alternatively, one could call "ops().run(func, input)" at the expense
	 * of some speed (e.g., converting separately for individual features in
	 * the feature set can be costly).
	 * 
	 * @param func
	 *            function used to compute output. Will be any function added as
	 *            OpRef.
	 * @param input
	 *            input object
	 * @return
	 */
	protected O evalFunction(final UnaryFunctionOp<Object, ? extends O> func, final I input) {
		return func.compute1(input);
	}
}
