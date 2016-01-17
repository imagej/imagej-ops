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

package net.imagej.ops.special.inplace;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpUtils;
import net.imagej.ops.special.SpecialOp;

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
	public static <A, OP extends Op> UnaryInplaceOp<A> unary(
		final OpEnvironment ops, final Class<OP> opType, final Class<A> argType,
		final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryInplaceOp<A> op = SpecialOp.op(ops, opType, UnaryInplaceOp.class,
			null, OpUtils.args(otherArgs, argType));
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
	public static <A, OP extends Op> UnaryInplaceOp<A> unary(
		final OpEnvironment ops, final Class<OP> opType, final A arg,
		final Object... otherArgs)
	{
		@SuppressWarnings("unchecked")
		final UnaryInplaceOp<A> op = SpecialOp.op(ops, opType, UnaryInplaceOp.class,
			null, OpUtils.args(otherArgs, arg));
		return op;
	}

}
