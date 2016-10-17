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

package net.imagej.oops;

import static java.lang.Character.isDigit;
import static java.lang.Character.isJavaIdentifierPart;
import static java.lang.Character.isWhitespace;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.MathOps.Add;
import net.imagej.ops.MathOps.Divide;
import net.imagej.ops.MathOps.Multiply;
import net.imagej.ops.MathOps.Subtract;
import net.imagej.ops.OpService;

/**
 * A parser accepting an expression as a {@link String} and returning an
 * {@link Expression} ready to be bound and evaluated.
 *
 * @author Johannes Schindelin
 */
public class ExpressionParser {

	private final OpService op;
	private char[] array;
	private int index;

	public ExpressionParser(final OpService op) {
		this.op = op;
	}

	public synchronized Expression parse(final String expression) {
		array = expression.toCharArray();
		if (array.length == 0) {
			throw new IllegalArgumentException("Empty expression");
		}
		index = 0;
		final Expression result = parseArithmetic();
		if (index != array.length) {
			throw new IllegalArgumentException("Invalid expression, ends in '" +
				new String(array, index, array.length - index));
		}
		return result;
	}

	private Expression parseArithmetic() {
		Expression expr = parseOne();
		while (index < array.length) {
			skipWhitespace();

			final char c = array[index++];

			if (c == '+' || c == '-') {
				final List<Expression> args = new ArrayList<Expression>();
				args.add(expr);
				args.add(parseArithmetic());
				return new OpExpression(op, c == '+' ? Add.NAME : Subtract.NAME,
					args);
			}
			if (c == '*' || c == '/') {
				final List<Expression> args = new ArrayList<Expression>();
				args.add(expr);
				args.add(parseOne());
				expr =
					new OpExpression(op, c == '*' ? Multiply.NAME : Divide.NAME, args);
				continue;
			}
			index--;
			break;
		}
		return expr;
	}

	private Expression parseOne() {
		skipWhitespace();

		char c = array[index++];

		final int start = index - 1;

		if (c == '(') {
			final Expression expr = parseArithmetic();
			if (index >= array.length || array[index++] != ')') {
				throw new IllegalArgumentException("Incomplete expression: " +
					new String(array, start, index - start));
			}
			return expr;
		}

		if (isDigit(c)) {
			boolean dot = false;
			while (index < array.length) {
				c = array[index];
				if (c == '.') {
					if (dot) {
						throw new IllegalArgumentException("Invalid number: " +
							new String(array, start, index - start));
					}
					dot = true;
					continue;
				}
				if (!isDigit(c)) {
					break;
				}
				index++;
			}
			final String value = new String(array, start, index - start);
			if (dot) {
				return new Constant(Double.parseDouble(value));
			}
			return new Constant(Long.parseLong(value));
		}

		if (isJavaIdentifierPart(c)) {
			while (index < array.length) {
				if (!isJavaIdentifierPart(array[index])) {
					break;
				}
				index++;
			}
			final String identifier = new String(array, start, index - start);
			skipWhitespace();
			if (index >= array.length || array[index] != '(') {
				return new Variable(identifier);
			}

			if (++index >= array.length) {
				throw new IllegalArgumentException("Unterminated op: " +
					new String(array, start, index - start));
			}
			final List<Expression> args = new ArrayList<Expression>();
			for (;;) {
				final int startArg = index;
				args.add(parseOne());
				skipWhitespace();
				if (index >= array.length) {
					throw new IllegalArgumentException("Unterminated op: " +
						new String(array, start, index - start));
				}
				c = array[index++];
				if (c == ')') {
					return new OpExpression(op, identifier, args);
				}
				if (c != ',') {
					throw new IllegalArgumentException("Unrecognized arg: " +
						new String(array, startArg, index - startArg));
				}
			}
		}

		throw new IllegalArgumentException("Invalid identifier/constant: " +
			new String(array, index, array.length - index));
	}

	private void skipWhitespace() {
		while (index < array.length && isWhitespace(array[index])) {
			index++;
		}
	}

}
