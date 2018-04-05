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
package net.imagej.ops.coloc;

/**
 * Sorts and <code>int[]</code> according to a custom comparator.
 * <p>
 * This is an implementation of introsort, i.e. it is stable because
 * it tries the quicksort algorithm first and falls back to the heap
 * sort when it detects an unfavorable execution path.
 * </p>
 *  
 * @author Johannes Schindelin
 */
public class IntArraySorter {

	private final static int SORT_SIZE_THRESHOLD = 16;

	public static void sort(int[] array, IntComparator comparator) {
		introSort(array, comparator, 0, array.length, array.length);
		insertionSort(array, comparator);
	}

	private static void introSort(int[] array, IntComparator comparator, int begin, int end, int limit)
	{
		while (end - begin > SORT_SIZE_THRESHOLD) {
			if (limit == 0) {
				heapSort(array, comparator, begin, end);
				return;
			}
			limit >>= 1;

			// median of three
			int a = array[begin];
			int b = array[begin + (end - begin) / 2 + 1];
			int c = array[end - 1];
			int median;
			if (comparator.compare(a, b) < 0) {
				median = comparator.compare(b, c) < 0 ?
					b : (comparator.compare(a, c) < 0 ? c : a);
			} else {
				median = comparator.compare(b, c) > 0 ?
					b : (comparator.compare(a, c) > 0 ? c : a);
			}

			// partition
			int pivot, i = begin, j = end;
			for (;;) {
				while (comparator.compare(array[i], median) < 0) {
					++i;
				}
				--j;
				while (comparator.compare(median, array[j]) < 0) {
					--j;
				}
				if (i >= j) {
					pivot = i;
					break;
				}
				int swap = array[i];
				array[i] = array[j];
				array[j] = swap;
				++i;
			}

			introSort(array, comparator, pivot, end, limit);
			end = pivot;
		}
	}

	private static void heapSort(int[] array, IntComparator comparator,
			int begin, int end)
	{
		int count = end - begin;
		for (int i = count / 2 - 1; i >= 0; --i) {
			siftDown(array, comparator, i, count, begin);
		}
		for (int i = count - 1; i > 0; --i) {
			// swap begin and begin + i
			int swap = array[begin + i];
			array[begin + i] = array[begin];
			array[begin] = swap;

			siftDown(array, comparator, 0, i, begin);
		}
	}

	private static void siftDown(int[] array, IntComparator comparator,
			int i, int count, int offset)
	{
		int value = array[offset + i];
		while (i < count / 2) {
			int child = 2 * i + 1;
			if (child + 1 < count &&
					comparator.compare(array[child], array[child + 1]) < 0) {
				++child;
			}
			if (comparator.compare(value, array[child]) >= 0) {
				break;
			}
			array[offset + i] = array[offset + child];
			i = child;
		}
		array[offset + i] = value;
	}

	private static void insertionSort(int[] array,
			IntComparator comparator)
	{
		for (int j = 1; j < array.length; ++j) {
			int t = array[j];
			int i = j - 1;
			while (i >= 0 && comparator.compare(array[i], t) > 0) {
				array[i + 1] = array[i];
				i = i - 1;
			}
			array[i + 1] = t;
		}
	}
}
