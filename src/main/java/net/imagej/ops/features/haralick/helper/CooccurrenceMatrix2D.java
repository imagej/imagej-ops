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

package net.imagej.ops.features.haralick.helper;

import java.util.Arrays;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MaxFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MinFeature;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This Helper Class holds a co-occurrence matrix.
 * 
 * @author Stephan Sellien, University of Konstanz
 * @author Christian Dietz, University of Konstanz
 * @author Andreas Graumann, University of Konstanz
 */
@Plugin(type = Op.class)
public class CooccurrenceMatrix2D implements CooccurrenceMatrix {

	public static enum MatrixOrientation {
		DIAGONAL(1, -1), ANTIDIAGONAL(1, 1), HORIZONTAL(1, 0), VERTICAL(0, 1);

		public final int dx;

		public final int dy;

		private MatrixOrientation(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
	}

	@Parameter
	private IterableInterval<? extends RealType<?>> ii;

	@Parameter(label = "Number of Gray Levels", min = "0", max = "128", stepSize = "1", initializer = "32")
	private int nrGreyLevels;

	@Parameter(label = "Distance", min = "0", max = "128", stepSize = "1", initializer = "1")
	private int distance;

	// TODO use enum
	@Parameter(label = "Matrix Orientation", choices = {"DIAGONAL", "ANTIDIAGONAL", "HORIZONTAL", "VERTICAL"})
	private String orientation;

	@Parameter
	private MinFeature<? extends RealType<?>> min;

	@Parameter
	private MaxFeature<? extends RealType<?>> max;

	@Parameter(type = ItemIO.OUTPUT)
	private double[][] output;

	@Override
	public void run() {

		int dimX = -1;
		int dimY = -1;

		for (int d = 0; d < ii.numDimensions(); d++) {
			if (ii.dimension(d) > 1) {
				if (dimX == -1) {
					dimX = d;
				} else {
					dimY = d;
					break;
				}
			}
		}
		
		final MatrixOrientation orientation = MatrixOrientation
				.valueOf(this.orientation);

		double[][] matrix = new double[nrGreyLevels][nrGreyLevels];

		final Cursor<? extends RealType<?>> cursor = ii.cursor();

		final double localMin = min.getOutput().getRealDouble();

		final double localMax = max.getOutput().getRealDouble();

		final int[][] pixels = new int[(int) ii.dimension(dimY)][(int) ii
				.dimension(dimX)];

		for (int i = 0; i < pixels.length; i++) {
			Arrays.fill(pixels[i], Integer.MAX_VALUE);
		}

		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(dimY) - (int) ii.min(dimY)][cursor
					.getIntPosition(dimX) - (int) ii.min(dimX)] = (int) (((cursor
					.get().getRealDouble() - localMin) / (localMax - localMin)) * (nrGreyLevels - 1));
		}

		int nrPairs = 0;

		for (int y = 0; y < pixels.length; y++) {
			for (int x = 0; x < pixels[y].length; x++) {
				// ignore pixels not in mask
				if (pixels[y][x] == Integer.MAX_VALUE) {
					continue;
				}

				// // get second pixel
				final int sx = x + orientation.dx * distance;
				final int sy = y + orientation.dy * distance;

				// second pixel in interval and mask
				if (sx >= 0 && sy >= 0 && sy < pixels.length
						&& sx < pixels[sy].length
						&& pixels[sy][sx] != Integer.MAX_VALUE) {
					matrix[pixels[y][x]][pixels[sy][sx]]++;
					nrPairs++;
				}

			}
		}

		if (nrPairs > 0) {
			double divisor = 1.0 / nrPairs;
			for (int row = 0; row < matrix.length; row++) {
				for (int col = 0; col < matrix[row].length; col++) {
					matrix[row][col] *= divisor;
				}
			}
		}

		output = matrix;
	}

	@Override
	public double[][] getOutput() {
		return output;
	}

	@Override
	public void setOutput(double[][] output) {
		this.output = output;
	}
}
