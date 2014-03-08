/*
 * #%L
 * A framework for reusable algorithms.
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
import imagej.command.CommandService;
import imagej.module.Module;
import imagej.module.ModuleItem;
import imagej.module.ModuleService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.scijava.InstantiableException;
import org.scijava.log.LogService;
import org.scijava.plugin.AbstractPTService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;
import org.scijava.util.ConversionUtils;

/**
 * Default service that manages and executes {@link Op}s.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Service.class)
public class DefaultOpService extends AbstractPTService<Op> implements
	OpService
{

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private CommandService commandService;

	@Parameter
	private LogService log;

	// -- OpService methods --

	@Override
	public Module lookup(final String name, final Object... args) {
		OUTER:
		for (final CommandInfo info : commandService.getCommandsOfType(Op.class)) {
			if (!name.equals(info.getName())) continue;

			// the name matches; now check the fields
			final Class<?> opClass;
			try {
				opClass = info.loadClass();
			}
			catch (final InstantiableException exc) {
				log.error("Invalid op: " + info.getClassName());
				continue;
			}

			// check that each parameter is compatible with its argument
			int i = 0;
			for (final ModuleItem<?> item : info.inputs()) {
				if (i >= args.length) continue; // too few arguments
				final Object arg = args[i++];
				if (!canAssign(arg, item)) continue OUTER; // incompatible argument
			}
			if (i != args.length) continue; // too many arguments

			// create module and assign the inputs
			final CommandModule module = (CommandModule) createModule(info, args);

			// make sure the op itself is happy with these arguments
			if (Contingent.class.isAssignableFrom(opClass)) {
				final Contingent c = (Contingent) module.getCommand();
				if (!c.conforms()) continue;
			}

			// found a match!
			return module;
		}
		return null;
	}

	@Override
	public Object run(final String name, final Object... args) {
		final Module module = lookup(name, args);
		if (module == null) {
			throw new IllegalArgumentException("No matching op: " + name);
		}
		return run(module);
	}

	@Override
	public Object run(final Op op, final Object... args) {
		return run(createModule(op, args));
	}

	// -- Helper methods --

	private Object run(final Module module) {
		module.run();
		return result(module);
	}

	private Object result(final Module module) {
		final List<Object> outputs = new ArrayList<Object>();
		for (final ModuleItem<?> output : module.getInfo().outputs()) {
			final Object value = output.getValue(module);
			outputs.add(value);
		}
		return outputs.size() == 1 ? outputs.get(0) : outputs;
	}

	@Override
	public Object add(final Object... o) {
		return run("add", o);
	}

	@Override
	public Module asModule(final Op op) {
		final CommandInfo info = commandService.getCommand(op.getClass());
		final Module module = info.createModule(op);
		getContext().inject(module.getDelegateObject());
		return module;
	}

	// -- PTService methods --

	@Override
	public Class<Op> getPluginType() {
		return Op.class;
	}

	// -- Helper methods --

	private Module createModule(final Op op, final Object... args) {
		final Module module = asModule(op);
		return assignInputs(module, args);
	}

	private Module createModule(final CommandInfo info, final Object... args) {
		final Module module = moduleService.createModule(info);
		getContext().inject(module.getDelegateObject());
		return assignInputs(module, args);
	}

	/** Assigns arguments into the given module's inputs. */
	private Module assignInputs(final Module module, final Object... args) {
		int i = 0;
		for (final ModuleItem<?> item : module.getInfo().inputs()) {
			assign(module, args[i++], item);
		}
		return module;
	}

	private boolean canAssign(final Object arg, final ModuleItem<?> item) {
		// FIXME: Pending new feature in scijava-common
//		if (item instanceof CommandModuleItem) {
//			final CommandModuleItem<?> commandItem = (CommandModuleItem<?>) item;
//			final Type type = commandItem.getField().getGenericType();
//			return ConversionUtils.canConvert(arg, type);
//		}
		return ConversionUtils.canConvert(arg, item.getType());
	}

	private void assign(final Module module, final Object arg,
		final ModuleItem<?> item)
	{
		Object value;
		if (item instanceof CommandModuleItem) {
			final CommandModuleItem<?> commandItem = (CommandModuleItem<?>) item;
			final Type type = commandItem.getField().getGenericType();
			value = ConversionUtils.convert(arg, type);
		}
		else value = ConversionUtils.convert(arg, item.getType());
		module.setInput(item.getName(), value);
		module.setResolved(item.getName(), true);
	}

}
