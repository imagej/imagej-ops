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

import net.imagej.ops.convert.ConvertPix;
import net.imagej.ops.create.CreateNamespace;
import net.imagej.ops.deconvolve.DeconvolveNamespace;
import net.imagej.ops.filter.FilterNamespace;
import net.imagej.ops.image.ImageNamespace;
import net.imagej.ops.labeling.LabelingNamespace;
import net.imagej.ops.logic.LogicNamespace;
import net.imagej.ops.math.MathNamespace;
import net.imagej.ops.stats.StatsNamespace;
import net.imagej.ops.thread.ThreadNamespace;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;

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
	private NamespaceService namespaceService;

	@Parameter
	private LogService log;

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

	@Override
	public <NS extends Namespace> NS namespace(final Class<NS> nsClass) {
		return namespaceService.getInstance(nsClass);
	}

	// -- Operation shortcuts - global namespace --

	@Override
	public Object convert(final Object... args) {
		return run(Ops.Convert.NAME, args);
	}

	@Override
	public <I extends RealType<I>, O extends RealType<O>> O convert(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) run(Ops.Convert.NAME, out, in);
		return result;
	}

	@Override
	public <I extends RealType<I>, O extends RealType<O>> IterableInterval<O>
		convert(final IterableInterval<O> out, final IterableInterval<I> in,
			final ConvertPix<I, O> pixConvert)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) run(net.imagej.ops.convert.ConvertIterableInterval.class, out,
				in, pixConvert);
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
	public Object help(final Object... args) {
		return run(Ops.Help.NAME, args);
	}

	@Override
	public String help(final Op op) {
		final String result = (String) run(net.imagej.ops.help.HelpForOp.class, op);
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
	public Object join(final Object... args) {
		return run(Ops.Join.NAME, args);
	}

	@Override
	public <A, B, C> C join(final C out, final A in, final ComputerOp<A, B> first,
		final ComputerOp<B, C> second)
	{
		@SuppressWarnings("unchecked")
		final C result =
			(C) run(net.imagej.ops.join.DefaultJoinComputerAndComputer.class, out,
				in, first, second);
		return result;
	}

	@Override
	public <A, B, C> C join(final C out, final A in, final ComputerOp<A, B> first,
		final ComputerOp<B, C> second, final BufferFactory<A, B> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final C result =
			(C) run(net.imagej.ops.join.DefaultJoinComputerAndComputer.class, out,
				in, first, second, bufferFactory);
		return result;
	}

	@Override
	public <A> A join(final A arg, final InplaceOp<A> first,
		final InplaceOp<A> second)
	{
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.join.DefaultJoinInplaceAndInplace.class, arg,
				first, second);
		return result;
	}

	@Override
	public <A> A join(final A out, final A in,
		final List<? extends ComputerOp<A, A>> functions,
		final BufferFactory<A, A> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.join.DefaultJoinComputers.class, out, in,
				functions, bufferFactory);
		return result;
	}

	@Override
	public <A> A join(final A arg, final List<InplaceOp<A>> functions) {
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.join.DefaultJoinInplaces.class, arg,
				functions);
		return result;
	}

	@Override
	public <A, B> B join(final B out, final A in, final InplaceOp<A> first,
		final ComputerOp<A, B> second)
	{
		@SuppressWarnings("unchecked")
		final B result =
			(B) run(net.imagej.ops.join.DefaultJoinInplaceAndComputer.class, out, in,
				first, second);
		return result;
	}

	@Override
	public <A, B> B join(final B out, final A in, final InplaceOp<A> first,
		final ComputerOp<A, B> second, final BufferFactory<A, A> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final B result =
			(B) run(net.imagej.ops.join.DefaultJoinInplaceAndComputer.class, out, in,
				first, second, bufferFactory);
		return result;
	}

	@Override
	public <A, B> B join(final B out, final A in, final ComputerOp<A, B> first,
		final InplaceOp<B> second)
	{
		@SuppressWarnings("unchecked")
		final B result =
			(B) run(net.imagej.ops.join.DefaultJoinComputerAndInplace.class, out, in,
				first, second);
		return result;
	}

	@Override
	public <A, B> B join(final B out, final A in, final ComputerOp<A, B> first,
		final InplaceOp<B> second, final BufferFactory<A, B> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final B result =
			(B) run(net.imagej.ops.join.DefaultJoinComputerAndInplace.class, out, in,
				first, second, bufferFactory);
		return result;
	}

	@Override
	public Object loop(final Object... args) {
		return run(Ops.Loop.NAME, args);
	}

	@Override
	public <I> I loop(final I arg, final ComputerOp<I, I> function, final int n) {
		@SuppressWarnings("unchecked")
		final I result =
			(I) run(net.imagej.ops.loop.DefaultLoopInplace.class, arg, function, n);
		return result;
	}

	@Override
	public <A> A loop(final A out, final A in, final ComputerOp<A, A> function,
		final BufferFactory<A, A> bufferFactory, final int n)
	{
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.loop.DefaultLoopComputer.class, out, in, function,
				bufferFactory, n);
		return result;
	}

	@Override
	public Object map(final Object... args) {
		return run(Ops.Map.NAME, args);
	}

	@Override
	public <A, B extends Type<B>> RandomAccessibleInterval<B> map(
		final RandomAccessibleInterval<A> input, final ComputerOp<A, B> function,
		final B type)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<B> result =
			(RandomAccessibleInterval<B>) run(
				net.imagej.ops.map.MapConvertRAIToRAI.class, input, function, type);
		return result;
	}

	@Override
	public <A, B extends Type<B>> RandomAccessible<B>
		map(final RandomAccessible<A> input, final ComputerOp<A, B> function,
			final B type)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessible<B> result =
			(RandomAccessible<B>) run(
				net.imagej.ops.map.MapConvertRandomAccessToRandomAccess.class, input,
				function, type);
		return result;
	}

	@Override
	public <A, B extends Type<B>> IterableInterval<B>
		map(final IterableInterval<A> input, final ComputerOp<A, B> function,
			final B type)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<B> result =
			(IterableInterval<B>) run(
				net.imagej.ops.map.MapIterableIntervalToView.class, input, function,
				type);
		return result;
	}

	@Override
	public <A> IterableInterval<A> map(final IterableInterval<A> arg,
		final InplaceOp<A> func)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<A> result =
			(IterableInterval<A>) run(net.imagej.ops.map.MapParallel.class, arg, func);
		return result;
	}

	@Override
	public <A, B> IterableInterval<B> map(final IterableInterval<B> out,
		final IterableInterval<A> in, final ComputerOp<A, B> func)
	{
		// net.imagej.ops.map.MapIterableToIterableParallel.class
		// net.imagej.ops.map.MapIterableIntervalToIterableInterval.class
		@SuppressWarnings("unchecked")
		final IterableInterval<B> result =
			(IterableInterval<B>) run(net.imagej.ops.Ops.Map.class, out, in, func);
		return result;
	}

	@Override
	public <A, B> RandomAccessibleInterval<B> map(
		final RandomAccessibleInterval<B> out, final IterableInterval<A> in,
		final ComputerOp<A, B> func)
	{
		// net.imagej.ops.map.MapIterableToRAIParallel.class
		// net.imagej.ops.map.MapIterableIntervalToRAI.class
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<B> result =
			(RandomAccessibleInterval<B>) run(net.imagej.ops.Ops.Map.class, out, in,
				func);
		return result;
	}

	@Override
	public <A> Iterable<A> map(final Iterable<A> arg,
		final InplaceOp<A> func)
	{
		@SuppressWarnings("unchecked")
		final Iterable<A> result =
			(Iterable<A>) run(net.imagej.ops.map.MapIterableInplace.class, arg, func);
		return result;
	}

	@Override
	public <A, B> IterableInterval<B> map(final IterableInterval<B> out,
		final RandomAccessibleInterval<A> in, final ComputerOp<A, B> func)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<B> result =
			(IterableInterval<B>) run(
				net.imagej.ops.map.MapRAIToIterableInterval.class, out, in, func);
		return result;
	}

	@Override
	public <I, O> RandomAccessibleInterval<O> map(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in,
		final ComputerOp<Iterable<I>, O> func, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.map.MapNeighborhood.class, out, in, func, shape);
		return result;
	}

	@Override
	public <A, B> Iterable<B> map(final Iterable<B> out, final Iterable<A> in,
		final ComputerOp<A, B> func)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result =
			(Iterable<B>) run(net.imagej.ops.map.MapIterableToIterable.class, out,
				in, func);
		return result;
	}

	@Override
	public Object slicewise(final Object... args) {
		return run(Ops.Slicewise.NAME, args);
	}

	@Override
	public <I, O> RandomAccessibleInterval<O> slicewise(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final ComputerOp<I, O> func,
		final int... axisIndices)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.slicewise.SlicewiseRAI2RAI.class, out, in, func,
				axisIndices);
		return result;
	}

	@Override
	public <I, O> RandomAccessibleInterval<O> slicewise(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final ComputerOp<I, O> func,
		final int[] axisIndices, final boolean dropSingleDimensions)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.slicewise.SlicewiseRAI2RAI.class, out, in, func,
				axisIndices, dropSingleDimensions);
		return result;
	}

	// -- Operation shortcuts - other namespaces --

	@Override
	public CreateNamespace create() {
		return namespace(CreateNamespace.class);
	}

	@Override
	public DeconvolveNamespace deconvolve() {
		return namespace(DeconvolveNamespace.class);
	}

	@Override
	public FilterNamespace filter() {
		return namespace(FilterNamespace.class);
	}

	@Override
	public ImageNamespace image() {
		return namespace(ImageNamespace.class);
	}

	@Override
	public LabelingNamespace labeling() {
		return namespace(LabelingNamespace.class);
	}

	@Override
	public LogicNamespace logic() {
		return namespace(LogicNamespace.class);
	}

	@Override
	public MathNamespace math() {
		return namespace(MathNamespace.class);
	}

	@Override
	public StatsNamespace stats() {
		return namespace(StatsNamespace.class);
	}

	@Override
	public ThreadNamespace thread() {
		return namespace(ThreadNamespace.class);
	}

	@Override
	public ThresholdNamespace threshold() {
		return namespace(ThresholdNamespace.class);
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

}
