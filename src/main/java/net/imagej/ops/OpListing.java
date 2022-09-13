/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.scijava.ItemIO;
import org.scijava.module.DefaultMutableModuleItem;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.util.Types;

/**
 * A data structure designed to capture an abstract algorithm. As far as ImageJ
 * Ops is concerned, Ops that have the same {@link OpListing} are <em>different
 * implementations of the same algorithm</em>, much as method overloading in
 * Java might be considered different implementations of the same functionality.
 * <p>
 * If you have <b>concrete Objects</b> instead of <b>types</b>, use
 * {@link OpRef}.
 *
 * @author Gabriel Selzer
 * @see OpRef
 * @see OpInfo
 */
public class OpListing {

	/** The name of the Op */
	private final String name;
	/** The fundamental type of the Op */
	private final Class<?> functionalType;
	/** The parameters expected by the Op */
	private final List<Parameter> params;

	/**
	 * Constructor used to generate an {@link OpListing} from an {@link OpInfo}s.
	 *
	 * @param info the {@link OpInfo} to scrape for the listing data.
	 */
	public OpListing(final OpInfo info) {
		this.name = info.getName();
		this.functionalType = info.getType();
		this.params = new ArrayList<>();
		// Add all input parameters
		info.inputs().forEach(item -> params.add(new Parameter(item)));
		info.outputs().stream().filter(item -> !item.isInput()).forEach(
			item -> params.add(new Parameter(item)));
	}

	private OpListing(final String name, final Class<?> functionalType,
		final List<Parameter> params)
	{
		this.name = name;
		this.functionalType = functionalType;
		this.params = params;
	}

	/**
	 * Gets the name of the Op
	 *
	 * @return the name of the Op
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the functional type of the Op
	 *
	 * @return the functional type of the Op
	 */
	public Class<?> getFunctionalType() {
		return functionalType;
	}

	/**
	 * Creates a set of {@link ModuleItem}s for <b>each</b> input described by
	 * this {@link OpListing}.
	 *
	 * @param info the {@link ModuleInfo} to which the resulting
	 *          {@link ModuleItem}s will belong.
	 * @return {@link ModuleItem}s describing the inputs of this {@link OpListing}
	 */
	public List<ModuleItem<?>> inputsFor(final ModuleInfo info) {
		return params.stream().filter(Parameter::isInput)//
			.map(param -> param.toModuleItem(info)) //
			.collect(Collectors.toList());
	}

	/**
	 * @return the {@link Type}s of each input, in order, required by the listing.
	 */
	public List<Type> getInputTypes() {
		return params.stream().filter(Parameter::isInput) //
			.map(p -> p.type).collect(Collectors.toList());
	}

	/**
	 * @return the names of each input, in order, required by the listing.
	 */
	public List<String> getInputNames() {
		return params.stream().filter(Parameter::isInput) //
			.map(p -> p.name).collect(Collectors.toList());
	}

	/**
	 * Creates a set of {@link ModuleItem}s for <b>each</b> output described by
	 * this {@link OpListing}.
	 *
	 * @param info the {@link ModuleInfo} to which the resulting
	 *          {@link ModuleItem}s will belong.
	 * @return {@link ModuleItem}s describing the outputs of this
	 *         {@link OpListing}
	 */
	public List<ModuleItem<?>> outputsFor(final ModuleInfo info) {
		return params.stream().filter(Parameter::isOutput) //
			.map(p -> p.toModuleItem(info)).collect(Collectors.toList());
	}

	/**
	 * @return the {@link Type}s of each output, in order, required by the
	 *         listing.
	 */
	public List<Type> getReturnTypes() {
		return params.stream().filter(Parameter::isOutput).map(Parameter::type)
			.collect(Collectors.toList());
	}

