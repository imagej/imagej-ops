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

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@code stats.variance} using the online algorithm
 * from Knuth and Welford.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @author Christian Dietz, University of Konstanz.
 * @param <I> input type
 * @param <O> output type
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm">
 *      Wikipedia</a>
 */
@Plugin(type = Ops.Stats.Variance.class, label = "Statistics: Variance",
	priority = Priority.VERY_HIGH_PRIORITY)
public class IterableVariance<I extends RealType<I>, O extends RealType<O>>
	extends AbstractStatsOp<Iterable<I>, O> implements Ops.Stats.Variance
{

	@Override
	public void compute(final Iterable<I> input, final O output) {
		int n = 0;
		double mean = 0.0;
		double M2 = 0.0;

		for (final I in : input) {
			double x = in.getRealDouble();

			n = n + 1;
			double delta = x - mean;
			mean = mean + delta / n;
			M2 = M2 + delta * (x - mean);
		}

		if (n < 2) {
			output.setReal(Double.NaN);
		}
		else {
			output.setReal(M2 / (n - 1));
		}
	}

}
