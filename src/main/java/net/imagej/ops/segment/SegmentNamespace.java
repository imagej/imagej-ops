/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2024 ImageJ2 developers.
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

package net.imagej.ops.segment;

import java.util.List;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imagej.ops.segment.hough.HoughCircle;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.roi.geom.real.WritablePolyline;
import net.imglib2.type.BooleanType;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * The segment namespace contains segmentation operations.
 *
 * @author Gabe Selzer
 */
@Plugin(type = Namespace.class)
public class SegmentNamespace extends AbstractNamespace {

	// -- SegmentNamespace methods --

	// -- detectRidges --

	@OpMethod(op = net.imagej.ops.segment.detectRidges.DefaultDetectRidges.class)
	public <T extends RealType<T>> List<? extends WritablePolyline> detectRidges(
		final RandomAccessibleInterval<T> input, final double width,
		final double lowerThreshold, final double higherThreshold,
		final int ridgeLengthMin)
	{
		@SuppressWarnings("unchecked")
		final List<? extends WritablePolyline> result =
			(List<? extends WritablePolyline>) ops().run(
				Ops.Segment.DetectRidges.class, input, width, lowerThreshold,
				higherThreshold, ridgeLengthMin);

		return result;
	}

	// -- detectJunctions --

	@OpMethod(
		op = net.imagej.ops.segment.detectJunctions.DefaultDetectJunctions.class)
	public List<RealPoint> detectJunctions(
		final List<? extends WritablePolyline> lines)
	{
		@SuppressWarnings("unchecked")
		final List<RealPoint> result = (List<RealPoint>) ops().run(
			Ops.Segment.DetectJunctions.class, lines);

		return result;
	}

	@OpMethod(
		op = net.imagej.ops.segment.detectJunctions.DefaultDetectJunctions.class)
	public List<RealPoint> detectJunctions(
		final List<? extends WritablePolyline> lines, final double threshold)
	{
		@SuppressWarnings("unchecked")
		final List<RealPoint> result = (List<RealPoint>) ops().run(
			Ops.Segment.DetectJunctions.class, lines, threshold);

		return result;
	}

	// -- detectHoughCircleDoG --

	@OpMethod(op = net.imagej.ops.segment.hough.HoughCircleDetectorDogOp.class)
	public <T extends RealType<T> & NativeType<T>> List<HoughCircle>
		detectHoughCircleDoG(final RandomAccessibleInterval<T> in,
			final double circleThickness, final double minRadius,
			final double stepRadius, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final List<HoughCircle> result = (List<HoughCircle>) ops().run(
			net.imagej.ops.Ops.Segment.DetectHoughCircleDoG.class, in,
			circleThickness, minRadius, stepRadius, sigma);
		return result;
	}

	@OpMethod(op = net.imagej.ops.segment.hough.HoughCircleDetectorDogOp.class)
	public <T extends RealType<T> & NativeType<T>> List<HoughCircle>
		detectHoughCircleDoG(final RandomAccessibleInterval<T> in,
			final double circleThickness, final double minRadius,
			final double stepRadius, final double sigma, final double sensitivity)
	{
		@SuppressWarnings("unchecked")
		final List<HoughCircle> result = (List<HoughCircle>) ops().run(
			net.imagej.ops.Ops.Segment.DetectHoughCircleDoG.class, in,
			circleThickness, minRadius, stepRadius, sigma, sensitivity);
		return result;
	}

	// -- detectHoughCircleLE --

	@OpMethod(
		op = net.imagej.ops.segment.hough.HoughCircleDetectorLocalExtremaOp.class)
	public <T extends RealType<T> & NativeType<T>> List<HoughCircle>
		detectHoughCircleLE(final RandomAccessibleInterval<T> in,
			final double minRadius, final double stepRadius)
	{
		@SuppressWarnings("unchecked")
		final List<HoughCircle> result = (List<HoughCircle>) ops().run(
			net.imagej.ops.Ops.Segment.DetectHoughCircleLE.class, in, minRadius,
			stepRadius);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.segment.hough.HoughCircleDetectorLocalExtremaOp.class)
	public <T extends RealType<T> & NativeType<T>> List<HoughCircle>
		detectHoughCircleLE(final RandomAccessibleInterval<T> in,
			final double minRadius, final double stepRadius, final double sensitivity)
	{
		@SuppressWarnings("unchecked")
		final List<HoughCircle> result = (List<HoughCircle>) ops().run(
			net.imagej.ops.Ops.Segment.DetectHoughCircleLE.class, in, minRadius,
			stepRadius, sensitivity);
		return result;
	}

