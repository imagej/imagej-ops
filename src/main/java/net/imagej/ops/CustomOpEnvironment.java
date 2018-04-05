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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.scijava.AbstractContextual;
import org.scijava.Context;

/**
 * A customized op execution environment.
 *
 * @author Curtis Rueden
 */
public class CustomOpEnvironment extends AbstractContextual implements
	OpEnvironment
{

	// -- Fields --

	private final OpEnvironment parent;
	private final OpIndex index;

	// -- Constructors --

	/** Creates an empty op context. */
	public CustomOpEnvironment(final Context context) {
		this(context, null, null);
	}

	/**
	 * Creates an op context with the same configuration as the given parent
	 * environment.
	 */
	public CustomOpEnvironment(final OpEnvironment parent) {
		this(parent, null);
	}

	/**
	 * Creates an op context with the same configuration as the given parent
	 * environment, plus the specified additional ops.
	 */
	public CustomOpEnvironment(final OpEnvironment parent,
		final Collection<? extends OpInfo> infos)
	{
		this(parent.getContext(), parent, infos);
	}

	private CustomOpEnvironment(final Context context,
		final OpEnvironment parent, final Collection<? extends OpInfo> infos)
	{
		setContext(context);
		this.parent = parent;
		index = new OpIndex();
		// NB: If this is not performant and/or dynamic enough, we could create/use
		// a concatenating collection (see e.g. Guava's Iterables.concat method)
		// that does not copy all the elements.
		if (parent != null) index.addOps(parent.infos());
		index.addOps(infos);
	}

	// -- OpEnvironment methods --

	@Override
	public OpMatchingService matcher() {
		return parent().matcher();
	}

	@Override
	public OpInfo info(final Class<? extends Op> type) {
		return index.get(type.getName());
	}

	@Override
	public Collection<OpInfo> infos() {
		return index.values();
	}

	@Override
	public OpEnvironment parent() {
		return parent;
	}

	@Override
	public <NS extends Namespace> NS namespace(Class<NS> nsClass) {
		return parent().namespace(nsClass);
	}

	// -- Helper classes --

	/** A table mapping available ops from class name to {@link OpInfo}. */
	private static class OpIndex extends HashMap<String, OpInfo> {

		public void addOps(final Collection<? extends OpInfo> infos) {
			if (infos == null) return;
			for (final OpInfo info : infos) {
				put(info.cInfo().getDelegateClassName(), info);
			}
		}
		
		@Override
		public Collection<OpInfo> values() {
			final ArrayList<OpInfo> infos = new ArrayList<>();
			infos.addAll(super.values());
			Collections.sort(infos);
			return infos;
		}

	}

}
