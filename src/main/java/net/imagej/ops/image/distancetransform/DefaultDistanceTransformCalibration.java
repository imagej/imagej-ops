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
package net.imagej.ops.image.distancetransform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.img.CreateImgFromDimsAndType;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.IntervalIndexer;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Computes a distance transform, i.e. for every foreground pixel its distance
 * to the nearest background pixel.
 * 
 * @author Simon Schmid (University of Konstanz)
 */
@Plugin(type = Ops.Image.DistanceTransform.class, priority = Priority.LAST)
public class DefaultDistanceTransformCalibration<B extends BooleanType<B>, T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<B>, RandomAccessibleInterval<T>>
		implements Ops.Image.DistanceTransform, Contingent {

	@Parameter(required = true)
	private double[] calibration;

	@Parameter
	private ThreadService ts;

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<FinalInterval, RandomAccessibleInterval> createOp;

	private ExecutorService es;

	@Override
	public boolean conforms() {
		long max_dist = 0;
		for (int i = 0; i < in().numDimensions(); i++)
			max_dist += in().dimension(i) * in().dimension(i);
		return max_dist <= Integer.MAX_VALUE;
	}

	@Override
	public void initialize() {
		es = ts.getExecutorService();
		createOp = Functions.unary(ops(), CreateImgFromDimsAndType.class, RandomAccessibleInterval.class,
				new FinalInterval(in()), new FloatType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public RandomAccessibleInterval<T> createOutput(final RandomAccessibleInterval<B> in) {
		return createOp.calculate(new FinalInterval(in));
	}

	/*
	 * extended version of meijsters raster scan alogrithm to compute n-d inputs
	 * Source: http://fab.cba.mit.edu/classes/S62.12/docs/Meijster_distance.pdf
	 */
	@Override
	public void compute(final RandomAccessibleInterval<B> in, final RandomAccessibleInterval<T> out) {
		// stores the size of each dimension
		final int[] dimensSizes = new int[in.numDimensions()];

		// stores the actual position in the image
		final int[] positions = new int[in.numDimensions()];

		// calculates the number of points in the n-d space
		int numPoints = 1;
		for (int i = 0; i < in.numDimensions(); i++) {
			numPoints *= in.dimension(i);
			dimensSizes[i] = (int) in.dimension(i);
			positions[i] = 0;
		}
		// stores the values calculated after each phase
		final double[] actualValues = new double[numPoints];

		// stores each Thread to execute
		final List<Callable<Void>> list = new ArrayList<>();

		/*
		 * initial phase calculates the first dimension
		 */
		int index = dimensSizes.length - 1;
		list.add(new InitPhaseCal<>(actualValues, in, dimensSizes, positions.clone(), calibration));
		while (index > 0) {
			if (positions[index] < dimensSizes[index] - 1) {
				positions[index]++;
				index = positions.length - 1;
				list.add(new InitPhaseCal<>(actualValues, in, dimensSizes, positions.clone(), calibration));
			} else {
				positions[index] = 0;
				index--;
			}
		}

		try {
			es.invokeAll(list);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		list.clear();

		// squared values needed for further calculations
		for (int i = 0; i < actualValues.length; i++) {
			actualValues[i] *= actualValues[i];
		}

		/*
		 * next phases calculates remaining dimensions
		 */
		for (int actualDimension = 1; actualDimension < in.numDimensions(); actualDimension++) {
			Arrays.fill(positions, 0);
			positions[actualDimension] = -1;
			index = positions.length - 1;
			list.add(new NextPhaseCal<>(actualValues, dimensSizes, positions.clone(), actualDimension, calibration));
			while (index >= 0) {
				if (positions[index] == -1) {
					index--;
				} else {
					if (positions[index] < dimensSizes[index] - 1) {
						positions[index]++;
						index = positions.length - 1;
						list.add(new NextPhaseCal<>(actualValues, dimensSizes, positions.clone(), actualDimension,
								calibration));
					} else {
						positions[index] = 0;
						index--;
					}
				}
			}

			try {
				es.invokeAll(list);
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}

			list.clear();
		}

		/*
		 * create output
		 */
		Arrays.fill(positions, 0);
		final RandomAccess<T> raOut = out.randomAccess();
		raOut.setPosition(positions);
		raOut.get().setReal(Math.sqrt(actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)]));
		index = dimensSizes.length - 1;
		while (index >= 0) {
			if (positions[index] < dimensSizes[index] - 1) {
				positions[index]++;
				index = positions.length - 1;
				raOut.setPosition(positions);
				raOut.get().setReal(Math.sqrt(actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)]));
			} else {
				positions[index] = 0;
				index--;
			}
		}
	}
}

class InitPhaseCal<B extends BooleanType<B>, T extends RealType<T>> implements Callable<Void> {
	private final double[] actualValues;
	private final RandomAccess<B> raIn;
	private final int infinite;
	private final int[] dimensSizes;
	private final int[] positions;
	private final double[] calibration;

