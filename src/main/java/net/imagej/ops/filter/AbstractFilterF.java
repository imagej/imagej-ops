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

package net.imagej.ops.filter;

import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for binary filter that performs operations using an image and
 * kernel
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractFilterF<I extends RealType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K>, C extends ComplexType<C> & NativeType<C>>
		extends
		AbstractBinaryFunctionOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> {

	/**
	 * Border size in each dimension. If null default border size will be
	 * calculated and added.
	 */
	@Parameter(required = false)
	private long[] borderSize = null;

	/**
	 * Defines the out of bounds strategy for the extended area of the input
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput;

	/**
	 * Defines the out of bounds strategy for the extended area of the kernel
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel;

	/**
	 * The output type. If null a default output type will be used.
	 */
	@Parameter(required = false)
	private Type<O> outType;

	/**
	 * Op used to pad the input
	 */
	private BinaryFunctionOp<RandomAccessibleInterval<I>, Dimensions, RandomAccessibleInterval<I>> padOp;

	public BinaryFunctionOp<RandomAccessibleInterval<I>, Dimensions, RandomAccessibleInterval<I>> getPadOp() {
		return padOp;
	}

	public void setPadOp(BinaryFunctionOp<RandomAccessibleInterval<I>, Dimensions, RandomAccessibleInterval<I>> padOp) {
		this.padOp = padOp;
	}

	public BinaryFunctionOp<RandomAccessibleInterval<K>, Dimensions, RandomAccessibleInterval<K>> getPadKernelOp() {
		return padKernelOp;
	}

	public void setPadKernelOp(
			BinaryFunctionOp<RandomAccessibleInterval<K>, Dimensions, RandomAccessibleInterval<K>> padKernelOp) {
		this.padKernelOp = padKernelOp;
	}

	/**
	 * Op used to pad the kernel
	 */
	private BinaryFunctionOp<RandomAccessibleInterval<K>, Dimensions, RandomAccessibleInterval<K>> padKernelOp;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		if (this.obfInput == null) {
			obfInput = new OutOfBoundsMirrorFactory<>(Boundary.SINGLE);
		}

	}

	/**
	 * Create the output using the outFactory and outType if they exist. If
	 * these are null use a default factory and type
	 */
	@SuppressWarnings("unchecked")
	public RandomAccessibleInterval<O> createOutput(RandomAccessibleInterval<I> input,
			RandomAccessibleInterval<K> kernel) {

		if (outType == null) {

			// if the input type and kernel type are the same use this type
			if (Util.getTypeFromInterval(input).getClass() == Util.getTypeFromInterval(kernel).getClass()) {
				Object temp = Util.getTypeFromInterval(input).createVariable();
				outType = (Type<O>) temp;

			}
			// otherwise default to float
			else {
				Object temp = new FloatType();
				outType = (Type<O>) temp;
			}
		}

		return ops().create().img(input, outType.createVariable());
	}

	/**
	 * compute output by extending the input(s) and running the filter
	 */
	@Override
	public RandomAccessibleInterval<O> calculate(final RandomAccessibleInterval<I> input,
			final RandomAccessibleInterval<K> kernel) {

		RandomAccessibleInterval<O> output = createOutput(input, kernel);

		final int numDimensions = input.numDimensions();

		// 1. Calculate desired extended size of the image

		final long[] paddedSize = new long[numDimensions];

		if (borderSize == null) {
			// if no border size was passed in, then extend based on kernel size
			for (int d = 0; d < numDimensions; ++d) {
				paddedSize[d] = (int) input.dimension(d) + (int) kernel.dimension(d) - 1;
			}

		} else {
			// if borderSize was passed in
			for (int d = 0; d < numDimensions; ++d) {

				paddedSize[d] = Math.max(kernel.dimension(d) + 2 * borderSize[d],
						input.dimension(d) + 2 * borderSize[d]);
			}
		}

		RandomAccessibleInterval<I> paddedInput = padOp.calculate(input, new FinalDimensions(paddedSize));

		RandomAccessibleInterval<K> paddedKernel = padKernelOp.calculate(kernel, new FinalDimensions(paddedSize));

		computeFilter(paddedInput, paddedKernel, output, paddedSize);

		return output;

	}

	abstract protected void computeFilter(final RandomAccessibleInterval<I> input,
			final RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> output, long[] paddedSize);

	protected long[] getBorderSize() {
		return borderSize;
	}

	protected OutOfBoundsFactory<I, RandomAccessibleInterval<I>> getOBFInput() {
		return obfInput;
	}

	protected void setOBFInput(OutOfBoundsFactory<I, RandomAccessibleInterval<I>> objInput) {
		this.obfInput = objInput;
	}

	protected OutOfBoundsFactory<K, RandomAccessibleInterval<K>> getOBFKernel() {
		return obfKernel;
	}

	protected void setOBFKernel(OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel) {
		this.obfKernel = obfKernel;
	}

	protected Type<O> getOutType() {
		return outType;
	}

}
