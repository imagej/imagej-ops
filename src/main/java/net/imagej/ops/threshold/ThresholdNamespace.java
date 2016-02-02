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
import net.imagej.ops.Ops;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

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

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Apply.class)
	public Object apply(final Object... args) {
		return ops().run(Ops.Threshold.Apply.NAME, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.apply.ApplyConstantThreshold.class)
	public <T extends RealType<T>> Iterable<BitType> apply(
		final Iterable<BitType> out, final Iterable<T> in, final T threshold)
	{
		@SuppressWarnings("unchecked")
		final Iterable<BitType> result =
			(Iterable<BitType>) ops().run(
				net.imagej.ops.threshold.apply.ApplyConstantThreshold.class,
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
				net.imagej.ops.threshold.apply.ApplyManualThreshold.class, in,
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
				net.imagej.ops.threshold.apply.ApplyManualThreshold.class, out,
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
				net.imagej.ops.threshold.apply.ApplyThresholdComparable.class,
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
				net.imagej.ops.threshold.apply.ApplyThresholdComparator.class,
				out, in, threshold, comparator);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.apply.LocalThreshold.class)
	public <T extends RealType<T>> RandomAccessibleInterval<BitType> apply(
		final RandomAccessibleInterval<BitType> out,
		final RandomAccessibleInterval<T> in, final LocalThresholdMethod<T> method,
		final Shape shape)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<BitType> result =
			(RandomAccessibleInterval<BitType>) ops().run(
				net.imagej.ops.threshold.apply.LocalThreshold.class, out, in, method,
				shape);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.apply.LocalThreshold.class)
	public <T extends RealType<T>> RandomAccessibleInterval<BitType> apply(
		final RandomAccessibleInterval<BitType> out,
		final RandomAccessibleInterval<T> in, final LocalThresholdMethod<T> method,
		final Shape shape,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<BitType> result =
			(RandomAccessibleInterval<BitType>) ops().run(
				net.imagej.ops.threshold.apply.LocalThreshold.class, out, in, method,
				shape, outOfBounds);
		return result;
	}

	// -- huang --

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Huang.class)
	public Object huang(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Huang.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.huang.ComputeHuangThreshold.class)
	public <T extends RealType<T>> T huang(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.huang.ComputeHuangThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.huang.ComputeHuangThreshold.class)
	public <T extends RealType<T>> T huang(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.huang.ComputeHuangThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Huang.class)
	public <T extends RealType<T>> IterableInterval<BitType> huang(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Huang.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Huang.class)
	public <T extends RealType<T>> IterableInterval<BitType> huang(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Huang.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.IJ1.class)
	public Object ij1(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.IJ1.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ij1.ComputeIJ1Threshold.class)
	public <T extends RealType<T>> T ij1(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.ij1.ComputeIJ1Threshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ij1.ComputeIJ1Threshold.class)
	public <T extends RealType<T>> T ij1(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.ij1.ComputeIJ1Threshold.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.IJ1.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> ij1(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.IJ1.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.IJ1.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> ij1(final IterableInterval<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.IJ1.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Intermodes.class)
	public Object intermodes(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Intermodes.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.intermodes.ComputeIntermodesThreshold.class)
	public
		<T extends RealType<T>> List<Object> intermodes(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops()
				.run(
					net.imagej.ops.threshold.intermodes.ComputeIntermodesThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.intermodes.ComputeIntermodesThreshold.class)
	public
		<T extends RealType<T>> List<Object> intermodes(final T out,
			final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops()
				.run(
					net.imagej.ops.threshold.intermodes.ComputeIntermodesThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Intermodes.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> intermodes(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Intermodes.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Intermodes.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> intermodes(final IterableInterval<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Intermodes.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.IsoData.class)
	public Object isoData(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.IsoData.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.IsoData.class)
	public <T extends RealType<T>> IterableInterval<BitType> isoData(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops()
				.run(
					net.imagej.ops.threshold.ApplyThresholdMethod.IsoData.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.IsoData.class)
	public <T extends RealType<T>> IterableInterval<BitType> isoData(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.IsoData.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.isoData.ComputeIsoDataThreshold.class)
	public <T extends RealType<T>> List<Object> isoData(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.isoData.ComputeIsoDataThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.isoData.ComputeIsoDataThreshold.class)
	public <T extends RealType<T>> List<Object> isoData(final T out,
		final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.isoData.ComputeIsoDataThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Li.class)
	public Object li(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Li.class, args);
	}

	@OpMethod(op = net.imagej.ops.threshold.ApplyThresholdMethod.Li.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> li(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Li.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.ApplyThresholdMethod.Li.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> li(final IterableInterval<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops()
				.run(net.imagej.ops.threshold.ApplyThresholdMethod.Li.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.li.ComputeLiThreshold.class)
	public <T extends RealType<T>> T li(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.li.ComputeLiThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.li.ComputeLiThreshold.class)
	public <T extends RealType<T>> T li(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.li.ComputeLiThreshold.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localContrast.LocalContrast.class)
	public <T extends RealType<T>> BitType localContrast(final BitType out,
		final T center, final Iterable<T> neighborhood)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localContrast.LocalContrast.class, out, center,
			neighborhood);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMean.LocalMean.class)
	public <T extends RealType<T>> BitType localMean(final BitType out,
		final T center, final Iterable<T> neighborhood, final double c)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localMean.LocalMean.class, out, center,
			neighborhood, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMedian.LocalMedian.class)
	public <T extends RealType<T>> BitType localMedian(final BitType out,
		final T center, final Iterable<T> neighborhood, final double c)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localMedian.LocalMedian.class, out, center,
			neighborhood, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localMidGrey.LocalMidGrey.class)
	public <T extends RealType<T>> BitType localMidGrey(final BitType out,
		final T center, final Iterable<T> neighborhood, final double c)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localMidGrey.LocalMidGrey.class, out, center,
			neighborhood, c);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localNiblack.LocalNiblack.class)
	public <T extends RealType<T>> BitType localNiblack(final BitType out,
		final T center, final Iterable<T> neighborhood, final double c, final double k)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localNiblack.LocalNiblack.class, out, center,
			neighborhood, c, k);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.threshold.localBernsen.LocalBernsen.class)
	public <T extends RealType<T>> BitType localBernsen(final BitType out,
		final T center, final Iterable<T> neighborhood, final double contrastThreshold,
		final double halfMaxValue)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localBernsen.LocalBernsen.class, out, center,
			neighborhood, contrastThreshold, halfMaxValue);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <T extends RealType<T>> BitType localPhansalkar(final BitType out,
		final T center, final Iterable<T> neighborhood, final double k, final double r)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class, out,
			center, neighborhood, k, r);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <T extends RealType<T>> BitType localPhansalkar(final BitType out,
		final T center, final Iterable<T> neighborhood, final double k)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class, out,
			center, neighborhood, k);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class)
	public <T extends RealType<T>> BitType localPhansalkar(final BitType out,
		final T center, final Iterable<T> neighborhood)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar.class, out,
			center, neighborhood);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <T extends RealType<T>> BitType localSauvola(final BitType out,
		final T center, final Iterable<T> neighborhood, final double k, final double r)
	{
		final BitType result =
			(BitType) ops().run(net.imagej.ops.threshold.localSauvola.LocalSauvola.class,
				out, center, neighborhood, k, r);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <T extends RealType<T>> BitType localSauvola(final BitType out,
		final T center, final Iterable<T> neighborhood, final double k)
	{
		final BitType result = (BitType) ops().run(
			net.imagej.ops.threshold.localSauvola.LocalSauvola.class, out, center,
			neighborhood, k);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.threshold.localSauvola.LocalSauvola.class)
	public <T extends RealType<T>> BitType localSauvola(final BitType out,
		final T center, final Iterable<T> neighborhood)
	{
		final BitType result =
			(BitType) ops().run(net.imagej.ops.threshold.localSauvola.LocalSauvola.class,
				out, center, neighborhood);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.MaxEntropy.class)
	public Object maxEntropy(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.MaxEntropy.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.MaxEntropy.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> maxEntropy(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.MaxEntropy.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.MaxEntropy.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> maxEntropy(final IterableInterval<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.MaxEntropy.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.maxEntropy.ComputeMaxEntropyThreshold.class)
	public
		<T extends RealType<T>> T maxEntropy(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.maxEntropy.ComputeMaxEntropyThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.maxEntropy.ComputeMaxEntropyThreshold.class)
	public
		<T extends RealType<T>> T maxEntropy(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.maxEntropy.ComputeMaxEntropyThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.MaxLikelihood.class)
	public Object maxLikelihood(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.MaxLikelihood.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.MaxLikelihood.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> maxLikelihood(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops()
				.run(
					net.imagej.ops.threshold.ApplyThresholdMethod.MaxLikelihood.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.MaxLikelihood.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> maxLikelihood(final IterableInterval<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops()
				.run(
					net.imagej.ops.threshold.ApplyThresholdMethod.MaxLikelihood.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.maxLikelihood.ComputeMaxLikelihoodThreshold.class)
	public
		<T extends RealType<T>> List<Object> maxLikelihood(final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops()
				.run(
					net.imagej.ops.threshold.maxLikelihood.ComputeMaxLikelihoodThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.maxLikelihood.ComputeMaxLikelihoodThreshold.class)
	public
		<T extends RealType<T>> List<Object> maxLikelihood(final T out,
			final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops()
				.run(
					net.imagej.ops.threshold.maxLikelihood.ComputeMaxLikelihoodThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Mean.class)
	public Object mean(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Mean.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Mean.class)
	public <T extends RealType<T>> IterableInterval<BitType> mean(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Mean.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Mean.class)
	public <T extends RealType<T>> IterableInterval<BitType> mean(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Mean.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.mean.ComputeMeanThreshold.class)
	public <T extends RealType<T>> T mean(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.mean.ComputeMeanThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.mean.ComputeMeanThreshold.class)
	public <T extends RealType<T>> T mean(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.mean.ComputeMeanThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.MinError.class)
	public Object minError(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.MinError.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.MinError.class)
	public <T extends RealType<T>> IterableInterval<BitType> minError(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.MinError.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.MinError.class)
	public <T extends RealType<T>> IterableInterval<BitType> minError(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.MinError.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.minError.ComputeMinErrorThreshold.class)
	public
		<T extends RealType<T>> List<Object> minError(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.minError.ComputeMinErrorThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.minError.ComputeMinErrorThreshold.class)
	public
		<T extends RealType<T>> List<Object> minError(final T out,
			final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.minError.ComputeMinErrorThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Minimum.class)
	public Object minimum(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Minimum.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Minimum.class)
	public <T extends RealType<T>> IterableInterval<BitType> minimum(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops()
				.run(
					net.imagej.ops.threshold.ApplyThresholdMethod.Minimum.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Minimum.class)
	public <T extends RealType<T>> IterableInterval<BitType> minimum(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Minimum.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.minimum.ComputeMinimumThreshold.class)
	public <T extends RealType<T>> List<Object> minimum(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.minimum.ComputeMinimumThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.minimum.ComputeMinimumThreshold.class)
	public <T extends RealType<T>> List<Object> minimum(final T out,
		final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.minimum.ComputeMinimumThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Moments.class)
	public Object moments(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Moments.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.moments.ComputeMomentsThreshold.class)
	public <T extends RealType<T>> T moments(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.moments.ComputeMomentsThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.moments.ComputeMomentsThreshold.class)
	public <T extends RealType<T>> T
		moments(final T out, final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.moments.ComputeMomentsThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Moments.class)
	public <T extends RealType<T>> IterableInterval<BitType> moments(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops()
				.run(
					net.imagej.ops.threshold.ApplyThresholdMethod.Moments.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Moments.class)
	public <T extends RealType<T>> IterableInterval<BitType> moments(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Moments.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Otsu.class)
	public Object otsu(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Otsu.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.otsu.ComputeOtsuThreshold.class)
	public <T extends RealType<T>> T otsu(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.otsu.ComputeOtsuThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.otsu.ComputeOtsuThreshold.class)
	public <T extends RealType<T>> T otsu(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.otsu.ComputeOtsuThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Otsu.class)
	public <T extends RealType<T>> IterableInterval<BitType> otsu(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Otsu.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Otsu.class)
	public <T extends RealType<T>> IterableInterval<BitType> otsu(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Otsu.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Percentile.class)
	public Object percentile(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Percentile.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Percentile.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> percentile(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Percentile.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Percentile.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> percentile(final IterableInterval<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Percentile.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.percentile.ComputePercentileThreshold.class)
	public
		<T extends RealType<T>> T percentile(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.percentile.ComputePercentileThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.percentile.ComputePercentileThreshold.class)
	public
		<T extends RealType<T>> T percentile(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.percentile.ComputePercentileThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.RenyiEntropy.class)
	public Object renyiEntropy(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.RenyiEntropy.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.RenyiEntropy.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> renyiEntropy(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops()
				.run(
					net.imagej.ops.threshold.ApplyThresholdMethod.RenyiEntropy.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.RenyiEntropy.class)
	public
		<T extends RealType<T>> IterableInterval<BitType> renyiEntropy(final IterableInterval<BitType> out,
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops()
				.run(
					net.imagej.ops.threshold.ApplyThresholdMethod.RenyiEntropy.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.renyiEntropy.ComputeRenyiEntropyThreshold.class)
	public
		<T extends RealType<T>> T renyiEntropy(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.renyiEntropy.ComputeRenyiEntropyThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.renyiEntropy.ComputeRenyiEntropyThreshold.class)
	public
		<T extends RealType<T>>
		T
		renyiEntropy(final T out, final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.renyiEntropy.ComputeRenyiEntropyThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Shanbhag.class)
	public Object shanbhag(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Shanbhag.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Shanbhag.class)
	public <T extends RealType<T>> IterableInterval<BitType> shanbhag(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Shanbhag.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Shanbhag.class)
	public <T extends RealType<T>> IterableInterval<BitType> shanbhag(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Shanbhag.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.shanbhag.ComputeShanbhagThreshold.class)
	public
		<T extends RealType<T>> T shanbhag(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.shanbhag.ComputeShanbhagThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.shanbhag.ComputeShanbhagThreshold.class)
	public
		<T extends RealType<T>> T shanbhag(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.shanbhag.ComputeShanbhagThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Triangle.class)
	public Object triangle(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Triangle.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Triangle.class)
	public <T extends RealType<T>> IterableInterval<BitType> triangle(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Triangle.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Triangle.class)
	public <T extends RealType<T>> IterableInterval<BitType> triangle(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Triangle.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.triangle.ComputeTriangleThreshold.class)
	public
		<T extends RealType<T>> T triangle(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.triangle.ComputeTriangleThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.triangle.ComputeTriangleThreshold.class)
	public
		<T extends RealType<T>> T triangle(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.triangle.ComputeTriangleThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Threshold.Yen.class)
	public Object yen(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Threshold.Yen.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.yen.ComputeYenThreshold.class)
	public <T extends RealType<T>> T yen(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.yen.ComputeYenThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.yen.ComputeYenThreshold.class)
	public <T extends RealType<T>> T yen(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.yen.ComputeYenThreshold.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Yen.class)
	public <T extends RealType<T>> IterableInterval<BitType> yen(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Yen.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.ApplyThresholdMethod.Yen.class)
	public <T extends RealType<T>> IterableInterval<BitType> yen(final IterableInterval<BitType> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<BitType> result =
			(IterableInterval<BitType>) ops().run(
				net.imagej.ops.threshold.ApplyThresholdMethod.Yen.class, out,
				in);
		return result;
	}
	
	// -- Named methods --

	@Override
	public String getName() {
		return "threshold";
	}
}
