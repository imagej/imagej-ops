/*
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

package net.imagej.ops.search;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.imagej.ops.OpInfo;
import net.imagej.ops.OpService;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.search.SearchResult;
import org.scijava.search.Searcher;
import org.scijava.util.Types;

/**
 * {@link Searcher} plugin for ImageJ Ops.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Searcher.class, priority = Priority.VERY_HIGH - 1)
public class OpSearcher implements Searcher {

	private Function<Type, Type> paramReducer = (type) -> {
		final Optional<Type> simpleName = SIG_SIMPLIFICATIONS.entrySet() //
			.parallelStream() //
			.filter(e -> Types.isAssignable(type, e.getKey())) //
			.map(Map.Entry::getValue) //
			.findFirst();

		return simpleName.orElseGet(() -> type);
	};

	@Parameter
	private OpService opService;

	@Override
	public String title() {
		return "Ops";
	}

	/**
	 * Map containing all searchable Ops, sorted into bins by Op name.
	 */
	private final Map<String, Set<OpInfo>> opsOfName = new HashMap<>();

	@Override
	public List<SearchResult> search(final String text, final boolean fuzzy) {
		if (text.isEmpty()) return Collections.emptyList();

		// Update nameSets
		updateNameSets();

		// Find all names matching text
		final String filterText = filterString(text);

		return opsOfName.keySet().parallelStream() //
			// Find all names that "match" the search text
			.filter(name -> namePriority(name, filterText) > 0) //
			// grab all OpInfos of each "matched" name
			.flatMap(name -> opsOfName.get(name).stream()) //
			// create an OpListing for each matching OpInfo
			.map(OpInfo::listing) //
			// reduce the listing to basic types, filtering out duplicates
			.map(sig -> sig.reduce(paramReducer)).distinct() //
			// sort listing by how well the name aligns to search text
			.sorted((a, b) -> sortByName(a.getName(), b.getName(), filterText)) //
			// create a SearchResult from each listing
			.map(sig -> new OpSearchResult(opService.context(), sig)) //
			// return a list
			.collect(Collectors.toList());
	}

	public void setParameterReducer(final Function<Type, Type> func) {
		this.paramReducer = func;
	}

	// -- Helper methods --

	private int sortByName(final String name1, final String name2,
		final String filterText)
	{
		final String filterName1 = filterString(name1);
		final String filterName2 = filterString(name2);
		// first, sort by name priority
		final int priorityDiff = namePriority(filterName2, filterText) -
			namePriority(filterName1, filterText);
		if (priorityDiff != 0) return priorityDiff;
		// if they are the same priority, sort in increasing name length
		return name1.length() - name2.length();
	}

	private static final Map<Type, Type> SIG_SIMPLIFICATIONS =
		new HashMap<Type, Type>()
		{

			{
				put(RandomAccessible.class, Img.class);
				put(RealRandomAccessible.class, Img.class);
				put(IterableInterval.class, Img.class);
				put(Number.class, Number.class);
				put(RealType.class, Number.class);
			}
		};

	private void updateNameSets() {
		for (final OpInfo info : opService.infos()) {
			final String name = filterString(info.getName());

			opsOfName.computeIfAbsent(name, (str) -> new HashSet<>());
			opsOfName.get(name).add(info);
		}
	}

	/**
	 * Filters {@code text} for searching. This allows better matching by ignoring
	 * non-alphanumeric characters.
	 *
	 * @param text the {@link String} whose characters should be filterd
	 * @return {@code} text, but filtered!
	 */
	private String filterString(String text) {
		if (text == null) text = "";
		return text.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
	}

	private int namePriority(final String opName, final String searchText) {
		if (opName.equals(searchText)) return 3;
		if (startsWith(opName, searchText)) return 2;
		if (hasSubstring(opName, searchText)) return 1;
		return 0;
	}

	private boolean startsWith(final String name, final String desiredLower) {
		return name != null && //
			name.toLowerCase().matches("(^|.*\\.)" + desiredLower + ".*");
	}

	private boolean hasSubstring(final String name, final String desiredLower) {
		return name != null && name.matches(".*" + desiredLower + ".*");
	}
}
