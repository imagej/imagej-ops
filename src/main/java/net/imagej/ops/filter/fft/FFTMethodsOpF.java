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

package net.imagej.ops.filter.fft;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.pad.PadInputFFTMethods;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Function that uses FFTMethods to perform a forward FFT
 * 
 * @author Brian Northan
 * @param <T> TODO Documentation
 * @param <C> TODO Documentation
 */
@Plugin(type = Ops.Filter.FFT.class, priority = Priority.HIGH)
public class FFTMethodsOpF<T extends RealType<T>, C extends ComplexType<C>>
	extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<C>>
	implements Ops.Filter.FFT
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
	private boolean fast = true;

	/**
	 * The OutOfBoundsFactory used to extend the image
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf;

	/**
	 * The complex type of the output
	 */
	@Parameter(required = false)
	private Type<C> fftType;

	private BinaryFunctionOp<RandomAccessibleInterval<T>, Dimensions, RandomAccessibleInterval<T>> padOp;

	private UnaryFunctionOp<Dimensions, RandomAccessibleInterval<C>> createOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<C>> fftMethodsOp;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		// if no type was passed in the default is ComplexFloatType
		if (fftType == null) {
			fftType = (C)ops().create().nativeType(ComplexFloatType.class);
		}

		padOp = (BinaryFunctionOp) Functions.binary(ops(), PadInputFFTMethods.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			Dimensions.class, fast);

		createOp = (UnaryFunctionOp) Functions.unary(ops(),
			CreateOutputFFTMethods.class, RandomAccessibleInterval.class,
			Dimensions.class, fftType, fast);

		fftMethodsOp = (UnaryComputerOp) Computers.nullary(ops(),
			FFTMethodsOpC.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class);

	}

	@Override
	public RandomAccessibleInterval<C> calculate(
		final RandomAccessibleInterval<T> input)
	{
		// calculate the padded size
		long[] paddedSize = new long[in().numDimensions()];

		for (int d = 0; d < in().numDimensions(); d++) {
			paddedSize[d] = in().dimension(d);

			if (borderSize != null) {
				paddedSize[d] += borderSize[d];
			}
		}

		Dimensions paddedDimensions = new FinalDimensions(paddedSize);

		// create the complex output
		RandomAccessibleInterval<C> output = createOp.calculate(paddedDimensions);

		// pad the input
		RandomAccessibleInterval<T> paddedInput = padOp.calculate(input,
			paddedDimensions);

		// compute and return fft
		fftMethodsOp.compute(paddedInput, output);

		return output;

	}

}
