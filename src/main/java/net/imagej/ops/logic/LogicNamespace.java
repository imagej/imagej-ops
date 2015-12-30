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

import java.util.List;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.RealType;

/**
 * The logic namespace contains logical (i.e., boolean) operations.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class LogicNamespace extends AbstractNamespace {

	// -- and --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanAnd.class)
	public boolean and(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanAnd.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.AndCondition.class)
	public <T> BoolType and(final Object in, final Condition<T> c1,
		final Condition<T> c2)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.AndCondition.class, in, c1, c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.AndCondition.class)
	public <T> BoolType and(final BoolType out, final Object in,
		final Condition<T> c1, final Condition<T> c2)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.AndCondition.class, out, in,
				c1, c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IntersectionCondition.class)
	public <T> BoolType and(final T in, final List<Condition<T>> conditions) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.IntersectionCondition.class,
				in, conditions);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IntersectionCondition.class)
	public <T> BoolType and(final BoolType out, final T in,
		final List<Condition<T>> conditions)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.IntersectionCondition.class,
				out, in, conditions);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.And.class)
	public <I extends RealType<I>, O extends RealType<O>> O and(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.And.class, out, in);
		return result;
	}

	// -- bool --

	@OpMethod(op = net.imagej.ops.logic.BooleanCondition.class)
	public BoolType bool(final Boolean in) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.BooleanCondition.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanCondition.class)
	public BoolType bool(final BoolType out, final Boolean in) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.BooleanCondition.class,
				out, in);
		return result;
	}
	
	// -- equal --

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
	public <T> BoolType equal(final T in, final Object o) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.ObjectsEqual.class, in, o);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ObjectsEqual.class)
	public <T> BoolType equal(final BoolType out, final T in, final Object o) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.ObjectsEqual.class, out, in, o);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.Equal.class)
	public <I extends RealType<I>, O extends RealType<O>> O equal(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.Equal.class, out, in);
		return result;
	}
	
	// -- logicalEqual --
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.LogicalEqual.class)
	public <I extends RealType<I>, O extends RealType<O>> O logicalEqual(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.LogicalEqual.class, out, in);
		return result;
	}

	// -- greaterThan --

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
	public <T> BoolType greaterThan(final T in, final Comparable<? super T> c) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.ComparableGreaterThan.class,
				in, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableGreaterThan.class)
	public <T> BoolType greaterThan(final BoolType out, final T in,
		final Comparable<? super T> c)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.ComparableGreaterThan.class,
				out, in, c);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.GreaterThan.class)
	public <I extends RealType<I>, O extends RealType<O>> O greaterThan(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.GreaterThan.class, out, in);
		return result;
	}

	// -- greaterThanOrEqual --

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
	public <T> BoolType greaterThanOrEqual(final T in,
		final Comparable<? super T> c)
	{
		final BoolType result =
			(BoolType) ops().run(
				net.imagej.ops.logic.ComparableGreaterThanOrEqual.class, in, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableGreaterThanOrEqual.class)
	public <T> BoolType greaterThanOrEqual(final BoolType out, final T in,
		final Comparable<? super T> c)
	{
		final BoolType result =
			(BoolType) ops().run(
				net.imagej.ops.logic.ComparableGreaterThanOrEqual.class, out, in, c);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.GreaterThanOrEqual.class)
	public <I extends RealType<I>, O extends RealType<O>> O greaterThanOrEqual(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.GreaterThanOrEqual.class, out, in);
		return result;
	}

	// -- lessThan --

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
	public <T> BoolType lessThan(final T in, final Comparable<? super T> c) {
		final BoolType result =
			(BoolType) ops()
				.run(net.imagej.ops.logic.ComparableLessThan.class, in, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableLessThan.class)
	public <T> BoolType lessThan(final BoolType out, final T in,
		final Comparable<? super T> c)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.ComparableLessThan.class, out,
				in, c);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.LessThan.class)
	public <I extends RealType<I>, O extends RealType<O>> O lessThan(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.LessThan.class, out, in);
		return result;
	}

	// -- lessThanOrEqual --

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
	public <T> BoolType
		lessThanOrEqual(final T in, final Comparable<? super T> c)
	{
		final BoolType result =
			(BoolType) ops().run(
				net.imagej.ops.logic.ComparableLessThanOrEqual.class, in, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ComparableLessThanOrEqual.class)
	public <T> BoolType lessThanOrEqual(final BoolType out, final T in,
		final Comparable<? super T> c)
	{
		final BoolType result =
			(BoolType) ops().run(
				net.imagej.ops.logic.ComparableLessThanOrEqual.class, out, in, c);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.LessThanOrEqual.class)
	public <I extends RealType<I>, O extends RealType<O>> O lessThanOrEqual(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.LessThanOrEqual.class, out, in);
		return result;
	}

	// -- not --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanNot.class)
	public boolean not(final boolean a) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanNot.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.NotCondition.class)
	public <T> BoolType not(final Object in, final Condition<T> c1) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.NotCondition.class, in, c1);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.NotCondition.class)
	public <T> BoolType not(final BoolType out, final Object in,
		final Condition<T> c1)
	{
		final BoolType result =
			(BoolType) ops()
				.run(net.imagej.ops.logic.NotCondition.class, out, in, c1);
		return result;
	}

	// -- notEqual --

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
	public BoolType notEqual(final Object in, final Object o) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.ObjectsNotEqual.class, in, o);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ObjectsNotEqual.class)
	public BoolType notEqual(final BoolType out, final Object in, final Object o)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.ObjectsNotEqual.class, out, in,
				o);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.NotEqual.class)
	public <I extends RealType<I>, O extends RealType<O>> O notEqual(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.NotEqual.class, out, in);
		return result;
	}
	
	// -- logicalNotEqual --
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.LogicalNotEqual.class)
	public <I extends RealType<I>, O extends RealType<O>> O logicalNotEqual(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.LogicalNotEqual.class, out, in);
		return result;
	}

	// -- or --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanOr.class)
	public boolean or(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanOr.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.OrCondition.class)
	public <T> BoolType or(final Object in, final Condition<T> c1,
		final Condition<T> c2)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.OrCondition.class, in, c1, c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.OrCondition.class)
	public <T> BoolType or(final BoolType out, final Object in,
		final Condition<T> c1, final Condition<T> c2)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.OrCondition.class, out, in, c1,
				c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.UnionCondition.class)
	public <T> BoolType or(final T in, final List<Condition<T>> conditions) {
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.UnionCondition.class, in,
				conditions);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.UnionCondition.class)
	public <T> BoolType or(final BoolType out, final T in,
		final List<Condition<T>> conditions)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.UnionCondition.class, out, in,
				conditions);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.Or.class)
	public <I extends RealType<I>, O extends RealType<O>> O or(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.Or.class, out, in);
		return result;
	}

	// -- xor --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanXor.class)
	public boolean xor(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanXor.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.XorCondition.class)
	public <T> BoolType xor(final Object in, final Condition<T> c1,
		final Condition<T> c2)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.XorCondition.class, in, c1, c2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.XorCondition.class)
	public <T> BoolType xor(final BoolType out, final Object in,
		final Condition<T> c1, final Condition<T> c2)
	{
		final BoolType result =
			(BoolType) ops().run(net.imagej.ops.logic.XorCondition.class, out, in,
				c1, c2);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.logic.RealLogic.XOr.class)
	public <I extends RealType<I>, O extends RealType<O>> O xor(final O out, final I in) {
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.logic.RealLogic.XOr.class, out, in);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "logic";
	}

}
