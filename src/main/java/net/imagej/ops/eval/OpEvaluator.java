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

package net.imagej.ops.eval;

import java.util.Deque;
import java.util.HashMap;

import net.imagej.ops.LogicOps;
import net.imagej.ops.MathOps;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;

import org.scijava.sjep.Function;
import org.scijava.sjep.Operator;
import org.scijava.sjep.Operators;
import org.scijava.sjep.Verb;
import org.scijava.sjep.eval.AbstractStandardStackEvaluator;
import org.scijava.sjep.eval.Evaluator;

/**
 * An SJEP {@link Evaluator} using available {@link Op}s.
 * 
 * @author Curtis Rueden
 */
public class OpEvaluator extends AbstractStandardStackEvaluator {

	private final OpService ops;

	/** Map of SJEP {@link Operator}s to OPS operation names. */
	private final HashMap<Operator, String> opMap;

	public OpEvaluator(final OpService ops) {
		this.ops = ops;
		opMap = new HashMap<Operator, String>();

		// Map each standard SJEP operator to its associated op name.
		// TODO: Consider creating a plugin extension point for defining these.

		// -- dot --
		//opMap.put(Operators.DOT, "dot");

		// -- transpose, power --
		//opMap.put(Operators.TRANSPOSE, "transpose");
		//opMap.put(Operators.DOT_TRANSPOSE, "dotTranspose");
		opMap.put(Operators.POW, MathOps.Power.NAME);
		//opMap.put(Operators.DOT_POW, "dotPow");

		// -- unary --
		opMap.put(Operators.POS, Ops.Identity.NAME);
		opMap.put(Operators.NEG, MathOps.Negate.NAME);
		//opMap.put(Operators.COMPLEMENT, "complement");
		//opMap.put(Operators.NOT, "not");

		// -- multiplicative --
		opMap.put(Operators.MUL, MathOps.Multiply.NAME);
		opMap.put(Operators.DIV, MathOps.Divide.NAME);
		opMap.put(Operators.MOD, MathOps.Remainder.NAME);
		//opMap.put(Operators.RIGHT_DIV, "rightDiv");
		//opMap.put(Operators.DOT_DIV, "dotDiv");
		//opMap.put(Operators.DOT_RIGHT_DIV, "dotRightDiv");

		// -- additive --
		opMap.put(Operators.ADD, MathOps.Add.NAME);
		opMap.put(Operators.SUB, MathOps.Subtract.NAME);

		// -- shift --
		opMap.put(Operators.LEFT_SHIFT, MathOps.LeftShift.NAME);
		opMap.put(Operators.RIGHT_SHIFT, MathOps.RightShift.NAME);
		opMap.put(Operators.UNSIGNED_RIGHT_SHIFT, MathOps.UnsignedRightShift.NAME);

		// -- colon --
		//opMap.put(Operators.COLON, "colon");

		// -- relational --
		opMap.put(Operators.LESS_THAN, LogicOps.LessThan.NAME);
		opMap.put(Operators.GREATER_THAN, LogicOps.GreaterThan.NAME);
		opMap.put(Operators.LESS_THAN_OR_EQUAL, LogicOps.LessThanOrEqual.NAME);
		opMap.put(Operators.GREATER_THAN_OR_EQUAL, LogicOps.GreaterThanOrEqual.NAME);
		//opMap.put(Operators.INSTANCEOF, "instanceof");

		// -- equality --
		opMap.put(Operators.EQUAL, LogicOps.Equal.NAME);
		opMap.put(Operators.NOT_EQUAL, LogicOps.NotEqual.NAME);

		// -- bitwise --
		opMap.put(Operators.BITWISE_AND, MathOps.And.NAME);
		opMap.put(Operators.BITWISE_OR, MathOps.Or.NAME);

		// -- logical --
		opMap.put(Operators.LOGICAL_AND, LogicOps.And.NAME);
		opMap.put(Operators.LOGICAL_OR, LogicOps.Or.NAME);
	}

	// -- OpEvaluator methods --

	/**
	 * Executes the given {@link Verb operation} (typically an {@link Operator} or
	 * a {@link Function}) with the specified argument list.
	 */
	public Object execute(final Verb verb, final Object... args) {
		// Unwrap the arguments.
		final Object[] argValues = new Object[args.length];
		for (int i=0; i<args.length; i++) {
			argValues[i] = value(args[i]);
		}

		// Try executing the op.
		return ops.run(getOpName(verb), argValues);
	}

	/** Gets the op name associated with the given {@link Verb}. */
	public String getOpName(final Verb verb) {
		return opMap.containsKey(verb) ? opMap.get(verb) : verb.getToken();
	}

	// -- StandardEvaluator methods --

	// -- dot --

	@Override
	public Object dot(final Object a, final Object b) {
		return execute(Operators.DOT, a, b);
	}

	// -- transpose, power --

	@Override
	public Object transpose(final Object a) {
		return execute(Operators.TRANSPOSE, a);
	}

