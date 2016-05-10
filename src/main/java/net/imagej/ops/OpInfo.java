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

import java.lang.reflect.Field;

import org.scijava.command.CommandInfo;
import org.scijava.plugin.SciJavaPlugin;
import org.scijava.util.ClassUtils;

/**
 * Metadata about a particular {@link Op} implementation.
 * 
 * @author Curtis Rueden
 * @see CommandInfo
 * @see OpEnvironment#ops()
 */
public class OpInfo implements Comparable<OpInfo> {

	private final CommandInfo cInfo;
	private String[] aliases = null;
	private String name = null;
	private boolean initializedAliases = false;
	private boolean initializedName = false;

	public OpInfo(final CommandInfo cInfo) {
		this.cInfo = cInfo;
	}

	public OpInfo(final Class<? extends Op> opClass) {
		this(new CommandInfo(opClass));
	}

	/** Gets the associated {@link CommandInfo} metadata. */
	public CommandInfo cInfo() {
		return cInfo;
	}

	/** Gets whether the op has a name. */
	public boolean isNamed() {
		final String opName = getName();
		return opName != null && !opName.isEmpty();
	}

	/** Gets the fully qualified name, with namespace. */
	public String getName() {
		if (!initializedName) createName();
		return name;
	}

	/** Gets the name without namespace prefix. */
	public String getSimpleName() {
		return OpUtils.stripNamespace(getName());
	}

	/** Gets whether the given name matches this op. */
	public boolean nameMatches(final String testName) {
		if (testName == null) return true; // not filtering on name

		// check if name matches exactly
		final String opName = getName();
		if (nameMatches(opName, testName)) return true;

		// check for aliases
		final String[] opAliases = getAliases();
		if (opAliases != null) {
			for (final String a : opAliases) {
				if (nameMatches(a, testName)) return true;
			}
		}

		return false;
	}

	/** Gets the fully qualified aliases. */
	public String[] getAliases() {
		if (!initializedAliases) createAliases();
		return aliases;
	}

	/** Gets the namespace. */
	public String getNamespace() {
		return OpUtils.getNamespace(getName());
	}

	/** Gets whether the op belongs to the given namespace. */
	public boolean isNamespace(final String namespace) {
		final String ns = getNamespace();
		if (ns == null) return namespace == null;
		return ns.equals(namespace) || ns.startsWith(namespace + ".");
	}

	/**
	 * Gets the type of op&mdash, as specified via {@code @Plugin(type = <type>)}).
	 */
	public Class<? extends Op> getType() {
		// HACK: The CommandInfo.getPluginType() method returns Command.class.
		// But we want to get the actual type of the wrapped PluginInfo.
		// CommandInfo does not have a getPluginInfo() unwrap method, though.
		// So let's extract the type directly from the @Plugin annotation.
		final Class<? extends SciJavaPlugin> type = cInfo.getAnnotation().type();
		if (type == null || !Op.class.isAssignableFrom(type)) {
			throw new IllegalStateException("Op type " + type + " is not an Op!");
		}
		@SuppressWarnings("unchecked")
		final Class<? extends Op> opType = (Class<? extends Op>) type;
		return opType;
	}

	// -- Object methods --

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof OpInfo)) return false;
		final OpInfo that = (OpInfo) o;
		return cInfo().equals(that.cInfo());
	}

	@Override
	public int hashCode() {
		return cInfo().hashCode();
	}

	@Override
	public String toString() {
		return OpUtils.opString(cInfo());
	}

	// -- Comparable methods --

	@Override
	public int compareTo(final OpInfo that) {
		return cInfo().compareTo(that.cInfo());
	}

	// -- Helper methods --

	/**
	 * Synchronized double-locked method to ensure aliases are only discovered
	 * once per {@link OpInfo}.
	 */
	private synchronized void createAliases() {
		if (!initializedAliases) {
			// check for an alias
			final String alias = cInfo().get("alias");
			String[] aliasArray = null;
			if (alias != null) aliasArray = new String[] { alias };

			// no single alias; check for a list of aliases
			if (aliasArray == null) {
				final String opAliases = cInfo().get("aliases");
				if (opAliases != null) aliasArray = opAliases.split("\\s*,\\s*");
			}

			// alias not explicitly specified; look for ALIAS constant
			if (aliasArray == null) {
				final String aliasField = getFieldValue(String.class, "ALIAS");
				if (aliasField != null) aliasArray = new String[] {aliasField};
			}

			// no single alias; look for ALIASES constant
			if (aliasArray == null) {
				final String aliasesField = getFieldValue(String.class, "ALIASES");
				if (aliasesField != null) aliasArray = aliasesField.split("\\s*,\\s*");
			}
			aliases = aliasArray;
			initializedAliases = true;
		}
	}

	/**
	 * Synchronized double-locked method to ensure names are only discovered once
	 * per {@link OpInfo}.
	 */
	private synchronized void createName() {
		if (!initializedName) {
			String opName = cInfo().getName();

			// if name not explicitly specified; look for NAME constant
			if (opName == null || opName.isEmpty())
				opName = getFieldValue(String.class, "NAME");

			name = opName;
			initializedName = true;
		}
	}

	/** Helper method of {@link #getName} and {@link #getAliases}. */
	private <T> T getFieldValue(final Class<T> fieldType, final String fieldName)
	{
		final Class<? extends Op> opType = getType();
		final Field nameField = ClassUtils.getField(opType, fieldName);
		if (nameField == null) return null;
		if (!fieldType.isAssignableFrom(nameField.getType())) return null;
		@SuppressWarnings("unchecked")
		final T value = (T) ClassUtils.getValue(nameField, null);
		return value;
	}

	private static boolean nameMatches(final String opName, final String name) {
		if (opName == null) return false;
		if (name.equals(opName)) return true;

		// check if name matches w/o namespace (e.g., 'add' matches 'math.add')
		final int dot = opName.lastIndexOf(".");
		return dot >= 0 && name.equals(opName.substring(dot + 1));
	}

}
