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

package net.imagej.ops;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.scijava.Context;
import org.scijava.InstantiableException;
import org.scijava.command.CommandInfo;
import org.scijava.command.CommandModuleItem;
import org.scijava.command.CommandService;
import org.scijava.convert.ConvertService;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.module.ModuleService;
import org.scijava.plugin.AbstractSingletonService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Default service for finding {@link Op}s which match a template.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Service.class)
public class DefaultOpMatchingService extends
	AbstractSingletonService<Optimizer> implements OpMatchingService
{

	@Parameter
	private Context context;

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private CommandService commandService;

	@Parameter
	private ConvertService convertService;

	@Parameter
	private LogService log;

	// -- OpMatchingService methods --

	@Override
	public List<CommandInfo> getOps() {
		return commandService.getCommandsOfType(Op.class);
	}

	@Override
	public Module findModule(final String name, final Class<? extends Op> type,
		final Object... args)
	{
		final String label = type == null ? name : type.getName();

		// find candidates with matching name & type
		final List<ModuleInfo> candidates = findCandidates(name, type);
		if (candidates.isEmpty()) {
			throw new IllegalArgumentException("No candidate '" + label + "' ops");
		}

		// narrow down candidates to the exact matches
		final List<Module> matches = findMatches(candidates, args);

		if (matches.size() == 1) {
			// a single match: return it
			if (log.isDebug()) {
				log.debug("Selected '" + label + "' op: " +
					matches.get(0).getDelegateObject().getClass().getName());
			}
			return optimize(matches.get(0));
		}

		final StringBuilder sb = new StringBuilder();

		if (matches.isEmpty()) {
			// no matches
			sb.append("No matching '" + label + "' op\n");
		}
		else {
			// multiple matches
			final double priority = matches.get(0).getInfo().getPriority();
			sb.append("Multiple '" + label + "' ops of priority " + priority + ":\n");
			for (final Module module : matches) {
				sb.append("\t" + getOpString(module.getInfo()) + "\n");
			}
		}

		// fail, with information about the template and candidates
		sb.append("Template:\n");
		sb.append("\t" + getOpString(label, args) + "\n");
		sb.append("Candidates:\n");
		for (final ModuleInfo info : candidates) {
			sb.append("\t" + getOpString(info) + "\n");
		}
		throw new IllegalArgumentException(sb.toString());
	}

	@Override
	public List<ModuleInfo> findCandidates(final String name,
		final Class<? extends Op> type)
	{
		final List<CommandInfo> ops = getOps();
		final ArrayList<ModuleInfo> candidates = new ArrayList<ModuleInfo>();

		for (final CommandInfo info : ops) {
			if (isCandidate(info, name, type)) candidates.add(info);
		}
		return candidates;
	}

	@Override
	public List<Module> findMatches(final List<? extends ModuleInfo> ops,
		final Object... args)
	{
		final ArrayList<Module> matches = new ArrayList<Module>();

		double priority = Double.NaN;
		for (final ModuleInfo info : ops) {
			final double p = info.getPriority();
			if (p != priority && !matches.isEmpty()) {
				// NB: Lower priority was reached; stop looking for any more matches.
				break;
			}
			priority = p;

			final Module module = match(info, args);

			if (module != null) matches.add(module);
		}

		return matches;
	}

	@Override
	public Module match(final ModuleInfo info, final Object... args) {
		if (!info.isValid()) return null; // skip invalid modules

		// check the number of args, padding optional args with null as needed
		int inputCount = 0, requiredCount = 0;
		for (final ModuleItem<?> item : info.inputs()) {
			inputCount++;
			if (item.isRequired()) requiredCount++;
		}
		if (args.length > inputCount) return null; // too many arguments
		if (args.length < requiredCount) return null; // too few arguments

		if (args.length != inputCount) {
			// pad optional parameters with null (from right to left)
			final int argsToPad = inputCount - args.length;
			final int optionalCount = inputCount - requiredCount;
			final int optionalsToFill = optionalCount - argsToPad;
			final Object[] paddedArgs = new Object[inputCount];
			int argIndex = 0, paddedIndex = 0, optionalIndex = 0;
			for (final ModuleItem<?> item : info.inputs()) {
				if (!item.isRequired() && optionalIndex++ >= optionalsToFill) {
					// skip this optional parameter (pad with null)
					paddedIndex++;
					continue;
				}
				paddedArgs[paddedIndex++] = args[argIndex++];
			}
			return match(info, paddedArgs);
		}

		// check that each parameter is compatible with its argument
		int i = 0;
		for (final ModuleItem<?> item : info.inputs()) {
			final Object arg = args[i++];
			if (!canAssign(arg, item)) return null;
		}

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

	@Override
	public Module assignInputs(final Module module, final Object... args) {
		int i = 0;
		for (final ModuleItem<?> item : module.getInfo().inputs()) {
			assign(module, args[i++], item);
		}
		return module;
	}

	@Override
	public Module optimize(final Module module) {
		final ArrayList<Module> optimal = new ArrayList<Module>();
		final ArrayList<Optimizer> optimizers = new ArrayList<Optimizer>();

		double priority = Double.NaN;
		for (final Optimizer optimizer : getInstances()) {
			final double p = optimizer.getPriority();
			if (p != priority && !optimal.isEmpty()) {
				// NB: Lower priority was reached; stop looking for any more matches.
				break;
			}
			priority = p;
			final Module m = optimizer.optimize(module);
			if (m != null) {
				optimal.add(m);
				optimizers.add(optimizer);
			}
		}

		if (optimal.size() == 1) return optimal.get(0);
		if (optimal.isEmpty()) return module;

		// multiple matches
		final double p = optimal.get(0).getInfo().getPriority();
		final StringBuilder sb = new StringBuilder();
		final String label = module.getDelegateObject().getClass().getName();
		sb.append("Multiple '" + label + "' optimizations of priority " + p + ":\n");
		for (int i = 0; i < optimizers.size(); i++) {
			sb.append("\t" + optimizers.get(i).getClass().getName() + " produced:");
			sb.append("\t\t" + getOpString(optimal.get(i).getInfo()) + "\n");
		}
		log.warn(sb.toString());
		return module;
	}

	@Override
	public String getOpString(final String name, final Object... args) {
		final StringBuilder sb = new StringBuilder();
		sb.append(name + "(");
		boolean first = true;
		for (final Object arg : args) {
			if (first) first = false;
			else sb.append(", ");
			if (arg != null) sb.append(arg.getClass().getName() + " ");
			if (arg instanceof Class) sb.append(((Class<?>) arg).getName());
			else sb.append(arg);
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String getOpString(final ModuleInfo info) {
		final StringBuilder sb = new StringBuilder();
		if (!info.isValid()) sb.append("{!INVALID!} ");
		sb.append("[" + info.getPriority() + "] ");
		sb.append("(" + paramString(info.outputs()) + ")");
		sb.append(" = " + info.getDelegateClassName());
		sb.append("(" + paramString(info.inputs()) + ")");
		return sb.toString();
	}

	@Override
	public boolean isCandidate(final CommandInfo info, final String name) {
		return isCandidate(info, name, null);
	}

	@Override
	public boolean isCandidate(final CommandInfo info,
		final Class<? extends Op> type)
	{
		return isCandidate(info, null, type);
	}

	@Override
	public boolean isCandidate(final CommandInfo info, final String name,
		final Class<? extends Op> type)
	{
		if (!nameMatches(info, name)) return false;

		// the name matches; now check the class
		final Class<?> opClass;
		try {
			opClass = info.loadClass();
		}
		catch (final InstantiableException exc) {
			log.error("Invalid op: " + info.getClassName());
			return false;
		}
		if (type != null && !type.isAssignableFrom(opClass)) return false;

		return true;
	}

	// -- PTService methods --

	@Override
	public Class<Optimizer> getPluginType() {
		return Optimizer.class;
	}

	// -- Helper methods --

	private boolean nameMatches(final ModuleInfo info, final String name) {
		if (name == null || name.equals(info.getName())) return true;

		// check for an alias
		final String alias = info.get("alias");
		if (name.equals(alias)) return true;

		// check for a list of aliases
		final String aliases = info.get("aliases");
		if (aliases != null) {
			for (final String a : aliases.split(",")) {
				if (name.equals(a.trim())) return true;
			}
		}

		return false;
	}

	/** Helper method of {@link #match}. */
	private Module createModule(final ModuleInfo info, final Object... args) {
		final Module module = moduleService.createModule(info);
		context.inject(module.getDelegateObject());
		return assignInputs(module, args);
	}

	private boolean canAssign(final Object arg, final ModuleItem<?> item) {
		if (arg == null) return !item.isRequired();
		if (item instanceof CommandModuleItem) {
			final CommandModuleItem<?> commandItem = (CommandModuleItem<?>) item;
			final Type type = commandItem.getField().getGenericType();
			return canConvert(arg, type);
		}
		return canConvert(arg, item.getType());
	}

	private boolean canConvert(final Object o, final Type type) {
		if (o instanceof Class && convertService.supports((Class<?>) o, type)) {
			// NB: Class argument for matching, to help differentiate op signatures.
			return true;
		}
		return convertService.supports(o, type);
	}

	private boolean canConvert(final Object o, final Class<?> type) {
		if (o instanceof Class && convertService.supports((Class<?>) o, type)) {
			// NB: Class argument for matching, to help differentiate op signatures.
			return true;
		}
		return convertService.supports(o, type);
	}

	/** Helper method of {@link #assignInputs}. */
	private void assign(final Module module, final Object arg,
		final ModuleItem<?> item)
	{
		if (arg != null) {
			Object value;
			if (item instanceof CommandModuleItem) {
				final CommandModuleItem<?> commandItem = (CommandModuleItem<?>) item;
				final Type type = commandItem.getField().getGenericType();
				value = convert(arg, type);
			}
			else value = convert(arg, item.getType());
			module.setInput(item.getName(), value);
		}
		module.setResolved(item.getName(), true);
	}

	private Object convert(final Object o, final Type type) {
		if (o instanceof Class && convertService.supports((Class<?>) o, type)) {
			// NB: Class argument for matching; fill with null.
			return null;
		}
		return convertService.convert(o, type);
	}

	private Object convert(final Object o, final Class<?> type) {
		if (o instanceof Class && convertService.supports((Class<?>) o, type)) {
			// NB: Class argument for matching; fill with null.
			return true;
		}
		return convertService.convert(o, type);
	}

	private String paramString(final Iterable<ModuleItem<?>> items) {
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final ModuleItem<?> item : items) {
			if (first) first = false;
			else sb.append(", ");
			sb.append(item.getType().getName() + " " + item.getName());
			if (!item.isRequired()) sb.append("?");
		}
		return sb.toString();
	}

}
