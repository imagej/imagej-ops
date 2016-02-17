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
package net.imagej.ops.features.sets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.featuresets.AbstractFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imagej.ops.featuresets.NamedFeature;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} to calculate {@link AbstractOpRefFeatureSet<I, O>}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 * @author jaywarrick
 * @param <I>
 * @param <O>
 */
@SuppressWarnings("rawtypes")
@Plugin(type = FeatureSet.class, label = "Centroid", description = "Calculates the Centroid")
public class CentroidFeatureSet extends AbstractFeatureSet<LabelRegion, DoubleType> {

	@Override
	public List<NamedFeature> getFeatures() {
		List<NamedFeature> fs = new ArrayList<NamedFeature>();

		for (int i = 0; i < in().numDimensions(); i++) {
			fs.add(new NamedFeature("Centroid of dimension#" + i));
		}
		return fs;
	}

	@Override
	public Map<NamedFeature, DoubleType> compute1(LabelRegion input) {
		Map<NamedFeature, DoubleType> res = new LinkedHashMap<NamedFeature, DoubleType>();
		RealLocalizable centroid = ops().geom().centroid(input);

		for (int i = 0; i < getFeatures().size(); i++) {
			res.put(new NamedFeature("Centroid of dimension#" + i), new DoubleType(centroid.getDoublePosition(i)));
		}

		return res;
	}

}
