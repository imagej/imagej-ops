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

package net.imagej.ops.filter.derivativeGauss;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Performs the 2-D partial derivative Gaussian kernel convolutions on an image,
 * at a particular point.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Filter.DerivativeGauss.class)
public class DefaultDerivativeGauss<T extends RealType<T>> extends
	AbstractBinaryComputerOp<RandomAccessibleInterval<T>, int[], RandomAccessibleInterval<DoubleType>>
	implements Ops.Filter.DerivativeGauss, Contingent
{

	@Parameter
	private double sigma;

	double SQRT2PI = Math.sqrt(2 * Math.PI);

	/**
	 * Calculates a value at a specified location in a normal mask.
	 * 
	 * @param x - the location in the mask.
	 * @param sigma - the sigma for the convolution.
	 * @return double - the value of the mask at location x.
	 */
	private double phi0(double x, double sigma) {
		double t = x / sigma;
		return (-sigma * Math.exp(-0.5 * t * t) / (SQRT2PI * x));
	}

	/**
	 * Calculates a value at a specified location in a first partial derivative
	 * mask.
	 * 
	 * @param x - the location in the mask.
	 * @param sigma - the sigma for the convolution.
	 * @return double - the value of the mask at location x.
	 */
	private double phi1(double x, double sigma) {
		double t = x / sigma;
		return (Math.exp(-0.5 * t * t) / (SQRT2PI * sigma));
	}

	/**
	 * Calculates a value at a specified location in a second partial derivative
	 * mask.
	 * 
	 * @param x - the location in the mask.
	 * @param sigma - the sigma for the convolution.
	 * @return double - the value of the mask at location x.
	 */
	private double phi2(double x, double sigma) {
		double t = x / sigma;
		return (-x * Math.exp(-0.5 * t * t) / (SQRT2PI * Math.pow(sigma, 3)));
	}

	/**
	 * Creates the mask for normal convolutions
	 * 
	 * @param sigma - The sigma for the convolution.
	 * @return double[] - The mask.
	 */
	private double[] get_mask_0(double sigma) {

		int x = (int) Math.ceil(4 * sigma);
		double[] h = new double[2 * x + 1];

		for (int i = -x + 1; i < x; i++) {
			h[i + x] = Math.abs(phi0(i + 0.5, sigma) - phi0(i - 0.5, sigma));
		}
		h[0] = phi0(-x + 0.5, sigma);
		h[h.length - 1] = phi0(-x + 0.5, sigma);
		return h;
	}

	/**
	 * Creates the mask for first partial derivative convolutions
	 * 
	 * @param sigma - The sigma for the convolution.
	 * @return double[] - The mask.
	 */
	private double[] get_mask_1(double sigma) {

		int x = (int) Math.ceil(4 * sigma);
		double[] h = new double[2 * x + 1];

		for (int i = -x + 1; i < x; i++) {
			h[i + x] = phi1(-i + 0.5, sigma) - phi1(-i - 0.5, sigma);
		}
		h[0] = -phi1(x - 0.5, sigma);
		h[h.length - 1] = phi1(-x + 0.5, sigma);
		return h;
	}

	/**
	 * Creates the mask for second partial derivative convolutions
	 * 
	 * @param sigma - The sigma for the convolution.
	 * @return double[] - The mask.
	 */
	private double[] get_mask_2(double sigma) {

		int x = (int) Math.ceil(4 * sigma);
		double[] h = new double[2 * x + 1];

		for (int i = -x + 1; i < x; i++) {
			h[i + x] = phi2(-i + 0.5, sigma) - phi2(-i - 0.5, sigma);
		}
		h[0] = -phi2(-x + 0.5, sigma);
		h[h.length - 1] = phi2(x - 0.5, sigma);
		return h;
	}

	/**
	 * Returns the correct mask of nth partial derivative. Leaves the calculations
	 * to the helper methods.
	 * 
	 * @param sigma - The sigma for the convolution.
	 * @param n - A number specifying the nth partial derivative.
	 * @return double[] - The mask.
	 */
	private double[] get_mask_general(int n, double sigma) {
		double[] h;
		switch (n) {
			case 0:
				h = get_mask_0(sigma);
				break;
			case 1:
				h = get_mask_1(sigma);
				break;
			case 2:
				h = get_mask_2(sigma);
				break;
			default:
				h = get_mask_0(sigma);
				break;
		}
		return h;
	}

	/**
	 * Convolves the columns of the image
	 * 
	 * @param input - The input image.
	 * @param output - The output image.
	 * @param mask - The mask needed for the convolution, determined beforehand.
	 */
	private <T extends RealType<T>> void convolve_x(
		RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output,
		double[] mask)
	{
		double sum;
		Cursor<T> cursor = Views.iterable(input).localizingCursor();
		OutOfBoundsMirrorFactory<T, RandomAccessibleInterval<T>> osmf =
			new OutOfBoundsMirrorFactory<>(Boundary.SINGLE);
		RandomAccess<T> inputRA = osmf.create(input);
		RandomAccess<T> outputRA = output.randomAccess();

		while (cursor.hasNext()) {
			cursor.fwd();
			inputRA.setPosition(cursor);
			outputRA.setPosition(cursor);
			sum = 0;
			// loop from the bottom of the image to the top
			final int halfWidth = mask.length / 2;
			for (int i = -halfWidth; i <= halfWidth; i++) {
				inputRA.setPosition(cursor.getLongPosition(0) + i, 0);
				inputRA.setPosition(cursor.getLongPosition(1), 1);
				sum += inputRA.get().getRealDouble() * mask[i + halfWidth];
			}
			outputRA.get().setReal(sum);
		}

	}

	/**
	 * Convolves the rows of the image
	 * 
	 * @param input - The input image.
	 * @param output - The output image.
	 * @param mask - The mask needed for the convolution, determined beforehand.
	 */
	private <T extends RealType<T>> void convolve_y(
		RandomAccessibleInterval<T> input, RandomAccessibleInterval<DoubleType> output,
		double[] mask)
	{
		double sum;
		Cursor<T> cursor = Views.iterable(input).localizingCursor();
		OutOfBoundsMirrorFactory<T, RandomAccessibleInterval<T>> osmf =
			new OutOfBoundsMirrorFactory<>(Boundary.SINGLE);
		RandomAccess<T> inputRA = osmf.create(input);
		RandomAccess<DoubleType> outputRA = output.randomAccess();

		while (cursor.hasNext()) {
			cursor.fwd();
			inputRA.setPosition(cursor);
			outputRA.setPosition(cursor);
			sum = 0;
			// loop from the bottom of the image to the top
			final int halfWidth = mask.length / 2;
			for (int i = -halfWidth; i <= halfWidth; i++) {
				inputRA.setPosition(cursor.getLongPosition(0), 0);
				inputRA.setPosition(cursor.getLongPosition(1) + i, 1);
				sum += inputRA.get().getRealDouble() * mask[i + halfWidth];
			}
			outputRA.get().setReal(sum);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(RandomAccessibleInterval<T> input, int[] derivatives,
		RandomAccessibleInterval<DoubleType> output)
	{
		RandomAccessibleInterval<T> intermediate =
			(RandomAccessibleInterval<T>) ops().run(Ops.Copy.RAI.class, output);

		// TODO n-Dimensional support.
		convolve_x(input, intermediate, get_mask_general(derivatives[0], sigma));
		convolve_y(intermediate, output, get_mask_general(derivatives[1], sigma));
	}

	@Override
	public boolean conforms() {
		return (in1().numDimensions() == in2().length && out()
			.numDimensions() == in1().numDimensions());
	}
}
