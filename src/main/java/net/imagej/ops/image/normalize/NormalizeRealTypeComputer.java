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

package net.imagej.ops.image.normalize;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.OpEnvironment;
import net.imglib2.IterableInterval;
import net.imglib2.converter.Converter;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

/**
 * Simple {@link ComputerOp} and {@link Converter} to perform a normalization.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
class NormalizeRealTypeComputer<T extends RealType<T>> extends
	AbstractComputerOp<T, T> implements Converter<T, T>
{

	private double targetMin, targetMax, sourceMin, factor;

	public NormalizeRealTypeComputer(final OpEnvironment ops, final T sourceMin,
		final T sourceMax, final T targetMin, final T targetMax,
		final IterableInterval<T> input)
	{
		double tmp = 0.0;
		
		if (sourceMin != null && sourceMax != null) {
			this.sourceMin = sourceMin.getRealDouble();
			tmp = sourceMax.getRealDouble();
		} else {
			final Pair<T,T> minMax = ops.stats().minMax(input);
			if (sourceMin == null) {
				this.sourceMin = minMax.getA().getRealDouble();
			}
			else {
				this.sourceMin = sourceMin.getRealDouble();
			}
			if (sourceMax == null) {
				tmp = minMax.getB().getRealDouble();
			}
			else {
				tmp = sourceMax.getRealDouble();
			}
		}

		if (targetMax == null) {
			this.targetMax = input.firstElement().getMaxValue();
		}
		else {
			this.targetMax = targetMax.getRealDouble();
		}

		if (targetMin == null) {
			this.targetMin = input.firstElement().getMinValue();
		}
		else {
			this.targetMin = targetMin.getRealDouble();
		}

		this.factor =
			1.0d / (tmp - this.sourceMin) * (this.targetMax - this.targetMin);
	}

	@Override
	public void compute(final T input, final T output) {

		final double res = (input.getRealDouble() - sourceMin) * factor + targetMin;

		if (res > targetMax) {
			output.setReal(targetMax);
		}
		else if (res < targetMin) {
			output.setReal(targetMin);
		}
		else {
			output.setReal(res);
		}
	}

	@Override
	public void convert(T input, T output) {
		compute(input, output);
	}
}
