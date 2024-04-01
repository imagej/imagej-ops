/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2024 ImageJ2 developers.
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

package net.imagej.ops.coloc;

/**
 * Helper class for MaxKendallTau op. 
 *
 * @author Shulei Wang
 * @author Ellen Arena
 */
public class MergeSort {

	private int[] data;
	private final IntComparator comparator;

	public MergeSort(int[] data, IntComparator comparator) {
		this.data = data;
		this.comparator = comparator;
	}

	public int[] getSortedData() {
		return data;
	}

	/**
	 * Sorts the {@link #data} array.
	 * <p>
	 * This implements a non-recursive merge sort.
	 * </p>
	 * 
	 * @return the equivalent number of BubbleSort swaps
	 */
	public long sort() {
		long swaps = 0;
		int n = data.length;
		// There are merge sorts which perform in-place, but their runtime is worse
		// than O(n log n)
		int[] data2 = new int[n];
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
					int compare = comparator.compare(data[i], data[j]);
					if (compare > 0) {
						swaps += (begin2 - i);
						data2[k++] = data[j++];
					}
					else {
						data2[k++] = data[i++];
					}
				}
				if (i < begin2) {

					do {
						data2[k++] = data[i++];
					}
					while (i < begin2);
				}
				else {
					while (j < end) {
						data2[k++] = data[j++];
					}
				}
				begin = end;
			}
			if (k < n) {
				System.arraycopy(data, k, data2, k, n - k);
			}
			int[] swapIndex = data2;
			data2 = data;
			data = swapIndex;
		}

		return swaps;
	}

}
