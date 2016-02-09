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

package net.imagej.ops.threshold.localNiblack;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imagej.ops.threshold.apply.LocalThreshold;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * LocalThresholdMethod using Niblack's thresholding method.
 * 
 * @author Jonathan Hale
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalNiblackThreshold.class)
public class LocalNiblackThreshold<T extends RealType<T>> extends LocalThreshold<T>
	implements Ops.Threshold.LocalNiblackThreshold
{

	@Parameter
	private double c;

	@Parameter
	private double k;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		method = new LocalNiblackThresholdComputer<>((UnaryComputerOp) Computers
			.unary(ops(), Ops.Stats.Mean.class, new DoubleType(), in()),
			(UnaryComputerOp) Computers.unary(ops(), Ops.Stats.StdDev.class,
				new DoubleType(), in()));

		super.initialize();
	}
	
	private class LocalNiblackThresholdComputer<I extends RealType<I>> extends
		LocalThresholdMethod<I>
	{

		private UnaryComputerOp<Iterable<I>, DoubleType> mean;
		private UnaryComputerOp<Iterable<I>, DoubleType> stdDeviation;

		public LocalNiblackThresholdComputer(
			final UnaryComputerOp<Iterable<I>, DoubleType> mean,
			final UnaryComputerOp<Iterable<I>, DoubleType> stdDeviation)
		{
			super();
			this.mean = mean;
			this.stdDeviation = stdDeviation;
		}

		@Override
		public void compute1(final Pair<I, Iterable<I>> input,
			final BitType output)
		{

			final DoubleType m = new DoubleType();
			mean.compute1(input.getB(), m);

			final DoubleType stdDev = new DoubleType();
			stdDeviation.compute1(input.getB(), stdDev);

			output.set(input.getA().getRealDouble() > m.getRealDouble() + k * stdDev
				.getRealDouble() - c);
		}
	}
}
