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

import net.imagej.ops.FunctionOp;
import net.imagej.ops.OpRef;
import net.imglib2.type.numeric.RealType;

import org.scijava.command.CommandService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;

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
	protected CommandService cs;

	@Parameter(required = false)
	protected Class<? extends O> outType;

	// all features
	protected Map<NamedFeature, FunctionOp<Object, ? extends O>> namedFeatureMap;

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

		namedFeatureMap = new LinkedHashMap<NamedFeature, FunctionOp<Object, ? extends O>>();

		final Module self = cs.getCommand(this.getClass()).createModule(this);
		try {
			for (final ModuleItem<?> item : self.getInfo().inputs()) {
				// we found a feature. lets create a named feature!
				if (item.get(ATTR_FEATURE) != null && ((Boolean) item.getValue(self))) {
					final String[] params;
					if(item.get(ATTR_PARAMS).equals(""))
					{
						params = new String[0];
					}
					else
					{
						params = item.get(ATTR_PARAMS).split(",");
					}
					final Object[] args = new Object[params.length];

					int i = 0;
					for (final String param : params) {
						args[i++] = self.getInput(param);
					}
					// make sure we have an outtype
					final Class<? extends O> outType = this.outType == null ? (Class<? extends O>) RealType.class
							: this.outType;

					@SuppressWarnings("rawtypes")
					final OpRef ref = new OpRef(Class.forName((String) item.get(ATTR_TYPE)), args);

					namedFeatureMap.put(new NamedFeature(ref), (FunctionOp<Object, ? extends O>) ops()
							.function(ref.getType(), outType, in(), ref.getArgs()));
				}
			}
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
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
}
