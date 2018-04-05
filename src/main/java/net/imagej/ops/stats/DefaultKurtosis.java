/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RTs;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@code stats.kurtosis} using {@code stats.stdDev}
 * and {@code stats.moment4AboutMean}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = Ops.Stats.Kurtosis.class, label = "Statistics: Kurtosis")
public class DefaultKurtosis<I extends RealType<I>, O extends RealType<O>>
	extends AbstractStatsOp<Iterable<I>, O> implements Ops.Stats.Kurtosis
{

	private UnaryFunctionOp<Iterable<I>, O> stdDevFunc;
	
	private UnaryFunctionOp<Iterable<I>, O> moment4AboutMeanFunc;

	@Override
	public void initialize() {
		stdDevFunc = RTs.function(ops(), Ops.Stats.StdDev.class, in());
		moment4AboutMeanFunc = RTs.function(ops(), Ops.Stats.Moment4AboutMean.class, in());
	}
	
	@Override
	public void compute(final Iterable<I> input, final O output) {
		output.setReal(Double.NaN);

		final double std = stdDevFunc.calculate(input).getRealDouble();
		final double moment4 =
				moment4AboutMeanFunc.calculate(input).getRealDouble();

		if (std != 0) {
			output.setReal((moment4) / (std * std * std * std));
		}
	}
}
