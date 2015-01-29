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

import java.util.List;

import net.imagej.ops.OpCandidate.StatusCode;

import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;

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
		final String outputString = paramString(info.outputs(), null).trim();
		if (!outputString.isEmpty()) sb.append("(" + outputString + ") =\n\t");
		sb.append(info.getDelegateClassName());
		sb.append("(" + paramString(info.inputs(), special) + ")");
		return sb.toString();
	}

	public static <OP extends Op> String matchInfo(
		final List<OpCandidate<OP>> candidates, final List<Module> matches)
	{
		final StringBuilder sb = new StringBuilder();

		final OpRef<OP> ref = candidates.get(0).getRef();
		if (matches.isEmpty()) {
			// no matches
			sb.append("No matching '" + ref.getLabel() + "' op\n");
		}
		else {
			// multiple matches
			final double priority = matches.get(0).getInfo().getPriority();
			sb.append("Multiple '" + ref.getLabel() + "' ops of priority " +
				priority + ":\n");
			int count = 0;
			for (final Module module : matches) {
				sb.append(++count + ". ");
				sb.append(opString(module.getInfo()) + "\n");
			}
		}

		// fail, with information about the request and candidates
		sb.append("\n");
		sb.append("Request:\n");
		sb.append("-\t" + opString(ref.getLabel(), ref.getArgs()) + "\n");
		sb.append("\n");
		sb.append("Candidates:\n");
		int count = 0;
		for (final OpCandidate<OP> candidate : candidates) {
			final ModuleInfo info = candidate.getInfo();
			sb.append(++count + ". ");
			sb.append("\t" + opString(info, candidate.getStatusItem()) + "\n");
			final String status = candidate.getStatus();
			if (status != null) sb.append("\t" + status + "\n");
			if (candidate.getStatusCode() == StatusCode.DOES_NOT_CONFORM) {
				// show argument values when a contingent op rejects them
				for (final ModuleItem<?> item : info.inputs()) {
					final Object value = item.getValue(candidate.getModule());
					sb.append("\t\t" + item.getName() + " = " + value + "\n");
				}
			}
		}
		return sb.toString();
	}

	// -- Helper methods --

	/** Helper method of {@link #opString(ModuleInfo, ModuleItem)}. */
	private static String paramString(final Iterable<ModuleItem<?>> items,
		final ModuleItem<?> special)
	{
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final ModuleItem<?> item : items) {
			if (first) first = false;
			else sb.append(",");
			sb.append("\n");
			if (item == special) sb.append("==>"); // highlight special item
			sb.append("\t\t");
			sb.append(item.getType().getSimpleName() + " " + item.getName());
			if (!item.isRequired()) sb.append("?");
		}
		return sb.toString();
	}

	/** Helper method to cast an Object o to a given O. */ 
	@SuppressWarnings("unchecked")
	public static <O> O cast(Object src) {
		return (O) src;
	}
}
