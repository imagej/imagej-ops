/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.convolve;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.util.Intervals;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Convolves an image by transforming the kernel and the image into fourier
 * space, multiplying them and transforming the result back.
 */
@Plugin(type = Op.class, name = Convolve.NAME)
public class ConvolveFourier<I extends RealType<I>, K extends RealType<K>, O extends RealType<O>>
	extends
	AbstractFunction<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
	implements Contingent, Convolve
{

	@Parameter
	private RandomAccessibleInterval<K> kernel;

	// keep the last used image to avoid a newly fourier transformation if the
	// images has not been changed (only the kernel)
	private RandomAccessibleInterval<I> last = null;

	private FFTConvolution<I, K, O> fc = null;

	@Override
	public RandomAccessibleInterval<O> compute(RandomAccessibleInterval<I> input,
		RandomAccessibleInterval<O> output)
	{
		if (input.numDimensions() != kernel.numDimensions()) {
			throw new IllegalArgumentException(
				"Kernel dimensions do not match to Img dimensions!");
		}

		if (last != input) {
			last = input;
			fc =
				FFTConvolution.create(last, kernel, output,
					new ArrayImgFactory<ComplexFloatType>());
			fc.setKernel(kernel);
			fc.setKeepImgFFT(true);
		}
		else {
			fc.setKernel(kernel);
			fc.setOutput(output);
		}

		fc.run();
		return output;
	}

	@Override
	public boolean conforms() {
		// TODO: only conforms if the kernel is sufficiently large (else the
		// naive approach should be used) -> what is a good heuristic??
		return Intervals.numElements(kernel) > 9;
	}

}
