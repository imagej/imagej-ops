/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.Ops.Stats.Median;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@link Median}
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @author Christian Dietz, University of Konstanz.
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = StatsOp.class, name = Median.NAME, label = "Statistics: Median")
public class DefaultMedian<I extends RealType<I>, O extends RealType<O>>
	extends AbstractStatsOp<Iterable<I>, O> implements Median
{

	@Override
	public void compute(final Iterable<I> input, final O output) {
		final ArrayList<Double> statistics = new ArrayList<Double>();

		final Iterator<I> it = input.iterator();
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
