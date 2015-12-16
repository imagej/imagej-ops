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

package net.imagej.ops.filter;

import org.scijava.plugin.Parameter;

import net.imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

/**
 * Abstract class for iterative FFT filters that perform on RAI. Boundary
 * conditions handled by extending the borders of the RAIs with an {@link OutOfBoundsFactory}.  
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class IterativeCirculantFFTFilterRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractIterativeFFTFilterRAI<I, O, K, C>
{

	/**
	 * An OutOfBoundsFactory which defines the extension strategy
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput;

	protected void initializeImages() {
		// if no output out of bounds factory exists create the obf for output
		if (getObfOutput() == null) {
			setObfOutput(
				new OutOfBoundsConstantValueFactory<O, RandomAccessibleInterval<O>>(Util
					.getTypeFromInterval(out()).createVariable()));
		}

		Type<O> outType = Util.getTypeFromInterval(out());

		// create image for the reblurred
		Img<O> reblurred = getImgFactory().create(out(), outType.createVariable());

		// extend the output and use it as a buffer to store the estimate
		setRAIExtendedEstimate(Views.interval(Views.extend(out(), getObfOutput()),
			getImgConvolutionInterval()));

		// assemble the extended view of the reblurred
		setRAIExtendedReblurred(Views.interval(Views.extend(reblurred,
			getObfOutput()), getImgConvolutionInterval()));

		// set first guess of estimate
		// TODO: implement logic for various first guesses.
		// for now just set to original image
		Cursor<O> c = Views.iterable(getRAIExtendedEstimate()).cursor();
		Cursor<I> cIn = Views.iterable(in()).cursor();

		while (c.hasNext()) {
			c.fwd();
			cIn.fwd();
			c.get().setReal(cIn.get().getRealFloat());
		}

		// perform fft of input
		ops().filter().fft(getFFTInput(), in());

		// perform fft of psf
		ops().filter().fft(getFFTKernel(), getRAIExtendedKernel());
	}

	public OutOfBoundsFactory<O, RandomAccessibleInterval<O>> getObfOutput() {
		return obfOutput;
	}

	public void setObfOutput(
		OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput)
	{
		this.obfOutput = obfOutput;
	}

}
