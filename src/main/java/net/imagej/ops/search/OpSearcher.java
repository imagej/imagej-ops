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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import net.imagej.ops.OpInfo;
import net.imagej.ops.OpService;

import org.scijava.Priority;
import org.scijava.app.AppService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.search.SearchResult;
import org.scijava.search.Searcher;

/**
 * {@link Searcher} plugin for ImageJ Ops.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Searcher.class, priority = Priority.VERY_HIGH - 1)
public class OpSearcher implements Searcher {

	@Parameter
	private OpService opService;

	@Parameter
	private AppService appService;

	@Override
	public String title() {
		return "Ops";
	}

	@Override
	public List<SearchResult> search(final String text, final boolean fuzzy) {
		if (text.isEmpty()) return Collections.emptyList();

		final String baseDir = //
			appService.getApp().getBaseDirectory().getAbsolutePath();

		final LinkedHashSet<OpInfo> matches = new LinkedHashSet<>();

		// Get the list of all ops for consideration.
		final List<OpInfo> ops = opService.infos().stream() //
			.collect(Collectors.toList());

		final String textLower = text.toLowerCase();

		// First, add ops where name starts with the text.
		ops.stream() //
			.filter(info -> startsWith(info, textLower)) //
			.forEach(matches::add);

		// Next, add ops where op string has text inside somewhere.
		ops.stream() //
			.filter(info -> hasSubstring(info, textLower)) //
			.forEach(matches::add);

		// Wrap each matching OpInfo in an OpSearchResult.
		return matches.stream() //
			.map(info -> new OpSearchResult(opService.context(), info, baseDir)) //
			.collect(Collectors.toList());
	}

	// -- Helper methods --

	private boolean startsWith(final OpInfo info, final String desiredLower) {
		final String name = info.getName();
		return name != null && //
			name.toLowerCase().matches("(^|.*\\.)" + desiredLower + ".*");
	}

	private boolean hasSubstring(final OpInfo info, final String desiredLower) {
		final String sig = info.toString();
		return sig != null && sig.matches(".*" + desiredLower + ".*");
	}
}
