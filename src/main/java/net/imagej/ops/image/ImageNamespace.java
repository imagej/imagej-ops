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

package net.imagej.ops.image;

import net.imagej.ImgPlus;
import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imagej.ops.image.cooccurrencematrix.MatrixOrientation;
import net.imagej.ops.special.UnaryComputerOp;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.img.Img;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * The image namespace contains operations relating to images.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class ImageNamespace extends AbstractNamespace {

	// -- ascii --

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = Ops.Image.ASCII.class)
	public Object ascii(final Object... args) {
		return ops().run(Ops.Image.ASCII.NAME, args);
	}

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.ascii.DefaultASCII.class)
	public <T extends RealType<T>> String ascii(final IterableInterval<T> image) {
		final String result = (String) ops().run(
				net.imagej.ops.image.ascii.DefaultASCII.class, image);
		return result;
	}

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.ascii.DefaultASCII.class)
	public <T extends RealType<T>> String ascii(
			final IterableInterval<T> image, final RealType<T> min) {
		final String result = (String) ops().run(
				net.imagej.ops.image.ascii.DefaultASCII.class, image, min);
		return result;
	}

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.ascii.DefaultASCII.class)
	public <T extends RealType<T>> String ascii(
			final IterableInterval<T> image, final RealType<T> min,
			final RealType<T> max) {
		final String result = (String) ops().run(
				net.imagej.ops.image.ascii.DefaultASCII.class, image, min, max);
		return result;
	}

	// -- cooccurrence matrix --

	@OpMethod(ops = {
			net.imagej.ops.image.cooccurrencematrix.CooccurrenceMatrix3D.class,
			net.imagej.ops.image.cooccurrencematrix.CooccurrenceMatrix2D.class })
	public <T extends RealType<T>> double[][] cooccurrencematrix(
			final IterableInterval<T> in, final int nrGreyLevels,
			final int distance, final MatrixOrientation orientation) {
		final double[][] result = (double[][]) ops().run(
				Ops.Image.CooccurrenceMatrix.class, in, nrGreyLevels, distance,
				orientation);
		return result;
	}

	// -- crop --

	/** Executes the "crop" operation on the given arguments. */
	@OpMethod(op = Ops.Image.Crop.class)
	public Object crop(final Object... args) {
		return ops().run(Ops.Image.Crop.NAME, args);
	}

	/** Executes the "crop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.crop.CropImgPlus.class)
	public <T extends Type<T>> ImgPlus<T> crop(final ImgPlus<T> in,
			final Interval interval) {
		@SuppressWarnings("unchecked")
		final ImgPlus<T> result = (ImgPlus<T>) ops().run(
				net.imagej.ops.image.crop.CropImgPlus.class, in, interval);
		return result;
	}

	/** Executes the "crop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.crop.CropImgPlus.class)
	public <T extends Type<T>> ImgPlus<T> crop(final ImgPlus<T> in,
			final Interval interval, final boolean dropSingleDimensions) {
		@SuppressWarnings("unchecked")
		final ImgPlus<T> result = (ImgPlus<T>) ops().run(
				net.imagej.ops.image.crop.CropImgPlus.class, in, interval,
				dropSingleDimensions);
		return result;
	}

	/** Executes the "crop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.crop.CropRAI.class)
	public <T> RandomAccessibleInterval<T> crop(
			final RandomAccessibleInterval<T> in, final Interval interval) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.image.crop.CropRAI.class, in, interval);
		return result;
	}

	/** Executes the "crop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.crop.CropRAI.class)
	public <T> RandomAccessibleInterval<T> crop(
			final RandomAccessibleInterval<T> in, final Interval interval,
			final boolean dropSingleDimensions) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.image.crop.CropRAI.class, in, interval,
						dropSingleDimensions);
		return result;
	}

	// -- equation --

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = Ops.Image.Equation.class)
	public Object equation(final Object... args) {
		return ops().run(Ops.Image.Equation.NAME, args);
	}

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.equation.DefaultEquation.class)
	public <T extends RealType<T>> IterableInterval<T> equation(final String in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
				net.imagej.ops.image.equation.DefaultEquation.class, in);
		return result;
	}

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.equation.DefaultEquation.class)
	public <T extends RealType<T>> IterableInterval<T> equation(
			final IterableInterval<T> out, final String in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
				net.imagej.ops.image.equation.DefaultEquation.class, out, in);
		return result;
	}

	// -- histogram --

	/** Executes the "histogram" operation on the given arguments. */
	@OpMethod(op = Ops.Image.Histogram.class)
	public Object histogram(final Object... args) {
		return ops().run(Ops.Image.Histogram.NAME, args);
	}

	/** Executes the "histogram" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.histogram.HistogramCreate.class)
	public <T extends RealType<T>> Histogram1d<T> histogram(final Iterable<T> in) {
		@SuppressWarnings("unchecked")
		final Histogram1d<T> result = (Histogram1d<T>) ops().run(
				net.imagej.ops.image.histogram.HistogramCreate.class, in);
		return result;
	}

	/** Executes the "histogram" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.histogram.HistogramCreate.class)
	public <T extends RealType<T>> Histogram1d<T> histogram(
			final Iterable<T> in, final int numBins) {
		@SuppressWarnings("unchecked")
		final Histogram1d<T> result = (Histogram1d<T>) ops().run(
				net.imagej.ops.image.histogram.HistogramCreate.class, in,
				numBins);
		return result;
	}

	// -- invert --

	/** Executes the "invert" operation on the given arguments. */
	@OpMethod(op = Ops.Image.Invert.class)
	public Object invert(final Object... args) {
		return ops().run(Ops.Image.Invert.NAME, args);
	}

	/** Executes the "invert" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.invert.InvertIterableInterval.class)
	public <I extends RealType<I>, O extends RealType<O>> IterableInterval<O> invert(
			final IterableInterval<O> out, final IterableInterval<I> in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
				net.imagej.ops.image.invert.InvertIterableInterval.class, out,
				in);
		return result;
	}

	// -- normalize --

	/** Executes the "normalize" operation on the given arguments. */
	@OpMethod(op = Ops.Image.Normalize.class)
	public Object normalize(final Object... args) {
		return ops().run(Ops.Image.Normalize.NAME, args);
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIterableComputer.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> out, final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops()
				.run(net.imagej.ops.image.normalize.NormalizeIterableComputer.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIterableComputer.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> out, final IterableInterval<T> in,
			final T sourceMin)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableComputer.class, out,
				in, sourceMin);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIterableComputer.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> out, final IterableInterval<T> in,
			final T sourceMin, final T sourceMax)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableComputer.class, out,
				in, sourceMin, sourceMax);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIterableComputer.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> out, final IterableInterval<T> in,
			final T sourceMin, final T sourceMax, final T targetMin)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableComputer.class, out,
				in, sourceMin, sourceMax, targetMin);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIterableComputer.class)
	public
		<T extends RealType<T>>
		IterableInterval<T>
		normalize(final IterableInterval<T> out, final IterableInterval<T> in,
			final T sourceMin, final T sourceMax, final T targetMin, final T targetMax)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableComputer.class, out,
				in, sourceMin, sourceMax, targetMin, targetMax);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> in, final T sourceMin)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class,
				in, sourceMin);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> in, final T sourceMin, final T sourceMax)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class,
				in, sourceMin, sourceMax);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> in, final T sourceMin, final T sourceMax,
			final T targetMin)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class,
				in, sourceMin, sourceMax, targetMin);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> in, final T sourceMin, final T sourceMax,
			final T targetMin, final T targetMax)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class,
				in, sourceMin, sourceMax, targetMin, targetMax);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class)
	public
		<T extends RealType<T>> IterableInterval<T> normalize(
			final IterableInterval<T> in, final T sourceMin, final T sourceMax,
			final T targetMin, final T targetMax, final boolean isLazy)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(
				net.imagej.ops.image.normalize.NormalizeIterableIntervalFunction.class,
				in, sourceMin, sourceMax, targetMin, targetMax, isLazy);
		return result;
	}

	// -- project --

	/** Executes the "project" operation on the given arguments. */
	@OpMethod(op = Ops.Image.Project.class)
	public Object project(final Object... args) {
		return ops().run(Ops.Image.Project.NAME, args);
	}

	/** Executes the "project" operation on the given arguments. */
	@OpMethod(ops = {
			net.imagej.ops.image.project.DefaultProjectParallel.class,
			net.imagej.ops.image.project.ProjectRAIToIterableInterval.class })
	public <T, V> IterableInterval<V> project(final IterableInterval<V> out,
			final RandomAccessibleInterval<T> in,
			final UnaryComputerOp<Iterable<T>, V> method, final int dim) {
		@SuppressWarnings("unchecked")
		final IterableInterval<V> result = (IterableInterval<V>) ops().run(
				net.imagej.ops.Ops.Image.Project.class, out, in, method, dim);
		return result;
	}

	// -- scale --

	/** Executes the "scale" operation on the given arguments. */
	@OpMethod(op = Ops.Image.Scale.class)
	public Object scale(final Object... args) {
		return ops().run(Ops.Image.Scale.NAME, args);
	}

	/** Executes the "scale" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.scale.ScaleImg.class)
	public <T extends RealType<T>> Img<T> scale(final Img<T> in,
			final double[] scaleFactors,
			final InterpolatorFactory<T, RandomAccessible<T>> interpolator) {
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(
				net.imagej.ops.image.scale.ScaleImg.class, in, scaleFactors,
				interpolator);
		return result;
	}

	@Override
	public String getName() {
		return "image";
	}

}
