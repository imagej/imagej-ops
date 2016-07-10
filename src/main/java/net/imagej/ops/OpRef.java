/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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
import java.util.Collections;
import java.util.Set;

import org.scijava.util.MiscUtils;

/**
 * Data structure which identifies an op by name and/or type(s) and/or argument
 * type(s), along with a list of input arguments.
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
	private final String name;

	/** Type of the op, or null for any type. */
	private final Class<OP> type;

	/** Extra types which the op must match. */
	private final Collection<? extends Class<?>> extraTypes;

	/** The op's output parameter types, or null for no constraints. */
	private final Collection<? extends Class<?>> outTypes;

	/** Arguments to be passed to the op. */
	private final Object[] args;

	// -- Static construction methods --

	/**
	 * Creates a new op reference.
	 * 
	 * @param name name of the op, or null for any name.
	 * @param args arguments to the op.
	 */
	public static OpRef<Op> create(final String name, final Object... args) {
		return new OpRef<>(name, null, null, null, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param type type of op, or null for any type.
	 * @param args arguments to the op.
	 */
	public static <OP extends Op> OpRef<OP> create(final Class<OP> type,
		final Object... args)
	{
		return new OpRef<>(null, type, null, null, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param type type of op, or null for any type.
	 * @param extraType the additional type constraint.
	 * @param outType the type of the op's primary output, or null for any type.
	 * @param args arguments to the op.
	 */
	public static <OP extends Op> OpRef<OP> createTypes(final Class<OP> type,
		final Class<?> extraType, final Class<?> outType, final Object... args)
	{
		return new OpRef<>(null, type, set(extraType), set(outType), args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param type type of op, or null for any type.
	 * @param extraTypes additional types which the ops must match.
	 * @param args arguments to the op.
	 */
	public static <OP extends Op> OpRef<OP> createTypes(final Class<OP> type,
		final Collection<? extends Class<?>> extraTypes, final Object... args)
	{
		return new OpRef<>(null, type, extraTypes, null, args);
	}

	// -- Constructor --

	/**
	 * Creates a new op reference.
	 * 
	 * @param name name of the op, or null for any name.
	 * @param type type of op, or null for any type.
	 * @param extraTypes additional types which the ops must match.
	 * @param outTypes the op's required output types.
	 * @param args arguments to the op.
	 */
	public OpRef(final String name, final Class<OP> type,
		final Collection<? extends Class<?>> extraTypes,
		final Collection<? extends Class<?>> outTypes, final Object... args)
	{
		this.name = name;
		this.type = type;
		this.extraTypes = extraTypes;
		this.outTypes = outTypes;
		this.args = args;
	}

	// -- OpRef methods --

	/** Gets the name of the op. */
	public String getName() {
		return name;
	}

	/** Gets the type of the op. */
	public Class<OP> getType() {
		return type;
	}

	/** Gets the types with which the op must match. */
	public Collection<? extends Class<?>> getExtraTypes() {
		return extraTypes;
	}

	/** Gets the op's output types, or null for no constraints. */
	public Collection<? extends Class<?>> getOutTypes() {
		return outTypes;
	}

	/** Gets the op's arguments. */
	public Object[] getArgs() {
		return args;
	}

	/** Gets a label identifying the op's scope (i.e., its name and/or types). */
	public String getLabel() {
		final StringBuilder sb = new StringBuilder();
		append(sb, name);
		if (type != null) append(sb, type.getName());
		if (extraTypes != null) {
			for (final Class<?> t : extraTypes) {
				append(sb, t.getName());
			}
		}
		return sb.toString();
	}

	/** Determines whether the op's required types match the given class. */
	public boolean typesMatch(final Class<?> c) {
		if (type != null && !type.isAssignableFrom(c)) return false;
		if (extraTypes != null) {
			for (final Class<?> t : extraTypes) {
				if (!t.isAssignableFrom(c)) return false;
			}
		}
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
		if (!MiscUtils.equal(type, other.type)) return false;
		if (!MiscUtils.equal(extraTypes, other.extraTypes)) return false;
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

	// -- Helper methods --

	private void append(final StringBuilder sb, final String s) {
		if (s == null) return;
		if (sb.length() > 0) sb.append("/");
		sb.append(s);
	}

	private static <E> Set<E> set(final E element) {
		return element == null ? null : Collections.singleton(element);
	}

}
