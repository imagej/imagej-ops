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
package imagej.ops.onthefly;

import imagej.command.CommandInfo;
import imagej.module.Module;
import imagej.module.ModuleInfo;
import imagej.module.ModuleService;
import imagej.ops.AbstractOpMatcher;
import imagej.ops.Op;
import imagej.ops.OpMatcher;
import imagej.ops.OpService;

import java.util.HashMap;
import java.util.Map;

import org.scijava.Context;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * An {@link OpMatcher} using Javassist to generate efficient ops for common argument types.
 * 
 * @author Johannes Schindelin
 */
@Plugin(type = OpMatcher.class, priority = Priority.NORMAL_PRIORITY + 1)
public class JavassistOpMatcher extends AbstractOpMatcher {

	@Parameter
	private Context context;

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private OpService opService;

	private final Map<String, String> arithmeticOps;

	{
		arithmeticOps = new HashMap<String, String>();
		arithmeticOps.put("add", "+");
		arithmeticOps.put("subtract", "-");
		arithmeticOps.put("multiply", "*");
		arithmeticOps.put("divide", "/");
	}

	@Override
	public Module match(ModuleInfo info, Object... args) {
		final String name = info.getName();
		final String arithmeticOp = arithmeticOps.get(name);
		if (arithmeticOp != null && args.length == 3) {
			final Op op = ArithmeticOp.findOp(name, arithmeticOp, args[0], args[1], args[2]);
			if (op != null) {
				return createAndPopulateModule(op, args);
			}
		}
		return null;
	}

	private Module createAndPopulateModule(Op op, Object[] args) {
		final CommandInfo info = new CommandInfo(op.getClass());
		info.setPriority(Priority.FIRST_PRIORITY);
		final Module module = info.createModule(op);
		opService.assignInputs(module, args);
		return module;
	}
}
