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

package imagej.ops;

import imagej.command.CommandModuleItem;
import imagej.module.Module;
import imagej.module.ModuleInfo;
import imagej.module.ModuleItem;
import imagej.module.ModuleService;

import java.lang.reflect.Type;

import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.ConversionUtils;

/**
 * Default matching heuristic for {@link Op}s.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = OperationMatcher.class)
public class DefaultOperationMatcher extends AbstractOperationMatcher {

	@Parameter
	private Context context;

	@Parameter
	private OpService opService;

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private LogService log;

	@Override
	public Module match(final ModuleInfo info, final Object... args) {
		// check that each parameter is compatible with its argument
		int i = 0;
		for (final ModuleItem<?> item : info.inputs()) {
			if (i >= args.length) return null; // too few arguments
			final Object arg = args[i++];
			if (!canAssign(arg, item)) return null;
		}
		if (i != args.length) return null; // too many arguments

		// create module and assign the inputs
		final Module module = createModule(info, args);

		// make sure the op itself is happy with these arguments
		final Object op = module.getDelegateObject();
		if (op instanceof Contingent) {
			final Contingent c = (Contingent) op;
			if (!c.conforms()) return null;
		}

		// found a match!
		return module;
	}

	// -- Helper methods --

	private Module createModule(final ModuleInfo info, final Object... args) {
		final Module module = moduleService.createModule(info);
		context.inject(module.getDelegateObject());
		return opService.assignInputs(module, args);
	}

	private boolean canAssign(final Object arg, final ModuleItem<?> item) {
		if (arg == null) return !item.isRequired();
		if (item instanceof CommandModuleItem) {
			final CommandModuleItem<?> commandItem = (CommandModuleItem<?>) item;
			final Type type = commandItem.getField().getGenericType();
			return ConversionUtils.canConvert(arg, type);
		}
		return ConversionUtils.canConvert(arg, item.getType());
	}

}
