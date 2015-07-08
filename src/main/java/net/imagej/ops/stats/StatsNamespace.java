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
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imagej.ops.stats.mean.Mean;
import net.imagej.ops.stats.moment1AboutMean.Moment2AboutMean;
import net.imagej.ops.stats.size.Size;
import net.imagej.ops.stats.sum.Sum;
import net.imagej.ops.stats.variance.Variance;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * The stats namespace contains operations related to numerical statistics.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class StatsNamespace extends AbstractNamespace {

	// -- max --

	@OpMethod(op = net.imagej.ops.Ops.Stats.Max.class)
	public Object max(final Object... args) {
		return ops().run(Ops.Stats.Max.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.stats.max.MaxRealType.class)
	public <T extends RealType<T>> T max(final T out, final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.stats.max.MaxRealType.class, out, in);
		return result;
	}

	// -- mean --

	@OpMethod(op = net.imagej.ops.Ops.Stats.Mean.class)
	public Object mean(final Object... args) {
		return ops().run(Ops.Stats.Mean.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.stats.mean.MeanRealType.class)
	public <I extends RealType<I>, O extends RealType<O>> O mean(final O out,
		final Iterable<I> in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.stats.mean.MeanRealType.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.mean.MeanRealType.class)
	public <I extends RealType<I>, O extends RealType<O>> O mean(final O out,
		final Iterable<I> in, final Sum<Iterable<I>, O> sumFunc)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.stats.mean.MeanRealType.class, out, in, sumFunc);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.mean.MeanRealType.class)
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

	@OpMethod(op = net.imagej.ops.Ops.Stats.Median.class)
	public Object median(final Object... args) {
		return ops().run(Ops.Stats.Median.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.stats.median.MedianRealType.class)
	public <T extends RealType<T>> T median(final T out, final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.stats.median.MedianRealType.class, out, in);
		return result;
	}

	// -- min --

	@OpMethod(op = net.imagej.ops.Ops.Stats.Min.class)
	public Object min(final Object... args) {
		return ops().run(Ops.Stats.Min.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.stats.min.MinRealType.class)
	public <T extends RealType<T>> T min(final T out, final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.stats.min.MinRealType.class, out, in);
		return result;
	}

	// -- minMax --

	@OpMethod(op = net.imagej.ops.Ops.Stats.MinMax.class)
	public Object minMax(final Object... args) {
		return ops().run(Ops.Stats.MinMax.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.stats.minMax.MinMaxRealType.class)
	public <T extends RealType<T>> List<T> minMax(final Iterable<T> img) {
		@SuppressWarnings("unchecked")
		final List<T> result =
			(List<T>) ops().run(net.imagej.ops.stats.minMax.MinMaxRealType.class, img);
		return result;
	}

	// -- moment1AboutMean --

	@OpMethod(op = net.imagej.ops.stats.moment1AboutMean.Moment1AboutMean.class)
	public <T extends RealType<T>> DoubleType moment1AboutMean(
		final DoubleType out, final Iterable<T> in)
	{
		final DoubleType result =
			(DoubleType) ops().run(
				net.imagej.ops.stats.moment1AboutMean.Moment1AboutMean.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.moment1AboutMean.Moment1AboutMean.class)
	public <T extends RealType<T>> DoubleType moment1AboutMean(
		final DoubleType out, final Iterable<T> in,
		final Mean<Iterable<T>, DoubleType> mean)
	{
		final DoubleType result =
			(DoubleType) ops().run(
				net.imagej.ops.stats.moment1AboutMean.Moment1AboutMean.class, out, in,
				mean);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.stats.moment1AboutMean.Moment1AboutMean.class,
		net.imagej.ops.stats.moment1AboutMean.Moment2AboutMean.class })
	public <T extends RealType<T>> DoubleType moment1AboutMean(
		final DoubleType out, final Iterable<T> in,
		final Mean<Iterable<T>, DoubleType> mean, final Size<Iterable<T>> size)
	{
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.Ops.Stats.Moment1AboutMean.class,
				out, in, mean, size);
		return result;
	}

	// -- quantile --

	@OpMethod(op = net.imagej.ops.Ops.Stats.Quantile.class)
	public Object quantile(final Object... args) {
		return ops().run(Ops.Stats.Quantile.NAME, args);
	}

	// -- size --

	@OpMethod(op = net.imagej.ops.Ops.Stats.Size.class)
	public Object size(final Object... args) {
		return ops().run(Ops.Stats.Size.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.stats.size.SizeIterableInterval.class)
	public LongType size(final LongType out, final IterableInterval<?> in) {
		final LongType result =
			(LongType) ops().run(net.imagej.ops.stats.size.SizeIterableInterval.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.size.SizeIterable.class)
	public LongType size(final LongType out, final Iterable<?> in) {
		final LongType result =
			(LongType) ops().run(net.imagej.ops.stats.size.SizeIterable.class, out, in);
		return result;
	}

	// -- stdDev --

	@OpMethod(op = net.imagej.ops.Ops.Stats.StdDeviation.class)
	public Object stdDev(final Object... args) {
		return ops().run(Ops.Stats.StdDeviation.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.stats.stdDev.StdDevRealTypeDirect.class)
	public <T extends RealType<T>> T stdDev(final T out, final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.stats.stdDev.StdDevRealTypeDirect.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.stdDev.StdDevRealType.class)
	public <T extends RealType<T>> DoubleType stdDev(final DoubleType out,
		final Iterable<T> in)
	{
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.stats.stdDev.StdDevRealType.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.stdDev.StdDevRealType.class)
	public <T extends RealType<T>> DoubleType stdDev(final DoubleType out,
		final Iterable<T> in, final Variance<T, DoubleType> variance)
	{
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.stats.stdDev.StdDevRealType.class, out, in,
				variance);
		return result;
	}

	// -- sum --

	@OpMethod(op = net.imagej.ops.Ops.Stats.Sum.class)
	public Object sum(final Object... args) {
		return ops().run(Ops.Stats.Sum.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.stats.sum.SumRealType.class)
	public <T extends RealType<T>, V extends RealType<V>> V sum(final V out,
		final Iterable<T> in)
	{
		@SuppressWarnings("unchecked")
		final V result =
			(V) ops().run(net.imagej.ops.stats.sum.SumRealType.class, out, in);
		return result;
	}

	// -- variance --

	@OpMethod(op = net.imagej.ops.Ops.Stats.Variance.class)
	public Object variance(final Object... args) {
		return ops().run(Ops.Stats.Variance.NAME, args);
	}

	@OpMethod(ops = { net.imagej.ops.stats.variance.VarianceRealType.class,
		net.imagej.ops.stats.variance.VarianceRealTypeDirect.class })
	public <T extends RealType<T>> DoubleType variance(final DoubleType out,
		final Iterable<T> in)
	{
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.Ops.Stats.Variance.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.variance.VarianceRealType.class)
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
