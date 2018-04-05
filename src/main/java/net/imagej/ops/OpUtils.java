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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.imagej.ops.OpCandidate.StatusCode;

import org.scijava.Context;
import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.SciJavaPlugin;
import org.scijava.service.Service;
import org.scijava.util.GenericUtils;

/**
 * Utility methods for working with ops. In particular, this class contains
 * handy methods for generating human-readable strings describing ops and match
 * requests against them.
 * 
 * @author Curtis Rueden
 */
public final class OpUtils {

	private OpUtils() {
		// NB: prevent instantiation of utility class.
	}

	// -- Utility methods --

	public static Object[] args(final Object[] latter, final Object... former) {
		final Object[] result = new Object[former.length + latter.length];
		int i = 0;
		for (final Object o : former) {
			result[i++] = o;
		}
		for (final Object o : latter) {
			result[i++] = o;
		}
		return result;
	}

	/**
	 * Gets the given {@link ModuleInfo}'s list of inputs, excluding special ones
	 * like {@link Service}s and {@link Context}s.
	 */
	public static List<ModuleItem<?>> inputs(final ModuleInfo info) {
		final List<ModuleItem<?>> inputs = asList(info.inputs());
		return filter(inputs, input -> !isInjectable(input.getType()));
	}

	/** Gets the given {@link ModuleInfo}'s list of outputs. */
	public static List<ModuleItem<?>> outputs(final ModuleInfo info) {
		return asList(info.outputs());
	}

	/** Gets the namespace portion of the given op name. */
	public static String getNamespace(final String opName) {
		if (opName == null) return null;
		final int dot = opName.lastIndexOf(".");
		return dot < 0 ? null : opName.substring(0, dot);
	}

	/** Gets the simple portion (without namespace) of the given op name. */
	public static String stripNamespace(final String opName) {
		if (opName == null) return null;
		final int dot = opName.lastIndexOf(".");
		return dot < 0 ? opName : opName.substring(dot + 1);
	}

	/**
	 * Unwraps the delegate object of the given {@link Module}, ensuring it is an
	 * instance whose type matches the specified {@link OpRef}.
	 * 
	 * @param module The module to unwrap.
	 * @param ref The {@link OpRef} defining the op's type restrictions.
	 * @return The unwrapped {@link Op}.
	 * @throws IllegalStateException if the op does not conform to the expected
	 *           types.
	 */
	public static Op unwrap(final Module module, final OpRef ref) {
		return unwrap(module, ref.getTypes());
	}

	/**
	 * Unwraps the delegate object of the given {@link Module}, ensuring it is an
	 * instance of the specified type(s).
	 * 
	 * @param module The module to unwrap.
	 * @param types Required types for the op.
	 * @return The unwrapped {@link Op}.
	 * @throws IllegalStateException if the op does not conform to the expected
	 *           types.
	 */
	public static Op unwrap(final Module module,
		final Collection<? extends Type> types)
	{
		if (module == null) return null;
		final Object delegate = module.getDelegateObject();
		if (types != null) {
			for (final Type t : types) {
				// FIXME: Use generic isInstance test, once it exists.
				final Class<?> raw = GenericUtils.getClass(t);
				if (!raw.isInstance(delegate)) {
					throw new IllegalStateException(delegate.getClass().getName() +
						" is not of type " + raw.getName());
				}
			}
		}
		if (!(delegate instanceof Op)) {
			throw new IllegalStateException(delegate.getClass().getName() +
				" is not an Op");
		}
		final Op op = (Op) delegate;
		return op;
	}

