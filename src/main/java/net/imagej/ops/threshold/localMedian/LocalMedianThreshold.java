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

package net.imagej.ops.threshold.localMedian;

import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imagej.ops.threshold.apply.LocalThreshold;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * LocalThresholdMethod using median.
 * 
 * @author Jonathan Hale
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalMedianThreshold.class)
public class LocalMedianThreshold<T extends RealType<T>> extends LocalThreshold<T>
	implements Ops.Threshold.LocalMedianThreshold
{

	@Parameter
	private double c;

	@Override
	protected CenterAwareComputerOp<T, BitType> unaryComputer(final T inClass,
		final BitType outClass)
	{
		final LocalThresholdMethod<T> op = new LocalThresholdMethod<T>() {

			private UnaryComputerOp<Iterable<T>, DoubleType> median;

			@Override
			public void compute(final Iterable<T> neighborhood, final T center, final BitType output) {

				if (median == null) {
					median = Computers
							.unary(ops(), Ops.Stats.Median.class, DoubleType.class, neighborhood);
				}

				final DoubleType m = new DoubleType();
				median.compute(neighborhood, m);
				output.set(center.getRealDouble() > m.getRealDouble() - c);
			}
		};

		op.setEnvironment(ops());
		return op;
	}
	
}
