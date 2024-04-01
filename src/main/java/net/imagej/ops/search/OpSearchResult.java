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

package net.imagej.ops.search;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import net.imagej.ops.OpListing;
import net.imagej.ops.OpListingInfo;
import org.scijava.Context;
import org.scijava.cache.CacheService;
import org.scijava.search.SearchResult;
import org.scijava.search.module.ModuleSearcher;

import net.imagej.ops.OpInfo;
import net.imagej.ops.OpService;

/**
 * Search result for the {@link OpSearcher}.
 *
 * @author Curtis Rueden
 * @author Gabriel Selzer
 */
public class OpSearchResult implements SearchResult {

	private final OpListingInfo info;
	private final String shortSig;
	private final Map<String, String> props;

	public OpSearchResult(final Context context, final OpInfo info,
		final String baseDir)
	{
		this(context, new OpListing(info));
		final String[] aliases = info.getAliases();
		if (aliases != null && aliases.length > 0) {
			props.put("Aliases", Arrays.toString(aliases));
		}

		props.put("Identifier", info.cInfo().getIdentifier());
		props.put("Location", ModuleSearcher.location(info.cInfo(), baseDir));
	}

	public OpSearchResult(final Context context, final OpListing signature) {
		CacheService cacheService = context.getService(CacheService.class);
		this.info = new OpListingInfo(context.getService(OpService.class),
			signature);

		final Object shortSigKey = new ShortSigKey(info);
		final Object shortSigValue = cacheService.get(shortSigKey);
		if (shortSigValue == null) {
			shortSig = signature.toString();
			cacheService.put(shortSigKey, shortSig);
		}
		else shortSig = (String) shortSigValue;

		props = new LinkedHashMap<>();

		props.put("Signature", info.toString());

		final String opType = info.getDelegateClassName();
		props.put("Op type", opType.startsWith("net.imagej.ops.Ops$") ? //
			opType.substring(15).replace('$', '.') : opType);
	}

	public OpListingInfo info() {
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
		return ModuleSearcher.iconPath(info);
	}

	@Override
	public Map<String, String> properties() {
		return props;
	}

	// -- Helper classes --

	private static class ShortSigKey {

		private final OpListingInfo opInfo;

		public ShortSigKey(final OpListingInfo info) {
			this.opInfo = info;
		}

		@Override
		public boolean equals(final Object o) {
			if (!(o instanceof ShortSigKey)) return false;
			ShortSigKey that = (ShortSigKey) o;
			return this.opInfo == that.opInfo;
		}

		@Override
		public int hashCode() {
			return opInfo.hashCode();
		}
	}
}
