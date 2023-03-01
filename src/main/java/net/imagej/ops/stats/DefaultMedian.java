/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2023 ImageJ2 developers.
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

import java.util.ArrayList;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@code stats.median}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @author Jan Eglinger
 * @author Richard Domander
 * @param <I>
 *            input type
 * @param <O>
 *            output type
 */
@Plugin(type = Ops.Stats.Median.class, label = "Statistics: Median")
public class DefaultMedian<I extends RealType<I>, O extends RealType<O>> extends AbstractStatsOp<Iterable<I>, O>
		implements Ops.Stats.Median {

	@Override
	public void compute(final Iterable<I> input, final O output) {
		final ArrayList<Double> statistics = new ArrayList<>();

		input.forEach(i -> statistics.add(i.getRealDouble()));

		final int k = statistics.size() / 2;
		double result = DefaultQuantile.select(statistics, k);
		if (statistics.size() % 2 == 0) {
			result += DefaultQuantile.select(statistics, k - 1);
			result *= 0.5;
		}

		output.setReal(result);
	}
}
