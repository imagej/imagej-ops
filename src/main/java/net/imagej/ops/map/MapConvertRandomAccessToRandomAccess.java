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

package net.imagej.ops.map;

import net.imagej.ops.ComputerConverter;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.converter.read.ConvertedRandomAccessible;
import net.imglib2.type.Type;

import org.scijava.plugin.Plugin;

/**
 * Maps values of a {@link RandomAccessible} in View.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <A>
 * @param <B>
 */
@Plugin(type = Ops.Map.class)
public class MapConvertRandomAccessToRandomAccess<A, B extends Type<B>> extends
	AbstractMapView<A, B, RandomAccessible<A>, RandomAccessible<B>>
{

	@Override
	public RandomAccessible<B> compute(final RandomAccessible<A> input) {
		final ComputerConverter<A, B> converter =
			new ComputerConverter<A, B>(getOp());
		return new ConvertedRandomAccessible<A, B>(input, converter, getType());
	}

}
