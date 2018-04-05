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

package net.imagej.ops.special.inplace;

import java.util.Arrays;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpUtils;
import net.imagej.ops.special.hybrid.BinaryHybridCI;
import net.imagej.ops.special.hybrid.BinaryHybridCI1;
import net.imagej.ops.special.hybrid.UnaryHybridCI;

/**
 * Utility class for looking up inplace ops in a type-safe way.
 *
 * @author Curtis Rueden
 * @see UnaryInplaceOp
 */
public final class Inplaces {

	private Inplaces() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Gets the best {@link UnaryInplaceOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryInplaceOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryInplaceOp}s implement), then
	 *          the best {@link UnaryInplaceOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link UnaryInplaceOp} typed
	 *          argument.
	 * @param otherArgs The operation's arguments, excluding the typed argument
	 *          value.
	 * @return An {@link UnaryInplaceOp} with populated inputs, ready to use.
	 */
	public static <O> UnaryInplaceOp<? super O, O> unary(final OpEnvironment ops,
		final Class<? extends Op> opType, final Class<O> argType,
		final Object... otherArgs)
	{
		final OpRef[] refs = new OpRef[2];
		refs[0] = OpRef.createTypes(opType, UnaryInplaceOnlyOp.class, null, OpUtils
			.args(otherArgs, argType));
		refs[1] = OpRef.createTypes(opType, UnaryHybridCI.class, null, OpUtils.args(
			otherArgs, null, argType));
		@SuppressWarnings("unchecked")
		final UnaryInplaceOp<? super O, O> op = (UnaryInplaceOp<? super O, O>) ops
			.op(Arrays.asList(refs));
		return op;
	}

	/**
	 * Gets the best {@link UnaryInplaceOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryInplaceOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryInplaceOp}s implement), then
	 *          the best {@link UnaryInplaceOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param arg The typed argument.
	 * @param otherArgs The operation's arguments, excluding the typed argument
	 *          value.
	 * @return An {@link UnaryInplaceOp} with populated inputs, ready to use.
	 */
	public static <O> UnaryInplaceOp<? super O, O> unary(final OpEnvironment ops,
		final Class<? extends Op> opType, final O arg, final Object... otherArgs)
	{
		final OpRef[] refs = new OpRef[2];
		refs[0] = OpRef.createTypes(opType, UnaryInplaceOnlyOp.class, null, OpUtils
			.args(otherArgs, arg));
		refs[1] = OpRef.createTypes(opType, UnaryHybridCI.class, null, OpUtils.args(
			otherArgs, null, arg));
		@SuppressWarnings("unchecked")
		final UnaryInplaceOp<? super O, O> op = (UnaryInplaceOp<? super O, O>) ops
			.op(Arrays.asList(refs));
		return op;
	}

	/**
	 * Gets the best {@link BinaryInplace1Op} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryInplace1Op}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryInplace1Op}s implement),
	 *          then the best {@link BinaryInplace1Op} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link BinaryInplace1Op} mutable
	 *          argument (typed output / first input).
	 * @param inType The {@link Class} of the {@link BinaryInplace1Op} second
	 *          typed input.
	 * @param otherArgs The operation's arguments, excluding the typed argument
	 *          values.
	 * @return An {@link BinaryInplace1Op} with populated inputs, ready to use.
	 */

	public static <I2, O> BinaryInplace1Op<? super O, I2, O> binary1(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> argType, final Class<I2> inType, final Object... otherArgs)
	{
		final OpRef[] refs = new OpRef[2];
		refs[0] = OpRef.createTypes(opType, BinaryInplace1OnlyOp.class, null,
			OpUtils.args(otherArgs, argType, inType));
		refs[1] = OpRef.createTypes(opType, BinaryHybridCI1.class, null, OpUtils
			.args(otherArgs, null, argType, inType));
		@SuppressWarnings("unchecked")
		final BinaryInplace1Op<? super O, I2, O> op =
			(BinaryInplace1Op<? super O, I2, O>) ops.op(Arrays.asList(refs));
		return op;
	}

	/**
	 * Gets the best {@link BinaryInplace1Op} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryInplace1Op}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryInplace1Op}s implement),
	 *          then the best {@link BinaryInplace1Op} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param arg The mutable argument (typed output / first input).
	 * @param in The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed argument
	 *          values.
	 * @return An {@link BinaryInplace1Op} with populated inputs, ready to use.
	 */
	public static <I2, O> BinaryInplace1Op<? super O, I2, O> binary1(
		final OpEnvironment ops, final Class<? extends Op> opType, final O arg,
		final I2 in, final Object... otherArgs)
	{
		final OpRef[] refs = new OpRef[2];
		refs[0] = OpRef.createTypes(opType, BinaryInplace1OnlyOp.class, null,
			OpUtils.args(otherArgs, arg, in));
		refs[1] = OpRef.createTypes(opType, BinaryHybridCI1.class, null, OpUtils
			.args(otherArgs, null, arg, in));
		@SuppressWarnings("unchecked")
		final BinaryInplace1Op<? super O, I2, O> op =
			(BinaryInplace1Op<? super O, I2, O>) ops.op(Arrays.asList(refs));
		return op;
	}

	/**
	 * Gets the best {@link BinaryInplaceOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryInplaceOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryInplaceOp}s implement), then
	 *          the best {@link BinaryInplaceOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link BinaryInplaceOp} typed
	 *          arguments.
	 * @param otherArgs The operation's arguments, excluding the typed argument
	 *          values.
	 * @return An {@link BinaryInplaceOp} with populated inputs, ready to use.
	 */
	public static <O> BinaryInplaceOp<? super O, O> binary(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> argType, final Object... otherArgs)
	{
		final OpRef[] refs = new OpRef[2];
		refs[0] = OpRef.createTypes(opType, BinaryInplaceOnlyOp.class, null, OpUtils
			.args(otherArgs, argType, argType));
		refs[1] = OpRef.createTypes(opType, BinaryHybridCI.class, null, OpUtils
			.args(otherArgs, null, argType, argType));
		@SuppressWarnings("unchecked")
		final BinaryInplaceOp<? super O, O> op = (BinaryInplaceOp<? super O, O>) ops
			.op(Arrays.asList(refs));
		return op;
	}

	/**
	 * Gets the best {@link BinaryInplaceOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryInplaceOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryInplaceOp}s implement), then
	 *          the best {@link BinaryInplaceOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param arg1 The first typed argument.
	 * @param arg2 The second typed argument.
	 * @param otherArgs The operation's arguments, excluding the typed argument
	 *          values.
	 * @return An {@link BinaryInplaceOp} with populated inputs, ready to use.
	 */
	public static <O> BinaryInplaceOp<? super O, O> binary(
		final OpEnvironment ops, final Class<? extends Op> opType, final O arg1,
		final O arg2, final Object... otherArgs)
	{
		final OpRef[] refs = new OpRef[2];
		refs[0] = OpRef.createTypes(opType, BinaryInplaceOnlyOp.class, null, OpUtils
			.args(otherArgs, arg1, arg2));
		refs[1] = OpRef.createTypes(opType, BinaryHybridCI.class, null, OpUtils
			.args(otherArgs, null, arg1, arg2));
		@SuppressWarnings("unchecked")
		final BinaryInplaceOp<? super O, O> op = (BinaryInplaceOp<? super O, O>) ops
			.op(Arrays.asList(refs));
		return op;
	}

}
