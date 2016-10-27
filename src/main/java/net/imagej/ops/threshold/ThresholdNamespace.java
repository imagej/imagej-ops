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

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.BooleanType;
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

	@OpMethod(op = net.imagej.ops.threshold.manual.Manual.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> manual(
		final IterableInterval<I> in, final I threshold)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.threshold.manual.Manual.class, in, threshold);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.manual.Manual.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> manual(
		final IterableInterval<O> out, final IterableInterval<I> in,
		final I threshold)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.threshold.manual.Manual.class, out, in, threshold);
		return result;
	}

	// -- huang --

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Huang.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> huang(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Huang.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.HuangComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		huang(final IterableInterval<O> out, final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.Huang.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalHuang.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		huang(final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalHuang.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalHuang.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		huang(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalHuang.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalHuang.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		huang(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalHuang.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IJ1.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> ij1(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.IJ1.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IJ1Computer.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> ij1(final IterableInterval<O> out,
			final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.IJ1.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		ij1(final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		ij1(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		ij1(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIJ1.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Intermodes.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> intermodes(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Intermodes.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IntermodesComputer.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> intermodes(final IterableInterval<O> out,
			final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Intermodes.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		intermodes(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		intermodes(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		intermodes(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIntermodes.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IsoData.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> isoData(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.IsoData.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.IsoDataComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> isoData(final IterableInterval<O> out,
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.IsoData.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		isoData(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		isoData(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		isoData(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalIsoData.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Li.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> li(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Li.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.LiComputer.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> li(final IterableInterval<O> out,
			final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops()
				.run(net.imagej.ops.Ops.Threshold.Li.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalLi.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		li(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalLi.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalLi.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		li(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalLi.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalLi.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		li(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalLi.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localContrast.LocalContrast.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localContrastThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalContrastThreshold.class,
				out, in, shape, outOfBounds);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localContrast.LocalContrast.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localContrastThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalContrastThreshold.class,
				out, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localContrast.LocalContrast.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localContrastThreshold(
		final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalContrastThreshold.class, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localContrast.ContrastComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localContrastThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localContrast.Contrast.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localContrast.Contrast.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localContrastThreshold(
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localContrast.Contrast.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMean.LocalMean.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMeanThreshold(
		final IterableInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, out, in, shape,
				outOfBounds, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMean.LocalMean.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMeanThreshold(
		final IterableInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, out, in,
				shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMean.LocalMean.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMeanThreshold(
		final RandomAccessibleInterval<I> in, final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, in,
				shape, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMean.IntegralLocalMean.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMeanThreshold(
		final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
		final RectangleShape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, out, in,
				shape, outOfBounds, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMean.IntegralLocalMean.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMeanThreshold(
		final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
		final RectangleShape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, out, in,
				shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMean.IntegralLocalMean.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMeanThreshold(
		final RandomAccessibleInterval<I> in, final RectangleShape shape,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMeanThreshold.class, in, shape, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMedian.LocalMedian.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMedianThreshold(
		final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
		final Shape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMedianThreshold.class, out,
				in, shape, outOfBounds, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMedian.LocalMedian.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMedianThreshold(
		final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
		final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMedianThreshold.class, out,
				in, shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMedian.LocalMedian.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMedianThreshold(
		final RandomAccessibleInterval<I> in, final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalMedianThreshold.class, in, shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMedian.MedianComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMedianThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localMedian.Median.class, out, in, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMedian.Median.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMedianThreshold(
		final IterableInterval<I> in, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localMedian.Median.class, in, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMidGrey.LocalMidGrey.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMidGreyThreshold(
		final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
		final Shape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMidGreyThreshold.class, out,
				in, shape, outOfBounds, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localMidGrey.LocalMidGrey.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMidGreyThreshold(
		final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
		final Shape shape,
		final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalMidGreyThreshold.class, out,
				in, shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMidGrey.LocalMidGrey.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMidGreyThreshold(
		final RandomAccessibleInterval<I> in, final Shape shape, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalMidGreyThreshold.class, in, shape, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMidGrey.MidGreyComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMidGreyThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localMidGrey.MidGrey.class, out, in, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMidGrey.MidGrey.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localMidGreyThreshold(
		final IterableInterval<I> in, final double c)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localMidGrey.MidGrey.class, in, c);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.LocalNiblack.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localNiblackThreshold(
		final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
		final Shape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
		final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, out,
				in, shape, outOfBounds, c, k);
		return result;
	}
	
	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.LocalNiblack.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localNiblackThreshold(
		final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
		final Shape shape,
		final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, out,
				in, shape, c, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localNiblack.LocalNiblack.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localNiblackThreshold(
		final RandomAccessibleInterval<I> in, final Shape shape, final double c,
		final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, in, shape, c,
			k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localNiblack.NiblackComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localNiblackThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in, final double c,
		final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localNiblack.Niblack.class, out, in, c, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localNiblack.Niblack.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localNiblackThreshold(
		final IterableInterval<I> in, final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localNiblack.Niblack.class, in, c, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.IntegralLocalNiblack.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localNiblackThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
			final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, out, in,
				shape, outOfBounds, c, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.IntegralLocalNiblack.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localNiblackThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape,
			final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, out, in,
				shape, c, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localNiblack.IntegralLocalNiblack.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localNiblackThreshold(final RandomAccessibleInterval<I> in,
			final RectangleShape shape, final double c, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalNiblackThreshold.class, in, shape,
				c, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localBernsen.LocalBernsen.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localBernsenThreshold(
		final IterableInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
		final double contrastThreshold,
		final double halfMaxValue)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalBernsenThreshold.class, out, in, shape,
				outOfBounds, contrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localBernsen.LocalBernsen.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localBernsenThreshold(
		final IterableInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape,
		final double contrastThreshold,
		final double halfMaxValue)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalBernsenThreshold.class, out, in, shape,
				contrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localBernsen.LocalBernsen.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localBernsenThreshold(
		final RandomAccessibleInterval<I> in, final Shape shape,
		final double contrastThreshold,
		final double halfMaxValue)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalBernsenThreshold.class, in, shape,
				contrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localBernsen.BernsenComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localBernsenThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in,
		final double constrastThreshold, final double halfMaxValue)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localBernsen.Bernsen.class, out, in,
			constrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localBernsen.Bernsen.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localBernsenThreshold(
		final IterableInterval<I> in, final double constrastThreshold,
		final double halfMaxValue)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localBernsen.Bernsen.class, in,
			constrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
			final double k, final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(
				net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class,
				out, in, shape, outOfBounds, k, r);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
			final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(
				net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class,
				out, in, shape, outOfBounds, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(
				net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class,
				out, in, shape, outOfBounds);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(
				net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class,
				out, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localPhansalkarThreshold(
		final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.PhansalkarComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localPhansalkarThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in, final double k,
		final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localPhansalkar.Phansalkar.class, out, in, k, r);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.PhansalkarComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localPhansalkarThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localPhansalkar.Phansalkar.class, out, in, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.PhansalkarComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localPhansalkarThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localPhansalkar.Phansalkar.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.Phansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<I> in, final double k,
			final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localPhansalkar.Phansalkar.class, in, k, r);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.Phansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<I> in, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localPhansalkar.Phansalkar.class, in, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.Phansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localPhansalkarThreshold(
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localPhansalkar.Phansalkar.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.IntegralLocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
			final double k, final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, out, in,
				shape, outOfBounds, k, r);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.IntegralLocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
			final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, out, in,
				shape, outOfBounds, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.IntegralLocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, out, in,
				shape, outOfBounds);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.IntegralLocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, out, in,
				shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localPhansalkar.IntegralLocalPhansalkar.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localPhansalkarThreshold(final RandomAccessibleInterval<I> in,
			final RectangleShape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalPhansalkarThreshold.class, in,
				shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final IterableInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
		final double k, final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in, shape,
				outOfBounds, k, r);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final IterableInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
		final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in, shape,
				outOfBounds, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final IterableInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape,
		final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in, shape,
				outOfBounds);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final IterableInterval<O> out,
		final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.SauvolaComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in, final double k,
		final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localSauvola.Sauvola.class, out, in, k, r);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.SauvolaComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localSauvola.Sauvola.class, out, in, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.SauvolaComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final IterableInterval<O> out, final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localSauvola.Sauvola.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.Sauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localSauvolaThreshold(final IterableInterval<I> in, final double k,
			final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localSauvola.Sauvola.class, in, k, r);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.Sauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localSauvolaThreshold(final IterableInterval<I> in, final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localSauvola.Sauvola.class, in, k);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.Sauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> localSauvolaThreshold(
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.localSauvola.Sauvola.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.IntegralLocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localSauvolaThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
			final double k, final double r)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in,
				shape, outOfBounds, k, r);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.IntegralLocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localSauvolaThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds,
			final double k)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in,
				shape, outOfBounds, k);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.IntegralLocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localSauvolaThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in,
				shape, outOfBounds);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.IntegralLocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localSauvolaThreshold(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final RectangleShape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, out, in,
				shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.localSauvola.IntegralLocalSauvola.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		localSauvolaThreshold(final RandomAccessibleInterval<I> in,
			final RectangleShape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops()
			.run(net.imagej.ops.Ops.Threshold.LocalSauvolaThreshold.class, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MaxEntropy.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> maxEntropy(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.MaxEntropy.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MaxEntropyComputer.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> maxEntropy(final IterableInterval<O> out,
			final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.MaxEntropy.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		maxEntropy(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		maxEntropy(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		maxEntropy(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MaxLikelihood.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> maxLikelihood(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.MaxLikelihood.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MaxLikelihoodComputer.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> maxLikelihood(final IterableInterval<O> out,
			final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.MaxLikelihood.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		maxLikelihood(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		maxLikelihood(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		maxLikelihood(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Mean.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> mean(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Mean.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MeanComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> mean(final IterableInterval<O> out,
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Mean.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MinError.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> minError(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.MinError.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MinErrorComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> minError(final IterableInterval<O> out,
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.MinError.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinError.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		minError(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinError.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinError.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		minError(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinError.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinError.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		minError(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinError.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Minimum.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> minimum(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.Minimum.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MinimumComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> minimum(final IterableInterval<O> out,
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Minimum.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		minimum(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		minimum(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		minimum(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMinimum.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Moments.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> moments(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.Moments.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.MomentsComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> moments(final IterableInterval<O> out,
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Moments.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMoments.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		moments(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMoments.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMoments.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		moments(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMoments.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalMoments.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		moments(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalMoments.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Otsu.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> otsu(
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.Otsu.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.OtsuComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> otsu(
		final IterableInterval<O> out, final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.Otsu.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		otsu(final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class,
			in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		otsu(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class, out, in, shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		otsu(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalOtsu.class, out, in, shape,
			outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Percentile.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> percentile(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Percentile.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.PercentileComputer.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> percentile(final IterableInterval<O> out,
			final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Percentile.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		percentile(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		percentile(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		percentile(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalPercentile.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.RenyiEntropy.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> renyiEntropy(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.RenyiEntropy.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.RenyiEntropyComputer.class)
	public
		<I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> renyiEntropy(final IterableInterval<O> out,
			final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops()
				.run(
					net.imagej.ops.Ops.Threshold.RenyiEntropy.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		renyiEntropy(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		renyiEntropy(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		renyiEntropy(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.Shanbhag.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> shanbhag(
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.Shanbhag.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.GlobalThresholders.ShanbhagComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> shanbhag(
		final IterableInterval<O> out, final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Threshold.Shanbhag.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		shanbhag(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		shanbhag(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		shanbhag(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalShanbhag.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Triangle.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> triangle(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Triangle.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.TriangleComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> triangle(final IterableInterval<O> out,
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Triangle.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		triangle(final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		triangle(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		triangle(final IterableInterval<O> out,
			final RandomAccessibleInterval<I> in, final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalTriangle.class,
			out, in, shape, outOfBoundsFactory);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.Yen.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> yen(final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Yen.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.GlobalThresholders.YenComputer.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O> yen(final IterableInterval<O> out,
		final IterableInterval<I> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.Ops.Threshold.Yen.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalYen.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		yen(final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalYen.class,
			in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalYen.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		yen(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.threshold.LocalThresholders.LocalYen.class,
			out, in, shape);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.LocalThresholders.LocalYen.class)
	public <I extends RealType<I>, O extends BooleanType<O>> IterableInterval<O>
		yen(final IterableInterval<O> out, final RandomAccessibleInterval<I> in,
			final Shape shape,
			final OutOfBoundsFactory<I, RandomAccessibleInterval<I>> outOfBoundsFactory)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
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
