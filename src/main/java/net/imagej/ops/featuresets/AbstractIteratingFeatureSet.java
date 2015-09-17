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
import java.util.Map;

/**
 * {@link FeatureSet} to calculate features which are organized in a structure,
 * accessible via an index. Typical example is {@link HistogramFeatureSet}.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
public abstract class AbstractIteratingFeatureSet<I, O> extends
	AbstractCachedFeatureSet<I, O>implements FeatureSet<I, O>
{

	private ArrayList<NamedFeature> infos;

	@Override
	public void initialize() {
		super.initialize();

		infos = new ArrayList<NamedFeature>();

		for (int i = 0; i < getNumEntries(); i++) {
			final int f = i;
			infos.add(new NamedFeature() {

				@Override
				public String getName() {
					return getNamePrefix() + " " + f;
				}

				@Override
				public void setName(String name) {
					throw new UnsupportedOperationException("setName not supported");
				}
			});
		}
	}

	@Override
	public Map<NamedFeature, O> compute(final I input) {
		final Map<NamedFeature, O> res = new HashMap<NamedFeature, O>();

		preCompute(input);

		int i = 0;
		for (final NamedFeature info : infos) {
			res.put(info, getResultAtIndex(i));
			i++;
		}

		return res;
	}

	@Override
	public Collection<NamedFeature> getFeatures() {
		return infos;
	}

	/**
	 * Called once before getResultAtIndex is called subsequently for each entry
	 * in the {@link FeatureSet}.
	 * 
	 * @param input the input
	 */
	protected abstract void preCompute(final I input);

	/**
	 * @param i index
	 * @return result hat given index
	 */
	protected abstract O getResultAtIndex(int i);

	/**
	 * @return number of entries in feature-set
	 */
	protected abstract int getNumEntries();

	/**
	 * @return semantic label prefix for the entries of the feature-set
	 */
	protected abstract String getNamePrefix();
}
