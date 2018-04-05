/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginService;

/**
 * An <em>op</em> is an operation that computes a result in a deterministic and
 * consistent way.
 * <p>
 * <p>
 * Ops discoverable at runtime must implement this interface and be annotated
 * with @{@link Plugin} with attribute {@link Plugin#type()} = {@link Op}.class
 * or a subtype thereof (see the interfaces of {@link Ops}). While it is
 * possible to create an op merely by implementing this interface, it is
 * encouraged to instead extend {@link AbstractOp}, for convenience.
 * </p>
 * <h2>Naming and matching</h2>
 * <p>
 * Each op has a name (provided in the {@link Plugin#name()} attribute, which is
 * not necessarily unique. This allows multiple "overloaded" ops with different
 * combinations of parameter types, similar to the method overloading feature of
 * Java and other programming languages. The
 * {@link OpService#op(String, Object...)} and
 * {@link OpService#run(String, Object...)} methods can be used to obtain and
 * execute (respectively) the best matching op instances for a given name and
 * set of input arguments.
 * <p>
 * An op may optionally have a namespace. This is analogous to packages in Java
 * and similar features in other languages. The namespace is expressed as a
 * prefix; such ops are referenced by their namespace, followed by a dot,
 * followed by the op name. Ops without a namespace belong to the "global"
 * namespace, with no prefix or dot. Two ops with the same name but different
 * namespaces only partially "overload" or "override" one other; see
 * {@link OpService#run(String, Object...)} for details.
 * </p>
 * <p>
 * The naming convention for both namespaces and op names is to use an
 * alphameric string, in lower camel case.
 * </p>
 * <h2>Comparison with {@link Command}</h2>
 * <p>
 * Every op is a {@link Command}. And like {@link Command}s, ops may have
 * multiple inputs and multiple outputs. However, ops impose some additional
 * requirements beyond those of regular {@link Command}s:
 * </p>
 * <ul>
 * <li>Op arguments are fixed: an op may not dynamically alter the number or
 * types of its arguments.</li>
 * <li>Calling the same op twice with the same argument values must result in
 * the same result (for a given op execution environment).</li>
 * </ul>
 * 
 * @author Curtis Rueden
 * @see Plugin
 * @see PluginService
 */
public interface Op extends Command, Environmental {
	// NB: Marker interface.
}
