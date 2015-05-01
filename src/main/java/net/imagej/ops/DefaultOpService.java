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
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;

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
	public Object correlate(Object... args) {
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
	public Object eval(final Object... args) {
		return run(Ops.Eval.NAME, args);
	}

	@Override
	public Object fft(final Object... args) {
		return run("fft", args);
	}

	@Override
	public Object fftsize(Object... args) {
		return run(Ops.FFTSize.NAME, args);
	}

	@Override
	public Object gauss(final Object... args) {
		return run(Ops.Gauss.NAME, args);
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
	public Object histogram(final Object... args) {
		return run(Ops.Histogram.NAME, args);
	}

	@Override
	public Object identity(final Object... args) {
		return run(Ops.Identity.NAME, args);
	}

	@Override
	public Object ifft(final Object... args) {
		return run("ifft", args);
	}

	@Override
	public Object invert(final Object... args) {
		return run(Ops.Invert.NAME, args);
	}

	@Override
	public Object join(final Object... args) {
		return run(Ops.Join.NAME, args);
	}

	@Override
	public Object log(Object... args) {
		return run(Ops.Log.NAME, args);
	}

	@Override
	public Object logKernel(final Object... args) {
		return run("logkernel", args);
	}

	@Override
	public Object lookup(final Object... args) {
		return run(Ops.Lookup.NAME, args);
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
