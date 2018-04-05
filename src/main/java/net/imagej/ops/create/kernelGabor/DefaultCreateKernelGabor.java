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
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Creates a Gabor kernel
 * with specifications for individual sigma per axis, and a period vector.
 * <p>
 * Kernels valid only for a subset of available axes are also supported.
 * If, for instance, filtering only along 2nd axis is desired, one may
 * provide the sigmas array filled with zeroes (0) except for the 2nd element.
 * <p>
 * The period vector is a vector along which oscilates the frequency part
 * of the Gabor filter. The length of this vector equals precisely the
 * wave-length of the oscilations (the length of 1 period).
 * <p>
 * All values are in units of pixels.
 * Both input arrays have to be of the same length.
 *
 * @author Vladimír Ulman
 * @param <T>
 */
@Plugin(type = Ops.Create.KernelGabor.class, name = "create.kernelGabor")
public class DefaultCreateKernelGabor<T extends ComplexType<T>>
	extends AbstractBinaryFunctionOp<double[], double[], RandomAccessibleInterval<T>>
	implements Ops.Create.KernelGabor
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
	public RandomAccessibleInterval<T> calculate(final double[] sigmas, final double[] period) {
		//both input arrays must be of the same length
		if (sigmas.length != period.length)
			throw new IllegalArgumentException("Params length mismatch: The number "
					+ "of sigmas must match the dimensionality of the period vector.");

		//sigmas must be reasonable
		//NB: sigma==0 indicates no filtering along its axis
		for (final double s : sigmas)
		if (s < 0.0)
			throw new IllegalArgumentException("Input sigma must be non-negative.");

		//the size and center of the output image
		final long[] dims = new long[sigmas.length];
		final long[] centre = new long[sigmas.length];
		for (int d = 0; d < dims.length; d++) {
			dims[d] = Math.max(3, (2 * (int) (3 * sigmas[d] + 0.5) + 1));
			centre[d] = (int)(dims[d]/2);
		}

		//prepare the output image
		final RandomAccessibleInterval<T> out
			= createImgOp.calculate(new FinalInterval(dims));

		//calculate the squared length of the period vector
		double perLengthSq = 0.0;
		for (int d = 0; d < period.length; d++)
			perLengthSq += period[d]*period[d];

		//fill the output image
		final Cursor<T> cursor = Views.iterable(out).cursor();
		while (cursor.hasNext()) {
			cursor.fwd();

			//obtain the current coordinate (use dims to store it)
			cursor.localize(dims);

			//to calculate current Gabor kernel value
			double GaussExp = 0.0;
			double freqPart = 0.0;

			//but produce no Gaussian envelope for axes for which sigma==0
			double blockingExp = 1.0; //no blocking by default

			//sweep over all dimensions to determine voxel value
			for (int d = 0; d < dims.length; d++) {
				final double dx = dims[d]-centre[d];

				if (sigmas[d] > 0.)
					//normal case: cummulate exp's argument
					GaussExp += (dx*dx) / (sigmas[d]*sigmas[d]);
				else if (dx != 0.)
					//sigmas[d] == 0 && we are off the blocking axis
					blockingExp = 0.f;

				//cummulates scalar product...
				freqPart += dx*period[d];
			}
			GaussExp = Math.exp(-0.5 * GaussExp) * blockingExp;
			freqPart = 6.28318 * freqPart / perLengthSq;

			//compose the real value finally
			cursor.get().setReal( GaussExp * Math.cos(freqPart) );

			//are we a truly complex image?
			//TODO NB: RealTypes have (empty) setImaginary method to be used too
			//TODO NB: is it faster to determine type or calculate the math (possible uselessly)
			if (!(typeVar instanceof RealType<?>))
				//set then the imaginary part of the kernel too
				cursor.get().setImaginary( GaussExp * Math.sin(freqPart) );
		}

		return out;
	}
}
