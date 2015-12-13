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

package net.imagej.ops.chain;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.special.BinaryComputerOp;
import net.imagej.ops.special.BinaryFunctionOp;
import net.imagej.ops.special.BinaryHybridOp;
import net.imagej.ops.special.Computers;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.Hybrids;
import net.imagej.ops.special.InplaceOp;
import net.imagej.ops.special.Inplaces;
import net.imagej.ops.special.UnaryComputerOp;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imagej.ops.special.UnaryHybridOp;
import net.imglib2.type.numeric.RealType;

/**
 * Utility class for working with {@link RealType}s.
 *
 * @author Curtis Rueden
 */
public final class RTs {

	private RTs() {
		// NB: prevent instantiation of utility class.
	}

	// -- Utility methods --

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <I, O extends RealType<O>> UnaryComputerOp<I, O> computer(
		final OpEnvironment ops, final Class<? extends Op> opType, final I in,
		final Object... otherArgs)
	{
		return (UnaryComputerOp) Computers.unary(ops, opType, RealType.class, in, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <I, O extends RealType<O>> UnaryFunctionOp<I, O> function(
		final OpEnvironment ops, final Class<? extends Op> opType, final I in,
		final Object... otherArgs)
	{
		return (UnaryFunctionOp) Functions.unary(ops, opType, RealType.class, in, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <I, O extends RealType<O>> UnaryHybridOp<I, O> hybrid(
		final OpEnvironment ops, final Class<? extends Op> opType, final I in,
		final Object... otherArgs)
	{
		return (UnaryHybridOp) Hybrids.unary(ops, opType, RealType.class, in, otherArgs);
	}

	public static <A extends RealType<A>> InplaceOp<A> inplace(
		final OpEnvironment ops, final Class<? extends Op> opType, final A arg,
		final Object... otherArgs)
	{
		return Inplaces.unary(ops, opType, arg, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <I1, I2, O extends RealType<O>> BinaryComputerOp<I1, I2, O>
		binaryComputer(final OpEnvironment ops, final Class<? extends Op> opType,
			final I1 in1, final I2 in2, final Object... otherArgs)
	{
		return (BinaryComputerOp) Computers.binary(ops, opType, RealType.class, in1,
			in2, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <I1, I2, O extends RealType<O>> BinaryFunctionOp<I1, I2, O>
		binaryFunction(final OpEnvironment ops, final Class<? extends Op> opType,
			final I1 in1, I2 in2, final Object... otherArgs)
	{
		return (BinaryFunctionOp) Functions.binary(ops, opType, RealType.class, in1,
			in2, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <I1, I2, O extends RealType<O>> BinaryHybridOp<I1, I2, O>
		binaryHybrid(final OpEnvironment ops, final Class<? extends Op> opType,
			final I1 in1, I2 in2, final Object... otherArgs)
	{
		return (BinaryHybridOp) Hybrids.binary(ops, opType, RealType.class, in1, in2,
			otherArgs);
	}

}
