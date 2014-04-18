/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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
package net.imagej.ops.onthefly;

import java.util.HashMap;
import java.util.Map;

import net.imagej.ops.AbstractOptimizer;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Optimizer;

import org.scijava.Context;
import org.scijava.Priority;
import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.module.ModuleService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * An {@link Optimizer} using Javassist to generate efficient ops for common argument types.
 * 
 * @author Johannes Schindelin
 */
@Plugin(type = Optimizer.class, priority = Priority.NORMAL_PRIORITY + 1)
public class JavassistOptimizer extends AbstractOptimizer {

	@Parameter
	private Context context;

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private OpService opService;

	private final Map<String, String> arithmeticOps;

	{
		// TODO: need to match by class
		arithmeticOps = new HashMap<String, String>();
		arithmeticOps.put("add", "+");
		arithmeticOps.put("subtract", "-");
		arithmeticOps.put("multiply", "*");
		arithmeticOps.put("divide", "/");
	}

	@Override
	public Module optimize(Module module) {
		// TODO: use precise class information instead: final Class<?> clazz = module.getDelegateObject().getClass();
		final String name = module.getInfo().getName();
		final String operator = arithmeticOps.get(name);

		if (operator != null) {
			final Map<String, Object> inputs = module.getInputs();
			final Object result = inputs.get("result");
			final Object a = inputs.get("a");
			final Object b = inputs.get("b");
			if (inputs.size() == 3 && result != null && a != null && b != null) {
				final Op op = ArithmeticOp.findOp(name, operator, result, a, b);
				if (op != null) {
					return createModule(op);
				}
			}
		}
		return null;
	}

	private Module createModule(final Op op) {
		final CommandInfo info = new CommandInfo(op.getClass());
		info.setPriority(Priority.FIRST_PRIORITY);
		return info.createModule(op);
	}
}
