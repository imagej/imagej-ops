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

import net.imagej.ops.Contingent;
import net.imagej.ops.features.AbstractFeatureSet;
import net.imagej.ops.features.Feature;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MaxFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MinFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ASMFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ClusterPromenenceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ClusterShadeFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ContrastFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.CorrelationFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.DifferenceEntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.DifferenceVarianceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.EntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ICM1Feature;
import net.imagej.ops.features.haralick.HaralickFeatures.ICM2Feature;
import net.imagej.ops.features.haralick.HaralickFeatures.IFDMFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.SumAverageFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.SumEntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.SumVarianceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.VarianceFeature;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;
import net.imglib2.IterableInterval;

import org.scijava.ItemIO;


import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing Haralick texture {@link Feature}s
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <I>
 */
@Plugin(type = FeatureSet.class, label = "Texture Features (Haralick)")
public class HaralickFeatureSet<T> extends
        AbstractFeatureSet<IterableInterval<T>> implements Contingent {

    @Parameter(type = ItemIO.INPUT, label = "Number of Gray Levels", description = "The dimensionality of the co-occurrence matrix.", min = "1", max = "2147483647", stepSize = "1")
    private Double nrGrayLevels = 8d;

    @Parameter(type = ItemIO.INPUT, label = "Distance", description = "The distance at which the co-occurrence matrix is computed.", min = "1", max = "2147483647", stepSize = "1")
    private Double distance = 1d;

    @Parameter(type = ItemIO.INPUT, label = "Orientation", description = "The orientation of the co-occurrence matrix.", choices = {
            "DIAGONAL", "ANTIDIAGONAL", "HORIZONTAL", "VERTICAL" })
    private String orientation = "HORIZONTAL";

    @Override
    protected void init() {
        addVisible(ASMFeature.class);
        addVisible(ClusterPromenenceFeature.class);
        addVisible(ClusterShadeFeature.class);
        addVisible(ContrastFeature.class);
        addVisible(CorrelationFeature.class);
        addVisible(DifferenceVarianceFeature.class);
        addVisible(DifferenceEntropyFeature.class);
        addVisible(EntropyFeature.class);
        addVisible(ICM1Feature.class);
        addVisible(ICM2Feature.class);
        addVisible(IFDMFeature.class);
        addVisible(SumAverageFeature.class);
        addVisible(SumEntropyFeature.class);
        addVisible(SumVarianceFeature.class);
        addVisible(VarianceFeature.class);

        // add cooc parameters
        addInvisible(CooccurrenceMatrix.class, getInput().getClass(),
                nrGrayLevels, distance, orientation, MinFeature.class,
                MaxFeature.class);
    }

    @Override
    public boolean conforms() {

        int count = 0;
        for (int d = 0; d < getInput().numDimensions(); d++) {
            count += getInput().dimension(d) > 1 ? 1 : 0;
        }

        return count == 2;
    }
}
