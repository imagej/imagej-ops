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

package net.imagej.ops.stats;

import java.util.List;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Ops;
import net.imagej.ops.StatsOps;
import net.imagej.ops.misc.Size;
import net.imagej.ops.stats.moment1AboutMean.Moment2AboutMean;
import net.imagej.ops.stats.sum.Sum;
import net.imagej.ops.stats.variance.Variance;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * The stats namespace contains operations related to numerical statistics.
 *
 * @author Curtis Rueden
 */
public class StatsNamespace extends AbstractNamespace {

	// -- max --

	public Object max(final Object... args) {
		return ops().run(StatsOps.Max.NAME, args);
	}

	public <T extends RealType<T>> T max(final T out, final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.stats.max.MaxRealType.class, out, in);
		return result;
	}

	// -- mean --

	public Object mean(final Object... args) {
		return ops().run(StatsOps.Mean.NAME, args);
	}

	public <I extends RealType<I>, O extends RealType<O>> O mean(final O out,
		final Iterable<I> in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.stats.mean.MeanRealType.class, out, in);
		return result;
	}

	public <I extends RealType<I>, O extends RealType<O>> O mean(final O out,
		final Iterable<I> in, final Sum<Iterable<I>, O> sumFunc)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.stats.mean.MeanRealType.class, out, in, sumFunc);
		return result;
	}

	public <I extends RealType<I>, O extends RealType<O>> O mean(final O out,
		final Iterable<I> in, final Sum<Iterable<I>, O> sumFunc,
		final Size<Iterable<I>> sizeFunc)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.stats.mean.MeanRealType.class, out, in, sumFunc,
				sizeFunc);
		return result;
	}

	// -- median --

	public Object median(final Object... args) {
		return ops().run(StatsOps.Median.NAME, args);
	}

	public <T extends RealType<T>> T median(final T out, final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.stats.median.MedianRealType.class, out, in);
		return result;
	}

	// -- min --

	public Object min(final Object... args) {
		return ops().run(StatsOps.Min.NAME, args);
	}

	public <T extends RealType<T>> T min(final T out, final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.stats.min.MinRealType.class, out, in);
		return result;
	}

	// -- minMax --

	public Object minMax(final Object... args) {
		return ops().run(StatsOps.MinMax.NAME, args);
	}

	public <T extends RealType<T>> List<T> minMax(final Iterable<T> img) {
		@SuppressWarnings("unchecked")
		final List<T> result =
			(List<T>) ops().run(net.imagej.ops.misc.MinMaxRealType.class, img);
		return result;
	}

	// -- quantile --

	public Object quantile(final Object... args) {
		return ops().run(StatsOps.Quantile.NAME, args);
	}

	// -- size --

	public Object size(final Object... args) {
		return ops().run(StatsOps.Size.NAME, args);
	}

	public LongType size(final LongType out, final IterableInterval<?> in) {
		final LongType result =
			(LongType) ops().run(net.imagej.ops.misc.SizeIterableInterval.class, out, in);
		return result;
	}

	public LongType size(final LongType out, final Iterable<?> in) {
		final LongType result =
			(LongType) ops().run(net.imagej.ops.misc.SizeIterable.class, out, in);
		return result;
	}

	// -- stdDev --

	public Object stdDev(final Object... args) {
		return ops().run(StatsOps.StdDeviation.NAME, args);
	}

	public <T extends RealType<T>> T stdDev(final T out, final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.stats.stdDev.StdDevRealTypeDirect.class, out, in);
		return result;
	}

	public <T extends RealType<T>> DoubleType stdDev(final DoubleType out,
		final Iterable<T> in)
	{
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.stats.stdDev.StdDevRealType.class, out, in);
		return result;
	}

	public <T extends RealType<T>> DoubleType stdDev(final DoubleType out,
		final Iterable<T> in, final Variance<T, DoubleType> variance)
	{
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.stats.stdDev.StdDevRealType.class, out, in,
				variance);
		return result;
	}

	// -- sum --

	public Object sum(final Object... args) {
		return ops().run(StatsOps.Sum.NAME, args);
	}

	public <T extends RealType<T>, V extends RealType<V>> V sum(final V out,
		final Iterable<T> in)
	{
		@SuppressWarnings("unchecked")
		final V result =
			(V) ops().run(net.imagej.ops.stats.sum.SumRealType.class, out, in);
		return result;
	}

	// -- variance --

	public Object variance(final Object... args) {
		return ops().run(StatsOps.Variance.NAME, args);
	}

	public <T extends RealType<T>> DoubleType variance(final DoubleType out,
		final Iterable<T> in)
	{
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.stats.variance.VarianceRealTypeDirect.class,
				out, in);
		return result;
	}

	public <T extends RealType<T>> DoubleType variance(final DoubleType out,
		final Iterable<T> in, final Moment2AboutMean<T> moment2)
	{
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.stats.variance.VarianceRealType.class, out,
				in, moment2);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "stats";
	}

}
