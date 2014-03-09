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

import imagej.command.CommandInfo;
import imagej.command.CommandModule;
import imagej.command.CommandModuleItem;
import imagej.module.Module;
import imagej.module.ModuleItem;
import imagej.module.ModuleService;

import java.lang.reflect.Type;

import org.scijava.Context;
import org.scijava.InstantiableException;
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
	public Module match(final CommandInfo info, final String name,
		final Class<? extends Op> type, final Object... args)
	{
		if (name != null && !name.equals(info.getName())) return null;

		// the name matches; now check the class
		final Class<?> opClass;
		try {
			opClass = info.loadClass();
		}
		catch (final InstantiableException exc) {
			log.error("Invalid op: " + info.getClassName());
			return null;
		}
		if (type != null && !type.isAssignableFrom(opClass)) return null;

		// check that each parameter is compatible with its argument
		int i = 0;
		for (final ModuleItem<?> item : info.inputs()) {
			if (i >= args.length) return null; // too few arguments
			final Object arg = args[i++];
			if (!canAssign(arg, item)) return null;
		}
		if (i != args.length) return null; // too many arguments

		// create module and assign the inputs
		final CommandModule module = (CommandModule) createModule(info, args);

		// make sure the op itself is happy with these arguments
		if (Contingent.class.isAssignableFrom(opClass)) {
			final Contingent c = (Contingent) module.getCommand();
			if (!c.conforms()) return null;
		}

		if (log.isDebug()) {
			log.debug("OpService.module(" + name + "): op=" +
				module.getDelegateObject().getClass().getName());
		}

		// found a match!
		return module;
	}

	// -- Helper methods --

	private Module createModule(final CommandInfo info, final Object... args) {
		final Module module = moduleService.createModule(info);
		context.inject(module.getDelegateObject());
		return opService.assignInputs(module, args);
	}

	private boolean canAssign(final Object arg, final ModuleItem<?> item) {
		if (item instanceof CommandModuleItem) {
			final CommandModuleItem<?> commandItem = (CommandModuleItem<?>) item;
			final Type type = commandItem.getField().getGenericType();
			return ConversionUtils.canConvert(arg, type);
		}
		return ConversionUtils.canConvert(arg, item.getType());
	}

}
