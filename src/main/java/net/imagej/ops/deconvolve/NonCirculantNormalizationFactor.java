/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2021 ImageJ developers.
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
import net.imagej.ops.filter.correlate.CorrelateFFTC;
import net.imagej.ops.map.MapBinaryInplace1s;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.inplace.AbstractBinaryInplace1Op;
import net.imagej.ops.special.inplace.AbstractUnaryInplaceOp;
import net.imagej.ops.special.inplace.BinaryInplace1Op;
import net.imagej.ops.special.inplace.Inplaces;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculate non-circulant normalization factor. This is used as part of the
 * Boundary condition handling scheme described here
 * http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl)
 *
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */

@Plugin(type = Ops.Deconvolve.NormalizationFactor.class,
	priority = Priority.LOW)
public class NonCirculantNormalizationFactor<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractUnaryInplaceOp<RandomAccessibleInterval<O>> implements
	Ops.Deconvolve.NormalizationFactor
{

	/**
	 * k is the size of the measurement window. That is the size of the acquired
	 * image before extension, k is required to calculate the non-circulant
	 * normalization factor
	 */
	@Parameter
	private Dimensions k;

	/**
	 * l is the size of the psf, l is required to calculate the non-circulant
	 * normalization factor
	 */
	@Parameter
	private Dimensions l;

	@Parameter
	RandomAccessibleInterval<C> fftInput;

	@Parameter
	RandomAccessibleInterval<C> fftKernel;

	// Normalization factor for edge handling (see
	// http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl)
	private Img<O> normalization = null;

	private UnaryFunctionOp<Dimensions, Img<O>> create;

	private BinaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>> correlater;

	private DivideHandleZeroMap<O> divide;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		create = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, Dimensions.class, Util.getTypeFromInterval(out()));

		correlater = (BinaryComputerOp) Computers.binary(ops(), CorrelateFFTC.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, fftInput, fftKernel, true, false);

		divide = new DivideHandleZeroMap<>();
		divide.setEnvironment(ops());
		divide.initialize();
	}

	/**
	 * apply the normalization image needed for semi noncirculant model see
	 * http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl
	 */
	@Override
	public void mutate(RandomAccessibleInterval<O> arg) {
		// if the normalization image hasn't been computed yet, then compute it
		if (normalization == null) {
			this.createNormalizationImageSemiNonCirculant(arg);
		}

		// normalize for non-circulant deconvolution
		divide.mutate1(normalization, Views.iterable(arg));
	}

	protected void createNormalizationImageSemiNonCirculant(Interval fastFFTInterval) {

		// k is the window size (valid image region)
		final int length = k.numDimensions();

		final long[] n = new long[length];
		final long[] nFFT = new long[length];

		// n is the valid image size plus the extended region
		// also referred to as object space size
		for (int d = 0; d < length; d++) {
			n[d] = k.dimension(d) + l.dimension(d) - 1;
		}

		// nFFT is the size of n after (potentially) extending further
		// to a fast FFT size
		for (int d = 0; d < length; d++) {
			nFFT[d] = fastFFTInterval.dimension(d);
		}

		FinalDimensions fd = new FinalDimensions(nFFT);

		// create the normalization image
		normalization = create.calculate(fd);

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

		final Img<O> tempImg = create.calculate(fd);

		// 3. correlate psf with the output of step 2.
		correlater.compute(normalization, tempImg);

		normalization = tempImg;

		final Cursor<O> cursorN = normalization.cursor();

		while (cursorN.hasNext()) {
			cursorN.fwd();

			if (cursorN.get().getRealFloat() <= 1e-3f) {
				cursorN.get().setReal(1.0f);

			}
		}
	}

	// -- Helper classes --

	private static class DivideHandleZeroMap<T extends RealType<T>> extends
		AbstractBinaryInplace1Op<IterableInterval<T>, IterableInterval<T>>
	{

		private BinaryInplace1Op<T, T, T> divide;

		private BinaryInplace1Op<IterableInterval<T>, IterableInterval<T>, IterableInterval<T>> map;

		@Override
		@SuppressWarnings("unchecked")
		public void initialize() {
			divide = new DivideHandleZeroOp<>();
			divide.setEnvironment(ops());
			divide.initialize();

			map = (BinaryInplace1Op) Inplaces.binary1(ops(),
				MapBinaryInplace1s.IIAndII.class, IterableInterval.class,
				IterableInterval.class, divide);
		}

		@Override
		public void mutate1(final IterableInterval<T> input1,
			final IterableInterval<T> input2)
		{
			map.mutate1(input1, input2);
		}
	}

	private static class DivideHandleZeroOp<I extends RealType<I> & NumericType<I>>
		extends AbstractBinaryInplace1Op<I, I>
	{

		@Override
		public void mutate1(final I input, final I outin) {
			final I tmp = outin.copy();

			if (input.getRealFloat() > 0) {

				tmp.set(outin);
				tmp.div(input);
				outin.set(tmp);
			}
			else {
				outin.setReal(0.0);
			}
		}
	}
}
