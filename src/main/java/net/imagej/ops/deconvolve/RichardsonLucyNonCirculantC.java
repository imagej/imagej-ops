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

package net.imagej.ops.deconvolve;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractIterativeFFTFilterC;
import net.imagej.ops.filter.convolve.ConvolveFFTC;
import net.imagej.ops.filter.correlate.CorrelateFFTC;
import net.imagej.ops.filter.fft.FFTMethodsOpC;
import net.imagej.ops.math.divide.DivideHandleZero;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.app.StatusService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

/**
 * TODO: This class repeats parts of RichardsonLucyC -- figure out best way to
 * make it DRY Non-circulant Richardson Lucy algorithm for (@link
 * RandomAccessibleInterval). Boundary conditions are handled by the scheme
 * described at:
 * http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl)
 *
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */

@Plugin(type = Ops.Deconvolve.RichardsonLucy.class,
	priority = Priority.LOW_PRIORITY)
public class RichardsonLucyNonCirculantC<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractIterativeFFTFilterC<I, O, K, C> implements
	Ops.Deconvolve.RichardsonLucy
{

	@Parameter(required = false)
	private StatusService status;

	@Parameter(required = false)
	private UIService ui;

	/**
	 * TODO: review and document! - k is the size of the measurement window. That
	 * is the size of the acquired image before extension, k is required to
	 * calculate the non-circulant normalization factor
	 */
	@Parameter
	private Dimensions k;

	/**
	 * TODO: review and document! - l is the size of the psf, l is required to
	 * calculate the non-circulant normalization factor
	 */
	@Parameter
	private Dimensions l;

	// Normalization factor for edge handling (see
	// http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl)
	private Img<O> normalization = null;

	/**
	 * Op that computes Richardson Lucy update
	 */
	@Parameter(required = false)
	private UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update;

	private BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> rlCorrection;

	private BinaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> divide;

	private UnaryFunctionOp<Interval, Img<O>> create;

	private UnaryHybridCF<RandomAccessibleInterval<I>, O> sum;

	private UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<C>> fftIn;

	private UnaryComputerOp<RandomAccessibleInterval<K>, RandomAccessibleInterval<C>> fftKernel;

	private BinaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> convolver;

	private UnaryHybridCF<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> copy;

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

		divide = (BinaryComputerOp) Computers.binary(ops(), DivideHandleZero.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class);

		fftIn = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsOpC.class,
			getFFTInput(), RandomAccessibleInterval.class);

		fftKernel = (UnaryComputerOp) Computers.unary(ops(), FFTMethodsOpC.class,
			getFFTKernel(), RandomAccessibleInterval.class);

		copy = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Copy.RAI.class,
			RandomAccessibleInterval.class, IntervalView.class);

		create = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, Dimensions.class, Util.getTypeFromInterval(out()));

		sum = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Stats.Sum.class, Util
			.getTypeFromInterval(out()), RandomAccessibleInterval.class);
	}

	@Override
	public void performIterations(final RandomAccessibleInterval<I> in,
		final RandomAccessibleInterval<K> kernel,
		final RandomAccessibleInterval<O> out)
	{

		for (int i = 0; i < getMaxIterations(); i++) {

			if (status != null) {
				status.showProgress(i, getMaxIterations());
			}

			// create reblurred by convolving kernel with estimate
			convolver.compute2(this.getRAIExtendedEstimate(), in2(), this
				.getRAIExtendedReblurred());

			// compute correction factor
			rlCorrection.compute2(in, getRAIExtendedReblurred(),
				getRAIExtendedReblurred());

			// perform update
			update.compute1(getRAIExtendedReblurred(), getRAIExtendedEstimate());

			// normalize for non-circulant deconvolution
			divide.compute2(getRAIExtendedEstimate(), normalization,
				getRAIExtendedEstimate());

			// accelerate
			if (getAccelerator() != null) {
				getAccelerator().mutate(getRAIExtendedEstimate());
			}
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void preProcess(final RandomAccessibleInterval<I> in,
		final RandomAccessibleInterval<K> kernel,
		final RandomAccessibleInterval<O> out)
	{

		final Type<O> outType = Util.getTypeFromInterval(out);

		// create image for the estimate, this image is defined over the entire
		// convolution interval
		final Img<O> estimate = create.compute1(getImgConvolutionInterval());

		// set first guess to be a constant = to the average value

		// so first compute the sum...
		final O s = sum.compute1(in);

		// then the number of pixels
		final long numPixels = k.dimension(0) * k.dimension(1) * k.dimension(2);

		// then the average value...
		final double average = s.getRealDouble() / (numPixels);

		// set first guess as the average value computed above (TODO: use fill op)
		for (final O type : estimate) {
			type.setReal(average);
		}

		// create image for the reblurred
		final Img<O> reblurred = this.getImgFactory().create(
			getImgConvolutionInterval(), outType.createVariable());

		setRAIExtendedEstimate(estimate);
		setRAIExtendedReblurred(reblurred);

		// perform fft of input
		fftIn.compute1(in, getFFTInput());

		// perform fft of psfs
		fftKernel.compute1(kernel, getFFTKernel());

		// now that the FFTs are computed use them to initialize a convolver
		convolver = (BinaryComputerOp) Computers.binary(ops(), ConvolveFFTC.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, this.getFFTInput(), this.getFFTKernel(),
			true, false);

		normalization = getImgFactory().create(estimate, outType.createVariable());

		this.createNormalizationImageSemiNonCirculant(kernel);

	}

	/**
	 * postProcess copy back to original size
	 */
	@Override
	protected void postProcess(final RandomAccessibleInterval<I> in,
		final RandomAccessibleInterval<K> kernel,
		final RandomAccessibleInterval<O> out)
	{

		// when doing non circulant deconvolution we need to crop and copy back to
		// the
		// original image size

		final long[] start = new long[k.numDimensions()];
		final long[] end = new long[k.numDimensions()];

		for (int d = 0; d < k.numDimensions(); d++) {
			start[d] = 0;
			end[d] = start[d] + k.dimension(d) - 1;
		}

		copy.compute1(Views.interval(getRAIExtendedEstimate(), new FinalInterval(
			start, end)), out);

	}

	/**
	 * create the normalization image needed for semi noncirculant model see
	 * http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl
	 */
	protected void createNormalizationImageSemiNonCirculant(
		final RandomAccessibleInterval<K> kernel)
	{

		// k is the window size (valid image region)
		final int length = k.numDimensions();

		final long[] n = new long[length];
		final long[] nFFT = new long[length];

		// n is the valid image size plus the extended region
		// also referred to as object space size
		for (int d = 0; d < length; d++) {
			n[d] = k.dimension(d) + l.dimension(d) - 1;
		}

		for (int d = 0; d < length; d++) {
			nFFT[d] = getRAIExtendedReblurred().dimension(d);
		}

		// create the normalization image
		normalization = create.compute1(getRAIExtendedReblurred());

		// size of the measurement window
		final Point size = new Point(length);
		final long[] sizel = new long[length];

		for (int d = 0; d < length; d++) {
			size.setPosition(k.dimension(d), d);
			sizel[d] = k.dimension(d);
		}

		// starting point of the measurement window when it is centered in fft space
		final Point start = new Point(length);
		final long[] startl = new long[length];
		final long[] endl = new long[length];

		for (int d = 0; d < length; d++) {
			start.setPosition((nFFT[d] - k.dimension(d)) / 2, d);
			startl[d] = (nFFT[d] - k.dimension(d)) / 2;
			endl[d] = startl[d] + sizel[d] - 1;
		}

		// size of the object space
		final Point maskSize = new Point(length);
		final long[] maskSizel = new long[length];

		for (int d = 0; d < length; d++) {
			maskSize.setPosition(Math.min(n[d], nFFT[d]), d);
			maskSizel[d] = Math.min(n[d], nFFT[d]);
		}

		// starting point of the object space within the fft space
		final Point maskStart = new Point(length);
		final long[] maskStartl = new long[length];

		for (int d = 0; d < length; d++) {
			maskStart.setPosition((Math.max(0, nFFT[d] - n[d]) / 2), d);
			maskStartl[d] = (Math.max(0, nFFT[d] - n[d]) / 2);
		}

		final RandomAccessibleInterval<O> temp = Views.interval(normalization,
			new FinalInterval(startl, endl));
		final Cursor<O> normCursor = Views.iterable(temp).cursor();

		// draw a cube the size of the measurement space
		while (normCursor.hasNext()) {
			normCursor.fwd();
			normCursor.get().setReal(1.0);
		}

		final Img<O> tempImg = create.compute1(getRAIExtendedReblurred());

		// 3. correlate psf with the output of step 2.
		// TODO: Discuss how to initialize this and whether FFT filters should be
		// unary...
		ops().run(CorrelateFFTC.class, tempImg, normalization, kernel,
			getFFTInput(), getFFTKernel(), true, false);

		normalization = tempImg;

		final Cursor<O> cursorN = normalization.cursor();

		while (cursorN.hasNext()) {
			cursorN.fwd();

			if (cursorN.get().getRealFloat() <= 1e-7f) {
				cursorN.get().setReal(0.0f);

			}
		}
	}

}
