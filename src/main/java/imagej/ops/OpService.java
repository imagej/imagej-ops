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

import imagej.module.Module;
import imagej.service.ImageJService;

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
	 *          implementation to use will be selected automatically from the
	 *          type and arguments.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	Object run(Class<? extends Op> type, Object... args);

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
	Op op(Class<? extends Op> type, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 * 
	 * @param name The name of the operation.
	 * @param args The operation's arguments
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	Module module(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 * 
	 * @param type The {@link Class} of the operation. If multiple {@link Op}s
	 *          share this type (e.g., the type is an interface which multiple
	 *          {@link Op}s implement), then the best {@link Op} implementation to
	 *          use will be selected automatically from the type and arguments.
	 * @param args The operation's arguments
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	Module module(Class<? extends Op> type, Object... args);

	/**
	 * Wraps the given {@link Op} as a {@link Module}, populating its inputs.
	 * 
	 * @param op The {@link Op} to wrap and populate.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the {@link Op}, with populated inputs,
	 *         ready to run.
	 */
	Module module(Op op, Object... args);

	/** Assigns arguments into the given module's inputs. */
	Module assignInputs(Module module, Object... args);

	// -- Operation shortcuts --

	/** Executes the "add" operation on the given arguments. */
	Object add(Object... args);

	/** Executes the "chunker" operation on the given arguments. */
	Object chunker(Object... args);

	/** Executes the "convert" operation on the given arguments. */
	Object convert(Object... args);

	/** Executes the "convolve" operation on the given arguments. */
	Object convolve(Object... args);

	/** Executes the "divide" operation on the given arguments. */
	Object divide(Object... args);

	/** Executes the "gauss" operation on the given arguments. */
	Object gauss(Object... args);

	/** Executes the "infinity" operation on the given arguments. */
	Object infinity(Object... args);

	/** Executes the "map" operation on the given arguments. */
	Object map(Object... args);

	/** Executes the "max" operation on the given arguments. */
	Object max(Object... args);

	/** Executes the "minmax" operation on the given arguments. */
	Object minmax(Object... args);

	/** Executes the "multiply" operation on the given arguments. */
	Object multiply(Object... args);

	/** Executes the "neighborhood" operation on the given arguments. */
	Object neighborhood(Object... args);

	/** Executes the "otsu" operation on the given arguments. */
	Object otsu(Object... args);

	/** Executes the "pixThreshold" operation on the given arguments. */
	Object pixThreshold(Object... args);

	/** Executes the "project" operation on the given arguments. */
	Object project(Object... args);

	/** Executes the "slicemapper" operation on the given arguments. */
	Object slicemapper(Object... args);

	/** Executes the "slicer" operation on the given arguments. */
	Object slicer(Object... args);

	/** Executes the "subtract" operation on the given arguments. */
	Object subtract(Object... args);

	/** Executes the "sum" operation on the given arguments. */
	Object sum(Object... args);

	/** Executes the "threshold" operation on the given arguments. */
	Object threshold(Object... args);

}
