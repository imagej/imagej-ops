/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2020 ImageJ developers.
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

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.RealComposite;

import org.scijava.plugin.Plugin;

/**
 * The filter namespace contains ops that filter data.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class FilterNamespace extends AbstractNamespace {

	// -- addNoise --

	@OpMethod(ops = { net.imagej.ops.filter.addGaussianNoise.AddGaussianNoiseRealType.class,
		net.imagej.ops.filter.addGaussianNoise.AddGaussianNoiseRealTypeCFI.class })
	public <I extends RealType<I>, O extends RealType<O>> O addGaussianNoise(final O out,
		final I in, final double rangeMin, final double rangeMax,
		final double rangeStdDev)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(Ops.Filter.AddGaussianNoise.class, out, in, rangeMin,
			rangeMax, rangeStdDev);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.filter.addGaussianNoise.AddGaussianNoiseRealType.class,
		net.imagej.ops.filter.addGaussianNoise.AddGaussianNoiseRealTypeCFI.class })
	public <I extends RealType<I>, O extends RealType<O>> O addGaussianNoise(final O out,
		final I in, final double rangeMin, final double rangeMax,
		final double rangeStdDev, final long seed)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(Ops.Filter.AddGaussianNoise.class, out, in, rangeMin,
			rangeMax, rangeStdDev, seed);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.addGaussianNoise.AddGaussianNoiseRealTypeCFI.class)
	public <T extends RealType<T>> T addGaussianNoise(final T in, final double rangeMin,
		final double rangeMax, final double rangeStdDev)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Filter.AddGaussianNoise.class, in, rangeMin,
			rangeMax, rangeStdDev);
		return result;
	}

	// -- addPoissonNoise --

	@OpMethod(
		op = net.imagej.ops.filter.addPoissonNoise.AddPoissonNoiseRealType.class)
	public <I extends RealType<I>, O extends RealType<O>> O addPoissonNoise(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(Ops.Filter.AddPoissonNoise.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.filter.addPoissonNoise.AddPoissonNoiseRealType.class)
	public <I extends RealType<I>, O extends RealType<O>> O addPoissonNoise(
		final O out, final I in, final long seed)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(Ops.Filter.AddPoissonNoise.class, out, in,
			seed);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.addPoissonNoise.AddPoissonNoiseMap.class)
	public <I extends RealType<I>, O extends RealType<O>> IterableInterval<O>
		addPoissonNoise(final IterableInterval<O> out, final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			Ops.Filter.AddPoissonNoise.class, out, in);
		return result;
	}
	
	// -- Uniform Noise --
	
	@OpMethod(op = net.imagej.ops.filter.addUniformNoise.AddUniformNoiseRealType.class)
	public <I extends RealType<I>> I addUniformNoise(final I out,
		final I in, final double rangeMin, final double rangeMax)
	{
		@SuppressWarnings("unchecked")
		final I result = (I) ops().run(Ops.Filter.AddUniformNoise.class, out, in, rangeMin,
			rangeMax);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.filter.addUniformNoise.AddUniformNoiseRealType.class)
	public <I extends RealType<I>> I addUniformNoise(final I out,
		final I in, final double rangeMin, final double rangeMax, final Long seed)
	{
		@SuppressWarnings("unchecked")
		final I result = (I) ops().run(Ops.Filter.AddUniformNoise.class, out, in, rangeMin,
			rangeMax, seed);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.filter.addUniformNoise.AddUniformNoiseIntegerType.class)
	public <I extends IntegerType<I>> I addUniformNoise(final I out,
		final I in, final long rangeMin, final long rangeMax)
	{
		@SuppressWarnings("unchecked")
		final I result = (I) ops().run(Ops.Filter.AddUniformNoise.class, out, in, rangeMin,
			rangeMax);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.filter.addUniformNoise.AddUniformNoiseIntegerType.class)
	public <I extends IntegerType<I>> I addUniformNoise(final I out,
		final I in, final long rangeMin, final long rangeMax, final boolean clampOutput)
	{
		@SuppressWarnings("unchecked")
		final I result = (I) ops().run(Ops.Filter.AddUniformNoise.class, out, in, rangeMin,
			rangeMax, clampOutput);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.filter.addUniformNoise.AddUniformNoiseIntegerType.class)
	public <I extends IntegerType<I>> I addUniformNoise(final I out,
		final I in, final long rangeMin, final long rangeMax, final boolean clampOutput, final Long seed)
	{
		@SuppressWarnings("unchecked")
		final I result = (I) ops().run(Ops.Filter.AddUniformNoise.class, out, in, rangeMin,
			rangeMax, clampOutput, seed);
		return result;
	}

	// -- bilateral --

	/** Executes the "bilateral" filter on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.bilateral.DefaultBilateral.class)
	public <I extends RealType<I>, O extends RealType<O>>
		RandomAccessibleInterval<O> bilateral(final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in, final double sigmaR,
			final double sigmaS, final int radius)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Bilateral.class, out,
				in, sigmaR, sigmaS, radius);
		return result;
	}
	
	
	// -- convolve --
	
	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveNaiveF.class,
		net.imagej.ops.filter.convolve.PadAndConvolveFFTF.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> convolve(
			final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class,
				in, kernel);
		return result;
	}
	
	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveFFTC.class,
		net.imagej.ops.filter.convolve.PadAndConvolveFFT.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> convolve(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, output,
				raiExtendedInput, raiExtendedKernel);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveNaiveF.class,
		net.imagej.ops.filter.convolve.PadAndConvolveFFTF.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obf)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, in,
				kernel, obf);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.filter.convolve.ConvolveNaiveF.class,
		net.imagej.ops.filter.convolve.PadAndConvolveFFTF.class })
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obf,
			final O outType)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, in,
				kernel, obf, outType);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.PadAndConvolveFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long... borderSize)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class,out, in,
				kernel, borderSize);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.PadAndConvolveFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, out, in,
				kernel, borderSize, obfInput);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.PadAndConvolveFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, out, in,
				kernel, borderSize, obfInput, obfKernel);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.PadAndConvolveFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final O outType)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, out, in,
				kernel, borderSize, obfInput, obfKernel, outType);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.PadAndConvolveFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final O outType, final C fftType)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, out, in,
				kernel, borderSize, obfInput, obfKernel, outType, fftType);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveNaiveC.class)
	public <I extends RealType<I>, K extends RealType<K>, O extends RealType<O>>
		RandomAccessibleInterval<O> convolve(final RandomAccessibleInterval<O> out,
			final RandomAccessible<I> in, final RandomAccessibleInterval<K> kernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, out,
				in, kernel);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> convolve(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> convolve(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> convolve(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT);
		return result;
	}

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.convolve.ConvolveFFTC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> convolve(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Convolve.class, output,
				raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT);
		return result;
	}

	// -- correlate --

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(ops = {net.imagej.ops.filter.correlate.CorrelateFFTC.class,net.imagej.ops.filter.correlate.PadAndCorrelateFFT.class})
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> correlate(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class,out, in,
				kernel);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.PadAndCorrelateFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> correlate(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long... borderSize)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class,out, in,
				kernel, borderSize);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.PadAndCorrelateFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> correlate(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class, out, in,
				kernel, borderSize, obfInput);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.PadAndCorrelateFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> correlate(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class, out, in,
				kernel, borderSize, obfInput, obfKernel);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.PadAndCorrelateFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>>
		RandomAccessibleInterval<O> correlate(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final O outType)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class, out, in,
				kernel, borderSize, obfInput, obfKernel, outType);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.PadAndCorrelateFFT.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> correlate(final RandomAccessibleInterval<O> out,final RandomAccessibleInterval<I> in,
			final RandomAccessibleInterval<K> kernel, final long[] borderSize,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput,
			final OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel,
			final O outType, final C fftType)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class, out, in,
				kernel, borderSize, obfInput, obfKernel, outType, fftType);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> correlate(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class,
				output, raiExtendedInput, raiExtendedKernel, fftInput);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> correlate(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class,
				output, raiExtendedInput, raiExtendedKernel, fftInput, fftKernel);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> correlate(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class,
				output, raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT);
		return result;
	}

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.correlate.CorrelateFFTC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> correlate(
			final RandomAccessibleInterval<O> output,
			final RandomAccessibleInterval<I> raiExtendedInput,
			final RandomAccessibleInterval<K> raiExtendedKernel,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.Correlate.class,
				output, raiExtendedInput, raiExtendedKernel, fftInput, fftKernel,
				performInputFFT, performKernelFFT);
		return result;
	}

	// -- create fft output

	@OpMethod(op = net.imagej.ops.filter.fft.CreateOutputFFTMethods.class)
	public <T> Img<T> createFFTOutput(final Dimensions in1, final T in2) {
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(Ops.Filter.CreateFFTOutput.class,
			in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.fft.CreateOutputFFTMethods.class)
	public <T> Img<T> createFFTOutput(final Dimensions in1, final T in2,
		final boolean fast)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(Ops.Filter.CreateFFTOutput.class,
			in1, in2, fast);
		return result;
	}

	// -- fft --

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTMethodsOpF.class)
	public <T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(Ops.Filter.FFT.class, in);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTMethodsOpF.class)
	public <T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long... borderSize)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(Ops.Filter.FFT.class, in,
				borderSize);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTMethodsOpF.class)
	public <T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long[] borderSize, final boolean fast)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(Ops.Filter.FFT.class, in,
				borderSize, fast);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTMethodsOpF.class)
	public <T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long[] borderSize, final boolean fast,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(Ops.Filter.FFT.class, in,
				borderSize, fast, obf);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTMethodsOpF.class)
	public <T extends RealType<T>, I extends RandomAccessibleInterval<T>, C extends ComplexType<C>, O extends RandomAccessibleInterval<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<T> in,
			final long[] borderSize, final boolean fast,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf,
			final C fftType)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(Ops.Filter.FFT.class, in,
				borderSize, fast, obf, fftType);
		return result;
	}

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.fft.FFTMethodsOpC.class)
	public <T extends RealType<T>, C extends ComplexType<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(Ops.Filter.FFT.class, out, in);
		return result;
	}

	// -- fftSize --
	@OpMethod(op = net.imagej.ops.filter.fftSize.ComputeFFTMethodsSize.class)
	public long[][] fftSize(final Dimensions in1, final boolean forward,
		final boolean fast)
	{
		final long[][] result = (long[][]) ops().run(Ops.Filter.FFTSize.class, in1,
			forward, fast);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.fftSize.DefaultComputeFFTSize.class)
	public long[][] fftSize(final Dimensions in1, final boolean powerOfTwo) {
		final long[][] result = (long[][]) ops().run(Ops.Filter.FFTSize.class, in1,
			powerOfTwo);
		return result;
	}

	// -- derivativeGauss --

	@OpMethod(
		op = net.imagej.ops.filter.derivativeGauss.DefaultDerivativeGauss.class)
	public <T extends RealType<T>> RandomAccessibleInterval<DoubleType>
		derivativeGauss(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<DoubleType> in, final int[] derivatives,
			final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<DoubleType> result =
			(RandomAccessibleInterval<DoubleType>) ops().run(
				Ops.Filter.DerivativeGauss.class, out, in, derivatives, sigma);
		return result;
	}

	// -- dog --

	@OpMethod(op = net.imagej.ops.filter.dog.DefaultDoG.class)
	public <T extends NumericType<T> & NativeType<T>> RandomAccessibleInterval<T>
		dog(final RandomAccessibleInterval<T> in,
			final UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss1,
			final UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss2,
			final UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> outputCreator,
			final UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> tmpCreator)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Filter.DoG.class, in, gauss1,
				gauss2, outputCreator, tmpCreator);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DefaultDoG.class)
	public <T extends NumericType<T> & NativeType<T>> RandomAccessibleInterval<T>
		dog(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<T> in,
			final UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss1,
			final UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> gauss2,
			final UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> outputCreator,
			final UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> tmpCreator)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Filter.DoG.class, out, in,
				gauss1, gauss2, outputCreator, tmpCreator);
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
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.DoG.class, out, in,
				sigmas1, sigmas2, outOfBounds);
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
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.DoG.class, out, in,
				sigmas1, sigmas2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DoGVaryingSigmas.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> dog(final RandomAccessibleInterval<T> in,
			final double[] sigmas1, final double... sigmas2)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.DoG.class, in, sigmas1,
				sigmas2);
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
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.DoG.class, out, in,
				sigma1, sigma2, outOfBounds);
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
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.DoG.class, out, in,
				sigma1, sigma2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.dog.DoGSingleSigmas.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> dog(final RandomAccessibleInterval<T> in,
			final double sigma1, final double sigma2)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.DoG.class, null, in,
				sigma1, sigma2);
		return result;
	}

	// -- frangiVesselness --

	/**
	 * Executes the "Frangi Vesselness" filter operation on the given arguments.
	 *
	 * @param in - input image
	 * @param out - output image
	 * @param spacing - n-dimensional array indicating the physical distance
	 *          between data points in the image
	 * @param scale - the scale (number of pixels) over which the filter
	 *          calculates. The smaller the value, the more sensitive the filter.
	 */
	@OpMethod(op = net.imagej.ops.filter.vesselness.DefaultFrangi.class)
	public <T extends RealType<T>, U extends RealType<U>>
		RandomAccessibleInterval<U> frangiVesselness(
			final RandomAccessibleInterval<U> out,
			final RandomAccessibleInterval<T> in, final double[] spacing,
			final int scale)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<U> result =
			(RandomAccessibleInterval<U>) ops().run(Ops.Filter.FrangiVesselness.class,
				out, in, spacing, scale);
		return result;
	}

	// -- gauss --

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends NumericType<T>, V extends NumericType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double[] sigmas,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.Gauss.class, out, in,
				sigmas, outOfBounds);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends NumericType<T>, V extends NumericType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double... sigmas)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.Gauss.class, out, in,
				sigmas);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends NumericType<T>, V extends NumericType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<T> in,
			final double... sigmas)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.Gauss.class, in,
				sigmas);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends NumericType<T>, V extends NumericType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.Gauss.class, out, in,
				sigma);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends NumericType<T>, V extends NumericType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double sigma,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.Gauss.class, out, in,
				sigma, outOfBounds);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends NumericType<T>, V extends NumericType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<T> in,
			final double sigma)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.Gauss.class, in,
				sigma);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRA.class)
	public <T extends NumericType<T>, V extends NumericType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessible<T> in, final double... sigmas)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(Ops.Filter.Gauss.class, out, in,
				sigmas);
		return result;
	}

	// -- hessian --

	@OpMethod(op = net.imagej.ops.filter.hessian.HessianRAI.class)
	public <T extends RealType<T>> CompositeIntervalView<T, RealComposite<T>>
		hessian(final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final CompositeIntervalView<T, RealComposite<T>> result =
			(CompositeIntervalView<T, RealComposite<T>>) ops().run(
				net.imagej.ops.filter.hessian.HessianRAI.class, in);
		return result;
	}

	// -- ifft --

	/** Executes the "ifft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.ifft.IFFTMethodsOpC.class)
	public <C extends ComplexType<C>, T extends RealType<T>>
		RandomAccessibleInterval<T> ifft(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<C> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Filter.IFFT.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.ifft.IFFTMethodsOpI.class)
	public <C extends ComplexType<C>> RandomAccessibleInterval<C> ifft(
		final RandomAccessibleInterval<C> arg)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) ops().run(Ops.Filter.IFFT.class, arg);
		return result;
	}

	// -- linear filter --

	/** Executes the "linearFilter" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.FFTMethodsLinearFFTFilterC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> linearFilter(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final BinaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> frequencyOp)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.LinearFilter.class,
				out, in1, in2, frequencyOp);
		return result;
	}

	/** Executes the "linearFilter" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.FFTMethodsLinearFFTFilterC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> linearFilter(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final BinaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> frequencyOp)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.LinearFilter.class,
				out, in1, in2, fftInput, frequencyOp);
		return result;
	}

	/** Executes the "linearFilter" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.FFTMethodsLinearFFTFilterC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> linearFilter(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final BinaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> frequencyOp)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.LinearFilter.class,
				out, in1, in2, fftInput, fftKernel, frequencyOp);
		return result;
	}

	/** Executes the "linearFilter" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.FFTMethodsLinearFFTFilterC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> linearFilter(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT,
			final BinaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> frequencyOp)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.LinearFilter.class,
				out, in1, in2, fftInput, fftKernel, performInputFFT, frequencyOp);
		return result;
	}

	/** Executes the "linearFilter" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.FFTMethodsLinearFFTFilterC.class)
	public <I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		RandomAccessibleInterval<O> linearFilter(
			final RandomAccessibleInterval<O> out,
			final RandomAccessibleInterval<I> in1,
			final RandomAccessibleInterval<K> in2,
			final RandomAccessibleInterval<C> fftInput,
			final RandomAccessibleInterval<C> fftKernel,
			final boolean performInputFFT, final boolean performKernelFFT,
			final BinaryComputerOp<RandomAccessibleInterval<C>, RandomAccessibleInterval<C>, RandomAccessibleInterval<C>> frequencyOp)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(Ops.Filter.LinearFilter.class,
				out, in1, in2, fftInput, fftKernel, performInputFFT, performKernelFFT,
				frequencyOp);
		return result;
	}

	// -- mean filter --

	/** Executes the "mean" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.mean.DefaultMeanFilter.class)
	public <I extends ComplexType<I>, O extends ComplexType<O>>
		IterableInterval<O> mean(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			Ops.Filter.Mean.class, out, in, shape);
		return result;
	}

	/** Executes the "mean" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.mean.DefaultMeanFilter.class)
	public <I extends ComplexType<I>, O extends ComplexType<O>>
		IterableInterval<O> mean(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			Ops.Filter.Mean.class, out, in, shape, outOfBoundsFactory);
		return result;
	}

	// -- non-linear filters --

	/** Executes the "max" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.max.DefaultMaxFilter.class)
	public <T extends RealType<T>, V extends RealType<V>> IterableInterval<T> max(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Max.class, out, in, shape);
		return result;
	}

	/** Executes the "max" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.max.DefaultMaxFilter.class)
	public <T extends RealType<T>> IterableInterval<T> max(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape, final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Max.class, out, in, shape, outOfBoundsFactory);
		return result;
	}

	/** Executes the "median" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.median.DefaultMedianFilter.class)
	public <T extends RealType<T>> IterableInterval<T> median(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Median.class, out, in, shape);
		return result;
	}

	/** Executes the "median" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.median.DefaultMedianFilter.class)
	public <T extends RealType<T>> IterableInterval<T> median(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape, final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Median.class, out, in, shape, outOfBoundsFactory);
		return result;
	}

	/** Executes the "min" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.min.DefaultMinFilter.class)
	public <T extends RealType<T>> IterableInterval<T> min(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Min.class, out, in, shape);
		return result;
	}

	/** Executes the "min" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.min.DefaultMinFilter.class)
	public <T extends RealType<T>> IterableInterval<T> min(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape, final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Min.class, out, in, shape, outOfBoundsFactory);
		return result;
	}

	/**
	 * Executes the "paddingIntervalCentered" operation on the given arguments.
	 */
	@OpMethod(op = net.imagej.ops.filter.pad.PaddingIntervalCentered.class)
	public <T extends ComplexType<T>> Interval paddingIntervalCentered(
		final RandomAccessibleInterval<T> in, final Dimensions paddedDimensions)
	{
		final Interval result = (Interval) ops().run(
			Ops.Filter.PaddingIntervalCentered.class, in, paddedDimensions);
		return result;
	}

	/**
	 * Executes the "paddingIntervalOrigin" operation on the given arguments.
	 */
	@OpMethod(op = net.imagej.ops.filter.pad.PaddingIntervalOrigin.class)
	public <T extends ComplexType<T>> Interval paddingIntervalOrigin(
		final RandomAccessibleInterval<T> in, final Interval centeredInterval)
	{
		final Interval result = (Interval) ops().run(
			Ops.Filter.PaddingIntervalOrigin.class, in, centeredInterval);
		return result;
	}

	/** Executes the "padInput" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.pad.PadInput.class)
	public <T extends ComplexType<T>> RandomAccessibleInterval<T> padInput(
		final RandomAccessibleInterval<T> in, final Dimensions paddedDimensions)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Filter.PadInput.class, in,
				paddedDimensions);
		return result;
	}

	/** Executes the "padInput" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.pad.PadInput.class)
	public <T extends ComplexType<T>> RandomAccessibleInterval<T> padInput(
		final RandomAccessibleInterval<T> in, final Dimensions paddedDimensions,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Filter.PadInput.class, in,
				paddedDimensions, obf);
		return result;
	}

	// -- pad input fft methods

	/**
	 * Executes the "padInputFFT" filter operation on the given arguments.
	 */
	@OpMethod(ops = { net.imagej.ops.filter.pad.PadInputFFTMethods.class,
		net.imagej.ops.filter.pad.DefaultPadInputFFT.class })
	public <T extends ComplexType<T>> RandomAccessibleInterval<T> padFFTInput(
		final RandomAccessibleInterval<T> in1, final Dimensions in2)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Filter.PadFFTInput.class, in1,
				in2);
		return result;
	}

	/**
	 * Executes the "padInputFFT" filter operation on the given arguments.
	 */
	@OpMethod(ops = { net.imagej.ops.filter.pad.PadInputFFTMethods.class,
		net.imagej.ops.filter.pad.DefaultPadInputFFT.class })
	public <T extends ComplexType<T>> RandomAccessibleInterval<T> padFFTInput(
		final RandomAccessibleInterval<T> in1, final Dimensions in2,
		final boolean fast)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Filter.PadFFTInput.class, in1,
				in2, fast);
		return result;
	}

	/**
	 * Executes the "padInputFFT" filter operation on the given arguments.
	 */
	@OpMethod(ops = { net.imagej.ops.filter.pad.PadInputFFTMethods.class,
		net.imagej.ops.filter.pad.DefaultPadInputFFT.class })
	public <T extends ComplexType<T>> RandomAccessibleInterval<T> padFFTInput(
		final RandomAccessibleInterval<T> in1, final Dimensions in2,
		final boolean fast,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Filter.PadFFTInput.class, in1,
				in2, fast, obf);
		return result;
	}

	// - pad shift fft kernel

	/**
	 * Executes the "padShiftFFTKernel" filter operation on the given arguments.
	 */
	@OpMethod(ops = { net.imagej.ops.filter.pad.PadShiftKernelFFTMethods.class,
		net.imagej.ops.filter.pad.PadShiftKernel.class,
		net.imagej.ops.filter.pad.DefaultPadShiftKernelFFT.class })
	public <T extends ComplexType<T>> RandomAccessibleInterval<T>
		padShiftFFTKernel(final RandomAccessibleInterval<T> in1,
			final Dimensions in2)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				Ops.Filter.PadShiftFFTKernel.class, in1, in2);
		return result;
	}

	/**
	 * Executes the "padShiftFFTKernel" filter operation on the given arguments.
	 */
	@OpMethod(ops = { net.imagej.ops.filter.pad.PadShiftKernelFFTMethods.class,
		net.imagej.ops.filter.pad.DefaultPadShiftKernelFFT.class })
	public <T extends ComplexType<T>> RandomAccessibleInterval<T>
		padShiftFFTKernel(final RandomAccessibleInterval<T> in1,
			final Dimensions in2, final boolean fast)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				Ops.Filter.PadShiftFFTKernel.class, in1, in2, fast);
		return result;
	}

	// -- partial derivative --

	/** Executes the "partial derivative" operation on the given arguments */
	@OpMethod(op = net.imagej.ops.filter.derivative.PartialDerivativeRAI.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> partialDerivative(
		final RandomAccessibleInterval<T> in, final int dimension)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.derivative.PartialDerivativeRAI.class, in,
				dimension);
		return result;
	}

	/** Executes the "partial derivative" operation on the given arguments */
	@OpMethod(op = net.imagej.ops.filter.derivative.PartialDerivativeRAI.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> partialDerivative(
		final RandomAccessibleInterval<T> out, final RandomAccessibleInterval<T> in,
		final int dimension)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.derivative.PartialDerivativeRAI.class, out, in,
				dimension);
		return result;
	}

	/** Executes the "partial derivative" operation on all dimensions */
	@OpMethod(op = net.imagej.ops.filter.derivative.PartialDerivativesRAI.class)
	public <T extends RealType<T>> CompositeIntervalView<T, RealComposite<T>>
		allPartialDerivatives(final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final CompositeIntervalView<T, RealComposite<T>> result =
			(CompositeIntervalView<T, RealComposite<T>>) ops().run(
				net.imagej.ops.filter.derivative.PartialDerivativesRAI.class, in);
		return result;
	}

	/** Executes the "sigma" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.sigma.DefaultSigmaFilter.class)
	public <T extends RealType<T>> IterableInterval<T> sigma(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape, final Double range, final Double minPixelFraction)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Sigma.class, out, in, shape, range, minPixelFraction);
		return result;
	}

	/** Executes the "sigma" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.sigma.DefaultSigmaFilter.class)
	public <T extends RealType<T>> IterableInterval<T> sigma(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape, final OutOfBoundsFactory<T, T> outOfBoundsFactory,
		final Double range, final Double minPixelFraction)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Sigma.class, out, in, shape, outOfBoundsFactory, range,
			minPixelFraction);
		return result;
	}

	// -- Sobel

	@OpMethod(op = net.imagej.ops.filter.sobel.SobelRAI.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> sobel(
		final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.sobel.SobelRAI.class, in);
		return result;
	}

	/** Executes the "tubeness" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.tubeness.DefaultTubeness.class)
	public <T extends RealType<T>> IterableInterval<DoubleType> tubeness(
		final IterableInterval<DoubleType> out,
		final RandomAccessibleInterval<T> in, final double sigma,
		final double... calibration)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<DoubleType> result =
			(IterableInterval<DoubleType>) ops().run(Ops.Filter.Tubeness.class, out,
				in, sigma, calibration);
		return result;
	}

	/** Executes the "tubeness" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.tubeness.DefaultTubeness.class)
	public <T extends RealType<T>> IterableInterval<DoubleType> tubeness(
		final RandomAccessibleInterval<T> in, final double sigma,
		final double... calibration)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<DoubleType> result =
			(IterableInterval<DoubleType>) ops().run(Ops.Filter.Tubeness.class, in,
				sigma, calibration);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.sobel.SobelRAI.class)
	public <T extends RealType<T>> RandomAccessibleInterval<T> sobel(
		final RandomAccessibleInterval<T> out, final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.filter.sobel.SobelRAI.class, out, in);
		return result;
	}

	/** Executes the "variance" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.variance.DefaultVarianceFilter.class)
	public <T extends RealType<T>> IterableInterval<T> variance(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Variance.class, out, in, shape);
		return result;
	}

	/** Executes the "variance" filter operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.variance.DefaultVarianceFilter.class)
	public <T extends RealType<T>> IterableInterval<T> variance(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in,
		final Shape shape, final OutOfBoundsFactory<T, T> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Filter.Variance.class, out, in, shape, outOfBoundsFactory);
		return result;
	}

	// -- Namespace methods --

	@Override
	public String getName() {
		return "filter";
	}

}
