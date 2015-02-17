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
package net.imagej.ops.features.sets;

import java.util.HashSet;
import java.util.Set;

import net.imagej.ops.OpRef;
import net.imagej.ops.features.AbstractAutoResolvingFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.GeometricMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.HarmonicMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.KurtosisFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MaxFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MedianFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MinFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment1AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment2AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment3AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment4AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.SumFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.VarianceFeature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing all kinds of first order statistics.
 *
 * @author Christian Dietz (University of Konstanz)
 * @param <I>
 */
@Plugin(type = FeatureSet.class, label = "First Order Statistics", description = "Calculates the First Order Statistics Features")
public class FirstOrderStatFeatureSet<I> extends
		AbstractAutoResolvingFeatureSet<I, DoubleType> {

	@Override
	public Set<OpRef<?>> getOutputOps() {
		final HashSet<OpRef<?>> outputOps = new HashSet<OpRef<?>>();
		outputOps.add(createOpRef(MeanFeature.class));
		outputOps.add(createOpRef(SumFeature.class));
		outputOps.add(createOpRef(KurtosisFeature.class));
		outputOps.add(createOpRef(Moment1AboutMeanFeature.class));
		outputOps.add(createOpRef(Moment2AboutMeanFeature.class));
		outputOps.add(createOpRef(Moment3AboutMeanFeature.class));
		outputOps.add(createOpRef(Moment4AboutMeanFeature.class));
		outputOps.add(createOpRef(HarmonicMeanFeature.class));
		outputOps.add(createOpRef(GeometricMeanFeature.class));
		outputOps.add(createOpRef(VarianceFeature.class));
		outputOps.add(createOpRef(MedianFeature.class));
		outputOps.add(createOpRef(MinFeature.class));
		outputOps.add(createOpRef(MaxFeature.class));
		return outputOps;
	}

	@Override
	public Set<OpRef<?>> getHiddenOps() {
		return new HashSet<OpRef<?>>();
	}
}