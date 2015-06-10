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

package net.imagej.ops.crop;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Crop;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz, University of Konstanz
 * @author Martin Horn, University of Konstanz
 */
@Plugin(type = Op.class, name = Ops.Crop.NAME, attrs = { @Attr(
	name = "aliases", value = Ops.Crop.ALIASES) },
	priority = Priority.LOW_PRIORITY)
public class CropRAI<T> implements Crop {

	@Parameter(type = ItemIO.OUTPUT)
	private RandomAccessibleInterval<T> out;

	@Parameter
	private RandomAccessibleInterval<T> in;

	@Parameter
	protected Interval interval;

	@Parameter(required = false)
	private boolean dropSingleDimensions = true;

	@Override
	public void run() {
		boolean oneSizedDims = false;

		if (dropSingleDimensions) {
			for (int d = 0; d < interval.numDimensions(); d++) {
				if (interval.dimension(d) == 1) {
					oneSizedDims = true;
					break;
				}
			}
		}

		if (Intervals.equals(in, interval) && !oneSizedDims) {
			out = in;
		}
		else {
			IntervalView<T> res;
			if (Intervals.contains(in, interval)) res =
				Views.offsetInterval(in, interval);
			else {
				throw new RuntimeException("Intervals don't match!");
			}
			out = oneSizedDims ? Views.dropSingletonDimensions(res) : res;
		}
	}
}
