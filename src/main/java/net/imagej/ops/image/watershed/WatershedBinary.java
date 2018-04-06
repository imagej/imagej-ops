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
package net.imagej.ops.image.watershed;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * <p>
 * The Watershed algorithm segments and labels a grayscale image analogous to a
 * heightmap. In short, a drop of water following the gradient of an image flows
 * along a path to finally reach a local minimum.
 * </p>
 * <p>
 * Lee Vincent, Pierre Soille, Watersheds in digital spaces: An efficient
 * algorithm based on immersion simulations, IEEE Trans. Pattern Anal. Machine
 * Intell., 13(6) 583-598 (1991)
 * </p>
 * <p>
 * Input is a binary image with arbitrary number of dimensions. The heightmap is
 * calculated by an inverse distance transform, which can optionally be smoothed
 * with an gaussian filter with parameter sigma to prevent having many small
 * segments in the result. Sigma must have the same dimension as the input image.
 * It needs to be defined whether a neighborhood with eight- or four-connectivity 
 * (respective to 2D) is used. A binary image can be set as mask which defines the
 * area where computation shall be done. It may make sense to use the input as 
 * mask as well. If desired, the watersheds are drawn and labeled as 0. Otherwise 
 * the watersheds will be labeled as one of their neighbors.
 * </p>
 * <p>
 * Output is a labeling of the different catchment basins.
 * </p>
 *
 * @param <T> element type of input
 * @param <B> element type of mask
 *
 * @author Simon Schmid (University of Konstanz)
 */
@Plugin(type = Ops.Image.Watershed.class)
public class WatershedBinary<T extends BooleanType<T>, B extends BooleanType<B>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, ImgLabeling<Integer, IntType>>
		implements Ops.Image.Watershed, Contingent {

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Interval, ImgLabeling> createOp;

	@Parameter(required = true)
	private boolean useEightConnectivity;

	@Parameter(required = true)
	private boolean drawWatersheds;

	@Parameter(required = true)
	private double[] sigma;
	
	@Parameter(required = false)
	private RandomAccessibleInterval<B> mask;

	@Override
	public void compute(final RandomAccessibleInterval<T> in, final ImgLabeling<Integer, IntType> out) {
		// compute distance transform
		final RandomAccessibleInterval<FloatType> distMap = ops().image().distancetransform(in);
		final RandomAccessibleInterval<FloatType> invertedDT = ops().create().img(in, new FloatType());
		ops().image().invert(Views.iterable(invertedDT), Views.iterable(distMap));
		final RandomAccessibleInterval<FloatType> gauss = ops().filter().gauss(invertedDT, sigma);
		// run the default watershed
		ops().run(Watershed.class, out, gauss, useEightConnectivity, drawWatersheds, mask);
	}

	@Override
	public boolean conforms() {
		boolean conformed = sigma.length != in().numDimensions();
		for (int i = 0; i < sigma.length; i++) {
			conformed &= sigma[i] >= 0;
		}
		if (mask != null) {
			conformed &= Intervals.equalDimensions(mask, in());
		}
		return conformed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImgLabeling<Integer, IntType> createOutput(final RandomAccessibleInterval<T> in) {
		return createOp.calculate(in);
	}

	@Override
	public void initialize() {
		createOp = Functions.unary(ops(), CreateImgLabelingFromInterval.class, ImgLabeling.class, in());
	}
}
