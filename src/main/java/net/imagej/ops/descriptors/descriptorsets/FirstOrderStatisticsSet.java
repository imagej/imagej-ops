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

package net.imagej.ops.descriptors.descriptorsets;

import net.imagej.ops.descriptors.AbstractDoubleDescSet;
import net.imagej.ops.descriptors.firstorderstatistics.GeometricMean;
import net.imagej.ops.descriptors.firstorderstatistics.HarmonicMean;
import net.imagej.ops.descriptors.firstorderstatistics.Kurtosis;
import net.imagej.ops.descriptors.firstorderstatistics.Max;
import net.imagej.ops.descriptors.firstorderstatistics.Mean;
import net.imagej.ops.descriptors.firstorderstatistics.Min;
import net.imagej.ops.descriptors.firstorderstatistics.Moment1AboutMean;
import net.imagej.ops.descriptors.firstorderstatistics.Moment2AboutMean;
import net.imagej.ops.descriptors.firstorderstatistics.Moment3AboutMean;
import net.imagej.ops.descriptors.firstorderstatistics.Moment4AboutMean;
import net.imagej.ops.descriptors.firstorderstatistics.Percentile;
import net.imagej.ops.descriptors.firstorderstatistics.Skewness;
import net.imagej.ops.descriptors.firstorderstatistics.StdDev;
import net.imagej.ops.descriptors.firstorderstatistics.Sum;
import net.imagej.ops.descriptors.firstorderstatistics.SumOfInverses;
import net.imagej.ops.descriptors.firstorderstatistics.SumOfLogs;
import net.imagej.ops.descriptors.firstorderstatistics.SumOfSquares;
import net.imagej.ops.descriptors.firstorderstatistics.Variance;

import org.scijava.Context;

/**
 * Descriptor Set for First Order Statistics
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 */
public class FirstOrderStatisticsSet extends AbstractDoubleDescSet {

	public FirstOrderStatisticsSet(final Context context) {
		super(context);

		addOp(GeometricMean.class);
		addOp(Max.class);
		addOp(Min.class);
		addOp(HarmonicMean.class);
		addOp(Kurtosis.class);
		addOp(Mean.class);
		addOp(Moment1AboutMean.class);
		addOp(Moment2AboutMean.class);
		addOp(Moment3AboutMean.class);
		addOp(Moment4AboutMean.class);
		addOp(Percentile.class);
		addOp(Skewness.class);
		addOp(StdDev.class);
		addOp(Sum.class);
		addOp(SumOfInverses.class);
		addOp(SumOfLogs.class);
		addOp(SumOfSquares.class);
		addOp(Variance.class);
	}
}
