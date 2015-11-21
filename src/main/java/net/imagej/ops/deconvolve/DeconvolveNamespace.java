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

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
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

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucy(

		final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucy(

		final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, obfInput, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, obfInput, obfKernel, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final boolean nonCirculant)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations, nonCirculant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyFunction.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final boolean nonCirculant,
			final boolean accelerate)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyFunction.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations, nonCirculant, accelerate);
		return result;

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				maxIterations, imgConvolutionInterval, imgFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, maxIterations, imgConvolutionInterval, imgFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l,
			final boolean nonCirculant)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l, nonCirculant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l,
			final boolean nonCirculant, final boolean accelerate)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l, nonCirculant, accelerate);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l,
			final boolean nonCirculant, final boolean accelerate,
			final OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l, nonCirculant, accelerate,
				obfOutput);
		return result;
	}

//-- DeconvolveOps.RichardsonLucyTV

	@OpMethod(op = net.imagej.ops.Ops.Deconvolve.RichardsonLucyTV.class)
	public Object richardsonLucyTV(final Object... args) {
		return ops()
			.run(net.imagej.ops.Ops.Deconvolve.RichardsonLucyTV.class, args);
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, obfInput, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, obfInput, obfKernel, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, maxIterations,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, maxIterations,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final float regularizationFactor,
			final boolean nonCirculant)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations, regularizationFactor, nonCirculant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVImg.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory,
			final ComplexType<C> fftType, final ImgFactory<C> fftFactory,
			final int maxIterations, final float regularizationFactor,
			final boolean nonCirculant, final boolean accelerate)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVImg.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations, regularizationFactor, nonCirculant,
				accelerate);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops()
				.run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class, output,
					raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
					maxIterations, imgConvolutionInterval, imgFactory,
					regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops()
				.run(net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class,
					raiExtendedInput, raiExtendedKernel, fftInput, fftKernel, output,
					performInputFFT, maxIterations, imgConvolutionInterval, imgFactory,
					regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l,
			final boolean nonCirculant, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l, nonCirculant,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l,
			final boolean nonCirculant, final boolean accelerate,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l, nonCirculant, accelerate,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class)
	public
		<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l,
			final boolean nonCirculant, final boolean accelerate,
			final OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVRAI.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l, nonCirculant, accelerate,
				obfOutput, regularizationFactor);
		return result;
	}

	@Override
	public String getName() {
		return "deconvolve";
	}

}
