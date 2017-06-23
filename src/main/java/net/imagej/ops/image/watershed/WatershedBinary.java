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
package net.imagej.ops.image.watershed;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * The Watershed algorithm segments and labels a grayscale image analogous to a
 * heightmap. In short, a drop of water following the gradient of an image flows
 * along a path to finally reach a local minimum.
 *
 * Lee Vincent, Pierre Soille, Watersheds in digital spaces: An efficient
 * algorithm based on immersion simulations, IEEE Trans. Pattern Anal. Machine
 * Intell., 13(6) 583-598 (1991)
 *
 * Input is a binary image with arbitrary number of dimensions. The heightmap is
 * calculated by an inverse distance transform, which is smoothed with an
 * gaussian filter with parameter sigma. It needs to be defined whether a
 * neighborhood with eight- or four-connectivity (respective to 2D) is used.
 *
 * Output is a labeling of the different catchment basins.
 *
 * @author Simon Schmid (University of Konstanz)
 */
@Plugin(type = Ops.Image.Watershed.class)
public class WatershedBinary<B extends BooleanType<B>, T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<B>, ImgLabeling<Integer, IntType>>
		implements Ops.Image.Watershed, Contingent {

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<FinalInterval, ImgLabeling> createOp;

	@Parameter(required = true)
	private boolean eightConnectivity;

	@Parameter(required = true)
	private double[] sigma;

	@Override
	public void compute(final RandomAccessibleInterval<B> in, final ImgLabeling<Integer, IntType> out) {

		// compute distance transform
		final RandomAccessibleInterval<FloatType> distMap = ops().image().distancetransform(in);
		final RandomAccessibleInterval<FloatType> invertedDT = ops().create().img(in, new FloatType());
		ops().image().invert(Views.iterable(invertedDT), Views.iterable(distMap));
		final RandomAccessibleInterval<FloatType> gauss = ops().filter().gauss(invertedDT, sigma);

		// run the default watershed
		ops().run(Watershed.class, out, gauss, eightConnectivity, in);
	}

	@Override
	public boolean conforms() {
		boolean conforms = true;
		for (int i = 0; i < sigma.length; i++) {
			conforms &= sigma[i] >= 0;
		}
		return conforms;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImgLabeling<Integer, IntType> createOutput(final RandomAccessibleInterval<B> in) {
		return createOp.calculate(new FinalInterval(in));
	}

	@Override
	public void initialize() {
		createOp = Functions.unary(ops(), CreateImgLabelingFromInterval.class, ImgLabeling.class,
				new FinalInterval(in()));
	}
}
