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

import net.imagej.ops.convert.ConvertNamespace;
import net.imagej.ops.create.CreateNamespace;
import net.imagej.ops.deconvolve.DeconvolveNamespace;
import net.imagej.ops.features.haralick.HaralickNamespace;
import net.imagej.ops.features.zernike.ZernikeNamespace;
import net.imagej.ops.features.tamura2d.TamuraNamespace;
import net.imagej.ops.filter.FilterNamespace;
import net.imagej.ops.geometric.Geometric2DNamespace;
import net.imagej.ops.geometric3d.Geometric3DNamespace;
import net.imagej.ops.image.ImageNamespace;
import net.imagej.ops.imagemoments.ImageMomentsNamespace;
import net.imagej.ops.labeling.LabelingNamespace;
import net.imagej.ops.logic.LogicNamespace;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.math.MathNamespace;
import net.imagej.ops.stats.StatsNamespace;
import net.imagej.ops.thread.ThreadNamespace;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.Type;

import org.scijava.AbstractContextual;
import org.scijava.command.CommandInfo;
import org.scijava.command.CommandService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;

/**
 * Abstract superclass for {@link OpEnvironment} implementations.
 *
 * @author Curtis Rueden
 */
public abstract class AbstractOpEnvironment extends AbstractContextual
	implements OpEnvironment
{

	@Parameter
	private CommandService commandService;

	@Parameter
	private OpMatchingService matcher;

	@Parameter
	private NamespaceService namespaceService;

	// -- OpEnvironment methods --

	@Override
	public Object run(final String name, final Object... args) {
		return run(module(name, args));
	}

	@Override
	public <OP extends Op> Object run(final Class<OP> type, final Object... args)
	{
		return run(module(type, args));
	}

	@Override
	public Object run(final Op op, final Object... args) {
		return run(module(op, args));
	}

	@Override
	public Op op(final String name, final Object... args) {
		return OpUtils.unwrap(module(name, args), Op.class, null);
	}

	@Override
	public <OP extends Op> OP op(final Class<OP> type, final Object... args) {
		return OpUtils.unwrap(module(type, args), type, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, O, OP extends Op> ComputerOp<I, O> computer(
		final Class<OP> opType, final Class<O> outType, final Class<I> inType,
		final Object... otherArgs)
	{
		final Object[] args = args2(outType, inType, otherArgs);
		return (ComputerOp<I, O>) specialOp(opType, ComputerOp.class, null, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, O, OP extends Op> ComputerOp<I, O> computer(
		final Class<OP> opType, final Class<O> outType, final I in,
		final Object... otherArgs)
	{
		final Object[] args = args2(outType, in, otherArgs);
		return (ComputerOp<I, O>) specialOp(opType, ComputerOp.class, null, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, O, OP extends Op> ComputerOp<I, O> computer(
		final Class<OP> opType, final O out, final I in, final Object... otherArgs)
	{
		final Object[] args = args2(out, in, otherArgs);
		return (ComputerOp<I, O>) specialOp(opType, ComputerOp.class, null, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, O, OP extends Op> FunctionOp<I, O> function(
		final Class<OP> opType, final Class<O> outType, final Class<I> inType,
		final Object... otherArgs)
	{
		final Object[] args = args1(inType, otherArgs);
		return (FunctionOp<I, O>) specialOp(opType, FunctionOp.class, outType, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, O, OP extends Op> FunctionOp<I, O> function(
		final Class<OP> opType, final Class<O> outType, final I in,
		final Object... otherArgs)
	{
		final Object[] args = args1(in, otherArgs);
		return (FunctionOp<I, O>) specialOp(opType, FunctionOp.class, outType, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, O, OP extends Op> HybridOp<I, O> hybrid(final Class<OP> opType,
		final Class<O> outType, final Class<I> inType, final Object... otherArgs)
	{
		final Object[] args = args2(outType, inType, otherArgs);
		return (HybridOp<I, O>) specialOp(opType, HybridOp.class, null, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, O, OP extends Op> HybridOp<I, O> hybrid(final Class<OP> opType,
		final Class<O> outType, final I in, final Object... otherArgs)
	{
		final Object[] args = args2(outType, in, otherArgs);
		return (HybridOp<I, O>) specialOp(opType, HybridOp.class, null, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, O, OP extends Op> HybridOp<I, O> hybrid(final Class<OP> opType,
		final O out, final I in, final Object... otherArgs)
	{
		final Object[] args = args2(out, in, otherArgs);
		return (HybridOp<I, O>) specialOp(opType, HybridOp.class, null, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A, OP extends Op> InplaceOp<A> inplace(final Class<OP> opType,
		final Class<A> argType, final Object... otherArgs)
	{
		final Object[] args = args1(argType, otherArgs);
		return (InplaceOp<A>) specialOp(opType, InplaceOp.class, null, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A, OP extends Op> InplaceOp<A> inplace(final Class<OP> opType,
		final A arg, final Object... otherArgs)
	{
		final Object[] args = args1(arg, otherArgs);
		return (InplaceOp<A>) specialOp(opType, InplaceOp.class, null, args);
	}

	@Override
	public Module module(final String name, final Object... args) {
		return matcher.findModule(this, new OpRef<Op>(name, args));
	}

	@Override
	public <OP extends Op> Module module(final Class<OP> type,
		final Object... args)
	{
		return matcher.findModule(this, new OpRef<OP>(type, args));
	}

	@Override
	public Module module(final Op op, final Object... args) {
		final Module module = info(op).createModule(op);
		getContext().inject(module.getDelegateObject());
		return matcher.assignInputs(module, args);
	}

	@Override
	public Collection<String> ops() {
		// collect list of unique operation names
		final HashSet<String> operations = new HashSet<String>();
		for (final CommandInfo info : infos()) {
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
		return namespaceService.create(nsClass, this);
	}

	// -- Operation shortcuts - global namespace --

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
	public String help(final Namespace namespace) {
		final String result =
			(String) run(net.imagej.ops.help.HelpForNamespace.class, namespace);
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
		final List<? extends ComputerOp<A, A>> ops,
		final BufferFactory<A, A> bufferFactory)
	{
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.join.DefaultJoinComputers.class, out, in,
				ops, bufferFactory);
		return result;
	}

	@Override
	public <A> A join(final A arg, final List<InplaceOp<A>> ops) {
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.join.DefaultJoinInplaces.class, arg, ops);
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
	public <I> I loop(final I arg, final ComputerOp<I, I> op, final int n) {
		@SuppressWarnings("unchecked")
		final I result =
			(I) run(net.imagej.ops.loop.DefaultLoopInplace.class, arg, op, n);
		return result;
	}

	@Override
	public <A> A loop(final A out, final A in, final ComputerOp<A, A> op,
		final BufferFactory<A, A> bufferFactory, final int n)
	{
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.loop.DefaultLoopComputer.class, out, in, op,
				bufferFactory, n);
		return result;
	}

	@Override
	public Object map(final Object... args) {
		return run(Ops.Map.NAME, args);
	}

	@Override
	public <A, B extends Type<B>> RandomAccessibleInterval<B> map(
		final RandomAccessibleInterval<A> input, final ComputerOp<A, B> op,
		final B type)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<B> result =
			(RandomAccessibleInterval<B>) run(
				net.imagej.ops.map.MapConvertRAIToRAI.class, input, op, type);
		return result;
	}

	@Override
	public <A, B extends Type<B>> RandomAccessible<B> map(
		final RandomAccessible<A> input, final ComputerOp<A, B> op, final B type)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessible<B> result =
			(RandomAccessible<B>) run(
				net.imagej.ops.map.MapConvertRandomAccessToRandomAccess.class, input,
				op, type);
		return result;
	}

	@Override
	public <A, B extends Type<B>> IterableInterval<B> map(
		final IterableInterval<A> input, final ComputerOp<A, B> op, final B type)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<B> result =
			(IterableInterval<B>) run(
				net.imagej.ops.map.MapIterableIntervalToView.class, input, op,
				type);
		return result;
	}

	@Override
	public <A> IterableInterval<A> map(final IterableInterval<A> arg,
		final InplaceOp<A> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<A> result =
			(IterableInterval<A>) run(net.imagej.ops.map.MapParallel.class, arg, op);
		return result;
	}

	@Override
	public <A, B> IterableInterval<B> map(final IterableInterval<B> out,
		final IterableInterval<A> in, final ComputerOp<A, B> op)
	{
		// net.imagej.ops.map.MapIterableToIterableParallel.class
		// net.imagej.ops.map.MapIterableIntervalToIterableInterval.class
		@SuppressWarnings("unchecked")
		final IterableInterval<B> result =
			(IterableInterval<B>) run(net.imagej.ops.Ops.Map.class, out, in, op);
		return result;
	}

	@Override
	public <A, B> RandomAccessibleInterval<B> map(
		final RandomAccessibleInterval<B> out, final IterableInterval<A> in,
		final ComputerOp<A, B> op)
	{
		// net.imagej.ops.map.MapIterableToRAIParallel.class
		// net.imagej.ops.map.MapIterableIntervalToRAI.class
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<B> result =
			(RandomAccessibleInterval<B>) run(net.imagej.ops.Ops.Map.class, out, in,
				op);
		return result;
	}

	@Override
	public <A> Iterable<A> map(final Iterable<A> arg, final InplaceOp<A> op) {
		@SuppressWarnings("unchecked")
		final Iterable<A> result =
			(Iterable<A>) run(net.imagej.ops.map.MapIterableInplace.class, arg, op);
		return result;
	}

	@Override
	public <A, B> IterableInterval<B> map(final IterableInterval<B> out,
		final RandomAccessibleInterval<A> in, final ComputerOp<A, B> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<B> result =
			(IterableInterval<B>) run(
				net.imagej.ops.map.MapRAIToIterableInterval.class, out, in, op);
		return result;
	}

	@Override
	public <I, O> RandomAccessibleInterval<O> map(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final ComputerOp<Iterable<I>, O> op,
		final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.map.neighborhood.MapNeighborhood.class, out, in, op, shape);
		return result;
	}

	@Override
	public <I, O> RandomAccessibleInterval<O> map(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in,
		final CenterAwareComputerOp<Iterable<I>, O> func, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.map.neighborhood.MapNeighborhoodWithCenter.class, out, in, func, shape);
		return result;
	}

	@Override
	public <A, B> Iterable<B> map(final Iterable<B> out, final Iterable<A> in,
		final ComputerOp<A, B> op)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result =
			(Iterable<B>) run(net.imagej.ops.map.MapIterableToIterable.class, out,
				in, op);
		return result;
	}

	@Override
	public Object slicewise(final Object... args) {
		return run(Ops.Slicewise.NAME, args);
	}

	@Override
	public <I, O> RandomAccessibleInterval<O> slicewise(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final ComputerOp<I, O> op,
		final int... axisIndices)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.slicewise.SlicewiseRAI2RAI.class, out, in, op,
				axisIndices);
		return result;
	}

	@Override
	public <I, O> RandomAccessibleInterval<O> slicewise(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final ComputerOp<I, O> op,
		final int[] axisIndices, final boolean dropSingleDimensions)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.slicewise.SlicewiseRAI2RAI.class, out, in, op,
				axisIndices, dropSingleDimensions);
		return result;
	}

	// -- Operation shortcuts - other namespaces --

	@Override
	public ConvertNamespace convert() {
		return namespace(ConvertNamespace.class);
	}

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
	public Geometric2DNamespace geometric2d() {
		return namespace(Geometric2DNamespace.class);
	}

	@Override
	public Geometric3DNamespace geometric3d() {
		return namespace(Geometric3DNamespace.class);
	}

	@Override
	public HaralickNamespace haralick() {
		return namespace(HaralickNamespace.class);
	}

	@Override
	public ImageNamespace image() {
		return namespace(ImageNamespace.class);
	}

	@Override
	public ImageMomentsNamespace imagemoments() {
		return namespace(ImageMomentsNamespace.class);
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
	public TamuraNamespace tamura() {
		return namespace(TamuraNamespace.class);
	}

	@Override
	public ThreadNamespace thread() {
		return namespace(ThreadNamespace.class);
	}

	@Override
	public ThresholdNamespace threshold() {
		return namespace(ThresholdNamespace.class);
	}
	
	@Override
	public ZernikeNamespace zernike() {
		return namespace(ZernikeNamespace.class);
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

	/**
	 * Looks up an op of the given type, similar to {@link #op(Class, Object...)},
	 * but with an additional type constraint&mdash;e.g., matches could be
	 * restricted to {@link ComputerOp}s.
	 * 
	 * @param opType The type of op to match.
	 * @param specialType The additional constraint (e.g., {@link ComputerOp}).
	 * @param outType The type of the op's primary output, or null for any type.
	 * @param args The arguments to use when matching.
	 * @return The matched op.
	 */
	private <OP extends Op> OP specialOp(final Class<OP> opType,
		final Class<?> specialType, final Class<?> outType, final Object... args)
	{
		final OpRef<OP> ref =
			new OpRef<OP>(Collections.singleton(specialType), opType, args);
		if (outType != null) ref.setOutputs(Collections.singleton(outType));
		final Module module = matcher.findModule(this, ref);
		return OpUtils.unwrap(module, ref);
	}

	private Object[] args1(final Object o0, final Object... more) {
		final Object[] result = new Object[1 + more.length];
		result[0] = o0;
		int i = 1;
		for (final Object o : more) {
			result[i++] = o;
		}
		return result;
	}

	private Object[]
		args2(final Object o0, final Object o1, final Object... more)
	{
		final Object[] result = new Object[2 + more.length];
		result[0] = o0;
		result[1] = o1;
		int i = 2;
		for (final Object o : more) {
			result[i++] = o;
		}
		return result;
	}

}
