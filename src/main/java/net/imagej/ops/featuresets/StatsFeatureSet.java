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
import java.util.HashSet;
import java.util.Set;

import org.scijava.plugin.Plugin;

import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Stats.GeometricMean;
import net.imagej.ops.Ops.Stats.HarmonicMean;
import net.imagej.ops.Ops.Stats.Kurtosis;
import net.imagej.ops.Ops.Stats.Max;
import net.imagej.ops.Ops.Stats.Mean;
import net.imagej.ops.Ops.Stats.Median;
import net.imagej.ops.Ops.Stats.Min;
import net.imagej.ops.Ops.Stats.Moment1AboutMean;
import net.imagej.ops.Ops.Stats.Moment2AboutMean;
import net.imagej.ops.Ops.Stats.Moment3AboutMean;
import net.imagej.ops.Ops.Stats.Skewness;
import net.imagej.ops.Ops.Stats.StdDev;
import net.imagej.ops.Ops.Stats.Sum;
import net.imagej.ops.Ops.Stats.SumOfInverses;
import net.imagej.ops.Ops.Stats.SumOfLogs;
import net.imagej.ops.Ops.Stats.SumOfSquares;
import net.imagej.ops.Ops.Stats.Variance;
import net.imagej.ops.stats.StatOp;

/**
 * {@link FeatureSet} to calculate {@link StatOp}s.
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Statistic Features",
description = "Calculates the Statistic Features")
public class StatsFeatureSet<I, O> extends AbstractOpRefFeatureSet<I, O> {

	@Override
	protected Collection<? extends OpRef<?>> initOpRefs() {
		final Set<OpRef<?>> refs = new HashSet<OpRef<?>>();

		refs.add(ref(Mean.class));
		refs.add(ref(Min.class));
		refs.add(ref(Max.class));
		refs.add(ref(Sum.class));
		refs.add(ref(Median.class));
		refs.add(ref(Skewness.class));
		refs.add(ref(Kurtosis.class));
		refs.add(ref(StdDev.class));
		refs.add(ref(Variance.class));
		refs.add(ref(SumOfLogs.class));
		refs.add(ref(SumOfSquares.class));
		refs.add(ref(SumOfInverses.class));
		refs.add(ref(Moment1AboutMean.class));
		refs.add(ref(Moment2AboutMean.class));
		refs.add(ref(Moment3AboutMean.class));
		refs.add(ref(Moment3AboutMean.class));
		refs.add(ref(GeometricMean.class));
		refs.add(ref(HarmonicMean.class));

		return refs;
	}
}
