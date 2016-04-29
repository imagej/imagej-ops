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

package net.imagej.ops.fill;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imglib2.Localizable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fill.Filter;
import net.imglib2.algorithm.fill.Writer;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.Type;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

@Plugin(type = Namespace.class)
public class FillNamespace extends AbstractNamespace {

	@OpMethod(op = Ops.Fill.Flood.class)
	public <T extends Type<T>, U extends Type<U>> RandomAccessibleInterval<U>
		flood(final RandomAccessibleInterval<T> in, final Localizable seed,
			final U fillLabel, final Shape shape,
			final Filter<Pair<T, U>, Pair<T, U>> filter)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<U> result =
			(RandomAccessibleInterval<U>) ops().run(Ops.Fill.Flood.class, in, seed,
				fillLabel, shape, filter);
		return result;
	}

	@OpMethod(op = Ops.Fill.Flood.class)
	public <T extends Type<T>, U extends Type<U>> RandomAccessibleInterval<U>
		flood(final RandomAccessibleInterval<U> out,
			final RandomAccessibleInterval<T> in, final Localizable seed,
			final U fillLabel, final Shape shape,
			final Filter<Pair<T, U>, Pair<T, U>> filter)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<U> result =
			(RandomAccessibleInterval<U>) ops().run(Ops.Fill.Flood.class, out, in,
				seed, fillLabel, shape, filter);
		return result;
	}

	@OpMethod(op = Ops.Fill.Flood.class)
	public <T extends Type<T>, U extends Type<U>> RandomAccessibleInterval<U>
		flood(final RandomAccessibleInterval<U> out,
			final RandomAccessibleInterval<T> in, final Localizable seed,
			final T seedLabel, final U fillLabel, final Shape shape,
			final Filter<Pair<T, U>, Pair<T, U>> filter)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<U> result =
			(RandomAccessibleInterval<U>) ops().run(Ops.Fill.Flood.class, out, in,
				seed, seedLabel, fillLabel, shape, filter);
		return result;
	}

	@OpMethod(op = Ops.Fill.Flood.class)
	public <T extends Type<T>, U extends Type<U>> RandomAccessibleInterval<U>
		flood(final RandomAccessibleInterval<U> out,
			final RandomAccessibleInterval<T> in, final Localizable seed,
			final T seedLabel, final U fillLabel, final Shape shape,
			final Filter<Pair<T, U>, Pair<T, U>> filter, final Writer<U> writer)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<U> result =
			(RandomAccessibleInterval<U>) ops().run(Ops.Fill.Flood.class, out, in,
				seed, seedLabel, fillLabel, shape, filter, writer);
		return result;
	}

	@Override
	public String getName() {
		return "fill";
	}

}
