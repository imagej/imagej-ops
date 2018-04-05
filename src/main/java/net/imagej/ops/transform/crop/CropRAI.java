/*
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

package net.imagej.ops.transform.crop;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Transform.Crop.class, priority = Priority.LOW)
public class CropRAI<T>
		extends AbstractBinaryFunctionOp<RandomAccessibleInterval<T>, Interval, RandomAccessibleInterval<T>>
		implements Ops.Transform.Crop {

	@Parameter(required = false)
	private boolean dropSingleDimensions = true;

	@Override
	public RandomAccessibleInterval<T> calculate(final RandomAccessibleInterval<T> input, final Interval interval) {
		boolean oneSizedDims = false;

		if (dropSingleDimensions) {
			for (int d = 0; d < interval.numDimensions(); d++) {
				if (interval.dimension(d) == 1) {
					oneSizedDims = true;
					break;
				}
			}
		}

		if (Intervals.equals(input, interval) && !oneSizedDims)
			return input;
		if (!Intervals.contains(input, interval))
			throw new RuntimeException("Intervals don't match!");
		IntervalView<T> res = Views.offsetInterval(input, interval);
		return oneSizedDims ? Views.dropSingletonDimensions(res) : res;
	}
}