	public InitPhaseCal(final double[] actualValues, final RandomAccessibleInterval<B> raIn, final int[] dimensSizes,
			final int[] positions, final double[] calibration) {
		this.actualValues = actualValues;
		this.raIn = raIn.randomAccess();
		int inf = 0;
		for (int i = 0; i < dimensSizes.length; i++)
			inf += calibration[i] * calibration[i] * dimensSizes[i] * dimensSizes[i];
		this.infinite = inf;
		this.dimensSizes = dimensSizes;
		this.positions = positions;
		this.calibration = calibration;
	}

	@Override
	public Void call() throws Exception {
		// scan1
		positions[0] = 0;
		raIn.setPosition(positions);
		if (!raIn.get().get()) {
			actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)] = 0;
		} else {
			actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)] = infinite;
		}
		for (int x = 1; x < dimensSizes[0]; x++) {
			positions[0] = x;
			raIn.setPosition(positions);
			if (!raIn.get().get()) {
				actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)] = 0;
			} else {
				final int[] temp = positions.clone();
				temp[0] = x - 1;
				actualValues[IntervalIndexer.positionToIndex(positions,
						dimensSizes)] = actualValues[IntervalIndexer.positionToIndex(temp, dimensSizes)]
								+ calibration[0];
			}
		}
		// scan2
		for (int x = dimensSizes[0] - 2; x >= 0; x--) {
			positions[0] = x;
			final int[] temp = positions.clone();
			temp[0] = x + 1;
			if (actualValues[IntervalIndexer.positionToIndex(temp, dimensSizes)] < actualValues[IntervalIndexer
					.positionToIndex(positions, dimensSizes)]) {
				actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)] = calibration[0]
						+ actualValues[IntervalIndexer.positionToIndex(temp, dimensSizes)];
			}
		}
		return null;
	}

}

class NextPhaseCal<T extends RealType<T>> implements Callable<Void> {
	private final double[] actualValues;
	private final int[] dimensSizes;
	private final int[] positions;
	private final int[] positions2;
	private final int actualDimension;
	private final double[] calibration;

	public NextPhaseCal(final double[] actualValues, final int[] dimensSizes, final int[] positions,
			final int actualDimension, final double[] calibration) {
		this.actualValues = actualValues;
		this.dimensSizes = dimensSizes;
		this.positions = positions;
		this.positions2 = positions.clone();
		this.actualDimension = actualDimension;
		this.calibration = calibration;
	}

	// help function
	private double distancefunc(final int x, final int i, final double raOutValue) {
		return calibration[actualDimension] * calibration[actualDimension] * (x - i) * (x - i) + raOutValue;
	}

	// help function
	private int sep(final int i, final int u, final double w, final double v) {
		return (int) Math
				.floor(Math.nextUp((u * u - i * i + (w / (calibration[actualDimension] * calibration[actualDimension]))
						- (v / (calibration[actualDimension] * calibration[actualDimension]))) / (2 * (u - i))));
	}

	@Override
	public Void call() throws Exception {
		final int[] s = new int[dimensSizes[actualDimension]];
		final int[] t = new int[dimensSizes[actualDimension]];
		int q = 0;
		s[0] = 0;
		t[0] = 0;

		// scan 3
		for (int u = 1; u < dimensSizes[actualDimension]; u++) {
			positions[actualDimension] = s[q];
			positions2[actualDimension] = u;
			while ((q >= 0) && (distancefunc(t[q], s[q],
					actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)]) > distancefunc(t[q], u,
							actualValues[IntervalIndexer.positionToIndex(positions2, dimensSizes)]))) {
				q--;
				if (q >= 0)
					positions[actualDimension] = s[q];
			}
			if (q < 0) {
				q = 0;
				s[0] = u;
			} else {
				positions[actualDimension] = s[q];
				positions2[actualDimension] = u;
				final int w = 1 + sep(s[q], u, actualValues[IntervalIndexer.positionToIndex(positions2, dimensSizes)],
						actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)]);
				if (w < dimensSizes[actualDimension]) {
					q++;
					s[q] = u;
					t[q] = w;
				}
			}
		}

		// scan 4
		final double[] newValues = new double[dimensSizes[actualDimension]];
		for (int u = dimensSizes[actualDimension] - 1; u >= 0; u--) {
			positions[actualDimension] = s[q];
			newValues[u] = distancefunc(u, s[q], actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)]);
			if (u == t[q]) {
				q--;
			}
		}
		for (int u = dimensSizes[actualDimension] - 1; u >= 0; u--) {
			positions[actualDimension] = u;
			actualValues[IntervalIndexer.positionToIndex(positions, dimensSizes)] = newValues[u];
		}
		return null;
	}
}
