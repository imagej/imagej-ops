/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2021 ImageJ2 developers.
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MergeSortTest extends ColocalisationTest {

	// Test the class MergeSort... with sort & count example.
	@Test
	public void testMergeSort() {
		final int[] array = {3, 1, 4, 1, 5, 9, 2, 6};

		final MergeSort mergeSort = new MergeSort(array, Integer::compare);

		long bubbleCount = mergeSort.sort();
		int[] sorted = mergeSort.getSortedData();
		assertArrayEquals(new int[] {1, 1, 2, 3, 4, 5, 6, 9}, sorted);
		assertEquals(8, bubbleCount);
	}
	
	// Test the class MergeSort... with already-sorted example.
	@Test
	public void testNoSort() {
		final int[] array = {1, 2, 3, 4, 5, 6, 7, 8};

		final MergeSort mergeSort = new MergeSort(array, Integer::compare);

		long bubbleCount = mergeSort.sort();
		int[] sorted = mergeSort.getSortedData();
		assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, sorted);
		assertEquals(0, bubbleCount);
	}
}
