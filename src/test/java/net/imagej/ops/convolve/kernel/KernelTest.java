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

package net.imagej.ops.convolve.kernel;

import static org.junit.Assert.assertEquals;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

/**
 * Kernel generation test class.
 * 
 * @author bnorthan
 */
public class KernelTest extends AbstractOpTest {

	@Test
	public void test() {

		double sigma = 5.0;

		int numDimensions = 2;

		double[] sigmas = new double[numDimensions];

		for (int i = 0; i < numDimensions; i++) {
			sigmas[i] = sigma;
		}

		Img<FloatType> gaussianKernel =
			(Img<FloatType>) ops.run("gausskernel", new FloatType(), new ArrayImgFactory(), numDimensions, sigma, null
				);

		Img<FloatType> gaussianKernel2 =
			(Img<FloatType>) ops.run("gausskernel", new FloatType(),
				new ArrayImgFactory(), sigmas, null);

		Img<FloatType> logKernel =
			(Img<FloatType>) ops.run("logkernel",
				new FloatType(), new ArrayImgFactory(), numDimensions, sigma, null);

		Img<FloatType> logKernel2 =
			(Img<FloatType>) ops.run("logkernel", new FloatType(),
				new ArrayImgFactory(), sigmas, null);

		assertEquals(gaussianKernel.dimension(1), 31);
		assertEquals(gaussianKernel2.dimension(1), 31);

		assertEquals(logKernel.dimension(1), 27);
		assertEquals(logKernel2.dimension(1), 27);

	}

	@Test
	public void testDefaults() {

		double sigma = 5.0;

		int numDimensions = 2;

		double[] sigmas = new double[numDimensions];

		for (int i = 0; i < numDimensions; i++) {
			sigmas[i] = sigma;
		}

		/*Img<FloatType> gaussianKernel =
			(Img<FloatType>) ops.run("gausskernel", sigmas, null, new FloatType(),
				new ArrayImgFactory());

		// no factory
		Img<FloatType> gaussianKernel2 =
			(Img<FloatType>) ops.run("gausskernel", sigmas, null, new FloatType());
		*/
		Img<FloatType> gaussianKernel =
			(Img<FloatType>) ops.run("gausskernel", new FloatType(),
				new ArrayImgFactory(), sigmas, null);

		// no factory
		Img<FloatType> gaussianKernel2 =
			(Img<FloatType>) ops.run("gausskernel", new FloatType(), null, sigmas,
				null);
		// no factory, no type
		Img<FloatType> gaussianKernel3 =
			(Img<FloatType>) ops.run("gausskernel", sigmas);

		assertEquals(gaussianKernel.dimension(1), 31);
		assertEquals(gaussianKernel2.dimension(1), 31);
		assertEquals(gaussianKernel2.dimension(1), 31);

		Img<FloatType> logKernel =
			(Img<FloatType>) ops.run("logkernel", new FloatType(),
				new ArrayImgFactory(), sigmas, null);

		// no factory
		Img<FloatType> logKernel2 =
			(Img<FloatType>) ops
				.run("logkernel", new FloatType(), null, sigmas, null);

		// no factory, no type
		Img<FloatType> logKernel3 = (Img<FloatType>) ops.run("logkernel", sigmas);

		assertEquals(logKernel.dimension(1), 27);
		assertEquals(logKernel2.dimension(1), 27);
		assertEquals(logKernel2.dimension(1), 27);

	}

}
