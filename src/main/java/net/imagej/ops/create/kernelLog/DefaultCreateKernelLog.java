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

package net.imagej.ops.create.kernelLog;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Laplacian of Gaussian filter ported from
 * fiji.plugin.trackmate.detection.DetectionUtil. Permission granted by
 * Jean-Yves Tinevez to change license from GPL. Creates a laplacian of gaussian
 * (LoG) kernel tuned for blobs with a radius (sigma) and <b> calibrated units
 * (default calibration is 1) </b>. The specified sigma and calibration is used
 * to determine the dimensionality of the kernel and to map it on a pixel grid.
 *
 * @author Jean-Yves Tinevez
 * @author Brian Northan
 */
@Plugin(type = Ops.Create.KernelLog.class)
public class DefaultCreateKernelLog<T extends ComplexType<T>> extends
	AbstractUnaryFunctionOp<double[], RandomAccessibleInterval<T>> implements
	Ops.Create.KernelLog
{

	@Parameter
	private T type;

	private UnaryFunctionOp<Interval, RandomAccessibleInterval<T>> createOp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		createOp = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			RandomAccessibleInterval.class, Dimensions.class, type);
	}

	@Override
	public RandomAccessibleInterval<T> calculate(double[] sigmas) {
		final double[] sigmaPixels = new double[sigmas.length];
		for (int i = 0; i < sigmaPixels.length; i++) {
			// Optimal sigma for LoG approach and dimensionality.
			final double sigma_optimal = sigmas[i] / Math.sqrt(sigmas.length);

			sigmaPixels[i] = sigma_optimal;
		}
		final int n = sigmaPixels.length;
		final long[] dims = new long[n];
		final long[] middle = new long[n];
		for (int d = 0; d < n; ++d) {
			// The half size of the kernel is 3 standard deviations (or a
			// minimum half size of 2)
			final int hksizes = Math.max(2, (int) (3 * sigmaPixels[d] + 0.5) + 1);
			// add 3 border pixels to achieve smoother derivatives at the border
			dims[d] = 3 + 2 * hksizes;
			middle[d] = 1 + hksizes;
		}

		final RandomAccessibleInterval<T> output = createOp.calculate(
			new FinalInterval(dims));

		final Cursor<T> c = Views.iterable(output).cursor();
		final long[] coords = new long[sigmas.length];
		/*
		 * The gaussian normalization factor, divided by a constant value. This
		 * is a fudge factor, that more or less put the quality values close to
		 * the maximal value of a blob of optimal radius.
		 */
		final double C = 1d / 20d * Math.pow(1d / sigmas[0] / Math.sqrt(2 *
			Math.PI), sigmas.length);
		// Work in image coordinates
		while (c.hasNext()) {
			c.fwd();
			c.localize(coords);
			double mantissa = 0;
			double exponent = 0;
			for (int d = 0; d < coords.length; d++) {
				final double x = (coords[d] - middle[d]);
				mantissa += -C * (x * x / sigmas[0] / sigmas[0] - 1d);
				exponent += -x * x / 2d / sigmas[0] / sigmas[0];
			}
			c.get().setReal(mantissa * Math.exp(exponent));
		}

		return output;
	}

}
