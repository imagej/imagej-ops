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
	public static <O> NullaryHybridCF<O> nullaryCF(final OpEnvironment ops,
		final Class<? extends Op> opType, final Class<O> outType,
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
	public static <O> NullaryHybridCF<O> nullaryCF(final OpEnvironment ops,
		final Class<? extends Op> opType, final O out, final Object... otherArgs)
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
	public static <I, O> UnaryHybridCF<I, O> unaryCF(final OpEnvironment ops,
		final Class<? extends Op> opType, final Class<O> outType,
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
	public static <I, O> UnaryHybridCF<I, O> unaryCF(final OpEnvironment ops,
		final Class<? extends Op> opType, final Class<O> outType, final I in,
		final Object... otherArgs)
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
	public static <I, O> UnaryHybridCF<I, O> unaryCF(final OpEnvironment ops,
		final Class<? extends Op> opType, final O out, final I in,
		final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCF<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCF.class, null, OpUtils.args(otherArgs, out, in));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCI}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCI}s implement), then
	 *          the best {@link UnaryHybridCI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryHybridCI} typed output.
	 * @param inType The {@link Class} of the {@link UnaryHybridCI} typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> UnaryHybridCI<I, O> unaryCI(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final Class<I> inType, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCI<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCI.class, null, OpUtils.args(otherArgs, outType, inType));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCI}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCI}s implement), then
	 *          the best {@link UnaryHybridCI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link UnaryHybridCI} typed output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> UnaryHybridCI<I, O> unaryCI(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final I in, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCI<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCI.class, null, OpUtils.args(otherArgs, outType, in));
		return op;
	}

	/**
	 * Gets the best {@link UnaryHybridCI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link UnaryHybridCI}s share this type (e.g., the type is an
	 *          interface which multiple {@link UnaryHybridCI}s implement), then
	 *          the best {@link UnaryHybridCI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> UnaryHybridCI<I, O> unaryCI(
		final OpEnvironment ops, final Class<? extends Op> opType, final O out,
		final I in, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCI<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCI.class, null, OpUtils.args(otherArgs, out, in));
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
	 * @param outType The {@link Class} of the {@link UnaryHybridCFI} typed
	 *          output.
	 * @param inType The {@link Class} of the {@link UnaryHybridCFI} typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> UnaryHybridCFI<I, O> unaryCFI(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final Class<I> inType, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCFI<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCFI.class, null, OpUtils.args(otherArgs, outType, inType));
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
	 * @param outType The {@link Class} of the {@link UnaryHybridCFI} typed
	 *          output.
	 * @param in The typed input.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return A {@link UnaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> UnaryHybridCFI<I, O> unaryCFI(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final I in, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCFI<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCFI.class, null, OpUtils.args(otherArgs, outType, in));
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
	public static <I, O extends I> UnaryHybridCFI<I, O> unaryCFI(
		final OpEnvironment ops, final Class<? extends Op> opType, final O out,
		final I in, final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryHybridCFI<I, O> op = SpecialOp.op(ops, opType,
			UnaryHybridCFI.class, null, OpUtils.args(otherArgs, out, in));
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
	public static <I1, I2, O> BinaryHybridCF<I1, I2, O> binaryCF(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final Class<I1> in1Type, final Class<I2> in2Type,
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
	public static <I1, I2, O> BinaryHybridCF<I1, I2, O> binaryCF(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final I1 in1, final I2 in2,
		final Object... otherArgs)
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
	public static <I1, I2, O> BinaryHybridCF<I1, I2, O> binaryCF(
		final OpEnvironment ops, final Class<? extends Op> opType, final O out,
		final I1 in1, final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCF<I1, I2, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCF.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCI1} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCI1}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCI1}s implement), then
	 *          the best {@link BinaryHybridCI1} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryHybridCI1} typed
	 *          output.
	 * @param in1Type The {@link Class} of the {@link BinaryHybridCI1} first typed
	 *          input.
	 * @param in2Type The {@link Class} of the {@link BinaryHybridCI1} second
	 *          typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCI1} with populated inputs, ready to use.
	 */
	public static <I1, I2, O extends I1> BinaryHybridCI1<I1, I2, O> binaryCI1(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final Class<I1> in1Type, final Class<I2> in2Type,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1Type, in2Type);
		@SuppressWarnings("unchecked")
		final BinaryHybridCI1<I1, I2, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCI1.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCI1} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCI1}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCI1}s implement), then
	 *          the best {@link BinaryHybridCI1} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryHybridCI1} typed
	 *          output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCI1} with populated inputs, ready to use.
	 */
	public static <I1, I2, O extends I1> BinaryHybridCI1<I1, I2, O> binaryCI1(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final I1 in1, final I2 in2,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCI1<I1, I2, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCI1.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCI1} implementation for the given types
	 * and arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCI1}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCI1}s implement), then
	 *          the best {@link BinaryHybridCI1} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCI1} with populated inputs, ready to use.
	 */
	public static <I1, I2, O extends I1> BinaryHybridCI1<I1, I2, O> binaryCI1(
		final OpEnvironment ops, final Class<? extends Op> opType, final O out,
		final I1 in1, final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCI1<I1, I2, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCI1.class, null, args);
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
	 * @param outType The {@link Class} of the {@link BinaryHybridCFI1} typed
	 *          output.
	 * @param in1Type The {@link Class} of the {@link BinaryHybridCFI1} first
	 *          typed input.
	 * @param in2Type The {@link Class} of the {@link BinaryHybridCFI1} second
	 *          typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI1} with populated inputs, ready to use.
	 */
	public static <I1, I2, O extends I1> BinaryHybridCFI1<I1, I2, O> binaryCFI1(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final Class<I1> in1Type, final Class<I2> in2Type,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1Type, in2Type);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI1<I1, I2, O> op = SpecialOp.op(ops, opType,
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
	 * @param outType The {@link Class} of the {@link BinaryHybridCFI1} typed
	 *          output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI1} with populated inputs, ready to use.
	 */
	public static <I1, I2, O extends I1> BinaryHybridCFI1<I1, I2, O> binaryCFI1(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final I1 in1, final I2 in2,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI1<I1, I2, O> op = SpecialOp.op(ops, opType,
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
	public static <I1, I2, O extends I1> BinaryHybridCFI1<I1, I2, O> binaryCFI1(
		final OpEnvironment ops, final Class<? extends Op> opType, final O out,
		final I1 in1, final I2 in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI1<I1, I2, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCFI1.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCI}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCI}s implement), then
	 *          the best {@link BinaryHybridCI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryHybridCI} typed
	 *          output.
	 * @param inType The {@link Class} of the {@link BinaryHybridCI} typed inputs.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> BinaryHybridCI<I, O> binaryCI(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final Class<I> inType, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, inType, inType);
		@SuppressWarnings("unchecked")
		final BinaryHybridCI<I, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCI.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCI}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCI}s implement), then
	 *          the best {@link BinaryHybridCI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param outType The {@link Class} of the {@link BinaryHybridCI} typed
	 *          output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> BinaryHybridCI<I, O> binaryCI(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final I in1, final I in2,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCI<I, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCI.class, null, args);
		return op;
	}

	/**
	 * Gets the best {@link BinaryHybridCI} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link BinaryHybridCI}s share this type (e.g., the type is an
	 *          interface which multiple {@link BinaryHybridCI}s implement), then
	 *          the best {@link BinaryHybridCI} implementation to use will be
	 *          selected automatically from the type and arguments.
	 * @param out The typed output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> BinaryHybridCI<I, O> binaryCI(
		final OpEnvironment ops, final Class<? extends Op> opType, final O out,
		final I in1, final I in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCI<I, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCI.class, null, args);
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
	 * @param outType The {@link Class} of the {@link BinaryHybridCFI} typed
	 *          output.
	 * @param inType The {@link Class} of the {@link BinaryHybridCFI} typed
	 *          inputs.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> BinaryHybridCFI<I, O> binaryCFI(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final Class<I> inType, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, inType, inType);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI<I, O> op = SpecialOp.op(ops, opType,
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
	 * @param outType The {@link Class} of the {@link BinaryHybridCFI} typed
	 *          output.
	 * @param in1 The first typed input.
	 * @param in2 The second typed input.
	 * @param otherArgs The operation's arguments, excluding the typed inputs and
	 *          output values.
	 * @return A {@link BinaryHybridCFI} with populated inputs, ready to use.
	 */
	public static <I, O extends I> BinaryHybridCFI<I, O> binaryCFI(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final Class<O> outType, final I in1, final I in2,
		final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, outType, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI<I, O> op = SpecialOp.op(ops, opType,
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
	public static <I, O extends I> BinaryHybridCFI<I, O> binaryCFI(
		final OpEnvironment ops, final Class<? extends Op> opType, final O out,
		final I in1, final I in2, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, out, in1, in2);
		@SuppressWarnings("unchecked")
		final BinaryHybridCFI<I, O> op = SpecialOp.op(ops, opType,
			BinaryHybridCFI.class, null, args);
		return op;
	}

}
