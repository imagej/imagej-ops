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
import net.imagej.ops.conditions.Condition;

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

	@OpMethod(op = net.imagej.ops.conditions.AndCondition.class)
	public <T> Boolean and(final Object in, final Condition<T> c1,
		final Condition<T> c2)
	{
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.AndCondition.class, in, c1,
				c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.conditions.AndCondition.class)
	public <T> Boolean and(final Boolean out, final Object in,
		final Condition<T> c1, final Condition<T> c2)
	{
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.AndCondition.class, out,
				in, c1, c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.conditions.BooleanCondition.class)
	public Boolean bool(final Boolean in) {
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.BooleanCondition.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.conditions.BooleanCondition.class)
	public Boolean bool(final Boolean out, final Boolean in) {
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.BooleanCondition.class,
				out, in);
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
	public boolean greaterThan(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongGreaterThan.class)
	public boolean greaterThan(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongGreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThan.class)
	public boolean greaterThan(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThan.class)
	public boolean greaterThan(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableGreaterThan.class)
	public <T> boolean greaterThan(final Comparable<T> a, final Object b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.ComparableGreaterThan.class, a,
				b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThanOrEqual.class)
	public boolean greaterThanOrEqual(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThanOrEqual.class, a,
				b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.LongGreaterThanOrEqual.class)
	public boolean greaterThanOrEqual(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongGreaterThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThanOrEqual.class)
	public boolean greaterThanOrEqual(final float a, final float b) {
		final boolean result =
			(Boolean) ops()
				.run(net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThanOrEqual.class,
					a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThanOrEqual.class)
	public boolean greaterThanOrEqual(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThanOrEqual.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableGreaterThanOrEqual.class)
	public <T> boolean greaterThanOrEqual(final Comparable<T> a, final Object b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.ComparableGreaterThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerLessThan.class)
	public boolean lessThan(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerLessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongLessThan.class)
	public boolean lessThan(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongLessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatLessThan.class)
	public boolean lessThan(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.FloatLessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleLessThan.class)
	public boolean lessThan(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleLessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableLessThan.class)
	public <T> boolean lessThan(final Comparable<T> a, final Object b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.ComparableLessThan.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.IntegerLessThanOrEqual.class)
	public boolean lessThanOrEqual(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerLessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongLessThanOrEqual.class)
	public boolean lessThanOrEqual(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongLessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatLessThanOrEqual.class)
	public
		boolean lessThanOrEqual(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.FloatLessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.DoubleLessThanOrEqual.class)
	public boolean lessThanOrEqual(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleLessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableLessThanOrEqual.class)
	public <T> boolean lessThanOrEqual(final Comparable<T> a, final Object b) {
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

	@OpMethod(op = net.imagej.ops.conditions.NotCondition.class)
	public <T> Boolean not(final Object in, final Condition<T> c1) {
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.NotCondition.class, in, c1);
		return result;
	}

	@OpMethod(op = net.imagej.ops.conditions.NotCondition.class)
	public <T> Boolean not(final Boolean out, final Object in,
		final Condition<T> c1)
	{
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.NotCondition.class, out,
				in, c1);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanNotEqual.class)
	public boolean notEqual(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.BooleanNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerNotEqual.class)
	public boolean notEqual(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongNotEqual.class)
	public boolean notEqual(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.LongNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatNotEqual.class)
	public boolean notEqual(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.FloatNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleNotEqual.class)
	public boolean notEqual(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleNotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ObjectsNotEqual.class)
	public boolean notEqual(final Object a, final Object b) {
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

	@OpMethod(op = net.imagej.ops.conditions.OrCondition.class)
	public <T> Boolean or(final Object in, final Condition<T> c1,
		final Condition<T> c2)
	{
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.OrCondition.class, in, c1,
				c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.conditions.OrCondition.class)
	public <T> Boolean or(final Boolean out, final Object in,
		final Condition<T> c1, final Condition<T> c2)
	{
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.OrCondition.class, out, in,
				c1, c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanXor.class)
	public boolean xor(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanXor.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.conditions.XorCondition.class)
	public <T> Boolean xor(final Object in, final Condition<T> c1,
		final Condition<T> c2)
	{
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.XorCondition.class, in, c1,
				c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.conditions.XorCondition.class)
	public <T> Boolean xor(final Boolean out, final Object in,
		final Condition<T> c1, final Condition<T> c2)
	{
		final Boolean result =
			(Boolean) ops().run(net.imagej.ops.conditions.XorCondition.class, out,
				in, c1, c2);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "logic";
	}

}
