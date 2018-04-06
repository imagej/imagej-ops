/*-
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

package net.imagej.ops.coloc.pValue;

import net.imagej.ops.Ops;
import net.imagej.ops.coloc.ShuffledView;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This algorithm repeatedly executes a colocalization algorithm, computing a
 * p-value. It is based on a new statistical framework published by Wang et al
 * (2017) IEEE Signal Processing "Automated and Robust Quantification of
 * Colocalization in Dual-Color Fluorescence Microscopy: A Nonparametric
 * Statistical Approach".
 */
@Plugin(type = Ops.Coloc.PValue.class)
public class DefaultPValue<T extends RealType<T>, U extends RealType<U>> extends
	AbstractBinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<U>, PValueResult>
	implements Ops.Coloc.PValue
{

	@Parameter
	private BinaryFunctionOp<Iterable<T>, Iterable<U>, Double> op;

	@Parameter(required = false)
	private int nrRandomizations = 1000;

	@Parameter(required = false)
	private Dimensions psfSize;

	@Parameter(required = false)
	private long seed = 0x27372034;

	@Override
	public void compute(final RandomAccessibleInterval<T> image1,
		final RandomAccessibleInterval<U> image2, PValueResult output)
	{
		final int[] blockSize = blockSize(image1, psfSize);
		final RandomAccessibleInterval<T> trimmedImage1 = trim(image1, blockSize);
		final RandomAccessibleInterval<U> trimmedImage2 = trim(image2, blockSize);

		final ShuffledView<T> shuffled = new ShuffledView<>(image1, blockSize,
			seed);
		final IterableInterval<T> shuffledIterable = Views.iterable(shuffled);
		final double[] sampleDistribution = new double[nrRandomizations];

		final IterableInterval<T> iterableImage1 = Views.iterable(trimmedImage1);
		final IterableInterval<U> iterableImage2 = Views.iterable(trimmedImage2);
		final double value = op.calculate(iterableImage1, iterableImage2); 

		for (int i = 0; i < nrRandomizations; i++) {
			shuffled.shuffleBlocks();
			sampleDistribution[i] = op.calculate(shuffledIterable, iterableImage2);
		}
		output.setColocValue(value);
		output.setColocValuesArray(sampleDistribution);
		output.setPValue(calculatePvalue(value, sampleDistribution));
	}

	private double calculatePvalue(final double input,
		final double[] distribution)
	{
		double count = 0;
		for (int i = 0; i < distribution.length; i++) {
			if (distribution[i] > input) {
				count++;
			}
		}
		final double pval = count / distribution.length;
		return pval;
	}

	private static int[] blockSize(final Dimensions image,
		final Dimensions psfSize)
	{
		if (psfSize != null) return Intervals.dimensionsAsIntArray(psfSize);

		final int[] blockSize = new int[image.numDimensions()];
		for (int d = 0; d < blockSize.length; d++) {
			final long size = (long) Math.floor(Math.sqrt(image.dimension(d)));
			if (size > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Image dimension #" + d +
					" is too large: " + image.dimension(d));
			}
			blockSize[d] = (int) size;
		}
		return blockSize;
	}

	private static <V> RandomAccessibleInterval<V> trim(
		final RandomAccessibleInterval<V> image, final int[] blockSize)
	{
		final long[] min = Intervals.minAsLongArray(image);
		final long[] max = Intervals.maxAsLongArray(image);
		for (int d = 0; d < blockSize.length; d++) {
			final long trimSize = image.dimension(d) % blockSize[d];
			final long half = trimSize / 2;
			min[d] += half;
			max[d] -= trimSize - half;
		}
		return Views.interval(image, min, max);
	}
}
