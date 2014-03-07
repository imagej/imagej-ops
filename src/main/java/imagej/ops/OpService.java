/*
 * #%L
 * OPS.
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
import imagej.command.CommandService;
import imagej.module.Module;
import imagej.module.ModuleInfo;
import imagej.module.ModuleItem;
import imagej.module.ModuleService;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.scijava.Context;
import org.scijava.InstantiableException;
import org.scijava.log.LogService;
import org.scijava.plugin.AbstractPTService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;
import org.scijava.util.ClassUtils;

/**
 * Default service for managing {@link Op} plugins.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Service.class)
public class OpService extends AbstractPTService<Op> {

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private CommandService commandService;

	@Parameter
	private LogService log;

	// -- OpService methods --

	public CommandInfo lookup(final String name, final Object... args) {
		for (final CommandInfo op : commandService.getCommandsOfType(Op.class)) {
			if (!name.equals(op.getName())) continue;
			Class<?> opClass;
			try {
				opClass = op.loadClass();
			}
			catch (final InstantiableException exc) {
				log.error("Invalid op: " + op.getClassName());
				continue;
			}
			final List<Field> params =
				ClassUtils.getAnnotatedFields(opClass, Parameter.class);
			boolean match = true;
			int i = 0;
			for (final Field param : params) {
				if (i >= args.length) {
					match = false;
					break;
				}
				// NB: Skip types handled by the application framework itself.
				// I.e., these parameters get injected by Context#inject(Object).
				if (Service.class.isAssignableFrom(param.getType())) continue;
				if (Context.class.isAssignableFrom(param.getType())) continue;

				final Object arg = args[i++];
				if (!canConvert(arg, param.getGenericType())) {
					match = false;
					break;
				}
			}
			if (match) return op;
		}
		return null;
	}

	public Object op(final String name, final Object... args) {
		final CommandInfo op = lookup(name, args);
		if (op == null) throw new IllegalArgumentException("No matching op: " +
			name);
		return run(op, args);
	}

	public Object run(final Op op, final Object... args) {
		final CommandModule module = asModule(op);
		final Map<String, Object> inputs = inputs(module.getInfo(), args);
		final Future<CommandModule> result =
			moduleService.run(module, true, inputs);
		return result(module.getInfo(), result);
	}

	public Object run(final CommandInfo op, final Object... args) {
		final Map<String, Object> inputs = inputs(op, args);
		final Future<CommandModule> result = commandService.run(op, true, inputs);
		return result(op, result);
	}

	private Object result(final ModuleInfo info,
		final Future<? extends Module> result)
	{
		final Module module = moduleService.waitFor(result);
		final List<Object> outputs = new ArrayList<Object>();
		for (final ModuleItem<?> output : info.outputs()) {
			final Object value = output.getValue(module);
			outputs.add(value);
		}
		return outputs.size() == 1 ? outputs.get(0) : outputs;
	}

	private Map<String, Object>
		inputs(final CommandInfo op, final Object... args)
	{
		final Map<String, Object> inputs = new HashMap<String, Object>();
		int i = 0;
		for (final ModuleItem<?> input : op.inputs()) {
			inputs.put(input.getName(), args[i++]);
		}
		return inputs;
	}

	public Object add(final Object... o) {
		return op("add", o);
	}

	// -- PTService methods --

	@Override
	public Class<Op> getPluginType() {
		return Op.class;
	}

	// -- Helper methods --

	private boolean canConvert(final Object arg, final Type genericType) {
//		return ConversionUtils.canConvert(arg, genericType);
		return true; // FIXME
	}

	private CommandModule asModule(final Op op) {
//		return commandService.asModule(op);
		return null; // FIXME
	}

}
