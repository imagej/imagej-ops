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
package net.imagej.ops.filter.sigma;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Stats.Variance;
import net.imagej.ops.filter.AbstractCenterAwareNeighborhoodBasedFilter;
import net.imagej.ops.map.neighborhood.AbstractCenterAwareComputerOp;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link SigmaFilterOp}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <T>
 *            type
 */
@Plugin(type = Ops.Filter.Sigma.class, name = Ops.Filter.Sigma.NAME, priority = Priority.LOW_PRIORITY)
public class DefaultSigmaFilter<T extends RealType<T>> extends
		AbstractCenterAwareNeighborhoodBasedFilter<T, T> implements
		SigmaFilterOp<RandomAccessibleInterval<T>>, Contingent {

	@Parameter
	private Double range;

	@Parameter
	private Double minPixelFraction;

	@Override
	protected CenterAwareComputerOp<T, T> getComputer(Class<?> inClass,
			Class<?> outClass) {
		final AbstractCenterAwareComputerOp<T, T> op =
			new AbstractCenterAwareComputerOp<T, T>() {

			private ComputerOp<Iterable<T>, DoubleType> variance;

			@Override
			public void compute(Pair<T, Iterable<T>> input, T output) {
				if (variance == null) {
					variance = (ComputerOp<Iterable<T>, DoubleType>) ops().op(
							Variance.class, DoubleType.class, input.getB());
				}

				DoubleType varianceResult = new DoubleType();
				variance.compute(input.getB(), varianceResult);
				double varianceValue = varianceResult.getRealDouble() * range;

				final double centerValue = input.getA().getRealDouble();
				double sumAll = 0;
				double sumWithin = 0;
				long countAll = 0;
				long countWithin = 0;

				for (T neighbor : input.getB()) {
					final double pixelValue = neighbor.getRealDouble();
					final double diff = centerValue - pixelValue;

					sumAll += pixelValue;
					++countAll;

					if (diff > varianceValue || diff < -varianceValue) {
						continue;
					}

					// pixel within variance range
					sumWithin += pixelValue;
					++countWithin;
				}

				if (countWithin < (int) (minPixelFraction * countAll)) {
					output.setReal(sumAll / countAll); // simply mean
				} else {
					// mean over pixels in variance range only
					output.setReal(sumWithin / countWithin);
				}
			}

		};
		op.setEnvironment(ops());
		return op;
	}

	@Override
	public boolean conforms() {
		return range > 0.0;
	}
}
