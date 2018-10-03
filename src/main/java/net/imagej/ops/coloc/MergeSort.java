
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
