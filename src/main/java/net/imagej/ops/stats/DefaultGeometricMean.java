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
 * {@link Op} to calculate the {@code stats.geometricMean} using
 * {@code stats.size} and {@code stats.sumOfLogs}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = Ops.Stats.GeometricMean.class, label = "Statistics: GeometricMean")
public class DefaultGeometricMean<I extends RealType<I>, O extends RealType<O>>
	extends AbstractStatsOp<Iterable<I>, O> implements Ops.Stats.GeometricMean
{
	
	private UnaryFunctionOp<Iterable<I>, O> sizeFunc;

	private UnaryFunctionOp<Iterable<I>, O> sumOfLogsFunc;

	@Override
	public void initialize() {
		sumOfLogsFunc = RTs.function(ops(), Ops.Stats.SumOfLogs.class, in());
		sizeFunc = RTs.function(ops(), Ops.Stats.Size.class, in());
	}
	
	@Override
	public void compute(final Iterable<I> input, final O output) {
		final double size = sizeFunc.calculate(input).getRealDouble();
		final double sumOfLogs = sumOfLogsFunc.calculate(input).getRealDouble();

		if (size != 0) {
			output.setReal(Math.exp(sumOfLogs / size));
		}
		else {
			output.setReal(0);
		}

	}
}
