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

package net.imagej.ops.filter.bilateral;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhoodFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Performs a bilateral filter on an image.
 *
 * @author Gabe Selzer
 * @param <I>
 * @param <O>
 */

@Plugin(type = Ops.Filter.Bilateral.class, priority = Priority.NORMAL)
public class DefaultBilateral<I extends RealType<I>, O extends RealType<O>>
		extends AbstractUnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
		implements Ops.Filter.Bilateral, Contingent {

	public final static int MIN_DIMS = 2;

	public final static int MAX_DIMS = 2;

	/**
	 * refers to the range smoothing parameter; the greater the sigma, the greater
	 * the effect of intensity differences.
	 */
	@Parameter
	private double sigmaR;

	/**
	 * refers to the spatial smoothing parameter; the greater the sigma, the
	 * smoother the image.
	 */
	@Parameter
	private double sigmaS;

	/**
	 * refers to the square that is considered when doing the filter on each
	 * individual picture.
	 */
	@Parameter
	private int radius;

	private static double gauss(final double x, final double sigma) {
		final double mu = 0.0;
		return (1 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp((-0.5 * (x - mu) * (x - mu)) / (sigma * sigma));
	}

	private double getDistance(long[] x, long[] y) {
		double distance = 0;

		for (int i = 0; i < x.length; i++) {
			double separation = (x[i] - y[i]);
			if (separation != 0) {
				distance += (separation * separation);
			}

		}

		return Math.sqrt(distance);
	}

	@Override
	public void compute(final RandomAccessibleInterval<I> input, final RandomAccessibleInterval<O> output) {

		final long[] size = new long[input.numDimensions()];
		input.dimensions(size);

		final RandomAccess<O> outputRA = output.randomAccess();
		final Cursor<I> inputCursor = Views.iterable(input).localizingCursor();
		final long[] currentPos = new long[input.numDimensions()];
		final long[] neighborhoodPos = new long[input.numDimensions()];
		final long[] neighborhoodMin = new long[input.numDimensions()];
		final long[] neighborhoodMax = new long[input.numDimensions()];
		Neighborhood<I> neighborhood;
		Cursor<I> neighborhoodCursor;
		final RectangleNeighborhoodFactory<I> fac = RectangleNeighborhood.factory();
		while (inputCursor.hasNext()) {
			inputCursor.fwd();
			inputCursor.localize(currentPos);
			double distance;
			inputCursor.localize(neighborhoodMin);
			inputCursor.localize(neighborhoodMax);
			neighborhoodMin[0] = Math.max(0, neighborhoodMin[0] - radius);
			neighborhoodMin[1] = Math.max(0, neighborhoodMin[1] - radius);
			neighborhoodMax[0] = Math.min(input.max(0), neighborhoodMax[0] + radius);
			neighborhoodMax[1] = Math.min(input.max(1), neighborhoodMax[1] + radius);
			final Interval interval = new FinalInterval(neighborhoodMin, neighborhoodMax);
			neighborhood = fac.create(currentPos, neighborhoodMin, neighborhoodMax, interval, input.randomAccess());
			neighborhoodCursor = neighborhood.localizingCursor();
			double weight, v = 0.0;
			double w = 0.0;
			do {
				neighborhoodCursor.fwd();
				neighborhoodCursor.localize(neighborhoodPos);
				distance = getDistance(currentPos, neighborhoodPos);
				weight = gauss(distance, sigmaS);// spatial kernel

				distance = Math.abs(inputCursor.get().getRealDouble() - neighborhoodCursor.get().getRealDouble());// intensity
																													// difference
				weight *= gauss(distance, sigmaR);// range kernel, then exponent addition

				v += weight * neighborhoodCursor.get().getRealDouble();
				w += weight;
			} while (neighborhoodCursor.hasNext());
			outputRA.setPosition(currentPos);
			outputRA.get().setReal(v / w);
		}

	}

	@Override
	public boolean conforms() {
		return (Intervals.equalDimensions(in(), out()));
	}
}
