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

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * The deconvolve namespace contains deconvolution operations.
 * 
 * @author Alison Walter
 */
@Plugin(type = Namespace.class)
public class DeconvolveNamespace extends AbstractNamespace {

	// -- Deconvolve namespace ops --

	// -- DeconvolveOps.RichardsonLucy

	@OpMethod(op = net.imagej.ops.Ops.Deconvolve.RichardsonLucy.class)
	public Object richardsonLucy(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Deconvolve.RichardsonLucy.class, args);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucy(final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class, null, in,
				kernel, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, obfInput, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType,
				maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, fftFactory, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final boolean nonCirculant)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, fftFactory, maxIterations, nonCirculant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> richardsonLucy(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final boolean nonCirculant,
			final boolean accelerate)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, fftFactory, maxIterations, nonCirculant, accelerate);
		return result;

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(final RandomAccessibleInterval<I> raiExtendedInput,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, maxIterations, imgConvolutionInterval, imgFactory);

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, maxIterations,
			imgConvolutionInterval, imgFactory);

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, maxIterations,
			imgConvolutionInterval, imgFactory);

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, maxIterations,
			imgConvolutionInterval, imgFactory);

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			maxIterations, imgConvolutionInterval, imgFactory);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, maxIterations, imgConvolutionInterval, imgFactory);

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory);

	}

	/** Executes the "deconvolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftInput,
			Img<C> fftKernel, RandomAccessibleInterval<O> output,
			boolean performInputFFT, boolean performKernelFFT, int maxIterations,
			Interval imgConvolutionInterval, ImgFactory<O> imgFactory, Dimensions k)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k);

	}

	/** Executes the "deconvolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftInput,
			Img<C> fftKernel, RandomAccessibleInterval<O> output,
			boolean performInputFFT, boolean performKernelFFT, int maxIterations,
			Interval imgConvolutionInterval, ImgFactory<O> imgFactory, Dimensions k,
			Dimensions l)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, l);

	}

	/** Executes the "deconvolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftInput,
			Img<C> fftKernel, RandomAccessibleInterval<O> output,
			boolean performInputFFT, boolean performKernelFFT, int maxIterations,
			Interval imgConvolutionInterval, ImgFactory<O> imgFactory, Dimensions k,
			Dimensions l, boolean noncirculant)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, l, noncirculant);

	}

	/** Executes the "deconvolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftInput,
			Img<C> fftKernel, RandomAccessibleInterval<O> output,
			boolean performInputFFT, boolean performKernelFFT, int maxIterations,
			Interval imgConvolutionInterval, ImgFactory<O> imgFactory, Dimensions k,
			Dimensions l, boolean noncirculant, boolean accelerate)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, l, noncirculant, accelerate);
	}

	/** Executes the "deconvolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucy(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftInput,
			Img<C> fftKernel, RandomAccessibleInterval<O> output,
			boolean performInputFFT, boolean performKernelFFT, int maxIterations,
			Interval imgConvolutionInterval, ImgFactory<O> imgFactory, Dimensions k,
			Dimensions l, boolean noncirculant, boolean accelerate,
			OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, l, noncirculant, accelerate, obfOutput);
	}

//-- DeconvolveOps.RichardsonLucyTV

	@OpMethod(op = net.imagej.ops.Ops.Deconvolve.RichardsonLucyTV.class)
	public Object richardsonLucyTV(final Object... args) {
		return ops()
			.run(net.imagej.ops.Ops.Deconvolve.RichardsonLucyTV.class, args);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucyTV(final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				in, kernel, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, obfInput, maxIterations,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, maxIterations,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType,
				maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, fftFactory, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final float regularizationFactor,
			final boolean nonCirculant)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, fftFactory, maxIterations, regularizationFactor, nonCirculant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		Img<O> richardsonLucyTV(final Img<O> out, final Img<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final float regularizationFactor,
			final boolean nonCirculant, final boolean accelerate)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVImg.class,
				out, in, kernel, borderSize, obfInput, obfKernel, outType, outFactory,
				fftType, fftFactory, maxIterations, regularizationFactor, nonCirculant, accelerate);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, maxIterations, imgConvolutionInterval, imgFactory,
			regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, maxIterations,
			imgConvolutionInterval, imgFactory, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, maxIterations,
			imgConvolutionInterval, imgFactory, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, maxIterations,
			imgConvolutionInterval, imgFactory, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			maxIterations, imgConvolutionInterval, imgFactory, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, maxIterations, imgConvolutionInterval, imgFactory,
			regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final Dimensions k, final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final Dimensions k, final Dimensions l, final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, l, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final Dimensions k, final Dimensions l, final boolean nonCirculant,
			final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, l, nonCirculant, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final Dimensions k, final Dimensions l, final boolean nonCirculant,
			final boolean accelerate, final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, l, nonCirculant, accelerate, regularizationFactor);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		void richardsonLucyTV(final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final Img<C> fftInput, final Img<C> fftKernel,
			final RandomAccessibleInterval<O> output, final boolean performInputFFT,
			final boolean performKernelFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final Dimensions k, final Dimensions l, final boolean nonCirculant,
			final boolean accelerate,
			final OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput,
			final float regularizationFactor)
	{
		ops().run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
			raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
			performInputFFT, performKernelFFT, maxIterations, imgConvolutionInterval,
			imgFactory, k, l, nonCirculant, accelerate, obfOutput,
			regularizationFactor);
	}

	@Override
	public String getName() {
		return "deconvolve";
	}

}
