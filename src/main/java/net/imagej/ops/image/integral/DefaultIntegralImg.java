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

package net.imagej.ops.image.integral;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Slicewise;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCI;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;

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
@Plugin(type = Ops.Image.Integral.class, priority = Priority.LOW_PRIORITY+1)
public class DefaultIntegralImg<I extends RealType<I>> extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<I>, RandomAccessibleInterval<RealType<?>>>
	implements Ops.Image.Integral, Contingent
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


	@Override
	public void compute1(RandomAccessibleInterval<I> input,
		RandomAccessibleInterval<RealType<?>> output)
	{
		if (slicewiseOps == null) {
			slicewiseOps = new UnaryComputerOp[in().numDimensions()];

			for (int i = 0; i < in().numDimensions(); ++i) {
				slicewiseOps[i] = Computers.unary(ops(), Slicewise.class,
					RandomAccessibleInterval.class, RandomAccessibleInterval.class,
					integralAdd, i);
			}
		}
		
		RandomAccessibleInterval<? extends RealType<?>> generalizedInput = input; // FIXME
		
		// Create integral image
		for (int i=0; i < input.numDimensions(); ++i) {
			// Slicewise integral addition in one direction
			slicewiseOps[i].compute1(generalizedInput, output);
			generalizedInput = output;
		}
	
	}

	@Override
	public RandomAccessibleInterval<RealType<?>> createOutput(
		RandomAccessibleInterval<I> input)
	{
		// Create integral image
		if (Util.getTypeFromInterval(input) instanceof IntegerType) {
			return (RandomAccessibleInterval) ops().create().img(input, new LongType());
		}
		
		return (RandomAccessibleInterval) ops().create().img(input, new DoubleType());
	}

	/**
	 * Implements the row-wise addition required for computations of integral
	 * images. Uses the {@code order} provided to the surrounding class.
	 * 
	 * @author Stefan Helfrich (University of Konstanz)
	 */
	private class IntegralAddComputer extends
		AbstractUnaryHybridCI<IterableInterval<RealType<?>>>
	{

		@Override
		public void compute1(final IterableInterval<RealType<?>> input,
			final IterableInterval<RealType<?>> output)
		{

			Cursor<RealType<?>> inputCursor = input.cursor();
			Cursor<RealType<?>> outputCursor = output.cursor();

			double tmp = 0.0d;
			while (outputCursor.hasNext()) {

				RealType<?> inputValue = inputCursor.next();
				RealType<?> outputValue = outputCursor.next();
				
				// Compute inputValue^order
				tmp += Math.pow(inputValue.getRealDouble(), order);
				
				outputValue.setReal(tmp);
			}
		}

	}

	@Override
	public boolean conforms() {
		// TODO Check for size matches instead of dimension
		return in().numDimensions() == out().numDimensions();
	}

}
