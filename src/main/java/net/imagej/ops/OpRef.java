/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2024 ImageJ2 developers.
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.imglib2.type.numeric.RealType;

import org.scijava.util.Types;

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
public class OpRef {

	/** Name of the op, or null for any name. */
	private final String name;

	/** Types which the op must match. */
	private final Collection<Type> types;

	/** The op's output parameter types, or null for no constraints. */
	private final List<Type> outTypes;

	/** Arguments to be passed to the op. */
	private final Object[] args;

	// -- Static construction methods --

	/**
	 * Creates a new op reference.
	 * 
	 * @param name name of the op, or null for any name.
	 * @param args arguments to the op.
	 */
	public static OpRef create(final String name, final Object... args) {
		return new OpRef(name, null, null, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param type type of op, or null for any type.
	 * @param args arguments to the op.
	 */
	public static OpRef create(final Type type, final Object... args) {
		return new OpRef(null, types(type), null, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param type1 first type constraint of the op.
	 * @param type2 second type constraint of the op.
	 * @param outType the type of the op's primary output, or null for any type.
	 * @param args arguments to the op.
	 */
	public static OpRef createTypes(final Type type1, final Type type2,
		final Type outType, final Object... args)
	{
		return new OpRef(null, types(type1, type2), types(outType), args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param types type constraints of op, or null for any type.
	 * @param args arguments to the op.
	 */
	public static OpRef createTypes(final Collection<? extends Type> types,
		final Object... args)
	{
		return new OpRef(null, types, null, args);
	}

	// -- Constructor --

	/**
	 * Creates a new op reference.
	 * 
	 * @param name name of the op, or null for any name.
	 * @param types types which the ops must match.
	 * @param outTypes the op's required output types.
	 * @param args arguments to the op.
	 */
	public OpRef(final String name, final Collection<? extends Type> types,
		final Collection<? extends Type> outTypes, final Object... args)
	{
		this.name = name;
		this.types = list(types);
		this.outTypes = list(outTypes);
		this.args = args;
	}

	// -- OpRef methods --

	/** Gets the name of the op. */
	public String getName() {
		return name;
	}

	/** Gets the types which the op must match. */
	public Collection<Type> getTypes() {
		return types;
	}

	/**
	 * Gets the op's output types (one constraint per output), or null for no
	 * constraints.
	 */
	public List<Type> getOutTypes() {
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
		if (types != null) {
			for (final Type t : types) {
				append(sb, Types.name(t));
			}
		}
		return sb.toString();
	}

	/** Determines whether the op's required types match the given class. */
	public boolean typesMatch(final Class<?> c) {
		if (types == null) return true;
		for (final Type t : types) {
			// NB: We have only a raw class as source. We check only for
			// raw assignability against the raw classes of each required type.
			if (!Types.raws(t).stream().allMatch(raw -> raw.isAssignableFrom(c))) {
				return false;
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
		for (final Object arg : args) {
			if (first) first = false;
			else sb.append(", ");

			// If arg itself *is* a Type, we use it directly,
			// because it's a special "typed null" placeholder.
			// Otherwise, we simply use the raw class of the arg.
			final Type argType = Type.class.isInstance(arg) ? //
				(Type) arg : arg.getClass();

			final List<Class<?>> classes = Types.raws(argType);
			if (classes.size() == 0) {
				// Exotic generic type with no upper bound classes!
				sb.append(Types.name(argType));
			}
			else {
				// Concatenate the list of simple class names with ampersands.
				// If arg is a Class, this will just resolve to its simple name.
				// But for generic types like "? extends foo.Foo & bar.Bar"
				// this will resolve to a string like "Foo & Bar".
				final List<String> classNames = classes.stream() //
					.map(c -> c.getSimpleName()).collect(Collectors.toList());
				sb.append(String.join(" & ", classNames));
			}
		}
		sb.append(")");

		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final OpRef other = (OpRef) obj;
		if (!Objects.equals(name, other.name)) return false;
		if (!Objects.equals(types, other.types)) return false;
		if (!Objects.equals(outTypes, other.outTypes)) return false;
		if (!Arrays.equals(args, other.args)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, types, outTypes, args);
	}

	// -- Utility methods --

	public static List<Type> types(final Type... types) {
		final ArrayList<Type> list = new ArrayList<>();
		for (Type t : types) if (t != null) list.add(t);
		return list;
	}

	// -- Helper methods --

	private void append(final StringBuilder sb, final String s) {
		if (s == null) return;
		if (sb.length() > 0) sb.append("/");
		sb.append(s);
	}

	private List<Type> list(final Collection<? extends Type> c) {
		final ArrayList<Type> list = new ArrayList<>();
		if (c != null) list.addAll(c);
		return list;
	}

}
