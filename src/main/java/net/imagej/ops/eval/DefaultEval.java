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

import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Evaluates an expression.
 * <p>
 * The expression is parsed using <a
 * href="https://github.com/scijava/scijava-expression-parser">SJEP</a>, then
 * evaluated by invoking available ops.
 * </p>
 * 
 * @author Curtis Rueden
 * @see OpEvaluator
 */
@Plugin(type = Op.class, name = Ops.Eval.NAME)
public class DefaultEval implements Ops.Eval {

	@Parameter
	private OpService ops;

	@Parameter(type = ItemIO.OUTPUT)
	private Object result;

	@Parameter
	private String expression;

	@Parameter(required = false)
	private Map<String, Object> vars;

	@Override
	public void run() {
		final OpEvaluator e = new OpEvaluator(ops);
		if (vars != null) e.setAll(vars);
		result = e.evaluate(expression);
	}

}
