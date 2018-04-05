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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Utility class for {@link OpMatchingService}.
 * 
 * @author Leon Yang
 */
public class OpMatchingUtil {

	private OpMatchingUtil() {
		// NB: prevent instantiation of utility class.
	}

	/**
	 * Gets the "useful" class information carries on the given object, which
	 * depends on the actual type of the object.
	 */
	public static Class<?> getClass(final Object obj) {
		if (obj == null) return null;
		if (obj instanceof Class) return (Class<?>) obj;
		if (obj instanceof ParameterizedType)
			return (Class<?>) ((ParameterizedType) obj).getRawType();
		return obj.getClass();
	}

	/**
	 * Finds the levels of casting between <code>origin</code> and
	 * <code>dest</code>. Returns 0 if dest and origin are the same. Returns -1 if
	 * dest is not assignable from origin.
	 */
	public static int findCastLevels(final Class<?> dest, final Class<?> origin) {
		if (dest.equals(origin)) return 0;

		int level = 1;
		Class<?> currType = origin;
		// BFS if dest is an interface
		if (dest.isInterface()) {
			final HashSet<String> seen = new HashSet<>();
			final ArrayList<Type> currIfaces = new ArrayList<>(Arrays.asList(currType
				.getGenericInterfaces()));
			do {
				final ArrayList<Type> nextIfaces = new ArrayList<>();
				for (final Type iface : currIfaces) {
					if (seen.contains(iface.getTypeName())) continue;

					final Class<?> cls = getClass(iface);
					if (cls.equals(dest)) return level;
					seen.add(iface.getTypeName());
					nextIfaces.addAll(Arrays.asList(cls.getGenericInterfaces()));
				}
				currIfaces.clear();
				currIfaces.addAll(nextIfaces);
				if (currType.getSuperclass() != null) {
					currType = currType.getSuperclass();
					currIfaces.addAll(Arrays.asList(currType.getGenericInterfaces()));
				}
				level++;
			}
			while (!currIfaces.isEmpty() || currType.getSuperclass() != null);
		}
		// otherwise dest is a class, so search the list of ancestors
		else {
			while (currType.getSuperclass() != null) {
				currType = currType.getSuperclass();
				if (currType.equals(dest)) return level;
				level++;
			}
		}
		return -1;
	}
}
