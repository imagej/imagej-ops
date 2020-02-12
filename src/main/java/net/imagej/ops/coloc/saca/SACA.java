/*-
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

package net.imagej.ops.coloc.saca;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This algorithm is adapted from Spatially Adaptive Colocalization Analysis
 * (SACA) by Wang et al (2019); computes thresholds using Otsu method.
 *
 * @param <I> Type of the input images
 * @param <O> Type of the output image
 */
@Plugin(type = Ops.Coloc.SACA.class)
public class SACA<I extends RealType<I>, O extends RealType<O>> extends
	AbstractBinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
	implements Ops.Coloc.SACA
{

	@Parameter(required = false)
	private I thres1;

	@Parameter(required = false)
	private I thres2;

	@Parameter(required = false)
	private long seed = 0xdeadbeef;

	@Override
	public void compute(final RandomAccessibleInterval<I> image1,
		final RandomAccessibleInterval<I> image2,
		final RandomAccessibleInterval<O> result)
	{

		// check image sizes
		if (!(Intervals.equalDimensions(image1, image2))) {
			throw new IllegalArgumentException("Image dimensions do not match");
		}

		// compute thresholds if necessary
		if (thres1 == null) thres1 = threshold(image1);
		if (thres2 == null) thres2 = threshold(image2);

		AdaptiveSmoothedKendallTau.execute(image1, image2, thres1, thres2, result, seed);
	}

	<V extends RealType<V>> V threshold(final RandomAccessibleInterval<V> image) {
		final Histogram1d<V> histogram = ops().image().histogram(Views.iterable(
			image));
		return ops().threshold().otsu(histogram);
	}
}
