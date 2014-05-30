/*
 * #%L
 * SciJava OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2013 Board of Regents of the University of
 * Wisconsin-Madison, and University of Konstanz.
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
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */
package net.imagej.ops.descriptors.haralick;

import java.util.Arrays;

import net.imagej.ops.Op;
import net.imagej.ops.descriptors.firstorderstatistics.MinMax;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.ops.data.CooccurrenceMatrix;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class)
public class CoocMatrixCreateIRT implements CoocMatrixCreate {

	@Parameter
	private CoocParameter parameter;

	@Parameter
	private IterableInterval<? extends RealType<?>> ii;

	@Parameter
	private MinMax<? extends RealType<?>> minmax;

	@Parameter(type = ItemIO.OUTPUT)
	private CooccurrenceMatrix matrix;

	@Override
	public void run() {

		final Cursor<? extends RealType<?>> cursor = ii.cursor();

		final double localMin = this.minmax.getMin().getRealDouble();

		final double localMax = this.minmax.getMax().getRealDouble();

		final int[][] pixels = new int[(int) ii.dimension(0)][(int) ii
				.dimension(1)];

		for (int i = 0; i < pixels.length; i++) {
			Arrays.fill(pixels[i], Integer.MAX_VALUE);
		}

		final CooccurrenceMatrix matrix = new CooccurrenceMatrix(
				parameter.getNrGrayLevels());

		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(0) - (int) ii.min(0)][cursor
					.getIntPosition(1) - (int) ii.min(1)] = (int) (((cursor
					.get().getRealDouble() - localMin) / (localMax - localMin)) * (parameter.nrGrayLevels - 1));
		}

		int nrPairs = 0;

		for (int y = 0; y < pixels.length; y++) {
			for (int x = 0; x < pixels[y].length; x++) {
				// ignore pixels not in mask
				if (pixels[y][x] == Integer.MAX_VALUE) {
					continue;
				}

				// // get second pixel
				final int sx = x + parameter.getOrientation().dx
						* parameter.getDistance();
				final int sy = y + parameter.getOrientation().dy
						* parameter.getDistance();
				// get third pixel
				final int tx = x - parameter.getOrientation().dx
						* parameter.getDistance();
				final int ty = y - parameter.getOrientation().dy
						* parameter.getDistance();

				// second pixel in interval and mask
				if (sx >= 0 && sy >= 0 && sy < pixels.length
						&& sx < pixels[sy].length
						&& pixels[sy][sx] != Integer.MAX_VALUE) {
					matrix.incValueAt(pixels[y][x], pixels[sy][sx]);
					nrPairs++;
				}
				// third pixel in interval
				if (tx >= 0 && ty >= 0 && ty < pixels.length
						&& tx < pixels[ty].length
						&& pixels[ty][tx] != Integer.MAX_VALUE) {
					matrix.incValueAt(pixels[y][x], pixels[ty][tx]);
					nrPairs++;
				}
			}
		}

		if (nrPairs > 0) {
			matrix.divideBy(nrPairs);
		}

		this.matrix = matrix;
	}

	@Override
	public CooccurrenceMatrix getOutput() {
		return matrix;
	}
}
