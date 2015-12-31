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

package net.imagej.ops.features.sets;

import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} to calculate first order statistic features
 * 
 * @author Christian Dietz, University of Konstanz
 * @author jaywarrick
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Statistic Features", description = "Calculates the Statistic Features")
public class StatsFeatureSet<T, O extends RealType<O>> extends AbstractOpRefFeatureSet<IterableInterval<T>, O> {

	private static final String PKG = "net.imagej.ops.Ops$Stats$";
	
	@Parameter(required = false, label = "Min", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Min") })
	private boolean isMinActive = true;

	@Parameter(required = false, label = "Max", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Max") })
	private boolean isMaxActive = true;

	@Parameter(required = false, label = "Mean", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Mean") })
	private boolean isMeanActive = true;

	@Parameter(required = false, label = "Sum", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Sum") })
	private boolean isSumActive = true;

	@Parameter(required = false, label = "Skewness", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Skewness") })
	private boolean isSkewnessActive = true;

	@Parameter(required = false, label = "Median", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Median") })
	private boolean isMedianActive = true;

	@Parameter(required = false, label = "Kurtosis", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Kurtosis") })
	private boolean isKurtosisActive = true;

	@Parameter(required = false, label = "StdDev", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "StdDev") })
	private boolean isStdDevActive = true;

	@Parameter(required = false, label = "Variance", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Variance") })
	private boolean isVarianceActive = true;

	@Parameter(required = false, label = "SumOfLogs", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "SumOfLogs") })
	private boolean isSumOfLogsActive = true;

	@Parameter(required = false, label = "SumOfSquares", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "SumOfSquares") })
	private boolean isSumOfSquaresActive = true;

	@Parameter(required = false, label = "SumOfInverses", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "SumOfInverses") })
	private boolean isSumOfInversesActive = true;

	@Parameter(required = false, label = "Moment1AboutMean", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Moment1AboutMean") })
	private boolean isMoment1AboutMeanActive = true;

	@Parameter(required = false, label = "Moment2AboutMean", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Moment2AboutMean") })
	private boolean isMoment2AboutMeanActive = true;

	@Parameter(required = false, label = "Moment3AboutMean", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Moment3AboutMean") })
	private boolean isMoment3AboutMeanActive = true;

	@Parameter(required = false, label = "Moment4AboutMean", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Moment4AboutMean") })
	private boolean isMoment4AboutMeanActive = true;

	@Parameter(required = false, label = "HarmonicMean", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "HarmonicMean") })
	private boolean isHarmonicMeanActive = true;

	@Parameter(required = false, label = "GeometricMean", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "GeometricMean") })
	private boolean isGeometricMeanActive = true;

	public StatsFeatureSet() {
		// NB: Empty cofstruction
	}

}