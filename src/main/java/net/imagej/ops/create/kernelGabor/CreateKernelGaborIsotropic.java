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

package net.imagej.ops.create.kernelGabor;

import java.util.Arrays;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Creates an isotropic Gabor kernel, i.e.,
 * with one sigma for all axes, and a period vector.
 * The dimension of the kernel is given by the length of the period vector.
 * <p>
 * Is uses {@link DefaultCreateKernelGabor} to do the work, refer there
 * for the explanation of the parameters.
 *
 * @author Vladimír Ulman
 * @param <T>
 */
@Plugin(type = Ops.Create.KernelGabor.class, name = "create.kernelGabor")
public class CreateKernelGaborIsotropic<T extends ComplexType<T>>
	extends AbstractBinaryFunctionOp<Double, double[], RandomAccessibleInterval<T>>
	implements Ops.Create.KernelGabor
{
	@Parameter
	private T typeVar;

	///a handle to use the default and general kernelGabor function
	private
	BinaryFunctionOp<double[], double[], RandomAccessibleInterval<T>> defaultKernelOp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		defaultKernelOp = (BinaryFunctionOp) Functions.binary(ops(),
			Ops.Create.KernelGabor.class, RandomAccessibleInterval.class,
			double[].class, double[].class, typeVar);
	}

	@Override
	public RandomAccessibleInterval<T> calculate(final Double sigma, final double[] period) {
		final double[] sigmas = new double[period.length];
		Arrays.fill(sigmas, sigma);
		return defaultKernelOp.calculate(sigmas, period);
	}
}
