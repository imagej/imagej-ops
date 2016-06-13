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
package net.imagej.ops.features.hog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.img.CreateImgFromDimsAndType;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gradient.PartialDerivative;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.RectangleShape.NeighborhoodsAccessible;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Calculates a histogram of oriented gradients which is a feature descriptor.
 * The technique first calculates the partial derivatives and then for each
 * pixel a histogram of gradient directions by summing up the magnitudes of the
 * neighbored (@param spanOfNeighborhood) pixels. The directions are divided in
 * (@param numOrientations) bins. The output is 3d: for each bin an own channel.
 * 
 * The algorithm is based on the paper "Histograms of Oriented Gradients for
 * Human Detection" by Navneet Dalal and Bill Triggs, published 2005.
 * 
 * @author Simon Schmid (University of Konstanz)
 */
@Plugin(type = Ops.HoG.HistogramOfOrientedGradients.class)
public class HistogramOfOrientedGradients2D<T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		implements Ops.HoG.HistogramOfOrientedGradients, Contingent {

	@Parameter(required = true)
	private int numOrientations;

	@Parameter(required = true)
	private int spanOfNeighborhood;

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<FinalInterval, RandomAccessibleInterval> createOp;

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<RandomAccessibleInterval, RandomAccessibleInterval> createImgOp;

	private Converter<T, FloatType> converter;

	private ExecutorService es;

	@Parameter
	private ThreadService ts;

	@Override
	public void initialize() {
		es = ts.getExecutorService();

		createOp = Functions.unary(ops(), CreateImgFromDimsAndType.class, RandomAccessibleInterval.class,
				new FinalInterval(in().dimension(0), in().dimension(1), numOrientations), new FloatType());

		createImgOp = Functions.unary(ops(), CreateImgFromDimsAndType.class, RandomAccessibleInterval.class, in(),
				new FloatType());

		converter = new Converter<T, FloatType>() {
			@Override
			public void convert(final T arg0, final FloatType arg1) {
				arg1.setReal(arg0.getRealFloat());
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public RandomAccessibleInterval<T> createOutput(RandomAccessibleInterval<T> in) {
		return createOp.compute1(new FinalInterval(in().dimension(0), in().dimension(1), numOrientations));
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute1(RandomAccessibleInterval<T> in, RandomAccessibleInterval<T> out) {
		RandomAccessible<FloatType> convertedIn = Converters.convert(Views.extendMirrorDouble(in), converter,
				new FloatType());

		// compute partial derivative for each dimension
		RandomAccessibleInterval<FloatType> derivative0 = createImgOp.compute0();
		RandomAccessibleInterval<FloatType> derivative1 = createImgOp.compute0();
		PartialDerivative.gradientCentralDifference(convertedIn, derivative0, 0);
		PartialDerivative.gradientCentralDifference(convertedIn, derivative1, 1);

		// compute angles and magnitudes
		RandomAccessibleInterval<FloatType> angles = createImgOp.compute0();
		RandomAccessibleInterval<FloatType> magnitudes = createImgOp.compute0();

		Cursor<FloatType> cursorAngles = Views.flatIterable(angles).cursor();
		Cursor<FloatType> cursorMagnitudes = Views.flatIterable(magnitudes).cursor();
		Cursor<FloatType> cursorDerivative0 = Views.flatIterable(derivative0).cursor();
		Cursor<FloatType> cursorDerivative1 = Views.flatIterable(derivative1).cursor();

		while (cursorAngles.hasNext()) {
			cursorAngles.fwd();
			cursorMagnitudes.fwd();
			cursorDerivative0.fwd();
			cursorDerivative1.fwd();
			final float x = cursorDerivative0.get().getRealFloat();
			final float y = cursorDerivative1.get().getRealFloat();
			cursorAngles.get().setReal(getAngle(x, y));
			cursorMagnitudes.get().setReal(getMagnitude(x, y));
		}

		// stores each Thread to execute
		final List<Callable<Void>> listCallables = new ArrayList<>();

		// compute descriptor (default 3x3, i.e. 9 channels: one channel for
		// each bin)
		final RectangleShape shape = new RectangleShape(spanOfNeighborhood, false);
		final NeighborhoodsAccessible<FloatType> neighborHood = shape.neighborhoodsRandomAccessible(convertedIn);

		for (int i = 0; i < in.dimension(0); i++) {
			listCallables.add(new ComputeDescriptor(Views.interval(convertedIn, in), i, angles.randomAccess(),
					magnitudes.randomAccess(), (RandomAccess<FloatType>) out.randomAccess(),
					neighborHood.randomAccess()));
		}

		try {
			es.invokeAll(listCallables);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		listCallables.clear();
	}

	private class ComputeDescriptor implements Callable<Void> {
		final private RandomAccessibleInterval<FloatType> in;
		final private long i;
		final private RandomAccess<FloatType> raAngles;
		final private RandomAccess<FloatType> raMagnitudes;
		final private RandomAccess<FloatType> raOut;
		final private RandomAccess<Neighborhood<FloatType>> raNeighbor;

		public ComputeDescriptor(final RandomAccessibleInterval<FloatType> in, final long i,
				final RandomAccess<FloatType> raAngles, final RandomAccess<FloatType> raMagnitudes,
				final RandomAccess<FloatType> raOut, final RandomAccess<Neighborhood<FloatType>> raNeighbor) {
			this.in = in;
			this.i = i;
			this.raAngles = raAngles;
			this.raMagnitudes = raMagnitudes;
			this.raOut = raOut;
			this.raNeighbor = raNeighbor;
		}

		@Override
		public Void call() throws Exception {

			for (int j = 0; j < in.dimension(1); j++) {
				// sum up the magnitudes of all bins in a neighborhood
				raNeighbor.setPosition(new long[] { i, j });
				final Cursor<FloatType> cursorNeighborHood = raNeighbor.get().cursor();
				final long[] posNeighbor = new long[cursorNeighborHood.numDimensions()];

				while (cursorNeighborHood.hasNext()) {
					cursorNeighborHood.next();
					cursorNeighborHood.localize(posNeighbor);
					if (Intervals.contains(in, new Point(posNeighbor))) {
						raAngles.setPosition(posNeighbor);
						raMagnitudes.setPosition(posNeighbor);
						raOut.setPosition(
								new long[] { i, j, (int) raAngles.get().getRealDouble() / (360 / numOrientations) });
						raOut.get().add(raMagnitudes.get());
					}
				}
			}
			return null;
		}
	}

	// returns the signed angle of a vector
	private double getAngle(final double x, final double y) {
		float angle = (float) Math.toDegrees(Math.atan2(x, y));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	// returns the magnitude of a vector
	private double getMagnitude(final double x, final double y) {
		return Math.sqrt(x * x + y * y);
	}
}
