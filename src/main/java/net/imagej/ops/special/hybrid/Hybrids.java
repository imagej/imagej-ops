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

package net.imagej.ops.special.hybrid;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpUtils;
import net.imagej.ops.special.SpecialOp;

/**
 * Utility class for looking up hybrid ops in a type-safe way.
 *
 * @author Curtis Rueden
 * @see NullaryHybridCF
 * @see UnaryHybridCF
 * @see UnaryHybridCFI
 * @see BinaryHybridCF
 * @see BinaryHybridCFI
 * @see BinaryHybridCFI1
 */
public final class Hybrids {

	private Hybrids() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Gets the best {@link NullaryHybridCF} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link NullaryHybridCF}s share this type (e.g., the type is an
	 *          interface which multiple {@link NullaryHybridCF}s implement), then
	 *          the best {@link NullaryHybridCF} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link NullaryHybridCF} typed
	 *          output.
	 * @param otherArgs The operation's arguments, excluding the typed output
	 *          value.
	 * @return A {@link NullaryHybridCF} with populated inputs, ready to use.
	 */
	public static <O, OP extends Op> NullaryHybridCF<O> nullaryCF(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final NullaryHybridCF<O> op = SpecialOp.op(ops, opType,
			NullaryHybridCF.class, null, OpUtils.args(otherArgs, outType));
		return op;
	}

	/**
	 * Gets the best {@link NullaryHybridCF} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link NullaryHybridCF}s share this type (e.g., the type is an
	 *          interface which multiple {@link NullaryHybridCF}s implement), then
	 *          the best {@link NullaryHybridCF} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param otherArgs The operation's arguments, excluding the typed output
	 *          value.
	 * @return A {@link NullaryHybridCF} with populated inputs, ready to use.
	 */
	public static <O, OP extends Op> NullaryHybridCF<O> nullaryCF(
		final OpEnvironment ops, final Class<OP> opType, final O out,
		final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final NullaryHybridCF<O> op = SpecialOp.op(ops, opType,
			NullaryHybridCF.class, null, OpUtils.args(otherArgs, out));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCF} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCF}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCF}s implement), then
	 *          the best {@link UnaryHybridCF} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryHybridCF} typed output.
	 * @param inType The {@link Class} of the {@link UnaryHybridCF} typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCF} with populated inputs, ready to use.
	 */
	public static <I, O, OP extends Op> UnaryHybridCF<I, O> unaryCF(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Class<I> inType, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCF<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCF.class, null, OpUtils.args(otherArgs, outType, inType));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCF} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCF}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCF}s implement), then
	 *          the best {@link UnaryHybridCF} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryHybridCF} typed output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCF} with populated inputs, ready to use.
	 */
	public static <I, O, OP extends Op> UnaryHybridCF<I, O> unaryCF(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final I in, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCF<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCF.class, null, OpUtils.args(otherArgs, outType, in));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCF} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCF}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCF}s implement), then
	 *          the best {@link UnaryHybridCF} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCF} with populated inputs, ready to use.
	 */
	public static <I, O, OP extends Op> UnaryHybridCF<I, O> unaryCF(
		final OpEnvironment ops, final Class<OP> opType, final O out, final I in,
		final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCF<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCF.class, null, OpUtils.args(otherArgs, out, in));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCFI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCFI}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCFI}s implement), then
	 *          the best {@link UnaryHybridCFI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link UnaryHybridCFI} typed
	 *          arguments.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <A, OP extends Op> UnaryHybridCFI<A> unaryCFI(
		final OpEnvironment ops, final Class<OP> opType, final Class<A> argType,
		final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCFI<A> op = SpecialOp.op(ops, opType, UnaryHybridCFI.class,
			null, OpUtils.args(otherArgs, argType, argType));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCFI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCFI}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCFI}s implement), then
	 *          the best {@link UnaryHybridCFI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link UnaryHybridCFI} typed
	 *          arguments.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <A, OP extends Op> UnaryHybridCFI<A> unaryCFI(
		final OpEnvironment ops, final Class<OP> opType, final Class<A> argType,
		final A in, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCFI<A> op = SpecialOp.op(ops, opType, UnaryHybridCFI.class,
			null, OpUtils.args(otherArgs, argType, in));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCFI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCFI}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCFI}s implement), then
	 *          the best {@link UnaryHybridCFI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <A, OP extends Op> UnaryHybridCFI<A> unaryCFI(
		final OpEnvironment ops, final Class<OP> opType, final A out, final A in,
		final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCFI<A> op = SpecialOp.op(ops, opType, UnaryHybridCFI.class,
			null, OpUtils.args(otherArgs, out, in));
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCF} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCF}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCF}s implement), then
	 *          the best {@link BinaryHybridCF} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryHybridCF} typed
	 *          output.
	 * @param in1Type The {@link Class} of the {@link BinaryHybridCF} first typed
	 *          input.
	 * @param in2Type The {@link Class} of the {@link BinaryHybridCF} second typed
	 *          input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCF} with populated inputs, ready to use.
	 */
	public static <I1, I2, O, OP extends Op> BinaryHybridCF<I1, I2, O> binaryCF(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final Class<I1> in1Type, final Class<I2> in2Type,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1Type, in2Type);
		@SuppressWarnings("unchecked")
		final BinaryHybridCF<I1, I2, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCF.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCF} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCF}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCF}s implement), then
	 *          the best {@link BinaryHybridCF} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryHybridCF} typed
	 *          output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCF} with populated inputs, ready to use.
	 */
	public static <I1, I2, O, OP extends Op> BinaryHybridCF<I1, I2, O> binaryCF(
		final OpEnvironment ops, final Class<OP> opType, final Class<O> outType,
		final I1 in1, final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCF<I1, I2, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCF.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCF} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCF}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCF}s implement), then
	 *          the best {@link BinaryHybridCF} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCF} with populated inputs, ready to use.
	 */
	public static <I1, I2, O, OP extends Op> BinaryHybridCF<I1, I2, O> binaryCF(
		final OpEnvironment ops, final Class<OP> opType, final O out, final I1 in1,
		final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCF<I1, I2, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCF.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCFI1} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCFI1}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCFI1}s implement),
	 *          then the best {@link BinaryHybridCFI1} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link BinaryHybridCFI1} typed
	 *          output and first input.
	 * @param in2Type The {@link Class} of the {@link BinaryHybridCFI1} second
	 *          typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI1} with populated inputs, ready to use.
	 */
	public static <A, I, OP extends Op> BinaryHybridCFI1<A, I> binaryCFI1(
		final OpEnvironment ops, final Class<OP> opType, final Class<A> argType,
		final Class<I> in2Type, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, argType, argType, in2Type);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI1<A, I> op = SpecialOp.op(ops, opType,
			BinaryHybridCFI1.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCFI1} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCFI1}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCFI1}s implement),
	 *          then the best {@link BinaryHybridCFI1} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link BinaryHybridCFI1} typed
	 *          output and first input.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI1} with populated inputs, ready to use.
	 */
	public static <A, I, OP extends Op> BinaryHybridCFI1<A, I> binaryCFI1(
		final OpEnvironment ops, final Class<OP> opType, final Class<A> argType,
		final A in1, final I in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, argType, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI1<A, I> op = SpecialOp.op(ops, opType,
			BinaryHybridCFI1.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCFI1} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCFI1}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCFI1}s implement),
	 *          then the best {@link BinaryHybridCFI1} implementation to use will
	 *          be selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI1} with populated inputs, ready to use.
	 */
	public static <A, I, OP extends Op> BinaryHybridCFI1<A, I> binaryCFI1(
		final OpEnvironment ops, final Class<OP> opType, final A out, final A in1,
		final I in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI1<A, I> op = SpecialOp.op(ops, opType,
			BinaryHybridCFI1.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCFI} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCFI}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCFI}s implement), then
	 *          the best {@link BinaryHybridCFI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link BinaryHybridCFI} typed
	 *          arguments.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <A, I, OP extends Op> BinaryHybridCFI<A> binaryCFI(
		final OpEnvironment ops, final Class<OP> opType, final Class<A> argType,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, argType, argType, argType);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI<A> op = SpecialOp.op(ops, opType,
			BinaryHybridCFI.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCFI} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCFI}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCFI}s implement), then
	 *          the best {@link BinaryHybridCFI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link BinaryHybridCFI} typed
	 *          arguments.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <A, I, OP extends Op> BinaryHybridCFI<A> binaryCFI(
		final OpEnvironment ops, final Class<OP> opType, final Class<A> argType,
		final A in1, final I in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, argType, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI<A> op = SpecialOp.op(ops, opType,
			BinaryHybridCFI.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCFI} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCFI}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCFI}s implement), then
	 *          the best {@link BinaryHybridCFI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <A, I, OP extends Op> BinaryHybridCFI<A> binaryCFI(
		final OpEnvironment ops, final Class<OP> opType, final A out, final A in1,
		final I in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI<A> op = SpecialOp.op(ops, opType,
			BinaryHybridCFI.class, null, args);
		return op;
	}

}
