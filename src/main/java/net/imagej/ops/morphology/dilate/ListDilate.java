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

package net.imagej.ops.morphology.dilate;

import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.Maps;
import net.imagej.ops.morphology.Morphologies;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Computes the dilation of a {@link RandomAccessibleInterval} using a
 * {@link List} of {@link Shape}s. It is the caller's responsibility to provide
 * a {@link RandomAccessibleInterval} with enough padding for the output.
 * 
 * @author Leon Yang
 * @param <T> element type
 * @see net.imglib2.algorithm.morphology.Dilation
 */
@Plugin(type = Ops.Morphology.Dilate.class, priority = Priority.LOW)
public class ListDilate<T extends RealType<T>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<T>, List<Shape>, IterableInterval<T>>
	implements Ops.Morphology.Dilate, Contingent
{

	@Parameter(required = false)
	private boolean isFull;

	private T minVal;
	private UnaryFunctionOp<Interval, Img<T>> imgCreator;
	private UnaryComputerOp<IterableInterval<T>, IterableInterval<T>> copyImg;
	private BinaryComputerOp<RandomAccessibleInterval<T>, Shape, IterableInterval<T>> dilateComputer;

	@Override
	public boolean conforms() {
		return in1() == null || in2() == null || out() == null || //
			Maps.compatible(in1(), out());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		minVal = Util.getTypeFromInterval(in()).createVariable();
		minVal.setReal(minVal.getMinValue());

		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), minVal.createVariable());

		copyImg = (UnaryComputerOp) Computers.unary(ops(),
			Ops.Copy.IterableInterval.class, IterableInterval.class, Views.iterable(
				in1()));

		dilateComputer = (BinaryComputerOp) Computers.unary(ops(),
			Ops.Morphology.Dilate.class, IterableInterval.class, in1(), in2().get(0),
			false);
	}

	@Override
	public IterableInterval<T> createOutput(final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		if (isFull) {
			final long[][] minSize = Morphologies.computeMinSize(in1, in2);
			return imgCreator.calculate(new FinalInterval(minSize[1]));
		}
		return imgCreator.calculate(in1);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> in1,
		final List<Shape> in2, final IterableInterval<T> out)
	{
		final long[][] minSize = Morphologies.computeMinSize(in1, in2);
		final Interval interval = new FinalInterval(minSize[1]);
		Img<T> upstream = imgCreator.calculate(interval);
		Img<T> downstream = imgCreator.calculate(interval);
		Img<T> tmp;

		dilateComputer.compute(in1, in2.get(0), Views.translate(downstream,
			minSize[0]));
		for (int i = 1; i < in2.size(); i++) {
			// Ping-ponging intermediate results between upstream and downstream to
			// avoid repetitively creating new Imgs.
			tmp = downstream;
			downstream = upstream;
			upstream = tmp;
			dilateComputer.compute(upstream, in2.get(i), downstream);
		}
		if (isFull) copyImg.compute(downstream, out);
		else copyImg.compute(Views.interval(Views.translate(downstream,
			minSize[0]), out), out);
	}
}
