/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

package net.imagej.ops.map;

import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.NullaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.inplace.BinaryInplace1Op;
import net.imagej.ops.special.inplace.BinaryInplaceOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;

/**
 * Utility class for {@link MapOp}s.
 *
 * @author Leon Yang
 */
public class Maps {

	private Maps() {
		// NB: Prevent instantiation of utility class.
	}

	// -- Helpers for conforms() --

	public static <I, O> boolean compatible(final IterableInterval<I> a,
		final IterableInterval<O> b)
	{
		return a.iterationOrder().equals(b.iterationOrder());
	}

	public static <I, O> boolean compatible(final IterableInterval<I> a,
		final RandomAccessibleInterval<O> b)
	{
		return Intervals.contains(b, a);
	}

	public static <I, O> boolean compatible(final RandomAccessibleInterval<I> a,
		final IterableInterval<O> b)
	{
		return Intervals.contains(a, b);
	}

	public static <I1, I2, O> boolean compatible(final IterableInterval<I1> a,
		final IterableInterval<I2> b, final IterableInterval<O> c)
	{
		return a.iterationOrder().equals(b.iterationOrder()) && a.iterationOrder()
			.equals(c.iterationOrder());
	}

	public static <I1, I2, O> boolean compatible(final IterableInterval<I1> a,
		final IterableInterval<I2> b, final RandomAccessibleInterval<O> c)
	{
		return a.iterationOrder().equals(b.iterationOrder()) && Intervals.contains(
			c, a);
	}

	public static <I1, I2, O> boolean compatible(final IterableInterval<I1> a,
		final RandomAccessibleInterval<I2> b, final IterableInterval<O> c)
	{
		return a.iterationOrder().equals(c.iterationOrder()) && Intervals.contains(
			b, a);
	}

	public static <I1, I2, O> boolean compatible(
		final RandomAccessibleInterval<I1> a, final IterableInterval<I2> b,
		final IterableInterval<O> c)
	{
		return b.iterationOrder().equals(c.iterationOrder()) && Intervals.contains(
			a, b);
	}

	public static <I1, I2, O> boolean compatible(final IterableInterval<I1> a,
		final RandomAccessibleInterval<I2> b, final RandomAccessibleInterval<O> c)
	{
		return Intervals.contains(b, a) && Intervals.contains(c, a);
	}

	public static <I1, I2, O> boolean compatible(
		final RandomAccessibleInterval<I1> a, final IterableInterval<I2> b,
		final RandomAccessibleInterval<O> c)
	{
		return Intervals.contains(a, b) && Intervals.contains(c, b);
	}

	public static <I1, I2, O> boolean compatible(
		final RandomAccessibleInterval<I1> a, final RandomAccessibleInterval<I2> b,
		final IterableInterval<O> c)
	{
		return Intervals.contains(a, c) && Intervals.contains(b, c);
	}

	// -- Nullary Maps --

	public static <O> void map(final Iterable<O> a,
		final NullaryComputerOp<O> op)
	{
		for (final O e : a)
			op.compute(e);
	}

