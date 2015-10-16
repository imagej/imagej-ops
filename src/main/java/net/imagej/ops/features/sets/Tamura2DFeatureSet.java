package net.imagej.ops.features.sets;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Tamura.Coarseness;
import net.imagej.ops.Ops.Tamura.Contrast;
import net.imagej.ops.Ops.Tamura.Directionality;
import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
import net.imagej.ops.featuresets.DimensionBoundFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

/**
 * {@link FeatureSet} to calculate Tamura 2D Features
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Tamura Features", description = "Calculates the Tamura Features")
public class Tamura2DFeatureSet<T, O extends RealType<O>>
		extends AbstractOpRefFeatureSet<RandomAccessibleInterval<T>, O>
		implements Contingent, DimensionBoundFeatureSet<RandomAccessibleInterval<T>, O> {

	@Parameter(type = ItemIO.INPUT, label = "Histogram Size (Directionality)", description = "The size of the histogram used by the directionality feature.", min = "1", max = "2147483647", stepSize = "1")
	private int histogramSize = 16;

	@Parameter(required = false, label = "Coarseness")
	private boolean isCoarsenessActive = true;

	@Parameter(required = false, label = "Contrast")
	private boolean isContrastActive = true;

	@Parameter(required = false, label = "Directionality")
	private boolean isDirectionalityActive = true;

	public int getHistogramSize() {
		return histogramSize;
	}

	public void setHistogramSize(int histogramSize) {
		this.histogramSize = histogramSize;
	}

	public boolean isCoarsenessActive() {
		return isCoarsenessActive;
	}

	public void setCoarsenessActive(boolean isCoarsenessActive) {
		handleStatus(this.isCoarsenessActive = isCoarsenessActive, Coarseness.class);
	}

	public boolean isContrastActive() {
		return isContrastActive;
	}

	public void setContrastActive(boolean isContrastActive) {
		handleStatus(this.isContrastActive = isContrastActive, Contrast.class);
	}

	public boolean isDirectionalityActive() {
		return isDirectionalityActive;
	}

	public void setDirectionalityActive(boolean isDirectionalityActive) {
		handleStatus(this.isDirectionalityActive = isDirectionalityActive, Directionality.class);
	}

	@Override
	protected void initOpRefs() {
		setCoarsenessActive(isCoarsenessActive);
		setDirectionalityActive(isDirectionalityActive);
		setContrastActive(isContrastActive);
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
