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

import net.imglib2.Interval;
import net.imglib2.Positionable;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPositionable;
import net.imglib2.outofbounds.OutOfBounds;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.util.Intervals;
import net.imglib2.view.ExtendedRandomAccessibleInterval;

/**
 * Wrapper of a {@link RandomAccessibleInterval} and a
 * {@link OutOfBoundsFactory} that keeps the interval information. All the
 * method except for accessing out of bounds data are handled by the source RAI.
 * <p>
 * Creating an instance of this class could be considered a shorthand for:<br/>
 * <code>Views.interval(Views.extend(source, outOfBoundsFactory), source);</code>
 * </p>
 * 
 * @author Leon Yang
 * @param <T> element type
 * @see ExtendedRandomAccessibleInterval
 */
public class ExtendedRAI<T, F extends RandomAccessibleInterval<T>> implements
	RandomAccessibleInterval<T>
{

	final protected F source;

	final protected OutOfBoundsFactory<T, ? super F> factory;

	public ExtendedRAI(final F source,
		final OutOfBoundsFactory<T, ? super F> factory)
	{
		this.source = source;
		this.factory = factory;
	}

	@Override
	final public OutOfBounds<T> randomAccess() {
		return factory.create(source);
	}

	@Override
	final public RandomAccess<T> randomAccess(final Interval interval) {
		assert source.numDimensions() == interval.numDimensions();

		if (Intervals.contains(source, interval)) {
			return source.randomAccess(interval);
		}
		return randomAccess();
	}

	public F getSource() {
		return source;
	}

	// -- delegated methods --

	@Override
	public long min(int d) {
		return source.min(d);
	}

	@Override
	public void min(long[] min) {
		source.min(min);
	}

	@Override
	public void min(Positionable min) {
		source.min(min);
	}

	@Override
	public long max(int d) {
		return source.max(d);
	}

	@Override
	public void max(long[] max) {
		source.max(max);
	}

	@Override
	public void max(Positionable max) {
		source.max(max);
	}

	@Override
	final public int numDimensions() {
		return source.numDimensions();
	}

	@Override
	public double realMin(int d) {
		return source.realMin(d);
	}

	@Override
	public void realMin(double[] min) {
		source.realMin(min);
	}

	@Override
	public void realMin(RealPositionable min) {
		source.realMin(min);
	}

	@Override
	public double realMax(int d) {
		return source.realMax(d);
	}

	@Override
	public void realMax(double[] max) {
		source.realMax(max);
	}

	@Override
	public void realMax(RealPositionable max) {
		source.realMax(max);
	}

	@Override
	public void dimensions(long[] dimensions) {
		source.dimensions(dimensions);
	}

	@Override
	public long dimension(int d) {
		return source.dimension(d);
	}
}
