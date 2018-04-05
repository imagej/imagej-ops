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

package net.imagej.ops.threshold.localBernsen;

import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imagej.ops.threshold.apply.LocalThreshold;
import net.imagej.ops.threshold.localMidGrey.LocalMidGreyThreshold;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * LocalThresholdMethod which is similar to {@link LocalMidGreyThreshold}, but uses a
 * constant value rather than the value of the input pixel when the contrast in
 * the neighborhood of that pixel is too small.
 *
 * @author Jonathan Hale
 * @author Stefan Helfrich (University of Konstanz)
 * @param <T> input type
 */
@Plugin(type = Ops.Threshold.LocalBernsenThreshold.class)
public class LocalBernsenThreshold<T extends RealType<T>> extends
	LocalThreshold<T> implements Ops.Threshold.LocalBernsenThreshold
{

	@Parameter
	private double contrastThreshold;

	@Parameter
	private double halfMaxValue;

	@Override
	protected CenterAwareComputerOp<T, BitType> unaryComputer(final T inClass,
		final BitType outClass)
	{
		final LocalThresholdMethod<T> op = new LocalThresholdMethod<T>() {

			private UnaryFunctionOp<Iterable<T>, Pair<T, T>> minMaxFunc;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void compute(final Iterable<T> neighborhood, final T center,
				final BitType output)
			{

				if (minMaxFunc == null) {
					minMaxFunc = (UnaryFunctionOp) Functions.unary(ops(), Ops.Stats.MinMax.class, Pair.class, neighborhood);
				}

				final Pair<T, T> outputs = minMaxFunc.calculate(neighborhood);
				final double minValue = outputs.getA().getRealDouble();
				final double maxValue = outputs.getB().getRealDouble();
				final double midGrey = (maxValue + minValue) / 2.0;

				if ((maxValue - minValue) < contrastThreshold) {
					output.set(midGrey >= halfMaxValue);
				}
				else {
					output.set(center.getRealDouble() >= midGrey);
				}
			}

			};
		
		op.setEnvironment(ops());	
		return op;
	}

}
