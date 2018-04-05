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

import java.util.Arrays;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Creates a symmetric Gaussian kernel.
 *
 * @author Christian Dietz (University of Konstanz)
 * @author Brian Northan
 * @param <T> type of kernel image to create
 */
@Plugin(type = Ops.Create.KernelGauss.class)
public class CreateKernelGaussSymmetric<T extends ComplexType<T>> extends
	AbstractUnaryFunctionOp<Double, RandomAccessibleInterval<T>> implements
	Ops.Create.KernelGauss
{

	@Parameter
	private int numDims;

	@Parameter
	private T type;

	private UnaryFunctionOp<double[], RandomAccessibleInterval<T>> kernelOp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		kernelOp = (UnaryFunctionOp) Functions.unary(ops(),
			Ops.Create.KernelGauss.class, RandomAccessibleInterval.class,
			double[].class, type);
	}

	@Override
	public RandomAccessibleInterval<T> calculate(final Double input) {
		final double[] sigmas = new double[numDims];
		Arrays.fill(sigmas, input);
		return kernelOp.calculate(sigmas);
	}

}
