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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.scijava.ItemIO;
import org.scijava.module.DefaultMutableModuleItem;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.module.MutableModuleItem;
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
	/** The inputs expected by the Op */
	private final List<String> inputNames;
	private final List<Type> inputTypes;
	/** The outputs given by the Op */
	private final Optional<String> returnName;
	private final Optional<Type> returnType;

	/**
	 * Convenience constructor, mainly used by {@link OpInfo}s.
	 *
	 * @param info the {@link OpInfo} to scrape for the listing data.
	 */
	public OpListing(final OpInfo info) {
		this.name = info.getName();
		this.functionalType = info.getType();
		this.inputNames = new ArrayList<>();
		this.inputTypes = new ArrayList<>();
		for (final ModuleItem<?> input : info.inputs()) {
			inputNames.add(input.getName());
			inputTypes.add(input.getGenericType());
		}
		final Optional<ModuleItem<?>> first = info.outputs().stream().filter(
			item -> !item.isInput()).findFirst();
		this.returnName = first.map(ModuleItem::getName);
		this.returnType = first.map(ModuleItem::getGenericType);
	}

	/**
	 * Convenience {@link OpListing} constructor for Ops without returns.
	 *
	 * @param name the name of the Op.
	 * @param functionalType the basic type of the Op (e.g.
	 *          {@link net.imagej.ops.special.function.UnaryFunctionOp})
	 * @param inputNames the input names of the Op
	 * @param inputTypes the input types of the Op
	 */
	public OpListing( //
		final String name, //
		final Class<?> functionalType, //
		final List<String> inputNames, //
		final List<Type> inputTypes)
	{
		this(name, functionalType, inputNames, inputTypes, null, null);
	}

	/**
	 * Standard {@link OpListing} constructor.
	 *
	 * @param name the name of the Op.
	 * @param functionalType the basic type of the Op (e.g.
	 *          {@link net.imagej.ops.special.function.UnaryFunctionOp})
	 * @param inputNames the input names of the Op
	 * @param inputTypes the input types of the Op
	 * @param returnName the return name of the Op
	 * @param returnType the return type of the Op
	 */
	public OpListing( //
		final String name, //
		final Class<?> functionalType, //
		final List<String> inputNames, //
		final List<Type> inputTypes, //
		final String returnName, //
		final Type returnType)
	{
		this.name = name;
		this.functionalType = functionalType;
		this.inputNames = inputNames;
		this.inputTypes = inputTypes;
		this.returnName = Optional.ofNullable(returnName);
		this.returnType = Optional.ofNullable(returnType);
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
		return IntStream.range(0, inputNames.size()) //
			.mapToObj(i -> new DefaultMutableModuleItem<>( //
				info, //
				inputNames.get(i), //
				Types.raw(inputTypes.get(i)))) //
			.collect(Collectors.toList());
	}

	/**
	 * @return the {@link Type}s of each input, in order, required by the listing.
	 */
	public List<Type> getInputTypes() {
		return inputTypes;
	}

	/**
	 * @return the names of each input, in order, required by the listing.
	 */
	public List<String> getInputNames() {
		return inputNames;
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
		if (returnName.isPresent() && returnType.isPresent()) {
			final String name = returnName.get();
			final Class<?> type = Types.raw(returnType.get());
			final MutableModuleItem<?> outItem = //
				new DefaultMutableModuleItem<>(info, name, type);
			outItem.setIOType(ItemIO.OUTPUT);
			return Collections.singletonList(outItem);
		}
		return Collections.emptyList();
	}

	/**
	 * @return the {@link Type}s of each output, in order, required by the
	 *         listing.
	 */
	public Optional<Type> getReturnType() {
		return returnType;
	}

	/**
	 * @return the names of each output, in order, required by the listing.
	 */
	public Optional<String> getReturnName() {
		return returnName;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof OpListing)) return false;
		final OpListing that = (OpListing) obj;

		return Objects.equals(getName(), that.getName()) && //
			Objects.equals(getFunctionalType(), that.getFunctionalType()) && //
			Objects.equals(getInputTypes(), that.getInputTypes()) && //
			Objects.equals(getInputNames(), that.getInputNames()) && //
			Objects.equals(getReturnType(), that.getReturnType()) && //
			Objects.equals(getReturnName(), that.getReturnName());
	}

	@Override
	public int hashCode() {
		return Objects.hash( //
			getName(), //
			getFunctionalType(), //
			getInputTypes(), //
			getInputNames(), //
			getReturnType(), //
			getReturnName() //
		);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		// name
		sb.append(getName());
		// inputs
		sb.append("(").append(paramList(inputNames, inputTypes)).append(")");
		// (pure) output
		if (returnName.isPresent() && returnType.isPresent()) {
			final String returnString = shortParam(returnName.get(), returnType
				.get());
			sb.append(" -> ").append(returnString);
		}
		return sb.toString();
	}

	public OpListing reduce(final Function<Type, Type> typeReducer) {
		final List<Type> newInputTypes = inputTypes.stream().map(typeReducer)
			.collect(Collectors.toList());
		if (returnName.isPresent() && returnType.isPresent()) {
			final Type newReturnType = typeReducer.apply(returnType.get());
			return new OpListing(name, functionalType, inputNames, newInputTypes,
				returnName.get(), newReturnType);
		}
		return new OpListing(name, functionalType, inputNames, newInputTypes);
	}

	// -- Helper methods --

	/**
	 * Creates a {@link String} describing all {@link ModuleItem}s in
	 * {@code items}, along with the introductory {@link String}
	 * {@code beforeFirst} prepended.
	 *
	 * @param names The parameter names to join
	 * @param types The parameter types to join
	 * @return the joining of {@code items}, with {@code beforeFirst} prepended.
	 */
	private String paramList(final List<String> names, final List<Type> types) {
		boolean first = true;
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < names.size(); i++) {
			if (first) {
				first = false;
			}
			else sb.append(", ");
			sb.append(shortParam(names.get(i), types.get(i)));
		}
		return sb.toString();
	}

	/**
	 * @param name the name of the parameter
	 * @param type the {@link Type} of the parameter
	 * @return a description (type + name) of that {@link ModuleInfo}
	 */
	private String shortParam(final String name, final Type type) {
		final String typeString = varFromType(type);
		// We ignore these names because they are uninformative
		return name.matches("(in|out)\\d*") ? //
			typeString : typeString + " \"" + name + "\"";
	}

	/**
	 * Creates a type {@link String} describing the {@link Type} of a passed
	 * {@link ModuleItem}
	 *
	 * @param type the {@link Type} to describe
	 * @return a {@link String} describing {@code item}'s {@link Type}
	 */
	private String varFromType(final Type type) {
		final Class<?> raw = Types.raw(type);
		return lowerCamelCase(raw.getSimpleName());
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
