/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

package net.imagej.ops.create.kernelDiffraction;

import static org.junit.Assert.assertArrayEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

/**
 * Tests {@link DefaultCreateKernelGibsonLanni}.
 * 
 * @author Curtis Rueden
 */
public class CreateKernelDiffractionTest extends AbstractOpTest {

	@Test
	public void testKernelDiffraction() {
		final Dimensions dims = new FinalDimensions(10, 10);
		final double NA = 1.4; // numerical aperture
		final double lambda = 610E-09; // wavelength
		final double ns = 1.33; // specimen refractive index
		final double ni = 1.5; // immersion refractive index, experimental
		final double resLateral = 100E-9; // lateral pixel size
		final double resAxial = 250E-9; // axial pixel size
		final double pZ = 2000E-9D; // position of particle
		final DoubleType type = new DoubleType(); // pixel type of created kernel

		final Img<DoubleType> kernel = //
			ops.create().kernelDiffraction(dims, NA, lambda, ns, ni, resLateral,
				resAxial, pZ, type);

		final double[] expected = { 0.03298495871588273, 0.04246786111102021,
			0.0543588031627261, 0.06650574371357207, 0.07370280610722534,
			0.07370280610722534, 0.06650574371357207, 0.0543588031627261,
			0.04246786111102021, 0.03298495871588273, 0.04246786111102021,
			0.05962205221267819, 0.08320071670150801, 0.10800022978800021,
			0.1247473245002288, 0.1247473245002288, 0.10800022978800021,
			0.08320071670150801, 0.05962205221267819, 0.04246786111102021,
			0.0543588031627261, 0.08320071670150801, 0.1247473245002288,
			0.1971468112729564, 0.2691722397359577, 0.2691722397359577,
			0.1971468112729564, 0.1247473245002288, 0.08320071670150801,
			0.0543588031627261, 0.06650574371357207, 0.10800022978800021,
			0.1971468112729564, 0.40090474481128285, 0.6227157103102976,
			0.6227157103102976, 0.40090474481128285, 0.1971468112729564,
			0.10800022978800021, 0.06650574371357207, 0.07370280610722534,
			0.1247473245002288, 0.2691722397359577, 0.6227157103102976, 1.0, 1.0,
			0.6227157103102976, 0.2691722397359577, 0.1247473245002288,
			0.07370280610722534, 0.07370280610722534, 0.1247473245002288,
			0.2691722397359577, 0.6227157103102976, 1.0, 1.0, 0.6227157103102976,
			0.2691722397359577, 0.1247473245002288, 0.07370280610722534,
			0.06650574371357207, 0.10800022978800021, 0.1971468112729564,
			0.40090474481128285, 0.6227157103102976, 0.6227157103102976,
			0.40090474481128285, 0.1971468112729564, 0.10800022978800021,
			0.06650574371357207, 0.0543588031627261, 0.08320071670150801,
			0.1247473245002288, 0.1971468112729564, 0.2691722397359577,
			0.2691722397359577, 0.1971468112729564, 0.1247473245002288,
			0.08320071670150801, 0.0543588031627261, 0.04246786111102021,
			0.05962205221267819, 0.08320071670150801, 0.10800022978800021,
			0.1247473245002288, 0.1247473245002288, 0.10800022978800021,
			0.08320071670150801, 0.05962205221267819, 0.04246786111102021,
			0.03298495871588273, 0.04246786111102021, 0.0543588031627261,
			0.06650574371357207, 0.07370280610722534, 0.07370280610722534,
			0.06650574371357207, 0.0543588031627261, 0.04246786111102021,
			0.03298495871588273,

		};

		assertArrayEquals(expected, asArray(kernel), 0.0);
	}
}