	/**
	 * @return the names of each output, in order, required by the listing.
	 */
	public List<String> getReturnNames() {
		return params.stream().filter(Parameter::isOutput).map(Parameter::name)
			.collect(Collectors.toList());
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof OpListing)) return false;
		final OpListing that = (OpListing) obj;

		return Objects.equals(getName(), that.getName()) && //
			Objects.equals(getFunctionalType(), that.getFunctionalType()) && //
			params.equals(that.params);
	}

	@Override
	public int hashCode() {
		return Objects.hash( //
			getName(), //
			getFunctionalType(), //
			params //
		);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final List<Parameter> inputs = params.stream().filter(Parameter::isInput)
			.collect(Collectors.toList());
		final List<Parameter> outputs = params.stream().filter(Parameter::isOutput)
			.collect(Collectors.toList());
		// name
		sb.append(getName());
		// inputs
		sb.append(paramList(inputs));
		// outputs
		if (outputs.size() > 0) {
			sb.append(" -> ").append(paramList(outputs));
		}
		return sb.toString();
	}

	public OpListing reduce(final Function<Type, Type> typeReducer) {
		List<Parameter> reducedParams = params.stream().map(p -> p.reduce(
			typeReducer)).collect(Collectors.toList());
		return new OpListing(name, functionalType, reducedParams);
	}

	// -- Helper methods --

	/**
	 * Creates a {@link String} describing all {@link ModuleItem}s in
	 * {@code items}, along with the introductory {@link String}
	 * {@code beforeFirst} prepended.
	 *
	 * @param params The {@link Parameter}s to join
	 * @return the joining of {@code items}, with {@code beforeFirst} prepended.
	 */
	private String paramList(final List<Parameter> params) {
		boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (Parameter param : params) {
			if (first) {
				first = false;
			}
			else sb.append(", ");
			sb.append(param);
		}
		sb.append(")");
		return sb.toString();
	}

	private static class Parameter {

		private final String name;
		private final Type type;
		private final ItemIO ioType;
		private final boolean isRequired;

		public Parameter(final ModuleItem<?> item) {
			name = item.getName();
			type = item.getGenericType();
			ioType = item.getIOType();
			isRequired = item.isRequired();
		}

		public Parameter(final String name, final Type type, final ItemIO ioType,
			final boolean isRequired)
		{
			this.name = name;
			this.type = type;
			this.ioType = ioType;
			this.isRequired = isRequired;
		}

		public String name() {
			return name;
		}

		public Type type() {
			return type;
		}

		public boolean isRequired() {
			return isRequired;
		}

		public boolean isInput() {
			return ioType == ItemIO.INPUT || ioType == ItemIO.BOTH;
		}

		public boolean isOutput() {
			return ioType == ItemIO.OUTPUT || ioType == ItemIO.BOTH;
		}

		public ModuleItem<?> toModuleItem(ModuleInfo info) {
			DefaultMutableModuleItem<?> item = //
				new DefaultMutableModuleItem<>(info, name, Types.raw(type));
			item.setIOType(ioType);
			item.setRequired(isRequired);
			return item;
		}

		@Override
		public boolean equals(Object that) {
			if (!(that instanceof Parameter)) return false;
			Parameter thatParam = (Parameter) that;
			return name.equals(thatParam.name) && //
				type.equals(thatParam.type) && //
				ioType.equals(thatParam.ioType) && //
				isRequired == thatParam.isRequired;
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(new Object[] { name, type, ioType, isRequired });
		}

		public Parameter reduce(Function<Type, Type> typeReducer) {
			Type reduced = typeReducer.apply(type);
			// reuse this Parameter unless the types are different
			return (type == reduced) ? //
				this : //
				new Parameter(name, typeReducer.apply(type), ioType, isRequired);
		}

		@Override
		public String toString() {
			final Class<?> raw = Types.raw(type);
			final String typeString = lowerCamelCase(raw.getSimpleName());
			final String optionalString = isRequired ? "" : "?";
			return typeString + " \"" + name + "\"" + optionalString;
		}

		/**
		 * Converts a {@link String} from upper- to lower-camel-case.
		 *
		 * @param s the upper-camel-case {@link String}
		 * @return a lower-camel-case version of {@code s}
		 */
		private String lowerCamelCase(final String s) {
			if (s == null || s.isEmpty() || !isUpperCase(s.charAt(0))) return s;
			if (s.length() > 1 && isUpperCase(s.charAt(1))) {
				// RGBColor -> rgbColor
				int index = 1;
				while (index < s.length() && isUpperCase(s.charAt(index)))
					index++;
				return s.substring(0, index - 1).toLowerCase() + s.substring(index - 1);
			}
			// FooBar -> fooBar
			return s.substring(0, 1).toLowerCase() + s.substring(1);
		}

		private boolean isUpperCase(final char c) {
			return c >= 'A' && c <= 'Z';
		}
	}

}
