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

package net.imagej.ops.filter.fft;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Function that performs the following steps, 1. create output, 2. pad input,
 * 3. perform forward FFT
 * 
 * @author Brian Northan
 */
@Plugin(type = Ops.Filter.FFT.class, priority = Priority.HIGH_PRIORITY)
public class DefaultFFTOp<I extends Dimensions, O extends Dimensions> extends
	AbstractUnaryFunctionOp<I, O> implements Ops.Filter.FFT
{

	/**
	 * Op used to pad the input
	 */
	@Parameter
	private BinaryFunctionOp<I, Dimensions, I> padOp;

	/**
	 * Op used to create the complex output
	 */
	@Parameter
	private UnaryFunctionOp<Dimensions, O> createOp;

	/**
	 * Op used to compute the fft
	 */
	@Parameter
	private UnaryComputerOp<I, O> fftOp;

	/**
	 * The size of border to apply in each dimension
	 */
	@Parameter(required = false)
	private long[] borderSize = null;

	@Override
	public O compute1(final I input) {

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
		O output = createOp.compute1(paddedDimensions);

		// pad the input
		I paddedInput = padOp.compute2(input, paddedDimensions);

		// compute and return fft
		fftOp.compute1(paddedInput, output);

		return output;
	}

}
