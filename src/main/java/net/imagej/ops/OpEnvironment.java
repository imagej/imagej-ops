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

package net.imagej.ops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.imagej.ops.cached.CachedOpEnvironment;
import net.imagej.ops.coloc.ColocNamespace;
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
import net.imagej.ops.linalg.LinAlgNamespace;
import net.imagej.ops.logic.LogicNamespace;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.math.MathNamespace;
import net.imagej.ops.morphology.MorphologyNamespace;
import net.imagej.ops.special.SpecialOp;
import net.imagej.ops.special.UnaryOutputFactory;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.NullaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.inplace.BinaryInplace1Op;
import net.imagej.ops.special.inplace.BinaryInplaceOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imagej.ops.stats.StatsNamespace;
import net.imagej.ops.thread.ThreadNamespace;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imagej.ops.topology.TopologyNamespace;
import net.imagej.ops.transform.TransformNamespace;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.Type;

import org.scijava.Contextual;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;

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
		return run(module(name, args));
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
	default Object run(final Class<? extends Op> type, final Object... args) {
		return run(module(type, args));
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
		return run(module(op, args));
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
		return OpUtils.unwrap(module(name, args), OpRef.types(Op.class));
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
		return (OP) OpUtils.unwrap(module(type, args), OpRef.types(type));
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
	default Op op(final OpRef ref) {
		return op(Collections.singletonList(ref));
	}

	/**
	 * Looks up an op whose constraints are specified by the given list of
	 * {@link OpRef} descriptor.
	 * <p>
	 * NB: While it is typically the case that the returned {@link Op} instance is
	 * of the requested type(s), it may differ in certain circumstances. For
	 * example, the {@link CachedOpEnvironment} wraps the matching {@link Op}
	 * instance in some cases so that the values it computes can be cached for
	 * performance reasons.
	 * </p>
	 * 
	 * @param refs The list of {@link OpRef}s describing the op to match.
	 * @return The matched op.
	 */
	default Op op(final List<OpRef> refs) {
		final OpCandidate match = matcher().findMatch(this, refs);
		return OpUtils.unwrap(match.getModule(), match.getRef());
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
		return matcher().findMatch(this, OpRef.create(name, args)).getModule();
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
	default Module module(final Class<? extends Op> type, final Object... args) {
		return matcher().findMatch(this, OpRef.create(type, args)).getModule();
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
	@OpMethod(op = net.imagej.ops.eval.DefaultEval.class)
	default Object eval(final String expression) {
		final Object result = run(net.imagej.ops.Ops.Eval.class,
			expression);
		return result;
	}

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.eval.DefaultEval.class)
	default Object eval(final String expression, final Map<String, Object> vars) {
		final Object result = run(net.imagej.ops.Ops.Eval.class, expression,
			vars);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpForOp.class)
	default String help(final Op op) {
		final String result = (String) run(net.imagej.ops.Ops.Help.class, op);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpForNamespace.class)
	default String help(final Namespace namespace) {
		final String result = (String) run(
			net.imagej.ops.Ops.Help.class, namespace);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	default String help() {
		final String result = (String) run(
			net.imagej.ops.Ops.Help.class);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	default String help(final String name) {
		final String result = (String) run(net.imagej.ops.Ops.Help.class,
			name);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	default String help(final String name, final Class<? extends Op> opType) {
		final String result = (String) run(net.imagej.ops.Ops.Help.class,
			name, opType);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	default String help(final String name, final Class<? extends Op> opType,
		final Integer arity)
	{
		final String result = (String) run(net.imagej.ops.Ops.Help.class,
			name, opType, arity);
		return result;
	}

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	default String help(final String name, final Class<? extends Op> opType,
		final Integer arity, final SpecialOp.Flavor flavor)
	{
		final String result = (String) run(net.imagej.ops.Ops.Help.class,
			name, opType, arity, flavor);
		return result;
	}

	/** Executes the "identity" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.identity.DefaultIdentity.class)
	default <A> A identity(final A arg) {
		@SuppressWarnings("unchecked")
		final A result = (A) run(net.imagej.ops.Ops.Identity.class,
			arg);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoin2Computers.class)
	default <A, B, C> C join(final C out, final A in,
		final UnaryComputerOp<A, B> first, final UnaryComputerOp<B, C> second,
		final UnaryOutputFactory<A, B> outputFactory)
	{
		@SuppressWarnings("unchecked")
		final C result = (C) run(net.imagej.ops.Ops.Join.class,
			out, in, first, second, outputFactory);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoin2Inplaces.class)
	default <A, B extends A, C extends B> C join(final C arg,
		final UnaryInplaceOp<A, B> first, final UnaryInplaceOp<B, C> second)
	{
		@SuppressWarnings("unchecked")
		final C result = (C) run(net.imagej.ops.Ops.Join.class, arg, first, second);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinNComputers.class)
	default <A> A join(final A out, final A in,
		final List<? extends UnaryComputerOp<A, A>> ops,
		final UnaryOutputFactory<A, A> outputFactory)
	{
		@SuppressWarnings("unchecked")
		final A result = (A) run(net.imagej.ops.Ops.Join.class,
			out, in, ops, outputFactory);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinNInplaces.class)
	default <I, O extends I> O join(final O arg,
		final List<? extends UnaryInplaceOp<I, O>> ops)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) run(net.imagej.ops.Ops.Join.class, arg, ops);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceAndComputer.class)
	default <AI, AO extends AI, B> B join(final B out, final AO in,
		final UnaryInplaceOp<AI, AO> first, final UnaryComputerOp<AO, B> second)
	{
		@SuppressWarnings("unchecked")
		final B result = (B) run(net.imagej.ops.Ops.Join.class, out, in, first,
			second);
		return result;
	}

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinComputerAndInplace.class)
	default <A, BI, BO extends BI> BO join(final BO out, final A in,
		final UnaryComputerOp<A, BO> first, final UnaryInplaceOp<BI, BO> second)
	{
		@SuppressWarnings("unchecked")
		final BO result = (BO) run(net.imagej.ops.Ops.Join.class, out, in, first,
			second);
		return result;
	}

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.loop.DefaultLoopInplace.class)
	default <I, O extends I> O loop(final O arg, final UnaryInplaceOp<I, O> op,
		final int n)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) run(net.imagej.ops.Ops.Loop.class, arg, op, n);
		return result;
	}

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.loop.DefaultLoopComputer.class)
	default <A> A loop(final A out, final A in, final UnaryComputerOp<A, A> op,
		final UnaryOutputFactory<A, A> outputFactory, final int n)
	{
		@SuppressWarnings("unchecked")
		final A result = (A) run(net.imagej.ops.Ops.Loop.class, out,
			in, op, outputFactory, n);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapNullaryIterable.class)
	default <EO> Iterable<EO> map(final Iterable<EO> out,
		final NullaryComputerOp<EO> op)
	{
		@SuppressWarnings("unchecked")
		final Iterable<EO> result = (Iterable<EO>) run(net.imagej.ops.Ops.Map.class,
			out, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapNullaryII.class)
	default <EO> Iterable<EO> map(final IterableInterval<EO> out,
		final NullaryComputerOp<EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result = (IterableInterval<EO>) run(
			net.imagej.ops.Ops.Map.class, out, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapUnaryComputers.IIToIIParallel.class,
		net.imagej.ops.map.MapUnaryComputers.IIToII.class })
	default <EI, EO> IterableInterval<EO> map(
		final IterableInterval<EO> out, final IterableInterval<EI> in,
		final UnaryComputerOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapUnaryComputers.IIToRAIParallel.class,
		net.imagej.ops.map.MapUnaryComputers.IIToRAI.class })
	default <EI, EO> RandomAccessibleInterval<EO> map(
		final RandomAccessibleInterval<EO> out, final IterableInterval<EI> in,
		final UnaryComputerOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<EO> result =
			(RandomAccessibleInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapUnaryComputers.RAIToIIParallel.class,
		net.imagej.ops.map.MapUnaryComputers.RAIToII.class })
	default <EI, EO> IterableInterval<EO> map(
		final IterableInterval<EO> out, final RandomAccessibleInterval<EI> in,
		final UnaryComputerOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapBinaryComputers.IIAndIIToIIParallel.class,
		net.imagej.ops.map.MapBinaryComputers.IIAndIIToII.class })
	default <EI1, EI2, EO> IterableInterval<EO> map(
		final IterableInterval<EO> out, final IterableInterval<EI1> in1,
		final IterableInterval<EI2> in2, final BinaryComputerOp<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in1, in2, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapBinaryComputers.IIAndIIToRAIParallel.class,
		net.imagej.ops.map.MapBinaryComputers.IIAndIIToRAI.class })
	default <EI1, EI2, EO> RandomAccessibleInterval<EO> map(
		final RandomAccessibleInterval<EO> out, final IterableInterval<EI1> in1,
		final IterableInterval<EI2> in2, final BinaryComputerOp<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<EO> result =
			(RandomAccessibleInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in1, in2, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapBinaryComputers.IIAndRAIToIIParallel.class,
		net.imagej.ops.map.MapBinaryComputers.IIAndRAIToII.class })
	default <EI1, EI2, EO> IterableInterval<EO> map(
		final IterableInterval<EO> out, final IterableInterval<EI1> in1,
		final RandomAccessibleInterval<EI2> in2, final BinaryComputerOp<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in1, in2, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapBinaryComputers.IIAndRAIToRAIParallel.class,
		net.imagej.ops.map.MapBinaryComputers.IIAndRAIToRAI.class })
	default <EI1, EI2, EO> RandomAccessibleInterval<EO> map(
		final RandomAccessibleInterval<EO> out, final IterableInterval<EI1> in1,
		final RandomAccessibleInterval<EI2> in2, final BinaryComputerOp<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<EO> result =
			(RandomAccessibleInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in1, in2, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapBinaryComputers.RAIAndIIToIIParallel.class,
		net.imagej.ops.map.MapBinaryComputers.RAIAndIIToII.class })
	default <EI1, EI2, EO> IterableInterval<EO> map(
		final IterableInterval<EO> out, final RandomAccessibleInterval<EI1> in1,
		final IterableInterval<EI2> in2, final BinaryComputerOp<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in1, in2, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapBinaryComputers.RAIAndIIToRAIParallel.class,
		net.imagej.ops.map.MapBinaryComputers.RAIAndIIToRAI.class })
	default <EI1, EI2, EO> RandomAccessibleInterval<EO> map(
		final RandomAccessibleInterval<EO> out, final RandomAccessibleInterval<EI1> in1,
		final IterableInterval<EI2> in2, final BinaryComputerOp<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<EO> result =
			(RandomAccessibleInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in1, in2, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapBinaryComputers.RAIAndRAIToIIParallel.class,
		net.imagej.ops.map.MapBinaryComputers.RAIAndRAIToII.class })
	default <EI1, EI2, EO> IterableInterval<EO> map(
		final IterableInterval<EO> out, final RandomAccessibleInterval<EI1> in1,
		final RandomAccessibleInterval<EI2> in2, final BinaryComputerOp<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(net.imagej.ops.Ops.Map.class, out, in1, in2, op);
		return result;
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
				net.imagej.ops.Ops.Map.class, input, op, type);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapViewRandomAccessToRandomAccess.class)
	default <EI, EO extends Type<EO>> RandomAccessible<EO> map(
		final RandomAccessible<EI> input, final UnaryComputerOp<EI, EO> op,
		final EO type)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessible<EO> result = (RandomAccessible<EO>) run(
			net.imagej.ops.Ops.Map.class, input, op,
			type);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.map.MapViewIIToII.class)
	default <EI, EO extends Type<EO>> IterableInterval<EO> map(
		final IterableInterval<EI> input, final UnaryComputerOp<EI, EO> op,
		final EO type)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result = (IterableInterval<EO>) run(
			net.imagej.ops.Ops.Map.class, input,
			op, type);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIIInplaceParallel.class)
	default <EI, EO extends EI> IterableInterval<EO> map(
		final IterableInterval<EO> arg, final UnaryInplaceOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result = (IterableInterval<EO>) run(
			net.imagej.ops.Ops.Map.class, arg, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIterableInplace.class)
	default <EI, EO extends EI> Iterable<EO> map(final Iterable<EO> arg,
		final UnaryInplaceOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final Iterable<EO> result = (Iterable<EO>) run(net.imagej.ops.Ops.Map.class,
			arg, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.neighborhood.DefaultMapNeighborhood.class)
	default <EI, EO> IterableInterval<EO> map(
		final IterableInterval<EO> out,
		final RandomAccessibleInterval<EI> in, final Shape shape,
		final UnaryComputerOp<Iterable<EI>, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(
				net.imagej.ops.Ops.Map.class, out, in, op, shape);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.map.neighborhood.MapNeighborhoodWithCenter.class)
	default <EI, EO> IterableInterval<EO> map(
		final IterableInterval<EO> out, final RandomAccessibleInterval<EI> in,
		final Shape shape, final CenterAwareComputerOp<EI, EO> func)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result =
			(IterableInterval<EO>) run(
				net.imagej.ops.Ops.Map.class, out, in, func, shape);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIterableToIterable.class)
	default <EI, EO> Iterable<EO> map(final Iterable<EO> out,
		final Iterable<EI> in, final UnaryComputerOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final Iterable<EO> result = (Iterable<EO>) run(net.imagej.ops.Ops.Map.class,
			out, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapIIAndIIInplaceParallel.class,
		net.imagej.ops.map.MapIIAndIIInplace.class })
	default <EI, EO extends EI> IterableInterval<EO> map(
		final IterableInterval<EO> arg, final IterableInterval<EO> in,
		final BinaryInplaceOp<EI, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result = (IterableInterval<EO>) run(
			net.imagej.ops.Ops.Map.class, arg, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapBinaryInplace1s.IIAndIIParallel.class,
		net.imagej.ops.map.MapBinaryInplace1s.IIAndII.class })
	default <EI1, EI2, EO extends EI1> IterableInterval<EO> map(
		final IterableInterval<EO> arg, final IterableInterval<EI2> in,
		final BinaryInplace1Op<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result = (IterableInterval<EO>) run(
			Ops.Map.class, arg, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = {
		net.imagej.ops.map.MapBinaryInplace1s.IIAndRAIParallel.class,
		net.imagej.ops.map.MapBinaryInplace1s.IIAndRAI.class })
	default <EI1, EI2, EO extends EI1> IterableInterval<EO> map(
		final IterableInterval<EO> arg, final RandomAccessibleInterval<EI2> in,
		final BinaryInplace1Op<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<EO> result = (IterableInterval<EO>) run(
			Ops.Map.class, arg, in, op);
		return result;
	}

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = {
		net.imagej.ops.map.MapBinaryInplace1s.RAIAndIIParallel.class,
		net.imagej.ops.map.MapBinaryInplace1s.RAIAndII.class })
	default <EI1, EI2, EO extends EI1> RandomAccessibleInterval<EO> map(
		final RandomAccessibleInterval<EO> arg, final IterableInterval<EI2> in,
		final BinaryInplace1Op<EI1, EI2, EO> op)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<EO> result =
			(RandomAccessibleInterval<EO>) run(Ops.Map.class, arg, in, op);
		return result;
	}

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.slice.SliceRAI2RAI.class)
	default <I, O> RandomAccessibleInterval<O> slice(
		final RandomAccessibleInterval<O> out, final RandomAccessibleInterval<I> in,
		final UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> op,
		final int... axisIndices)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.Ops.Slice.class, out, in, op,
				axisIndices);
		return result;
	}

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.slice.SliceRAI2RAI.class)
	default <I, O> RandomAccessibleInterval<O> slice(
		final RandomAccessibleInterval<O> out, final RandomAccessibleInterval<I> in,
		final UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> op,
		final int[] axisIndices, final boolean dropSingleDimensions)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) run(
				net.imagej.ops.Ops.Slice.class, out, in, op,
				axisIndices, dropSingleDimensions);
		return result;
	}

	// -- Operation shortcuts - other namespaces --

	/** Gateway into ops of the "coloc" namespace. */
	default ColocNamespace coloc() {
		return namespace(ColocNamespace.class);
	}
	
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

	/** Gateway into ops of the "linalg" namespace */
	default LinAlgNamespace linalg() {
		return namespace(LinAlgNamespace.class);
	}

	/** Gateway into ops of the "logic" namespace. */
	default LogicNamespace logic() {
		return namespace(LogicNamespace.class);
	}

	/** Gateway into ops of the "math" namespace. */
	default MathNamespace math() {
		return namespace(MathNamespace.class);
	}

	/** Gateway into ops of the "morphology" namespace. */
	default MorphologyNamespace morphology() {
		return namespace(MorphologyNamespace.class);
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

	/** Gateway into ops of the "topology" namespace */
	default TopologyNamespace topology() { return namespace(TopologyNamespace.class); }

	/** Gateway into ops of the "transform" namespace. */
	default TransformNamespace transform() {
		return namespace(TransformNamespace.class);
	}

	/** Gateway into ops of the "zernike" namespace. */
	default ZernikeNamespace zernike() {
		return namespace(ZernikeNamespace.class);
	}

	// -- Helper methods --

	static Object run(final Module module) {
		module.run();

		final List<Object> outputs = new ArrayList<>();
		for (final ModuleItem<?> output : module.getInfo().outputs()) {
			final Object value = output.getValue(module);
			outputs.add(value);
		}
		return outputs.size() == 1 ? outputs.get(0) : outputs;
	}
}
