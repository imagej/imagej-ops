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
 * Interface for services that find {@link Op}s which match an {@link OpRef}.
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
	 * @param ref The op reference describing the op to match.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 * @throws IllegalArgumentException if there is no match, or if there is more
	 *           than one match at the same priority.
	 */
	public <OP extends Op> Module findModule(OpRef<OP> ref);

	/**
	 * Builds a list of candidate ops which might match the given op reference.
	 * 
	 * @param ref The op reference describing the op to match.
	 * @return The list of candidate operations.
	 */
	<OP extends Op> List<OpCandidate<OP>> findCandidates(OpRef<OP> ref);

	/**
	 * Filters a list of ops to those matching the given arguments.
	 * 
	 * @param candidates The list of op candidates to scan for matches.
	 * @return The list of matching ops as {@link Module} instances.
	 */
	<OP extends Op> List<Module> findMatches(List<OpCandidate<OP>> candidates);

	/**
	 * Attempts to match the given arguments to the {@link Op} described by the
	 * specified {@link ModuleInfo}.
	 * 
	 * @return A populated {@link Module} instance for the matching {@link Op}, or
	 *         null if the arguments do not match the {@link Op}.
	 */
	<OP extends Op> Module match(OpCandidate<OP> candidate);

	/**
	 * Checks that each parameter is type-compatible with its corresponding
	 * argument.
	 */
	<OP extends Op> boolean typesMatch(OpCandidate<OP> candidate);

	/** Checks the number of args, padding optional args with null as needed. */
	<OP extends Op> Object[] padArgs(OpCandidate<OP> candidate);

	/**
	 * Optimizes the performance of the given {@link Module} using all available
	 * {@link Optimizer}s.
	 */
	Module optimize(Module module);

	/** Assigns arguments into the given module's inputs. */
	Module assignInputs(Module module, Object... args);

}
