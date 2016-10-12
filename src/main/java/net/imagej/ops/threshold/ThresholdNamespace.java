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

package net.imagej.ops.threshold;

import java.util.Comparator;
import java.util.List;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * The threshold namespace contains operations related to binary thresholding.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class ThresholdNamespace extends AbstractNamespace {

	// -- Threshold namespace ops --

	// -- apply --

	@OpMethod(
		op = net.imagej.ops.threshold.apply.ApplyConstantThreshold.class)
	public <T extends RealType<T>> Iterable<BitType> apply(
		final Iterable<BitType> out, final Iterable<T> in, final T threshold)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Apply.class,
				out, in, threshold);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.apply.ApplyManualThreshold.class)
	public <T extends RealType<T>> IterableInterval<BitType> apply(final IterableInterval<T> in,
		final T threshold)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Apply.class, in,
				threshold);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.apply.ApplyManualThreshold.class)
	public <T extends RealType<T>> IterableInterval<BitType> apply(final IterableInterval<BitType> out,
		final IterableInterval<T> in, final T threshold)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Apply.class, out,
				in, threshold);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.apply.ApplyThresholdComparable.class)
	public <T> BitType apply(final BitType out,
		final Comparable<? super T> in, final T threshold)
	{
		final BitType result =
			(BitType) ops().run(
				net.imagej.ops.Ops.Threshold.Apply.class,
				out, in, threshold);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.apply.ApplyThresholdComparator.class)
	public <T> BitType apply(final BitType out, final T in,
		final T threshold, final Comparator<? super T> comparator)
	{
		final BitType result =
			(BitType) ops().run(
				net.imagej.ops.Ops.Threshold.Apply.class,
				out, in, threshold, comparator);
		return result;
	}

	// -- huang --

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Huang.class)
	public <T extends RealType<T>> Iterable<BitType> huang(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Huang.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Huang.class)
	public <T extends RealType<T>> Iterable<BitType> huang(final Iterable<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Huang.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalHuang.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		huang(final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalHuang.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalHuang.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		huang(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalHuang.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalHuang.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		huang(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalHuang.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IJ1.class)
	public
		<T extends RealType<T>> Iterable<BitType> ij1(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.IJ1.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IJ1.class)
	public
		<T extends RealType<T>> Iterable<BitType> ij1(final Iterable<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.IJ1.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		ij1(final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		ij1(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		ij1(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Intermodes.class)
	public
		<T extends RealType<T>> Iterable<BitType> intermodes(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Intermodes.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Intermodes.class)
	public
		<T extends RealType<T>> Iterable<BitType> intermodes(final Iterable<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Intermodes.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		intermodes(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		intermodes(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		intermodes(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IsoData.class)
	public <T extends RealType<T>> Iterable<BitType> isoData(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.IsoData.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IsoData.class)
	public <T extends RealType<T>> Iterable<BitType> isoData(final Iterable<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.IsoData.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		isoData(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		isoData(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		isoData(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Li.class)
	public
		<T extends RealType<T>> Iterable<BitType> li(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Li.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Li.class)
	public
		<T extends RealType<T>> Iterable<BitType> li(final Iterable<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops()
				.run(net.imagej.ops.Ops.Threshold.Li.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalLi.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		li(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalLi.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalLi.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		li(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalLi.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalLi.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		li(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalLi.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localContrast.LocalContrast.class)
	public <T extends RealType<T>> Iterable<BitType>
		localContrastThreshold(final Iterable<BitType> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalContrastThreshold.class,
				out, in, shape, outOfBounds);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localContrast.LocalContrast.class)
	public <T extends RealType<T>> Iterable<BitType>
		localContrastThreshold(final Iterable<BitType> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalContrastThreshold.class,
				out, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localContrast.LocalContrast.class)
	public <T extends RealType<T>> Iterable<BitType> localContrastThreshold(
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalContrastThreshold.class, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMean.LocalMean.class)
	public <T extends RealType<T>> Iterable<BitType> localMeanThreshold(
		final Iterable<BitType> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, out, in, shape,
				outOfBounds, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMean.LocalMean.class)
	public <T extends RealType<T>> Iterable<BitType> localMeanThreshold(
		final Iterable<BitType> out,
		final RandomAccessibleInterval<T> in, final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, out, in,
				shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMean.LocalMean.class)
	public <T extends RealType<T>> Iterable<BitType> localMeanThreshold(
		final RandomAccessibleInterval<T> in, final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, in,
				shape, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMean.LocalMeanThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType> localMeanThreshold(
		final IterableInterval<BitType> out, final RandomAccessibleInterval<T> in,
		final RectangleShape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, out, in,
				shape, outOfBounds, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMean.LocalMeanThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType> localMeanThreshold(
		final IterableInterval<BitType> out, final RandomAccessibleInterval<T> in,
		final RectangleShape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, out, in,
				shape, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMedian.LocalMedian.class)
	public <T extends RealType<T>> Iterable<BitType> localMedianThreshold(
		final Iterable<BitType> out, final RandomAccessibleInterval<T> in,
		final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMedianThreshold.class, out,
				in, shape, outOfBounds, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMedian.LocalMedian.class)
	public <T extends RealType<T>> Iterable<BitType> localMedianThreshold(
		final Iterable<BitType> out, final RandomAccessibleInterval<T> in,
		final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMedianThreshold.class, out,
				in, shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMedian.LocalMedian.class)
	public <T extends RealType<T>> Iterable<BitType> localMedianThreshold(
		final RandomAccessibleInterval<T> in, final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalMedianThreshold.class, in, shape, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMidGrey.LocalMidGrey.class)
	public <T extends RealType<T>> Iterable<BitType> localMidGreyThreshold(
		final Iterable<BitType> out, final RandomAccessibleInterval<T> in,
		final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMidGreyThreshold.class, out,
				in, shape, outOfBounds, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMidGrey.LocalMidGrey.class)
	public <T extends RealType<T>> Iterable<BitType> localMidGreyThreshold(
		final Iterable<BitType> out, final RandomAccessibleInterval<T> in,
		final Shape shape,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMidGreyThreshold.class, out,
				in, shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMidGrey.LocalMidGrey.class)
	public <T extends RealType<T>> Iterable<BitType> localMidGreyThreshold(
		final RandomAccessibleInterval<T> in, final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalMidGreyThreshold.class, in, shape, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.LocalNiblack.class)
	public <T extends RealType<T>> Iterable<BitType> localNiblackThreshold(
		final Iterable<BitType> out, final RandomAccessibleInterval<T> in,
		final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
		final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, out,
				in, shape, outOfBounds, c, k);
		return result;
	}
	
	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.LocalNiblack.class)
	public <T extends RealType<T>> Iterable<BitType> localNiblackThreshold(
		final Iterable<BitType> out, final RandomAccessibleInterval<T> in,
		final Shape shape,
		final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, out,
				in, shape, c, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localNiblack.LocalNiblack.class)
	public <T extends RealType<T>> Iterable<BitType> localNiblackThreshold(
		final RandomAccessibleInterval<T> in, final Shape shape, final double c,
		final double k)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, in, shape, c,
			k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.LocalNiblackThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localNiblackThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
			final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, out, in,
				shape, outOfBounds, c, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.LocalNiblackThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localNiblackThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape,
			final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, out, in,
				shape, c, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localBernsen.LocalBernsen.class)
	public <T extends RealType<T>> Iterable<BitType> localBernsenThreshold(
		final Iterable<BitType> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
		final double contrastThreshold,
		final double halfMaxValue)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalBernsenThreshold.class, out, in, shape,
				outOfBounds, contrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localBernsen.LocalBernsen.class)
	public <T extends RealType<T>> Iterable<BitType> localBernsenThreshold(
		final Iterable<BitType> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final double contrastThreshold,
		final double halfMaxValue)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalBernsenThreshold.class, out, in, shape,
				contrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localBernsen.LocalBernsen.class)
	public <T extends RealType<T>> Iterable<BitType> localBernsenThreshold(
		final RandomAccessibleInterval<T> in, final Shape shape,
		final double contrastThreshold,
		final double halfMaxValue)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalBernsenThreshold.class, in, shape,
				contrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <T extends RealType<T>> Iterable<BitType>
		localPhansalkarThreshold(final Iterable<BitType> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
			final double k, final double r)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(
				net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class,
				out, in, shape, outOfBounds, k, r);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <T extends RealType<T>> Iterable<BitType>
		localPhansalkarThreshold(final Iterable<BitType> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
			final double k)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(
				net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class,
				out, in, shape, outOfBounds, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <T extends RealType<T>> Iterable<BitType>
		localPhansalkarThreshold(final Iterable<BitType> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(
				net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class,
				out, in, shape, outOfBounds);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <T extends RealType<T>> Iterable<BitType>
		localPhansalkarThreshold(final Iterable<BitType> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops()
			.run(
				net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class,
				out, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <T extends RealType<T>> Iterable<BitType> localPhansalkarThreshold(
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkarThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localPhansalkarThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
			final double k, final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, out, in,
				shape, outOfBounds, k, r);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkarThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localPhansalkarThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
			final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, out, in,
				shape, outOfBounds, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkarThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localPhansalkarThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, out, in,
				shape, outOfBounds);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkarThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localPhansalkarThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, out, in,
				shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <T extends RealType<T>> Iterable<BitType> localSauvolaThreshold(
		final Iterable<BitType> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
		final double k, final double r)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in, shape,
				outOfBounds, k, r);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <T extends RealType<T>> Iterable<BitType> localSauvolaThreshold(
		final Iterable<BitType> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
		final double k)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in, shape,
				outOfBounds, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <T extends RealType<T>> Iterable<BitType> localSauvolaThreshold(
		final Iterable<BitType> out,
		final RandomAccessibleInterval<T> in, final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in, shape,
				outOfBounds);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <T extends RealType<T>> Iterable<BitType> localSauvolaThreshold(
		final Iterable<BitType> out,
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <T extends RealType<T>> Iterable<BitType> localSauvolaThreshold(
		final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.LocalSauvolaThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localSauvolaThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
			final double k, final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in,
				shape, outOfBounds, k, r);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.LocalSauvolaThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localSauvolaThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds,
			final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in,
				shape, outOfBounds, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.LocalSauvolaThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localSauvolaThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in,
				shape, outOfBounds);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.LocalSauvolaThresholdIntegral.class)
	public <T extends RealType<T>> IterableInterval<BitType>
		localSauvolaThreshold(final IterableInterval<BitType> out,
			final RandomAccessibleInterval<T> in, final RectangleShape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result = (IterableInterval<BitType>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in,
				shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MaxEntropy.class)
	public
		<T extends RealType<T>> Iterable<BitType> maxEntropy(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.MaxEntropy.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MaxEntropy.class)
	public
		<T extends RealType<T>> Iterable<BitType> maxEntropy(final Iterable<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.MaxEntropy.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		maxEntropy(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		maxEntropy(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		maxEntropy(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MaxLikelihood.class)
	public
		<T extends RealType<T>> Iterable<BitType> maxLikelihood(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.MaxLikelihood.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MaxLikelihood.class)
	public
		<T extends RealType<T>> Iterable<BitType> maxLikelihood(final Iterable<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.MaxLikelihood.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		maxLikelihood(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		maxLikelihood(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		maxLikelihood(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Mean.class)
	public <T extends RealType<T>> Iterable<BitType> mean(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Mean.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Mean.class)
	public <T extends RealType<T>> Iterable<BitType> mean(final Iterable<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Mean.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MinError.class)
	public <T extends RealType<T>> Iterable<BitType> minError(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.MinError.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MinError.class)
	public <T extends RealType<T>> Iterable<BitType> minError(final Iterable<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.MinError.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinError.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		minError(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinError.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinError.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		minError(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinError.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinError.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		minError(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinError.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Minimum.class)
	public <T extends RealType<T>> Iterable<BitType> minimum(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.Minimum.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Minimum.class)
	public <T extends RealType<T>> Iterable<BitType> minimum(final Iterable<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Minimum.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		minimum(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		minimum(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		minimum(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Moments.class)
	public <T extends RealType<T>> Iterable<BitType> moments(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.Moments.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Moments.class)
	public <T extends RealType<T>> Iterable<BitType> moments(final Iterable<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Moments.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMoments.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		moments(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMoments.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMoments.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		moments(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMoments.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMoments.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		moments(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMoments.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Otsu.class)
	public <T extends RealType<T>> Iterable<BitType> otsu(
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.Otsu.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Otsu.class)
	public <T extends RealType<T>> Iterable<BitType> otsu(
		final Iterable<BitType> out, final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.Otsu.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		otsu(final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class,
			in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		otsu(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class, out, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		otsu(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class, out, in, shape,
			outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Percentile.class)
	public
		<T extends RealType<T>> Iterable<BitType> percentile(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Percentile.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Percentile.class)
	public
		<T extends RealType<T>> Iterable<BitType> percentile(final Iterable<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Percentile.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		percentile(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		percentile(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		percentile(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.RenyiEntropy.class)
	public
		<T extends RealType<T>> Iterable<BitType> renyiEntropy(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.RenyiEntropy.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.RenyiEntropy.class)
	public
		<T extends RealType<T>> Iterable<BitType> renyiEntropy(final Iterable<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.RenyiEntropy.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		renyiEntropy(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		renyiEntropy(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		renyiEntropy(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Shanbhag.class)
	public <T extends RealType<T>> Iterable<BitType> shanbhag(
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.Shanbhag.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Shanbhag.class)
	public <T extends RealType<T>> Iterable<BitType> shanbhag(
		final Iterable<BitType> out, final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result = (Iterable<BitType>) ops().run(
			net.imagej.ops.Ops.Threshold.Shanbhag.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		shanbhag(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		shanbhag(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		shanbhag(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Triangle.class)
	public <T extends RealType<T>> Iterable<BitType> triangle(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Triangle.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Triangle.class)
	public <T extends RealType<T>> Iterable<BitType> triangle(final Iterable<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Triangle.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		triangle(final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		triangle(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		triangle(final Iterable<B> out,
			final RandomAccessibleInterval<T> in, final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Yen.class)
	public <T extends RealType<T>> Iterable<BitType> yen(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Yen.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Yen.class)
	public <T extends RealType<T>> Iterable<BitType> yen(final Iterable<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.Ops.Threshold.Yen.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalYen.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		yen(final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalYen.class,
			in, shape);
		return result;
	}
	
	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalYen.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		yen(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalYen.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalYen.class)
	public <T extends RealType<T>, B extends BooleanType<B>> Iterable<B>
		yen(final Iterable<B> out, final RandomAccessibleInterval<T> in,
			final Shape shape,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final Iterable<B> result = (Iterable<B>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalYen.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "threshold";
	}
}
