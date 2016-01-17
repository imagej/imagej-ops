/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

import org.scijava.InstantiableException;
import org.scijava.object.SortedObjectIndex;

/**
 * Data structure for managing registered ops.
 * 
 * @author Curtis Rueden
 */
public class OpIndex extends SortedObjectIndex<OpInfo> {

	public OpIndex() {
		super(OpInfo.class);
	}

	// -- Internal methods --

	/**
	 * Overrides the type by which the entries are indexed.
	 * <p>
	 * Ops are indexed on their actual concrete types.
	 * </p>
	 */
	@Override
	protected Class<?> getType(final OpInfo info) {
		try {
			return info.cInfo().loadClass();
		}
		catch (final InstantiableException exc) {
			return null;
		}
	}

	/**
	 * Removes the op from all type lists.
	 * <p>
	 * NB: This behavior differs from the default
	 * {@link org.scijava.object.ObjectIndex} behavior in that the {@code info}
	 * object's actual type hierarchy is not used for classification, but rather
	 * the object is classified according to the referenced op's concrete type.
	 * </p>
	 */
	@Override
	protected boolean remove(final Object o, final boolean batch) {
		if (!(o instanceof OpInfo)) return false;
		final OpInfo info = (OpInfo) o;
		return remove(info, getType(info), batch);
	}

}
