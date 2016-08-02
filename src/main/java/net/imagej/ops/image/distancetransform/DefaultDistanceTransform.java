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

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * @author Simon Schmid (University of Konstanz)
 */
@Plugin(type = Ops.Image.DistanceTransform.class, priority = Priority.LAST_PRIORITY)
public class DefaultDistanceTransform<B extends BooleanType<B>, T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<B>, RandomAccessibleInterval<T>>
		implements Ops.Image.DistanceTransform, Contingent {

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
		return createOp.compute1(new FinalInterval(in));
	}

	/*
	 * all values are stored in a 1D array this function computes the index in
	 * the array for a multidimensional position
	 */
	private int getIndex(final int[] positions, final int[] dimensSizes) {
		int index = 0;
		for (int i = 0; i < positions.length; i++) {
			int temp = 1;
			for (int j = 0; j < i; j++)
				temp *= dimensSizes[j];
			index += temp * positions[i];
		}
		return index;
	}

	/*
	 * extended version of meijsters raster scan alogrithm to compute n-d inputs
	 * Source: http://fab.cba.mit.edu/classes/S62.12/docs/Meijster_distance.pdf
	 */
	@Override
	public void compute1(final RandomAccessibleInterval<B> in, final RandomAccessibleInterval<T> out) {
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
		final int[] actualValues = new int[numPoints];

		// stores each Thread to execute
		final List<Callable<Void>> list = new ArrayList<>();

		/*
		 * initial phase calculates the first dimension
		 */
		int index = dimensSizes.length - 1;
		list.add(new InitPhase<>(actualValues, in, dimensSizes, positions.clone()));
		while (index > 0) {
			if (positions[index] < dimensSizes[index] - 1) {
				positions[index]++;
				index = positions.length - 1;
				list.add(new InitPhase<>(actualValues, in, dimensSizes, positions.clone()));
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
			list.add(new NextPhase<>(actualValues, dimensSizes, positions.clone(), actualDimension));
			while (index >= 0) {
				if (positions[index] == -1) {
					index--;
				} else {
					if (positions[index] < dimensSizes[index] - 1) {
						positions[index]++;
						index = positions.length - 1;
						list.add(new NextPhase<>(actualValues, dimensSizes, positions.clone(), actualDimension));
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
		raOut.get().setReal(Math.sqrt(actualValues[getIndex(positions, dimensSizes)]));
		index = dimensSizes.length - 1;
		while (index >= 0) {
			if (positions[index] < dimensSizes[index] - 1) {
				positions[index]++;
				index = positions.length - 1;
				raOut.setPosition(positions);
				raOut.get().setReal(Math.sqrt(actualValues[getIndex(positions, dimensSizes)]));
			} else {
				positions[index] = 0;
				index--;
			}
		}
	}
}

class InitPhase<B extends BooleanType<B>, T extends RealType<T>> implements Callable<Void> {
	private final int[] actualValues;
	private final RandomAccess<B> raIn;
	private final int infinite;
	private final int[] dimensSizes;
	private final int[] positions;

	public InitPhase(final int[] actualValues, final RandomAccessibleInterval<B> raIn, final int[] dimensSizes,
			final int[] positions) {
		this.actualValues = actualValues;
		this.raIn = raIn.randomAccess();
		int inf = 0;
		for (int i = 0; i < dimensSizes.length; i++)
			inf += dimensSizes[i];
		this.infinite = inf;
		this.dimensSizes = dimensSizes;
		this.positions = positions;
	}

	/*
	 * all values are stored in a 1D array this function computes the index in
	 * the array for a multidimensional position
	 */
	private int getIndex(final int[] pos, final int[] dim) {
		int index = 0;
		for (int i = 0; i < pos.length; i++) {
			int temp = 1;
			for (int j = 0; j < i; j++)
				temp *= dim[j];
			index += temp * pos[i];
		}
		return index;
	}

	@Override
	public Void call() throws Exception {
		// scan1
		positions[0] = 0;
		raIn.setPosition(positions);
		if (!raIn.get().get()) {
			actualValues[getIndex(positions, dimensSizes)] = 0;
		} else {
			actualValues[getIndex(positions, dimensSizes)] = infinite;
		}
		for (int x = 1; x < dimensSizes[0]; x++) {
			positions[0] = x;
			raIn.setPosition(positions);
			if (!raIn.get().get()) {
				actualValues[getIndex(positions, dimensSizes)] = 0;
			} else {
				final int[] temp = positions.clone();
				temp[0] = x - 1;
				actualValues[getIndex(positions, dimensSizes)] = actualValues[getIndex(temp, dimensSizes)] + 1;
			}
		}
		// scan2
		for (int x = dimensSizes[0] - 2; x >= 0; x--) {
			positions[0] = x;
			final int[] temp = positions.clone();
			temp[0] = x + 1;
			if (actualValues[getIndex(temp, dimensSizes)] < actualValues[getIndex(positions, dimensSizes)]) {
				actualValues[getIndex(positions, dimensSizes)] = 1 + actualValues[getIndex(temp, dimensSizes)];
			}
		}
		return null;
	}

}

class NextPhase<T extends RealType<T>> implements Callable<Void> {
	private final int[] actualValues;
	private final int[] dimensSizes;
	private final int[] positions;
	private final int[] positions2;
	private final int actualDimension;

	public NextPhase(final int[] actualValues, final int[] dimensSizes, final int[] positions,
			final int actualDimension) {
		this.actualValues = actualValues;
		this.dimensSizes = dimensSizes;
		this.positions = positions;
		this.positions2 = positions.clone();
		this.actualDimension = actualDimension;
	}

	/*
	 * all values are stored in a 1D array this function computes the index in
	 * the array for a multidimensional position
	 */
	private int getIndex(final int[] pos, final int[] dim) {
		int index = 0;
		for (int i = 0; i < pos.length; i++) {
			int temp = 1;
			for (int j = 0; j < i; j++)
				temp *= dim[j];
			index += temp * pos[i];
		}
		return index;
	}

	// help function
	private int distancefunc(final int x, final int i, final int raOutValue) {
		return (x - i) * (x - i) + raOutValue;
	}

	// help function
	private int sep(final int i, final int u, final int w, final int v) {
		return (u * u - i * i + w - v) / (2 * (u - i));
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
			while ((q >= 0)
					&& (distancefunc(t[q], s[q], actualValues[getIndex(positions, dimensSizes)]) > distancefunc(t[q], u,
							actualValues[getIndex(positions2, dimensSizes)]))) {
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
				final double w = 1 + sep(s[q], u, actualValues[getIndex(positions2, dimensSizes)],
						actualValues[getIndex(positions, dimensSizes)]);
				if (w < dimensSizes[actualDimension]) {
					q++;
					s[q] = u;
					t[q] = (int) w;
				}
			}
		}

		// scan 4
		final int[] newValues = new int[dimensSizes[actualDimension]];
		for (int u = dimensSizes[actualDimension] - 1; u >= 0; u--) {
			positions[actualDimension] = s[q];
			newValues[u] = distancefunc(u, s[q], actualValues[getIndex(positions, dimensSizes)]);
			if (u == t[q]) {
				q--;
			}
		}
		for (int u = dimensSizes[actualDimension] - 1; u >= 0; u--) {
			positions[actualDimension] = u;
			actualValues[getIndex(positions, dimensSizes)] = newValues[u];
		}
		return null;
	}

}
