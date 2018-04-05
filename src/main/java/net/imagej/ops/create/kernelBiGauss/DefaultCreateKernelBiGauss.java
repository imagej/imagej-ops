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

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Creates an isotropic BiGauss kernel
 * with the pair of sigmas specification.
 * <p>
 * The BiGauss kernel is a composition of two standard Gauss kernels. If
 * we were to assume 1D kernel centered at zero (0), an inner kernel of the
 * BiGauss, with its shape given with sigmas[0], would span from -sigmas[0]
 * till +sigmas[0]; outer kernel, with its shape given with sigmas[1],
 * surrounds the inner, e.g. for the positive side, from sigmas[0] till
 * sigmas[0]+2*sigmas[1] and its center having at sigmas[0]-sigmas[1].
 * That is, the inner Gauss exist up to its inflection points from which
 * the filter takes shape of the outer Gauss. Both kernels are, however, 
 * appropriately scaled and shifted to obtain a smooth BiGauss kernel.
 * <p>
 * Note that the kernel is always isotropic.
 * The second parameter gives dimensionality of the created kernel.
 * <p>
 * All values are in units of pixels.
 * <p>
 * <b>Literature:</b>C. Xiao, M. Staring, Y. Wang, D.P. Shamonin, B.C. Stoel.
 * Multiscale Bi-Gaussian Filter for Adjacent Curvilinear Structures Detection
 * with Application to Vasculature Images. IEEE TMI, vol. 22, no. 1, 2013.
 *
 * @author Vladimír Ulman
 * @param <T>
 */
@Plugin(type = Ops.Create.KernelBiGauss.class, name = "create.kernelBiGauss")
public class DefaultCreateKernelBiGauss<T extends ComplexType<T>>
	extends AbstractBinaryFunctionOp<double[], Integer, RandomAccessibleInterval<T>>
	implements Ops.Create.KernelBiGauss
{
	@Parameter
	private T typeVar;

	///a handle to create image of given size
	private UnaryFunctionOp<Interval, RandomAccessibleInterval<T>> createImgOp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		createImgOp = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			RandomAccessibleInterval.class, Dimensions.class, typeVar);
	}

	@Override
	public RandomAccessibleInterval<T> calculate(final double[] sigmas,
	                                             final Integer dimensionality) {
		//both sigmas must be available
		if (sigmas.length < 2)
			throw new IllegalArgumentException("Two sigmas (for inner and outer Gauss)"
			+ " must be supplied.");

		//both sigmas must be reasonable
		if (sigmas[0] <= 0 || sigmas[1] <= 0)
			throw new IllegalArgumentException("Input sigmas must be both positive.");

		//dimension as well...
		if (dimensionality <= 0)
			throw new IllegalArgumentException("Input dimensionality must both positive.");

		//the size and center of the output image
		final long[] dims = new long[dimensionality];
		final long[] centre = new long[dimensionality];

		//time-saver... (must hold now: dimensionality > 0)
		dims[0]=Math.max(3, (2 * (int)(sigmas[0] + 2*sigmas[1] + 0.5) + 1));
		centre[0]=(int)(dims[0]/2);

		//fill the size and center arrays
		for (int d = 1; d < dims.length; d++) {
			dims[d] = dims[0];
			centre[d] = centre[0];
		}

		//prepare some scaling constants
		final double k = (sigmas[1]/sigmas[0]) * (sigmas[1]/sigmas[0]);        //eq. (6)
		final double c0 = 0.24197 * ((sigmas[1]/sigmas[0]) - 1.0) / sigmas[0]; //eq. (9)
		//0.24197 = 1/sqrt(2*PI*e) = 1/sqrt(2*PI) * exp(-0.5)
		final double[] C = { 1.0/(2.50663*sigmas[0]), 1.0/(2.50663*sigmas[1]) };
		//2.50663 = sqrt(2*PI)

		//prepare squared input sigmas
		final double sigmasSq[] = { sigmas[0]*sigmas[0], sigmas[1]*sigmas[1] };

		//prepare the output image
		final RandomAccessibleInterval<T> out
			= createImgOp.calculate(new FinalInterval(dims));

		//fill the output image
		final Cursor<T> cursor = Views.iterable(out).cursor();
		while (cursor.hasNext()) {
			cursor.fwd();

			//obtain the current coordinate (use dims to store it)
			cursor.localize(dims);

			//calculate distance from the image centre
			double dist = 0.; //TODO: can JVM reuse this var or is it allocated again and again (and multipling in the memory)?
			for (int d = 0; d < dims.length; d++) {
				final double dx = dims[d]-centre[d];
				dist += dx*dx;
			}
			//dist = Math.sqrt(dist); -- gonna work with squared distance

			//which of the two Gaussians should we use?
			double val = 0.;
			if (dist < sigmasSq[0]) {
				//the inner one
				val = C[0] * Math.exp(-0.5 * dist / sigmasSq[0]) + c0;
			} else {
				//the outer one, get new distance first:
				dist  = Math.sqrt(dist) - (sigmas[0]-sigmas[1]);
				dist *= dist;
				val = k * C[1] * Math.exp(-0.5 * dist / sigmasSq[1]);
			}

			//compose the real value finally
			cursor.get().setReal(val);
		}

		return out;
	}
}
