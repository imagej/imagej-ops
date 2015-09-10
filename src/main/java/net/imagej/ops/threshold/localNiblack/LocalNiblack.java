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

package net.imagej.ops.threshold.localNiblack;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Stats.Mean;
import net.imagej.ops.Ops.Stats.StdDev;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * LocalThresholdMethod using Niblacks thresholding method.
 * 
 * @author Jonathan Hale
 */
@Plugin(type = Op.class)
public class LocalNiblack<T extends RealType<T>> extends
		LocalThresholdMethod<T> implements Ops.Threshold.LocalNiblack {

	@Parameter
	private double c;

	@Parameter
	private double k;

	private ComputerOp<Iterable<T>, DoubleType> mean;

	private ComputerOp<Iterable<T>, DoubleType> stdDeviation;

	@Override
	public void compute(Pair<T, Iterable<T>> input, BitType output) {

		if (stdDeviation == null) {
			// TODO: Ensure the mean is the mean used by stdDeviation
			// FIXME: use ops.computerop(...) as soon as available
			mean = (ComputerOp<Iterable<T>, DoubleType>) ops().op(Mean.class,
					new DoubleType(), input.getB());
			stdDeviation = (ComputerOp<Iterable<T>, DoubleType>) ops().op(
					StdDev.class, new DoubleType(), input.getB());
		}

		final DoubleType m = new DoubleType();
		mean.compute(input.getB(), m);

		final DoubleType stdDev = new DoubleType();
		stdDeviation.compute(input.getB(), stdDev);

		output.set(input.getA().getRealDouble() > m.getRealDouble() + k
				* stdDev.getRealDouble() - c);
	}
}
