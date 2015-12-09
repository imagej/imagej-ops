/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops.filter.fft;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Forward FFT function that operates on RAI
 * 
 * @author Brian Northan
 * @param <T>
 * @param <I>
 */
@Plugin(type = Ops.Filter.FFT.class, priority = Priority.HIGH_PRIORITY)
public class FFTFunctionOp<T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
	extends AbstractFunctionOp<I, O> implements Ops.Filter.FFT
{

	/**
	 * The size of border to apply in each dimension
	 */
	@Parameter(required = false)
	private long[] borderSize = null;

	/**
	 * Whether to perform a fast FFT. If true the input will be extended to the
	 * next fast FFT size. If false the input will be computed using the original
	 * input dimensions (if possible). If the input dimensions are not supported
	 * by the underlying FFT implementation the input will be extended to the
	 * nearest size that is supported.
	 */
	@Parameter(required = false)
	private Boolean fast = true;

	/**
	 * The OutOfBoundsFactory used to extend the image
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf;

	/**
	 * The ImgFactory used to create the output
	 */
	@Parameter(required = false)
	private ImgFactory<?> factory;

	/**
	 * The complex type of the output
	 */
	@Parameter(required = false)
	private Type<C> fftType;
	
	private Dimensions paddedDimensions; 
	
	//@Override
	public O createOutput(final I input) {
		long[] inputWithBordersSize = new long[input.numDimensions()];

		for (int d = 0; d < input.numDimensions(); d++) {
			inputWithBordersSize[d] = input.dimension(d);

			if (borderSize != null) {
				inputWithBordersSize[d] += borderSize[d];
			}
		}

		long[] paddedSize = new long[input.numDimensions()];
		long[] fftSize = new long[input.numDimensions()];

		// calculate the required input (paddedSize) and complex output (fftSize)
		// sizes for the FFT
		ops().filter().fftSize(inputWithBordersSize, paddedSize, fftSize, true,
			fast);

		paddedDimensions = new FinalDimensions(paddedSize);
		Dimensions fftDimensions = new FinalDimensions(fftSize);

		// if no type was passed in the default is ComplexFloatType
		if (fftType == null) {
			fftType = (C) new ComplexFloatType();
		}

		// create the complex output image
		return (O) ops().create().img(fftDimensions, fftType, factory);
	}

	@Override
	@SuppressWarnings("unchecked")
	public O compute(final I input) {		

		O output=createOutput(input);

		I paddedInput;
				
		// pad the input if necessary
		if (!FFTMethods.dimensionsEqual(input, paddedDimensions)) {

			paddedInput = (I) ops().filter().padInput(input, paddedDimensions, obf);
		}
		else {
			paddedInput = input;
		}

		return (O)(ops().filter().fft(output, paddedInput));

	}

}
