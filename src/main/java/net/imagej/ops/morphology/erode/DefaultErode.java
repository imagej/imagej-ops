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

package net.imagej.ops.morphology.erode;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.Maps;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.morphology.Erosion;
import net.imglib2.algorithm.morphology.MorphologyUtils;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Computes the erosion of a {@link RandomAccessibleInterval} using a single
 * {@link Shape}.
 * 
 * @author Leon Yang
 * @param <T> element type
 * @see net.imglib2.algorithm.morphology.Erosion
 */
@Plugin(type = Ops.Morphology.Erode.class)
public class DefaultErode<T extends RealType<T>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<T>, Shape, IterableInterval<T>>
	implements Ops.Morphology.Erode, Contingent
{

	@Parameter(required = false)
	private boolean isFull;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> f;

	private T maxVal;
	private UnaryFunctionOp<Dimensions, Img<T>> imgCreator;

	@Override
	public boolean conforms() {
		if (in1() == null || in2() == null || out() == null) return true;
		return isFull || Maps.compatible(in(), out());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		maxVal = Util.getTypeFromInterval(in()).createVariable();
		maxVal.setReal(maxVal.getMaxValue());

		if (f == null) {
			f = new OutOfBoundsConstantValueFactory<>(maxVal);
		}

		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), maxVal.createVariable());

		if (out() == null) setOutput(createOutput(in()));
	}

	@Override
	public IterableInterval<T> createOutput(final RandomAccessibleInterval<T> in1,
		final Shape in2)
	{
		if (isFull) {
			final long[] dims = MorphologyUtils.computeTargetImageDimensionsAndOffset(
				in1, in2)[0];
			return imgCreator.calculate(new FinalInterval(dims));
		}
		return imgCreator.calculate(in1);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> in1, final Shape in2,
		final IterableInterval<T> output)
	{
		final RandomAccessibleInterval<T> shifted;
		if (isFull) {
			final long[] offset = MorphologyUtils
				.computeTargetImageDimensionsAndOffset(in1, in2)[1];
			shifted = Views.translate(in1, offset);
		}
		else {
			shifted = in1;
		}
		final ExtendedRandomAccessibleInterval<T, RandomAccessibleInterval<T>> extended =
			Views.extend(shifted, f);
		Erosion.erode(extended, output, in2, maxVal, Runtime.getRuntime()
			.availableProcessors());
	}
}