	public static <O> void map(final IterableInterval<O> a,
		final NullaryComputerOp<O> op, final int startIndex, final int stepSize,
		final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<O> aCursor = a.cursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			aCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			op.compute(aCursor.get());
		}
	}

	// -- Unary Maps --

	public static <I, O> void map(final IterableInterval<I> a,
		final IterableInterval<O> b, final UnaryComputerOp<I, O> op)
	{
		final Cursor<I> aCursor = a.cursor();
		final Cursor<O> bCursor = b.cursor();
		while (aCursor.hasNext()) {
			op.compute(aCursor.next(), bCursor.next());
		}
	}

	public static <I, O> void map(final IterableInterval<I> a,
		final RandomAccessibleInterval<O> b, final UnaryComputerOp<I, O> op)
	{
		final Cursor<I> aCursor = a.localizingCursor();
		final RandomAccess<O> bAccess = b.randomAccess();
		while (aCursor.hasNext()) {
			aCursor.fwd();
			bAccess.setPosition(aCursor);
			op.compute(aCursor.get(), bAccess.get());
		}
	}

	public static <I, O> void map(final RandomAccessibleInterval<I> a,
		final IterableInterval<O> b, final UnaryComputerOp<I, O> op)
	{
		final RandomAccess<I> aAccess = a.randomAccess();
		final Cursor<O> bCursor = b.localizingCursor();
		while (bCursor.hasNext()) {
			bCursor.fwd();
			aAccess.setPosition(bCursor);
			op.compute(aAccess.get(), bCursor.get());
		}
	}

	// -- Binary Maps --

	public static <I1, I2, O> void map(final IterableInterval<I1> a,
		final IterableInterval<I2> b, final IterableInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op)
	{
		final Cursor<I1> aCursor = a.cursor();
		final Cursor<I2> bCursor = b.cursor();
		final Cursor<O> cCursor = c.cursor();
		while (aCursor.hasNext()) {
			op.compute(aCursor.next(), bCursor.next(), cCursor.next());
		}
	}

	public static <I1, I2, O> void map(final IterableInterval<I1> a,
		final IterableInterval<I2> b, final RandomAccessibleInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op)
	{
		final Cursor<I1> aCursor = a.localizingCursor();
		final Cursor<I2> bCursor = b.cursor();
		final RandomAccess<O> cAccess = c.randomAccess();
		while (aCursor.hasNext()) {
			aCursor.fwd();
			cAccess.setPosition(aCursor);
			op.compute(aCursor.get(), bCursor.next(), cAccess.get());
		}
	}

	public static <I1, I2, O> void map(final IterableInterval<I1> a,
		final RandomAccessibleInterval<I2> b, final IterableInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op)
	{
		final Cursor<I1> aCursor = a.localizingCursor();
		final RandomAccess<I2> bAccess = b.randomAccess();
		final Cursor<O> cCursor = c.cursor();
		while (aCursor.hasNext()) {
			aCursor.fwd();
			bAccess.setPosition(aCursor);
			op.compute(aCursor.get(), bAccess.get(), cCursor.next());
		}
	}

	public static <I1, I2, O> void map(final RandomAccessibleInterval<I1> a,
		final IterableInterval<I2> b, final IterableInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op)
	{
		final RandomAccess<I1> aAccess = a.randomAccess();
		final Cursor<I2> bCursor = b.localizingCursor();
		final Cursor<O> cCursor = c.cursor();
		while (bCursor.hasNext()) {
			bCursor.fwd();
			aAccess.setPosition(bCursor);
			op.compute(aAccess.get(), bCursor.get(), cCursor.next());
		}
	}

	public static <I1, I2, O> void map(final IterableInterval<I1> a,
		final RandomAccessibleInterval<I2> b, final RandomAccessibleInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op)
	{
		final Cursor<I1> aCursor = a.localizingCursor();
		final RandomAccess<I2> bAccess = b.randomAccess();
		final RandomAccess<O> cAccess = c.randomAccess();
		while (aCursor.hasNext()) {
			aCursor.fwd();
			bAccess.setPosition(aCursor);
			cAccess.setPosition(aCursor);
			op.compute(aCursor.get(), bAccess.get(), cAccess.get());
		}
	}

	public static <I1, I2, O> void map(final RandomAccessibleInterval<I1> a,
		final IterableInterval<I2> b, final RandomAccessibleInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op)
	{
		final RandomAccess<I1> aAccess = a.randomAccess();
		final Cursor<I2> bCursor = b.localizingCursor();
		final RandomAccess<O> cAccess = c.randomAccess();
		while (bCursor.hasNext()) {
			bCursor.fwd();
			aAccess.setPosition(bCursor);
			cAccess.setPosition(bCursor);
			op.compute(aAccess.get(), bCursor.get(), cAccess.get());
		}
	}

	public static <I1, I2, O> void map(final RandomAccessibleInterval<I1> a,
		final RandomAccessibleInterval<I2> b, final IterableInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op)
	{
		final RandomAccess<I1> aAccess = a.randomAccess();
		final RandomAccess<I2> bAccess = b.randomAccess();
		final Cursor<O> cCursor = c.localizingCursor();
		while (cCursor.hasNext()) {
			cCursor.fwd();
			aAccess.setPosition(cCursor);
			bAccess.setPosition(cCursor);
			op.compute(aAccess.get(), bAccess.get(), cCursor.get());
		}
	}

	// -- Parallel Unary Maps --

	public static <I, O> void map(final IterableInterval<I> a,
		final IterableInterval<O> b, final UnaryComputerOp<I, O> op,
		final int startIndex, final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<I> aCursor = a.cursor();
		final Cursor<O> bCursor = b.cursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			final int m = ctr == 0 ? startIndex + 1 : stepSize;
			aCursor.jumpFwd(m);
			bCursor.jumpFwd(m);
			op.compute(aCursor.get(), bCursor.get());
		}
	}

	public static <I, O> void map(final IterableInterval<I> a,
		final RandomAccessibleInterval<O> b, final UnaryComputerOp<I, O> op,
		final int startIndex, final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<I> aCursor = a.localizingCursor();
		final RandomAccess<O> bAccess = b.randomAccess();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			aCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			bAccess.setPosition(aCursor);
			op.compute(aCursor.get(), bAccess.get());
		}
	}

	public static <I, O> void map(final RandomAccessibleInterval<I> a,
		final IterableInterval<O> b, final UnaryComputerOp<I, O> op,
		final int startIndex, final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final RandomAccess<I> aAccess = a.randomAccess();
		final Cursor<O> bCursor = b.localizingCursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			bCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			aAccess.setPosition(bCursor);
			op.compute(aAccess.get(), bCursor.get());
		}
	}

	// -- Parallel Binary Maps --

	public static <I1, I2, O> void map(final IterableInterval<I1> a,
		final IterableInterval<I2> b, final IterableInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op, final int startIndex,
		final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<I1> aCursor = a.cursor();
		final Cursor<I2> bCursor = b.cursor();
		final Cursor<O> cCursor = c.cursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			final int m = ctr == 0 ? startIndex + 1 : stepSize;
			aCursor.jumpFwd(m);
			bCursor.jumpFwd(m);
			cCursor.jumpFwd(m);
			op.compute(aCursor.get(), bCursor.get(), cCursor.get());
		}
	}

	public static <I1, I2, O> void map(final IterableInterval<I1> a,
		final IterableInterval<I2> b, final RandomAccessibleInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op, final int startIndex,
		final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<I1> aCursor = a.localizingCursor();
		final Cursor<I2> bCursor = b.cursor();
		final RandomAccess<O> cAccess = c.randomAccess();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			final int m = ctr == 0 ? startIndex + 1 : stepSize;
			aCursor.jumpFwd(m);
			bCursor.jumpFwd(m);
			cAccess.setPosition(aCursor);
			op.compute(aCursor.get(), bCursor.get(), cAccess.get());
		}
	}

	public static <I1, I2, O> void map(final IterableInterval<I1> a,
		final RandomAccessibleInterval<I2> b, final IterableInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op, final int startIndex,
		final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<I1> aCursor = a.localizingCursor();
		final RandomAccess<I2> bAccess = b.randomAccess();
		final Cursor<O> cCursor = c.cursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			final int m = ctr == 0 ? startIndex + 1 : stepSize;
			aCursor.jumpFwd(m);
			cCursor.jumpFwd(m);
			bAccess.setPosition(aCursor);
			op.compute(aCursor.get(), bAccess.get(), cCursor.get());
		}
	}

	public static <I1, I2, O> void map(final RandomAccessibleInterval<I1> a,
		final IterableInterval<I2> b, final IterableInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op, final int startIndex,
		final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final RandomAccess<I1> aAccess = a.randomAccess();
		final Cursor<I2> bCursor = b.localizingCursor();
		final Cursor<O> cCursor = c.cursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			final int m = ctr == 0 ? startIndex + 1 : stepSize;
			bCursor.jumpFwd(m);
			cCursor.jumpFwd(m);
			aAccess.setPosition(bCursor);
			op.compute(aAccess.get(), bCursor.get(), cCursor.get());
		}
	}

	public static <I1, I2, O> void map(final IterableInterval<I1> a,
		final RandomAccessibleInterval<I2> b, final RandomAccessibleInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op, final int startIndex,
		final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<I1> aCursor = a.localizingCursor();
		final RandomAccess<I2> bAccess = b.randomAccess();
		final RandomAccess<O> cAccess = c.randomAccess();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			aCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			bAccess.setPosition(aCursor);
			cAccess.setPosition(aCursor);
			op.compute(aCursor.get(), bAccess.get(), cAccess.get());
		}
	}

	public static <I1, I2, O> void map(final RandomAccessibleInterval<I1> a,
		final IterableInterval<I2> b, final RandomAccessibleInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op, final int startIndex,
		final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final RandomAccess<I1> aAccess = a.randomAccess();
		final Cursor<I2> bCursor = b.localizingCursor();
		final RandomAccess<O> cAccess = c.randomAccess();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			bCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			aAccess.setPosition(bCursor);
			cAccess.setPosition(bCursor);
			op.compute(aAccess.get(), bCursor.get(), cAccess.get());
		}
	}

	public static <I1, I2, O> void map(final RandomAccessibleInterval<I1> a,
		final RandomAccessibleInterval<I2> b, final IterableInterval<O> c,
		final BinaryComputerOp<I1, I2, O> op, final int startIndex,
		final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final RandomAccess<I1> aAccess = a.randomAccess();
		final RandomAccess<I2> bAccess = b.randomAccess();
		final Cursor<O> cCursor = c.localizingCursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			cCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			aAccess.setPosition(cCursor);
			bAccess.setPosition(cCursor);
			op.compute(aAccess.get(), bAccess.get(), cCursor.get());
		}
	}

	// -- Unary Inplace Maps --

	public static <I, O extends I> void inplace(final Iterable<O> arg,
		final UnaryInplaceOp<I, O> op)
	{
		for (final O e : arg)
			op.mutate(e);
	}

	public static <I, O extends I> void inplace(final IterableInterval<O> arg,
		final UnaryInplaceOp<I, O> op, final int startIndex, final int stepSize,
		final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<O> argCursor = arg.cursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			argCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			op.mutate(argCursor.get());
		}
	}

	// -- Binary Inplace Maps --

	public static <I1, I2, O extends I1> void inplace(
		final IterableInterval<O> arg, final IterableInterval<I2> in,
		final BinaryInplace1Op<I1, I2, O> op)
	{
		final Cursor<O> argCursor = arg.cursor();
		final Cursor<I2> inCursor = in.cursor();
		while (argCursor.hasNext()) {
			op.mutate1(argCursor.next(), inCursor.next());
		}
	}

	public static <I1, I2, O extends I1> void inplace(
		final IterableInterval<O> arg, final RandomAccessibleInterval<I2> in,
		final BinaryInplace1Op<I1, I2, O> op)
	{
		final Cursor<O> argCursor = arg.localizingCursor();
		final RandomAccess<I2> inAccess = in.randomAccess();
		while (argCursor.hasNext()) {
			argCursor.fwd();
			inAccess.setPosition(argCursor);
			op.mutate1(argCursor.get(), inAccess.get());
		}
	}

	public static <A, I> void inplace(final RandomAccessibleInterval<A> arg,
		final IterableInterval<I> in, final BinaryInplace1Op<A, I, A> op)
	{
		final RandomAccess<A> argAccess = arg.randomAccess();
		final Cursor<I> inCursor = in.localizingCursor();
		while (inCursor.hasNext()) {
			inCursor.fwd();
			argAccess.setPosition(inCursor);
			op.mutate1(argAccess.get(), inCursor.get());
		}
	}

	public static <A, I> void inplace(final IterableInterval<A> arg,
		final IterableInterval<I> in, final BinaryInplace1Op<A, I, A> op,
		final int startIndex, final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<A> argCursor = arg.cursor();
		final Cursor<I> inCursor = in.cursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			final int m = ctr == 0 ? startIndex + 1 : stepSize;
			argCursor.jumpFwd(m);
			inCursor.jumpFwd(m);
			op.mutate1(argCursor.get(), inCursor.get());
		}
	}

	public static <A, I> void inplace(final IterableInterval<A> arg,
		final RandomAccessibleInterval<I> in, final BinaryInplace1Op<A, I, A> op,
		final int startIndex, final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<A> argCursor = arg.localizingCursor();
		final RandomAccess<I> inAccess = in.randomAccess();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			argCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			inAccess.setPosition(argCursor);
			op.mutate1(argCursor.get(), inAccess.get());
		}
	}

	public static <A, I> void inplace(final RandomAccessibleInterval<A> arg,
		final IterableInterval<I> in, final BinaryInplace1Op<A, I, A> op,
		final int startIndex, final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final RandomAccess<A> argAccess = arg.randomAccess();
		final Cursor<I> inCursor = in.localizingCursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			inCursor.jumpFwd(ctr == 0 ? startIndex + 1 : stepSize);
			argAccess.setPosition(inCursor);
			op.mutate1(argAccess.get(), inCursor.get());
		}
	}

	public static <A> void inplace(final IterableInterval<A> arg,
		final IterableInterval<A> in, final BinaryInplaceOp<A, A> op)
	{
		final Cursor<A> argCursor = arg.cursor();
		final Cursor<A> inCursor = in.cursor();
		while (argCursor.hasNext()) {
			op.mutate2(argCursor.next(), inCursor.next());
		}
	}

	public static <A> void inplace(final IterableInterval<A> arg,
		final IterableInterval<A> in, final BinaryInplaceOp<A, A> op,
		final int startIndex, final int stepSize, final int numSteps)
	{
		if (numSteps <= 0) return;
		final Cursor<A> argCursor = arg.cursor();
		final Cursor<A> inCursor = in.cursor();

		for (int ctr = 0; ctr < numSteps; ctr++) {
			final int m = ctr == 0 ? startIndex + 1 : stepSize;
			argCursor.jumpFwd(m);
			inCursor.jumpFwd(m);
			op.mutate2(argCursor.get(), inCursor.get());
		}
	}
}
