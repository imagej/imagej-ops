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

package net.imagej.ops.special;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpUtils;

/**
 * Utility class for looking up function ops in a type-safe way.
 *
 * @author Curtis Rueden
 * @see NullaryFunctionOp
 * @see UnaryFunctionOp
 * @see BinaryFunctionOp
 */
public final class Functions {

	private Functions() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Gets the best {@link NullaryFunctionOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link NullaryFunctionOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link NullaryFunctionOp}s implement),
	 *          then the best {@link NullaryFunctionOp} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link NullaryFunctionOp} typed
	 *          output.
	 * @param otherArgs The operation's arguments, excluding the typed output
	 *          value.
	 * @return A {@link NullaryFunctionOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <O, OP extends Op> NullaryFunctionOp<O> nullary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs);
		final OpRef<OP> ref = OpRef.createTypes(opType, NullaryFunctionOp.class,
			outType, args);
		return (NullaryFunctionOp<O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link UnaryFunctionOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryFunctionOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryFunctionOp}s implement), then
	 *          the best {@link UnaryFunctionOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryFunctionOp} typed
	 *          output.
	 * @param inType The {@link Class} of the {@link UnaryFunctionOp} typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryFunctionOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I, O, OP extends Op> UnaryFunctionOp<I, O> unary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Class<I> inType, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, inType);
		final OpRef<OP> ref = OpRef.createTypes(opType, UnaryFunctionOp.class,
			outType, args);
		return (UnaryFunctionOp<I, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link UnaryFunctionOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryFunctionOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryFunctionOp}s implement), then
	 *          the best {@link UnaryFunctionOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryFunctionOp} typed
	 *          output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryFunctionOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I, O, OP extends Op> UnaryFunctionOp<I, O> unary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final I in, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, in);
		final OpRef<OP> ref = OpRef.createTypes(opType, UnaryFunctionOp.class,
			outType, args);
		return (UnaryFunctionOp<I, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link BinaryFunctionOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryFunctionOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryFunctionOp}s implement),
	 *          then the best {@link BinaryFunctionOp} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryFunctionOp} typed
	 *          output.
	 * @param in1Type The {@link Class} of the {@link BinaryFunctionOp} first
	 *          typed input.
	 * @param in2Type The {@link Class} of the {@link BinaryFunctionOp} second
	 *          typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryFunctionOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I1, I2, O, OP extends Op> BinaryFunctionOp<I1, I2, O> binary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Class<I1> in1Type, final Class<I2> in2Type,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, in1Type, in2Type);
		final OpRef<OP> ref = OpRef.createTypes(opType, BinaryFunctionOp.class,
			outType, args);
		return (BinaryFunctionOp<I1, I2, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link BinaryFunctionOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryFunctionOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryFunctionOp}s implement),
	 *          then the best {@link BinaryFunctionOp} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryFunctionOp} typed
	 *          output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryFunctionOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I1, I2, O, OP extends Op> BinaryFunctionOp<I1, I2, O> binary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final I1 in1, final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, in1, in2);
		final OpRef<OP> ref = OpRef.createTypes(opType, BinaryFunctionOp.class,
			outType, args);
		return (BinaryFunctionOp<I1, I2, O>) ops.op(ref);
	}

}
