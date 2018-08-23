
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
