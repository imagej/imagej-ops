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

package net.imagej.ops.stats;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Stats.Kurtosis;
import net.imagej.ops.Ops.Stats.Moment4AboutMean;
import net.imagej.ops.Ops.Stats.StdDev;
import net.imagej.ops.RTs;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@link Kurtosis} using {@link StdDev} and
 * {@link Moment4AboutMean}
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @author Christian Dietz, University of Konstanz.
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = StatsOp.class, name = Kurtosis.NAME,
	label = "Statistics: Kurtosis")
public class DefaultKurtosis<I extends RealType<I>, O extends RealType<O>>
	extends AbstractStatsOp<Iterable<I>, O> implements Kurtosis
{

	private FunctionOp<Iterable<I>, O> stdDevFunc;
	
	private FunctionOp<Iterable<I>, O> moment4AboutMeanFunc;

	@Override
	public void initialize() {
		stdDevFunc = RTs.function(ops(), StdDev.class, in());
		moment4AboutMeanFunc = RTs.function(ops(), Moment4AboutMean.class, in());
	}
	
	@Override
	public void compute(final Iterable<I> input, final O output) {
		output.setReal(Double.NaN);

		final double std = stdDevFunc.compute(input).getRealDouble();
		final double moment4 =
				moment4AboutMeanFunc.compute(input).getRealDouble();

		if (std != 0) {
			output.setReal((moment4) / (std * std * std * std));
		}
	}
}
