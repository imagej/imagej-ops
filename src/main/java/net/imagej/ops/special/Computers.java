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
 * Utility class for looking up computer ops in a type-safe way.
 *
 * @author Curtis Rueden
 * @see NullaryComputerOp
 * @see UnaryComputerOp
 * @see BinaryComputerOp
 */
public final class Computers {

	private Computers() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Gets the best {@link NullaryComputerOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link NullaryComputerOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link NullaryComputerOp}s implement),
	 *          then the best {@link NullaryComputerOp} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link NullaryComputerOp} typed
	 *          output.
	 * @param otherArgs The operation's arguments, excluding the typed output
	 *          value.
	 * @return A {@link NullaryComputerOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <O, OP extends Op> NullaryComputerOp<O> nullary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType);
		final OpRef<OP> ref = OpRef.createTypes(opType, NullaryComputerOp.class,
			null, args);
		return (NullaryComputerOp<O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link NullaryComputerOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link NullaryComputerOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link NullaryComputerOp}s implement),
	 *          then the best {@link NullaryComputerOp} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param otherArgs The operation's arguments, excluding the typed output
	 *          value.
	 * @return A {@link NullaryComputerOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <O, OP extends Op> NullaryComputerOp<O> nullary(
		final OpEnvironment ops, final Class<OP> opType, final O out,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out);
		final OpRef<OP> ref = OpRef.createTypes(opType, NullaryComputerOp.class,
			null, args);
		return (NullaryComputerOp<O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link UnaryComputerOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryComputerOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryComputerOp}s implement), then
	 *          the best {@link UnaryComputerOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryComputerOp} typed
	 *          output.
	 * @param inType The {@link Class} of the {@link UnaryComputerOp} typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryComputerOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I, O, OP extends Op> UnaryComputerOp<I, O> unary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Class<I> inType, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, inType);
		final OpRef<OP> ref = OpRef.createTypes(opType, UnaryComputerOp.class,
			null, args);
		return (UnaryComputerOp<I, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link UnaryComputerOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryComputerOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryComputerOp}s implement), then
	 *          the best {@link UnaryComputerOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryComputerOp} typed
	 *          output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryComputerOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I, O, OP extends Op> UnaryComputerOp<I, O> unary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final I in, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in);
		final OpRef<OP> ref = OpRef.createTypes(opType, UnaryComputerOp.class,
			null, args);
		return (UnaryComputerOp<I, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link UnaryComputerOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryComputerOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryComputerOp}s implement), then
	 *          the best {@link UnaryComputerOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryComputerOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I, O, OP extends Op> UnaryComputerOp<I, O> unary(
		final OpEnvironment ops, final Class<OP> opType, final O out, final I in,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in);
		final OpRef<OP> ref = OpRef.createTypes(opType, UnaryComputerOp.class,
			null, args);
		return (UnaryComputerOp<I, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link BinaryComputerOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryComputerOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryComputerOp}s implement),
	 *          then the best {@link BinaryComputerOp} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryComputerOp} typed
	 *          output.
	 * @param in1Type The {@link Class} of the {@link BinaryComputerOp} first
	 *          typed input.
	 * @param in2Type The {@link Class} of the {@link BinaryComputerOp} second
	 *          typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryComputerOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I1, I2, O, OP extends Op> BinaryComputerOp<I1, I2, O> binary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Class<I1> in1Type, final Class<I2> in2Type,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1Type, in2Type);
		final OpRef<OP> ref = OpRef.createTypes(opType, BinaryComputerOp.class,
			null, args);
		return (BinaryComputerOp<I1, I2, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link BinaryComputerOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryComputerOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryComputerOp}s implement),
	 *          then the best {@link BinaryComputerOp} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryComputerOp} typed
	 *          output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryComputerOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I1, I2, O, OP extends Op> BinaryComputerOp<I1, I2, O> binary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final I1 in1, final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1, in2);
		final OpRef<OP> ref = OpRef.createTypes(opType, BinaryComputerOp.class,
			null, args);
		return (BinaryComputerOp<I1, I2, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link BinaryComputerOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryComputerOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryComputerOp}s implement),
	 *          then the best {@link BinaryComputerOp} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryComputerOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I1, I2, O, OP extends Op> BinaryComputerOp<I1, I2, O> binary(
		final OpEnvironment ops, final Class<OP> opType, final O out, final I1 in1,
		final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		final OpRef<OP> ref = OpRef.createTypes(opType, BinaryComputerOp.class,
			null, args);
		return (BinaryComputerOp<I1, I2, O>) ops.op(ref);
	}

}
