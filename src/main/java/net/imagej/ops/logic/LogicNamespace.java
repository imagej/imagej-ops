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

package net.imagej.ops.logic;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.OpMethod;

/**
 * The logic namespace contains logical (i.e., boolean) operations.
 *
 * @author Curtis Rueden
 */
public class LogicNamespace extends AbstractNamespace {

	// -- Logic namespace ops --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanAnd.class)
	public boolean and(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanAnd.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanEqual.class)
	public boolean equal(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.BooleanEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerEqual.class)
	public boolean equal(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongEqual.class)
	public boolean equal(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.LongEqual.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatEqual.class)
	public boolean equal(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.FloatEqual.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleEqual.class)
	public boolean equal(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ObjectsEqual.class)
	public boolean equal(final Object a, final Object b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.ObjectsEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThan.class)
	public boolean greaterthan(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongGreaterThan.class)
	public boolean greaterthan(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongGreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThan.class)
	public boolean greaterthan(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThan.class)
	public boolean greaterthan(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableGreaterThan.class)
	public <T> boolean greaterthan(final Comparable<T> a, final Object b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.ComparableGreaterThan.class, a,
				b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThanOrEqual.class)
	public boolean greaterthanorequal(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThanOrEqual.class, a,
				b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.LongGreaterThanOrEqual.class)
	public boolean greaterthanorequal(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongGreaterThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThanOrEqual.class)
	public boolean greaterthanorequal(final float a, final float b) {
		final boolean result =
			(Boolean) ops()
				.run(net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThanOrEqual.class,
					a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThanOrEqual.class)
	public boolean greaterthanorequal(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThanOrEqual.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableGreaterThanOrEqual.class)
	public <T> boolean greaterthanorequal(final Comparable<T> a, final Object b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.ComparableGreaterThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerLessThan.class)
	public boolean lessthan(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerLessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongLessThan.class)
	public boolean lessthan(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongLessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatLessThan.class)
	public boolean lessthan(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.FloatLessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleLessThan.class)
	public boolean lessthan(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleLessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableLessThan.class)
	public <T> boolean lessthan(final Comparable<T> a, final Object b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.ComparableLessThan.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.IntegerLessThanOrEqual.class)
	public boolean lessthanorequal(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerLessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongLessThanOrEqual.class)
	public boolean lessthanorequal(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongLessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatLessThanOrEqual.class)
	public
		boolean lessthanorequal(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.FloatLessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.DoubleLessThanOrEqual.class)
	public boolean lessthanorequal(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleLessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableLessThanOrEqual.class)
	public <T> boolean lessthanorequal(final Comparable<T> a, final Object b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.ComparableLessThanOrEqual.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanNot.class)
	public boolean not(final boolean a) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanNot.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanNotEqual.class)
	public boolean notequal(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.BooleanNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerNotEqual.class)
	public boolean notequal(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongNotEqual.class)
	public boolean notequal(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatNotEqual.class)
	public boolean notequal(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.FloatNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleNotEqual.class)
	public boolean notequal(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ObjectsNotEqual.class)
	public boolean notequal(final Object a, final Object b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.ObjectsNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanOr.class)
	public boolean or(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanOr.class,
				a, b);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "logic";
	}

}
