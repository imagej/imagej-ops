/*-
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

package net.imagej.ops.coloc.maxTKendallTau;

import java.util.Collections;
import java.util.Random;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.coloc.ColocUtil;
import net.imagej.ops.coloc.IntArraySorter;
import net.imagej.ops.coloc.MergeSort;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.IntervalIndexer;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.IntArray;

/**
 * This algorithm calculates Maximum Trunctated Kendall Tau (MTKT) from Wang et
 * al. (2017); computes thresholds using Otsu method.
 *
 * @param <T> Type of the first image
 * @param <U> Type of the second image
 * 
 * @author Ellen T Arena
 * @author Shulei Wang
 * @author Curtis Rueden
 */
@Plugin(type = Ops.Coloc.MaxTKendallTau.class)
public class MTKT<T extends RealType<T>, U extends RealType<U>>
	extends AbstractBinaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<U>, Double> implements
	Ops.Coloc.MaxTKendallTau, Contingent
{

	@Parameter(required=false)
	private long seed = 0x89302341;

	@Override
	public Double calculate(final RandomAccessibleInterval<T> image1, final RandomAccessibleInterval<U> image2) {
		// check image sizes
		// TODO: Add these checks to conforms().
		if (!(Intervals.equalDimensions(image1, image2))) {
			throw new IllegalArgumentException("Image dimensions do not match");
		}
		final long n1 = Intervals.numElements(image1);
		if (n1 > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Image dimensions too large: " + n1);
		}
		final int n = (int) n1;

		// compute thresholds
		final double thresh1 = threshold(image1);
		final double thresh2 = threshold(image2);

		double[][] rank = rankTransformation(image1, image2, thresh1, thresh2, n, seed);

		double maxtau = calculateMaxKendallTau(rank, thresh1, thresh2, n);
		return maxtau;
	}

	<V extends RealType<V>> double threshold(final RandomAccessibleInterval<V> image) {
		final Histogram1d<V> histogram = ops().image().histogram(Views.iterable(image));
		return ops().threshold().otsu(histogram).getRealDouble();
	}

	static <T extends RealType<T>, U extends RealType<U>> double[][] rankTransformation(final RandomAccessibleInterval<T> image1, final RandomAccessibleInterval<U> image2, final double thres1,
		final double thres2, final int n, long seed)
	{
		// FIRST...
		final int[] rankIndex1 = rankSamples(image1, seed);
		final int[] rankIndex2 = rankSamples(image2, seed);

		IntArray validIndex = new IntArray(new int[n]);
		validIndex.setSize(0);
		for (int i = 0; i < n; i++)
		{
			if(rankIndex1[i] >= thres1 && rankIndex2[i] >= thres2)
			{
				validIndex.addValue(i);
			}
		}
		int rn = validIndex.size();
		double[][] finalRanks = new double[rn][2];
		for (int i = 0; i < rn; i++) {
			final int index = validIndex.getValue(i);
			finalRanks[i][0] = Math.floor(rankIndex1[index]);
			finalRanks[i][1] = Math.floor(rankIndex2[index]);
		}
		return finalRanks;
	}

	private static <V extends RealType<V>> int[] rankSamples(RandomAccessibleInterval<V> image, long seed) {
		final long elementCount = Intervals.numElements(image);
		if (elementCount > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Image dimensions too large: " + elementCount);
		}
		final int n = (int) elementCount;

		// NB: Initialize rank index in random order, to ensure random tie-breaking.
		final int[] rankIndex = new int[n];
		for (int i = 0; i < n; i++) {
			rankIndex[i] = i;
		}
		Random r = new Random(seed);
		ColocUtil.shuffle(rankIndex, r);

		final V a = Util.getTypeFromInterval(image).createVariable();
		final RandomAccess<V> ra = image.randomAccess();
		Collections.sort(new IntArray(rankIndex), (indexA, indexB) -> {
			IntervalIndexer.indexToPosition(indexA, image, ra);
			a.set(ra.get());
			IntervalIndexer.indexToPosition(indexB, image, ra);
			final V b = ra.get();
			return a.compareTo(b);
		});
		return rankIndex;
	}

	static double calculateMaxKendallTau(final double[][] rank,
		final double thresholdRank1, final double thresholdRank2, final int n)
	{
		final int rn = rank.length;
		int an;
		final double step = 1 + 1.0 / Math.log(Math.log(n)); /// ONE PROBLEM IS HERE - STEP SIZE IS PERHAPS TOO SMALL??
		double tempOff1 = 1;
		double tempOff2;
		IntArray activeIndex = new IntArray();
		double sdTau;
		double kendallTau;
		double normalTau;
		double maxNormalTau = Double.MIN_VALUE;

		while (tempOff1 * step + thresholdRank1 < n) {
			tempOff1 *= step;
			tempOff2 = 1;
			while (tempOff2 * step + thresholdRank2 < n) {
				tempOff2 *= step;

				activeIndex.setSize(0);
				for (int i = 0; i < rn; i++) {
					if (rank[i][0] >= n - tempOff1 && rank[i][1] >= n - tempOff2) {
						activeIndex.addValue(i);
					}
				}
				an = activeIndex.size();
				if (an > 1) {
					kendallTau = calculateKendallTau(rank, activeIndex);
					sdTau = Math.sqrt(2.0 * (2 * an + 5) / 9 / an / (an - 1));
					normalTau = kendallTau / sdTau;
				}
				else {
					normalTau = Double.MIN_VALUE;
				}
				if (normalTau > maxNormalTau) maxNormalTau = normalTau;
			}
		}
		return maxNormalTau;
	}
	static double calculateKendallTau(final double[][] rank,
		final IntArray activeIndex)
	{
		final int an = activeIndex.size();
		
		int indicatr = 0;
		final double[][] partRank = new double[2][an];
		for (final Integer i : activeIndex) {
			partRank[0][indicatr] = rank[i][0];
			partRank[1][indicatr] = rank[i][1];
			indicatr++;
		}
		final double[] partRank1 = partRank[0];
		final double[] partRank2 = partRank[1];

		final int[] index = new int[an];
		for (int i = 0; i < an; i++) {
			index[i] = i;
		}

		IntArraySorter.sort(index, (a, b) -> Double.compare(partRank1[a], partRank1[b]));

		final MergeSort mergeSort = new MergeSort(index, (a, b) -> Double.compare(partRank2[a], partRank2[b]));

		final long n0 = an * (long) (an - 1) / 2;
		final long S = mergeSort.sort();
		return (n0 - 2 * S) / (double) n0;
	}

	@Override
	public boolean conforms() {
		return true;
	}
}
