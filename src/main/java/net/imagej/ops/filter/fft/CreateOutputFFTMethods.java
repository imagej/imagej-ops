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
import net.imagej.ops.create.img.DefaultCreateImg;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Function that creates an output for FFTMethods FFT
 *
 * @author Brian Northan
 * @param <T>
 */
@Plugin(type = Ops.Filter.CreateFFTOutput.class)
public class CreateOutputFFTMethods<T> extends
	AbstractBinaryFunctionOp<Dimensions, T, Img<T>> implements
	Ops.Filter.CreateFFTOutput
{

	@Parameter(required = false)
	private Boolean fast = true;

	@Parameter(required = false)
	private ImgFactory<T> fac;

	@SuppressWarnings("unchecked")
	@Override
	public Img<T> compute2(Dimensions paddedDimensions, T outType) {

		Dimensions paddedFFTMethodsFFTDimensions = FFTMethodsUtility
			.getFFTDimensionsRealToComplex(fast, paddedDimensions);

		return (Img<T>) ops().run(DefaultCreateImg.class,
			paddedFFTMethodsFFTDimensions, outType, fac);
	}

}
