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

package net.imagej.ops.features.tamura2d;

import java.util.ArrayList;

import net.imagej.ops.Ops;
import net.imagej.ops.image.histogram.HistogramCreate;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gradient.PartialDerivative;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Implementation of Tamura's Directionality Feature
 * 
 * @author Andreas Graumann (University of Konstanz)
 * @param <I>
 * @param <O>
 */
@SuppressWarnings("rawtypes")
@Plugin(type = Ops.Tamura.Directionality.class,
	label = "Tamura 2D: Directionality")
public class DefaultDirectionalityFeature<I extends RealType<I>, O extends RealType<O>>
	extends AbstractTamuraFeature<I, O> implements Ops.Tamura.Directionality
{

	@Parameter(required = true)
	private int histogramSize = 16;

	private UnaryFunctionOp<Iterable, Histogram1d> histOp;
	private UnaryFunctionOp<Iterable, RealType> stdOp;
	private UnaryFunctionOp<RandomAccessibleInterval<I>, Img<I>> imgCreator;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		stdOp = Functions.unary(ops(), Ops.Stats.StdDev.class, RealType.class,
			Iterable.class);
		histOp = Functions.unary(ops(), HistogramCreate.class, Histogram1d.class,
			Iterable.class, histogramSize);
		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), Util.getTypeFromInterval(in()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(final RandomAccessibleInterval<I> input,
		final O output)
	{

		// List to store all directions occuring within the image on borders
		ArrayList<DoubleType> dirList = new ArrayList<>();

		// Dimension of input region
		long[] dims = new long[input.numDimensions()];
		input.dimensions(dims);

		// create image for derivations in x and y direction
		Img<I> derX = imgCreator.calculate(input);
		Img<I> derY = imgCreator.calculate(input);

		// calculate derivations in x and y direction
		PartialDerivative.gradientCentralDifference2(Views.extendMirrorSingle(
			input), derX, 0);
		PartialDerivative.gradientCentralDifference2(Views.extendMirrorSingle(
			input), derY, 1);

		// calculate theta at each position: theta = atan(dX/dY) + pi/2
		Cursor<I> cX = derX.cursor();
		Cursor<I> cY = derY.cursor();

		// for each position calculate magnitude and direction
		while (cX.hasNext()) {
			cX.next();
			cY.next();

			double dx = cX.get().getRealDouble();
			double dy = cY.get().getRealDouble();

			double dir = 0.0;
			double mag = 0.0;

			mag = Math.sqrt(dx * dx + dy * dy);

			if (dx != 0 && mag > 0.0) {
				dir = Math.atan(dy / dx) + Math.PI / 2;
				dirList.add(new DoubleType(dir));
			}
		}

		// No directions: output is zero
		if (dirList.isEmpty()) {
			output.setReal(0.0);
		}
		// Otherwise compute histogram over all occuring directions
		// and calculate inverse second moment on it as output
		else {
			Histogram1d<Integer> hist = histOp.calculate(dirList);
			double std = stdOp.calculate(hist).getRealDouble();
			output.setReal(1 / std);
		}
	}
}
