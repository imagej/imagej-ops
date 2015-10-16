package net.imagej.ops.features.sets;
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

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Haralick.ASM;
import net.imagej.ops.Ops.Haralick.ClusterPromenence;
import net.imagej.ops.Ops.Haralick.ClusterShade;
import net.imagej.ops.Ops.Haralick.Contrast;
import net.imagej.ops.Ops.Haralick.Correlation;
import net.imagej.ops.Ops.Haralick.DifferenceEntropy;
import net.imagej.ops.Ops.Haralick.DifferenceVariance;
import net.imagej.ops.Ops.Haralick.Entropy;
import net.imagej.ops.Ops.Haralick.ICM1;
import net.imagej.ops.Ops.Haralick.ICM2;
import net.imagej.ops.Ops.Haralick.IFDM;
import net.imagej.ops.Ops.Haralick.MaxProbability;
import net.imagej.ops.Ops.Haralick.SumAverage;
import net.imagej.ops.Ops.Haralick.SumEntropy;
import net.imagej.ops.Ops.Haralick.SumVariance;
import net.imagej.ops.Ops.Haralick.TextureHomogeneity;
import net.imagej.ops.Ops.Haralick.Variance;
import net.imagej.ops.features.haralick.HaralickFeature;
import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
import net.imagej.ops.featuresets.DimensionBoundFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imagej.ops.image.cooccurrencematrix.MatrixOrientation2D;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

