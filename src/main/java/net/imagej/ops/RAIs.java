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

package net.imagej.ops;

import net.imglib2.RandomAccessibleInterval;

/**
 * Utility class for working with {@link RandomAccessibleInterval}s.
 *
 * @author Curtis Rueden
 */
public final class RAIs {

	private RAIs() {
		// NB: prevent instantiation of utility class.
	}

	// -- Utility methods --

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T>
		ComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		computer(final OpEnvironment ops, final Class<? extends Op> opType,
			final RandomAccessibleInterval<T> in, final Object... otherArgs)
	{
		return (ComputerOp) ops.computer(opType, RandomAccessibleInterval.class,
			in == null ? RandomAccessibleInterval.class : in, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T>
		FunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		function(final OpEnvironment ops, final Class<? extends Op> opType,
			final RandomAccessibleInterval<T> in, final Object... otherArgs)
	{
		return (FunctionOp) ops.function(opType, RandomAccessibleInterval.class,
			in == null ? RandomAccessibleInterval.class : in, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T>
		HybridOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> hybrid(
			final OpEnvironment ops, final Class<? extends Op> opType,
			final RandomAccessibleInterval<T> in, final Object... otherArgs)
	{
		return (HybridOp) ops.hybrid(opType, RandomAccessibleInterval.class,
			in == null ? RandomAccessibleInterval.class : in, otherArgs);
	}

	public static <T> InplaceOp<RandomAccessibleInterval<T>> inplace(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final RandomAccessibleInterval<T> arg, final Object... otherArgs)
	{
		return ops.inplace(opType, arg, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static
		<T>
		BinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		binaryComputer(final OpEnvironment ops, final Class<? extends Op> opType,
			final RandomAccessibleInterval<T> in1,
			final RandomAccessibleInterval<T> in2, final Object... otherArgs)
	{
		return (BinaryComputerOp) ops.binaryComputer(opType,
			RandomAccessibleInterval.class, in1, in2, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static
		<T>
		BinaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		binaryFunction(final OpEnvironment ops, final Class<? extends Op> opType,
			final RandomAccessibleInterval<T> in1,
			final RandomAccessibleInterval<T> in2, final Object... otherArgs)
	{
		return (BinaryFunctionOp) ops.binaryFunction(opType,
			RandomAccessibleInterval.class, in1, in2, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static
		<T>
		BinaryHybridOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		binaryHybrid(final OpEnvironment ops, final Class<? extends Op> opType,
			final RandomAccessibleInterval<T> in1,
			final RandomAccessibleInterval<T> in2, final Object... otherArgs)
	{
		return (BinaryHybridOp) ops.binaryHybrid(opType,
			RandomAccessibleInterval.class, in1, in2, otherArgs);
	}

}
