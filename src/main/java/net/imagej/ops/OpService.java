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

package net.imagej.ops;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.imagej.ImageJService;
import net.imagej.ImgPlus;
import net.imagej.ops.logic.LogicNamespace;
import net.imagej.ops.math.MathNamespace;
import net.imagej.ops.misc.Size;
import net.imagej.ops.statistics.Sum;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imglib2.Dimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.integer.LongType;

import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.plugin.PTService;

/**
 * Interface for services that manage and execute {@link Op}s.
 *
 * @author Curtis Rueden
 */
public interface OpService extends PTService<Op>, ImageJService {

	/**
	 * Executes the given operation with the specified arguments. The best
	 * {@link Op} implementation to use will be selected automatically from the
	 * operation name and arguments.
	 *
	 * @param name The operation to execute. If multiple {@link Op}s share this
	 *          name, then the best {@link Op} implementation to use will be
	 *          selected automatically from the name and arguments.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	Object run(String name, Object... args);

	/**
	 * Executes the operation of the given type with the specified arguments. The
	 * best {@link Op} implementation to use will be selected automatically from
	 * the operation type and arguments.
	 *
	 * @param type The {@link Class} of the operation to execute. If multiple
	 *          {@link Op}s share this type (e.g., the type is an interface which
	 *          multiple {@link Op}s implement), then the best {@link Op}
	 *          implementation to use will be selected automatically from the type
	 *          and arguments.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	<OP extends Op> Object run(Class<OP> type, Object... args);

	/**
	 * Executes the given {@link Op} with the specified arguments.
	 *
	 * @param op The {@link Op} to execute.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	Object run(Op op, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * populating its inputs.
	 *
	 * @param name The name of the operation. If multiple {@link Op}s share this
	 *          name, then the best {@link Op} implementation to use will be
	 *          selected automatically from the name and arguments.
	 * @param args The operation's arguments.
	 * @return An {@link Op} with populated inputs, ready to run.
	 */
	Op op(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and arguments,
	 * populating its inputs.
	 *
	 * @param type The {@link Class} of the operation. If multiple {@link Op}s
	 *          share this type (e.g., the type is an interface which multiple
	 *          {@link Op}s implement), then the best {@link Op} implementation to
	 *          use will be selected automatically from the type and arguments.
	 * @param args The operation's arguments.
	 * @return An {@link Op} with populated inputs, ready to run.
	 */
	<O extends Op> O op(Class<O> type, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 *
	 * @param name The name of the operation.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	Module module(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 *
	 * @param type The required type of the operation. If multiple {@link Op}s
	 *          share this type (e.g., the type is an interface which multiple
	 *          {@link Op}s implement), then the best {@link Op} implementation to
	 *          use will be selected automatically from the type and arguments.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	<OP extends Op> Module module(Class<OP> type, Object... args);

	/**
	 * Wraps the given {@link Op} as a {@link Module}, populating its inputs.
	 *
	 * @param op The {@link Op} to wrap and populate.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the {@link Op}, with populated inputs,
	 *         ready to run.
	 */
	Module module(Op op, Object... args);

	/** Gets the metadata for a given {@link Op}. */
	CommandInfo info(Op op);

	/** Gets the names of all available operations. */
	Collection<String> ops();

	// -- Operation shortcuts - global namespace --

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = Ops.ASCII.class)
	Object ascii(Object... args);

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.ascii.DefaultASCII.class)
	<T extends RealType<T>> String ascii(final IterableInterval<T> image);

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.ascii.DefaultASCII.class)
	<T extends RealType<T>> String ascii(final IterableInterval<T> image,
		final RealType<T> min);

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.ascii.DefaultASCII.class)
	<T extends RealType<T>> String ascii(final IterableInterval<T> image,
		final RealType<T> min, final RealType<T> max);

	/** Executes the "chunker" operation on the given arguments. */
	@OpMethod(op = Ops.Chunker.class)
	Object chunker(Object... args);

	/** Executes the "convert" operation on the given arguments. */
	@OpMethod(op = Ops.Convert.class)
	Object convert(Object... args);

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = Ops.Convolve.class)
	Object convolve(Object... args);

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = Ops.Correlate.class)
	Object correlate(Object... args);

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = Ops.CreateImg.class)
	Object createimg(Object... args);

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.CreateEmptyImgPlusCopy.class)
	<V extends NativeType<V>> ImgPlus<V> createimg(ImgPlus<V> input);

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.CreateImgDifferentNativeType.class)
	<V extends NativeType<V>> Img<V> createimg(Img<V> input, V type);

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.CreateImgNativeType.class)
	<V extends NativeType<V>> Img<V> createimg(ImgFactory<V> fac, V outType,
		Dimensions dims);

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.DefaultCreateImg.class)
	<V extends Type<V>> Img<V> createimg(long... dims);

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.DefaultCreateImg.class)
	<V extends Type<V>> Img<V> createimg(V outType, long... dims);

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.DefaultCreateImg.class)
	<V extends Type<V>> Img<V> createimg(V outType, ImgFactory<V> fac,
		long... dims);

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.CreateEmptyImgCopy.class)
	<V extends NativeType<V>> Img<V> createimg(Img<V> input);

	/** Executes the "crop" operation on the given arguments. */
	@OpMethod(op = Ops.Crop.class)
	Object crop(Object... args);

	/** Executes the "deconvolve" operation on the given arguments. */
	@OpMethod(op = Ops.Deconvolve.class)
	Object deconvolve(Object... args);

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = Ops.Equation.class)
	Object equation(Object... args);

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.equation.DefaultEquation.class)
	<T extends RealType<T>> IterableInterval<T> equation(final String in);

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.equation.DefaultEquation.class)
	<T extends RealType<T>> IterableInterval<T> equation(
		final IterableInterval<T> out, final String in);

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = Ops.Eval.class)
	Object eval(Object... args);

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.eval.DefaultEval.class)
	Object eval(final String expression);

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.eval.DefaultEval.class)
	Object eval(final String expression, final Map<String, Object> vars);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = Ops.FFT.class)
	Object fft(Object... args);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.image.FFTImg.class)
	<T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<I> in);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.image.FFTImg.class)
	<T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<ComplexFloatType> out, final Img<I> in);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.image.FFTImg.class)
	<T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<ComplexFloatType> out, final Img<I> in, final long... borderSize);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.image.FFTImg.class)
	<T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<ComplexFloatType> out, final Img<I> in, final long[] borderSize,
		final Boolean fast);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.image.FFTImg.class)
	<T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<ComplexFloatType> out, final Img<I> in, final long[] borderSize,
		final Boolean fast,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.methods.FFTRAI.class)
	<T extends RealType<T>, C extends ComplexType<C>> RandomAccessibleInterval<C>
		fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.methods.FFTRAI.class)
	<T extends RealType<T>, C extends ComplexType<C>> RandomAccessibleInterval<C>
		fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.methods.FFTRAI.class)
	<T extends RealType<T>, C extends ComplexType<C>> RandomAccessibleInterval<C>
		fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf,
			final long... paddedSize);

	/** Executes the "fftsize" operation on the given arguments. */
	@OpMethod(op = Ops.FFTSize.class)
	Object fftsize(Object... args);

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = Ops.Gauss.class)
	Object gauss(Object... args);

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.gauss.GaussRAI2RAI.class)
	<T extends RealType<T>> RandomAccessibleInterval<T> gauss(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final double sigma);

	/** Executes the "gausskernel" operation on the given arguments. */
	@OpMethod(op = Ops.GaussKernel.class)
	Object gaussKernel(Object... args);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = Ops.Help.class)
	Object help(Object... args);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpOp.class)
	String help(final Op op);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	String help();

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	String help(final String name);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	String help(final String name, final Class<? extends Op> opType);

	/** Executes the "histogram" operation on the given arguments. */
	@OpMethod(op = Ops.Histogram.class)
	Object histogram(Object... args);

	/** Executes the "identity" operation on the given arguments. */
	@OpMethod(op = Ops.Identity.class)
	Object identity(Object... args);

	/** Executes the "identity" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.identity.DefaultIdentity.class)
	<A> A identity(final A arg);

	/** Executes the "ifft" operation on the given arguments. */
	@OpMethod(op = Ops.IFFT.class)
	Object ifft(Object... args);

	/** Executes the "ifft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.image.IFFTImg.class)
	<T extends RealType<T>, O extends Img<T>> Img<O> ifft(final Img<O> out,
		final Img<ComplexFloatType> in);

	/** Executes the "ifft" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.fft.methods.IFFTRAI.class)
	<C extends ComplexType<C>, T extends RealType<T>> RandomAccessibleInterval<T>
		ifft(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<C> in);

	/** Executes the "invert" operation on the given arguments. */
	@OpMethod(op = Ops.Invert.class)
	Object invert(Object... args);

	/** Executes the "invert" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.invert.InvertII.class)
	<I extends RealType<I>, O extends RealType<O>> IterableInterval<O> invert(
		final IterableInterval<O> out, final IterableInterval<I> in);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = Ops.Join.class)
	Object join(Object... args);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinFunctionAndFunction.class)
	<A, B, C> C join(final C out, final A in, final Function<A, B> first,
		final Function<B, C> second);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinFunctionAndFunction.class)
	<A, B, C> C join(final C out, final A in, final Function<A, B> first,
		final Function<B, C> second, final BufferFactory<A, B> bufferFactory);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceAndInplace.class)
	<A> A join(final A arg, final InplaceFunction<A> first,
		final InplaceFunction<A> second);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinFunctions.class)
	<A> A join(final A out, final A in,
		final List<? extends Function<A, A>> functions,
		final BufferFactory<A, A> bufferFactory);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceFunctions.class)
	<A> A join(final A arg, final List<InplaceFunction<A>> functions);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceAndFunction.class)
	<A, B> B join(final B out, final A in, final InplaceFunction<A> first,
		final Function<A, B> second);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceAndFunction.class)
	<A, B> B join(final B out, final A in, final InplaceFunction<A> first,
		final Function<A, B> second, final BufferFactory<A, A> bufferFactory);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinFunctionAndInplace.class)
	<A, B> B join(final B out, final A in, final Function<A, B> first,
		final InplaceFunction<B> second);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinFunctionAndInplace.class)
	<A, B> B join(final B out, final A in, final Function<A, B> first,
		final InplaceFunction<B> second, final BufferFactory<A, B> bufferFactory);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(op = Ops.LogKernel.class)
	Object logKernel(Object... args);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.convolve.kernel.create.CreateSymmetricLogKernel.class)
	<T extends ComplexType<T>> Img<T> logkernel(final int numDimensions,
		final double sigma);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.convolve.kernel.create.CreateSymmetricLogKernel.class)
	<T extends ComplexType<T>> Img<T> logkernel(final Type<T> outType,
		final int numDimensions, final double sigma);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.convolve.kernel.create.CreateSymmetricLogKernel.class)
	<T extends ComplexType<T>> Img<T> logkernel(final Type<T> outType,
		final ImgFactory<T> fac, final int numDimensions, final double sigma);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.convolve.kernel.create.CreateSymmetricLogKernel.class)
	<T extends ComplexType<T>> Img<T> logkernel(final Type<T> outType,
		final ImgFactory<T> fac, final int numDimensions, final double sigma,
		final double... calibration);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.convolve.kernel.create.CreateLogKernel.class)
	<T extends ComplexType<T> & NativeType<T>> Img<T> logkernel(
		final double... sigma);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.convolve.kernel.create.CreateLogKernel.class)
	<T extends ComplexType<T> & NativeType<T>> Img<T> logkernel(
		final Type<T> outType, final double... sigma);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.convolve.kernel.create.CreateLogKernel.class)
	<T extends ComplexType<T> & NativeType<T>> Img<T> logkernel(
		final Type<T> outType, final ImgFactory<T> fac, final double... sigma);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.convolve.kernel.create.CreateLogKernel.class)
	<T extends ComplexType<T> & NativeType<T>> Img<T> logkernel(
		final Type<T> outType, final ImgFactory<T> fac, final double[] sigma,
		final double... calibration);

	/** Executes the "lookup" operation on the given arguments. */
	@OpMethod(op = Ops.Lookup.class)
	Object lookup(Object... args);

	/** Executes the "lookup" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.lookup.DefaultLookup.class)
	Op lookup(final String name, final Object... args);

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = Ops.Loop.class)
	Object loop(Object... args);

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.loop.DefaultLoopInplace.class)
	<I> I loop(final I arg, final Function<I, I> function, final int n);

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.loop.DefaultLoopFunction.class)
	<A> A loop(final A out, final A in, final Function<A, A> function,
		final BufferFactory<A, A> bufferFactory, final int n);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = Ops.Map.class)
	Object map(Object... args);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapRA2View.class)
	<A, B extends Type<B>> RandomAccessible<B> map(
		final RandomAccessible<A> input, final Function<A, B> function,
		final Type<B> type);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapII2View.class)
	<A, B extends Type<B>> IterableInterval<B> map(
		final IterableInterval<A> input, final Function<A, B> function,
		final Type<B> type);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapRAI2View.class)
	<A, B extends Type<B>> RandomAccessibleInterval<B> map(
		final RandomAccessibleInterval<A> input, final Function<A, B> function,
		final Type<B> type);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.ParallelMap.class)
	<A> IterableInterval<A> map(final IterableInterval<A> arg,
		final InplaceFunction<A> func);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.ParallelMapI2I.class,
		net.imagej.ops.map.MapII2II.class })
	<A, B> IterableInterval<B> map(final IterableInterval<B> out,
		final IterableInterval<A> in, final Function<A, B> func);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.ParallelMapI2R.class,
		net.imagej.ops.map.MapII2RAI.class })
	<A, B> RandomAccessibleInterval<B> map(final RandomAccessibleInterval<B> out,
		final IterableInterval<A> in, final Function<A, B> func);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapRAI2III.class)
	<A, B> IterableInterval<B> map(final IterableInterval<B> out,
		final RandomAccessibleInterval<A> in, final Function<A, B> func);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapI.class)
	<A> Iterable<A> map(final Iterable<A> arg, final InplaceFunction<A> func);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.neighborhood.MapNeighborhood.class)
	<I, O> RandomAccessibleInterval<O> map(final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape,
		final Function<Iterable<I>, O> func);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapI2I.class)
	<A, B> Iterable<B> map(final Iterable<B> out, final Iterable<A> in,
		final Function<A, B> func);

	/** Executes the "max" operation on the given arguments. */
	@OpMethod(op = Ops.Max.class)
	Object max(Object... args);

	/** Executes the "max" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.statistics.MaxRealType.class)
	<T extends RealType<T>> T max(final T out, final Iterable<T> in);

	/** Executes the "mean" operation on the given arguments. */
	@OpMethod(op = Ops.Mean.class)
	Object mean(Object... args);

	/** Executes the "mean" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.statistics.MeanRealType.class)
	<I extends RealType<I>, O extends RealType<O>> O mean(final O out,
		final Iterable<I> in);

	/** Executes the "mean" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.statistics.MeanRealType.class)
	<I extends RealType<I>, O extends RealType<O>> O mean(final O out,
		final Iterable<I> in, final Sum<Iterable<I>, O> sumFunc);

	/** Executes the "mean" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.statistics.MeanRealType.class)
	<I extends RealType<I>, O extends RealType<O>> O mean(final O out,
		final Iterable<I> in, final Sum<Iterable<I>, O> sumFunc,
		final Size<Iterable<I>> sizeFunc);

	/** Executes the "median" operation on the given arguments. */
	@OpMethod(op = Ops.Median.class)
	Object median(Object... args);

	/** Executes the "median" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.statistics.MedianRealType.class)
	<T extends RealType<T>> T median(final T out, final Iterable<T> in);

	/** Executes the "min" operation on the given arguments. */
	@OpMethod(op = Ops.Min.class)
	Object min(Object... args);

	/** Executes the "min" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.statistics.MinRealType.class)
	<T extends RealType<T>> T min(final T out, final Iterable<T> in);

	/** Executes the "minmax" operation on the given arguments. */
	@OpMethod(op = Ops.MinMax.class)
	Object minmax(Object... args);

	/** Executes the "minmax" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.misc.MinMaxRT.class)
	<T extends RealType<T>> List<T> minmax(final Iterable<T> img);

	/** Executes the "normalize" operation on the given arguments. */
	@OpMethod(op = Ops.Normalize.class)
	Object normalize(Object... args);

	/** Executes the "normalize" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.normalize.NormalizeII.class)
	<T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> out, final IterableInterval<T> in);

	/** Executes the "normalize" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.normalize.NormalizeRealType.class)
	<T extends RealType<T>> T normalize(final T out, final T in,
		final double oldMin, final double newMin, final double newMax,
		final double factor);

	/** Executes the "project" operation on the given arguments. */
	@OpMethod(op = Ops.Project.class)
	Object project(Object... args);

	/** Executes the "project" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.project.parallel.DefaultProjectP.class,
		net.imagej.ops.project.ProjectRAI2II.class })
	<T, V> IterableInterval<V> project(final IterableInterval<V> out,
		final RandomAccessibleInterval<T> in,
		final Function<Iterable<T>, V> method, final int dim);

	/** Executes the "quantile" operation on the given arguments. */
	@OpMethod(op = Ops.Quantile.class)
	Object quantile(Object... args);

	/** Executes the "scale" operation on the given arguments. */
	@OpMethod(op = Ops.Scale.class)
	Object scale(Object... args);

	/** Executes the "scale" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.scale.ScaleImg.class)
	<T extends RealType<T>> Img<T> scale(final Img<T> in,
		final double[] scaleFactors,
		final InterpolatorFactory<T, RandomAccessible<T>> interpolator);

	/** Executes the "size" operation on the given arguments. */
	@OpMethod(op = Ops.Size.class)
	Object size(Object... args);

	/** Executes the "size" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.misc.SizeIterableInterval.class)
	LongType size(final LongType out, final IterableInterval<?> in);

	/** Executes the "size" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.misc.SizeIterable.class)
	LongType size(final LongType out, final Iterable<?> in);

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = Ops.Slicewise.class)
	Object slicewise(Object... args);

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.slicer.SlicewiseRAI2RAI.class)
	<I, O> RandomAccessibleInterval<O> slicewise(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final Function<I, O> func,
		final int... axisIndices);

	/** Executes the "stddev" operation on the given arguments. */
	@OpMethod(op = Ops.StdDeviation.class)
	Object stddev(Object... args);

	/** Executes the "sum" operation on the given arguments. */
	@OpMethod(op = Ops.Sum.class)
	Object sum(Object... args);

	/** Executes the "threshold" operation on the given arguments. */
	@OpMethod(op = Ops.Threshold.class)
	Object threshold(Object... args);

	/** Executes the "variance" operation on the given arguments. */
	@OpMethod(op = Ops.Variance.class)
	Object variance(Object... args);

	// -- Operation shortcuts - other namespaces --

	/** Gateway into ops of the "logic" namespace. */
	LogicNamespace logic();

	/** Gateway into ops of the "math" namespace. */
	MathNamespace math();

	/** Gateway into ops of the "threshold" namespace. */
	ThresholdNamespace threshold();

	// -- Deprecated methods --

	/** @deprecated Use {@link #createimg} instead. */
	@Deprecated
	Object create(Object... args);

}