/**
 * {@link FeatureSet} for {@link HaralickFeature}s
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <T>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Haralick 2D Features", description = "Calculates the 2D Haralick Features")
public class Haralick2DFeatureSet<T, O extends RealType<O>> extends AbstractOpRefFeatureSet<IterableInterval<T>, O>
		implements Contingent, DimensionBoundFeatureSet<IterableInterval<T>, O> {

	@Parameter(type = ItemIO.INPUT, label = "Num. Grey Levels", description = "The number of grey values determines the size of the co-occurence matrix on which the Haralick features are calculated.", min = "1", max = "2147483647", stepSize = "1")
	private int numGreyLevels = 32;

	@Parameter(type = ItemIO.INPUT, label = "Distance", description = "The maximum distance between pairs of pixels which will be added to the co-occurence matrix.", min = "1", max = "2147483647", stepSize = "1")
	private int distance = 1;

	@Parameter(type = ItemIO.INPUT, label = "Orientation", description = "Orientation of the pairs of pixels which will be added to the co-occurence matrix", choices = {
			"DIAGONAL", "ANTIDIAGONAL", "HORIZONTAL", "VERTICAL" })
	private String orientation = "HORIZONTAL";

	@Parameter(required = false, label = "ASM")
	private boolean isASMActive = true;

	@Parameter(required = false, label = "Cluster Promenence")
	private boolean isClusterPromenenceActive = true;

	@Parameter(required = false, label = "Cluster Shade")
	private boolean isClusterShadeActive = true;

	@Parameter(required = false, label = "Cluster Contrast")
	private boolean isContrastActive = true;

	@Parameter(required = false, label = "Correlation")
	private boolean isCorrelationActive = true;

	@Parameter(required = false, label = "Difference Entropy")
	private boolean isDifferenceEntropyActive = true;

	@Parameter(required = false, label = "Difference Variance")
	private boolean isDifferenceVarianceActive = true;

	@Parameter(required = false, label = "Entropy")
	private boolean isEntropyActive = true;

	@Parameter(required = false, label = "ICM1")
	private boolean isICM1Active = true;

	@Parameter(required = false, label = "ICM2")
	private boolean isICM2Active = true;

	@Parameter(required = false, label = "IFDM")
	private boolean isIFDMActive = true;

	@Parameter(required = false, label = "Max Probability")
	private boolean isMaxProbabilityActive = true;

	@Parameter(required = false, label = "Sum Average")
	private boolean isSumAverageActive = true;

	@Parameter(required = false, label = "Sum Entropy")
	private boolean isSumEntropyActive = true;

	@Parameter(required = false, label = "Sum Variance")
	private boolean isSumVarianceActive = true;

	@Parameter(required = false, label = "Texture Homogenity")
	private boolean isTextureHomogeneityActive = true;

	@Parameter(required = false, label = "Variance")
	private boolean isVarianceActive = true;

	public int getNumGreyLevels() {
		return numGreyLevels;
	}

	public void setNumGreyLevels(int numGreyLevels) {
		this.numGreyLevels = numGreyLevels;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public MatrixOrientation2D getOrientation() {
		return MatrixOrientation2D.valueOf(orientation);
	}

	public void setOrientation(MatrixOrientation2D orientation) {
		this.orientation = orientation.toString();
	}

	public boolean isASMActive() {
		return isASMActive;
	}

	public void setASMActive(boolean isASMActive) {
		if (isASMActive && !this.isASMActive) {
			activateFeature(ASM.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isASMActive = isASMActive;
	}

	public boolean isClusterPromenenceActive() {
		return isClusterPromenenceActive;
	}

	public void setClusterPromenenceActive(boolean isClusterPromenenceActive) {
		if (isClusterPromenenceActive && !this.isClusterPromenenceActive) {
			activateFeature(ClusterPromenence.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isClusterPromenenceActive = isClusterPromenenceActive;
	}

	public boolean isClusterShadeActive() {
		return isClusterShadeActive;
	}

	public void setClusterShadeActive(boolean isClusterShadeActive) {
		if (isClusterShadeActive && !this.isClusterShadeActive) {
			activateFeature(ClusterShade.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isClusterShadeActive = isClusterShadeActive;
	}

	public boolean isContrastActive() {
		return isContrastActive;
	}

	public void setContrastActive(boolean isContrastActive) {
		if (isContrastActive && !this.isContrastActive) {
			activateFeature(Contrast.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isContrastActive = isContrastActive;
	}

	public boolean isCorrelationActive() {
		return isCorrelationActive;
	}

	public void setCorrelationActive(boolean isCorrelationActive) {
		if (isCorrelationActive && !this.isCorrelationActive) {
			activateFeature(Correlation.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isCorrelationActive = isCorrelationActive;
	}

	public boolean isDifferenceEntropyActive() {
		return isDifferenceEntropyActive;
	}

	public void setDifferenceEntropyActive(boolean isDifferenceEntropyActive) {
		if (isDifferenceEntropyActive && !this.isDifferenceEntropyActive) {
			activateFeature(DifferenceEntropy.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isDifferenceEntropyActive = isDifferenceEntropyActive;
	}

	public boolean isDifferenceVarianceActive() {
		return isDifferenceVarianceActive;
	}

	public void setDifferenceVarianceActive(boolean isDifferenceVarianceActive) {
		if (isDifferenceVarianceActive && !this.isDifferenceVarianceActive) {
			activateFeature(DifferenceVariance.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isDifferenceVarianceActive = isDifferenceVarianceActive;
	}

	public boolean isEntropyActive() {
		return isEntropyActive;
	}

	public void setEntropyActive(boolean isEntropyActive) {
		if (isEntropyActive && !this.isEntropyActive) {
			activateFeature(Entropy.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isEntropyActive = isEntropyActive;
	}

	public boolean isICM1Active() {
		return isICM1Active;
	}

	public void setICM1Active(boolean isICM1Active) {
		if (isICM1Active && !this.isICM1Active) {
			activateFeature(ICM1.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isICM1Active = isICM1Active;
	}

	public boolean isICM2Active() {
		return isICM2Active;
	}

	public void setICM2Active(boolean isICM2Active) {
		if (isICM2Active && !this.isICM2Active) {
			activateFeature(ICM2.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isICM2Active = isICM2Active;
	}

	public boolean isIFDMActive() {
		return isIFDMActive;
	}

	public void setIFDMActive(boolean isIFDMActive) {
		if (isIFDMActive && !this.isIFDMActive) {
			activateFeature(IFDM.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isIFDMActive = isIFDMActive;
	}

	public boolean isMaxProbabilityActive() {
		return isMaxProbabilityActive;
	}

	public void setMaxProbabilityActive(boolean isMaxProbabilityActive) {
		if (isMaxProbabilityActive && !this.isMaxProbabilityActive) {
			activateFeature(MaxProbability.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isMaxProbabilityActive = isMaxProbabilityActive;
	}

	public boolean isSumAverageActive() {
		return isSumAverageActive;
	}

	public void setSumAverageActive(boolean isSumAverageActive) {
		if (isSumAverageActive && !this.isSumAverageActive) {
			activateFeature(SumAverage.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isSumAverageActive = isSumAverageActive;
	}

	public boolean isSumEntropyActive() {
		return isSumEntropyActive;
	}

	public void setSumEntropyActive(boolean isSumEntropyActive) {
		if (isSumEntropyActive && !this.isSumEntropyActive) {
			activateFeature(SumEntropy.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isSumEntropyActive = isSumEntropyActive;
	}

	public boolean isSumVarianceActive() {
		return isSumVarianceActive;
	}

	public void setSumVarianceActive(boolean isSumVarianceActive) {
		if (isSumVarianceActive && !this.isSumVarianceActive) {
			activateFeature(SumVariance.class, getNumGreyLevels(), getDistance(), getOrientation());
		}
		this.isSumVarianceActive = isSumVarianceActive;
	}

	public boolean isTextureHomogeneityActive() {
		return isTextureHomogeneityActive;
	}

	public void setTextureHomogeneityActive(boolean isTextureHomogeneityActive) {
		if (isTextureHomogeneityActive && !this.isTextureHomogeneityActive) {
			activateFeature(TextureHomogeneity.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isTextureHomogeneityActive = isTextureHomogeneityActive;
	}

	public boolean isVarianceActive() {
		return isVarianceActive;
	}

	public void setVarianceActive(boolean isVarianceActive) {
		if (isVarianceActive && !this.isVarianceActive) {
			activateFeature(Variance.class, getNumGreyLevels(), getDistance(), getOrientation());
		}

		this.isVarianceActive = isVarianceActive;
	}

	@Override
	protected void initOpRefs() {
		setASMActive(isASMActive);
		setClusterPromenenceActive(isClusterPromenenceActive);
		setClusterShadeActive(isClusterShadeActive);
		setContrastActive(isContrastActive);
		setCorrelationActive(isCorrelationActive);
		setDifferenceEntropyActive(isDifferenceEntropyActive);
		setDifferenceVarianceActive(isDifferenceVarianceActive);
		setEntropyActive(isEntropyActive);
		setICM1Active(isICM1Active);
		setICM2Active(isICM2Active);
		setIFDMActive(isIFDMActive);
		setMaxProbabilityActive(isMaxProbabilityActive);
		setSumAverageActive(isSumAverageActive);
		setSumEntropyActive(isSumEntropyActive);
		setSumVarianceActive(isSumVarianceActive);
		setTextureHomogeneityActive(isTextureHomogeneityActive);
		setVarianceActive(isVarianceActive);
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

	@Override
	public int getMinDimensions() {
		return 2;
	}

	@Override
	public int getMaxDimensions() {
		return 2;
	}

}
