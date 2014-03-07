/*
 * #%L
 * A framework for reusable algorithms.
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

package imagej.ops.slicer;

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

/**
 * @author Christian Dietz
 */
public abstract class AbstractSlicer implements Slicer {

	@Override
	public <T> RandomAccessibleInterval<T> hyperSlice(
			final RandomAccessibleInterval<T> in, final Interval i) {
		boolean oneSizedDims = false;

		for (int d = 0; d < in.numDimensions(); d++) {
			if (in.dimension(d) == 1) {
				oneSizedDims = true;
				break;
			}
		}

		if (Intervals.equals(in, i) && !oneSizedDims)
			return in;

		IntervalView<T> res;
		if (Intervals.contains(in, i))
			res = Views.offsetInterval(in, i);
		else {
			throw new RuntimeException(
					"Interval must fit into src in SubsetViews.subsetView(...)");
		}

		for (int d = i.numDimensions() - 1; d >= 0; --d)
			if (i.dimension(d) == 1 && res.numDimensions() > 1)
				res = Views.hyperSlice(res, d, 0);

		return res;
	}

}
