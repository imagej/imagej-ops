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

package net.imagej.ops.segment.detectRidges;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.Context;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * Helper Method to generate the meta image used for {@link DefaultDetectRidges}
 *
 * @author Gabe Selzer
 */
public class RidgeDetectionMetadata {

	protected Img<DoubleType> pValues;
	protected Img<DoubleType> nValues;
	protected Img<DoubleType> gradients;

	RandomAccessibleInterval<DoubleType> x;
	RandomAccessibleInterval<DoubleType> y;
	RandomAccessibleInterval<DoubleType> xx;
	RandomAccessibleInterval<DoubleType> xy;
	RandomAccessibleInterval<DoubleType> yy;

	/**
	 * Generates the metadata images from the input image
	 *
	 * @param input - the image to be detected
	 * @param sigma - the sigma for the gaussian derivative convolutions
	 */
	public <T extends RealType<T>> RidgeDetectionMetadata(
		final RandomAccessibleInterval<T> input, final double sigma,
		final double smallMax, final double bigMax)
	{
		// create op service for metadata generation
		final Context context = new Context();
		final OpService opService = context.getService(OpService.class);

		// convert input to doubleType
		final RandomAccessibleInterval<DoubleType> converted =
			(RandomAccessibleInterval<DoubleType>) opService.run(
				Ops.Convert.Float64.class, input);

		final RandomAccessibleInterval<DoubleType> temp = opService.create().img(
			converted);

		// create copyOp instance for faster calculations
		final UnaryFunctionOp<RandomAccessibleInterval<DoubleType>, RandomAccessibleInterval<DoubleType>> copyOp =
			RAIs.function(opService, Ops.Copy.RAI.class, converted);

		// create partial derivative gaussian op for faster calculations
		final BinaryComputerOp<RandomAccessibleInterval<DoubleType>, int[], RandomAccessibleInterval<DoubleType>> partialDerivativeOp =
			Computers.binary(opService, Ops.Filter.DerivativeGauss.class, converted,
				temp, new int[] { 0, 0 }, new double[] { sigma, sigma });

		// create dimensions array for p and n values images, later used for setting
		// positions for their randomAccesses
		final long[] valuesArr = new long[input.numDimensions() + 1];
		for (int d = 0; d < input.numDimensions(); d++)
			valuesArr[d] = input.dimension(d);
		valuesArr[valuesArr.length - 1] = 2;

		// create metadata images and randomAccesses
		pValues = opService.create().img(valuesArr);
		final RandomAccess<DoubleType> pRA = pValues.randomAccess();
		nValues = opService.create().img(valuesArr);
		final RandomAccess<DoubleType> nRA = nValues.randomAccess();
		gradients = opService.create().img(input, new DoubleType());
		final RandomAccess<DoubleType> gradientsRA = gradients.randomAccess();

		// create a cursor of the input to direct all of the randomAccesses.
		final Cursor<T> cursor = Views.iterable(input).localizingCursor();

		// create partial derivative images, randomAccesses
		x = copyOp.calculate(converted);
		final RandomAccess<DoubleType> xRA = x.randomAccess();
		y = copyOp.calculate(converted);
		final RandomAccess<DoubleType> yRA = y.randomAccess();
		xx = copyOp.calculate(converted);
		final RandomAccess<DoubleType> xxRA = xx.randomAccess();
		xy = copyOp.calculate(converted);
		final RandomAccess<DoubleType> xyRA = xy.randomAccess();
		yy = copyOp.calculate(converted);
		final RandomAccess<DoubleType> yyRA = yy.randomAccess();

		// fill partial derivative images with gaussian derivative convolutions
		partialDerivativeOp.compute(converted, new int[] { 1, 0 }, x);
		partialDerivativeOp.compute(converted, new int[] { 2, 0 }, xx);
		partialDerivativeOp.compute(converted, new int[] { 1, 1 }, xy);
		partialDerivativeOp.compute(converted, new int[] { 0, 1 }, y);
		partialDerivativeOp.compute(converted, new int[] { 0, 2 }, yy);

		// loop through the points, fill in potentialPoints with second directional
		// derivative across the line, eigenx with the x component of the normal
		// vector to the line, and eigeny with the y component of that vector.
		while (cursor.hasNext()) {
			cursor.fwd();
			xRA.setPosition(cursor);
			yRA.setPosition(cursor);
			xxRA.setPosition(cursor);
			xyRA.setPosition(cursor);
			yyRA.setPosition(cursor);

			// Get all of the values needed for the point.
			final double rx = xRA.get().getRealDouble();
			final double ry = yRA.get().getRealDouble();
			final double rxx = xxRA.get().getRealDouble();
			final double rxy = xyRA.get().getRealDouble();
			final double ryy = yyRA.get().getRealDouble();

			// convolve image with 2D partial kernel,
			// make a Hessian using the kernels
			final Matrix hessian = new Matrix(input.numDimensions(), input
				.numDimensions());
			hessian.set(0, 0, xxRA.get().getRealDouble());
			hessian.set(0, 1, xyRA.get().getRealDouble());
			hessian.set(1, 0, xyRA.get().getRealDouble());
			hessian.set(1, 1, yyRA.get().getRealDouble());

			// Jacobian rotation to eliminate rxy
			final EigenvalueDecomposition e = hessian.eig();
			final Matrix eigenvalues = e.getD();
			final Matrix eigenvectors = e.getV();

			// since the eigenvalues matrix is diagonal, find the index of the largest
			// eigenvalue
			final int index = Math.abs(eigenvalues.get(0, 0)) > Math.abs(eigenvalues
				.get(1, 1)) ? 0 : 1;

			// get (nx, ny), i.e. the components of a vector perpendicular to our
			// line, with length of one.
			final double nx = eigenvectors.get(0, index);
			final double ny = eigenvectors.get(1, index);

			// obtain (px, py), the point in subpixel space where the first
			// directional derivative vanishes.
			final double t = -1 * (rx * nx + ry * ny) / (rxx * nx * nx + 2 * rxy *
				nx * ny + ryy * ny * ny);
			final double px = t * nx;
			final double py = t * ny;

			// so long as the absolute values of px and py are below 0.5, this point
			// is a line point.
			if (Math.abs(px) <= 0.5 && Math.abs(py) <= 0.5) {

				// create long array for setting position of pValues and nValues
				valuesArr[0] = cursor.getLongPosition(0);
				valuesArr[1] = cursor.getLongPosition(1);
				valuesArr[2] = 0;

				// set px to the first z slice of pValues
				pRA.setPosition(valuesArr);
				pRA.get().set(px);

				// set py to the second z slice of pValues
				pRA.fwd(2);
				pRA.get().set(py);

				// set nx t othe first z slice of nValues
				nRA.setPosition(valuesArr);
				nRA.get().set(nx);

				// set ny to the second z slice of nValues
				nRA.fwd(2);
				nRA.get().set(ny);

				// the eigenvalue is equal to the gradient at that pixel. If a large
				// negative, we are on a line. Otherwise 0.
				final double gradient = eigenvalues.get(index, index) < -smallMax ? Math
					.abs(eigenvalues.get(index, index)) : 0;

				// set the gradient
				gradientsRA.setPosition(cursor);
				gradientsRA.get().set(gradient);
			}

		}
	}

	/**
	 * returns the pValue image
	 *
	 * @return the image containing the pValues
	 */
	protected Img<DoubleType> getPValues() {
		return pValues;
	}

	/**
	 * returns the pValue image's RandomAccess
	 *
	 * @return the RandomAccess containing the pValues
	 */
	protected RandomAccess<DoubleType> getPValuesRandomAccess() {
		return pValues.randomAccess();
	}

	/**
	 * returns the nValue image
	 *
	 * @return the image containing the nValues
	 */
	protected Img<DoubleType> getNValues() {
		return nValues;
	}

	/**
	 * returns the nValue image's RandomAccess
	 *
	 * @return the RandomAccess containing the nValues
	 */
	protected RandomAccess<DoubleType> getNValuesRandomAccess() {
		return nValues.randomAccess();
	}

	/**
	 * returns the gradient image
	 *
	 * @return the image containing the gradients
	 */
	protected Img<DoubleType> getGradients() {
		return gradients;
	}

	/**
	 * returns the gradient image's RandomAccess
	 *
	 * @return the RandomAccess containing the gradients
	 */
	protected RandomAccess<DoubleType> getGradientsRandomAccess() {
		return gradients.randomAccess();
	}
}
