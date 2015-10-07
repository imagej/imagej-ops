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

import java.util.List;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.gauss.DefaultGaussRAI;
import net.imagej.ops.filter.gauss.GaussRAISingleSigma;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * The filter namespace contains ops that filter data.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class FilterNamespace extends AbstractNamespace {

	// -- addNoise --

	@OpMethod(op = net.imagej.ops.Ops.Filter.AddNoise.class)
	public Object addNoise(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Filter.AddNoise.class, args);
	}

	@OpMethod(op = net.imagej.ops.filter.addNoise.AddNoiseRealType.class)
	public <I extends RealType<I>, O extends RealType<O>> O addNoise(final O out,
		final I in, final double rangeMin, final double rangeMax,
		final double rangeStdDev)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.filter.addNoise.AddNoiseRealType.class, out,
				in, rangeMin, rangeMax, rangeStdDev);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.addNoise.AddNoiseRealType.class)
	public <I extends RealType<I>, O extends RealType<O>> O addNoise(final O out,
		final I in, final double rangeMin, final double rangeMax,
		final double rangeStdDev, final long seed)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.filter.addNoise.AddNoiseRealType.class, out,
				in, rangeMin, rangeMax, rangeStdDev, seed);
		return result;
	}

	// -- addPoissonNoise --

	@OpMethod(op = net.imagej.ops.Ops.Filter.AddPoissonNoise.class)
	public Object addPoissonNoise(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Filter.AddPoissonNoise.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.filter.addPoissonNoise.AddPoissonNoiseRealType.class)
	public <I extends RealType<I>, O extends RealType<O>> O addPoissonNoise(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(
				net.imagej.ops.filter.addPoissonNoise.AddPoissonNoiseRealType.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.filter.addPoissonNoise.AddPoissonNoiseRealType.class)
	public <I extends RealType<I>, O extends RealType<O>> O addPoissonNoise(
		final O out, final I in, final long seed)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(
				net.imagej.ops.filter.addPoissonNoise.AddPoissonNoiseRealType.class,
				out, in, seed);
		return result;
	}

	// -- convolve --

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = Ops.Filter.Convolve.class)
	public Object convolve(final Object... args) {
		return ops().run(Ops.Filter.Convolve.NAME, args);
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
		net.imagej.ops.filter.convolve.ConvolveNaiveImg.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> convolve(final Img<I> in, final RandomAccessibleInterval<K> kernel)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(Ops.Filter.Convolve.NAME, in, kernel);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
		net.imagej.ops.filter.convolve.ConvolveNaiveImg.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> convolve(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(Ops.Filter.Convolve.NAME, out, in, kernel);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
		net.imagej.ops.filter.convolve.ConvolveNaiveImg.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> convolve(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long... borderSize)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(Ops.Filter.Convolve.NAME, out, in, kernel, borderSize);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
		net.imagej.ops.filter.convolve.ConvolveNaiveImg.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> convolve(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(Ops.Filter.Convolve.NAME, out, in, kernel, borderSize,
				obfInput);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
		net.imagej.ops.filter.convolve.ConvolveNaiveImg.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> convolve(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(Ops.Filter.Convolve.NAME, out, in, kernel, borderSize,
				obfInput, obfKernel);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
		net.imagej.ops.filter.convolve.ConvolveNaiveImg.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> convolve(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(Ops.Filter.Convolve.NAME, out, in, kernel, borderSize,
				obfInput, obfKernel, outType);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
		net.imagej.ops.filter.convolve.ConvolveNaiveImg.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> convolve(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(Ops.Filter.Convolve.NAME, out, in, kernel, borderSize,
				obfInput, obfKernel, outType, outFactory);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> convolve(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> convolve(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.convolve.ConvolveFFTImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, fftFactory);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveNaive.class)
	public <I extends RealType<I>, K extends RealType<K>, O extends RealType<O>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<O> out,
			final RandomAccessible<I> in, final RandomAccessibleInterval<K> kernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.filter.convolve.ConvolveNaive.class, out, in, kernel);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void convolve(final RandomAccessibleInterval<I> raiExtendedInput)
	{
		ops().run(net.imagej.ops.filter.convolve.ConvolveFFTRAI.class,
			raiExtendedInput);
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void convolve(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel)
	{
		ops().run(net.imagej.ops.filter.convolve.ConvolveFFTRAI.class,
			raiExtendedInput, raiExtendedKernel);
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void
		convolve(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel, final Img<C> fftInput)
	{
		ops().run(net.imagej.ops.filter.convolve.ConvolveFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput);
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void convolve(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel)
	{
		ops().run(net.imagej.ops.filter.convolve.ConvolveFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel);
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void convolve(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output)
	{
		ops().run(net.imagej.ops.filter.convolve.ConvolveFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output);
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void convolve(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT)
	{
		ops().run(net.imagej.ops.filter.convolve.ConvolveFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT);
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void convolve(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT)
	{
		ops().run(net.imagej.ops.filter.convolve.ConvolveFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT);
	}

	// -- correlate --

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = Ops.Filter.Correlate.class)
	public Object correlate(final Object... args) {
		return ops().run(Ops.Filter.Correlate.NAME, args);
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<I> in, final RandomAccessibleInterval<K> kernel)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				in, kernel);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				out, in, kernel);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long... borderSize)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				out, in, kernel, borderSize);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				out, in, kernel, borderSize, obfInput);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> correlate(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.correlate.CorrelateFFTImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, fftFactory);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void correlate(final RandomAccessibleInterval<I> raiExtendedInput)
	{
		ops().run(net.imagej.ops.filter.correlate.CorrelateFFTRAI.class,
			raiExtendedInput);
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void correlate(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel)
	{
		ops().run(net.imagej.ops.filter.correlate.CorrelateFFTRAI.class,
			raiExtendedInput, raiExtendedKernel);
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void
		correlate(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel, final Img<C> fftInput)
	{
		ops().run(net.imagej.ops.filter.correlate.CorrelateFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput);
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void correlate(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel)
	{
		ops().run(net.imagej.ops.filter.correlate.CorrelateFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel);
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void correlate(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output)
	{
		ops().run(net.imagej.ops.filter.correlate.CorrelateFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output);
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void correlate(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT)
	{
		ops().run(net.imagej.ops.filter.correlate.CorrelateFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT);
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void correlate(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT)
	{
		ops().run(net.imagej.ops.filter.correlate.CorrelateFFTRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT);
	}

	// -- fft --

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = Ops.Filter.FFT.class)
	public Object fft(final Object... args) {
		return ops().run(Ops.Filter.FFT.NAME, args);
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTFunctionOp.class)
	public
		<T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTFunctionOp.class, in);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTFunctionOp.class)
	public
		<T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long... borderSize)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTFunctionOp.class, in, borderSize);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTFunctionOp.class)
	public
		<T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long[] borderSize, final Boolean fast)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTFunctionOp.class, in, borderSize, fast);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTFunctionOp.class)
	public
		<T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long[] borderSize, final Boolean fast,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTFunctionOp.class, in, borderSize, fast, obf);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTFunctionOp.class)
	public
		<T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long[] borderSize, final Boolean fast,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf,
			ImgFactory factory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTFunctionOp.class, in, borderSize, fast, obf,
				factory);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTFunctionOp.class)
	public
		<T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long[] borderSize, final Boolean fast,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf,
			ImgFactory factory, Type<C> fftType)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTFunctionOp.class, in, borderSize, fast, obf,
				factory, fftType);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTComputerOp.class)
	public <T extends RealType<T>, C extends ComplexType<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTComputerOp.class, out, in);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTComputerOp.class)
	public <T extends RealType<T>, C extends ComplexType<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTComputerOp.class, out, in, obf);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTComputerOp.class)
	public <T extends RealType<T>, C extends ComplexType<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf,
			final long... paddedSize)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(
				net.imagej.ops.filter.fft.FFTComputerOp.class, out, in, obf, paddedSize);
		return result;
	}

	// -- fftSize --

	/** Executes the "fftSize" operation on the given arguments. */
	@OpMethod(op = Ops.Filter.FFTSize.class)
	public Object fftSize(final Object... args) {
		return ops().run(Ops.Filter.FFTSize.NAME, args);
	}

	/** Executes the "fftSize" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fftSize.ComputeFFTSize.class)
	public List<long[]> fftSize(final long[] inputSize, final long[] paddedSize,
		final long[] fftSize, final Boolean forward, final Boolean fast)
	{
		@SuppressWarnings("unchecked")
		final List<long[]> result =
			(List<long[]>) ops().run(
				net.imagej.ops.filter.fftSize.ComputeFFTSize.class, inputSize,
				paddedSize, fftSize, forward, fast);
		return result;
	}

	// -- dog --

	@OpMethod(op = Ops.Filter.DoG.class)
	public Object dog(Object... args) {
		return ops().run(Ops.Filter.DoG.class, args);
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DefaultDoG.class)
	public
		<T extends NumericType<T> & NativeType<T>>
		RandomAccessibleInterval<T>
		dog(
			final RandomAccessibleInterval<T> in,
			final ComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss1,
			final ComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss2,
			final FunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> outputCreator,
			final FunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> tmpCreator)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.dog.DefaultDoG.class, in, gauss1, gauss2,
				outputCreator, tmpCreator);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DefaultDoG.class)
	public
		<T extends NumericType<T> & NativeType<T>>
		RandomAccessibleInterval<T>
		dog(
			final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<T> in,
			final ComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss1,
			final ComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss2,
			final FunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> outputCreator,
			final FunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> tmpCreator)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.dog.DefaultDoG.class, out, in, gauss1, gauss2,
				outputCreator, tmpCreator);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DefaultDoG.class)
	public
		<T extends NumericType<T> & NativeType<T>>
		RandomAccessibleInterval<T>
		dog(
			final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<T> in,
			final ComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss1,
			final ComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss2,
			final FunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> outputCreator,
			final FunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> tmpCreator,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> fac)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.dog.DefaultDoG.class, out, in, gauss1, gauss2,
				outputCreator, tmpCreator, fac);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DoGVaryingSigmas.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> dog(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double[] sigmas1,
			final double[] sigmas2,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(
				net.imagej.ops.filter.dog.DoGVaryingSigmas.class, out, in, sigmas1,
				sigmas2, outOfBounds);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DoGVaryingSigmas.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> dog(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double[] sigmas1,
			final double... sigmas2)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(
				net.imagej.ops.filter.dog.DoGVaryingSigmas.class, out, in, sigmas1,
				sigmas2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DoGVaryingSigmas.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> dog(final RandomAccessibleInterval<T> in,
			final double[] sigmas1, final double... sigmas2)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(
				net.imagej.ops.filter.dog.DoGVaryingSigmas.class, in, sigmas1, sigmas2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DoGSingleSigmas.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> dog(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double sigma1,
			final double sigma2,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(
				net.imagej.ops.filter.dog.DoGSingleSigmas.class, out, in, sigma1,
				sigma2, outOfBounds);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DoGSingleSigmas.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> dog(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double sigma1,
			final double sigma2)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(
				net.imagej.ops.filter.dog.DoGSingleSigmas.class, out, in, sigma1,
				sigma2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DoGSingleSigmas.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> dog(final RandomAccessibleInterval<T> in,
			final double sigma1, final double sigma2)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(
				net.imagej.ops.filter.dog.DoGSingleSigmas.class, null, in, sigma1,
				sigma2);
		return result;
	}

	// -- gauss --

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = Ops.Filter.Gauss.class)
	public Object gauss(final Object... args) {
		return ops().run(Ops.Filter.Gauss.NAME, args);
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double[] sigmas,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(DefaultGaussRAI.class, out, in,
				sigmas, outOfBounds);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double... sigmas)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(DefaultGaussRAI.class, out, in,
				sigmas);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<T> in,
			final double... sigmas)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops()
				.run(DefaultGaussRAI.class, in, sigmas);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(RandomAccessibleInterval<V> out,
			RandomAccessibleInterval<T> in, double sigma)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(GaussRAISingleSigma.class, out,
				in, sigma);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(RandomAccessibleInterval<V> out,
			RandomAccessibleInterval<T> in, double sigma,
			OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(GaussRAISingleSigma.class, out,
				in, sigma, outOfBounds);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<T> in,
			final double sigma)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(
				net.imagej.ops.filter.gauss.GaussRAISingleSigma.class, in, sigma);
		return result;
	}

	// -- ifft --

	/** Executes the "ifft" operation on the given arguments. */
	@OpMethod(op = Ops.Filter.IFFT.class)
	public Object ifft(final Object... args) {
		return ops().run(Ops.Filter.IFFT.NAME, args);
	}

	/** Executes the "ifft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.ifft.IFFTImg.class)
	public <T extends RealType<T>, O extends Img<T>> Img<O> ifft(
		final Img<O> out, final Img<ComplexFloatType> in)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.filter.ifft.IFFTImg.class, out, in);
		return result;
	}

	/** Executes the "ifft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.ifft.IFFTRAI.class)
	public <C extends ComplexType<C>, T extends RealType<T>>
		RandomAccessibleInterval<T> ifft(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<C> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.ifft.IFFTRAI.class, out, in);
		return result;
	}

	// -- mean filter --

	/** Executes the "mean" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.mean.DefaultMeanFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> mean(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.mean.DefaultMeanFilter.class, out, in, shape);
		return result;
	}

	/** Executes the "mean" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.mean.DefaultMeanFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> mean(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.mean.DefaultMeanFilter.class, out, in, shape,
				outOfBoundsFactory);
		return result;
	}

	// -- non-linear filters --

	/** Executes the "max" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.max.DefaultMaxFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> max(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.max.DefaultMaxFilter.class, out, in, shape);
		return result;
	}

	/** Executes the "max" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.max.DefaultMaxFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> max(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.max.DefaultMaxFilter.class, out, in, shape,
				outOfBoundsFactory);
		return result;
	}

	/** Executes the "median" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.median.DefaultMedianFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> median(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.median.DefaultMedianFilter.class, out, in, shape);
		return result;
	}

	/** Executes the "median" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.median.DefaultMedianFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> median(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.median.DefaultMedianFilter.class, out, in, shape,
				outOfBoundsFactory);
		return result;
	}

	/** Executes the "min" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.min.DefaultMinFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> min(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.min.DefaultMinFilter.class, out, in, shape);
		return result;
	}

	/** Executes the "min" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.min.DefaultMinFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> min(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.min.DefaultMinFilter.class, out, in, shape,
				outOfBoundsFactory);
		return result;
	}

	/** Executes the "sigma" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.sigma.DefaultSigmaFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> sigma(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final Double range, final Double minPixelFraction)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.sigma.DefaultSigmaFilter.class, out, in, shape,
				range, minPixelFraction);
		return result;
	}

	/** Executes the "sigma" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.sigma.DefaultSigmaFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> sigma(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, T> outOfBoundsFactory, final Double range,
		final Double minPixelFraction)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.sigma.DefaultSigmaFilter.class, out, in, shape,
				outOfBoundsFactory, range, minPixelFraction);
		return result;
	}

	/** Executes the "variance" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.variance.DefaultVarianceFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> variance(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.variance.DefaultVarianceFilter.class, out, in,
				shape);
		return result;
	}

	/** Executes the "variance" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.variance.DefaultVarianceFilter.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> variance(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.variance.DefaultVarianceFilter.class, out, in,
				shape, outOfBoundsFactory);
		return result;
	}

	// -- Namespace methods --

	@Override
	public String getName() {
		return "filter";
	}

}
