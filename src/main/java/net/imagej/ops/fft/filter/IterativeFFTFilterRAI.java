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

package net.imagej.ops.fft.filter;

import org.scijava.plugin.Parameter;

import net.imagej.ops.OpService;
import net.imagej.ops.convolve.ConvolveFFTRAI;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

/**
 * Abstract class for iterative FFT filters that perform on RAI
 * 
 * @author bnorthan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class IterativeFFTFilterRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFFTFilterRAI<I, O, K, C>
{

	@Parameter
	protected OpService ops;

	/**
	 * Max number of iterations to perform
	 */
	@Parameter
	protected int maxIterations;

	@Parameter
	protected Interval imgConvolutionInterval;

	@Parameter
	protected ImgFactory<O> imgFactory;

	@Parameter(required = false)
	protected OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput;

	protected RandomAccessibleInterval<O> raiExtendedReblurred;

	protected RandomAccessibleInterval<O> raiExtendedEstimate;

	protected Img<C> fftRoving;

	protected Img<O> reblurred;

	@Override
	public void run() {

		initialize();

		performIterations(maxIterations);

	}

	/**
	 * initialize TODO: review this function
	 */
	protected void initialize() {

		// if no output out of bounds factory exists create it
		if (obfOutput == null) {
			obfOutput =
				new OutOfBoundsConstantValueFactory<O, RandomAccessibleInterval<O>>(
					Util.getTypeFromInterval(output).createVariable());
		}

		Type<O> outType = Util.getTypeFromInterval(output);

		// create image for the reblurred
		reblurred = imgFactory.create(output, outType.createVariable());

		// TODO: review this step
		// extend the output and use it as a buffer to store the estimate
		raiExtendedEstimate =
			Views.interval(Views.extend(output, obfOutput), imgConvolutionInterval);

		// assemble the extended view of the reblurred
		raiExtendedReblurred =
			Views
				.interval(Views.extend(reblurred, obfOutput), imgConvolutionInterval);

		// perform fft of input
		ops.run("fft", fftInput, raiExtendedInput);

		// perform fft of psf
		ops.run("fft", fftKernel, raiExtendedKernel);

		// set first guess of estimate
		// TODO: implement logic for various first guesses.
		// for now just set to original image
		Cursor<O> c = Views.iterable(raiExtendedEstimate).cursor();
		Cursor<I> cIn = Views.iterable(raiExtendedInput).cursor();

		while (c.hasNext()) {
			c.fwd();
			cIn.fwd();
			c.get().setReal(cIn.get().getRealFloat());
		}

		createReblurred();

		// TODO: code for edge handling scheme
	}

	void performIterations(int maxIterations) {
		for (int i = 0; i < maxIterations; i++) {
			performIteration();
			createReblurred();
		}
	}

	protected void createReblurred() {
		// perform convolution -- kernel FFT should allready exist
		ops.run(ConvolveFFTRAI.class, raiExtendedEstimate, null,
				fftInput, fftKernel, raiExtendedReblurred, true, false);
	}

	abstract protected void performIteration();

}
