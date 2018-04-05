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

package net.imagej.ops.create.kernelGauss;

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
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Creates a Gaussian Kernel
 *
 * @author Christian Dietz (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 * @author Michael Zinsmaier (University of Konstanz)
 * @author Stephan Sellien (University of Konstanz)
 * @author Brian Northan
 * @param <T>
 */
@Plugin(type = Ops.Create.KernelGauss.class)
public class DefaultCreateKernelGauss<T extends ComplexType<T>> extends
	AbstractUnaryFunctionOp<double[], RandomAccessibleInterval<T>> implements
	Ops.Create.KernelGauss
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
	public RandomAccessibleInterval<T> calculate(double[] input) {
		final double[] sigmaPixels = new double[input.length];

		final long[] dims = new long[input.length];
		final double[][] kernelArrays = new double[input.length][];

		for (int d = 0; d < input.length; d++) {
			sigmaPixels[d] = input[d];

			dims[d] = Math.max(3, (2 * (int) (3 * sigmaPixels[d] + 0.5) + 1));
			kernelArrays[d] = Util.createGaussianKernel1DDouble(sigmaPixels[d], true);
		}

		final RandomAccessibleInterval<T> out = createOp.calculate(new FinalInterval(
			dims));

		final Cursor<T> cursor = Views.iterable(out).cursor();
		while (cursor.hasNext()) {
			cursor.fwd();
			double result = 1.0f;
			for (int d = 0; d < input.length; d++) {
				result *= kernelArrays[d][cursor.getIntPosition(d)];
			}

			cursor.get().setReal(result);
		}

		return out;
	}

}
