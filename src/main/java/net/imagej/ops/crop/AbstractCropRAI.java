/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
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

package net.imagej.ops.crop;

import net.imagej.ops.Ops;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;

/**
 * @author Christian Dietz
 * @author Martin Horn
 */
public abstract class AbstractCropRAI<T, I extends RandomAccessibleInterval<T>>
	implements Ops.Crop
{

	@Parameter
	protected Interval interval;

	protected RandomAccessibleInterval<T> crop(RandomAccessibleInterval<T> in) {
		boolean oneSizedDims = false;

		for (int d = 0; d < in.numDimensions(); d++) {
			if (in.dimension(d) == 1) {
				oneSizedDims = true;
				break;
			}
		}

		if (Intervals.equals(in, interval) && !oneSizedDims) return in;

		IntervalView<T> res;
		if (Intervals.contains(in, interval)) res =
			Views.offsetInterval(in, interval);
		else {
			throw new RuntimeException("Intervals don't match!");
		}

		for (int d = interval.numDimensions() - 1; d >= 0; --d)
			if (interval.dimension(d) == 1 && res.numDimensions() > 1) res =
				Views.hyperSlice(res, d, 0);

		return res;
	}

}
