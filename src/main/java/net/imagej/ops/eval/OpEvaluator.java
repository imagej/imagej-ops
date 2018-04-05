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

package net.imagej.ops.eval;

import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.Ops;

import org.scijava.parse.Operator;
import org.scijava.parse.Operators;
import org.scijava.parse.Variable;
import org.scijava.parse.eval.AbstractStandardStackEvaluator;
import org.scijava.parse.eval.Evaluator;

/**
 * A Parsington {@link Evaluator} using available {@link Op}s.
 * 
 * @author Curtis Rueden
 */
public class OpEvaluator extends AbstractStandardStackEvaluator {

	private final OpEnvironment ops;

	/** Map of Parsington {@link Operator}s to Ops operation names. */
	private final HashMap<Operator, String> opMap;

	public OpEvaluator(final OpEnvironment ops) {
		this.ops = ops;
		opMap = new HashMap<>();

		// Map each standard Parsington operator to its associated op name.
		// TODO: Consider creating a plugin extension point for defining these.

		// -- dot --
		//opMap.put(Operators.DOT, "dot");

		// -- groups --
		//opMap.put(Operators.PARENS, "parens");
		//opMap.put(Operators.BRACKETS, "brackets");
		//opMap.put(Operators.BRACES, "braces");

		// -- transpose, power --
		//opMap.put(Operators.TRANSPOSE, "transpose");
		//opMap.put(Operators.DOT_TRANSPOSE, "dotTranspose");
		opMap.put(Operators.POW, Ops.Math.Power.NAME);
		//opMap.put(Operators.DOT_POW, "dotPow");

		// -- unary --
		opMap.put(Operators.POS, Ops.Identity.NAME);
		opMap.put(Operators.NEG, Ops.Math.Negate.NAME);
		//opMap.put(Operators.COMPLEMENT, "complement");
		//opMap.put(Operators.NOT, "not");

		// -- multiplicative --
		opMap.put(Operators.MUL, Ops.Math.Multiply.NAME);
		opMap.put(Operators.DIV, Ops.Math.Divide.NAME);
		opMap.put(Operators.MOD, Ops.Math.Remainder.NAME);
		//opMap.put(Operators.RIGHT_DIV, "rightDiv");
		//opMap.put(Operators.DOT_DIV, "dotDiv");
		//opMap.put(Operators.DOT_RIGHT_DIV, "dotRightDiv");

		// -- additive --
		opMap.put(Operators.ADD, Ops.Math.Add.NAME);
		opMap.put(Operators.SUB, Ops.Math.Subtract.NAME);

		// -- shift --
		opMap.put(Operators.LEFT_SHIFT, Ops.Math.LeftShift.NAME);
		opMap.put(Operators.RIGHT_SHIFT, Ops.Math.RightShift.NAME);
		opMap.put(Operators.UNSIGNED_RIGHT_SHIFT, Ops.Math.UnsignedRightShift.NAME);

		// -- colon --
		//opMap.put(Operators.COLON, "colon");

		// -- relational --
		opMap.put(Operators.LESS_THAN, Ops.Logic.LessThan.NAME);
		opMap.put(Operators.GREATER_THAN, Ops.Logic.GreaterThan.NAME);
		opMap.put(Operators.LESS_THAN_OR_EQUAL, Ops.Logic.LessThanOrEqual.NAME);
		opMap.put(Operators.GREATER_THAN_OR_EQUAL, Ops.Logic.GreaterThanOrEqual.NAME);
		//opMap.put(Operators.INSTANCEOF, "instanceof");

		// -- equality --
		opMap.put(Operators.EQUAL, Ops.Logic.Equal.NAME);
		opMap.put(Operators.NOT_EQUAL, Ops.Logic.NotEqual.NAME);

		// -- bitwise --
		opMap.put(Operators.BITWISE_AND, Ops.Math.And.NAME);
		opMap.put(Operators.BITWISE_OR, Ops.Math.Or.NAME);

		// -- logical --
		opMap.put(Operators.LOGICAL_AND, Ops.Logic.And.NAME);
		opMap.put(Operators.LOGICAL_OR, Ops.Logic.Or.NAME);
	}

	// -- OpEvaluator methods --

	/**
	 * Executes the given {@link Operator operation} with the specified argument
	 * list.
	 */
	public Object execute(final Operator op, final Object... args) {
		return execute(getOpName(op), args);
	}

	/** Executes the given op with the specified argument list. */
	public Object execute(final String opName, final Object... args) {
		// Unwrap the arguments.
		final Object[] argValues = new Object[args.length];
		for (int i=0; i<args.length; i++) {
			argValues[i] = value(args[i]);
		}

		// Try executing the op.
		return ops.run(opName, argValues);
	}

	/** Gets the op name associated with the given {@link Operator}. */
	public String getOpName(final Operator op) {
		return opMap.containsKey(op) ? opMap.get(op) : op.getToken();
	}

	/**
	 * Gets the map of {@link Operator} to op names backing this evaluator.
	 * <p>
	 * Changes to this map will affect evaluation accordingly.
	 * </p>
	 */
	public Map<Operator, String> getOpMap() {
		return opMap;
	}

	// -- StandardEvaluator methods --

	// -- function --

	@Override
	public Object function(final Object a, final Object b) {
		if (a instanceof Variable) {
			// NB: Execute the op whose name matches the given variable token.
			return execute(((Variable) a).getToken(), list(b).toArray());
		}
		return null;
	}

	// -- dot --

	@Override
	public Object dot(final Object a, final Object b) {
		if (a instanceof Variable && b instanceof Variable) {
			// NB: Concatenate variable names, for namespace support
			final String namespace = ((Variable) a).getToken();
			final String opName = ((Variable) b).getToken();
			return new Variable(namespace + "." + opName);
		}
		return execute(Operators.DOT, a, b);
	}

	// -- groups --

	@Override
	public Object parens(final Object[] args) {
		if (args.length == 1) return args[0];
		return Arrays.asList(args);
	}

	@Override
	public Object brackets(final Object[] args) {
		return Arrays.asList(args);
	}

	@Override
	public Object braces(final Object[] args) {
		return Arrays.asList(args);
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
	public Object execute(final Operator op, final Deque<Object> stack) {
		// Pop the arguments.
		final int arity = op.getArity();
		final Object[] args = new Object[arity];
		for (int i = args.length - 1; i >= 0; i--) {
			args[i] = stack.pop();
		}

		// Try the base execute, which handles assignment-oriented operations.
		// (NB: super.execute pops the arguments again, so put them back first.)
		for (int i = 0; i < args.length; i++) {
			stack.push(args[i]);
		}
		final Object result = super.execute(op, stack);
		if (result != null) return result;

		// Unwrap the arguments.
		for (int i = 0; i < args.length; i++) {
			args[i] = value(args[i]);
		}

		return execute(op, args);
	}

	// -- Helper methods --

	private List<?> list(final Object o) {
		if (o instanceof List) return (List<?>) o;
		return Collections.singletonList(o);
	}

}
