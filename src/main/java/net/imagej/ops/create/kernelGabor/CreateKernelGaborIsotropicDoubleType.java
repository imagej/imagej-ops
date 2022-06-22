/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.BFViaBFSameIO;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Creates an isotropic Gabor kernel of type {@link DoubleType},
 * uses {@link DefaultCreateKernelGabor} to do the job.
 *
 * @author Vladimír Ulman
 */
@Plugin(type = Ops.Create.KernelGabor.class, name = "create.kernelGaborDouble")
public class CreateKernelGaborIsotropicDoubleType
	extends BFViaBFSameIO<Double, double[], RandomAccessibleInterval<DoubleType>>
	implements Ops.Create.KernelGabor
{
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BinaryFunctionOp<Double, double[], RandomAccessibleInterval<DoubleType>>
		createWorker(final Double t1, final double[] t2)
	{
		return (BinaryFunctionOp) Functions.binary(ops(),
			Ops.Create.KernelGabor.class, RandomAccessibleInterval.class,
			Double.class, double[].class, new DoubleType());
	}
}
