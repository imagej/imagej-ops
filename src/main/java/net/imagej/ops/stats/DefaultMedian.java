
package net.imagej.ops.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.Median;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = Median.NAME, label = "Statistics: Median")
public class DefaultMedian<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements Median
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		final ArrayList<Double> statistics = new ArrayList<Double>();

		final Iterator<T> it = input.iterator();
		while (it.hasNext()) {
			statistics.add(it.next().getRealDouble());
		}

		output.setReal(select(statistics, 0, statistics.size() - 1, statistics
			.size() / 2));
	}

	/**
	 * Returns the value of the kth lowest element. Do note that for nth lowest
	 * element, k = n - 1.
	 */
	private double select(final ArrayList<Double> array, final int inLeft,
		final int inRight, final int k)
	{

		int left = inLeft;
		int right = inRight;

		while (true) {

			if (right <= left + 1) {

				if (right == left + 1 && array.get(right) < array.get(left)) {
					swap(array, left, right);
				}

				return array.get(k);

			}
			final int middle = (left + right) >>> 1;
			swap(array, middle, left + 1);

			if (array.get(left) > array.get(right)) {
				swap(array, left, right);
			}

			if (array.get(left + 1) > array.get(right)) {
				swap(array, left + 1, right);
			}

			if (array.get(left) > array.get(left + 1)) {
				swap(array, left, left + 1);
			}

			int i = left + 1;
			int j = right;
			final double pivot = array.get(left + 1);

			while (true) {
				do
					++i;
				while (array.get(i) < pivot);
				do
					--j;
				while (array.get(j) > pivot);

				if (j < i) {
					break;
				}

				swap(array, i, j);
			}

			array.set(left + 1, array.get(j));
			array.set(j, pivot);

			if (j >= k) {
				right = j - 1;
			}

			if (j <= k) {
				left = i;
			}
		}
	}

	/** Helper method for swapping array entries */
	private void swap(final List<Double> array, final int a, final int b) {
		final double temp = array.get(a);
		array.set(a, array.get(b));
		array.set(b, temp);
	}
}
