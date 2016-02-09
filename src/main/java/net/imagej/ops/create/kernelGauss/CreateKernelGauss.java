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

package net.imagej.ops.create.kernelGauss;

import net.imagej.ops.Ops;
import net.imagej.ops.create.AbstractCreateGaussianKernel;
import net.imglib2.Cursor;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;

import org.scijava.plugin.Plugin;

/**
 * Gaussian filter ported from
 * org.knime.knip.core.algorithm.convolvers.filter.linear.Gaussian;
 *
 * @author Christian Dietz (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 * @author Michael Zinsmaier (University of Konstanz)
 * @author Stephan Sellien (University of Konstanz)
 * @author Brian Northan
 * @param <T>
 */
@Plugin(type = Ops.Create.KernelGauss.class)
public class CreateKernelGauss<T extends ComplexType<T> & NativeType<T>>
	extends AbstractCreateGaussianKernel<T> implements Ops.Create.KernelGauss
{

	@Override
	protected void createKernel() {
		final double[] sigmaPixels = new double[numDimensions];

		final long[] dims = new long[numDimensions];
		final double[][] kernelArrays = new double[numDimensions][];

		for (int d = 0; d < numDimensions; d++) {
			sigmaPixels[d] = sigma[d];

			dims[d] = Math.max(3, (2 * (int) (3 * sigmaPixels[d] + 0.5) + 1));
			kernelArrays[d] = Util.createGaussianKernel1DDouble(sigmaPixels[d], true);
		}

		createOutputImg(dims, getFac(), getOutType(),
			new ArrayImgFactory<DoubleType>(), new DoubleType());

		final Cursor<T> cursor = getOutput().cursor();
		while (cursor.hasNext()) {
			cursor.fwd();
			double result = 1.0f;
			for (int d = 0; d < numDimensions; d++) {
				result *= kernelArrays[d][cursor.getIntPosition(d)];
			}

			cursor.get().setReal(result);
		}

	}

}
