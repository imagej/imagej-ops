/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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
public final class Hybrids {

	private Hybrids() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Gets the best {@link NullaryHybridOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link NullaryHybridOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link NullaryHybridOp}s implement), then
	 *          the best {@link NullaryHybridOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link NullaryHybridOp} typed
	 *          output.
	 * @param otherArgs The operation's arguments, excluding the typed output
	 *          value.
	 * @return A {@link NullaryHybridOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <O, OP extends Op> NullaryHybridOp<O> nullary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType);
		final OpRef<OP> ref = OpRef.createTypes(opType, NullaryHybridOp.class,
			null, args);
		return (NullaryHybridOp<O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link NullaryHybridOp} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link NullaryHybridOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link NullaryHybridOp}s implement), then
	 *          the best {@link NullaryHybridOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param otherArgs The operation's arguments, excluding the typed output
	 *          value.
	 * @return A {@link NullaryHybridOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <O, OP extends Op> NullaryHybridOp<O> nullary(
		final OpEnvironment ops, final Class<OP> opType, final O out,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out);
		final OpRef<OP> ref = OpRef.createTypes(opType, NullaryHybridOp.class,
			null, args);
		return (NullaryHybridOp<O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link UnaryHybridOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridOp}s implement), then
	 *          the best {@link UnaryHybridOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryHybridOp} typed output.
	 * @param inType The {@link Class} of the {@link UnaryHybridOp} typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I, O, OP extends Op> UnaryHybridOp<I, O> unary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Class<I> inType, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, inType);
		final OpRef<OP> ref = OpRef.createTypes(opType, UnaryHybridOp.class,
			null, args);
		return (UnaryHybridOp<I, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link UnaryHybridOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridOp}s implement), then
	 *          the best {@link UnaryHybridOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryHybridOp} typed output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I, O, OP extends Op> UnaryHybridOp<I, O> unary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final I in, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in);
		final OpRef<OP> ref = OpRef.createTypes(opType, UnaryHybridOp.class,
			null, args);
		return (UnaryHybridOp<I, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link UnaryHybridOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridOp}s implement), then
	 *          the best {@link UnaryHybridOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I, O, OP extends Op> UnaryHybridOp<I, O> unary(
		final OpEnvironment ops, final Class<OP> opType, final O out, final I in,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in);
		final OpRef<OP> ref = OpRef.createTypes(opType, UnaryHybridOp.class,
			null, args);
		return (UnaryHybridOp<I, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link BinaryHybridOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridOp}s implement), then
	 *          the best {@link BinaryHybridOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryHybridOp} typed
	 *          output.
	 * @param in1Type The {@link Class} of the {@link BinaryHybridOp} first typed
	 *          input.
	 * @param in2Type The {@link Class} of the {@link BinaryHybridOp} second typed
	 *          input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I1, I2, O, OP extends Op> BinaryHybridOp<I1, I2, O> binary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Class<I1> in1Type, final Class<I2> in2Type,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1Type, in2Type);
		final OpRef<OP> ref = OpRef.createTypes(opType, BinaryHybridOp.class,
			null, args);
		return (BinaryHybridOp<I1, I2, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link BinaryHybridOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridOp}s implement), then
	 *          the best {@link BinaryHybridOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryHybridOp} typed
	 *          output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I1, I2, O, OP extends Op> BinaryHybridOp<I1, I2, O> binary(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final I1 in1, final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1, in2);
		final OpRef<OP> ref = OpRef.createTypes(opType, BinaryHybridOp.class,
			null, args);
		return (BinaryHybridOp<I1, I2, O>) ops.op(ref);
	}

	/**
	 * Gets the best {@link BinaryHybridOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridOp}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridOp}s implement), then
	 *          the best {@link BinaryHybridOp} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <I1, I2, O, OP extends Op> BinaryHybridOp<I1, I2, O> binary(
		final OpEnvironment ops, final Class<OP> opType, final O out, final I1 in1,
		final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		final OpRef<OP> ref = OpRef.createTypes(opType, BinaryHybridOp.class,
			null, args);
		return (BinaryHybridOp<I1, I2, O>) ops.op(ref);
	}

}
