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

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
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
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
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
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
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
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
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
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
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
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <I extends RealType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final C fftType, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, fftType, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final C fftType, final int maxIterations,
			final boolean nonCirculant)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, fftType, maxIterations, nonCirculant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyF.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final C fftType, final int maxIterations,
			final boolean nonCirculant, final boolean accelerate)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyF.class, in, kernel, borderSize,
				obfInput, obfKernel, outType, fftType, maxIterations, nonCirculant,
				accelerate);
		return result;

	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final UnaryInplaceOp<O, O> accelerator)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations,
				accelerator);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final UnaryInplaceOp<O, O> accelerator,
			final UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations,
				accelerator, update);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final UnaryInplaceOp<O, O> accelerator,
			final UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update,
			RandomAccessibleInterval<O> raiExtendedEstimate)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations,
				accelerator, update, raiExtendedEstimate);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucy(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final int maxIterations, final UnaryInplaceOp<O, O> accelerator,
			final UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update,
			RandomAccessibleInterval<O> raiExtendedEstimate,
			final ArrayList<UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>> iterativePostProcessing)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyC.class, out, in1, in2,
				fftInput, fftKernel, performInputFFT, performKernelFFT, maxIterations,
				accelerator, update, raiExtendedEstimate, iterativePostProcessing);
		return result;
	}

//-- DeconvolveOps.RichardsonLucyTV

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
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
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
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
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
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
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, maxIterations, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
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
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, maxIterations,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <I extends RealType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final C fftType, final int maxIterations,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, fftType, maxIterations,
				regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final C fftType, final int maxIterations,
			final boolean nonCirculant, final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, fftType, maxIterations,
				nonCirculant, regularizationFactor);
		return result;
	}

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyTVF.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> richardsonLucyTV(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final Type<O> outType, final C fftType, final int maxIterations,
			final boolean nonCirculant, final boolean accelerate,
			final float regularizationFactor)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.RichardsonLucyTVF.class, in, kernel,
				borderSize, obfInput, obfKernel, outType, fftType, maxIterations,
				nonCirculant, accelerate, regularizationFactor);
		return result;
	}

	// -- richardson lucy correction ops

	@OpMethod(op = net.imagej.ops.deconvolve.RichardsonLucyCorrection.class)
	public <I extends RealType<I>, O extends RealType<O>, C extends ComplexType<C>>
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
	public <I extends RealType<I>, O extends RealType<O>, C extends ComplexType<C>>
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
		final RandomAccessibleInterval<O> arg)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.accelerate.VectorAccelerator.class, arg);
		return result;
	}

	// -- normalization factor op

	@OpMethod(
		op = net.imagej.ops.deconvolve.NonCirculantNormalizationFactor.class)
	public <O extends RealType<O>> RandomAccessibleInterval<O>
		normalizationFactor(final RandomAccessibleInterval<O> arg,
			final Dimensions k, final Dimensions l,
			final RandomAccessibleInterval<O> fftInput,
			final RandomAccessibleInterval<O> fftKernel,
			final Interval imgConvolutionInterval)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.NonCirculantNormalizationFactor.class, arg, k,
				l, fftInput, fftKernel, imgConvolutionInterval);
		return result;
	}

	// -- first guess ops
	@OpMethod(op = net.imagej.ops.deconvolve.NonCirculantFirstGuess.class)
	public <I extends RealType<I>, O extends RealType<O>>
		RandomAccessibleInterval<O> firstGuess(final RandomAccessibleInterval<I> in,
			final Interval imgConvolutionInterval, final Type<O> outType,
			final Dimensions k)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.deconvolve.NonCirculantFirstGuess.class, in,
				imgConvolutionInterval, outType, k);
		return result;
	}

	@Override
	public String getName() {
		return "deconvolve";
	}

}
