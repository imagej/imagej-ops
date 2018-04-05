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

package net.imagej.ops.morphology.close;

import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.Maps;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imagej.ops.special.hybrid.BinaryHybridCF;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

/**
 * Computes the closing of a {@link RandomAccessibleInterval} using a
 * {@link List} of {@link Shape}s. It is the caller's responsibility to provide
 * a {@link RandomAccessibleInterval} with enough padding for the output.
 * 
 * @author Leon Yang
 * @param <T> element type
 * @see net.imglib2.algorithm.morphology.Closing
 */
@Plugin(type = Ops.Morphology.Close.class)
public class ListClose<T extends RealType<T>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<T>, List<Shape>, IterableInterval<T>>
	implements Ops.Morphology.Close, Contingent
{

	private T maxVal;

	private UnaryFunctionOp<Interval, Img<T>> imgCreator;
	private BinaryHybridCF<RandomAccessibleInterval<T>, List<Shape>, IterableInterval<T>> dilateComputer;
	private BinaryHybridCF<RandomAccessibleInterval<T>, List<Shape>, IterableInterval<T>> erodeComputer;

	@Override
	public boolean conforms() {
		return in1() == null || in2() == null || out() == null || //
			Maps.compatible(in1(), out());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		maxVal = Util.getTypeFromInterval(in()).createVariable();
		maxVal.setReal(maxVal.getMaxValue());

		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), maxVal.createVariable());

		dilateComputer = Hybrids.binaryCF(ops(), Ops.Morphology.Dilate.class, out(),
			in1(), in2(), false);

		erodeComputer = Hybrids.binaryCF(ops(), Ops.Morphology.Erode.class, out(),
			in1(), in2(), false);
	}

	@Override
	public IterableInterval<T> createOutput(final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		return erodeComputer.createOutput(in1, in2);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> in1,
		final List<Shape> in2, final IterableInterval<T> out)
	{
		final Img<T> buffer = imgCreator.calculate(out);
		dilateComputer.compute(in1, in2, buffer);
		erodeComputer.compute(Views.interval(Views.extendValue(buffer, maxVal),
			out), in2, out);
	}
}
