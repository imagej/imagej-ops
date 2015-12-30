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

import org.scijava.Contextual;
import org.scijava.module.Module;

import net.imagej.ops.cached.CachedOpEnvironment;
import net.imagej.ops.convert.ConvertNamespace;
import net.imagej.ops.copy.CopyNamespace;
import net.imagej.ops.create.CreateNamespace;
import net.imagej.ops.deconvolve.DeconvolveNamespace;
import net.imagej.ops.features.haralick.HaralickNamespace;
import net.imagej.ops.features.lbp2d.LBPNamespace;
import net.imagej.ops.features.tamura2d.TamuraNamespace;
import net.imagej.ops.features.zernike.ZernikeNamespace;
import net.imagej.ops.filter.FilterNamespace;
import net.imagej.ops.geom.GeomNamespace;
import net.imagej.ops.image.ImageNamespace;
import net.imagej.ops.imagemoments.ImageMomentsNamespace;
import net.imagej.ops.labeling.LabelingNamespace;
import net.imagej.ops.logic.LogicNamespace;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.math.MathNamespace;
import net.imagej.ops.special.BinaryComputerOp;
import net.imagej.ops.special.InplaceOp;
import net.imagej.ops.special.UnaryComputerOp;
import net.imagej.ops.special.UnaryOutputFactory;
import net.imagej.ops.stats.StatsNamespace;
import net.imagej.ops.thread.ThreadNamespace;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.Type;

/**
 * An op environment is the top-level entry point into op execution. It provides
 * all the built-in functionality of ops in a single place, including:
 * <ul>
 * <li>The pool of available ops, from which candidates are chosen.</li>
 * <li>Type-safe, built-in method signatures for all op implementations.</li>
 * <li>Selection (a.k.a. "matching") of op implementations from {@link OpRef}
 * descriptors.</li>
 * </ul>
 * <p>
 * Customizing the {@link OpEnvironment} allows customization of any or all of
 * the above. Potential use cases include:
 * <ul>
 * <li>Limiting or extending the pool of available op implementations.</li>
 * <li>Caching op outputs to improve subsequent time performance.</li>
 * <li>Configuration of environment "hints" to improve performance in time or
 * space.</li>
 * </ul>
 * <p>
 * The default&mdash;but not necessarily <em>only</em>&mdash;op environment is
 * the {@link OpService} of the application. The environment can be modified by
 * using a {@link CustomOpEnvironment}, or by implementing this interface
 * directly.
 * </p>
 * 
 * @author Curtis Rueden
 * @see OpService
 * @see CustomOpEnvironment
 */
public interface OpEnvironment extends Contextual {

	// -- OpEnvironment methods --

	OpMatchingService matcher();

