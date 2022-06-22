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

package net.imagej.ops.filter.vesselness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * Input is a 2- or 3-dimensional grayscales image. Applies the Frangi
 * Vesselness filter to an image to highlight vessel-like structures. The
 * spacing parameter refers to the physical distance between data points in the
 * RandomAccessibleInterval, and can vary for each dimension of the image.
 *
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Filter.FrangiVesselness.class)
public class DefaultFrangi<T extends RealType<T>, U extends RealType<U>> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<U>>
	implements Ops.Filter.FrangiVesselness
{

	public final static int MIN_DIMS = 2;

	public final static int MAX_DIMS = 3;

	@Parameter
	private double[] spacing;

	@Parameter
	private int scale;

	protected double alpha = 0.5;
	protected double beta = 0.5;

	protected double minimumVesselness = Double.MIN_VALUE;
	protected double maximumVesselness = Double.MAX_VALUE;

	public double getMinimumVesselness() {
		return minimumVesselness;
	}

	public double getMaximumVesselness() {
		return maximumVesselness;
	}

	private double getDistance(RandomAccess<T> ahead, RandomAccess<T> behind,
		int d)
	{
		double distance = 0;

		for (int i = 0; i < d; i++) {
			double separation = (ahead.getLongPosition(i) - behind.getLongPosition(
				i)) * spacing[i];
			if (separation != 0) {
				distance += (separation * separation);
			}

		}

		return Math.sqrt(distance);
	}

	private double derive(double val1, double val2, double distance) {
		return ((val2 - val1) / distance);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<U> output)
	{

		// set spacing if the parameter is not passed.
		if (spacing == null) {
			spacing = new double[input.numDimensions()];
			for (int i = 0; i < input.numDimensions(); i++)
				spacing[i] = 1;
		}

		frangi(input, output, scale);
	}

	private final void frangi(RandomAccessibleInterval<T> in,
		RandomAccessibleInterval<U> out, int step)
	{

		// create denominators used for gaussians later.
		double ad = 2 * alpha * alpha;
		double bd = 2 * beta * beta;

		// OutOfBoundsMirrorStrategy for use when the cursor reaches the edges.
		OutOfBoundsMirrorFactory<T, RandomAccessibleInterval<T>> osmf =
			new OutOfBoundsMirrorFactory<T, RandomAccessibleInterval<T>>(
				Boundary.SINGLE);

		Cursor<T> cursor = Views.iterable(in).localizingCursor();

		Matrix hessian = new Matrix(in.numDimensions(), in.numDimensions());

		// use three RandomAccess<T> Objects to find the values needed to calculate
		// the
		// second derivatives.
		RandomAccess<T> current = osmf.create(in);
		RandomAccess<T> behind = osmf.create(in);
		RandomAccess<T> ahead = osmf.create(in);

		RandomAccess<U> outputRA = out.randomAccess();

		while (cursor.hasNext()) {

			cursor.fwd();

			// calculate the hessian
			for (int m = 0; m < in.numDimensions(); m++) {
				for (int n = 0; n < in.numDimensions(); n++) {

					current.setPosition(cursor);
					ahead.setPosition(cursor);
					behind.setPosition(cursor);

					// move one behind to take the first derivative
					behind.move(-step, m);
					if (m != n) behind.move(-step, n);

					// take the derivative between the two points
					double derivativeA = derive(behind.get().getRealDouble(), current
						.get().getRealDouble(), getDistance(behind, current, in
							.numDimensions()));

					// move one ahead to take the other first derivative
					ahead.move(step, m);
					if (m != n) ahead.move(step, n);

					// take the derivative between the two points
					double derivativeB = derive(current.get().getRealDouble(), ahead.get()
						.getRealDouble(), getDistance(current, ahead, in.numDimensions()));

					// take the second derivative using the two first derivatives
					double derivative2 = derive(derivativeA, derivativeB, getDistance(
						behind, ahead, in.numDimensions()));

					hessian.set(m, n, derivative2);

				}
			}

			// find the FrobeniusNorm (used later)
			double s = hessian.normF();
			double cn = -(s * s);

			// find and sort the eigenvalues and eigenvectors of the Hessian
			EigenvalueDecomposition e = hessian.eig();
			double[] eigenvaluesArray = e.getRealEigenvalues();
			ArrayList<Double> eigenvaluesArrayList = new ArrayList<Double>();
			for (double d : eigenvaluesArray)
				eigenvaluesArrayList.add(d);
			Collections.sort(eigenvaluesArrayList, Comparator.comparingDouble(
				Math::abs));

			// vesselness value
			double v = 0;

			if (in.numDimensions() == 2) {
				double c = 15;
				double cd = 2 * c * c;

				// lambda values
				double l1 = eigenvaluesArrayList.get(0);
				double al1 = Math.abs(l1);
				double l2 = eigenvaluesArrayList.get(1);
				double al2 = Math.abs(l2);

				// Check to see if the point is on a tubular structure.
				if (l2 < 0) {

					// ratio Rb
					double rb = al1 / al2;

					// values for ease of final calculation
					double bn = -(rb * rb);
					v = Math.exp(bn / bd) * (1 - Math.exp(cn / cd));
				}
			}
			else if (in.numDimensions() == 3) {
				double c = 200;
				double cd = 2 * c * c;

				// lambda values
				double l1 = eigenvaluesArrayList.get(0);
				double al1 = Math.abs(l1);
				double l2 = eigenvaluesArrayList.get(1);
				double al2 = Math.abs(l2);
				double l3 = eigenvaluesArrayList.get(2);
				double al3 = Math.abs(l3);

				// Check to see if the point is on a tubular structure.
				/*
				 * N.B. This conditional statement only takes into account the sign 
				 * on the third-smallest eigenvalue, not both the second and third as 
				 * described in the paper. Original versions of this filter took the
				 * signs of both into account, but the result of the filter was an 
				 * empty image. Only by removing the condition of the sign of the 
				 * second eigenvalue were we able to obtain results that matched 
				 * human expectations of a 3-D version of the filter. This conditional
				 * in particular achieved results best aligned with human expectation.
				 */
				if (l3 < 0) {
					// ratios Rb and Ra
					double rb = al1 / Math.sqrt(al2 * al3);
					double ra = al2 / al3;

					// values for ease of final calculation
					double an = -(ra * ra);
					double bn = -(rb * rb);

//					System.out.println(l1 + "  " + l2 + "  " + l3);

					v = (1 - Math.exp(an / ad)) * Math.exp(bn / bd) * (1 - Math.exp(cn /
						cd));
				}

			}
			else throw new RuntimeException(
				"Currently only 2 or 3 dimensional images are supported");

			if (!Double.isNaN(v)) {
				maximumVesselness = Math.max(v, maximumVesselness);
				minimumVesselness = Math.min(v, minimumVesselness);
			}

			outputRA.setPosition(cursor);
			outputRA.get().setReal(v);
		}

	}

}
