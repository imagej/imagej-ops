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
package net.imagej.ops.coloc.kendallTau;

import java.util.Arrays;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.coloc.ColocUtil;
import net.imagej.ops.coloc.IntArraySorter;
import net.imagej.ops.coloc.IntComparator;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.IterablePair;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * This algorithm calculates Kendall's Tau-b rank correlation coefficient
 * <p>
 * According to <a href=
 * "http://en.wikipedia.org/wiki/Kendall_tau_rank_correlation_coefficient">this
 * article</a>, Tau-b (appropriate if multiple pairs share the same first, or
 * second, value), the rank correlation of a set of pairs <tt>(x_1, y_1), ...,
 * (x_n, y_n)</tt>:
 * </p>
 *
 * <pre>
 * Tau_B = (n_c - n_d) / sqrt( (n_0 - n_1) (n_0 - n_2) )
 * </pre>
 *
 * where
 *
 * <pre>
 * n_0 = n (n - 1) / 2
 * n_1 = sum_i t_i (t_i - 1) / 2
 * n_2 = sum_j u_j (u_j - 1) / 2
 * n_c = #{ i, j; i != j &amp;&amp; (x_i - x_j) * (y_i - y_j) &gt; 0 },
 * &nbsp; i.e. the number of pairs of pairs agreeing on the order of x and y, respectively
 * n_d = #{ i, j: i != j &amp;&amp; (x_i - x_j) * (y_i - y_j) &lt; 0 },
 * &nbsp; i.e. the number of pairs of pairs where x and y are ordered opposite of each other
 * t_i = number of tied values in the i-th group of ties for the first quantity
 * u_j = number of tied values in the j-th group of ties for the second quantity
 * </pre>
 *
 * @author Johannes Schindelin
 * @author Ellen T Arena
 * @param <T>
 */
