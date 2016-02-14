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
import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
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
	// TODO: think through whether we can always have a statusservice. 
	@Parameter(required = false)
	private StatusService status;

	/**
	 * An OutOfBoundsFactory which defines the extension strategy
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput;

	/**
	 * Op that computes Richardson Lucy update
	 * TODO: figure out best way to override for different algorithm (like RichardsonLucyTV) 
	 */
	@Parameter(required = false)
	private UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update =
		null;

	private BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> rlCorrection;

	private UnaryFunctionOp<Interval, Img<O>> create;

	private UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<C>> fftIn;

	private UnaryComputerOp<RandomAccessibleInterval<K>, RandomAccessibleInterval<C>> fftKernel;

	private BinaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> convolver;

	private UnaryHybridCF<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> copy;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		// TODO: look into using RAIs here
		if (update == null) {
			update = (UnaryComputerOp) Computers.unary(ops(),
				RichardsonLucyUpdate.class, RandomAccessibleInterval.class,
				RandomAccessibleInterval.class);
		}

		rlCorrection = (BinaryComputerOp) Computers.binary(ops(),
			RichardsonLucyCorrection.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			getFFTInput(), getFFTKernel());

		fftIn = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsOpC.class,
			getFFTInput(), RandomAccessibleInterval.class);

		fftKernel = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsOpC.class,
			getFFTKernel(), RandomAccessibleInterval.class);

		copy = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Copy.RAI.class,
			RandomAccessibleInterval.class, IntervalView.class);

		create = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, Dimensions.class, Util.getTypeFromInterval(out()));

	}

	@Override
	public void performIterations(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{

		for (int i = 0; i < getMaxIterations(); i++) {

			if (status != null) {
				status.showProgress(i, getMaxIterations());
			}

			convolver.compute2(this.getRAIExtendedEstimate(), in2(), this
				.getRAIExtendedReblurred());

			// compute correction factor
			rlCorrection.compute2(in, getRAIExtendedReblurred(),
				getRAIExtendedReblurred());

			// perform update
			update.compute1(getRAIExtendedReblurred(), getRAIExtendedEstimate());

			// accelerate
			if (getAccelerator() != null) {
				getAccelerator().mutate(getRAIExtendedEstimate());
			}
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void preProcess(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{

		// if no output out of bounds factory exists create the obf for output
		if (obfOutput == null) {
			obfOutput =
				new OutOfBoundsConstantValueFactory<O, RandomAccessibleInterval<O>>(Util
					.getTypeFromInterval(out).createVariable());
		}

		// create image for the reblurred
		Img<O> reblurred = create.compute1(out);

		// extend the output and use it as a buffer to store the estimate
		setRAIExtendedEstimate(Views.interval(Views.extend(out, obfOutput),
			getImgConvolutionInterval()));

		// assemble the extended view of the reblurred
		setRAIExtendedReblurred(Views.interval(Views.extend(reblurred, obfOutput),
			getImgConvolutionInterval()));

		// set first guess of estimate
		// TODO: implement logic for various first guesses.
		// for now just set to original image
		copy.compute1(in, getRAIExtendedEstimate());

		// perform fft of input
		fftIn.compute1(in, getFFTInput());

		// perform fft of psf
		fftKernel.compute1(kernel, getFFTKernel());

		// now that the FFTs are computed use them to initialize a convolver
		convolver = (BinaryComputerOp) Computers.binary(ops(), ConvolveFFTC.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, this.getFFTInput(), this.getFFTKernel(),
			true, false);
	}

}
