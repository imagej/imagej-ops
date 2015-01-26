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

import java.util.HashMap;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;

import org.scijava.Context;
import org.scijava.script.ScriptService;

/**
 * Convenience functions for working with optimized {@link Op}s.
 *
 * @author Johannes Schindelin
 */
public class OOps {

	/**
	 * Parse and evaluate the given expression using the specified {@link OpService}.
	 * 
	 * @param op the {@link OpService}
	 * @param expression the expression to evaluate
	 * @param args the variables on which to evaluate the expression
	 * @return the result of the evaluated expression
	 */
	public static Object eval(final OpService op, final String expression,
		final Object... args)
	{
		final Expression expr = parse(op, expression);
		final String[] variableNames = expr.getVariableNames();
		if (variableNames.length != args.length) {
			throw new IllegalArgumentException("Expected " + variableNames.length +
				" variables but got " + args.length);
		}
		final Map<String, Object> map =
			new HashMap<String, Object>(variableNames.length);
		for (int i = 0; i < args.length; i++) {
			map.put(variableNames[i], args[i]);
		}
		expr.bind(map);
		return expr.eval();
	}

	private static Expression parse(final OpService op, final String expression) {
		return new ExpressionParser(op).parse(expression);
	}

}