@Plugin(type = Ops.Coloc.KendallTau.class)
public class KendallTauBRank<T extends RealType<T>, U extends RealType<U>>
		/* extends Algorithm<T> */ extends AbstractBinaryFunctionOp<Iterable<T>, Iterable<U>, Double>
		implements Ops.Coloc.KendallTau, Contingent {
	
	@Override
	public Double calculate(Iterable<T> image1, Iterable<U> image2) {
		final Iterable<Pair<T, U>> samples = new IterablePair<>(image1, image2);
		return calculateMergeSort(samples);
	}

	private double[][] getPairs(final Iterable<Pair<T, U>> samples) {
		// TODO: it is ridiculous that this has to be counted all the time (i.e. in most if not all measurements!).
		// We only need an upper bound to begin with, so even the number of pixels in the first channel would be enough!
		int capacity = 0;
		for (@SuppressWarnings("unused") Pair<T, U> sample : samples) {
			capacity++;
		}

		double[] values1 = new double[capacity];
		double[] values2 = new double[capacity];
		int count = 0;
		for (Pair<T, U> sample : samples) {
			values1[count] = sample.getA().getRealDouble();
			values2[count] = sample.getB().getRealDouble();
			count++;
		}

		if (count < capacity) {
			values1 = Arrays.copyOf(values1, count);
			values2 = Arrays.copyOf(values2, count);
		}
		return new double[][] { values1, values2 };
	}

	/**
	 * Calculate Tau-b efficiently.
	 * <p>
	 * This implementation is based on <a
	 * href="http://en.wikipedia.org/wiki/Kendall_tau_rank_correlation_coefficient#Algorithms">this
	 * description of the merge sort based way to calculate Tau-b</a>. This is
	 * supposed to be the method described in:
	 * </p>
	 * <blockquote>Knight, W. (1966). "A Computer Method for Calculating
	 * Kendall's Tau with Ungrouped Data". Journal of the American Statistical
	 * Association 61 (314): 436–439. doi:10.2307/2282833.</blockquote>
	 * <p>
	 * but since that article is not available as Open Access, it is
	 * unnecessarily hard to verify.
	 * </p>
	 * 
	 * @param samples the iterator of the pairs
	 * @return Tau-b
	 */
	private double calculateMergeSort(final Iterable<Pair<T, U>> samples) {
		final double[][] pairs = getPairs(samples);
		final double[] x = pairs[0];
		final double[] y = pairs[1];
		final int n = x.length;

		int[] index = new int[n];
		for (int i = 0; i < n; i++) {
			index[i] = i;
		}

		// First sort by x as primary key, y as secondary one.
		// We use IntroSort here because it is fast and in-place.
		IntArraySorter.sort(index, new IntComparator() {

			@Override
			public int compare(int a, int b) {
				double xa = x[a], ya = y[a];
				double xb = x[b], yb = y[b];
				int result = Double.compare(xa, xb);
				return result != 0 ? result : Double.compare(ya, yb);
			}

		});

		// The trick is to count the ties of x (n1) and the joint ties of x and y (n3) now, while
		// index is sorted with regards to x.
		long n0 = n * (long)(n - 1) / 2;
		long n1 = 0, n3 = 0;

		for (int i = 1; i < n; i++) {
			double x0 = x[index[i - 1]];
			if (x[index[i]] != x0) {
				continue;
			}
			double y0 = y[index[i - 1]];
			int i1 = i;
			do {
				double y1 = y[index[i1++]];
				if (y1 == y0) {
					int i2 = i1;
					while (i1 < n && x[index[i1]] == x0 && y[index[i1]] == y0) {
						i1++;
					}
					n3 += (i1 - i2 + 2) * (long)(i1 - i2 + 1) / 2;
				}
				y0 = y1;
			} while (i1 < n && x[index[i1]] == x0);
			n1 += (i1 - i + 1) * (long)(i1 - i) / 2;
			i = i1;
		}

		// Now, let's perform that merge sort that also counts S, the number of
		// swaps a Bubble Sort would require (and which therefore is half the number
		// by which we have to adjust n_0 - n_1 - n_2 + n_3 to obtain n_c - n_d)
		final MergeSort mergeSort = new MergeSort(index, new IntComparator() {

			@Override
			public int compare(int a, int b) {
				double ya = y[a];
				double yb = y[b];
				return Double.compare(ya, yb);
			}
		});
		long S = mergeSort.sort();
		index = mergeSort.getSorted();
		long n2 = 0;

		for (int i = 1; i < n; i++) {
			double y0 = y[index[i - 1]];
			if (y[index[i]] != y0) {
				continue;
			}
			int i1 = i + 1;
			while (i1 < n && y[index[i1]] == y0) {
				i1++;
			}
			n2 += (i1 - i + 1) * (long)(i1 - i) / 2;
			i = i1;
		}

		return (n0 - n1 - n2 + n3 - 2 * S) / Math.sqrt((n0 - n1) * (double)(n0 - n2));
	}

	private final static class MergeSort {

		private int[] index;
		private final IntComparator comparator;

		public MergeSort(int[] index, IntComparator comparator) {
			this.index = index;
			this.comparator = comparator;
		}

		public int[] getSorted() {
			return index;
		}

		/**
		 * Sorts the {@link #index} array.
		 * <p>
		 * This implements a non-recursive merge sort.
		 * </p>
		 * @return the equivalent number of BubbleSort swaps
		 */
		public long sort() {
			long swaps = 0;
			int n = index.length;
			// There are merge sorts which perform in-place, but their runtime is worse than O(n log n)
			int[] index2 = new int[n];
			for (int step = 1; step < n; step <<= 1) {
				int begin = 0, k = 0;
				for (;;) {
					int begin2 = begin + step, end = begin2 + step;
					if (end >= n) {
						if (begin2 >= n) {
							break;
						}
						end = n;
					}

					// calculate the equivalent number of BubbleSort swaps
					// and perform merge, too
					int i = begin, j = begin2;
					while (i < begin2 && j < end) {
						int compare = comparator.compare(index[i], index[j]);
						if (compare > 0) {
							swaps += (begin2 - i);
							index2[k++] = index[j++];
						} else {
							index2[k++] = index[i++];
						}
					}
					if (i < begin2) {
						do {
							index2[k++] = index[i++];
						} while (i < begin2);
					} else {
						while (j < end) {
							index2[k++] = index[j++];
						}
					}
					begin = end;
				}
				if (k < n) {
					System.arraycopy(index, k, index2, k, n - k);
				}
				int[] swapIndex = index2;
				index2 = index;
				index = swapIndex;
			}

			return swaps;
		}
	}

	@Override
	public boolean conforms() {
		return ColocUtil.sameIterationOrder(in1(), in2());
	}
}
