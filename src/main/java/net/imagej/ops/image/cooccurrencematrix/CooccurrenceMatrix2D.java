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
package net.imagej.ops.image.cooccurrencematrix;

import java.util.Arrays;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Stats.MinMax;
import net.imagej.ops.special.AbstractUnaryFunctionOp;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates coocccurrence matrix from an 2D-{@link IterableInterval}.
 * 
 * @author Stephan Sellien, University of Konstanz
 * @author Christian Dietz, University of Konstanz
 * @author Andreas Graumann, University of Konstanz
 */
@Plugin(type = Ops.Image.CooccurrenceMatrix.class)
public class CooccurrenceMatrix2D<T extends RealType<T>> extends
		AbstractUnaryFunctionOp<IterableInterval<T>, double[][]> implements
		Ops.Image.CooccurrenceMatrix, Contingent {

	@Parameter(label = "Number of Gray Levels", min = "0", max = "128", stepSize = "1", initializer = "32")
	private int nrGreyLevels;

	@Parameter(label = "Distance", min = "0", max = "128", stepSize = "1", initializer = "1")
	private int distance;

	@Parameter(label = "Matrix Orientation")
	private MatrixOrientation orientation;

	private UnaryFunctionOp<IterableInterval<T>, Pair<T, T>> minmax;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		super.initialize();
		minmax = (UnaryFunctionOp) Functions.unary(ops(), MinMax.class, Pair.class, in());
	}

	@Override
	public double[][] compute1(final IterableInterval<T> input) {

		final double[][] output = new double[nrGreyLevels][nrGreyLevels];

		final Cursor<? extends RealType<?>> cursor = input.localizingCursor();

		final Pair<T, T> minMax = minmax.compute1(input);

		final double localMin = minMax.getA().getRealDouble();
		final double localMax = minMax.getB().getRealDouble();

		final int[][] pixels = new int[(int) input.dimension(1)][(int) input
				.dimension(0)];

		for (int i = 0; i < pixels.length; i++) {
			Arrays.fill(pixels[i], Integer.MAX_VALUE);
		}

		final int minimumX = (int) input.min(0);
		final int minimumY = (int) input.min(1);
		final double diff = localMax - localMin;
		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(1) - minimumY][cursor.getIntPosition(0) -
				minimumX] = (int) (((cursor.get().getRealDouble() - localMin) / diff) *
					(nrGreyLevels - 1));
		}

		int nrPairs = 0;

		final double orientationAtX = orientation.getValueAtDim(0) * distance;
		final double orientationAtY = orientation.getValueAtDim(1) * distance;
		for (int y = 0; y < pixels.length; y++) {
			for (int x = 0; x < pixels[y].length; x++) {
				// ignore pixels not in mask
				if (pixels[y][x] == Integer.MAX_VALUE) {
					continue;
				}

				// // get second pixel
				final int sx = (int) (x + orientationAtX);
				final int sy = (int) (y + orientationAtY);

				// second pixel in interval and mask
				if (sx >= 0 && sy >= 0 && sy < pixels.length
						&& sx < pixels[sy].length
						&& pixels[sy][sx] != Integer.MAX_VALUE) {
					output[pixels[y][x]][pixels[sy][sx]]++;
					nrPairs++;
				}

			}
		}

		if (nrPairs > 0) {
			double divisor = 1.0 / nrPairs;
			for (int row = 0; row < output.length; row++) {
				for (int col = 0; col < output[row].length; col++) {
					output[row][col] *= divisor;
				}
			}
		}

		return output;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2 && orientation.isCompatible(2);
	}
}
