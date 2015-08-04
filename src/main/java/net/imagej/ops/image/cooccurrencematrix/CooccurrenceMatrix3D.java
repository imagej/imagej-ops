/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

import java.util.List;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Image.CooccurrenceMatrix;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.ops.operation.iterableinterval.unary.MakeCooccurrenceMatrix.HaralickFeature;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This Helper Class holds a co-occurrence matrix for volumetric images
 * (3-Dimensional). It's features are ordered according to
 * {@link HaralickFeature}
 * 
 * @author Stephan Sellien, University of Konstanz
 * @author Andreas Graumann, University of Konstanz
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = CooccurrenceMatrix.class, name = CooccurrenceMatrix.NAME)
public class CooccurrenceMatrix3D<T extends RealType<T>> extends
		AbstractFunctionOp<IterableInterval<T>, double[][]> implements
		CooccurrenceMatrix, Contingent {

	public static enum MatrixOrientation {
		// 2D directions
		HORIZONTAL(1, 0, 0), VERTICAL(0, 1, 0), DIAGONAL(-1, 1, 0), ANTIDIAGONAL(
				1, 1, 0),

		// 3D Horizontal directions
		HORIZONTAL_VERTICAL(1, 0, 1), HORIZONTAL_DIAGONAL(1, 0, -1),

		// 3D Vertical directions
		VERTICAL_VERTICAL(0, 1, 1), VERTICAL_DIAGONAL(0, 1, -1),

		// 3D Diagonal directions
		DIAGONAL_VERTICAL(-1, 1, 1), DIAGONAL_DIAGONAL(-1, 1, -1),

		// 3D Antidiagonal directions
		ANTIDIAGONAL_VERTICAL(1, 1, 1), ANTIDIAGONAL_DIAGONAL(1, 1, -1),

		// 3D depth direction
		DEPTH(0, 0, 1);

		public final int dx;
		public final int dy;
		public final int dz;

		private MatrixOrientation(int dx, int dy, int dz) {
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
		}
	}

	@Parameter
	private OpService ops;

	@Parameter(label = "Number of Gray Levels", min = "0", max = "128", stepSize = "1", initializer = "32")
	private int nrGreyLevels;

	@Parameter(label = "Distance", min = "0", max = "128", stepSize = "1", initializer = "1")
	private int distance;

	@Parameter(label = "Matrix Orientation", choices = { "HORIZONTAL",
			"VERTICAL", "DIAGONAL", "ANTIDIAGONAL", "HORIZONTAL_VERTICAL",
			"HORIZONTAL_DIAGONAL", "VERTICAL_VERTICAL", "VERTICAL_DIAGONAL",
			"DIAGONAL_VERTICAL", "DIAGONAL_DIAGONAL", "ANTIDIAGONAL_VERTICAL",
			"ANTIDIAGONAL_DIAGONAL", "DEPTH" })
	private String orientation;

	@Override
	public double[][] compute(IterableInterval<T> input) {

		final MatrixOrientation orientation = MatrixOrientation
				.valueOf(this.orientation);

		double[][] matrix = new double[nrGreyLevels][nrGreyLevels];

		final Cursor<T> cursor = input.cursor();
		final List<T> minMax = ops.stats().minMax(input);

		double localMin = minMax.get(0).getRealDouble();
		double localMax = minMax.get(1).getRealDouble();

		final int[][][] pixels = new int[(int) input.dimension(2)][(int) input
				.dimension(1)][(int) input.dimension(0)];

		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(2) - (int) input.min(2)][cursor
					.getIntPosition(1) - (int) input.min(1)][cursor
					.getIntPosition(0) - (int) input.min(0)] = (int) (((cursor
					.get().getRealDouble() - localMin) / (localMax - localMin)) * (nrGreyLevels - 1));
		}

		int nrPairs = 0;

		for (int z = 0; z < pixels.length; z++) {
			for (int y = 0; y < pixels[z].length; y++) {
				for (int x = 0; x < pixels[z][y].length; x++) {

					// ignore pixels not in mask
					if (pixels[z][y][x] == Integer.MAX_VALUE) {
						continue;
					}

					// get second pixel
					final int sx = x + orientation.dx * distance;
					final int sy = y + orientation.dy * distance;
					final int sz = z + orientation.dz * distance;

					// second pixel in interval and mask
					if (sx >= 0 && sy >= 0 && sz >= 0 && sz < pixels.length
							&& sy < pixels[sz].length
							&& sx < pixels[sz][sy].length) {

						matrix[pixels[z][y][x]][pixels[sz][sy][sx]]++;
						nrPairs++;

					}
				}
			}
		}

		// normalize matrix
		if (nrPairs > 0) {
			double divisor = 1.0 / nrPairs;
			for (int row = 0; row < matrix.length; row++) {
				for (int col = 0; col < matrix[row].length; col++) {
					matrix[row][col] *= divisor;
				}
			}
		}

		return matrix;
	}

	@Override
	public boolean conforms() {
		return getInput().numDimensions() == 3;
	}
}
