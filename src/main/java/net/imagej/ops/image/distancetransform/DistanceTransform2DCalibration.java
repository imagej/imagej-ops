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

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Computes a distance transform, i.e. for every foreground pixel its distance
 * to the nearest background pixel.
 * 
 * @author Simon Schmid (University of Konstanz)
 */
@Plugin(type = Ops.Image.DistanceTransform.class)
public class DistanceTransform2DCalibration<B extends BooleanType<B>, T extends RealType<T>>
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
		if (in().numDimensions() == 2) {
			final long max_dist = in().dimension(0) * in().dimension(0) + in().dimension(1) * in().dimension(1);
			return ((in().numDimensions() == 2) && (max_dist <= Integer.MAX_VALUE));
		}
		return false;
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
	 * meijsters raster scan alogrithm Source:
	 * http://fab.cba.mit.edu/classes/S62.12/docs/Meijster_distance.pdf
	 */
	@Override
	public void compute(final RandomAccessibleInterval<B> in, final RandomAccessibleInterval<T> out) {

		// tempValues stores the integer values of the first phase, i.e. the
		// first two scans
		final double[][] tempValues = new double[(int) in.dimension(0)][(int) out.dimension(1)];

		// first phase
		final List<Callable<Void>> list = new ArrayList<>();

		for (int y = 0; y < in.dimension(1); y++) {
			list.add(new Phase1Runnable2DCal<>(tempValues, in, y, calibration));
		}

		try {
			es.invokeAll(list);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		list.clear();

		// second phase
		for (int x = 0; x < in.dimension(0); x++) {
			list.add(new Phase2Runnable2DCal<>(tempValues, out, x, calibration));
		}

		try {
			es.invokeAll(list);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}

class Phase1Runnable2DCal<B extends BooleanType<B>> implements Callable<Void> {

	private final double[][] tempValues;
	private final RandomAccess<B> raIn;
	private final int y;
	private final double infinite;
	private final int width;
	private final double[] calibration;

	public Phase1Runnable2DCal(final double[][] tempValues, final RandomAccessibleInterval<B> raIn, final int yPos,
			final double[] calibration) {
		this.tempValues = tempValues;
		this.raIn = raIn.randomAccess();
		this.y = yPos;
		this.infinite = calibration[0] * raIn.dimension(0) + calibration[1] * raIn.dimension(1);
		this.width = (int) raIn.dimension(0);
		this.calibration = calibration;
	}

	@Override
	public Void call() throws Exception {
		// scan1
		raIn.setPosition(0, 0);
		raIn.setPosition(y, 1);
		if (!raIn.get().get()) {
			tempValues[0][y] = 0;
		} else {
			tempValues[0][y] = infinite;
		}
		for (int x = 1; x < width; x++) {
			raIn.setPosition(x, 0);
			if (!raIn.get().get()) {
				tempValues[x][y] = 0;
			} else {
				tempValues[x][y] = tempValues[x - 1][y] + calibration[0];
			}
		}
		// scan2
		for (int x = width - 2; x >= 0; x--) {
			if (tempValues[x + 1][y] < tempValues[x][y]) {
				tempValues[x][y] = calibration[0] + tempValues[x + 1][y];
			}

		}
		return null;
	}

}

class Phase2Runnable2DCal<T extends RealType<T>> implements Callable<Void> {

	private final RandomAccessibleInterval<T> raOut;
	private final double[][] tempValues;
	private final int xPos;
	private final int height;
	private final double[] calibration;

	public Phase2Runnable2DCal(final double[][] tempValues, final RandomAccessibleInterval<T> raOut, final int xPos,
			final double[] calibration) {
		this.tempValues = tempValues;
		this.raOut = raOut;
		this.xPos = xPos;
		this.height = (int) raOut.dimension(1);
		this.calibration = calibration;
	}

	// help function used from the algorithm to compute distances
	private double distancefunc(final int x, final int i, final double raOutValue) {
		return calibration[1] * calibration[1] * (x - i) * (x - i) + raOutValue * raOutValue;
	}

	// help function used from the algorithm
	private int sep(final int i, final int u, final double w, final double v) {
		return (int) Math.floor(Math.nextUp((u * u - i * i + ((w * w) / (calibration[1] * calibration[1]))
				- ((v * v) / (calibration[1] * calibration[1]))) / (2 * (u - i))));
	}

	@Override
	public Void call() throws Exception {
		final int[] s = new int[height];
		final int[] t = new int[height];
		int q = 0;
		s[0] = 0;
		t[0] = 0;

		// scan 3
		for (int u = 1; u < height; u++) {
			while ((q >= 0) && (distancefunc(t[q], s[q], tempValues[xPos][s[q]]) > distancefunc(t[q], u,
					tempValues[xPos][u]))) {
				q--;
			}
			if (q < 0) {
				q = 0;
				s[0] = u;
			} else {
				final int w = 1 + sep(s[q], u, tempValues[xPos][u], tempValues[xPos][s[q]]);
				if (w < height) {
					q++;
					s[q] = u;
					t[q] = w;
				}
			}
		}

		// scan 4
		final RandomAccess<T> ra = raOut.randomAccess();
		for (int u = height - 1; u >= 0; u--) {
			ra.setPosition(u, 1);
			ra.setPosition(xPos, 0);
			ra.get().setReal(Math.sqrt(distancefunc(u, s[q], tempValues[xPos][s[q]])));
			if (u == t[q]) {
				q--;
			}
		}
		return null;
	}

}
