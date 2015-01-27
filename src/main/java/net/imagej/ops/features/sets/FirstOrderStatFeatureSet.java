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

import net.imagej.ops.Op;
import net.imagej.ops.features.AbstractFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.GeometricMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.HarmonicMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.KurtosisFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MaxFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MedianFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MinFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment1AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment3AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment4AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.SkewnessFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.StdDeviationFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.SumFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.SumOfInversesFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.SumOfLogsFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.SumOfSquaresFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.VarianceFeature;

import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing all kinds of first order statistics.
 * 
 * TODO: Add generic PercentileFeature
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <I>
 */
@Plugin(type = FeatureSet.class, label = "First Order Statistics")
public class FirstOrderStatFeatureSet<I> extends AbstractFeatureSet<I> {

    public Set<Class<? extends Op>> getPossibleFeatures() {
        Set<Class<? extends Op>> features = new HashSet<Class<? extends Op>>();

        features.add(MaxFeature.class);
        features.add(MinFeature.class);
        features.add(MeanFeature.class);
        features.add(MedianFeature.class);
        features.add(StdDeviationFeature.class);
        features.add(SumFeature.class);
        features.add(VarianceFeature.class);
        features.add(GeometricMeanFeature.class);
        features.add(KurtosisFeature.class);
        features.add(Moment1AboutMeanFeature.class);
        features.add(Moment3AboutMeanFeature.class);
        features.add(Moment4AboutMeanFeature.class);
        features.add(HarmonicMeanFeature.class);
        features.add(SkewnessFeature.class);
        features.add(KurtosisFeature.class);
        features.add(SumOfInversesFeature.class);
        features.add(SumOfLogsFeature.class);
        features.add(SumOfSquaresFeature.class);

        return features;
    }

    // @Parameter
    // private double[] percentiles = new double[] { 50 };

    protected void init() {
        for (Class<? extends Op> feature : getPossibleFeatures()) {
            addVisible(feature);
        }
    }

    // public double[] getPercentiles() {
    // return percentiles;
    // }
    //
    // public void setPercentiles(double[] percentiles) {
    // this.percentiles = percentiles;
    // }
}
