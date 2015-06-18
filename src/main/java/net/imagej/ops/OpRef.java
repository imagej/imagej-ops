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

import net.imagej.ops.Op;

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

	/** Type of the op, or null for any type. */
	private Class<OP> type;

	/** Arguments to be passed to the op. */
	private Object[] args;

	/**
	 * Creates a new op reference.
	 * 
	 * @param name
	 *            name of the op, or null for any name.
	 * @param args
	 *            arguments to the op.
	 */
	public OpRef(final String name, final Object... args) {
		this(name, null, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param type
	 *            type of op, or null for any type.
	 * @param args
	 *            arguments to the op.
	 */
	public OpRef(final Class<OP> type, final Object... args) {
		this(null, type, args);
	}

	/**
	 * Creates a new op reference.
	 * 
	 * @param name
	 *            name of the op, or null for any name.
	 * @param type
	 *            type of op, or null for any type.
	 * @param args
	 *            arguments to the op.
	 */
	public OpRef(final String name, final Class<OP> type, final Object... args) {
		this.name = name;
		this.type = type;
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

	/** Gets the op's arguments. */
	public Object[] getArgs() {
		return args;
	}

	/** Gets a label identifying the op's scope (i.e., its type or name). */
	public String getLabel() {
		if (type != null)
			return type.getName();
		return name == null ? "(any)" : name;
	}

	// -- Object methods --

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLabel());
		sb.append("(");
		boolean first = true;
		for (Object arg : args) {
			if (first)
				first = false;
			else
				sb.append(", ");
			if (arg.getClass() == Class.class) {
				// special typed null placeholder
				sb.append(((Class<?>) arg).getSimpleName());
			} else
				sb.append(arg.getClass().getSimpleName());

		}
		sb.append(")");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		int hash = 31;
		for (final Object o : args) {
			if (o != null) {
				hash += o.hashCode() * 31;
			}
		}
		return type.hashCode() * 31 + hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OpRef)) {
			return false;
		}

		final OpRef<?> ref = (OpRef<?>) obj;

		// check type
		if (ref.getType() != type) {
			return false;
		}

		// check name
		if (ref.getName() != name) {
			return false;
		}

		// check args
		if (!Arrays.equals(ref.getArgs(), args)) {
			return false;
		}

		return true;
	}

}