	/**
	 * Executes the given operation with the specified arguments. The best
	 * {@link Op} implementation to use will be selected automatically from the
	 * operation name and arguments.
	 *
	 * @param name The operation to execute. If multiple {@link Op}s share this
	 *          name, then the best {@link Op} implementation to use will be
	 *          selected automatically from the name and arguments. If a name
	 *          without namespace is given, then ops from all namespaces with that
	 *          name will be included in the match; e.g., {@code "and"} will match
	 *          both {@code "logic.and"} and {@code "math.and"} ops.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	@OpMethod(op = net.imagej.ops.run.RunByName.class)
	default Object run(final String name, final Object... args) {
		return OpUtils.run(module(name, args));
	}

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
	@OpMethod(op = net.imagej.ops.run.RunByType.class)
	default <OP extends Op> Object run(final Class<OP> type, final Object... args)
	{
		return OpUtils.run(module(type, args));
	}

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
	@OpMethod(op = net.imagej.ops.run.RunByOp.class)
	default Object run(final Op op, final Object... args) {
		return OpUtils.run(module(op, args));
	}

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
	@OpMethod(op = net.imagej.ops.lookup.LookupByName.class)
	default Op op(final String name, final Object... args) {
		return OpUtils.unwrap(module(name, args), Op.class, null);
	}

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
	@OpMethod(op = net.imagej.ops.lookup.LookupByType.class)
	default <OP extends Op> OP op(final Class<OP> type, final Object... args) {
		return OpUtils.unwrap(module(type, args), type, null);
	}

	/**
	 * Looks up an op whose constraints are specified by the given {@link OpRef}
	 * descriptor.
	 * <p>
	 * NB: While it is typically the case that the returned {@link Op} instance is
	 * of the requested type(s), it may differ in certain circumstances. For
	 * example, the {@link CachedOpEnvironment} wraps the matching {@link Op}
	 * instance in some cases so that the values it computes can be cached for
	 * performance reasons.
	 * </p>
	 * 
	 * @param ref The {@link OpRef} describing the op to match.
	 * @return The matched op.
	 */
	default Op op(final OpRef<?> ref) {
		final Module module = matcher().findModule(this, ref);
		return OpUtils.unwrap(module, ref);
	}

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 *
	 * @param name The name of the operation.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	default Module module(final String name, final Object... args) {
		return matcher().findModule(this, OpRef.create(name, args));
	}

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
	default <OP extends Op> Module module(final Class<OP> type,
		final Object... args)
	{
		return matcher().findModule(this, OpRef.create(type, args));
	}

	/**
	 * Wraps the given {@link Op} as a {@link Module}, populating its inputs.
	 *
	 * @param op The {@link Op} to wrap and populate.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the {@link Op}, with populated inputs,
	 *         ready to run.
	 */
	default Module module(final Op op, final Object... args) {
		final Module module = info(op).cInfo().createModule(op);
		getContext().inject(module.getDelegateObject());
		return matcher().assignInputs(module, args);
	}

	/** Gets the metadata for a given {@link Op} class. */
	OpInfo info(Class<? extends Op> type);

	/** Gets the metadata for a given {@link Op}. */
	default OpInfo info(final Op op) {
		return info(op.getClass());
	}

	/**
	 * The available ops for the context, <em>including</em> those of the parent.
	 *
	 * @see #parent()
	 */
	Collection<OpInfo> infos();

	/** Gets the fully qualified names of all available operations. */
	default Collection<String> ops() {
		// collect list of unique operation names
		final HashSet<String> operations = new HashSet<>();
		for (final OpInfo info : infos()) {
			if (info.isNamed()) operations.add(info.getName());
		}

		// convert the set into a sorted list
		final ArrayList<String> sorted = new ArrayList<>(operations);
		Collections.sort(sorted);
		return sorted;
	}

	/** The parent context, if any. */
	default OpEnvironment parent() {
		return null;
	}

	/** Gets the namespace of the given class. */
	<NS extends Namespace> NS namespace(final Class<NS> nsClass);

