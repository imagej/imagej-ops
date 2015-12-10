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
 * Utility class for looking up unary ops in a type-safe way.
 *
 * @author Curtis Rueden
 * @see InplaceOp
 */
public final class Inplaces {

	private Inplaces() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Gets the best {@link InplaceOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link InplaceOp}s share this type (e.g., the type is an interface
	 *          which multiple {@link InplaceOp}s implement), then the best
	 *          {@link InplaceOp} implementation to use will be selected
	 *          automatically from the type and arguments.
	 * @param argType The {@link Class} of the {@link InplaceOp} typed argument.
	 * @param otherArgs The operation's arguments, excluding the typed argument
	 *          value.
	 * @return An {@link InplaceOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <A, OP extends Op> InplaceOp<A> unary(final OpEnvironment ops,
		final Class<OP> opType, final Class<A> argType, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, argType);
		final OpRef<OP> ref = OpRef.createTypes(opType, InplaceOp.class, null,
			args);
		return (InplaceOp<A>) ops.op(ref);
	}

	/**
	 * Gets the best {@link InplaceOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link InplaceOp}s share this type (e.g., the type is an interface
	 *          which multiple {@link InplaceOp}s implement), then the best
	 *          {@link InplaceOp} implementation to use will be selected
	 *          automatically from the type and arguments.
	 * @param arg The typed argument.
	 * @param otherArgs The operation's arguments, excluding the typed input and
	 *          output values.
	 * @return An {@link InplaceOp} with populated inputs, ready to use.
	 */
	@SuppressWarnings("unchecked")
	public static <A, OP extends Op> InplaceOp<A> unary(final OpEnvironment ops,
		final Class<OP> opType, final A arg, final Object... otherArgs)
	{
		final Object[] args = OpUtils.args(otherArgs, arg);
		final OpRef<OP> ref = OpRef.createTypes(opType, InplaceOp.class, null,
			args);
		return (InplaceOp<A>) ops.op(ref);
	}

}
