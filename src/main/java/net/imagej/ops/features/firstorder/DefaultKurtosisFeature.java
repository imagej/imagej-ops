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

package net.imagej.ops.features.firstorder;

import net.imagej.ops.Op;
import net.imagej.ops.OpUtils;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.KurtosisFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment4AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.StdDeviationFeature;
import net.imagej.ops.statistics.FirstOrderOps.Kurtosis;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Kurtosis}. Use {@link FeatureService} to
 * compile this {@link Op}.
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, name = Kurtosis.NAME, label = KurtosisFeature.LABEL, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultKurtosisFeature<O extends RealType<O>> implements
        KurtosisFeature<O> {

    @Parameter
    private StdDeviationFeature<O> stddev;

    @Parameter
    private Moment4AboutMeanFeature<O> moment4;

    @Parameter(type = ItemIO.OUTPUT)
    private O out;

    @Override
    public void run() {
        if (out == null) {
            out = OpUtils.<O> cast(new DoubleType());
        }

        out.setReal(Double.NaN);

        final double std = stddev.getOutput().getRealDouble();
        final double moment4 = this.moment4.getOutput().getRealDouble();

        if (std != 0) {
            out.setReal((moment4) / (std * std * std * std));
        }

    }

    @Override
    public O getOutput() {
        return out;
    }

    @Override
    public void setOutput(O output) {
        out = output;
    }
}