	// -- Operation shortcuts - global namespace --

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = Ops.Eval.class)
	default Object eval(final Object... args) {
		return run(Ops.Eval.NAME, args);
	}

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.eval.DefaultEval.class)
	default Object eval(final String expression) {
		final Object result =
			run(net.imagej.ops.eval.DefaultEval.class, expression);
		return result;
	}

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.eval.DefaultEval.class)
	default Object eval(final String expression, final Map<String, Object> vars) {
		final Object result =
			run(net.imagej.ops.eval.DefaultEval.class, expression, vars);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = Ops.Help.class)
	default Object help(final Object... args) {
		return run(Ops.Help.NAME, args);
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpForOp.class)
	default String help(final Op op) {
		final String result = (String) run(net.imagej.ops.help.HelpForOp.class, op);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpForNamespace.class)
	default String help(final Namespace namespace) {
		final String result =
			(String) run(net.imagej.ops.help.HelpForNamespace.class, namespace);
		return result;
	}


	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	default String help() {
		final String result =
			(String) run(net.imagej.ops.help.HelpCandidates.class);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	default String help(final String name) {
		final String result =
			(String) run(net.imagej.ops.help.HelpCandidates.class, name);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	default String help(final String name, final Class<? extends Op> opType) {
		final String result =
			(String) run(net.imagej.ops.help.HelpCandidates.class, name, opType);
		return result;
	}

	/** Executes the "identity" operation on the given arguments. */
	@OpMethod(op = Ops.Identity.class)
	default Object identity(final Object... args) {
		return run(Ops.Identity.NAME, args);
	}

	/** Executes the "identity" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.identity.DefaultIdentity.class)
	default <A> A identity(final A arg) {
		@SuppressWarnings("unchecked")
		final A result =
			(A) run(net.imagej.ops.identity.DefaultIdentity.class, arg);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = Ops.Join.class)
	default Object join(final Object... args) {
		return run(Ops.Join.NAME, args);
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoin2Computers.class)
	default <A, B, C> C join(final C out, final A in,
		final UnaryComputerOp<A, B> first, final UnaryComputerOp<B, C> second,
		final UnaryOutputFactory<A, B> outputFactory)
	{
		@SuppressWarnings("unchecked")
		final C result = (C) run(net.imagej.ops.join.DefaultJoin2Computers.class,
			out, in, first, second, outputFactory);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoin2Inplaces.class)
	default <A> A join(final A arg, final InplaceOp<A> first,
		final InplaceOp<A> second)
	{
		@SuppressWarnings("unchecked")
		final A result = (A) run(net.imagej.ops.join.DefaultJoin2Inplaces.class,
			arg, first, second);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinNComputers.class)
	default <A> A join(final A out, final A in,
		final List<? extends UnaryComputerOp<A, A>> ops,
		final UnaryOutputFactory<A, A> outputFactory)
	{
		@SuppressWarnings("unchecked")
		final A result = (A) run(net.imagej.ops.join.DefaultJoinNComputers.class,
			out, in, ops, outputFactory);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinNInplaces.class)
	default <A> A join(final A arg, final List<? extends InplaceOp<A>> ops) {
		@SuppressWarnings("unchecked")
		final A result = (A) run(net.imagej.ops.join.DefaultJoinNInplaces.class,
			arg, ops);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceAndComputer.class)
	default <A, B> B join(final B out, final A in, final InplaceOp<A> first,
		final UnaryComputerOp<A, B> second)
	{
		@SuppressWarnings("unchecked")
		final B result = (B) run(
			net.imagej.ops.join.DefaultJoinInplaceAndComputer.class, out, in, first,
			second);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinComputerAndInplace.class)
	default <A, B> B join(final B out, final A in,
		final UnaryComputerOp<A, B> first, final InplaceOp<B> second)
	{
		@SuppressWarnings("unchecked")
		final B result = (B) run(
			net.imagej.ops.join.DefaultJoinComputerAndInplace.class, out, in, first,
			second);
		return result;
	}

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = Ops.Loop.class)
	default Object loop(final Object... args) {
		return run(Ops.Loop.NAME, args);
	}

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.loop.DefaultLoopInplace.class)
	default <A> A loop(final A arg, final InplaceOp<A> op, final int n) {
		@SuppressWarnings("unchecked")
		final A result = (A) run(net.imagej.ops.loop.DefaultLoopInplace.class, arg,
			op, n);
		return result;
	}

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.loop.DefaultLoopComputer.class)
	default <A> A loop(final A out, final A in, final UnaryComputerOp<A, A> op,
		final UnaryOutputFactory<A, A> outputFactory, final int n)
	{
		@SuppressWarnings("unchecked")
		final A result = (A) run(net.imagej.ops.loop.DefaultLoopComputer.class, out,
			in, op, outputFactory, n);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = Ops.Map.class)
	default Object map(final Object... args) {
		return run(Ops.Map.NAME, args);
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapViewRAIToRAI.class)
	default <EI, EO extends Type<EO>> RandomAccessibleInterval<EO> map(
		final RandomAccessibleInterval<EI> input, final UnaryComputerOp<EI, EO> op,
		final EO type)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<EO> result =
			(RandomAccessibleInterval<EO>) run(
				net.imagej.ops.map.MapViewRAIToRAI.class, input, op, type);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapViewRandomAccessToRandomAccess.class)
	default <EI, EO extends Type<EO>> RandomAccessible<EO> map(
		final RandomAccessible<EI> input, final UnaryComputerOp<EI, EO> op,
		final EO type)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessible<EO> result =
			(RandomAccessible<EO>) run(
				net.imagej.ops.map.MapViewRandomAccessToRandomAccess.class, input,
				op, type);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.map.MapViewIterableIntervalToIterableInterval.class)
	default <EI, EO extends Type<EO>> IterableInterval<EO> map(
		final IterableInterval<EI> input, final UnaryComputerOp<EI, EO> op,
		final EO type)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(
				net.imagej.ops.map.MapViewIterableIntervalToIterableInterval.class, input, op,
				type);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIterableIntervalInplaceParallel.class)
	default <A> IterableInterval<A> map(final IterableInterval<A> arg,
		final InplaceOp<A> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<A> result =
			(IterableInterval<A>) run(
				net.imagej.ops.map.MapIterableIntervalInplaceParallel.class, arg, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = {
		net.imagej.ops.map.MapIterableIntervalToIterableIntervalParallel.class,
		net.imagej.ops.map.MapIterableIntervalToIterableInterval.class })
	default <EI, EO> IterableInterval<EO> map(final IterableInterval<EO> out,
		final IterableInterval<EI> in, final UnaryComputerOp<EI, EO> op)
	{
		// net.imagej.ops.map.MapIterableToIterableParallel.class
		// net.imagej.ops.map.MapIterableIntervalToIterableInterval.class
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapIterableIntervalToRAIParallel.class,
		net.imagej.ops.map.MapIterableIntervalToRAI.class, net.imagej.ops.map.MapIterableIntervalToSamplingRAI.class})
	default <EI, EO> RandomAccessibleInterval<EO> map(
		final RandomAccessibleInterval<EO> out, final IterableInterval<EI> in,
		final UnaryComputerOp<EI, EO> op)
	{
		// net.imagej.ops.map.MapIterableIntervalToRAIParallel.class
		// net.imagej.ops.map.MapIterableIntervalToRAI.class
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<EO> result =
			(RandomAccessibleInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in,
				op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIterableInplace.class)
	default <A> Iterable<A> map(final Iterable<A> arg, final InplaceOp<A> op) {
		@SuppressWarnings("unchecked")
		final Iterable<A> result =
			(Iterable<A>) run(net.imagej.ops.map.MapIterableInplace.class, arg, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapRAIToIterableInterval.class)
	default <EI, EO> IterableInterval<EO> map(final IterableInterval<EO> out,
		final RandomAccessibleInterval<EI> in, final UnaryComputerOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(
				net.imagej.ops.map.MapRAIToIterableInterval.class, out, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.neighborhood.MapNeighborhood.class)
	default <I, O> RandomAccessibleInterval<O> map(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final UnaryComputerOp<Iterable<I>, O> op,
		final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.map.neighborhood.MapNeighborhood.class, out, in, op, shape);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.map.neighborhood.MapNeighborhoodWithCenter.class)
	default <I, O> RandomAccessibleInterval<O> map(
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

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIterableToIterable.class)
	default <EI, EO> Iterable<EO> map(final Iterable<EO> out,
		final Iterable<EI> in, final UnaryComputerOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final Iterable<EO> result =
			(Iterable<EO>) run(net.imagej.ops.map.MapIterableToIterable.class, out,
				in, op);
		return result;
	}

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = Ops.Slicewise.class)
	default Object slicewise(final Object... args) {
		return run(Ops.Slicewise.NAME, args);
	}

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.slicewise.SlicewiseRAI2RAI.class)
	default <I, O> RandomAccessibleInterval<O> slicewise(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final UnaryComputerOp<I, O> op,
		final int... axisIndices)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.slicewise.SlicewiseRAI2RAI.class, out, in, op,
				axisIndices);
		return result;
	}

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.slicewise.SlicewiseRAI2RAI.class)
	default <I, O> RandomAccessibleInterval<O> slicewise(
		final RandomAccessibleInterval<O> out,
		final RandomAccessibleInterval<I> in, final UnaryComputerOp<I, O> op,
		final int[] axisIndices, final boolean dropSingleDimensions)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.slicewise.SlicewiseRAI2RAI.class, out, in, op,
				axisIndices, dropSingleDimensions);
		return result;
	}
	
	// -- BinaryMapComputers --
	
	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.BinaryMapIIAndRAIToRAI.class })
	default <EI1, EI2, EO> RandomAccessibleInterval<EO> map(
		final RandomAccessibleInterval<EO> out, final IterableInterval<EI1> in1,
		final RandomAccessibleInterval<EI2> in2,
		final BinaryComputerOp<EI1, EI2, EO> op)
	{
		// net.imagej.ops.map.MapIterableIntervalToRAIParallel.class
		// net.imagej.ops.map.MapIterableIntervalToRAI.class
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<EO> result =
			(RandomAccessibleInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in1, in2,
				op);
		return result;
	}

	// -- Operation shortcuts - other namespaces --
	
	/** Gateway into ops of the "copy" namespace. */
	default CopyNamespace copy() {
		return namespace(CopyNamespace.class);
	}

	/** Gateway into ops of the "convert" namespace. */
	default ConvertNamespace convert() {
		return namespace(ConvertNamespace.class);
	}

	/** Gateway into ops of the "create" namespace. */
	default CreateNamespace create() {
		return namespace(CreateNamespace.class);
	}

	/** Gateway into ops of the "deconvolve" namespace. */
	default DeconvolveNamespace deconvolve() {
		return namespace(DeconvolveNamespace.class);
	}

	/** Gateway into ops of the "filter" namespace. */
	default FilterNamespace filter() {
		return namespace(FilterNamespace.class);
	}
	
	/** Gateway into ops of the "geom" namespace. */
	default GeomNamespace geom() {
		return namespace(GeomNamespace.class);
	}

	/** Gateway into ops of the "haralick" namespace. */
	default HaralickNamespace haralick() {
		return namespace(HaralickNamespace.class);
	}

	/** Gateway into ops of the "image" namespace. */
	default ImageNamespace image() {
		return namespace(ImageNamespace.class);
	}

	/** Gateway into ops of the "imagemoments" namespace. */
	default ImageMomentsNamespace imagemoments() {
		return namespace(ImageMomentsNamespace.class);
	}
	
	/** Gateway into ops of the "labeling" namespace. */
	default LabelingNamespace labeling() {
		return namespace(LabelingNamespace.class);
	}
	
	/** Gateway into ops of the "lbp" namespace. */
	default LBPNamespace lbp() {
		return namespace(LBPNamespace.class);
	}

	/** Gateway into ops of the "logic" namespace. */
	default LogicNamespace logic() {
		return namespace(LogicNamespace.class);
	}

	/** Gateway into ops of the "math" namespace. */
	default MathNamespace math() {
		return namespace(MathNamespace.class);
	}

	/** Gateway into ops of the "stats" namespace. */
	default StatsNamespace stats() {
		return namespace(StatsNamespace.class);
	}
	
	/** Gateway into ops of the "tamura" namespace. */
	default TamuraNamespace tamura() {
		return namespace(TamuraNamespace.class);
	}

	/** Gateway into ops of the "thread" namespace. */
	default ThreadNamespace thread() {
		return namespace(ThreadNamespace.class);
	}

	/** Gateway into ops of the "threshold" namespace. */
	default ThresholdNamespace threshold() {
		return namespace(ThresholdNamespace.class);
	}
	
	/** Gateway into ops of the "zernike" namespace. */
	default ZernikeNamespace zernike() {
		return namespace(ZernikeNamespace.class);
	}

}
