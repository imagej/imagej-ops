/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.statistics.firstorder;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpUtils;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MedianFeature;
import net.imagej.ops.statistics.FirstOrderOps.Median;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Calculate {@link Median} of {@link Iterable} of {@link RealType}.
 * 
 * @author Christian Dietz
 */
@Plugin(type = Op.class, name = Median.NAME, label = Median.LABEL, priority = Priority.LOW_PRIORITY)
public class DefaultMedianIRT<I extends RealType<I>, O extends RealType<O>>
		extends AbstractOutputFunction<Iterable<I>, O> implements
		Median, MedianFeature<O> {

	/**
	 * Returns the value of the kth lowest element. Do note that for nth lowest
	 * element, k = n - 1.
	 */
	private double select(final ArrayList<Double> array, int left, int right,
			final int k) {

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

	@Override
	public O createOutput(Iterable<I> in) {
        return OpUtils.<O> cast(new DoubleType());
	}

	@Override
	protected O safeCompute(Iterable<I> input, O output) {

		final ArrayList<Double> statistics = new ArrayList<Double>();

		for (final RealType<?> type : input) {
			statistics.add(type.getRealDouble());
		}

		output.setReal(select(statistics, 0, statistics.size() - 1,
				statistics.size() / 2));
		return output;
	}
}
