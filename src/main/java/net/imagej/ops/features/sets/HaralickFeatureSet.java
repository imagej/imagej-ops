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

import net.imagej.ops.features.AbstractFeatureSet;
import net.imagej.ops.features.Feature;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickASMFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickClusterPromenenceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickClusterShadeFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickContrastFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickCorrelationFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickDifferenceEntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickDifferenceVarianceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickEntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickICM1Feature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickICM2Feature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickIFDMFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickSumAverageFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickSumEntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickSumVarianceFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickVarianceFeature;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;

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
public class HaralickFeatureSet<I> extends AbstractFeatureSet<I> {

	@Parameter
	private double nrGrayLevels = 8;

	@Parameter
	private double distance = 1;

	@Parameter
	private String orientation = "HORIZONTAL";

	public HaralickFeatureSet() {
		super();

		addVisible(HaralickASMFeature.class);
		addVisible(HaralickClusterPromenenceFeature.class);
		addVisible(HaralickClusterShadeFeature.class);
		addVisible(HaralickContrastFeature.class);
		addVisible(HaralickCorrelationFeature.class);
		addVisible(HaralickDifferenceVarianceFeature.class);
		addVisible(HaralickDifferenceEntropyFeature.class);
		addVisible(HaralickEntropyFeature.class);
		addVisible(HaralickICM1Feature.class);
		addVisible(HaralickICM2Feature.class);
		addVisible(HaralickIFDMFeature.class);
		addVisible(HaralickSumAverageFeature.class);
		addVisible(HaralickSumEntropyFeature.class);
		addVisible(HaralickSumVarianceFeature.class);
		addVisible(HaralickVarianceFeature.class);

		// add cooc parameters
		addInvisible(CooccurrenceMatrix.class, nrGrayLevels, distance,
				orientation);
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getOrientation() {
		return orientation;
	}

	public double getNrGrayLevels() {
		return nrGrayLevels;
	}

	public void setNrGrayLevels(double nrGrayLevels) {
		this.nrGrayLevels = nrGrayLevels;
	}
}
