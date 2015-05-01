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

package net.imagej.ops.convolve.kernel.create;

import net.imagej.ops.Contingent;
import net.imagej.ops.create.AbstractCreateImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for kernel generation from sigma and <b> calibrated units
 * </b>. The specified sigma and calibration is used to determine the
 * dimensionality of the kernel and to map it on a pixel grid.
 * 
 * @author bnorthan
 * @param <T>
 */
public abstract class AbstractCreateKernel<T extends ComplexType<T> & NativeType<T>>
	extends AbstractCreateImg<T, DoubleType, ArrayImgFactory<DoubleType>>
	implements Contingent
{

	@Parameter
	double[] sigma;

	@Parameter(required = false)
	double[] calibration;

	int numDimensions;

	public void run() {

		numDimensions = sigma.length;

		if (calibration == null) {
			calibration = new double[numDimensions];

			for (int i = 0; i < numDimensions; i++) {
				calibration[i] = 1.0;
			}
		}

		createKernel();
	}

	@Override
	public boolean conforms() {

		if (calibration != null) {
			if (calibration.length != sigma.length) {
				return false;
			}
		}

		// if outType is not null make sure it is a supported type
		if (outType != null) {
			Object tmp = outType;
			if ((tmp instanceof FloatType) || (tmp instanceof DoubleType) ||
				(tmp instanceof ComplexFloatType) || (tmp instanceof ComplexDoubleType)) return true;
			return false;
		}

		return true;
	}

	abstract void createKernel();

}
