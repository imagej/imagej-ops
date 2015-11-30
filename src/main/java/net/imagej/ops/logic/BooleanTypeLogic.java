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

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractBinaryHybridOp;
import net.imglib2.type.BooleanType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Logical ops with ImgLib2 types.
 *
 * @author Curtis Rueden
 */
public final class BooleanTypeLogic {

	private BooleanTypeLogic() {
		// NB: Prevent instantiation of utility class.
	}

	/** Op that computes the logical AND (&&) of two boolean values. */
	@Plugin(type = Ops.Logic.And.class, priority = 0.5)
	public static class BooleanAnd<T extends BooleanType<T>> extends
		AbstractBinaryHybridOp<T, T, T> implements Ops.Logic.And
	{

		@Override
		public T createOutput(final T input1, final T input2) {
			return input1.createVariable();
		}

		@Override
		public void compute2(final T input1, final T input2, final T output) {
			final T tmp = input1.createVariable();
			tmp.and(input2);
			output.set(tmp);
		}
	}

	/** Op that tests for equality (==) between two boolean values. */
	@Plugin(type = Ops.Logic.Equal.class, priority = 0.5)
	public static class BooleanEqual extends AbstractOp
		implements Ops.Logic.Equal
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private boolean a;

		@Parameter
		private boolean b;

