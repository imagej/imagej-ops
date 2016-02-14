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

package net.imagej.ops.filter;

import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for binary filter that performs operation using an image and
 * kernel
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>DefaultCreateImg
 * @param <K>
 */
public abstract class AbstractFilterF<I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
	extends
	AbstractBinaryFunctionOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>>
{

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
	 * Factory to create output Img. If null a default output factory will be used
	 */
	@Parameter(required = false)
	private ImgFactory<O> outFactory;

	/**
	 * protected RandomAccessibleInterval<K> getKernel() { return kernel; } Create
	 * the output using the outFactory and outType if they exist. If these are
	 * null use a default factory and type
	 */
	@SuppressWarnings("unchecked")
	public RandomAccessibleInterval<O> createOutput(
		RandomAccessibleInterval<I> input, RandomAccessibleInterval<K> kernel)
	{

		if (outType == null) {

			// if the input type and kernel type are the same use this type
			if (Util.getTypeFromInterval(input).getClass() == Util
				.getTypeFromInterval(kernel).getClass())
			{
				Object temp = Util.getTypeFromInterval(input).createVariable();
				outType = (Type<O>) temp;

			}
			// otherwise default to float
			else {
				Object temp = new FloatType();
				outType = (Type<O>) temp;
			}
		}

		if (outFactory == null) {
			outFactory = (ImgFactory<O>) (ops().create().imgFactory(input));
		}

		return ops().create().img(input, outType.createVariable(), outFactory);
	}

	protected long[] getBorderSize() {
		return borderSize;
	}

	protected OutOfBoundsFactory<I, RandomAccessibleInterval<I>> getOBFInput() {
		return obfInput;
	}

	protected void setOBFInput(
		OutOfBoundsFactory<I, RandomAccessibleInterval<I>> objInput)
	{
		this.obfInput = objInput;
	}

	protected OutOfBoundsFactory<K, RandomAccessibleInterval<K>> getOBFKernel() {
		return obfKernel;
	}

	protected void setOBFKernel(
		OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel)
	{
		this.obfKernel = obfKernel;
	}

	protected ImgFactory<O> getOutFactory() {
		return outFactory;
	}

}