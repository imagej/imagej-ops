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
import imagej.command.CommandService;
import imagej.module.Module;
import imagej.module.ModuleService;

import java.util.ArrayList;
import java.util.List;

import org.scijava.InstantiableException;
import org.scijava.log.LogService;
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
public class DefaultOpMatcherService extends
	AbstractSingletonService<OperationMatcher> implements OpMatcherService
{

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private CommandService commandService;

	@Parameter
	private LogService log;

	// -- OpMatcherService methods --

	@Override
	public List<CommandInfo> findCandidates(final String name,
		final Class<? extends Op> type)
	{
		final List<CommandInfo> ops = commandService.getCommandsOfType(Op.class);
		final ArrayList<CommandInfo> candidates = new ArrayList<CommandInfo>();

		for (final CommandInfo info : ops) {
			if (!nameMatches(info, name)) continue;

			// the name matches; now check the class
			final Class<?> opClass;
			try {
				opClass = info.loadClass();
			}
			catch (final InstantiableException exc) {
				log.error("Invalid op: " + info.getClassName());
				return null;
			}
			if (type != null && !type.isAssignableFrom(opClass)) continue;

			candidates.add(info);
		}
		return candidates;
	}

	@Override
	public List<Module> findMatches(final List<? extends CommandInfo> ops,
		final Object... args)
	{
		final ArrayList<Module> matches = new ArrayList<Module>();

		// TODO: Consider inverting the loop nesting order here,
		// since we probably want to match higher priority Ops first.
		for (final OperationMatcher matcher : getInstances()) {
			double priority = Double.NaN;
			for (final CommandInfo info : ops) {
				final double p = info.getPriority();
				if (p != priority && !matches.isEmpty()) {
					// NB: Lower priority was reached; stop looking for any more matches.
					break;
				}
				priority = p;
				final Module module = matcher.match(info, args);
				if (module != null) matches.add(module);
			}
			if (!matches.isEmpty()) break;
		}

		return matches;
	}

	// -- SingletonService methods --

	@Override
	public Class<OperationMatcher> getPluginType() {
		return OperationMatcher.class;
	}

	// -- Helper methods --

	private boolean nameMatches(final CommandInfo info, final String name) {
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

}
