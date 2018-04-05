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

package net.imagej.ops.image.normalize;

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.UFViaUCAllSame;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.converter.read.ConvertedIterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Normalizes an {@link IterableInterval} given its minimum and maximum to
 * another range defined by minimum and maximum.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Leon Yang
 * @param <T>
 */
@Plugin(type = Ops.Image.Normalize.class)
public class NormalizeIIFunction<T extends RealType<T>> extends
	UFViaUCAllSame<IterableInterval<T>> implements Ops.Image.Normalize
{

	@Parameter(required = false)
	private T sourceMin;

	@Parameter(required = false)
	private T sourceMax;

	@Parameter(required = false)
	private T targetMin;

	@Parameter(required = false)
	private T targetMax;

	@Parameter(required = false)
	private boolean isLazy = true;

	private UnaryFunctionOp<IterableInterval<T>, Pair<T, T>> minMaxFunc;

	private UnaryFunctionOp<IterableInterval<T>, IterableInterval<T>> imgCreator;

	@Override
	public UnaryComputerOp<IterableInterval<T>, IterableInterval<T>> createWorker(
		IterableInterval<T> t)
	{
		return Computers.unary(ops(), Ops.Image.Normalize.class, t, t, sourceMin,
			sourceMax, targetMin, targetMax);
	}

	@Override
	public IterableInterval<T> createOutput(IterableInterval<T> input) {
		return imgCreator.calculate(input);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		// If isLazy is false, the getBounds() will never be called, and the worker
		// is always used to do the work of compute(...)
		if (isLazy) {
			if (sourceMin == null || sourceMax == null) minMaxFunc =
				(UnaryFunctionOp) Functions.unary(ops(), Ops.Stats.MinMax.class,
					Pair.class, in());
		}
		else {
			super.initialize();
		}
		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			IterableInterval.class, in());
	}

	private double[] getBounds(final IterableInterval<T> input) {
		// the four elements are source min, source max, target min, and target max.
		final double[] result = new double[4];
		if (minMaxFunc != null) {
			final Pair<T, T> minMax = minMaxFunc.calculate(input);
			result[0] = (sourceMin == null ? minMax.getA() : sourceMin)
				.getRealDouble();
			result[1] = (sourceMax == null ? minMax.getB() : sourceMax)
				.getRealDouble();
		}
		else {
			result[0] = sourceMin.getRealDouble();
			result[1] = sourceMax.getRealDouble();
		}
		final T first = input.firstElement();
		result[2] = targetMin == null ? first.getMinValue() : targetMin
			.getRealDouble();
		result[3] = targetMax == null ? first.getMaxValue() : targetMax
			.getRealDouble();
		return result;
	}

	@Override
	public IterableInterval<T> calculate(final IterableInterval<T> input) {
		if (isLazy) {
			final double[] bounds = getBounds(input);
			return new ConvertedIterableInterval<>(input,
				new NormalizeRealTypeComputer<>(bounds[0], bounds[1], bounds[2],
					bounds[3]), input.firstElement().createVariable());
		}
		return super.calculate(input);
	}
}
