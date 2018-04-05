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

import net.imagej.ops.Ops;
import net.imagej.ops.filter.correlate.CorrelateFFTC;
import net.imagej.ops.map.MapBinaryInplace1s;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.inplace.AbstractBinaryInplace1Op;
import net.imagej.ops.special.inplace.BinaryInplace1Op;
import net.imagej.ops.special.inplace.Inplaces;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Computes Richardson Lucy correction factor for (@link
 * RandomAccessibleInterval) (Lucy, L. B. (1974).
 * "An iterative technique for the rectification of observed distributions".)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <C>
 */
@Plugin(type = Ops.Deconvolve.RichardsonLucyCorrection.class,
	priority = Priority.HIGH)
public class RichardsonLucyCorrection<I extends RealType<I>, O extends RealType<O>, C extends ComplexType<C>>
	extends
	AbstractBinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>
	implements Ops.Deconvolve.RichardsonLucyCorrection
{

	/** fft of reblurred (will be computed) **/
	@Parameter
	private RandomAccessibleInterval<C> fftBuffer;

	/** fft of kernel (needs to be previously computed) **/
	@Parameter
	private RandomAccessibleInterval<C> fftKernel;

	private BinaryInplace1Op<RandomAccessibleInterval<O>, RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> divide;

	private BinaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> correlate;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		divide = new DivideHandleZeroMap1();
		divide.setEnvironment(ops());
		divide.initialize();

		correlate = (BinaryComputerOp) Computers.binary(ops(), CorrelateFFTC.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, fftBuffer, fftKernel, true, false);
	}

	/**
	 * computes the correction factor of the Richardson Lucy Algorithm
	 */
	@Override
	public void compute(RandomAccessibleInterval<I> observed,
		RandomAccessibleInterval<O> reblurred,
		RandomAccessibleInterval<O> correction)
	{
		// divide observed image by reblurred
		divide.mutate1(reblurred, observed);

		// correlate with psf to compute the correction factor
		// Note: FFT of psf is pre-computed and set as an input parameter of the op
		correlate.compute(reblurred, correction);

	}

	private static class DivideHandleZeroMap1<I extends RealType<I>, O extends RealType<O>>
		extends AbstractBinaryInplace1Op<IterableInterval<O>, IterableInterval<I>>
	{

		private BinaryInplace1Op<O, I, O> divide;

		private BinaryInplace1Op<IterableInterval<O>, IterableInterval<I>, IterableInterval<O>> map;

		@Override
		@SuppressWarnings("unchecked")
		public void initialize() {
			divide = new DivideHandleZeroOp1<>();
			divide.setEnvironment(ops());
			divide.initialize();

			map = (BinaryInplace1Op) Inplaces.binary1(ops(),
				MapBinaryInplace1s.IIAndII.class, IterableInterval.class,
				IterableInterval.class, divide);
		}

		@Override
		public void mutate1(final IterableInterval<O> outin,
			final IterableInterval<I> input2)
		{
			map.mutate1(outin, input2);
		}
	}

	private static class DivideHandleZeroOp1<I extends RealType<I> & NumericType<I>, O extends RealType<O> & NumericType<O>>
		extends AbstractBinaryInplace1Op<O, I>
	{

		@Override
		public void mutate1(final O outin, final I input) {
			final O tmp = outin.copy();

			if (outin.getRealFloat() > 0) {

				tmp.setReal(input.getRealFloat());
				tmp.div(outin);
				outin.set(tmp);
			}
			else {
				outin.setReal(0.0);
			}
		}
	}
}
