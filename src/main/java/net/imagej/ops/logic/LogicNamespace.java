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

package net.imagej.ops.logic;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.Type;

import org.scijava.plugin.Plugin;

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
			(Boolean) ops().run(Ops.Logic.And.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.And.class)
	public <T extends BooleanType<T>> T and(final T in1, final T in2) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Logic.And.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.And.class)
	public <T extends BooleanType<T>> T and(final T out, final T in1,
		final T in2)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Logic.And.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToIIOutputII.And.class)
	public <T extends BooleanType<T>> IterableInterval<T> and(
		final IterableInterval<T> in1, final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.And.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToIIOutputII.And.class)
	public <T extends BooleanType<T>> IterableInterval<T> and(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.And.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToRAIOutputII.And.class)
	public <T extends BooleanType<T>> IterableInterval<T> and(
		final IterableInterval<T> in1, final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.And.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToRAIOutputII.And.class)
	public <T extends BooleanType<T>> IterableInterval<T> and(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.And.class, out, in1, in2);
		return result;
	}

	// -- conditional --

	@OpMethod(op = net.imagej.ops.logic.If.class)
	public <I extends BooleanType<I>, O extends Type<O>> O conditional(
		final O out, final I in, final O ifTrueVal, final O ifFalseVal)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(Ops.Logic.Conditional.class, out, in,
			ifTrueVal, ifFalseVal);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.If.class)
	public <I extends BooleanType<I>, O extends Type<O>> O conditional(final I in,
		final O ifTrueVal, final O ifFalseVal)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(Ops.Logic.Conditional.class, in, ifTrueVal,
			ifFalseVal);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.Default.class)
	public <I extends BooleanType<I>, O extends Type<O>> O conditional(
		final O out, final I in, final O defaultVal)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(Ops.Logic.Conditional.class, out, in,
			defaultVal);
		return result;
	}

	// -- equal --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanEqual.class)
	public boolean equal(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.Equal.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerEqual.class)
	public boolean equal(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.Equal.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongEqual.class)
	public boolean equal(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(Ops.Logic.Equal.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatEqual.class)
	public boolean equal(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(Ops.Logic.Equal.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleEqual.class)
	public boolean equal(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.Equal.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.ObjectsEqual.class)
	public <T extends BooleanType<T>> T equal(final T out, final Object a,
		final Object b)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(
			Ops.Logic.Equal.class, out, a, b);
		return result;
	}

	// -- greaterThan --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThan.class)
	public boolean greaterThan(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.GreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongGreaterThan.class)
	public boolean greaterThan(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.GreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThan.class)
	public boolean greaterThan(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.GreaterThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThan.class)
	public boolean greaterThan(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.GreaterThan.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.BooleanTypeLogic.ComparableGreaterThan.class)
	public <I extends Comparable<I>, O extends BooleanType<O>> O greaterThan(
		final O out, final I a, final I b)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(
			Ops.Logic.GreaterThan.class, out, a,
			b);
		return result;
	}

	// -- greaterThanOrEqual --

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.IntegerGreaterThanOrEqual.class)
	public boolean greaterThanOrEqual(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.GreaterThanOrEqual.class, a,
				b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.LongGreaterThanOrEqual.class)
	public boolean greaterThanOrEqual(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.GreaterThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.FloatGreaterThanOrEqual.class)
	public boolean greaterThanOrEqual(final float a, final float b) {
		final boolean result =
			(Boolean) ops()
				.run(Ops.Logic.GreaterThanOrEqual.class,
					a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.DoubleGreaterThanOrEqual.class)
	public boolean greaterThanOrEqual(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.GreaterThanOrEqual.class, a,
				b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.BooleanTypeLogic.ComparableGreaterThanOrEqual.class)
	public <I extends Comparable<I>, O extends BooleanType<O>> O
		greaterThanOrEqual(final O out, final I a, final I b)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(
			Ops.Logic.GreaterThanOrEqual.class,
			out, a, b);
		return result;
	}

	// -- lessThan --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerLessThan.class)
	public boolean lessThan(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.LessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongLessThan.class)
	public boolean lessThan(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.LessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatLessThan.class)
	public boolean lessThan(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.LessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleLessThan.class)
	public boolean lessThan(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.LessThan.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.ComparableLessThan.class)
	public <I extends Comparable<I>, O extends BooleanType<O>> O lessThan(
		final O out, final I a, final I b)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(
			Ops.Logic.LessThan.class, out, a,
			b);
		return result;
	}

	// -- lessThanOrEqual --

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.IntegerLessThanOrEqual.class)
	public boolean lessThanOrEqual(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.LessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongLessThanOrEqual.class)
	public boolean lessThanOrEqual(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.LessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatLessThanOrEqual.class)
	public
		boolean lessThanOrEqual(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.LessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.PrimitiveLogic.DoubleLessThanOrEqual.class)
	public boolean lessThanOrEqual(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.LessThanOrEqual.class, a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.logic.BooleanTypeLogic.ComparableLessThanOrEqual.class)
	public <I extends Comparable<I>, O extends BooleanType<O>> O lessThanOrEqual(
		final O out, final I a, final I b)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(
			Ops.Logic.LessThanOrEqual.class,
			out, a, b);
		return result;
	}

	// -- not --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanNot.class)
	public boolean not(final boolean a) {
		final boolean result =
			(Boolean) ops().run(Ops.Logic.Not.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.Not.class)
	public <T extends BooleanType<T>> T not(final T in) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Logic.Not.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.Not.class)
	public <T extends BooleanType<T>> T not(final T out, final T in)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Logic.Not.class, out, in);
		return result;
	}

	// -- notEqual --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanNotEqual.class)
	public boolean notEqual(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.NotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerNotEqual.class)
	public boolean notEqual(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.NotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongNotEqual.class)
	public boolean notEqual(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.NotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatNotEqual.class)
	public boolean notEqual(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.NotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleNotEqual.class)
	public boolean notEqual(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				Ops.Logic.NotEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.ObjectsNotEqual.class)
	public <T extends BooleanType<T>> T notEqual(final T out, final Object a,
		final Object b)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(
			Ops.Logic.NotEqual.class, out, a, b);
		return result;
	}

	// -- or --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanOr.class)
	public boolean or(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(Ops.Logic.Or.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.Or.class)
	public <T extends BooleanType<T>> T or(final T in1, final T in2) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Logic.Or.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.Or.class)
	public <T extends BooleanType<T>> T or(final T out, final T in1,
		final T in2)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Logic.Or.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToIIOutputII.Or.class)
	public <T extends BooleanType<T>> IterableInterval<T> or(
		final IterableInterval<T> in1, final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.Or.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToIIOutputII.Or.class)
	public <T extends BooleanType<T>> IterableInterval<T> or(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.Or.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToRAIOutputII.Or.class)
	public <T extends BooleanType<T>> IterableInterval<T> or(
		final IterableInterval<T> in1, final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.Or.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToRAIOutputII.Or.class)
	public <T extends BooleanType<T>> IterableInterval<T> or(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.Or.class, out, in1, in2);
		return result;
	}

	// -- xor --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanXor.class)
	public boolean xor(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(Ops.Logic.Xor.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.Xor.class)
	public <T extends BooleanType<T>> T xor(final T in1, final T in2) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Logic.Xor.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.BooleanTypeLogic.Xor.class)
	public <T extends BooleanType<T>> T xor(final T out, final T in1,
		final T in2)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(Ops.Logic.Xor.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToIIOutputII.Xor.class)
	public <T extends BooleanType<T>> IterableInterval<T> xor(
		final IterableInterval<T> in1, final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.Xor.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToIIOutputII.Xor.class)
	public <T extends BooleanType<T>> IterableInterval<T> xor(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.Xor.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToRAIOutputII.Xor.class)
	public <T extends BooleanType<T>> IterableInterval<T> xor(
		final IterableInterval<T> in1, final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.Xor.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.IIToRAIOutputII.Xor.class)
	public <T extends BooleanType<T>> IterableInterval<T> xor(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			Ops.Logic.Xor.class, out, in1, in2);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "logic";
	}

}
