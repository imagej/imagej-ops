/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.filter.convolve;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractFilterImg;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Convolves an image naively (no FFTs).
 */
@Plugin(type = Ops.Filter.Convolve.class, priority = Priority.HIGH_PRIORITY)
public class ConvolveNaiveImg<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
	extends AbstractFilterImg<I, O, K> implements Ops.Filter.Convolve, Contingent
{

	@Override
	public void compute1(final Img<I> img, final Img<O> out) {
		if (getOBFInput() == null) {
			setOBFInput(new OutOfBoundsConstantValueFactory<I, RandomAccessibleInterval<I>>(
				Util.getTypeFromInterval(img).createVariable()));
		}

		if ((getOBFKernel() == null) && (getKernel() != null)) {
			setOBFKernel(new OutOfBoundsConstantValueFactory<K, RandomAccessibleInterval<K>>(
				Util.getTypeFromInterval(getKernel()).createVariable()));
		}

		// extend the input
		RandomAccessibleInterval<I> extendedIn =
			Views.interval(Views.extend(img, getOBFInput()), img);

		OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput =
			new OutOfBoundsConstantValueFactory<>(Util
				.getTypeFromInterval(out).createVariable());

		// extend the output
		RandomAccessibleInterval<O> extendedOut =
			Views.interval(Views.extend(out, obfOutput), out);

		ops().filter().convolve(extendedOut, extendedIn, getKernel());
	}

	@Override
	public boolean conforms() {
		// conforms only if the kernel is sufficiently small
		return Intervals.numElements(getKernel()) <= 9;
	}

}
