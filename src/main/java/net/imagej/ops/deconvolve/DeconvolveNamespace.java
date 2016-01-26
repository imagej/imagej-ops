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
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
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

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel,
				maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
		RandomAccessibleInterval<O> richardsonLucy(

			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
		RandomAccessibleInterval<O> richardsonLucy(

			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
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
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
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
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
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
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, outFactory, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final C fftType,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, outFactory, fftType, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final C fftType,
			final ImgFactory<C> fftFactory, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, outFactory, fftType, fftFactory,
				maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final C fftType,
			final ImgFactory<C> fftFactory, final int maxIterations,
			final boolean nonCirculant)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, outFactory, fftType, fftFactory,
				maxIterations, nonCirculant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final C fftType,
			final ImgFactory<C> fftFactory, final int maxIterations,
			final boolean nonCirculant, final boolean accelerate)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, outFactory, fftType, fftFactory,
				maxIterations, nonCirculant, accelerate);
		return result;

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, maxIterations, imgConvolutionInterval, imgFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, maxIterations,
				imgConvolutionInterval, imgFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final UnaryInplaceOp<O, O> accelerator)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, accelerator);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final UnaryInplaceOp<O, O> accelerator,
			final OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, accelerator, obfOutput);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final UnaryInplaceOp<O, O> accelerator,
			final OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput,
			AbstractUnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations,
				imgConvolutionInterval, imgFactory, accelerator, obfOutput, update);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final Dimensions k, final Dimensions l)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class, out, in1,
				in2, fftInput, fftKernel, maxIterations, imgConvolutionInterval,
				imgFactory, k, l);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final int maxIterations,
			final Interval imgConvolutionInterval, final ImgFactory<O> imgFactory,
			final Dimensions k, final Dimensions l)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class, out, in1,
				in2, fftInput, fftKernel, performInputFFT, maxIterations,
				imgConvolutionInterval, imgFactory, k, l);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final Dimensions k, final Dimensions l)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class, out, in1,
				in2, fftInput, fftKernel, performInputFFT, performKernelFFT,
				maxIterations, imgConvolutionInterval, imgFactory, k, l);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<K> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final UnaryInplaceOp<O, O> accelerator,
			final Dimensions k, final Dimensions l)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class, out, in1,
				in2, fftInput, fftKernel, performInputFFT, performKernelFFT,
				maxIterations, imgConvolutionInterval, imgFactory, accelerator, k, l);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class)
	public <
		I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<K> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final Interval imgConvolutionInterval,
			final ImgFactory<O> imgFactory, final UnaryInplaceOp<O, O> accelerator,
			final Dimensions k, final Dimensions l,
			final AbstractUnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyNonCirculantC.class, out, in1,
				in2, fftInput, fftKernel, performInputFFT, performKernelFFT,
				maxIterations, imgConvolutionInterval, imgFactory, accelerator, k, l,
				update);
		return result;
	}

//-- DeconvolveOps.RichardsonLucyTV

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
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
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
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
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, maxIterations,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, maxIterations,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final C fftType,
			final int maxIterations, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final C fftType,
			final ImgFactory<C> fftFactory, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final C fftType,
			final ImgFactory<C> fftFactory, final int maxIterations,
			final float regularizationFactor, final boolean nonCirculant)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations, regularizationFactor, nonCirculant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <
		I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final ImgFactory<O> outFactory, final C fftType,
			final ImgFactory<C> fftFactory, final int maxIterations,
			final float regularizationFactor, final boolean nonCirculant,
			final boolean accelerate)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, outFactory, fftType,
				fftFactory, maxIterations, regularizationFactor, nonCirculant,
				accelerate);
		return result;
	}

	// -- richardson lucy correction ops

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyCorrection.class)
	public <
		I extends RealType<I>, O extends RealType<O>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyCorrection(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<O> in2,
			final RandomAccessibleInterval<C> fftBuffer,
			final RandomAccessibleInterval<C> fftKernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyCorrection.class, out, in1, in2,
				fftBuffer, fftKernel);
		return result;
	}

	// -- richardson lucy update ops

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVUpdate.class)
	public <
		I extends RealType<I>, O extends RealType<O>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyUpdate(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<O> in, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVUpdate.class, out, in,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVUpdate.class)
	public <O extends RealType<O>> RandomAccessibleInterval<O>
		richardsonLucyUpdate(final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<O> in, final float regularizationFactor,
			final RandomAccessibleInterval<O> variation)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVUpdate.class, out, in,
				regularizationFactor, variation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyUpdate.class)
	public <O extends RealType<O>> RandomAccessibleInterval<O>
		richardsonLucyUpdate(final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<O> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyUpdate.class, out, in);
		return result;
	}

	// -- accelerate ops

	@OpMethod(op = net.imagej.ops.deconvolve.accelerate.VectorAccelerator.class)
	public <O extends RealType<O>> RandomAccessibleInterval<O> accelerate(
		final RandomAccessibleInterval<O> arg, final ImgFactory<O> imgFactory)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.accelerate.VectorAccelerator.class, arg,
				imgFactory);
		return result;
	}

	@Override
	public String getName() {
		return "deconvolve";
	}

}