	/**
	 * Gets a string describing the given op request.
	 * 
	 * @param name The op's name.
	 * @param args The op's input arguments.
	 * @return A string describing the op request.
	 */
	public static String opString(final String name, final Object... args) {
		final StringBuilder sb = new StringBuilder();
		sb.append(name + "(\n\t\t");
		boolean first = true;
		for (final Object arg : args) {
			if (first) first = false;
			else sb.append(",\n\t\t");
			if (arg == null) sb.append("null");
			else if (arg instanceof Class) {
				// NB: Class instance used to mark argument type.
				sb.append(((Class<?>) arg).getSimpleName());
			}
			else sb.append(arg.getClass().getSimpleName());
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Gets a string describing the given op.
	 * 
	 * @param info The {@link ModuleInfo} metadata which describes the op.
	 * @return A string describing the op.
	 */
	public static String opString(final ModuleInfo info) {
		return opString(info, null);
	}

	/**
	 * Gets a string describing the given op, highlighting the specific parameter.
	 * 
	 * @param info The {@link ModuleInfo} metadata which describes the op.
	 * @param special A parameter of particular interest when describing the op.
	 * @return A string describing the op.
	 */
	public static String opString(final ModuleInfo info,
		final ModuleItem<?> special)
	{
		final StringBuilder sb = new StringBuilder();
		final String outputString = paramString(outputs(info), null).trim();
		if (!outputString.isEmpty()) sb.append("(" + outputString + ") =\n\t");
		sb.append(info.getDelegateClassName());
		sb.append("(" + paramString(inputs(info), special) + ")");
		return sb.toString();
	}

	/**
	 * Similar to {@link #opString(ModuleInfo)} but prints a cleaner,
	 * more abstract representation of the Op method call in the format
	 * {@code return <= baseOp(param1, param2)}. Intended to be presented to users
	 * as the limited information reduces utility for debugging.
	 */
	public static String simpleString(final CommandInfo info) {
		final StringBuilder sb = new StringBuilder();
		final String outputString = paramString(outputs(info), null, ", ").trim();
		if (!outputString.isEmpty()) sb.append("" + outputString + "  <=  ");

		final Class<? extends SciJavaPlugin> type = info.getAnnotation().type();
		sb.append(type.getSimpleName());
		sb.append("(" + paramString(inputs(info), null, ", ") + ")");
		return sb.toString().replaceAll("\n|\t", "");
	}

	/**
	 * Returns a method call for the given {@link Op} using its name and the
	 * correct count of parameters. Assumes the op will be called via an
	 * {@link OpService} of the name "ops".
	 */
	public static String opCall(final CommandInfo info) {
		StringBuilder sb = new StringBuilder();

		sb.append("ops.run(");
		try {
			// try using the short name
			final String shortName = getOpName(info);
			sb.append("\"");
			sb.append(shortName);
			sb.append("\"");
		} catch (final Exception e) {
			// Use the class name if any errors pop up
			sb.append(info.getAnnotation().type().getName());
		}

		for (final ModuleItem<?> item : inputs(info)) {
			sb.append(", ");
			sb.append(item.getType().getSimpleName());
		}
		sb.append(")");

		return sb.toString();
	}

	/**
	 * Gets a string with an analysis of a particular match request failure.
	 * <p>
	 * This method is used to generate informative exception messages when no
	 * matches, or too many matches, are found.
	 * </p>
	 * 
	 * @param candidates The list of already-analyzed candidates from which a
	 *          match was desired.
	 * @param matches The list of matching candidates with attached {@link Module}
	 *          instances.
	 * @return A multi-line string describing the situation: 1) the type of match
	 *         failure; 2) the list of matching ops (if any); 3) the request
	 *         itself; and 4) the list of candidates including status (i.e.,
	 *         whether it matched, and if not, why not).
	 * @see OpMatchingService#filterMatches(List)
	 */
	public static String matchInfo(final List<OpCandidate> candidates,
		final List<OpCandidate> matches)
	{
		final StringBuilder sb = new StringBuilder();

		final OpRef ref = candidates.get(0).getRef();
		if (matches.isEmpty()) {
			// no matches
			sb.append("No matching '" + ref.getLabel() + "' op\n");
		}
		else {
			// multiple matches
			final double priority = matches.get(0).cInfo().getPriority();
			sb.append("Multiple '" + ref.getLabel() + "' ops of priority " +
				priority + ":\n");
			int count = 0;
			for (final OpCandidate match : matches) {
				sb.append(++count + ". ");
				sb.append(opString(match.getModule().getInfo()) + "\n");
			}
		}

		// fail, with information about the request and candidates
		sb.append("\n");
		sb.append("Request:\n");
		sb.append("-\t" + opString(ref.getLabel(), ref.getArgs()) + "\n");
		sb.append("\n");
		sb.append("Candidates:\n");
		int count = 0;
		for (final OpCandidate candidate : candidates) {
			final ModuleInfo info = candidate.opInfo().cInfo();
			sb.append(++count + ". ");
			sb.append("\t" + opString(info, candidate.getStatusItem()) + "\n");
			final String status = candidate.getStatus();
			if (status != null) sb.append("\t" + status + "\n");
			if (candidate.getStatusCode() == StatusCode.DOES_NOT_CONFORM) {
				// show argument values when a contingent op rejects them
				for (final ModuleItem<?> item : inputs(info)) {
					final Object value = item.getValue(candidate.getModule());
					sb.append("\t\t" + item.getName() + " = " + value + "\n");
				}
			}
		}
		return sb.toString();
	}

	/** Gets the string name of an op. */
	public static String getOpName(final CommandInfo info) {
		return new OpInfo(info).getName();
	}

	// -- Helper methods --

	/** Converts {@link Iterable} to {@link List}. */
	private static <T> List<T> asList(final Iterable<T> iterable) {
		final ArrayList<T> list = new ArrayList<>();
		iterable.forEach(input -> list.add(input));
		return list;
	}

	/** Filters a list with the given predicate, concealing boilerplate crap. */
	private static <T> List<T> filter(final List<T> list, final Predicate<T> p) {
		return list.stream().filter(p).collect(Collectors.toList());
	}

	// TODO: Move to Context.
	private static boolean isInjectable(final Class<?> type) {
		return Service.class.isAssignableFrom(type) || //
			Context.class.isAssignableFrom(type);
	}

	/**
	 * Helper method of {@link #opString(ModuleInfo, ModuleItem)} which parses a set of items
	 * with a default delimiter of ","
	 */
	private static String paramString(final Iterable<ModuleItem<?>> items,
		final ModuleItem<?> special)
	{
		return paramString(items, special, ",");
	}

	/**
	 * As {@link #paramString(Iterable, ModuleItem)} with an optional delimiter.
	 */
	private static String paramString(final Iterable<ModuleItem<?>> items,
		final ModuleItem<?> special, final String delim) {
		return paramString(items, special, delim, false);
	}

	/**
	 * As {@link #paramString(Iterable, ModuleItem, String)} with a toggle to control
	 * if inputs are types only or include the names.
	 */
	private static String paramString(final Iterable<ModuleItem<?>> items,
		final ModuleItem<?> special, final String delim, final boolean typeOnly)
	{
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final ModuleItem<?> item : items) {
			if (first) first = false;
			else sb.append(delim);
			sb.append("\n");
			if (item == special) sb.append("==>"); // highlight special item
			sb.append("\t\t");
			sb.append(item.getType().getSimpleName());

			if (!typeOnly){
				sb.append(" " + item.getName());
				if (!item.isRequired()) sb.append("?");
			}
		}
		return sb.toString();
	}
}
