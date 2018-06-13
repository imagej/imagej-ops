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

package net.imagej.ops.deconvolve;

import java.util.ArrayList;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractIterativeFFTFilterC;
import net.imagej.ops.filter.convolve.ConvolveFFTC;
import net.imagej.ops.filter.fft.FFTMethodsOpC;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Richardson Lucy algorithm for (@link RandomAccessibleInterval) (Lucy, L. B.
 * (1974). "An iterative technique for the rectification of observed
 * distributions".)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */

@Plugin(type = Ops.Deconvolve.RichardsonLucy.class, priority = Priority.HIGH)
public class RichardsonLucyC<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractIterativeFFTFilterC<I, O, K, C> implements
	Ops.Deconvolve.RichardsonLucy
{

	/**
	 * Op that computes Richardson Lucy update, can be overridden to implement
	 * variations of the algorithm (like RichardsonLucyTV)
	 */
	@Parameter(required = false)
	private UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> updateOp =
		null;

	/**
	 * The current estimate, by passing in the current estimate the user can
	 * define the starting point (first guess), if no starting estimate is
	 * provided the default starting point will be the input image
	 */
	@Parameter(required = false)
	private RandomAccessibleInterval<O> raiExtendedEstimate;

	/**
	 * A list of optional constraints that are applied at the end of each
	 * iteration (ie can be used to achieve noise removal, non-circulant
	 * normalization, etc.)
	 */
	@Parameter(required = false)
	private ArrayList<UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>> iterativePostProcessingOps =
		null;

	private BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> rlCorrectionOp;

	private UnaryFunctionOp<Interval, Img<O>> createOp;

	private UnaryComputerOp<RandomAccessibleInterval<K>, RandomAccessibleInterval<C>> fftKernelOp;

	private BinaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> convolverOp;

	private UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> copyOp;

	private UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> copy2Op;

	private RandomAccessibleInterval<O> raiExtendedReblurred;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		if (updateOp == null) {
			updateOp = (UnaryComputerOp) Computers.unary(ops(),
				RichardsonLucyUpdate.class, RandomAccessibleInterval.class,
				RandomAccessibleInterval.class);
		}

		rlCorrectionOp = (BinaryComputerOp) Computers.binary(ops(),
			RichardsonLucyCorrection.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			getFFTInput(), getFFTKernel());

		fftKernelOp = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsOpC.class,
			getFFTKernel(), RandomAccessibleInterval.class);

		copyOp = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Copy.RAI.class,
			RandomAccessibleInterval.class, IntervalView.class);

		copy2Op = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Copy.RAI.class,
			RandomAccessibleInterval.class, IntervalView.class);

		createOp = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, Dimensions.class, Util.getTypeFromInterval(out()));

		convolverOp = (BinaryComputerOp) Computers.binary(ops(), ConvolveFFTC.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, this.getFFTInput(), this.getFFTKernel(),
			true, false);

	}

	@Override
	public void compute(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{
		// create FFT input memory if needed
		if (getFFTInput() == null) {
			setFFTInput(getCreateOp().calculate(in));
		}

		// create FFT kernel memory if needed
		if (getFFTKernel() == null) {
			setFFTKernel(getCreateOp().calculate(in));
		}

		// if a starting point for the estimate was not passed in then create
		// estimate Img and use the input as the starting point
		if (raiExtendedEstimate == null) {

			raiExtendedEstimate = createOp.calculate(in);

			copyOp.compute(in, raiExtendedEstimate);
		}

		// create image for the reblurred
		raiExtendedReblurred = createOp.calculate(in);

		// perform fft of psf
		fftKernelOp.compute(kernel, getFFTKernel());

		// -- perform iterations --

		for (int i = 0; i < getMaxIterations(); i++) {

			if (status != null) {
				status.showProgress(i, getMaxIterations());
			}

			// create reblurred by convolving kernel with estimate
			// NOTE: the FFT of the PSF of the kernel has been passed in as a
			// parameter. when the op was set up, and computed above, so we can use
			// compute
			convolverOp.compute(raiExtendedEstimate, this.raiExtendedReblurred);

			// compute correction factor
			rlCorrectionOp.compute(in, raiExtendedReblurred, raiExtendedReblurred);

			// perform update to calculate new estimate
			updateOp.compute(raiExtendedReblurred, raiExtendedEstimate);

			// apply post processing
			if (iterativePostProcessingOps != null) {
				for (UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> pp : iterativePostProcessingOps) {
					pp.mutate(raiExtendedEstimate);
				}
			}

			// accelerate the algorithm by taking a larger step
			if (getAccelerator() != null) {
				getAccelerator().mutate(raiExtendedEstimate);
			}
		}

		// -- copy crop padded back to original size

		final long[] start = new long[out.numDimensions()];
		final long[] end = new long[out.numDimensions()];

		for (int d = 0; d < out.numDimensions(); d++) {
			start[d] = 0;
			end[d] = start[d] + out.dimension(d) - 1;
		}

		copy2Op.compute(Views.interval(raiExtendedEstimate, new FinalInterval(start,
			end)), out);
	}

}
