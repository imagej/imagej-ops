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

package net.imagej.ops.create.integralImg;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Slicewise;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCI;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

/**
 * <p>
 * <i>n</i>-dimensional integral image that stores sums using
 * {@code DoubleType}. Care must be taken that sums do not overflow the
 * capacity of {@code DoubleType}.
 * </p>
 * <p>
 * The integral image will be one pixel larger in each dimension as for easy
 * computation of sums it has to contain "zeros" at the beginning of each
 * dimension
 * </p>
 * 
 * @param <I> The type of the input image.
 * @author Stefan Helfrich (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
@Plugin(type = Ops.Create.IntegralImg.class, priority = Priority.LOW_PRIORITY+1)
public class DefaultCreateIntegralImg<I extends RealType<I>> extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<DoubleType>>
	implements Ops.Create.IntegralImg
{

	@Parameter(required = false)
	private int order = 1;
	
	private IntegralAddComputer integralAdd;
	private UnaryComputerOp[] slicewiseOps;
	
	@Override
	public void initialize() {
		integralAdd = new IntegralAddComputer();
		
		if (in() != null) {
			slicewiseOps = new UnaryComputerOp[in().numDimensions()];

			for (int i = 0; i < in().numDimensions(); ++i) {
				slicewiseOps[i] = Computers.unary(ops(), Slicewise.class,
					RandomAccessibleInterval.class, RandomAccessibleInterval.class,
					integralAdd, i);
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public RandomAccessibleInterval<DoubleType> compute1(
		final RandomAccessibleInterval<I> input)
	{
		if (slicewiseOps == null) {
			slicewiseOps = new UnaryComputerOp[in().numDimensions()];
			
			for (int i = 0; i < in().numDimensions(); ++i) {
				slicewiseOps[i] = Computers.unary(ops(), Slicewise.class, RandomAccessibleInterval.class, RandomAccessibleInterval.class, integralAdd, i);
			}
		}
		
		// Extend input in each dimension and fill with zeros
		RandomAccessibleInterval<? extends RealType> extendedInput = Views
			.zeroMin(Views.interval(Views.extendZero(input), extendInterval(input)));

		// Create integral image
		RandomAccessibleInterval<DoubleType> output = Views.zeroMin(ops().create()
			.img(extendInterval(input), new DoubleType()));

		for (int i=0; i < input.numDimensions(); ++i) {
			// Slicewise integral addition in one direction
			slicewiseOps[i].compute1(extendedInput, output);
			extendedInput = output;
		}

		return output;
	}

	/**
	 * Extend an interval by one in each dimension (only the minimum)
	 * 
	 * @param interval {@code Interval} that is to be extended and later converted
	 *          to an integral image
	 * @return {@code Interval} extended by one in each dimension
	 */
	private Interval extendInterval(Interval interval) {
		final long[] imgMinimum = new long[interval.numDimensions()];
		interval.min(imgMinimum);
		final long[] imgMaximum = new long[interval.numDimensions()];
		interval.max(imgMaximum);

		for (int d = 0; d < interval.numDimensions(); d++) {
			imgMinimum[d] = imgMinimum[d] - 1;
		}

		return new FinalInterval(imgMinimum, imgMaximum);
	}
	
	/**
	 * Implements the row-wise addition required for computations of integral
	 * images. Uses the {@code order} provided to the surrounding class.
	 * 
	 * @author Stefan Helfrich (University of Konstanz)
	 * @param <T>
	 */
	private class IntegralAddComputer<T extends RealType<T>> extends
		AbstractUnaryHybridCI<IterableInterval<T>>
	{

		@Override
		public void compute1(final IterableInterval<T> input,
			final IterableInterval<T> output)
		{
			// TODO Input should just be one-dimensional (check!)

			Cursor<T> inputCursor = input.cursor();
			Cursor<T> outputCursor = output.cursor();

			T previousOutputValue = null;

			while (outputCursor.hasNext()) {
				// TODO Second cursor which is one position behind

				T inputValue = inputCursor.next();
				T outputValue = outputCursor.next();

				if (previousOutputValue == null) {
					// TODO Test speed of copy vs. 2nd cursor
					previousOutputValue = outputValue.copy();
					continue;
				}

				// Compute inputValue^order
				for (int ord = 1; ord < order; ++ord) {
					inputValue.mul(inputValue);
				}

				previousOutputValue.add(inputValue);

				outputValue.set(previousOutputValue.copy());
			}
		}

	}

}