	// -- transformHoughCircle --

	@OpMethod(op = net.imagej.ops.segment.hough.HoughTransformOpNoWeights.class)
	public <T extends BooleanType<T>> Img<DoubleType> transformHoughCircle(
		final IterableInterval<T> in, final long minRadius, final long maxRadius)
	{
		@SuppressWarnings("unchecked")
		final Img<DoubleType> result = (Img<DoubleType>) ops().run(
			net.imagej.ops.Ops.Segment.TransformHoughCircle.class, in, minRadius,
			maxRadius);
		return result;
	}

	@OpMethod(op = net.imagej.ops.segment.hough.HoughTransformOpNoWeights.class)
	public <T extends BooleanType<T>> Img<DoubleType> transformHoughCircle(
		final Img<DoubleType> out, final IterableInterval<T> in,
		final long minRadius, final long maxRadius)
	{
		@SuppressWarnings("unchecked")
		final Img<DoubleType> result = (Img<DoubleType>) ops().run(
			net.imagej.ops.Ops.Segment.TransformHoughCircle.class, out, in, minRadius,
			maxRadius);
		return result;
	}

	@OpMethod(op = net.imagej.ops.segment.hough.HoughTransformOpNoWeights.class)
	public <T extends BooleanType<T>> Img<DoubleType> transformHoughCircle(
		final Img<DoubleType> out, final IterableInterval<T> in,
		final long minRadius, final long maxRadius, final long stepRadius)
	{
		@SuppressWarnings("unchecked")
		final Img<DoubleType> result = (Img<DoubleType>) ops().run(
			net.imagej.ops.Ops.Segment.TransformHoughCircle.class, out, in, minRadius,
			maxRadius, stepRadius);
		return result;
	}

	@OpMethod(op = net.imagej.ops.segment.hough.HoughTransformOpWeights.class)
	public <T extends BooleanType<T>, R extends RealType<R>> Img<DoubleType>
		transformHoughCircle(final IterableInterval<T> in, final long minRadius,
			final long maxRadius, final RandomAccessible<R> weights)
	{
		@SuppressWarnings("unchecked")
		final Img<DoubleType> result = (Img<DoubleType>) ops().run(
			net.imagej.ops.Ops.Segment.TransformHoughCircle.class, in, minRadius,
			maxRadius, weights);
		return result;
	}

	@OpMethod(op = net.imagej.ops.segment.hough.HoughTransformOpWeights.class)
	public <T extends BooleanType<T>, R extends RealType<R>> Img<DoubleType>
		transformHoughCircle(final Img<DoubleType> out,
			final IterableInterval<T> in, final long minRadius, final long maxRadius,
			final RandomAccessible<R> weights)
	{
		@SuppressWarnings("unchecked")
		final Img<DoubleType> result = (Img<DoubleType>) ops().run(
			net.imagej.ops.Ops.Segment.TransformHoughCircle.class, out, in, minRadius,
			maxRadius, weights);
		return result;
	}

	@OpMethod(op = net.imagej.ops.segment.hough.HoughTransformOpWeights.class)
	public <T extends BooleanType<T>, R extends RealType<R>> Img<DoubleType>
		transformHoughCircle(final Img<DoubleType> out,
			final IterableInterval<T> in, final long minRadius, final long maxRadius,
			final long stepRadius, final RandomAccessible<R> weights)
	{
		@SuppressWarnings("unchecked")
		final Img<DoubleType> result = (Img<DoubleType>) ops().run(
			net.imagej.ops.Ops.Segment.TransformHoughCircle.class, out, in, minRadius,
			maxRadius, stepRadius, weights);
		return result;
	}

	// -- Namespace methods --

	@Override
	public String getName() {
		return "segment";
	}

}
