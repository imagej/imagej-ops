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

package net.imagej.ops.filter.convolve;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractFilterF;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
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
public class ConvolveNaiveF<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
	extends AbstractFilterF<I, O, K> implements Ops.Filter.Convolve, Contingent
{

	UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> convolver;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		convolver = (UnaryComputerOp) Computers.unary(ops(), ConvolveNaiveC.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class, in2());

	}

	@Override
	public RandomAccessibleInterval<O> compute2(
		final RandomAccessibleInterval<I> img,
		final RandomAccessibleInterval<K> kernel)
	{

		RandomAccessibleInterval<O> out = createOutput(img, kernel);

		if (getOBFInput() == null) {
			setOBFInput(new OutOfBoundsMirrorFactory<>(Boundary.SINGLE));
		}

		if ((getOBFKernel() == null) && (kernel != null)) {
			setOBFKernel(
				new OutOfBoundsConstantValueFactory<K, RandomAccessibleInterval<K>>(Util
					.getTypeFromInterval(kernel).createVariable()));
		}

		// extend the input
		RandomAccessibleInterval<I> extendedIn = Views.interval(Views.extend(img,
			getOBFInput()), img);

		OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput =
			new OutOfBoundsConstantValueFactory<>(Util.getTypeFromInterval(out)
				.createVariable());

		// extend the output
		RandomAccessibleInterval<O> extendedOut = Views.interval(Views.extend(out,
			obfOutput), out);

		// ops().filter().convolve(extendedOut, extendedIn, kernel);
		convolver.compute1(extendedIn, extendedOut);

		return out;
	}

	@Override
	public boolean conforms() {
		// conforms only if the kernel is sufficiently small
		return Intervals.numElements(in2()) <= 9;
	}

}
