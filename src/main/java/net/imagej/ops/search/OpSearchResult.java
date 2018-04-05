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

package net.imagej.ops.search;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.OpInfo;
import net.imglib2.RandomAccessible;
import net.imglib2.RealRandomAccessible;

import org.scijava.Context;
import org.scijava.cache.CacheService;
import org.scijava.convert.ConvertService;
import org.scijava.module.ModuleItem;
import org.scijava.search.SearchResult;
import org.scijava.search.module.ModuleSearcher;

/**
 * Search result for the {@link OpSearcher}.
 *
 * @author Curtis Rueden
 */
public class OpSearchResult implements SearchResult {

	private static final List<Class<?>> KNOWN_IMAGE_TYPES = Arrays.asList(
		RandomAccessible.class, //
		RealRandomAccessible.class, //
		Iterable.class);

	private final CacheService cacheService;
	private final ConvertService convertService;
	private final OpInfo info;
	private final String shortSig;
	private final Map<String, String> props;

	public OpSearchResult(final Context context, final OpInfo info,
		final String baseDir)
	{
		cacheService = context.getService(CacheService.class);
		convertService = context.getService(ConvertService.class);
		this.info = info;

		final Object shortSigKey = new ShortSigKey(info);
		final Object shortSigValue = cacheService.get(shortSigKey);
		if (shortSigValue == null) {
			shortSig = buildShortSig();
			cacheService.put(shortSigKey, shortSig);
		}
		else shortSig = (String) shortSigValue;

		props = new LinkedHashMap<>();

		props.put("Signature", info.toString());

		final String opType = info.getType().getName();
		props.put("Op type", opType.startsWith("net.imagej.ops.Ops$") ? //
			opType.substring(15).replace('$', '.') : opType);

		final String[] aliases = info.getAliases();
		if (aliases != null && aliases.length > 0) {
			props.put("Aliases", Arrays.toString(aliases));
		}

		props.put("Identifier", info.cInfo().getIdentifier());
		props.put("Location", ModuleSearcher.location(info.cInfo(), baseDir));
	}

	public OpInfo info() {
		return info;
	}

	@Override
	public String name() {
		return shortSig;
	}

	@Override
	public String identifier() {
		return shortSig;
	}

	@Override
	public String iconPath() {
		return ModuleSearcher.iconPath(info.cInfo());
	}

	@Override
	public Map<String, String> properties() {
		return props;
	}

	// -- Helper methods --

	private String buildShortSig() {
		final StringBuilder sb = new StringBuilder();
		sb.append(info.getName());
		sb.append("(");
		boolean first = true;
		for (final ModuleItem<?> input : info.inputs()) {
			if (first) first = false;
			else sb.append(", ");
			final String name = input.getName();
			final String var = name.matches("(in|out)\\d*") ? //
				varFromType(name.replaceFirst("(in|out)", ""), input.getType()) : name;
				sb.append(var);
		}
		sb.append(")");
		return sb.toString();
	}

	private String varFromType(final String suffix, final Class<?> type) {
		if (typesMatch(KNOWN_IMAGE_TYPES, type)) return "image" + suffix;
		return lowerCamelCase(type.getSimpleName()) + suffix;
	}

	private boolean typesMatch(final List<Class<?>> types, final Class<?> type) {
		return types.stream() //
			.filter(destType -> convertService.supports(type, destType)) //
			.findFirst() //
			.isPresent();
	}

	private String lowerCamelCase(final String s) {
		if (s == null || s.isEmpty() || !isUpperCase(s.charAt(0))) return s;
		if (s.length() > 1 && isUpperCase(s.charAt(1))) {
			// RGBColor -> rgbColor
			int index = 1;
			while (index < s.length() && isUpperCase(s.charAt(index))) index++;
			return s.substring(0, index - 1).toLowerCase() + s.substring(index - 1);
		}
		// FooBar -> fooBar
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	private boolean isUpperCase(char c) {
		return c >= 'A' && c <= 'Z';
	}

	// -- Helper classes --

	private class ShortSigKey {

		private final OpInfo opInfo;

		public ShortSigKey(final OpInfo info) {
			this.opInfo = info;
		}

		@Override
		public boolean equals(final Object o) {
			if (o == null || !(o instanceof ShortSigKey)) return false;
			ShortSigKey that = (ShortSigKey) o;
			return this.opInfo == that.opInfo;
		}

		@Override
		public int hashCode() {
			return opInfo.hashCode();
		}
	}
}
