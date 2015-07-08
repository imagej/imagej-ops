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

package net.imagej.ops.filter;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.gauss.DefaultGaussRAI;
import net.imagej.ops.filter.gauss.GaussRAISingleSigma;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * The filter namespace contains ops that filter data.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class FilterNamespace extends AbstractNamespace {

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = Ops.Filter.Gauss.class)
	public Object gauss(final Object... args) {
		return ops().run(Ops.Filter.Gauss.NAME, args);
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double[] sigmas,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(DefaultGaussRAI.class, out, in,
				sigmas, outOfBounds);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<V> out,
			final RandomAccessibleInterval<T> in, final double... sigmas)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(DefaultGaussRAI.class, out, in,
				sigmas);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.DefaultGaussRAI.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<T> in,
			final double... sigmas)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops()
				.run(DefaultGaussRAI.class, in, sigmas);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(RandomAccessibleInterval<V> out,
			RandomAccessibleInterval<T> in, double sigma)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(GaussRAISingleSigma.class, out,
				in, sigma);
		return result;
	}

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(RandomAccessibleInterval<V> out,
			RandomAccessibleInterval<T> in, double sigma,
			OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(GaussRAISingleSigma.class, out,
				in, sigma, outOfBounds);
		return result;
	}

	@OpMethod(op = net.imagej.ops.filter.gauss.GaussRAISingleSigma.class)
	public <T extends RealType<T>, V extends RealType<V>>
		RandomAccessibleInterval<V> gauss(final RandomAccessibleInterval<T> in,
			final double sigma)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<V> result =
			(RandomAccessibleInterval<V>) ops().run(
				net.imagej.ops.filter.gauss.GaussRAISingleSigma.class, in, sigma);
		return result;
	}

	// -- Namespace methods --

	@Override
	public String getName() {
		return "filter";
	}

}
