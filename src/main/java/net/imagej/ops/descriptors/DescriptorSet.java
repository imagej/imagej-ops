/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.descriptors;

import java.awt.Polygon;
import java.util.Iterator;

import net.imagej.ops.Op;
import net.imglib2.IterableInterval;
import net.imglib2.Pair;

/**
 * Set of {@link Op}s to numerically describe certain objects. A
 * {@link DescriptorSet} can be compiled for several inputs. For example
 * geometric descriptors may be calculated on {@link Polygon}s or on
 * {@link IterableInterval}s. If you compile the {@link DescriptorSet} for
 * {@link IterableInterval}s the descriptors will be selected accordingly.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 */
public interface DescriptorSet extends Iterable<Pair<String, Double>> {

	/**
	 * Compiles the {@link DescriptorSet} for a given input type. If this
	 * {@link DescriptorSet} can't be compiled for a certain input type, an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param inputType
	 *            type of the input for which the {@link DescriptorSet} will be
	 *            compiled.
	 * 
	 */
	public void compileFor(final Class<?> inputType)
			throws IllegalArgumentException;

	/**
	 * Update the {@link DescriptorSet}. For example, an object may be an
	 * {@link Iterable} or {@link DescriptorParameters}
	 * 
	 * @param object
	 *            used to update the {@link DescriptorSet}
	 * 
	 */
	public void update(Object object);

	/**
	 * Iterator over a descriptor set. Descriptors are of type double, their
	 * names are given as strings. Please note, that the {@link DescriptorSet}
	 * needs to be compiled for a given input type before you can iterate over
	 * the individual descriptor values or update the set.
	 * 
	 * @return {@link Iterator} over {@link Pair}s of descriptor names (string)
	 *         and values (double)
	 */
	@Override
	public Iterator<Pair<String, Double>> iterator();
}
