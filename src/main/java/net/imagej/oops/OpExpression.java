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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;

/**
 * An {@link Expression} representing an {@link Op}.
 *
 * @author Johannes Schindelin
 */
public class OpExpression implements Expression {

	private final OpService op;
	private final String identifier;
	private final String[] variableNames;
	private final List<Expression> args;
	private final Object[] evaluated;

	public OpExpression(final OpService op, final String identifier,
		final List<Expression> args)
	{
		this.op = op;
		this.identifier = identifier;
		this.args = args;
		evaluated = new Object[args.size()];

		if (evaluated.length == 0) {
			variableNames = new String[0];
		}
		else {
			final Set<String> names = new LinkedHashSet<String>();
			for (final Expression expr : args) {
				for (final String name : expr.getVariableNames()) {
					names.add(name);
				}
			}
			variableNames = names.toArray(new String[names.size()]);
		}
	}

	@Override
	public String[] getVariableNames() {
		return variableNames;
	}

	@Override
	public void bind(final Map<String, Object> args) {
		for (final Expression expr : this.args) {
			expr.bind(args);
		}
	}

	@Override
	public Object eval() {
		for (int i = 0; i < evaluated.length; i++) {
			evaluated[i] = args.get(i).eval();
		}
		return op.run(identifier, evaluated);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(identifier).append('(');
		String separator = "";
		for (final Expression expr : args) {
			builder.append(separator).append(expr);
			separator = ", ";
		}
		builder.append(')');
		return builder.toString();
	}
}
