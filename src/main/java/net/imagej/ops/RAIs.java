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
	public static <I, O> ComputerOp<I, O> computer(
		final OpEnvironment ops, final Class<? extends Op> opType, final I in,
		final Object... otherArgs)
	{
		return (ComputerOp) ops.computer(opType, RandomAccessibleInterval.class, in, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <I, O> FunctionOp<I, O> function(
		final OpEnvironment ops, final Class<? extends Op> opType, final I in,
		final Object... otherArgs)
	{
		return (FunctionOp) ops.function(opType, RandomAccessibleInterval.class, in, otherArgs);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <I, O> HybridOp<I, O> hybrid(
		final OpEnvironment ops, final Class<? extends Op> opType, final I in,
		final Object... otherArgs)
	{
		return (HybridOp) ops.hybrid(opType, RandomAccessibleInterval.class, in, otherArgs);
	}

	public static <T> InplaceOp<RandomAccessibleInterval<T>> inplace(
		final OpEnvironment ops, final Class<? extends Op> opType,
		final RandomAccessibleInterval<T> arg, final Object... otherArgs)
	{
		return ops.inplace(opType, arg, otherArgs);
	}
	
}
