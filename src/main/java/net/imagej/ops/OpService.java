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

import java.util.Collection;

import net.imagej.ImageJService;

import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.plugin.PTService;

/**
 * Interface for services that manage and execute {@link Op}s.
 * 
 * @author Curtis Rueden
 */
public interface OpService extends PTService<Op>, ImageJService {

	/**
	 * Executes the given operation with the specified arguments. The best
	 * {@link Op} implementation to use will be selected automatically from the
	 * operation name and arguments.
	 * 
	 * @param name The operation to execute. If multiple {@link Op}s share this
	 *          name, then the best {@link Op} implementation to use will be
	 *          selected automatically from the name and arguments.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	Object run(String name, Object... args);

	/**
	 * Executes the operation of the given type with the specified arguments. The
	 * best {@link Op} implementation to use will be selected automatically from
	 * the operation type and arguments.
	 * 
	 * @param type The {@link Class} of the operation to execute. If multiple
	 *          {@link Op}s share this type (e.g., the type is an interface which
	 *          multiple {@link Op}s implement), then the best {@link Op}
	 *          implementation to use will be selected automatically from the type
	 *          and arguments.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	<OP extends Op> Object run(Class<OP> type, Object... args);

	/**
	 * Executes the given {@link Op} with the specified arguments.
	 * 
	 * @param op The {@link Op} to execute.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	Object run(Op op, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * populating its inputs.
	 * 
	 * @param name The name of the operation. If multiple {@link Op}s share this
	 *          name, then the best {@link Op} implementation to use will be
	 *          selected automatically from the name and arguments.
	 * @param args The operation's arguments.
	 * @return An {@link Op} with populated inputs, ready to run.
	 */
	Op op(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and arguments,
	 * populating its inputs.
	 * 
	 * @param type The {@link Class} of the operation. If multiple {@link Op}s
	 *          share this type (e.g., the type is an interface which multiple
	 *          {@link Op}s implement), then the best {@link Op} implementation to
	 *          use will be selected automatically from the type and arguments.
	 * @param args The operation's arguments.
	 * @return An {@link Op} with populated inputs, ready to run.
	 */
	<O extends Op> O op(Class<O> type, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 * 
	 * @param name The name of the operation.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	Module module(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 * 
	 * @param type The required type of the operation. If multiple {@link Op}s
	 *          share this type (e.g., the type is an interface which multiple
	 *          {@link Op}s implement), then the best {@link Op} implementation to
	 *          use will be selected automatically from the type and arguments.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	<OP extends Op> Module module(Class<OP> type, Object... args);

	/**
	 * Wraps the given {@link Op} as a {@link Module}, populating its inputs.
	 * 
	 * @param op The {@link Op} to wrap and populate.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the {@link Op}, with populated inputs,
	 *         ready to run.
	 */
	Module module(Op op, Object... args);

	/** Gets the metadata for a given {@link Op}. */
	CommandInfo info(Op op);

	/** Gets the names of all available operations. */
	Collection<String> ops();

	// -- Operation shortcuts --

	public @interface BuiltIn {
		// NB: Marker interface.
	}

	/** Executes the "add" operation on the given arguments. */
	Object add(Object... args);

	/** Executes the "ascii" operation on the given arguments. */
	Object ascii(Object... args);

	/** Executes the "chunker" operation on the given arguments. */
	Object chunker(Object... args);

	/** Executes the "convert" operation on the given arguments. */
	Object convert(Object... args);

	/** Executes the "convolve" operation on the given arguments. */
	Object convolve(Object... args);

	/** Executes the "createimg" operation on the given arguments. */
	Object createimg(Object... args);

	/** Executes the "crop" operation on the given arguments. */
	Object crop(Object... args);

	/** Executes the "deconvolve" operation on the given arguments. */
	Object deconvolve(Object... args);

	/** Executes the "divide" operation on the given arguments. */
	Object divide(Object... args);

	/** Executes the "equation" operation on the given arguments. */
	Object equation(Object... args);

	/** Executes the "eval" operation on the given arguments. */
	Object eval(Object... args);

	/** Executes the "fft" operation on the given arguments. */
	Object fft(Object... args);

	/** Executes the "gauss" operation on the given arguments. */
	Object gauss(Object... args);

	/** Executes the "gausskernel" operation on the given arguments. */
	Object gaussKernel(Object... args);

	/** Executes the "help" operation on the given arguments. */
	Object help(Object... args);

	/** Executes the "histogram" operation on the given arguments. */
	Object histogram(Object... args);

	/** Executes the "identity" operation on the given arguments. */
	Object identity(Object... args);

	/** Executes the "ifft" operation on the given arguments. */
	Object ifft(Object... args);

	/** Executes the "invert" operation on the given arguments. */
	Object invert(Object... args);

	/** Executes the "join" operation on the given arguments. */
	Object join(Object... args);

	/** Executes the "logkernel" operation on the given arguments. */
	Object logKernel(Object... args);

	/** Executes the "lookup" operation on the given arguments. */
	Object lookup(Object... args);

	/** Executes the "loop" operation on the given arguments. */
	Object loop(Object... args);

	/** Executes the "map" operation on the given arguments. */
	Object map(Object... args);

	/** Executes the "max" operation on the given arguments. */
	Object max(Object... args);

	/** Executes the "mean" operation on the given arguments. */
	Object mean(Object... args);

	/** Executes the "median" operation on the given arguments. */
	Object median(Object... args);

	/** Executes the "min" operation on the given arguments. */
	Object min(Object... args);

	/** Executes the "minmax" operation on the given arguments. */
	Object minmax(Object... args);

	/** Executes the "multiply" operation on the given arguments. */
	Object multiply(Object... args);

	/** Executes the "normalize" operation on the given arguments. */
	Object normalize(Object... args);

	/** Executes the "project" operation on the given arguments. */
	Object project(Object... args);

	/** Executes the "quantile" operation on the given arguments. */
	Object quantile(Object... args);

	/** Executes the "scale" operation on the given arguments. */
	Object scale(Object... args);

	/** Executes the "size" operation on the given arguments. */
	Object size(Object... args);

	/** Executes the "slicewise" operation on the given arguments. */
	Object slicewise(Object... args);

	/** Executes the "stddev" operation on the given arguments. */
	Object stddev(Object... args);

	/** Executes the "subtract" operation on the given arguments. */
	Object subtract(Object... args);

	/** Executes the "sum" operation on the given arguments. */
	Object sum(Object... args);

	/** Executes the "threshold" operation on the given arguments. */
	Object threshold(Object... args);

	/** Executes the "variance" operation on the given arguments. */
	Object variance(Object... args);

	// -- Deprecated methods --

	/** @deprecated Use {@link #createimg} instead. */
	@Deprecated
	Object create(Object... args);

}
