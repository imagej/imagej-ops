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

import java.util.Arrays;
import java.util.Collection;

import org.scijava.util.MiscUtils;

/**
 * Data structure which identifies an op by name and/or type, along with a list
 * of input arguments.
 * <p>
 * With the help of the {@link OpMatchingService}, an {@code OpRef} holds all
 * information needed to create an appropriate {@link Op}.
 * </p>
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 */
public class OpRef<OP extends Op> {

	/** Name of the op, or null for any name. */
	private String name;

	/** Types which the op must match. */
	private Collection<? extends Class<?>> types;

	/** Type of the op, or null for any type. */
	private Class<OP> type;

	/** Arguments to be passed to the op. */
	private Object[] args;

	/**
	 * Creates a new op reference.
	 * 
	 * @param name name of the op, or null for any name.
	 * @param args arguments to the op.
	 */
	public OpRef(final String name, final Object... args) {
		this(name, null, null, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param type type of op, or null for any type.
	 * @param args arguments to the op.
	 */
	public OpRef(final Class<OP> type, final Object... args) {
		this(null, null, type, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param types types which the ops must match.
	 * @param type type of op, or null for any type.
	 * @param args arguments to the op.
	 */
	public OpRef(final Collection<? extends Class<?>> types,
		final Class<OP> type, final Object... args)
	{
		this(null, types, type, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param name name of the op, or null for any name.
	 * @param types types which the ops must match.
	 * @param type type of op, or null for any type.
	 * @param args arguments to the op.
	 */
	public OpRef(final String name, final Collection<? extends Class<?>> types,
		final Class<OP> type, final Object... args)
	{
		this.name = name;
		this.types = types;
		this.type = type;
		this.args = args;
	}

	// -- OpRef methods --

	/** Gets the name of the op. */
	public String getName() {
		return name;
	}

	/** Gets the types with which the op must match. */
	public Collection<? extends Class<?>> getTypes() {
		return types;
	}

	/** Gets the type of the op. */
	public Class<OP> getType() {
		return type;
	}

	/** Gets the op's arguments. */
	public Object[] getArgs() {
		return args;
	}

	/** Gets a label identifying the op's scope (i.e., its type or name). */
	public String getLabel() {
		if (type != null) return type.getName();
		return name == null ? "(any)" : name;
	}

	/** Determines whether the op's required types match the given class. */
	public boolean typesMatch(final Class<?> c) {
		if (types != null) {
			for (final Class<?> t : types) {
				if (!t.isAssignableFrom(c)) return false;
			}
		}
		if (type != null && !type.isAssignableFrom(c)) return false;
		return true;
	}

	// -- Object methods --

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLabel());
		sb.append("(");
		boolean first = true;
		for (Object arg : args) {
			if (first) first = false;
			else sb.append(", ");
			if (arg.getClass() == Class.class) {
				// special typed null placeholder
				sb.append(((Class<?>) arg).getSimpleName());
			}
			else sb.append(arg.getClass().getSimpleName());

		}
		sb.append(")");

		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final OpRef<?> other = (OpRef<?>) obj;
		if (!MiscUtils.equal(name, other.name)) return false;
		if (!MiscUtils.equal(types, other.types)) return false;
		if (!MiscUtils.equal(type, other.type)) return false;
		if (!Arrays.equals(args, other.args)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 31;
		for (final Object o : args) {
			hash += o.hashCode() * 31;
		}
		return type.hashCode() * 31 + hash;
	}

}
