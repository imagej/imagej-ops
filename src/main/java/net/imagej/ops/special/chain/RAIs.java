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

package net.imagej.ops.special.chain;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.BinaryHybridCF;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imagej.ops.special.inplace.Inplaces;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.view.Views;

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
	public static <T> UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> computer(
			final OpEnvironment ops, final Class<? extends Op> opType, final RandomAccessibleInterval<T> in,
			final Object... otherArgs) {
		return (UnaryComputerOp) Computers.unary(ops, opType, RandomAccessibleInterval.class,
				in == null ? RandomAccessibleInterval.class : in, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, F> UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<F>> computer(
			final OpEnvironment ops, final Class<? extends Op> opType, final RandomAccessibleInterval<F> out,
			final RandomAccessibleInterval<T> in, final Object... otherArgs) {
		return (UnaryComputerOp) Computers.unary(ops, opType, out == null ? RandomAccessibleInterval.class : out,
				RandomAccessibleInterval.class, in == null ? RandomAccessibleInterval.class : in, otherArgs);
	}

	/**
	 * Extends an input using an {@link OutOfBoundsFactory}, if available,
	 * otherwise returns the unchanged input.
	 *
	 * @param in
	 *            {@link RandomAccessibleInterval} that is to be extended
	 * @param outOfBounds
	 *            the factory that is used for extending
	 * @return {@link RandomAccessibleInterval} extended using the
	 *         {@link OutOfBoundsFactory} with the interval of in
	 */
	public static <T> RandomAccessibleInterval<T> extend(final RandomAccessibleInterval<T> in,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds) {
		return outOfBounds == null ? in : Views.interval((Views.extend(in, outOfBounds)), in);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> function(
			final OpEnvironment ops, final Class<? extends Op> opType, final RandomAccessibleInterval<T> in,
			final Object... otherArgs) {
		return (UnaryFunctionOp) Functions.unary(ops, opType, RandomAccessibleInterval.class,
				in == null ? RandomAccessibleInterval.class : in, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> UnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> hybrid(
			final OpEnvironment ops, final Class<? extends Op> opType, final RandomAccessibleInterval<T> in,
			final Object... otherArgs) {
		return (UnaryHybridCF) Hybrids.unaryCF(ops, opType, RandomAccessibleInterval.class,
				in == null ? RandomAccessibleInterval.class : in, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, F> UnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<F>> hybrid(
			final OpEnvironment ops, final Class<? extends Op> opType, final RandomAccessibleInterval<F> out,
			final RandomAccessibleInterval<T> in, final Object... otherArgs) {
		return (UnaryHybridCF) Hybrids.unaryCF(ops, opType, out == null ? RandomAccessibleInterval.class : out,
				in == null ? RandomAccessibleInterval.class : in, otherArgs);
	}

	public static <T> UnaryInplaceOp<? super RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> inplace(final OpEnvironment ops,
			final Class<? extends Op> opType, final RandomAccessibleInterval<T> arg, final Object... otherArgs) {
		return Inplaces.unary(ops, opType, arg, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> BinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> binaryComputer(
			final OpEnvironment ops, final Class<? extends Op> opType, final RandomAccessibleInterval<T> in1,
			final RandomAccessibleInterval<T> in2, final Object... otherArgs) {
		return (BinaryComputerOp) Computers.binary(ops, opType, RandomAccessibleInterval.class, in1, in2, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> BinaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> binaryFunction(
			final OpEnvironment ops, final Class<? extends Op> opType, final RandomAccessibleInterval<T> in1,
			final RandomAccessibleInterval<T> in2, final Object... otherArgs) {
		return (BinaryFunctionOp) Functions.binary(ops, opType, RandomAccessibleInterval.class, in1, in2, otherArgs);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> BinaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> binaryHybrid(
			final OpEnvironment ops, final Class<? extends Op> opType, final RandomAccessibleInterval<T> in1,
			final RandomAccessibleInterval<T> in2, final Object... otherArgs) {
		return (BinaryHybridCF) Hybrids.binaryCF(ops, opType, RandomAccessibleInterval.class, in1, in2, otherArgs);
	}

}