		@Override
		public void run() {
			result = a == b;
		}
	}

	/** Op that computes the logical NOT (!) of a boolean value. */
	@Plugin(type = Ops.Logic.Not.class, priority = 0.5)
	public static class BooleanNot extends AbstractOp
		implements Ops.Logic.Not
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private boolean a;

		@Override
		public void run() {
			result = !a;
		}
	}

	/** Op that tests for inequality (!=) between two boolean values. */
	@Plugin(type = Ops.Logic.NotEqual.class, priority = 0.5)
	public static class BooleanNotEqual extends AbstractOp
		implements Ops.Logic.NotEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private boolean a;

		@Parameter
		private boolean b;

		@Override
		public void run() {
			result = a != b;
		}
	}

	/** Op that computes the logical OR (||) of two boolean values. */
	@Plugin(type = Ops.Logic.Or.class, priority = 0.5)
	public static class BooleanOr extends AbstractOp
		implements Ops.Logic.Or
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private boolean a;

		@Parameter
		private boolean b;

		@Override
		public void run() {
			result = a || b;
		}
	}

	/** Op that computes the logical XOR (^) of two boolean values. */
	@Plugin(type = Ops.Logic.Xor.class, priority = 0.5)
	public static class BooleanXor extends AbstractOp
		implements Ops.Logic.Xor
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private boolean a;

		@Parameter
		private boolean b;

		@Override
		public void run() {
			result = a ^ b;
		}
	}

	/** Op that tests for equality (==) between two int values. */
	@Plugin(type = Ops.Logic.Equal.class, priority = 0.4)
	public static class IntegerEqual extends AbstractOp
		implements Ops.Logic.Equal
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private int a;

		@Parameter
		private int b;

		@Override
		public void run() {
			result = a == b;
		}
	}

	/** Op that performs a greater-than (>) comparison on two int values. */
	@Plugin(type = Ops.Logic.GreaterThan.class, priority = 0.4)
	public static class IntegerGreaterThan extends AbstractOp
		implements Ops.Logic.GreaterThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private int a;

		@Parameter
		private int b;

		@Override
		public void run() {
			result = a > b;
		}
	}

	/** Op that performs a greater-than-or-equal (>=) comparison on two int values. */
	@Plugin(type = Ops.Logic.GreaterThanOrEqual.class, priority = 0.4)
	public static class IntegerGreaterThanOrEqual extends AbstractOp
		implements Ops.Logic.GreaterThanOrEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private int a;

		@Parameter
		private int b;

		@Override
		public void run() {
			result = a >= b;
		}
	}

	/** Op that performs a less-than (<) comparison on two int values. */
	@Plugin(type = Ops.Logic.LessThan.class, priority = 0.4)
	public static class IntegerLessThan extends AbstractOp
		implements Ops.Logic.LessThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private int a;

		@Parameter
		private int b;

		@Override
		public void run() {
			result = a < b;
		}
	}

	/** Op that performs a less-than-or-equal (<=) comparison on two int values. */
	@Plugin(type = Ops.Logic.LessThanOrEqual.class, priority = 0.4)
	public static class IntegerLessThanOrEqual extends AbstractOp
		implements Ops.Logic.LessThanOrEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private int a;

		@Parameter
		private int b;

		@Override
		public void run() {
			result = a <= b;
		}
	}

	/** Op that tests for inequality (!=) between two int values. */
	@Plugin(type = Ops.Logic.NotEqual.class, priority = 0.4)
	public static class IntegerNotEqual extends AbstractOp
		implements Ops.Logic.NotEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private int a;

		@Parameter
		private int b;

		@Override
		public void run() {
			result = a != b;
		}
	}

	/** Op that tests for equality (==) between two long values. */
	@Plugin(type = Ops.Logic.Equal.class, priority = 0.3)
	public static class LongEqual extends AbstractOp
		implements Ops.Logic.Equal
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private long a;

		@Parameter
		private long b;

		@Override
		public void run() {
			result = a == b;
		}
	}

	/** Op that performs a greater-than (>) comparison on two long values. */
	@Plugin(type = Ops.Logic.GreaterThan.class, priority = 0.3)
	public static class LongGreaterThan extends AbstractOp
		implements Ops.Logic.GreaterThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private long a;

		@Parameter
		private long b;

		@Override
		public void run() {
			result = a > b;
		}
	}

	/** Op that performs a greater-than-or-equal (>=) comparison on two long values. */
	@Plugin(type = Ops.Logic.GreaterThanOrEqual.class, priority = 0.3)
	public static class LongGreaterThanOrEqual extends AbstractOp
		implements Ops.Logic.GreaterThanOrEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private long a;

		@Parameter
		private long b;

		@Override
		public void run() {
			result = a >= b;
		}
	}

	/** Op that performs a less-than (<) comparison on two long values. */
	@Plugin(type = Ops.Logic.LessThan.class, priority = 0.3)
	public static class LongLessThan extends AbstractOp
		implements Ops.Logic.LessThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private long a;

		@Parameter
		private long b;

		@Override
		public void run() {
			result = a < b;
		}
	}

	/** Op that performs a less-than-or-equal (<=) comparison on two long values. */
	@Plugin(type = Ops.Logic.LessThanOrEqual.class, priority = 0.3)
	public static class LongLessThanOrEqual extends AbstractOp
		implements Ops.Logic.LessThanOrEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private long a;

		@Parameter
		private long b;

		@Override
		public void run() {
			result = a <= b;
		}
	}

	/** Op that tests for inequality (!=) between two long values. */
	@Plugin(type = Ops.Logic.NotEqual.class, priority = 0.3)
	public static class LongNotEqual extends AbstractOp
		implements Ops.Logic.NotEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private long a;

		@Parameter
		private long b;

		@Override
		public void run() {
			result = a != b;
		}
	}

	/** Op that tests for equality (==) between two float values. */
	@Plugin(type = Ops.Logic.Equal.class, priority = 0.2)
	public static class FloatEqual extends AbstractOp
		implements Ops.Logic.Equal
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private float a;

		@Parameter
		private float b;

		@Override
		public void run() {
			result = a == b;
		}
	}

	/** Op that performs a greater-than (>) comparison on two float values. */
	@Plugin(type = Ops.Logic.GreaterThan.class, priority = 0.2)
	public static class FloatGreaterThan extends AbstractOp
		implements Ops.Logic.GreaterThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private float a;

		@Parameter
		private float b;

		@Override
		public void run() {
			result = a > b;
		}
	}

	/** Op that performs a greater-than-or-equal (>=) comparison on two float values. */
	@Plugin(type = Ops.Logic.GreaterThanOrEqual.class, priority = 0.2)
	public static class FloatGreaterThanOrEqual extends AbstractOp
		implements Ops.Logic.GreaterThanOrEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private float a;

		@Parameter
		private float b;

		@Override
		public void run() {
			result = a >= b;
		}
	}

	/** Op that performs a less-than (<) comparison on two float values. */
	@Plugin(type = Ops.Logic.LessThan.class, priority = 0.2)
	public static class FloatLessThan extends AbstractOp
		implements Ops.Logic.LessThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private float a;

		@Parameter
		private float b;

		@Override
		public void run() {
			result = a < b;
		}
	}

	/** Op that performs a less-than-or-equal (<=) comparison on two float values. */
	@Plugin(type = Ops.Logic.LessThanOrEqual.class, priority = 0.2)
	public static class FloatLessThanOrEqual extends AbstractOp
		implements Ops.Logic.LessThanOrEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private float a;

		@Parameter
		private float b;

		@Override
		public void run() {
			result = a <= b;
		}
	}

	/** Op that tests for inequality (!=) between two float values. */
	@Plugin(type = Ops.Logic.NotEqual.class, priority = 0.2)
	public static class FloatNotEqual extends AbstractOp
		implements Ops.Logic.NotEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private float a;

		@Parameter
		private float b;

		@Override
		public void run() {
			result = a != b;
		}
	}

	/** Op that tests for equality (==) between two double values. */
	@Plugin(type = Ops.Logic.Equal.class, priority = 0.1)
	public static class DoubleEqual extends AbstractOp
		implements Ops.Logic.Equal
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private double a;

		@Parameter
		private double b;

		@Override
		public void run() {
			result = a == b;
		}
	}

	/** Op that performs a greater-than (>) comparison on two double values. */
	@Plugin(type = Ops.Logic.GreaterThan.class, priority = 0.1)
	public static class DoubleGreaterThan extends AbstractOp
		implements Ops.Logic.GreaterThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private double a;

		@Parameter
		private double b;

		@Override
		public void run() {
			result = a > b;
		}
	}

	/** Op that performs a greater-than-or-equal (>=) comparison on two double values. */
	@Plugin(type = Ops.Logic.GreaterThanOrEqual.class, priority = 0.1)
	public static class DoubleGreaterThanOrEqual extends AbstractOp
		implements Ops.Logic.GreaterThanOrEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private double a;

		@Parameter
		private double b;

		@Override
		public void run() {
			result = a >= b;
		}
	}

	/** Op that performs a less-than (<) comparison on two double values. */
	@Plugin(type = Ops.Logic.LessThan.class, priority = 0.1)
	public static class DoubleLessThan extends AbstractOp
		implements Ops.Logic.LessThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private double a;

		@Parameter
		private double b;

		@Override
		public void run() {
			result = a < b;
		}
	}

	/** Op that performs a less-than-or-equal (<=) comparison on two double values. */
	@Plugin(type = Ops.Logic.LessThanOrEqual.class, priority = 0.1)
	public static class DoubleLessThanOrEqual extends AbstractOp
		implements Ops.Logic.LessThanOrEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private double a;

		@Parameter
		private double b;

		@Override
		public void run() {
			result = a <= b;
		}
	}

	/** Op that tests for inequality (!=) between two double values. */
	@Plugin(type = Ops.Logic.NotEqual.class, priority = 0.1)
	public static class DoubleNotEqual extends AbstractOp
		implements Ops.Logic.NotEqual
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private double a;

		@Parameter
		private double b;

		@Override
		public void run() {
			result = a != b;
		}
	}

}
