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

import imagej.ops.OpService;
import imagej.service.ImageJService;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

@Plugin(type = Service.class)
public class HyperSlicingService extends AbstractService implements
	ImageJService
{

	@Parameter
	protected OpService opService;

	public RandomAccessibleInterval<?> hyperSlice(
		final RandomAccessibleInterval<?> rndAccessibleInterval, final Interval i)
	{
		return (RandomAccessibleInterval<?>) opService.run("hyperslicer",
			rndAccessibleInterval, i);
	}

	Interval[] resolveIntervals(final int[] selected, final Interval in) {

		final int totalSteps = getNumIterationSteps(selected, in);
		final Interval[] res = new Interval[totalSteps];

		final int offset = 0;

		final long[] min = new long[in.numDimensions()];
		final long[] pointCtr = new long[in.numDimensions()];
		final long[] srcDims = new long[in.numDimensions()];

		in.min(min);
		in.max(pointCtr);
		in.dimensions(srcDims);

		long[] max = pointCtr.clone();

		final int[] unselected = getUnselectedDimIndices(selected, srcDims.length);

		final long[] indicators = new long[unselected.length];
		final Interval interval = new FinalInterval(min, pointCtr);

		for (int j = indicators.length - 1; j > -1; j--) {
			indicators[j] = 1;
			if (j < indicators.length - 1) indicators[j] =
				(srcDims[unselected[j + 1]]) * indicators[j + 1];
		}

		for (final int u : unselected) {
			pointCtr[u] = -1;
		}

		for (int n = 0; n < getNumIterationSteps(selected, in); n++) {
			max = pointCtr.clone();

			for (int j = 0; j < indicators.length; j++) {
				if (n % indicators[j] == 0) pointCtr[unselected[j]]++;

				if (srcDims[unselected[j]] == pointCtr[unselected[j]]) pointCtr[unselected[j]] =
					0;
			}

			for (final int u : unselected) {
				max[u] = pointCtr[u] + min[u];
				min[u] = max[u];
			}

			res[offset + n] = new FinalInterval(min, max);
			interval.min(min);
		}
		return res;
	}

	/**
	 * @param dims
	 * @return
	 */
	private final int getNumIterationSteps(final int[] selectedDims,
		final Interval interval)
	{

		final long[] dims = new long[interval.numDimensions()];
		interval.dimensions(dims);

		final int[] unselectedDims =
			getUnselectedDimIndices(selectedDims, dims.length);
		int steps = 1;
		for (int i = 0; i < unselectedDims.length; i++) {
			steps *= dims[unselectedDims[i]];
		}

		return steps;
	}

	/**
	 * @return
	 */
	private final int[] getUnselectedDimIndices(final int[] selectedDims,
		final int numDims)
	{
		final boolean[] tmp = new boolean[numDims];
		int i;
		for (i = 0; i < selectedDims.length; i++) {
			if (selectedDims[i] >= numDims) {
				break;
			}
			tmp[selectedDims[i]] = true;
		}

		final int[] res = new int[numDims - i];

		int j = 0;
		for (int k = 0; j < res.length; k++) {
			if (k >= tmp.length || !tmp[k]) {
				res[j++] = k;
			}
		}
		return res;

	}

}
