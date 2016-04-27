/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.morphology.dilate;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.Maps;
import net.imagej.ops.map.neighborhood.MapNeighborhood;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.morphology.MorphologyUtils;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Computes the dilation of a {@link RandomAccessibleInterval} using a single
 * {@link Shape}. It is the caller's responsibility to provide a
 * {@link RandomAccessibleInterval} with enough padding for the output.
 * 
 * @author Leon Yang
 * @param <T> element type
 * @see net.imglib2.algorithm.morphology.Dilation
 */
@Plugin(type = Ops.Morphology.Dilate.class)
public class DefaultDilate<T extends RealType<T>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<T>, Shape, IterableInterval<T>>
	implements Ops.Morphology.Dilate, Contingent
{

	@Parameter(required = false)
	private boolean isFull;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> f;

	private T minVal;
	private MapNeighborhood<T, T, RandomAccessibleInterval<T>, IterableInterval<T>, UnaryComputerOp<Iterable<T>, T>> mapper;
	private UnaryFunctionOp<Dimensions, Img<T>> imgCreator;

	@Override
	public boolean conforms() {
		if (out() == null) return true;
		if (isFull) return createOutput(in()).iterationOrder().equals(out());
		return Maps.compatible(in(), out());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		minVal = Util.getTypeFromInterval(in()).createVariable();
		minVal.setReal(minVal.getMinValue());

		if (f == null) {
			f = new OutOfBoundsConstantValueFactory<>(
				minVal);
		}

		final UnaryComputerOp neighborComputer = minVal instanceof BitType
			? new DilateBitType() : Computers.unary(ops(), Ops.Stats.Max.class, minVal
				.createVariable(), Iterable.class);

		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), minVal.createVariable());

		if (out() == null) setOutput(createOutput(in()));

		mapper = ops().op(MapNeighborhood.class, out(), in1(), in2(),
			neighborComputer);
	}

	@Override
	public IterableInterval<T> createOutput(final RandomAccessibleInterval<T> in1,
		final Shape in2)
	{
		if (isFull) {
			final long[] dims = MorphologyUtils.computeTargetImageDimensionsAndOffset(
				in1, in2)[0];
			return imgCreator.compute1(new FinalInterval(dims));
		}
		return imgCreator.compute1(in1);
	}

	@Override
	public void compute2(final RandomAccessibleInterval<T> in1, final Shape in2,
		final IterableInterval<T> output)
	{
		final RandomAccessibleInterval<T> extended = RAIs.extend(in1, f);
		final RandomAccessibleInterval<T> shifted;
		if (isFull) {
			final long[] offset = MorphologyUtils
				.computeTargetImageDimensionsAndOffset(in1, in2)[1];
			shifted = Views.translate(extended, offset);
		}
		else {
			shifted = extended;
		}
		mapper.compute2(Views.interval(shifted, output), in2, output);
	}

	/**
	 * Helper op for computing the dilation of a {@link BitType} image.
	 */
	private static class DilateBitType extends
		AbstractUnaryComputerOp<Iterable<BitType>, BitType>
	{

		@Override
		public void compute1(final Iterable<BitType> input, final BitType output) {
			for (final BitType e : input)
				if (e.get()) {
					output.set(true);
					return;
				}
			output.set(false);
		}
	}
}
