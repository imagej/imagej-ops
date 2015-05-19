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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.imagej.ImgPlus;
import net.imagej.ops.create.CreateEmptyImgCopy;
import net.imagej.ops.create.CreateEmptyImgPlusCopy;
import net.imagej.ops.create.CreateImgDifferentNativeType;
import net.imagej.ops.create.CreateImgNativeType;
import net.imagej.ops.create.DefaultCreateImg;
import net.imagej.ops.logic.LogicNamespace;
import net.imagej.ops.math.MathNamespace;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imglib2.Dimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

import org.scijava.command.CommandInfo;
import org.scijava.command.CommandService;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.ModuleService;
import org.scijava.plugin.AbstractPTService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Default service for managing and executing {@link Op}s.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Service.class)
public class DefaultOpService extends AbstractPTService<Op> implements
	OpService
{

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private CommandService commandService;

	@Parameter
	private OpMatchingService matcher;

	@Parameter
	private LogService log;

	private LogicNamespace logic;
	private MathNamespace math;
	private ThresholdNamespace threshold;
	private boolean namespacesReady;

	// -- OpService methods --

	@Override
	public Object run(final String name, final Object... args) {
		final Module module = module(name, args);
		return run(module);
	}

	@Override
	public <OP extends Op> Object run(final Class<OP> type, final Object... args)
	{
		final Module module = module(type, args);
		return run(module);
	}

	@Override
	public Object run(final Op op, final Object... args) {
		return run(module(op, args));
	}

	@Override
	public Op op(final String name, final Object... args) {
		final Module module = module(name, args);
		if (module == null) return null;
		return (Op) module.getDelegateObject();
	}

	@Override
	public <OP extends Op> OP op(final Class<OP> type, final Object... args) {
		final Module module = module(type, args);
		if (module == null) return null;
		@SuppressWarnings("unchecked")
		final OP op = (OP) module.getDelegateObject();
		return op;
	}

	@Override
	public Module module(final String name, final Object... args) {
		return matcher.findModule(new OpRef<Op>(name, args));
	}

	@Override
	public <OP extends Op> Module module(final Class<OP> type,
		final Object... args)
	{
		return matcher.findModule(new OpRef<OP>(type, args));
	}

	@Override
	public Module module(final Op op, final Object... args) {
		final Module module = info(op).createModule(op);
		getContext().inject(module.getDelegateObject());
		return matcher.assignInputs(module, args);
	}

	@Override
	public CommandInfo info(final Op op) {
		return commandService.getCommand(op.getClass());
	}

	@Override
	public Collection<String> ops() {
		// collect list of unique operation names
		final HashSet<String> operations = new HashSet<String>();
		for (final CommandInfo info : matcher.getOps()) {
			final String name = info.getName();
			if (name != null && !name.isEmpty()) operations.add(info.getName());
		}

		// convert the set into a sorted list
		final ArrayList<String> sorted = new ArrayList<String>(operations);
		Collections.sort(sorted);
		return sorted;
	}

	// -- Operation shortcuts - global namespace --

	@Override
	public Object ascii(final Object... args) {
		return run(Ops.ASCII.NAME, args);
	}

	@Override
	public <T extends RealType<T>> String ascii(final IterableInterval<T> image) {
		final String result =
			(String) run(net.imagej.ops.ascii.DefaultASCII.class, image);
		return result;
	}

	@Override
	public <T extends RealType<T>> String ascii(final IterableInterval<T> image,
		final RealType<T> min)
	{
		final String result =
			(String) run(net.imagej.ops.ascii.DefaultASCII.class, image, min);
		return result;
	}

	@Override
	public <T extends RealType<T>> String ascii(final IterableInterval<T> image,
		final RealType<T> min, final RealType<T> max)
	{
		final String result =
			(String) run(net.imagej.ops.ascii.DefaultASCII.class, image, min, max);
		return result;
	}

	@Override
	public Object chunker(final Object... args) {
		return run(Ops.Chunker.NAME, args);
	}

	@Override
	public Object convert(final Object... args) {
		return run(Ops.Convert.NAME, args);
	}

	@Override
	public Object convolve(final Object... args) {
		return run(Ops.Convolve.NAME, args);
	}

	@Override
	public Object correlate(final Object... args) {
		return run(Ops.Correlate.NAME, args);
	}

	@Override
	public Object createimg(final Object... args) {
		return run(Ops.CreateImg.NAME, args);
	}

	@Override
	public <V extends NativeType<V>> ImgPlus<V> createimg(final ImgPlus<V> input)
	{
		@SuppressWarnings("unchecked")
		final ImgPlus<V> result =
			(ImgPlus<V>) run(CreateEmptyImgPlusCopy.class, input);
		return result;
	}

	@Override
	public <V extends NativeType<V>> Img<V> createimg(final Img<V> input,
		final V type)
	{
		@SuppressWarnings("unchecked")
		final Img<V> result =
			(Img<V>) run(CreateImgDifferentNativeType.class, input, type);
		return result;
	}

	@Override
	public <V extends NativeType<V>> Img<V> createimg(final ImgFactory<V> fac,
		final V outType, final Dimensions dims)
	{
		@SuppressWarnings("unchecked")
		final Img<V> result =
			(Img<V>) run(CreateImgNativeType.class, fac, outType, dims);
		return result;
	}

	@Override
	public <V extends Type<V>> Img<V> createimg(final long... dims) {
		@SuppressWarnings("unchecked")
		final Img<V> result = (Img<V>) run(DefaultCreateImg.class, dims);
		return result;
	}

	@Override
	public <V extends Type<V>> Img<V> createimg(final V outType,
		final long... dims)
	{
		@SuppressWarnings("unchecked")
		final Img<V> result = (Img<V>) run(DefaultCreateImg.class, outType, dims);
		return result;
	}

	@Override
	public <V extends Type<V>> Img<V> createimg(final V outType,
		final ImgFactory<V> fac, final long... dims)
	{
		@SuppressWarnings("unchecked")
		final Img<V> result =
			(Img<V>) run(DefaultCreateImg.class, outType, fac, dims);
		return result;
	}

	@Override
	public <V extends NativeType<V>> Img<V> createimg(final Img<V> input) {
		@SuppressWarnings("unchecked")
		final Img<V> result = (Img<V>) run(CreateEmptyImgCopy.class, input);
		return result;
	}

	@Override
	public Object crop(final Object... args) {
		return run(Ops.Crop.NAME, args);
	}

	@Override
	public Object deconvolve(final Object... args) {
		return run(Ops.Deconvolve.NAME, args);
	}

	@Override
	public Object equation(final Object... args) {
		return run(Ops.Equation.NAME, args);
	}

	@Override
	public <T extends RealType<T>> IterableInterval<T> equation(final String in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) run(net.imagej.ops.equation.DefaultEquation.class,
				in);
		return result;
	}

	@Override
	public <T extends RealType<T>> IterableInterval<T> equation(
		final IterableInterval<T> out, final String in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) run(net.imagej.ops.equation.DefaultEquation.class,
				out, in);
		return result;
	}

	@Override
	public Object eval(final Object... args) {
		return run(Ops.Eval.NAME, args);
	}

	@Override
	public Object eval(final String expression) {
		final Object result =
			run(net.imagej.ops.eval.DefaultEval.class, expression);
		return result;
	}

	@Override
	public Object eval(final String expression, final Map<String, Object> vars) {
		final Object result =
			run(net.imagej.ops.eval.DefaultEval.class, expression, vars);
		return result;
	}

	@Override
	public Object fft(final Object... args) {
		return run("fft", args);
	}

	@Override
	public <T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<I> in)
	{
		@SuppressWarnings("unchecked")
		final Img<ComplexFloatType> result =
			(Img<ComplexFloatType>) run(net.imagej.ops.fft.image.FFTImg.class, in);
		return result;
	}

	@Override
	public <T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<ComplexFloatType> out, final Img<I> in)
	{
		@SuppressWarnings("unchecked")
		final Img<ComplexFloatType> result =
			(Img<ComplexFloatType>) run(net.imagej.ops.fft.image.FFTImg.class, out,
				in);
		return result;
	}

	@Override
	public <T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<ComplexFloatType> out, final Img<I> in, final long... borderSize)
	{
		@SuppressWarnings("unchecked")
		final Img<ComplexFloatType> result =
			(Img<ComplexFloatType>) run(net.imagej.ops.fft.image.FFTImg.class, out,
				in, borderSize);
		return result;
	}

	@Override
	public <T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<ComplexFloatType> out, final Img<I> in, final long[] borderSize,
		final Boolean fast)
	{
		@SuppressWarnings("unchecked")
		final Img<ComplexFloatType> result =
			(Img<ComplexFloatType>) run(net.imagej.ops.fft.image.FFTImg.class, out,
				in, borderSize, fast);
		return result;
	}

	@Override
	public <T extends RealType<T>, I extends Img<T>> Img<ComplexFloatType> fft(
		final Img<ComplexFloatType> out, final Img<I> in, final long[] borderSize,
		final Boolean fast,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf)
	{
		@SuppressWarnings("unchecked")
		final Img<ComplexFloatType> result =
			(Img<ComplexFloatType>) run(net.imagej.ops.fft.image.FFTImg.class, out,
				in, borderSize, fast, obf);
		return result;
	}

	@Override
	public <T extends RealType<T>, C extends ComplexType<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) run(
				net.imagej.ops.fft.methods.FFTRAI.class, out, in);
		return result;
	}

	@Override
	public <T extends RealType<T>, C extends ComplexType<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) run(
				net.imagej.ops.fft.methods.FFTRAI.class, out, in, obf);
		return result;
	}

	@Override
	public <T extends RealType<T>, C extends ComplexType<C>>
		RandomAccessibleInterval<C> fft(final RandomAccessibleInterval<C> out,
			final RandomAccessibleInterval<T> in,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf,
			final long... paddedSize)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<C> result =
			(RandomAccessibleInterval<C>) run(
				net.imagej.ops.fft.methods.FFTRAI.class, out, in, obf, paddedSize);
		return result;
	}

	@Override
	public Object fftsize(final Object... args) {
		return run(Ops.FFTSize.NAME, args);
	}

	@Override
	public Object gauss(final Object... args) {
		return run(Ops.Gauss.NAME, args);
	}

	@Override
	public <T extends RealType<T>> RandomAccessibleInterval<T> gauss(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) run(
				net.imagej.ops.gauss.GaussRAI2RAI.class, out, in, sigma);
		return result;
	}

	@Override
	public Object gaussKernel(final Object... args) {
		return run("gausskernel", args);
	}

	@Override
	public Object help(final Object... args) {
		return run(Ops.Help.NAME, args);
	}

	@Override
	public String help(final Op op) {
		final String result = (String) run(net.imagej.ops.help.HelpOp.class, op);
		return result;
	}

	@Override
	public String help() {
		final String result =
			(String) run(net.imagej.ops.help.HelpCandidates.class);
		return result;
	}

	@Override
	public String help(final String name) {
		final String result =
			(String) run(net.imagej.ops.help.HelpCandidates.class, name);
		return result;
	}

	@Override
	public String help(final String name, final Class<? extends Op> opType) {
		final String result =
			(String) run(net.imagej.ops.help.HelpCandidates.class, name, opType);
		return result;
	}

	@Override
	public Object histogram(final Object... args) {
		return run(Ops.Histogram.NAME, args);
	}

	@Override
	public Object identity(final Object... args) {
		return run(Ops.Identity.NAME, args);
	}

	@Override
	public <A> A identity(final A arg) {
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.identity.DefaultIdentity.class, arg);
		return result;
	}

	@Override
	public Object ifft(final Object... args) {
		return run("ifft", args);
	}

	@Override
	public <T extends RealType<T>, O extends Img<T>> Img<O> ifft(
		final Img<O> out, final Img<ComplexFloatType> in)
	{
		@SuppressWarnings("unchecked")
		final Img<O> result =
			(Img<O>) run(net.imagej.ops.fft.image.IFFTImg.class, out, in);
		return result;
	}

	@Override
	public <C extends ComplexType<C>, T extends RealType<T>>
		RandomAccessibleInterval<T> ifft(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<C> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) run(
				net.imagej.ops.fft.methods.IFFTRAI.class, out, in);
		return result;
	}

	@Override
	public Object invert(final Object... args) {
		return run(Ops.Invert.NAME, args);
	}

	@Override
	public <I extends RealType<I>, O extends RealType<O>> IterableInterval<O>
		invert(final IterableInterval<O> out, final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) run(net.imagej.ops.invert.InvertII.class, out, in);
		return result;
	}

	@Override
	public Object join(final Object... args) {
		return run(Ops.Join.NAME, args);
	}

	@Override
	public <A, B, C> C join(final C out, final A in, final Function<A, B> first,
		final Function<B, C> second)
	{
		@SuppressWarnings("unchecked")
		final C result =
			(C) run(net.imagej.ops.join.DefaultJoinFunctionAndFunction.class, out,
				in, first, second);
		return result;
	}

	@Override
	public <A, B, C> C join(final C out, final A in, final Function<A, B> first,
		final Function<B, C> second, final BufferFactory<A, B> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final C result =
			(C) run(net.imagej.ops.join.DefaultJoinFunctionAndFunction.class, out,
				in, first, second, bufferFactory);
		return result;
	}

	@Override
	public <A> A join(final A arg, final InplaceFunction<A> first,
		final InplaceFunction<A> second)
	{
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.join.DefaultJoinInplaceAndInplace.class, arg,
				first, second);
		return result;
	}

	@Override
	public <A> A join(final A out, final A in,
		final List<? extends Function<A, A>> functions,
		final BufferFactory<A, A> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.join.DefaultJoinFunctions.class, out, in,
				functions, bufferFactory);
		return result;
	}

	@Override
	public <A> A join(final A arg, final List<InplaceFunction<A>> functions) {
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.join.DefaultJoinInplaceFunctions.class, arg,
				functions);
		return result;
	}

	@Override
	public <A, B> B join(final B out, final A in, final InplaceFunction<A> first,
		final Function<A, B> second)
	{
		@SuppressWarnings("unchecked")
		final B result =
			(B) run(net.imagej.ops.join.DefaultJoinInplaceAndFunction.class, out, in,
				first, second);
		return result;
	}

	@Override
	public <A, B> B join(final B out, final A in, final InplaceFunction<A> first,
		final Function<A, B> second, final BufferFactory<A, A> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final B result =
			(B) run(net.imagej.ops.join.DefaultJoinInplaceAndFunction.class, out, in,
				first, second, bufferFactory);
		return result;
	}

	@Override
	public <A, B> B join(final B out, final A in, final Function<A, B> first,
		final InplaceFunction<B> second)
	{
		@SuppressWarnings("unchecked")
		final B result =
			(B) run(net.imagej.ops.join.DefaultJoinFunctionAndInplace.class, out, in,
				first, second);
		return result;
	}

	@Override
	public <A, B> B join(final B out, final A in, final Function<A, B> first,
		final InplaceFunction<B> second, final BufferFactory<A, B> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final B result =
			(B) run(net.imagej.ops.join.DefaultJoinFunctionAndInplace.class, out, in,
				first, second, bufferFactory);
		return result;
	}

	@Override
	public Object logKernel(final Object... args) {
		return run("logkernel", args);
	}

	@Override
	public <T extends ComplexType<T>> Img<T> logkernel(final int numDimensions,
		final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) run(
				net.imagej.ops.convolve.kernel.create.CreateSymmetricLogKernel.class,
				numDimensions, sigma);
		return result;
	}

	@Override
	public <T extends ComplexType<T>> Img<T> logkernel(final Type<T> outType,
		final int numDimensions, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) run(
				net.imagej.ops.convolve.kernel.create.CreateSymmetricLogKernel.class,
				outType, numDimensions, sigma);
		return result;
	}

	@Override
	public <T extends ComplexType<T>> Img<T> logkernel(final Type<T> outType,
		final ImgFactory<T> fac, final int numDimensions, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) run(
				net.imagej.ops.convolve.kernel.create.CreateSymmetricLogKernel.class,
				outType, fac, numDimensions, sigma);
		return result;
	}

	@Override
	public <T extends ComplexType<T>> Img<T> logkernel(final Type<T> outType,
		final ImgFactory<T> fac, final int numDimensions, final double sigma,
		final double... calibration)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) run(
				net.imagej.ops.convolve.kernel.create.CreateSymmetricLogKernel.class,
				outType, fac, numDimensions, sigma, calibration);
		return result;
	}

	@Override
	public <T extends ComplexType<T> & NativeType<T>> Img<T> logkernel(
		final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) run(net.imagej.ops.convolve.kernel.create.CreateLogKernel.class,
				sigma);
		return result;
	}

	@Override
	public <T extends ComplexType<T> & NativeType<T>> Img<T> logkernel(
		final Type<T> outType, final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) run(net.imagej.ops.convolve.kernel.create.CreateLogKernel.class,
				outType, sigma);
		return result;
	}

	@Override
	public <T extends ComplexType<T> & NativeType<T>> Img<T> logkernel(
		final Type<T> outType, final ImgFactory<T> fac, final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) run(net.imagej.ops.convolve.kernel.create.CreateLogKernel.class,
				outType, fac, sigma);
		return result;
	}

	@Override
	public <T extends ComplexType<T> & NativeType<T>> Img<T> logkernel(
		final Type<T> outType, final ImgFactory<T> fac, final double[] sigma,
		final double... calibration)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) run(net.imagej.ops.convolve.kernel.create.CreateLogKernel.class,
				outType, fac, sigma, calibration);
		return result;
	}

	@Override
	public Object lookup(final Object... args) {
		return run(Ops.Lookup.NAME, args);
	}

	@Override
	public Op lookup(final String name, final Object... args) {
		final Op result =
			(Op) run(net.imagej.ops.lookup.DefaultLookup.class, name, args);
		return result;
	}

	@Override
	public Object loop(final Object... args) {
		return run(Ops.Loop.NAME, args);
	}

	@Override
	public Object map(final Object... args) {
		return run(Ops.Map.NAME, args);
	}

	@Override
	public Object max(final Object... args) {
		return run(Ops.Max.NAME, args);
	}

	@Override
	public Object mean(final Object... args) {
		return run(Ops.Mean.NAME, args);
	}

	@Override
	public Object median(final Object... args) {
		return run(Ops.Median.NAME, args);
	}

	@Override
	public Object min(final Object... args) {
		return run(Ops.Min.NAME, args);
	}

	@Override
	public Object minmax(final Object... args) {
		return run(Ops.MinMax.NAME, args);
	}

	@Override
	public Object normalize(final Object... args) {
		return run(Ops.Normalize.NAME, args);
	}

	@Override
	public Object project(final Object... args) {
		return run(Ops.Project.NAME, args);
	}

	@Override
	public Object quantile(final Object... args) {
		return run(Ops.Quantile.NAME, args);
	}

	@Override
	public Object scale(final Object... args) {
		return run(Ops.Scale.NAME, args);
	}

	@Override
	public Object size(final Object... args) {
		return run(Ops.Size.NAME, args);
	}

	@Override
	public Object slicewise(final Object... args) {
		return run(Ops.Slicewise.NAME, args);
	}

	@Override
	public Object stddev(final Object... args) {
		return run(Ops.StdDeviation.NAME, args);
	}

	@Override
	public Object sum(final Object... args) {
		return run(Ops.Sum.NAME, args);
	}

	@Override
	public Object threshold(final Object... args) {
		return run(Ops.Threshold.NAME, args);
	}

	@Override
	public Object variance(final Object... args) {
		return run(Ops.Variance.NAME, args);
	}

	// -- Operation shortcuts - other namespaces --

	@Override
	public LogicNamespace logic() {
		if (!namespacesReady) initNamespaces();
		return logic;
	}

	@Override
	public MathNamespace math() {
		if (!namespacesReady) initNamespaces();
		return math;
	}

	@Override
	public ThresholdNamespace threshold() {
		if (!namespacesReady) initNamespaces();
		return threshold;
	}

	// -- SingletonService methods --

	@Override
	public Class<Op> getPluginType() {
		return Op.class;
	}

	// -- Helper methods --

	private Object run(final Module module) {
		module.run();
		return result(module);
	}

	private Object result(final Module module) {
		final List<Object> outputs = new ArrayList<Object>();
		for (final ModuleItem<?> output : module.getInfo().outputs()) {
			final Object value = output.getValue(module);
			outputs.add(value);
		}
		return outputs.size() == 1 ? outputs.get(0) : outputs;
	}

	// -- Helper methods - lazy initialization --

	private synchronized void initNamespaces() {
		if (namespacesReady) return;
		logic = new LogicNamespace();
		getContext().inject(logic);
		math = new MathNamespace();
		getContext().inject(math);
		threshold = new ThresholdNamespace();
		getContext().inject(threshold);
		namespacesReady = true;
	}

	// -- Deprecated methods --

	@Deprecated
	@Override
	public Object create(final Object... args) {
		return createimg(args);
	}
}
