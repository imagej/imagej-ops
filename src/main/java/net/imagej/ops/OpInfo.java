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

import java.lang.reflect.Field;
import java.util.List;

import org.scijava.command.CommandInfo;
import org.scijava.module.ModuleItem;
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

	/** Gets the op's input parameters. */
	public List<ModuleItem<?>> inputs() {
		return OpUtils.inputs(cInfo());
	}

	/** Gets the op's output parameters. */
	public List<ModuleItem<?>> outputs() {
		return OpUtils.outputs(cInfo());
	}

	/** Gets whether the op has a name. */
	public boolean isNamed() {
		final String name = getName();
		return name != null && !name.isEmpty();
	}

	/** Gets the fully qualified name, with namespace. */
	public String getName() {
		final String name = cInfo().getName();
		if (name != null && !name.isEmpty()) return name;

		// name not explicitly specified; look for NAME constant
		return getFieldValue(String.class, "NAME");
	}

	/** Gets the name without namespace prefix. */
	public String getSimpleName() {
		return OpUtils.stripNamespace(getName());
	}

	/** Gets whether the given name matches this op. */
	public boolean nameMatches(final String name) {
		if (name == null) return true; // not filtering on name

		// check if name matches exactly
		final String opName = getName();
		if (nameMatches(opName, name)) return true;

		// check for aliases
		final String[] aliases = getAliases();
		if (aliases != null) {
			for (final String a : aliases) {
				if (nameMatches(a, name)) return true;
			}
		}

		return false;
	}

	/** Gets the fully qualified aliases. */
	public String[] getAliases() {
		// check for an alias
		final String alias = cInfo().get("alias");
		if (alias != null) return new String[] { alias };

		// no single alias; check for a list of aliases
		final String aliases = cInfo().get("aliases");
		if (aliases != null) return aliases.split("\\s*,\\s*");

		// alias not explicitly specified; look for ALIAS constant
		final String aliasField = getFieldValue(String.class, "ALIAS");
		if (aliasField != null) return new String[] {aliasField};

		// no single alias; look for ALIASES constant
		final String aliasesField = getFieldValue(String.class, "ALIASES");
		if (aliasesField != null) return aliasesField.split("\\s*,\\s*");

		return null;
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
	 * Gets the type of op, as specified via {@code @Plugin(type = <type>)}).
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
