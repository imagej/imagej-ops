package net.imagej.ops.features.sets;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Filter.Variance;
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
import net.imagej.ops.Ops.Stats.Moment4AboutMean;
import net.imagej.ops.Ops.Stats.Skewness;
import net.imagej.ops.Ops.Stats.StdDev;
import net.imagej.ops.Ops.Stats.Sum;
import net.imagej.ops.Ops.Stats.SumOfInverses;
import net.imagej.ops.Ops.Stats.SumOfLogs;
import net.imagej.ops.Ops.Stats.SumOfSquares;
import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imglib2.type.numeric.RealType;

/**
 * {@link FeatureSet} to calculate first order statistic features
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Statistic Features", description = "Calculates the Statistic Features")
public class StatsFeatureSet<I, O extends RealType<O>> extends AbstractOpRefFeatureSet<I, O> {

	@Parameter(required = false, label = "Min")
	private boolean isMinActive = true;

	@Parameter(required = false, label = "Max")
	private boolean isMaxActive = true;

	@Parameter(required = false, label = "Mean")
	private boolean isMeanActive = true;

	@Parameter(required = false, label = "Sum")
	private boolean isSumActive = true;

	@Parameter(required = false, label = "Skewness")
	private boolean isSkewnessActive = true;

	@Parameter(required = false, label = "Median")
	private boolean isMedianActive = true;

	@Parameter(required = false, label = "Kurtosis")
	private boolean isKurtosisActive = true;

	@Parameter(required = false, label = "StdDev")
	private boolean isStdDevActive = true;

	@Parameter(required = false, label = "Variance")
	private boolean isVarianceActive = true;

	@Parameter(required = false, label = "SumOfLogs")
	private boolean isSumOfLogsActive = true;

	@Parameter(required = false, label = "SumOfSquares")
	private boolean isSumOfSquaresActive = true;

	@Parameter(required = false, label = "SumOfInverses")
	private boolean isSumOfInversesActive = true;

	@Parameter(required = false, label = "Moment1AboutMean")
	private boolean isMoment1AboutMeanActive = true;

	@Parameter(required = false, label = "Moment2AboutMean")
	private boolean isMoment2AboutMeanActive = true;

	@Parameter(required = false, label = "Moment3AboutMean")
	private boolean isMoment3AboutMeanActive = true;

	@Parameter(required = false, label = "Moment4AboutMean")
	private boolean isMoment4AboutMeanActive = true;

	@Parameter(required = false, label = "HarmonicMean")
	private boolean isHarmonicMeanActive = true;

	@Parameter(required = false, label = "GeometricMean")
	private boolean isGeometricMeanActive = true;

	public StatsFeatureSet() {
		// NB: Empty cofstruction
	}

	@Override
	protected void initOpRefs() {
		setMinActive(isMinActive);
		setMaxActive(isMaxActive);
		setMeanActive(isMeanActive);
		setSumActive(isSumActive);
		setSkewnessActive(isSkewnessActive);
		setMedianActive(isMedianActive);
		setKurtosisActive(isKurtosisActive);
		setStdDevActive(isStdDevActive);
		setVarianceActive(isVarianceActive);
		setSumOfLogsActive(isSumOfLogsActive);
		setSumOfSquaresActive(isSumOfSquaresActive);
		setSumOfInversesActive(isSumOfInversesActive);
		setMoment1AboutMeanActive(isMoment1AboutMeanActive);
		setMoment2AboutMeanActive(isMoment2AboutMeanActive);
		setMoment3AboutMeanActive(isMoment3AboutMeanActive);
		setMoment4AboutMeanActive(isMoment4AboutMeanActive);
		setHarmonicMeanActive(isHarmonicMeanActive);
		setGeometricMeanActive(isGeometricMeanActive);
	}

	public boolean isMinActive() {
		return isMinActive;
	}

	public void setMinActive(boolean isActive) {
		handleStatus(this.isMinActive = isActive, Min.class);
	}

	public boolean isMaxActive() {
		return isMaxActive;
	}

	public void setMaxActive(boolean isActive) {
		handleStatus(this.isMaxActive = isActive, Max.class);
	}

	public boolean isMeanActive() {
		return isMeanActive;
	}

	public void setMeanActive(boolean isActive) {
		handleStatus(this.isMeanActive = isActive, Mean.class);
	}

	public boolean isSumActive() {
		return isSumActive;
	}

	public void setSumActive(boolean isActive) {
		handleStatus(this.isSumActive = isActive, Sum.class);
	}

	public boolean isSkewnessActive() {
		return isSkewnessActive;
	}

	public void setSkewnessActive(boolean isActive) {
		handleStatus(this.isSkewnessActive = isActive, Skewness.class);
	}

	public boolean isMedianActive() {
		return isMedianActive;
	}

	public void setMedianActive(boolean isActive) {
		handleStatus(this.isMedianActive = isActive, Median.class);
	}

	public boolean isKurtosisActive() {
		return isKurtosisActive;
	}

	public void setKurtosisActive(boolean isActive) {
		handleStatus(this.isKurtosisActive = isActive, Kurtosis.class);
	}

	public boolean isStdDevActive() {
		return isStdDevActive;
	}

	public void setStdDevActive(boolean isActive) {
		handleStatus(this.isStdDevActive = isActive, StdDev.class);
	}

	public boolean isVarianceActive() {
		return isVarianceActive;
	}

	public void setVarianceActive(boolean isActive) {
		handleStatus(this.isVarianceActive = isActive, Variance.class);
	}

	public boolean isSumOfLogsActive() {
		return isSumOfLogsActive;
	}

	public void setSumOfLogsActive(boolean isActive) {
		handleStatus(this.isSumOfLogsActive = isActive, SumOfLogs.class);
	}

	public boolean isSumOfSquaresActive() {
		return isSumOfSquaresActive;
	}

	public void setSumOfSquaresActive(boolean isActive) {
		handleStatus(this.isSumOfSquaresActive = isActive, SumOfSquares.class);
	}

	public boolean isSumOfInversesActive() {
		return isSumOfInversesActive;
	}

	public void setSumOfInversesActive(boolean isActive) {
		handleStatus(this.isSumOfInversesActive = isActive, SumOfInverses.class);
	}

	public boolean isMoment1AboutMeanActive() {
		return isMoment1AboutMeanActive;
	}

	public void setMoment1AboutMeanActive(boolean isActive) {
		handleStatus(this.isMoment1AboutMeanActive = isActive, Moment1AboutMean.class);
	}

	public boolean isMoment2AboutMeanActive() {
		return isMoment2AboutMeanActive;
	}

	public void setMoment2AboutMeanActive(boolean isActive) {
		handleStatus(this.isMoment2AboutMeanActive = isActive, Moment2AboutMean.class);
	}

	public boolean isMoment3AboutMeanActive() {
		return isMoment3AboutMeanActive;
	}

	public void setMoment3AboutMeanActive(boolean isActive) {
		handleStatus(this.isMoment3AboutMeanActive = isActive, Moment3AboutMean.class);
	}

	public boolean isMoment4AboutMeanActive() {
		return isMoment4AboutMeanActive;
	}

	public void setMoment4AboutMeanActive(boolean isActive) {
		handleStatus(this.isMoment4AboutMeanActive = isActive, Moment4AboutMean.class);
	}

	public boolean isHarmonicMeanActive() {
		return isHarmonicMeanActive;
	}

	public void setHarmonicMeanActive(boolean isActive) {
		handleStatus(this.isHarmonicMeanActive = isActive, HarmonicMean.class);
	}

	public boolean isGeometricMeanActive() {
		return isGeometricMeanActive;
	}

	public void setGeometricMeanActive(boolean isActive) {
		handleStatus(this.isGeometricMeanActive = isActive, GeometricMean.class);
	}

}