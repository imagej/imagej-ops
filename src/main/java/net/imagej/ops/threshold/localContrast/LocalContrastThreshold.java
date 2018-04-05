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

package net.imagej.ops.threshold.localContrast;

import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imagej.ops.threshold.apply.LocalThreshold;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * LocalThresholdMethod which determines whether a pixel is closer to the
 * maximum or minimum pixel of a neighborhood.
 * 
 * @author Jonathan Hale
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalContrastThreshold.class)
public class LocalContrastThreshold<T extends RealType<T>> extends
		LocalThreshold<T> implements Ops.Threshold.LocalContrastThreshold {

	@Override
	protected CenterAwareComputerOp<T, BitType> unaryComputer(final T inClass,
		final BitType outClass)
	{
		final LocalThresholdMethod<T> op = new LocalThresholdMethod<T>() {

			private UnaryFunctionOp<Iterable<T>, Pair<T, T>> minMaxFunc;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void compute(final Iterable<T> neighborhood, final T center, final BitType output) {

				if (minMaxFunc == null) {
					minMaxFunc = (UnaryFunctionOp) Functions.unary(ops(),
						Ops.Stats.MinMax.class, Pair.class, neighborhood);
				}

				final Pair<T, T> outputs = minMaxFunc.calculate(neighborhood);

				final double centerValue = center.getRealDouble();
				final double diffMin = centerValue - outputs.getA().getRealDouble();
				final double diffMax = outputs.getB().getRealDouble() - centerValue;

				// set to background (false) if pixel closer to min value,
				// and to foreground (true) if pixel closer to max value.
				// If diffMin and diffMax are equal, output will be set to fg.
				output.set(diffMin <= diffMax);
			}
		};

		op.setEnvironment(ops());
		return op;
	}

}
