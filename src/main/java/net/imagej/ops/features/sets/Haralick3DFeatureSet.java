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
import net.imagej.ops.features.AutoResolvingFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MaxFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MinFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.VarianceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ASMFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ClusterPromenenceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ClusterShadeFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ContrastFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.DifferenceEntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.DifferenceVarianceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.EntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ICM1Feature;
import net.imagej.ops.features.haralick.HaralickFeatures.ICM2Feature;
import net.imagej.ops.features.haralick.HaralickFeatures.IFDMFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.MaxProbabilityFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.SumAverageFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.SumEntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.SumVarianceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.TextureHomogenityFeature;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix3D;
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
@Plugin(type = FeatureSet.class, label = "Haralick Features")
public class Haralick3DFeatureSet<T> extends
AutoResolvingFeatureSet<IterableInterval<T>, DoubleType> implements Contingent {

	@Parameter(type = ItemIO.INPUT, label = "Number of Gray Levels", description = "Determines the size of the co-occurrence matrix", min = "1", max = "2147483647", stepSize = "1")
	private double nrGrayLevels = 8;

	@Parameter(type = ItemIO.INPUT, label = "Distance", description = "The distance at which the co-occurrence matrix is computed", min = "1", max = "2147483647", stepSize = "1")
	private double distance = 1;

	@Parameter(label = "Matrix Orientation", choices = { "HORIZONTAL",
			"VERTICAL", "DIAGONAL", "ANTIDIAGONAL", "HORIZONTAL_VERTICAL",
			"HORIZONTAL_DIAGONAL", "VERTICAL_VERTICAL", "VERTICAL_DIAGONAL",
			"DIAGONAL_VERTICAL", "DIAGONAL_DIAGONAL", "ANTIDIAGONAL_VERTICAL",
			"ANTIDIAGONAL_DIAGONAL", "DEPTH" })
	private String orientation;
	
	public void setDistance(final double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setOrientation(final String orientation) {
		this.orientation = orientation;
	}

	public String getOrientation() {
		return orientation;
	}

	public double getNrGrayLevels() {
		return nrGrayLevels;
	}

	public void setNrGrayLevels(final double nrGrayLevels) {
		this.nrGrayLevels = nrGrayLevels;
	}

	public Haralick3DFeatureSet() {

		addOutputOp(ASMFeature.class);
		addOutputOp(ClusterPromenenceFeature.class);
		addOutputOp(ClusterShadeFeature.class);
		addOutputOp(ContrastFeature.class);
		addOutputOp(DifferenceVarianceFeature.class);
		addOutputOp(DifferenceEntropyFeature.class);
		addOutputOp(EntropyFeature.class);
		addOutputOp(ICM1Feature.class);
		addOutputOp(ICM2Feature.class);
		addOutputOp(IFDMFeature.class);
		addOutputOp(SumAverageFeature.class);
		addOutputOp(SumEntropyFeature.class);
		addOutputOp(SumVarianceFeature.class);
		addOutputOp(VarianceFeature.class);
		addOutputOp(TextureHomogenityFeature.class);
		addOutputOp(MaxProbabilityFeature.class);

		// add cooc parameters
		addHiddenOp(CooccurrenceMatrix3D.class, getInput().getClass(),
				nrGrayLevels, distance, orientation, MinFeature.class,
				MaxFeature.class);
	}

	@Override
	public boolean conforms() {

		int count = 0;
		for (int d = 0; d < getInput().numDimensions(); d++) {
			count += getInput().dimension(d) > 1 ? 1 : 0;
		}

		return count == 3;
	}
}
