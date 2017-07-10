/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

package net.imagej.ops.convert.normalizeScale;

import net.imagej.ops.Ops;
import net.imagej.ops.convert.scale.ScaleRealTypes;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * Scales input values to their corresponding value in the output type range
 * based on the min/max values of an {@link IterableInterval} not the range of
 * the input type.
 *
 * @author Martin Horn (University of Konstanz)
 */
@Plugin(type = Ops.Convert.NormalizeScale.class)
public class NormalizeScaleRealTypes<I extends RealType<I>, O extends RealType<O>>
	extends ScaleRealTypes<I, O> implements Ops.Convert.NormalizeScale
{

	private UnaryFunctionOp<IterableInterval<I>, Pair<I, I>> minMaxFunc;

	protected double outMax;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		minMaxFunc = (UnaryFunctionOp) Functions.unary(ops(),
			Ops.Stats.MinMax.class, Pair.class, IterableInterval.class);
	}

	@Override
	public void checkInput(final I inType, final O outType) {
		outMin = outType.getMinValue();
		outMax = outType.getMaxValue();
	}

	@Override
	public void checkInput(final IterableInterval<I> in) {
		final Pair<I, I> minMax = minMaxFunc.calculate(in);
		factor = (minMax.getB().getRealDouble() - minMax.getA()
			.getRealDouble()) / (outMax - outMin);

		inMin = minMax.getA().getRealDouble();
	}

}
