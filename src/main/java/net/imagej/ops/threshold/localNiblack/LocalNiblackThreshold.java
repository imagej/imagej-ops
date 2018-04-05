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

package net.imagej.ops.threshold.localNiblack;

import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imagej.ops.threshold.apply.LocalThreshold;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * LocalThresholdMethod using Niblack's thresholding method.
 * 
 * @author Jonathan Hale
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalNiblackThreshold.class,
	priority = Priority.LOW)
public class LocalNiblackThreshold<T extends RealType<T>> extends LocalThreshold<T>
	implements Ops.Threshold.LocalNiblackThreshold
{

	@Parameter
	private double c;

	@Parameter
	private double k;
	
	@Override
	protected CenterAwareComputerOp<T, BitType> unaryComputer(final T inClass,
		final BitType outClass)
	{
		final LocalThresholdMethod<T> op = new LocalThresholdMethod<T>() {

			private UnaryComputerOp<Iterable<T>, DoubleType> mean;
			private UnaryComputerOp<Iterable<T>, DoubleType> stdDeviation;

			@Override
			public void compute(final Iterable<T> neighborhood, final T center, final BitType output) {

				if (mean == null) {
					mean = Computers.unary(ops(), Ops.Stats.Mean.class, new DoubleType(),
						neighborhood);
				}

				if (stdDeviation == null) {
					stdDeviation = Computers.unary(ops(), Ops.Stats.StdDev.class,
						new DoubleType(), neighborhood);
				}

				final DoubleType m = new DoubleType();
				mean.compute(neighborhood, m);

				final DoubleType stdDev = new DoubleType();
				stdDeviation.compute(neighborhood, stdDev);

				output.set(center.getRealDouble() > m.getRealDouble() + k * stdDev
					.getRealDouble() - c);
			}
		};

		op.setEnvironment(ops());
		return op;
	}

	@Override
	public boolean conforms() {
		RectangleShape rect = getShape() instanceof RectangleShape
			? (RectangleShape) getShape() : null;
		if (rect == null) {
			return true;
		}

		return rect.getSpan() <= 2;
	}

}