	@Override
	public Object dotTranspose(final Object a) {
		return execute(Operators.DOT_TRANSPOSE, a);
	}

	@Override
	public Object pow(final Object a, final Object b) {
		return execute(Operators.POW, a, b);
	}

	@Override
	public Object dotPow(final Object a, final Object b) {
		return execute(Operators.DOT_POW, a, b);
	}

	// -- unary --

	@Override
	public Object pos(final Object a) {
		return execute(Operators.POS, a);
	}

	@Override
	public Object neg(final Object a) {
		return execute(Operators.NEG, a);
	}

	@Override
	public Object complement(final Object a) {
		return execute(Operators.COMPLEMENT, a);
	}

	@Override
	public Object not(final Object a) {
		return execute(Operators.NOT, a);
	}

	// -- multiplicative --

	@Override
	public Object mul(final Object a, final Object b) {
		return execute(Operators.MUL, a, b);
	}

	@Override
	public Object div(final Object a, final Object b) {
		return execute(Operators.DIV, a, b);
	}

	@Override
	public Object mod(final Object a, final Object b) {
		return execute(Operators.MOD, a, b);
	}

	@Override
	public Object rightDiv(final Object a, final Object b) {
		return execute(Operators.RIGHT_DIV, a, b);
	}

	@Override
	public Object dotMul(final Object a, final Object b) {
		return execute(Operators.DOT_MUL, a, b);
	}

	@Override
	public Object dotDiv(final Object a, final Object b) {
		return execute(Operators.DOT_DIV, a, b);
	}

	@Override
	public Object dotRightDiv(final Object a, final Object b) {
		return execute(Operators.DOT_RIGHT_DIV, a, b);
	}

	// -- additive --

	@Override
	public Object add(final Object a, final Object b) {
		return execute(Operators.ADD, a, b);
	}

	@Override
	public Object sub(final Object a, final Object b) {
		return execute(Operators.SUB, a, b);
	}

	// -- shift --

	@Override
	public Object leftShift(final Object a, final Object b) {
		return execute(Operators.LEFT_SHIFT, a, b);
	}

	@Override
	public Object rightShift(final Object a, final Object b) {
		return execute(Operators.RIGHT_SHIFT, a, b);
	}

	@Override
	public Object unsignedRightShift(final Object a, final Object b) {
		return execute(Operators.UNSIGNED_RIGHT_SHIFT, a, b);
	}

	// -- colon --

	@Override
	public Object colon(final Object a, final Object b) {
		return execute(Operators.COLON, a, b);
	}

	// -- relational --

	@Override
	public Object lessThan(final Object a, final Object b) {
		return execute(Operators.LESS_THAN, a, b);
	}

	@Override
	public Object greaterThan(final Object a, final Object b) {
		return execute(Operators.GREATER_THAN, a, b);
	}

	@Override
	public Object lessThanOrEqual(final Object a, final Object b) {
		return execute(Operators.LESS_THAN_OR_EQUAL, a, b);
	}

	@Override
	public Object greaterThanOrEqual(final Object a, final Object b) {
		return execute(Operators.GREATER_THAN_OR_EQUAL, a, b);
	}

	@Override
	public Object instanceOf(final Object a, final Object b) {
		return execute(Operators.INSTANCEOF, a, b);
	}

	// -- equality --

	@Override
	public Object equal(final Object a, final Object b) {
		return execute(Operators.EQUAL, a, b);
	}

	@Override
	public Object notEqual(final Object a, final Object b) {
		return execute(Operators.NOT_EQUAL, a, b);
	}

	// -- bitwise --

	@Override
	public Object bitwiseAnd(final Object a, final Object b) {
		return execute(Operators.BITWISE_AND, a, b);
	}

	@Override
	public Object bitwiseOr(final Object a, final Object b) {
		return execute(Operators.BITWISE_OR, a, b);
	}

	// -- logical --

	@Override
	public Object logicalAnd(final Object a, final Object b) {
		return execute(Operators.LOGICAL_AND, a, b);
	}

	@Override
	public Object logicalOr(final Object a, final Object b) {
		return execute(Operators.LOGICAL_OR, a, b);
	}

	// -- StackEvaluator methods --

	@Override
	public Object execute(final Verb verb, final Deque<Object> stack) {
		// Pop the arguments.
		final int arity = verb.getArity();
		final Object[] args = new Object[arity];
		for (int i = args.length - 1; i >= 0; i--) {
			args[i] = stack.pop();
		}

		// Try the base execute, which handles assignment-oriented operations.
		// (NB: super.execute pops the arguments again, so put them back first.)
		for (int i = 0; i < args.length; i++) {
			stack.push(args[i]);
		}
		final Object result = super.execute(verb, stack);
		if (result != null) return result;

		// Unwrap the arguments.
		for (int i = 0; i < args.length; i++) {
			args[i] = value(args[i]);
		}

		return execute(verb, args);
	}

}
