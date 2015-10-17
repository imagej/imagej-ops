///*
// * #%L
// * ImageJ software for multidimensional image processing and analysis.
// * %%
// * Copyright (C) 2014 - 2015 Board of Regents of the University of
// * Wisconsin-Madison, University of Konstanz and Brian Northan.
// * %%
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are met:
// * 
// * 1. Redistributions of source code must retain the above copyright notice,
// *    this list of conditions and the following disclaimer.
// * 2. Redistributions in binary form must reproduce the above copyright notice,
// *    this list of conditions and the following disclaimer in the documentation
// *    and/or other materials provided with the distribution.
// * 
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
// * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// * POSSIBILITY OF SUCH DAMAGE.
// * #L%
// */
//
//package net.imagej.ops.features.sets;
//
//import org.scijava.plugin.Parameter;
//import org.scijava.plugin.Plugin;
//
//import net.imagej.ops.Ops.Filter.Variance;
//import net.imagej.ops.Ops.Stats.GeometricMean;
//import net.imagej.ops.Ops.Stats.HarmonicMean;
//import net.imagej.ops.Ops.Stats.Kurtosis;
//import net.imagej.ops.Ops.Stats.Max;
//import net.imagej.ops.Ops.Stats.Mean;
//import net.imagej.ops.Ops.Stats.Median;
//import net.imagej.ops.Ops.Stats.Min;
//import net.imagej.ops.Ops.Stats.Moment1AboutMean;
//import net.imagej.ops.Ops.Stats.Moment2AboutMean;
//import net.imagej.ops.Ops.Stats.Moment3AboutMean;
//import net.imagej.ops.Ops.Stats.Moment4AboutMean;
//import net.imagej.ops.Ops.Stats.Skewness;
//import net.imagej.ops.Ops.Stats.StdDev;
//import net.imagej.ops.Ops.Stats.Sum;
//import net.imagej.ops.Ops.Stats.SumOfInverses;
//import net.imagej.ops.Ops.Stats.SumOfLogs;
//import net.imagej.ops.Ops.Stats.SumOfSquares;
//import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
//import net.imagej.ops.featuresets.FeatureSet;
//import net.imglib2.type.numeric.RealType;
//
///**
// * {@link FeatureSet} to calculate first order statistic features
// * 
// * @author Christian Dietz, University of Konstanz
// *
// * @param <I>
// * @param <O>
// */
//@Plugin(type = FeatureSet.class, label = "Statistic Features", description = "Calculates the Statistic Features")
//public class StatsFeatureSet<I, O extends RealType<O>> extends AbstractOpRefFeatureSet<I, O> {
//
//	@Parameter(required = false, label = "Min")
//	private boolean isMinActive = true;
//
//	@Parameter(required = false, label = "Max")
//	private boolean isMaxActive = true;
//
//	@Parameter(required = false, label = "Mean")
//	private boolean isMeanActive = true;
//
//	@Parameter(required = false, label = "Sum")
//	private boolean isSumActive = true;
//
//	@Parameter(required = false, label = "Skewness")
//	private boolean isSkewnessActive = true;
//
//	@Parameter(required = false, label = "Median")
//	private boolean isMedianActive = true;
//
//	@Parameter(required = false, label = "Kurtosis")
//	private boolean isKurtosisActive = true;
//
//	@Parameter(required = false, label = "StdDev")
//	private boolean isStdDevActive = true;
//
//	@Parameter(required = false, label = "Variance")
//	private boolean isVarianceActive = true;
//
//	@Parameter(required = false, label = "SumOfLogs")
//	private boolean isSumOfLogsActive = true;
//
//	@Parameter(required = false, label = "SumOfSquares")
//	private boolean isSumOfSquaresActive = true;
//
//	@Parameter(required = false, label = "SumOfInverses")
//	private boolean isSumOfInversesActive = true;
//
//	@Parameter(required = false, label = "Moment1AboutMean")
//	private boolean isMoment1AboutMeanActive = true;
//
//	@Parameter(required = false, label = "Moment2AboutMean")
//	private boolean isMoment2AboutMeanActive = true;
//
//	@Parameter(required = false, label = "Moment3AboutMean")
//	private boolean isMoment3AboutMeanActive = true;
//
//	@Parameter(required = false, label = "Moment4AboutMean")
//	private boolean isMoment4AboutMeanActive = true;
//
//	@Parameter(required = false, label = "Harmonic Mean")
//	private boolean isHarmonicMeanActive = true;
//
//	@Parameter(required = false, label = "Geometric Mean")
//	private boolean isGeometricMeanActive = true;
//
//	public StatsFeatureSet() {
//		// NB: Empty cofstruction
//	}
//
//	@Override
//	protected void initFeatures() {
//		setFeature(isMinActive, Min.class);
//		setFeature(isMaxActive, Max.class);
//		setFeature(isMeanActive, Mean.class);
//		setFeature(isSumActive, Sum.class);
//		setFeature(isSkewnessActive, Skewness.class);
//		setFeature(isMedianActive, Median.class);
//		setFeature(isKurtosisActive, Kurtosis.class);
//		setFeature(isStdDevActive, StdDev.class);
//		setFeature(isVarianceActive, Variance.class);
//		setFeature(isSumOfLogsActive, SumOfLogs.class);
//		setFeature(isSumOfSquaresActive, SumOfSquares.class);
//		setFeature(isSumOfInversesActive, SumOfInverses.class);
//		setFeature(isMoment1AboutMeanActive, Moment1AboutMean.class);
//		setFeature(isMoment2AboutMeanActive, Moment2AboutMean.class);
//		setFeature(isMoment3AboutMeanActive, Moment3AboutMean.class);
//		setFeature(isMoment4AboutMeanActive, Moment4AboutMean.class);
//		setFeature(isHarmonicMeanActive, HarmonicMean.class);
//		setFeature(isGeometricMeanActive, GeometricMean.class);
//	}
//
//}