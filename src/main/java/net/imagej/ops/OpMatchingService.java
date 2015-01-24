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

import java.util.List;

import net.imagej.ImageJService;

import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.plugin.SingletonService;

/**
 * Interface for services that find {@link Op}s which match a template.
 * 
 * @author Curtis Rueden
 */
public interface OpMatchingService extends SingletonService<Optimizer>,
	ImageJService
{

	/** Gets the list of all available {@link Op} implementations. */
	public List<CommandInfo> getOps();

	/**
	 * Finds and initializes the best module matching the given op name and/or
	 * type + arguments.
	 * 
	 * @param name The name of the operation, or null to match all names.
	 * @param type The required type of the operation, or null to match all types.
	 *          If multiple {@link Op}s share this type (e.g., the type is an
	 *          interface which multiple {@link Op}s implement), then the best
	 *          {@link Op} implementation to use will be selected automatically
	 *          from the type and arguments.
	 * @param args The operation's input arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 * @throws IllegalArgumentException if there is no match, or if there is more
	 *           than one match at the same priority.
	 */
	public Module findModule(String name, Class<? extends Op> type,
		Object... args);

	/**
	 * Builds a list of candidate ops which match the given name and class.
	 * 
	 * @param name The op's name, or null to match all names.
	 * @param type Required type of the op, or null to match all types.
	 * @return The list of candidates as {@link ModuleInfo} metadata.
	 */
	List<ModuleInfo> findCandidates(String name, Class<? extends Op> type);

	/**
	 * Filters a list of ops to those matching the given arguments.
	 * 
	 * @param ops The list of ops to scan for matches.
	 * @param args The op's input arguments.
	 * @return The list of matching ops as {@link Module} instances.
	 */
	List<Module> findMatches(List<? extends ModuleInfo> ops, Object... args);

	/**
	 * Attempts to match the given arguments to the {@link Op} described by the
	 * specified {@link ModuleInfo}.
	 * 
	 * @return A populated {@link Module} instance for the matching {@link Op}, or
	 *         null if the arguments do not match the {@link Op}.
	 */
	Module match(ModuleInfo info, Object... args);

	/**
	 * Optimizes the performance of the given {@link Module} using all available
	 * {@link Optimizer}s.
	 */
	Module optimize(Module module);

	/** Assigns arguments into the given module's inputs. */
	Module assignInputs(Module module, Object... args);

	/**
	 * Gets a string describing the given op template.
	 * 
	 * @param name The op's name.
	 * @param args The op's input arguments.
	 * @return A string describing the op template.
	 */
	String getOpString(String name, Object... args);

	/**
	 * Gets a string describing the given op.
	 * 
	 * @param info The {@link ModuleInfo} metadata which describes the op.
	 * @return A string describing the op.
	 */
	String getOpString(ModuleInfo info);

	/**
	 * Analyzes the given list of candidates and module matches.
	 * 
	 * @return an explanation for why a single op could not be selected.
	 */
	String analyze(String label, List<ModuleInfo> candidates,
		List<Module> matches, Object... args);

	boolean isCandidate(CommandInfo info, String name);

	boolean isCandidate(CommandInfo info, Class<? extends Op> type);

	boolean isCandidate(CommandInfo info, String name, Class<? extends Op> type);

}
