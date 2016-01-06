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

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.create.img.DefaultCreateImg;
import net.imagej.ops.special.AbstractUnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;

/**
 * Default implementation of the "create.img" op.
 *
 * @author Daniel Seebacher (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Create.Img.class)
public class CreateOutputFFTMethods<T> extends
	AbstractUnaryFunctionOp<Dimensions, Img<T>> implements Ops.Create.Img
{

	@Parameter(required = false)
	private Boolean fast = true;

	@Parameter(required = false)
	private T outType;

	@Parameter(required = false)
	private ImgFactory<T> fac;

	@SuppressWarnings("unchecked")
	@Override
	public Img<T> compute1(Dimensions paddedDimensions) {

		long[] paddedSize = new long[paddedDimensions.numDimensions()];
		long[] fftSize = new long[paddedDimensions.numDimensions()];

		if (fast) {
			FFTMethods.dimensionsRealToComplexFast(paddedDimensions, paddedSize,
				fftSize);
		}
		else {
			FFTMethods.dimensionsRealToComplexSmall(paddedDimensions, paddedSize,
				fftSize);
		}

		Dimensions paddedFFTMethodsFFTDimensions = new FinalDimensions(fftSize);

		return (Img<T>) ops().run(DefaultCreateImg.class,
			paddedFFTMethodsFFTDimensions, outType, fac);
	}

}
