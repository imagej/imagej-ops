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

import net.imagej.ops.Contingent;
import net.imagej.ops.OpRef;
import net.imagej.ops.features.AbstractAutoResolvingFeatureSet;
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
import net.imglib2.type.numeric.real.DoubleType;

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
@Plugin(type = FeatureSet.class, label = "Haralick Features", description = "Calculates the Haralick Features")
public class HaralickFeatureSet<T> extends
		AbstractAutoResolvingFeatureSet<IterableInterval<T>, DoubleType>
		implements Contingent {

	@Parameter(type = ItemIO.INPUT, label = "Number of Gray Levels", description = "Determines the size of the co-occurrence matrix", min = "1", max = "2147483647", stepSize = "1")
	private double nrGrayLevels = 8;

	@Parameter(type = ItemIO.INPUT, label = "Distance", description = "The distance at which the co-occurrence matrix is computed", min = "1", max = "2147483647", stepSize = "1")
	private double distance = 1;

	@Parameter(type = ItemIO.INPUT, label = "Matrix Orientation", choices = {
			"DIAGONAL", "ANTIDIAGONAL", "HORIZONTAL", "VERTICAL" })
	private String orientation = "HORIZONTAL";

	public void setDistance(final double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return this.distance;
	}

	public void setOrientation(final String orientation) {
		this.orientation = orientation;
	}

	public String getOrientation() {
		return this.orientation;
	}

	public double getNrGrayLevels() {
		return this.nrGrayLevels;
	}

	public void setNrGrayLevels(final double nrGrayLevels) {
		this.nrGrayLevels = nrGrayLevels;
	}

	@Override
	public boolean conforms() {

		int count = 0;
		for (int d = 0; d < getInput().numDimensions(); d++) {
			count += getInput().dimension(d) > 1 ? 1 : 0;
		}

		return count == 2;
	}

	@Override
	public Set<OpRef<?>> getOutputOps() {

		final HashSet<OpRef<?>> outputOps = new HashSet<OpRef<?>>();
		outputOps.add(createOpRef(ASMFeature.class));
		outputOps.add(createOpRef(ClusterPromenenceFeature.class));
		outputOps.add(createOpRef(ClusterShadeFeature.class));
		outputOps.add(createOpRef(ContrastFeature.class));
		outputOps.add(createOpRef(CorrelationFeature.class));
		outputOps.add(createOpRef(DifferenceVarianceFeature.class));
		outputOps.add(createOpRef(DifferenceEntropyFeature.class));
		outputOps.add(createOpRef(EntropyFeature.class));
		outputOps.add(createOpRef(ICM1Feature.class));
		outputOps.add(createOpRef(ICM2Feature.class));
		outputOps.add(createOpRef(IFDMFeature.class));
		outputOps.add(createOpRef(SumAverageFeature.class));
		outputOps.add(createOpRef(SumEntropyFeature.class));
		outputOps.add(createOpRef(IFDMFeature.class));
		outputOps.add(createOpRef(SumVarianceFeature.class));
		outputOps.add(createOpRef(VarianceFeature.class));

		return outputOps;
	}

	@Override
	public Set<OpRef<?>> getHiddenOps() {
		final HashSet<OpRef<?>> hiddenOps = new HashSet<OpRef<?>>();
		hiddenOps.add(createOpRef(CooccurrenceMatrix.class,
				IterableInterval.class, this.nrGrayLevels, this.distance,
				this.orientation, MinFeature.class, MaxFeature.class));
		return hiddenOps;
	}

}
