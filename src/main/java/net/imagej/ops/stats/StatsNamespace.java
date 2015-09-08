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

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

/**
 * The stats namespace contains operations related to numerical statistics.
 *
 * @author Curtis Rueden
 * @author Daniel Seebacher, University of Konstanz.
 * @author Christian Dietz, University of Konstanz.
 */
@SuppressWarnings("unchecked")
@Plugin(type = Namespace.class)
public class StatsNamespace extends AbstractNamespace {

	@OpMethod(ops = { net.imagej.ops.stats.IterableGeometricMean.class,
		net.imagej.ops.stats.DefaultGeometricMean.class })
	public <T extends RealType<T>, O extends RealType<O>> O geometricMean(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.Stats.GeometricMean.class, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.IterableGeometricMean.class,
		net.imagej.ops.stats.DefaultGeometricMean.class })
	public <T extends RealType<T>, O extends RealType<O>> O geometricMean(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.Stats.GeometricMean.class, out, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.IterableHarmonicMean.class,
		net.imagej.ops.stats.DefaultHarmonicMean.class })
	public <T extends RealType<T>, O extends RealType<O>> O harmonicMean(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.Stats.HarmonicMean.class, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.IterableHarmonicMean.class,
		net.imagej.ops.stats.DefaultHarmonicMean.class })
	public <T extends RealType<T>, O extends RealType<O>> O harmonicMean(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.Stats.HarmonicMean.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultKurtosis.class)
	public <T extends RealType<T>, O extends RealType<O>> O kurtosis(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultKurtosis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultKurtosis.class)
	public <T extends RealType<T>, O extends RealType<O>> O kurtosis(final O out,
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultKurtosis.class, out, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.DefaultMax.class,
		net.imagej.ops.stats.IterableMax.class })
	public <T extends RealType<T>, O extends RealType<O>> O max(
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.Ops.Stats.Max.class, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.DefaultMax.class,
		net.imagej.ops.stats.IterableMax.class })
	public <T extends RealType<T>, O extends RealType<O>> O max(final O out,
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.Ops.Stats.Max.class, out, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.IterableMean.class,
		net.imagej.ops.stats.DefaultMean.class })
	public <T extends RealType<T>, O extends RealType<O>> O mean(
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.Ops.Stats.Mean.class, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.IterableMean.class,
		net.imagej.ops.stats.DefaultMean.class })
	public <T extends RealType<T>, O extends RealType<O>> O mean(final O out,
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.Stats.Mean.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMedian.class)
	public <T extends RealType<T>, O extends RealType<O>> O median(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultMedian.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMedian.class)
	public <T extends RealType<T>, O extends RealType<O>> O median(final O out,
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultMedian.class, out, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.DefaultMin.class,
		net.imagej.ops.stats.IterableMin.class })
	public <T extends RealType<T>, O extends RealType<O>> O min(
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.Ops.Stats.Min.class, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.DefaultMin.class,
		net.imagej.ops.stats.IterableMin.class })
	public <T extends RealType<T>, O extends RealType<O>> O min(final O out,
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.Ops.Stats.Min.class, out, in);

		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMinMax.class)
	public <T extends RealType<T>> List<T> minMax(final Iterable<T> in) {
		final List<T> result =
			(List<T>) ops().run(net.imagej.ops.stats.DefaultMinMax.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMoment1AboutMean.class)
	public <T extends RealType<T>, O extends RealType<O>> O moment1AboutMean(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultMoment1AboutMean.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMoment1AboutMean.class)
	public <T extends RealType<T>, O extends RealType<O>> O moment1AboutMean(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops()
				.run(net.imagej.ops.stats.DefaultMoment1AboutMean.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMoment2AboutMean.class)
	public <T extends RealType<T>, O extends RealType<O>> O moment2AboutMean(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultMoment2AboutMean.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMoment2AboutMean.class)
	public <T extends RealType<T>, O extends RealType<O>> O moment2AboutMean(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops()
				.run(net.imagej.ops.stats.DefaultMoment2AboutMean.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMoment3AboutMean.class)
	public <T extends RealType<T>, O extends RealType<O>> O moment3AboutMean(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultMoment3AboutMean.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMoment3AboutMean.class)
	public <T extends RealType<T>, O extends RealType<O>> O moment3AboutMean(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops()
				.run(net.imagej.ops.stats.DefaultMoment3AboutMean.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMoment4AboutMean.class)
	public <T extends RealType<T>, O extends RealType<O>> O moment4AboutMean(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultMoment4AboutMean.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultMoment4AboutMean.class)
	public <T extends RealType<T>, O extends RealType<O>> O moment4AboutMean(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops()
				.run(net.imagej.ops.stats.DefaultMoment4AboutMean.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.IterableIntervalSize.class)
	public <T extends RealType<T>, O extends RealType<O>> O size(
		final IterableInterval<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.IterableIntervalSize.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.IterableIntervalSize.class)
	public <T extends RealType<T>, O extends RealType<O>> O size(final O out,
		final IterableInterval<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.IterableIntervalSize.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSize.class)
	public <T extends RealType<T>, O extends RealType<O>> O size(
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.stats.DefaultSize.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSize.class)
	public <T extends RealType<T>, O extends RealType<O>> O size(final O out,
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSize.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSkewness.class)
	public <T extends RealType<T>, O extends RealType<O>> O skewness(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSkewness.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSkewness.class)
	public <T extends RealType<T>, O extends RealType<O>> O skewness(final O out,
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSkewness.class, out, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.IterableStandardDeviation.class,
		net.imagej.ops.stats.DefaultStandardDeviation.class })
	public <T extends RealType<T>, O extends RealType<O>> O stdDev(
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.Ops.Stats.StdDev.class, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.IterableStandardDeviation.class,
		net.imagej.ops.stats.DefaultStandardDeviation.class })
	public <T extends RealType<T>, O extends RealType<O>> O stdDev(final O out,
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.Stats.StdDev.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSum.class)
	public <T extends RealType<T>, O extends RealType<O>> O sum(
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.stats.DefaultSum.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSum.class)
	public <T extends RealType<T>, O extends RealType<O>> O sum(final O out,
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSum.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSumOfInverses.class)
	public <T extends RealType<T>, O extends RealType<O>> O sumOfInverses(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSumOfInverses.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSumOfInverses.class)
	public <T extends RealType<T>, O extends RealType<O>> O sumOfInverses(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSumOfInverses.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSumOfLogs.class)
	public <T extends RealType<T>, O extends RealType<O>> O sumOfLogs(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSumOfLogs.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSumOfLogs.class)
	public <T extends RealType<T>, O extends RealType<O>> O sumOfLogs(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSumOfLogs.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSumOfSquares.class)
	public <T extends RealType<T>, O extends RealType<O>> O sumOfSquares(
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSumOfSquares.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.stats.DefaultSumOfSquares.class)
	public <T extends RealType<T>, O extends RealType<O>> O sumOfSquares(
		final O out, final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.stats.DefaultSumOfSquares.class, out, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.DefaultVariance.class,
		net.imagej.ops.stats.IterableVariance.class })
	public <T extends RealType<T>, O extends RealType<O>> O variance(
		final Iterable<T> in)
	{
		final O result = (O) ops().run(net.imagej.ops.Ops.Stats.Variance.class, in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.stats.DefaultVariance.class,
		net.imagej.ops.stats.IterableVariance.class })
	public <T extends RealType<T>, O extends RealType<O>> O variance(final O out,
		final Iterable<T> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.Stats.Variance.class, out, in);
		return result;
	}

	// -- Named methods --
	@Override
	public String getName() {
		return "stats";
	}

}
