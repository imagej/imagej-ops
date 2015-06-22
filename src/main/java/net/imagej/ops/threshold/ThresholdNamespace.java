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

package net.imagej.ops.threshold;

import java.util.List;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.OpMethod;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

/**
 * The threshold namespace contains operations related to binary thresholding.
 *
 * @author Curtis Rueden
 */
public class ThresholdNamespace extends AbstractNamespace {

	// -- Threshold namespace ops --

	@OpMethod(op = net.imagej.ops.ThresholdOps.Huang.class)
	public Object huang(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Huang.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeHuangThreshold.class)
	public <T extends RealType<T>> T huang(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeHuangThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeHuangThreshold.class)
	public <T extends RealType<T>> T huang(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeHuangThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyHuangThreshold.class)
	public <T extends RealType<T>> Img<BitType> huang(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyHuangThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyHuangThreshold.class)
	public <T extends RealType<T>> Img<BitType> huang(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyHuangThreshold.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Intermodes.class)
	public Object intermodes(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Intermodes.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeIntermodesThreshold.class)
	public
		<T extends RealType<T>> List<Object> intermodes(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeIntermodesThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeIntermodesThreshold.class)
	public
		<T extends RealType<T>> List<Object> intermodes(final T out,
			final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeIntermodesThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyIntermodesThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> intermodes(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyIntermodesThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyIntermodesThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> intermodes(final Img<BitType> out,
			final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyIntermodesThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.IsoData.class)
	public Object isodata(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.IsoData.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyIsoDataThreshold.class)
	public <T extends RealType<T>> Img<BitType> isodata(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ApplyIsoDataThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyIsoDataThreshold.class)
	public <T extends RealType<T>> Img<BitType> isodata(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyIsoDataThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeIsoDataThreshold.class)
	public <T extends RealType<T>> List<Object> isodata(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeIsoDataThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeIsoDataThreshold.class)
	public <T extends RealType<T>> List<Object> isodata(final T out,
		final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeIsoDataThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Li.class)
	public Object li(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Li.class, args);
	}

	@OpMethod(op = net.imagej.ops.threshold.global.methods.ApplyLiThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> li(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyLiThreshold.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.threshold.global.methods.ApplyLiThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> li(final Img<BitType> out,
			final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops()
				.run(net.imagej.ops.threshold.global.methods.ApplyLiThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeLiThreshold.class)
	public <T extends RealType<T>> T li(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeLiThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeLiThreshold.class)
	public <T extends RealType<T>> T li(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeLiThreshold.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.MaxEntropy.class)
	public Object maxEntropy(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.MaxEntropy.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMaxEntropyThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> maxEntropy(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyMaxEntropyThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMaxEntropyThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> maxEntropy(final Img<BitType> out,
			final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyMaxEntropyThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMaxEntropyThreshold.class)
	public
		<T extends RealType<T>> T maxEntropy(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeMaxEntropyThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMaxEntropyThreshold.class)
	public
		<T extends RealType<T>> T maxEntropy(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeMaxEntropyThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.MaxLikelihood.class)
	public Object maxlikelihood(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.MaxLikelihood.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMaxLikelihoodThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> maxlikelihood(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ApplyMaxLikelihoodThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMaxLikelihoodThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> maxlikelihood(final Img<BitType> out,
			final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ApplyMaxLikelihoodThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMaxLikelihoodThreshold.class)
	public
		<T extends RealType<T>> List<Object> maxlikelihood(final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeMaxLikelihoodThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMaxLikelihoodThreshold.class)
	public
		<T extends RealType<T>> List<Object> maxlikelihood(final T out,
			final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeMaxLikelihoodThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Mean.class)
	public Object mean(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Mean.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMeanThreshold.class)
	public <T extends RealType<T>> Img<BitType> mean(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyMeanThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMeanThreshold.class)
	public <T extends RealType<T>> Img<BitType> mean(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyMeanThreshold.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMeanThreshold.class)
	public <T extends RealType<T>> T mean(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeMeanThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMeanThreshold.class)
	public <T extends RealType<T>> T mean(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeMeanThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.MinError.class)
	public Object minerror(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.MinError.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMinErrorThreshold.class)
	public <T extends RealType<T>> Img<BitType> minerror(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyMinErrorThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMinErrorThreshold.class)
	public <T extends RealType<T>> Img<BitType> minerror(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyMinErrorThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMinErrorThreshold.class)
	public
		<T extends RealType<T>> List<Object> minerror(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeMinErrorThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMinErrorThreshold.class)
	public
		<T extends RealType<T>> List<Object> minerror(final T out,
			final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeMinErrorThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Minimum.class)
	public Object minimum(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Minimum.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMinimumThreshold.class)
	public <T extends RealType<T>> Img<BitType> minimum(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ApplyMinimumThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMinimumThreshold.class)
	public <T extends RealType<T>> Img<BitType> minimum(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyMinimumThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMinimumThreshold.class)
	public <T extends RealType<T>> List<Object> minimum(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeMinimumThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMinimumThreshold.class)
	public <T extends RealType<T>> List<Object> minimum(final T out,
		final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final List<Object> result =
			(List<Object>) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeMinimumThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Moments.class)
	public Object moments(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Moments.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMomentsThreshold.class)
	public <T extends RealType<T>> T moments(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeMomentsThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeMomentsThreshold.class)
	public <T extends RealType<T>> T
		moments(final T out, final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeMomentsThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMomentsThreshold.class)
	public <T extends RealType<T>> Img<BitType> moments(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ApplyMomentsThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyMomentsThreshold.class)
	public <T extends RealType<T>> Img<BitType> moments(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyMomentsThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Otsu.class)
	public Object otsu(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Otsu.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeOtsuThreshold.class)
	public <T extends RealType<T>> T otsu(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeOtsuThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeOtsuThreshold.class)
	public <T extends RealType<T>> T otsu(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeOtsuThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyOtsuThreshold.class)
	public <T extends RealType<T>> Img<BitType> otsu(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyOtsuThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyOtsuThreshold.class)
	public <T extends RealType<T>> Img<BitType> otsu(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyOtsuThreshold.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Percentile.class)
	public Object percentile(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Percentile.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyPercentileThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> percentile(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyPercentileThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyPercentileThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> percentile(final Img<BitType> out,
			final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyPercentileThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputePercentileThreshold.class)
	public
		<T extends RealType<T>> T percentile(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputePercentileThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputePercentileThreshold.class)
	public
		<T extends RealType<T>> T percentile(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputePercentileThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.RenyiEntropy.class)
	public Object renyientropy(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.RenyiEntropy.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyRenyiEntropyThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> renyientropy(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ApplyRenyiEntropyThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyRenyiEntropyThreshold.class)
	public
		<T extends RealType<T>> Img<BitType> renyientropy(final Img<BitType> out,
			final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ApplyRenyiEntropyThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeRenyiEntropyThreshold.class)
	public
		<T extends RealType<T>> T renyientropy(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeRenyiEntropyThreshold.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeRenyiEntropyThreshold.class)
	public
		<T extends RealType<T>>
		T
		renyientropy(final T out, final Histogram1d<T> in)
	{
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops()
				.run(
					net.imagej.ops.threshold.global.methods.ComputeRenyiEntropyThreshold.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Shanbhag.class)
	public Object shanbhag(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Shanbhag.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyShanbhagThreshold.class)
	public <T extends RealType<T>> Img<BitType> shanbhag(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyShanbhagThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyShanbhagThreshold.class)
	public <T extends RealType<T>> Img<BitType> shanbhag(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyShanbhagThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeShanbhagThreshold.class)
	public
		<T extends RealType<T>> T shanbhag(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeShanbhagThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeShanbhagThreshold.class)
	public
		<T extends RealType<T>> T shanbhag(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeShanbhagThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Triangle.class)
	public Object triangle(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Triangle.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyTriangleThreshold.class)
	public <T extends RealType<T>> Img<BitType> triangle(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyTriangleThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyTriangleThreshold.class)
	public <T extends RealType<T>> Img<BitType> triangle(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyTriangleThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeTriangleThreshold.class)
	public
		<T extends RealType<T>> T triangle(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeTriangleThreshold.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeTriangleThreshold.class)
	public
		<T extends RealType<T>> T triangle(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeTriangleThreshold.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.ThresholdOps.Yen.class)
	public Object yen(final Object... args) {
		return ops().run(net.imagej.ops.ThresholdOps.Yen.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeYenThreshold.class)
	public <T extends RealType<T>> T yen(final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeYenThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ComputeYenThreshold.class)
	public <T extends RealType<T>> T yen(final T out, final Histogram1d<T> in) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(
				net.imagej.ops.threshold.global.methods.ComputeYenThreshold.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyYenThreshold.class)
	public <T extends RealType<T>> Img<BitType> yen(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyYenThreshold.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.threshold.global.methods.ApplyYenThreshold.class)
	public <T extends RealType<T>> Img<BitType> yen(final Img<BitType> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<BitType> result =
			(Img<BitType>) ops().run(
				net.imagej.ops.threshold.global.methods.ApplyYenThreshold.class, out,
				in);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "threshold";
	}
}
