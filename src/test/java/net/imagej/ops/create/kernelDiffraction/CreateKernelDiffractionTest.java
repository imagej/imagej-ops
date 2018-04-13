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

package net.imagej.ops.create.kernelDiffraction;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Tests {@link DefaultCreateKernelGibsonLanni}.
 * 
 * @author Curtis Rueden
 * @author Eric Czech
 */
public class CreateKernelDiffractionTest extends AbstractOpTest {

	@Test
	public void testKernelDiffraction2D() {
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
	
	/**
	 * Validate generation of a 3D diffraction kernel against a comparable result
	 * from <a href="http://bigwww.epfl.ch/algorithms/psfgenerator/">
	 * PSFGenerator</a>.
	 * <p>
	 * It is worth noting that results are only comparable between the two when
	 * using a particle position relative to the coverslip of 0. This is because
	 * imagej-ops automatically crops and centers kernels produced by the Fast
	 * Gibson-Lanni implementation in {@link DefaultCreateKernelGibsonLanni} while
	 * the Gibson &amp; Lanni kernels produced by PSFGenerator are not. See this
	 * github issue
	 * <a href="https://github.com/imagej/imagej-ops/issues/550">thread</a> for
	 * more details.
	 * </p>
	 * <p>
	 * It is also worth noting that when using a particle position of 0, the model
	 * degenerates to a standard Born &amp; Wolf PSF model [1].
	 * </p>
	 * <h3>References:</h3>
	 * <ol>
	 * <li>Jizhou Li, Feng Xue, and Thierry Blu, "Fast and accurate
	 * three-dimensional point spread function computation for fluorescence
	 * microscopy," J. Opt. Soc. Am. A 34, 1029-1034 (2017)</li>
	 * </ol>
	 */
	@Test
	public void testKernelDiffraction3D() {
		final Dimensions dims = new FinalDimensions(16, 16, 8);
		final double NA = 1.4; // numerical aperture
		final double lambda = 610E-09; // wavelength
		final double ns = 1.33; // specimen refractive index
		final double ni = 1.5; // immersion refractive index, experimental
		final double resLateral = 100E-9; // lateral pixel size
		final double resAxial = 250E-9; // axial pixel size
		final DoubleType type = new DoubleType(); // pixel type of created kernel
		
		// NB: It is important that this remain 0 for comparison to PSFGenerator.
		final double pZ = 0D; // position of particle

		final Img<DoubleType> kernel = 
			ops.create().kernelDiffraction(dims, NA, lambda, ns, ni, resLateral,
				resAxial, pZ, type);
		
		// The following image used for comparison was generated via PSFGenerator
		// with these arguments (where any not mentioned were left at default):
		// Optical Model: "Gibson & Lanni 3D Optical Model"
		// Particle position Z: 0
		// Wavelength: 610 (nm)
		// Pixelsize XY: 100 (nm)
		// Z-step: 250 (nm)
		// Size XYZ: 16, 16, 8
		Img<DoubleType> expected = openDoubleImg("kern3d1.tif");
		
		assertArrayEquals(asArray(expected), asArray(kernel), 1e-4);
	}
}
