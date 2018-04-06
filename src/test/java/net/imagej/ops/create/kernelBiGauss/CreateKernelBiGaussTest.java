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

package net.imagej.ops.create.kernelBiGauss;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

/**
 * Tests {@link DefaultCreateKernelBiGauss} and its derivates.
 * 
 * @author Vladimír Ulman
 */
public class CreateKernelBiGaussTest extends AbstractOpTest {

	@Test
	public void testKernelBiGauss() {
		final double sigma = 3.0;
		final double[] sigmas = {sigma, 0.5*sigma};

		//test the main convenience function:
		RandomAccessibleInterval<DoubleType> kernelD
			= ops.create().kernelBiGauss(sigmas, 2);

		//sizes are okay?
		assertEquals(13, kernelD.dimension(0));
		assertEquals(13, kernelD.dimension(1));

		//is value at the centre the expected one?
		final long[] position = {kernelD.dimension(0)/2, kernelD.dimension(1)/2};
		RandomAccess<DoubleType> samplerD = kernelD.randomAccess();
		samplerD.setPosition(position);
		assertEquals(0.09265, samplerD.get().getRealDouble(), 0.00005);

		//is value at the centre local maximum?
		final double[] values = new double[5];
		values[0] = samplerD.get().getRealDouble();
		samplerD.move(1,0);
		values[1] = samplerD.get().getRealDouble();
		samplerD.move(-2,0);
		values[2] = samplerD.get().getRealDouble();
		samplerD.move(1,0);
		samplerD.move(1,1);
		values[3] = samplerD.get().getRealDouble();
		samplerD.move(-2,1);
		values[4] = samplerD.get().getRealDouble();
		assertEquals(1.0,values[0]-values[1],0.999);
		assertEquals(1.0,values[0]-values[2],0.999);
		assertEquals(1.0,values[0]-values[3],0.999);
		assertEquals(1.0,values[0]-values[4],0.999);

		//is consistency checking okay?
		int wasCaught = 0;
		try {
			final double[] shortSigmas = {2.0*sigma};
			kernelD = ops.create().kernelBiGauss(shortSigmas,2);
		}
		catch (IllegalArgumentException e)
		{
			++wasCaught;
		}
		try {
			final double[] negativeSigmas = {-1.0, 0.0};
			kernelD = ops.create().kernelBiGauss(negativeSigmas,2);
		}
		catch (IllegalArgumentException e)
		{
			++wasCaught;
		}
		try {
			//wrong dimensionality
			kernelD = ops.create().kernelBiGauss(sigmas,0);
		}
		catch (IllegalArgumentException e)
		{
			++wasCaught;
		}
		assertEquals(3, wasCaught);

		//does the general kernel calculation work?
		//(should be pure real kernel)
		RandomAccessibleInterval<ComplexDoubleType> kernelCD
			= ops.create().kernelBiGauss(sigmas, 2, new ComplexDoubleType());
		RandomAccess<ComplexDoubleType> samplerCD = kernelCD.randomAccess();
		samplerCD.setPosition(position);
		assertEquals(0.0, samplerCD.get().getImaginaryDouble(), 0.00001);

		//general plugin system works?
		//@SuppressWarnings("unchecked")
		kernelCD = (RandomAccessibleInterval<ComplexDoubleType>)
			ops.run(Ops.Create.KernelBiGauss.class, sigmas, 3, new ComplexDoubleType());
	}
}
