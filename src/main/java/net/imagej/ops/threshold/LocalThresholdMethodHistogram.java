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

package net.imagej.ops.threshold;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Image.Histogram;
import net.imagej.ops.map.neighborhood.AbstractCenterAwareComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.threshold.apply.LocalThreshold;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;

/**
 * {@link AbstractCenterAwareComputerOp} for use in {@link LocalThreshold}s.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
public abstract class LocalThresholdMethodHistogram<T extends RealType<T>, O extends BooleanType<O>>
	extends AbstractCenterAwareComputerOp<T, O>
{

	protected UnaryFunctionOp<Iterable<T>, Histogram1d<T>> histCreator;
	protected UnaryComputerOp<Histogram1d<T>, T> thresholdComputer;
	protected BinaryComputerOp<T, T, O> applyThreshold;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		histCreator = (UnaryFunctionOp) Functions.unary(ops(), Histogram.class,
			Histogram1d.class, in1() == null ? Iterable.class : in1());

		thresholdComputer = getThresholdComputer();
	}

	@Override
	public void compute(final Iterable<T> neighborhood, final T center,
		final O output)
	{
		// TODO Move to initialize when NIL objects are available
		if (applyThreshold == null) {
			applyThreshold = Computers.binary(ops(), Ops.Threshold.Apply.class, output,
				center, center);
		}

		// Compute histogram for neighborhood
		final Histogram1d<T> hist = histCreator.calculate(neighborhood);

		// Compute threshold
		final T computedThreshold = center.createVariable();
		thresholdComputer.compute(hist, computedThreshold);

		// Apply threshold
		applyThreshold.compute(center, computedThreshold, output);
	}

	protected abstract UnaryComputerOp<Histogram1d<T>, T> getThresholdComputer();

}
