/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

import java.util.function.DoubleBinaryOperator;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imagej.ops.image.cooccurrenceMatrix.MatrixOrientation;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.BooleanType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;

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
	@OpMethod(op = net.imagej.ops.image.ascii.DefaultASCII.class)
	public <T extends RealType<T>> String ascii(final IterableInterval<T> image) {
		final String result = (String) ops().run(
			net.imagej.ops.Ops.Image.ASCII.class, image);
		return result;
	}

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.ascii.DefaultASCII.class)
	public <T extends RealType<T>> String ascii(final IterableInterval<T> image,
		final T min)
	{
		final String result = (String) ops().run(
			net.imagej.ops.Ops.Image.ASCII.class, image, min);
		return result;
	}

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.ascii.DefaultASCII.class)
	public <T extends RealType<T>> String ascii(final IterableInterval<T> image,
		final T min, final T max)
	{
		final String result = (String) ops().run(
			net.imagej.ops.Ops.Image.ASCII.class, image, min, max);
		return result;
	}

	// -- cooccurrence matrix --

	@OpMethod(ops = {
		net.imagej.ops.image.cooccurrenceMatrix.CooccurrenceMatrix3D.class,
		net.imagej.ops.image.cooccurrenceMatrix.CooccurrenceMatrix2D.class })
	public <T extends RealType<T>> double[][] cooccurrenceMatrix(
		final IterableInterval<T> in, final int nrGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final double[][] result = (double[][]) ops().run(
			Ops.Image.CooccurrenceMatrix.class, in, nrGreyLevels, distance,
			orientation);
		return result;
	}

	// -- distance transform --

	/** Executes the "distancetransform" operation on the given arguments. */
	@OpMethod(ops = {
		net.imagej.ops.image.distancetransform.DefaultDistanceTransform.class,
		net.imagej.ops.image.distancetransform.DistanceTransform2D.class,
		net.imagej.ops.image.distancetransform.DistanceTransform3D.class })
	public <B extends BooleanType<B>, T extends RealType<T>>
		RandomAccessibleInterval<T> distancetransform(
			final RandomAccessibleInterval<B> in,
			final RandomAccessibleInterval<T> out)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Image.DistanceTransform.class,
				in, out);
		return result;
	}

	/** Executes the "distancetransform" operation on the given arguments. */
	@OpMethod(ops = {
		net.imagej.ops.image.distancetransform.DefaultDistanceTransform.class,
		net.imagej.ops.image.distancetransform.DistanceTransform2D.class,
		net.imagej.ops.image.distancetransform.DistanceTransform3D.class })
	public <B extends BooleanType<B>, T extends RealType<T>>
		RandomAccessibleInterval<T> distancetransform(
			final RandomAccessibleInterval<B> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Image.DistanceTransform.class,
				in);
		return result;
	}

	/** Executes the "distancetransform" operation on the given arguments. */
	@OpMethod(ops = {
		net.imagej.ops.image.distancetransform.DefaultDistanceTransformCalibration.class,
		net.imagej.ops.image.distancetransform.DistanceTransform2DCalibration.class,
		net.imagej.ops.image.distancetransform.DistanceTransform3DCalibration.class })
	public <B extends BooleanType<B>, T extends RealType<T>>
		RandomAccessibleInterval<T> distancetransform(
			final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<B> in, final double... calibration)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Image.DistanceTransform.class,
				out, in, calibration);
		return result;
	}

	/** Executes the "distancetransform" operation on the given arguments. */
	@OpMethod(ops = {
		net.imagej.ops.image.distancetransform.DefaultDistanceTransformCalibration.class,
		net.imagej.ops.image.distancetransform.DistanceTransform2DCalibration.class,
		net.imagej.ops.image.distancetransform.DistanceTransform3DCalibration.class })
	public <B extends BooleanType<B>, T extends RealType<T>>
		RandomAccessibleInterval<T> distancetransform(
			final RandomAccessibleInterval<B> in, final double... calibration)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(Ops.Image.DistanceTransform.class,
				in, calibration);
		return result;
	}

	// -- equation --

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.equation.DefaultEquation.class)
	public <T extends RealType<T>> IterableInterval<T> equation(final String in) {
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Equation.class, in);
		return result;
	}

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.equation.DefaultEquation.class)
	public <T extends RealType<T>> IterableInterval<T> equation(
		final IterableInterval<T> out, final String in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Equation.class, out, in);
		return result;
	}

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.equation.DefaultCoordinatesEquation.class)
	public <T extends RealType<T>> IterableInterval<T> equation(
		final IterableInterval<T> out, final UnaryFunctionOp<long[], Double> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Equation.class, out, in);
		return result;
	}

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.equation.DefaultXYEquation.class)
	public <T extends RealType<T>> IterableInterval<T> equation(
		final IterableInterval<T> out, final DoubleBinaryOperator in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Equation.class, out, in);
		return result;
	}

	// -- fill --

	/** Executes the "fill" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.fill.DefaultFill.class)
	public <T extends Type<T>> Iterable<T> fill(final Iterable<T> out,
		final T in)
	{
		@SuppressWarnings("unchecked")
		final Iterable<T> result = (Iterable<T>) ops().run(
			net.imagej.ops.Ops.Image.Fill.class, out, in);
		return result;
	}

	// -- histogram --

	/** Executes the "histogram" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.histogram.HistogramCreate.class)
	public <T extends RealType<T>> Histogram1d<T> histogram(
		final Iterable<T> in)
	{
		@SuppressWarnings("unchecked")
		final Histogram1d<T> result = (Histogram1d<T>) ops().run(
			net.imagej.ops.Ops.Image.Histogram.class, in);
		return result;
	}

	/** Executes the "histogram" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.histogram.HistogramCreate.class)
	public <T extends RealType<T>> Histogram1d<T> histogram(final Iterable<T> in,
		final int numBins)
	{
		@SuppressWarnings("unchecked")
		final Histogram1d<T> result = (Histogram1d<T>) ops().run(
			net.imagej.ops.Ops.Image.Histogram.class, in, numBins);
		return result;
	}

	// -- integral --

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@OpMethod(op = net.imagej.ops.image.integral.DefaultIntegralImg.class)
	public <T extends RealType<T>> RandomAccessibleInterval<RealType> integral(
		final RandomAccessibleInterval<RealType> out,
		final RandomAccessibleInterval<T> in)
	{
		final RandomAccessibleInterval<RealType> result =
			(RandomAccessibleInterval) ops().run(
				net.imagej.ops.image.integral.DefaultIntegralImg.class, out, in);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@OpMethod(ops = { net.imagej.ops.image.integral.DefaultIntegralImg.class,
		net.imagej.ops.image.integral.WrappedIntegralImg.class })
	public <T extends RealType<T>> RandomAccessibleInterval<RealType> integral(
		final RandomAccessibleInterval<T> in)
	{
		final RandomAccessibleInterval<RealType> result =
			(RandomAccessibleInterval) ops().run(Ops.Image.Integral.class, in);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@OpMethod(op = net.imagej.ops.image.integral.SquareIntegralImg.class)
	public <T extends RealType<T>> RandomAccessibleInterval<RealType>
		squareIntegral(final RandomAccessibleInterval<RealType> out,
			final RandomAccessibleInterval<T> in)
	{
		final RandomAccessibleInterval<RealType> result =
			(RandomAccessibleInterval) ops().run(Ops.Image.SquareIntegral.class, out,
				in);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@OpMethod(op = net.imagej.ops.image.integral.SquareIntegralImg.class)
	public <T extends RealType<T>> RandomAccessibleInterval<RealType>
		squareIntegral(final RandomAccessibleInterval<T> in)
	{
		final RandomAccessibleInterval<RealType> result =
			(RandomAccessibleInterval) ops().run(Ops.Image.SquareIntegral.class, in);
		return result;
	}

	// -- invert --

	/** Executes the "invert" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.image.invert.InvertII.class,
		net.imagej.ops.image.invert.InvertIIInteger.class }, skipTypeCheck = true)
	public <T extends RealType<T>> IterableInterval<T> invert(
		final IterableInterval<T> out, final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Invert.class, out, in);
		return result;
	}

	/**
	 * Executes the "invert" operation on the given arguments, given a
	 * {@link RealType} minimum.
	 */
	@OpMethod(ops = { net.imagej.ops.image.invert.InvertII.class,
		net.imagej.ops.image.invert.InvertIIInteger.class }, skipTypeCheck = true)
	public <T extends RealType<T>> IterableInterval<T> invert(
		final IterableInterval<T> out, final IterableInterval<T> in, final T min)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Invert.class, out, in, min);
		return result;
	}

	/**
	 * Executes the "invert" operation on the given arguments, given a
	 * {@link RealType} minimum.
	 */
	@OpMethod(ops = { net.imagej.ops.image.invert.InvertII.class,
		net.imagej.ops.image.invert.InvertIIInteger.class }, skipTypeCheck = true)
	public <T extends RealType<T>> IterableInterval<T> invert(
		final IterableInterval<T> out, final IterableInterval<T> in, final T min,
		final T max)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Invert.class, out, in, min, max);
		return result;
	}

	// -- normalize --

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIComputer.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> out, final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIComputer.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> out, final IterableInterval<T> in,
		final T sourceMin)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, out, in, sourceMin);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIComputer.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> out, final IterableInterval<T> in,
		final T sourceMin, final T sourceMax)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, out, in, sourceMin, sourceMax);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIComputer.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> out, final IterableInterval<T> in,
		final T sourceMin, final T sourceMax, final T targetMin)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, out, in, sourceMin, sourceMax,
			targetMin);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIComputer.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> out, final IterableInterval<T> in,
		final T sourceMin, final T sourceMax, final T targetMin, final T targetMax)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, out, in, sourceMin, sourceMax,
			targetMin, targetMax);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIFunction.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIFunction.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> in, final T sourceMin)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, in, sourceMin);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIFunction.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> in, final T sourceMin, final T sourceMax)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, in, sourceMin, sourceMax);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIFunction.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> in, final T sourceMin, final T sourceMax,
		final T targetMin)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, in, sourceMin, sourceMax,
			targetMin);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIFunction.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> in, final T sourceMin, final T sourceMax,
		final T targetMin, final T targetMax)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, in, sourceMin, sourceMax,
			targetMin, targetMax);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.normalize.NormalizeIIFunction.class)
	public <T extends RealType<T>> IterableInterval<T> normalize(
		final IterableInterval<T> in, final T sourceMin, final T sourceMax,
		final T targetMin, final T targetMax, final boolean isLazy)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Image.Normalize.class, in, sourceMin, sourceMax,
			targetMin, targetMax, isLazy);
		return result;
	}

	// -- watershed --

	/** Executes the "watershed" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.watershed.Watershed.class)
	public <T extends RealType<T>> ImgLabeling<Integer, IntType> watershed(
		final ImgLabeling<Integer, IntType> out,
		final RandomAccessibleInterval<T> in, final boolean eightConnectivity,
		final boolean drawWatersheds)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.Watershed.class, out, in,
				eightConnectivity, drawWatersheds);
		return result;
	}

	/** Executes the "watershed" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.watershed.Watershed.class)
	public <T extends RealType<T>> ImgLabeling<Integer, IntType> watershed(
		final RandomAccessibleInterval<T> in, final boolean eightConnectivity,
		final boolean drawWatersheds)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.Watershed.class, in, eightConnectivity,
				drawWatersheds);
		return result;
	}

	/** Executes the "watershed" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.watershed.Watershed.class)
	public <B extends BooleanType<B>, T extends RealType<T>>
		ImgLabeling<Integer, IntType> watershed(
			final ImgLabeling<Integer, IntType> out,
			final RandomAccessibleInterval<T> in, final boolean eightConnectivity,
			final boolean drawWatersheds, final RandomAccessibleInterval<B> mask)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.Watershed.class, out, in,
				eightConnectivity, drawWatersheds, mask);
		return result;
	}

	// -- watershedBinary --

	@OpMethod(op = net.imagej.ops.image.watershed.WatershedBinary.class)
	public <B extends BooleanType<B>> ImgLabeling<Integer, IntType> watershed(
		final RandomAccessibleInterval<B> in, final boolean eightConnectivity,
		final boolean drawWatersheds, final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedBinary.class, in,
				eightConnectivity, drawWatersheds, sigma);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.watershed.WatershedBinary.class)
	public <B extends BooleanType<B>> ImgLabeling<Integer, IntType> watershed(
		final ImgLabeling<Integer, IntType> out,
		final RandomAccessibleInterval<B> in, final boolean eightConnectivity,
		final boolean drawWatersheds, final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedBinary.class, out, in,
				eightConnectivity, drawWatersheds, sigma);
		return result;
	}

	@OpMethod(op = net.imagej.ops.image.watershed.WatershedBinary.class)
	public <B extends BooleanType<B>> ImgLabeling<Integer, IntType> watershed(
		final ImgLabeling<Integer, IntType> out,
		final RandomAccessibleInterval<B> in, final boolean useEightConnectivity,
		final boolean drawWatersheds, final double[] sigma,
		final RandomAccessibleInterval<B> mask)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedBinary.class, out, in,
				useEightConnectivity, drawWatersheds, sigma, mask);
		return result;
	}

	// -- watershedBinarySingleSigma --

	@OpMethod(
		op = net.imagej.ops.image.watershed.WatershedBinarySingleSigma.class)
	public <T extends BooleanType<T>> ImgLabeling<Integer, IntType> watershed(
		final RandomAccessibleInterval<T> in, final boolean useEightConnectivity,
		final boolean drawWatersheds, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedBinarySingleSigma.class, in,
				useEightConnectivity, drawWatersheds, sigma);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.image.watershed.WatershedBinarySingleSigma.class)
	public <T extends BooleanType<T>> ImgLabeling<Integer, IntType> watershed(
		final ImgLabeling<Integer, IntType> out,
		final RandomAccessibleInterval<T> in, final boolean useEightConnectivity,
		final boolean drawWatersheds, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedBinarySingleSigma.class, out,
				in, useEightConnectivity, drawWatersheds, sigma);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.image.watershed.WatershedBinarySingleSigma.class)
	public <T extends BooleanType<T>, B extends BooleanType<B>>
		ImgLabeling<Integer, IntType> watershed(
			final ImgLabeling<Integer, IntType> out,
			final RandomAccessibleInterval<T> in, final boolean useEightConnectivity,
			final boolean drawWatersheds, final double sigma,
			final RandomAccessibleInterval<B> mask)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedBinarySingleSigma.class, out,
				in, useEightConnectivity, drawWatersheds, sigma, mask);
		return result;
	}

	// -- watershedSeeded --

	/** Executes the "watershedSeeded" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.watershed.WatershedSeeded.class)
	public <T extends RealType<T>> ImgLabeling<Integer, IntType> watershed(
		final RandomAccessibleInterval<T> in,
		final ImgLabeling<Integer, IntType> seeds, final boolean eightConnectivity,
		final boolean drawWatersheds)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			((ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedSeeded.class, in, seeds,
				eightConnectivity, drawWatersheds));
		return result;
	}

	/** Executes the "watershedSeeded" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.watershed.WatershedSeeded.class)
	public <T extends RealType<T>> ImgLabeling<Integer, IntType> watershed(
		final ImgLabeling<Integer, IntType> out,
		final RandomAccessibleInterval<T> in,
		final ImgLabeling<Integer, IntType> seeds, final boolean eightConnectivity,
		final boolean drawWatersheds)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedSeeded.class, out, in, seeds,
				eightConnectivity, drawWatersheds);
		return result;
	}

	/** Executes the "watershedSeeded" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.image.watershed.WatershedSeeded.class)
	public <B extends BooleanType<B>, T extends RealType<T>>
		ImgLabeling<Integer, IntType> watershed(
			final ImgLabeling<Integer, IntType> out,
			final RandomAccessibleInterval<T> in,
			final ImgLabeling<Integer, IntType> seeds,
			final boolean eightConnectivity, final boolean drawWatersheds,
			final RandomAccessibleInterval<B> mask)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<Integer, IntType> result =
			(ImgLabeling<Integer, IntType>) ops().run(
				net.imagej.ops.image.watershed.WatershedSeeded.class, out, in, seeds,
				eightConnectivity, drawWatersheds, mask);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "image";
	}

}
