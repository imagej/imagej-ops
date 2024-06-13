/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

import net.imagej.ops.Op;
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
import net.imagej.ops.Ops.Stats.Size;
import net.imagej.ops.Ops.Stats.Skewness;
import net.imagej.ops.Ops.Stats.StdDev;
import net.imagej.ops.Ops.Stats.Sum;
import net.imagej.ops.Ops.Stats.SumOfInverses;
import net.imagej.ops.Ops.Stats.SumOfLogs;
import net.imagej.ops.Ops.Stats.SumOfSquares;
import net.imagej.ops.Ops.Stats.Variance;
import net.imagej.ops.features.sets.processors.ComputerSetProcessorUtils;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link FirstOrderStatsComputerSet}.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
@Plugin(type = ComputerSet.class, label = "First Order Statistic Computers")
@SuppressWarnings("rawtypes")
public class DefaultFirstOrderStatsComputerSet extends AbstractConfigurableComputerSet<Iterable, DoubleType>
		implements FirstOrderStatsComputerSet<Iterable, DoubleType> {

	public DefaultFirstOrderStatsComputerSet() {
		super(new DoubleType(), Iterable.class);
	}

	@SuppressWarnings("unused")
	@Override
	public void initialize() {
		if (false) {
			// Add special implementations if needed.
			// initialize(Arrays.asList(MinBasedOnMinMax.class,
			// MaxBasedOnMinMax.class));
		} else {
			super.initialize();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Op>[] getComputers() {
		return new Class[] { GeometricMean.class, HarmonicMean.class, Kurtosis.class, Mean.class, Median.class,
				Min.class, Max.class, Moment1AboutMean.class, Moment2AboutMean.class, Moment3AboutMean.class,
				Moment4AboutMean.class, Size.class, Skewness.class, StdDev.class, Sum.class, SumOfInverses.class,
				SumOfLogs.class, SumOfSquares.class, Variance.class };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Iterable getFakeInput() {
		return ComputerSetProcessorUtils.getIterable();
	}
}
