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

package net.imagej.ops.image.integral;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Slice;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCI;
import net.imglib2.Dimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

/**
 * Abstract base class for <i>n</i>-dimensional integral images.
 *
 * @param <I> The type of the input image.
 * @author Stefan Helfrich (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractIntegralImg<I extends RealType<I>> extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<I>, RandomAccessibleInterval<RealType<?>>>
	implements Contingent
{

	private UnaryComputerOp[] slicewiseOps;
	private UnaryFunctionOp<Dimensions, RandomAccessibleInterval> createLongRAI;
	private UnaryFunctionOp<Dimensions, RandomAccessibleInterval> createDoubleRAI;

	@Override
	public void initialize() {
		if (in() != null) {
			slicewiseOps = new UnaryComputerOp[in().numDimensions()];

			for (int i = 0; i < in().numDimensions(); ++i) {
				slicewiseOps[i] = Computers.unary(ops(), Slice.class,
					RandomAccessibleInterval.class, RandomAccessibleInterval.class,
					getComputer(i), i);
			}
		}

		createLongRAI = Functions.unary(ops(), Ops.Create.Img.class,
			RandomAccessibleInterval.class, Dimensions.class, new LongType());
		createDoubleRAI = Functions.unary(ops(), Ops.Create.Img.class,
			RandomAccessibleInterval.class, Dimensions.class, new DoubleType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(final RandomAccessibleInterval<I> input,
		final RandomAccessibleInterval<RealType<?>> output)
	{
		// TODO Should become obsolete (duplication of initialize())
		if (slicewiseOps == null) {
			slicewiseOps = new UnaryComputerOp[in().numDimensions()];

			for (int i = 0; i < in().numDimensions(); ++i) {
				slicewiseOps[i] = Computers.unary(ops(), Slice.class,
					RandomAccessibleInterval.class, RandomAccessibleInterval.class,
					getComputer(i), i);
			}
		}

		// HACK Generalized to most common supertype of input and output
		RandomAccessibleInterval<? extends RealType<?>> generalizedInput = input;

		// Create integral image
		for (int i = 0; i < input.numDimensions(); ++i) {
			// Slicewise integral addition in one direction
			slicewiseOps[i].compute(generalizedInput, output);
			generalizedInput = output;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public RandomAccessibleInterval<RealType<?>> createOutput(
		final RandomAccessibleInterval<I> input)
	{
		// Create integral image
		if (Util.getTypeFromInterval(input) instanceof IntegerType) {
			return createLongRAI.calculate(input);
		}

		return createDoubleRAI.calculate(input);
	}

	@Override
	public boolean conforms() {
		return (in() != null && out() != null) ? Views.iterable(in())
			.iterationOrder().equals(Views.iterable(out())) : true;
	}

	/**
	 * Implements the row-wise addition required for computations of integral
	 * images.
	 */
	public abstract
		AbstractUnaryHybridCI<IterableInterval<I>, IterableInterval<I>> getComputer(
			int dimension);

}
