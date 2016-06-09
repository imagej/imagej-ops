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
import org.scijava.app.StatusService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Richardson Lucy algorithm for (@link RandomAccessibleInterval) (Lucy, L. B.
 * (1974).
 * "An iterative technique for the rectification of observed distributions".)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */

@Plugin(type = Ops.Deconvolve.RichardsonLucy.class,
	priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyC<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractIterativeFFTFilterC<I, O, K, C> implements
	Ops.Deconvolve.RichardsonLucy
{

	@Parameter(required = false)
	private StatusService status;

	/**
	 * Op that computes Richardson Lucy update, can be overridden to implement
	 * variations of the algorithm (like RichardsonLucyTV)
	 */
	@Parameter(required = false)
	private UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update =
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
	private ArrayList<UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>> iterativePostProcessing =
		null;

	private BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> rlCorrection;

	private UnaryFunctionOp<Interval, Img<O>> create;

	private UnaryComputerOp<RandomAccessibleInterval<K>, RandomAccessibleInterval<C>> fftKernel;

	private BinaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> convolver;

	private UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> copy;

	private UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> copy2;

	private RandomAccessibleInterval<O> raiExtendedReblurred;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		if (update == null) {
			update = (UnaryComputerOp) Computers.unary(ops(),
				RichardsonLucyUpdate.class, RandomAccessibleInterval.class,
				RandomAccessibleInterval.class);
		}

		rlCorrection = (BinaryComputerOp) Computers.binary(ops(),
			RichardsonLucyCorrection.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			getFFTInput(), getFFTKernel());

		fftKernel = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsOpC.class,
			getFFTKernel(), RandomAccessibleInterval.class);

		copy = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Copy.RAI.class,
			RandomAccessibleInterval.class, IntervalView.class);

		copy2 = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Copy.RAI.class,
			RandomAccessibleInterval.class, IntervalView.class);

		create = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, Dimensions.class, Util.getTypeFromInterval(out()));

		convolver = (BinaryComputerOp) Computers.binary(ops(), ConvolveFFTC.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, this.getFFTInput(), this.getFFTKernel(),
			true, false);

	}

	@Override
	public void compute2(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{
		// if a starting point for the estimate was not passed in then create
		// estimate Img and use the input as the starting point
		if (raiExtendedEstimate == null) {

			raiExtendedEstimate = create.compute1(getImgConvolutionInterval());

			copy.compute1(in, raiExtendedEstimate);
		}

		// create image for the reblurred
		raiExtendedReblurred = create.compute1(getImgConvolutionInterval());

		// perform fft of psf
		fftKernel.compute1(kernel, getFFTKernel());

		// -- perform iterations --

		for (int i = 0; i < getMaxIterations(); i++) {

			if (status != null) {
				status.showProgress(i, getMaxIterations());
			}

			// create reblurred by convolving kernel with estimate
			// NOTE: the FFT of the PSF of the kernel has been passed in as a
			// parameter. when the op was set up, and computed above, so we can use
			// compute1
			convolver.compute1(raiExtendedEstimate, this.raiExtendedReblurred);

			// compute correction factor
			rlCorrection.compute2(in, raiExtendedReblurred, raiExtendedReblurred);

			// perform update to calculate new estimate
			update.compute1(raiExtendedReblurred, raiExtendedEstimate);

			// apply post processing
			if (iterativePostProcessing != null) {
				for (UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> pp : iterativePostProcessing) {
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

		copy2.compute1(Views.interval(raiExtendedEstimate, new FinalInterval(start,
			end)), out);
	}

}