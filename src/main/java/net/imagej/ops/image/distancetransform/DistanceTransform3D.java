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
public class DistanceTransform3D<B extends BooleanType<B>, T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<B>, RandomAccessibleInterval<T>>
		implements Ops.Image.DistanceTransform, Contingent {

	@Parameter
	private ThreadService ts;

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<FinalInterval, RandomAccessibleInterval> createOp;

	private ExecutorService es;

	@Override
	public boolean conforms() {
		if (in().numDimensions() == 3) {
			final long max_dist = in().dimension(0) * in().dimension(0) + in().dimension(1) * in().dimension(1)
					+ in().dimension(2) * in().dimension(2);
			return ((in().numDimensions() == 3) && (max_dist <= Integer.MAX_VALUE));
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
		final int[][][] tempValues = new int[(int) in.dimension(0)][(int) out.dimension(1)][(int) out.dimension(2)];

		// first phase
		final List<Callable<Void>> list = new ArrayList<>();

		for (int z = 0; z < in.dimension(2); z++) {
			for (int y = 0; y < in.dimension(1); y++) {
				list.add(new Phase1Runnable3D<>(tempValues, in, y, z));
			}
		}

		try {
			es.invokeAll(list);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		list.clear();

		// second phase
		final int[][][] tempValues_new = new int[(int) in.dimension(0)][(int) out.dimension(1)][(int) out.dimension(2)];
		for (int z = 0; z < in.dimension(2); z++) {
			for (int x = 0; x < in.dimension(0); x++) {
				list.add(new Phase2Runnable3D<>(tempValues, tempValues_new, out, x, z));
			}
		}

		try {
			es.invokeAll(list);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		// third phase
		for (int x = 0; x < in.dimension(0); x++) {
			for (int y = 0; y < in.dimension(1); y++) {

				list.add(new Phase3Runnable3D<>(tempValues_new, out, x, y));
			}
		}

		try {
			es.invokeAll(list);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Phase1Runnable3D<B extends BooleanType<B>> implements Callable<Void> {

	private final int[][][] tempValues;
	private final RandomAccess<B> raIn;
	private final int y;
	private final int z;
	private final int infinite;
	private final int width;

	public Phase1Runnable3D(final int[][][] tempValues, final RandomAccessibleInterval<B> raIn, final int yPos,
			final int zPos) {
		this.tempValues = tempValues;
		this.raIn = raIn.randomAccess();
		this.y = yPos;
		this.z = zPos;
		this.infinite = (int) (raIn.dimension(0) + raIn.dimension(1));
		this.width = (int) raIn.dimension(0);
	}

	@Override
	public Void call() throws Exception {
		// scan1
		raIn.setPosition(0, 0);
		raIn.setPosition(y, 1);
		raIn.setPosition(z, 2);
		if (!raIn.get().get()) {
			tempValues[0][y][z] = 0;
		} else {
			tempValues[0][y][z] = infinite;
		}
		for (int x = 1; x < width; x++) {
			raIn.setPosition(x, 0);
			if (!raIn.get().get()) {
				tempValues[x][y][z] = 0;
			} else {
				tempValues[x][y][z] = tempValues[x - 1][y][z] + 1;
			}
		}
		// scan2
		for (int x = width - 2; x >= 0; x--) {
			if (tempValues[x + 1][y][z] < tempValues[x][y][z]) {
				tempValues[x][y][z] = 1 + tempValues[x + 1][y][z];
			}
		}
		return null;
	}

}

class Phase2Runnable3D<T extends RealType<T>> implements Callable<Void> {

	private final int[][][] tempValues;
	private final int[][][] tempValues_new;
	private final int xPos;
	private final int zPos;
	private final int height;

	public Phase2Runnable3D(final int[][][] tempValues, final int[][][] tempValues_new,
			final RandomAccessibleInterval<T> raOut, final int xPos, final int zPos) {
		this.tempValues = tempValues;
		this.tempValues_new = tempValues_new;
		this.xPos = xPos;
		this.zPos = zPos;
		this.height = (int) raOut.dimension(1);
	}

	// help function used from the algorithm to compute distances
	private int distancefunc(final int x, final int i, final int raOutValue) {
		return (x - i) * (x - i) + raOutValue * raOutValue;
	}

	// help function used from the algorithm
	private double sep(final double i, final double u, final double w, final double v) {
		return (u * u - i * i + w * w - v * v) / (2 * (u - i));
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
			while ((q >= 0) && (distancefunc(t[q], s[q], tempValues[xPos][s[q]][zPos]) > distancefunc(t[q], u,
					tempValues[xPos][u][zPos]))) {
				q--;
			}
			if (q < 0) {
				q = 0;
				s[0] = u;
			} else {
				final double w = 1 + sep(s[q], u, tempValues[xPos][u][zPos], tempValues[xPos][s[q]][zPos]);
				if (w < height) {
					q++;
					s[q] = u;
					t[q] = (int) w;
				}
			}
		}

		// scan 4
		for (int u = height - 1; u >= 0; u--) {
			tempValues_new[xPos][u][zPos] = (distancefunc(u, s[q], tempValues[xPos][s[q]][zPos]));
			if (u == t[q]) {
				q--;
			}
		}
		return null;
	}

}

class Phase3Runnable3D<T extends RealType<T>> implements Callable<Void> {

	private final RandomAccessibleInterval<T> raOut;
	private final int[][][] tempValues;
	private final int xPos;
	private final int yPos;
	private final int deep;

	public Phase3Runnable3D(final int[][][] tempValues, final RandomAccessibleInterval<T> raOut, final int xPos,
			final int yPos) {
		this.tempValues = tempValues;
		this.raOut = raOut;
		this.xPos = xPos;
		this.yPos = yPos;
		this.deep = (int) raOut.dimension(2);
	}

	// help function used from the algorithm to compute distances
	private int distancefunc(final int x, final int i, final int raOutValue) {
		return (x - i) * (x - i) + raOutValue;
	}

	// help function used from the algorithm
	private int sep(final int i, final int u, final int w, final int v) {
		return (u * u - i * i + w - v) / (2 * (u - i));
	}

	@Override
	public Void call() throws Exception {
		final int[] s = new int[deep];
		final int[] t = new int[deep];
		int q = 0;
		s[0] = 0;
		t[0] = 0;

		// scan 3
		for (int u = 1; u < deep; u++) {
			while ((q >= 0) && (distancefunc(t[q], s[q], tempValues[xPos][yPos][s[q]]) > distancefunc(t[q], u,
					tempValues[xPos][yPos][u]))) {
				q--;
			}
			if (q < 0) {
				q = 0;
				s[0] = u;
			} else {
				final int w = 1 + sep(s[q], u, tempValues[xPos][yPos][u], tempValues[xPos][yPos][s[q]]);
				if (w < deep) {
					q++;
					s[q] = u;
					t[q] = w;
				}
			}
		}

		// scan 4
		final RandomAccess<T> ra = raOut.randomAccess();
		for (int u = deep - 1; u >= 0; u--) {
			ra.setPosition(xPos, 0);
			ra.setPosition(yPos, 1);
			ra.setPosition(u, 2);
			ra.get().setReal(Math.sqrt(distancefunc(u, s[q], tempValues[xPos][yPos][s[q]])));
			if (u == t[q]) {
				q--;
			}
		}
		return null;
	}

}
