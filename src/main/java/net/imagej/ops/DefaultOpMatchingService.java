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

package net.imagej.ops;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.OpCandidate.StatusCode;

import org.scijava.Context;
import org.scijava.InstantiableException;
import org.scijava.command.CommandInfo;
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
 * Default service for finding {@link Op}s which match a request.
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
	public <OP extends Op> Module findModule(final OpRef<OP> ref) {
		// find candidates with matching name & type
		final List<OpCandidate<OP>> candidates = findCandidates(ref);
		if (candidates.isEmpty()) {
			throw new IllegalArgumentException("No candidate '" + ref.getLabel() +
				"' ops");
		}

		// narrow down candidates to the exact matches
		final List<Module> matches = findMatches(candidates);

		if (matches.size() == 1) {
			// a single match: return it
			if (log.isDebug()) {
				log.debug("Selected '" + ref.getLabel() + "' op: " +
					matches.get(0).getDelegateObject().getClass().getName());
			}
			return optimize(matches.get(0));
		}

		final String analysis = OpUtils.matchInfo(candidates, matches);
		throw new IllegalArgumentException(analysis);
	}

	@Override
	public <OP extends Op> List<OpCandidate<OP>> findCandidates(
		final OpRef<OP> ref)
	{
		final ArrayList<OpCandidate<OP>> candidates =
			new ArrayList<OpCandidate<OP>>();
		for (final CommandInfo info : getOps()) {
			if (isCandidate(info, ref)) {
				candidates.add(new OpCandidate<OP>(ref, info));
			}
		}
		return candidates;
	}

	@Override
	public <OP extends Op> List<Module> findMatches(
		final List<OpCandidate<OP>> candidates)
	{
		final ArrayList<Module> matches = new ArrayList<Module>();

		double priority = Double.NaN;
		for (final OpCandidate<?> candidate : candidates) {
			final ModuleInfo info = candidate.getInfo();
			final double p = info.getPriority();
			if (p != priority && !matches.isEmpty()) {
				// NB: Lower priority was reached; stop looking for any more matches.
				break;
			}
			priority = p;

			final Module module = match(candidate);

			if (module != null) matches.add(module);
		}

		return matches;
	}

	@Override
	public <OP extends Op> Module match(final OpCandidate<OP> candidate) {
		if (!valid(candidate)) return null;
		final Object[] args = padArgs(candidate);
		return args == null ? null : match(candidate, args);
	}

	@Override
	public <OP extends Op> boolean typesMatch(final OpCandidate<OP> candidate) {
		if (!valid(candidate)) return false;
		final Object[] args = padArgs(candidate);
		return args == null ? false : typesMatch(candidate, args);
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
	public <OP extends Op> Object[] padArgs(final OpCandidate<OP> candidate) {
		int inputCount = 0, requiredCount = 0;
		for (final ModuleItem<?> item : candidate.getInfo().inputs()) {
			inputCount++;
			if (item.isRequired()) requiredCount++;
		}
		final Object[] args = candidate.getRef().getArgs();
		if (args.length == inputCount) {
			// correct number of arguments
			return args;
		}
		if (args.length > inputCount) {
			// too many arguments
			candidate.setStatus(StatusCode.TOO_MANY_ARGS,
				args.length + " > " + inputCount);
			return null;
		}
		if (args.length < requiredCount) {
			// too few arguments
			candidate.setStatus(StatusCode.TOO_FEW_ARGS,
				args.length + " < " + requiredCount);
			return null;
		}

		// pad optional parameters with null (from right to left)
		final int argsToPad = inputCount - args.length;
		final int optionalCount = inputCount - requiredCount;
		final int optionalsToFill = optionalCount - argsToPad;
		final Object[] paddedArgs = new Object[inputCount];
		int argIndex = 0, paddedIndex = 0, optionalIndex = 0;
		for (final ModuleItem<?> item : candidate.getInfo().inputs()) {
			if (!item.isRequired() && optionalIndex++ >= optionalsToFill) {
				// skip this optional parameter (pad with null)
				paddedIndex++;
				continue;
			}
			paddedArgs[paddedIndex++] = args[argIndex++];
		}
		return paddedArgs;
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
			sb.append("\t\t" + OpUtils.opString(optimal.get(i).getInfo()) + "\n");
		}
		log.warn(sb.toString());
		return module;
	}

	// -- PTService methods --

	@Override
	public Class<Optimizer> getPluginType() {
		return Optimizer.class;
	}

	// -- Helper methods --

	/** Helper method of {@link #findCandidates}. */
	private <OP extends Op> boolean isCandidate(final CommandInfo info,
		final OpRef<OP> ref)
	{
		if (!nameMatches(info, ref.getName())) return false;

		// the name matches; now check the class
		final Class<?> opClass;
		try {
			opClass = info.loadClass();
		}
		catch (final InstantiableException exc) {
			log.error("Invalid op: " + info.getClassName());
			return false;
		}

		return ref.getType() == null || ref.getType().isAssignableFrom(opClass);
	}

	/** Verifies that the given candidate's module is valid. */
	private <OP extends Op> boolean valid(final OpCandidate<OP> candidate) {
		if (candidate.getInfo().isValid()) return true;
		candidate.setStatus(StatusCode.INVALID_MODULE);
		return false;
	}

	/** Helper method of {@link #match(OpCandidate)}. */
	private <OP extends Op> Module match(final OpCandidate<OP> candidate,
		final Object[] args)
	{
		// check that each parameter is compatible with its argument
		if (!typesMatch(candidate, args)) return null;

		// create module and assign the inputs
		final Module module = createModule(candidate.getInfo(), args);
		candidate.setModule(module);

		// make sure the op itself is happy with these arguments
		final Object op = module.getDelegateObject();
		if (op instanceof Contingent) {
			final Contingent c = (Contingent) op;
			if (!c.conforms()) {
				candidate.setStatus(StatusCode.DOES_NOT_CONFORM);
				return null;
			}
		}

		// found a match!
		return module;
	}

	/**
	 * Checks that each parameter is type-compatible with its corresponding
	 * argument.
	 */
	private <OP extends Op> boolean typesMatch(final OpCandidate<OP> candidate,
		final Object[] args)
	{
		int i = 0;
		for (final ModuleItem<?> item : candidate.getInfo().inputs()) {
			final Object arg = args[i++];
			if (!canAssign(candidate, arg, item)) return false;
		}
		return true;
	}

	/** Helper method of {@link #isCandidate}. */
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

	/** Helper method of {@link #match(OpCandidate, Object[])}. */
	private Module createModule(final ModuleInfo info, final Object... args) {
		final Module module = moduleService.createModule(info);
		context.inject(module.getDelegateObject());
		return assignInputs(module, args);
	}

	/** Helper method of {@link #match(OpCandidate, Object[])}. */
	private boolean canAssign(final OpCandidate<?> candidate, final Object arg,
		final ModuleItem<?> item)
	{
		if (arg == null) {
			if (item.isRequired()) {
				candidate.setStatus(StatusCode.REQUIRED_ARG_IS_NULL, null, item);
				return false;
			}
			return true;
		}

		final Type type = item.getGenericType();
		if (!canConvert(arg, type)) {
			candidate.setStatus(StatusCode.CANNOT_CONVERT,
				arg.getClass().getName() + " => " + type, item);
			return false;
		}

		return true;
	}

	/** Helper method of {@link #canAssign}. */
	private boolean canConvert(final Object arg, final Type type) {
		if (isMatchingClass(arg, type)) {
			// NB: Class argument for matching, to help differentiate op signatures.
			return true;
		}
		return convertService.supports(arg, type);
	}

	/** Helper method of {@link #assignInputs}. */
	private void assign(final Module module, final Object arg,
		final ModuleItem<?> item)
	{
		if (arg != null) {
			final Type type = item.getGenericType();
			final Object value = convert(arg, type);
			module.setInput(item.getName(), value);
		}
		module.setResolved(item.getName(), true);
	}

	/** Helper method of {@link #assign}. */
	private Object convert(final Object arg, final Type type) {
		if (isMatchingClass(arg, type)) {
			// NB: Class argument for matching; fill with null.
			return null;
		}
		return convertService.convert(arg, type);
	}

	/** Determines whether the argument is a matching class instance. */
	private boolean isMatchingClass(final Object arg, final Type type) {
		return arg instanceof Class &&
			convertService.supports((Class<?>) arg, type);
	}
}
