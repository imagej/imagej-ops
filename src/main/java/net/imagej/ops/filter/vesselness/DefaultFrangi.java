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

package net.imagej.ops.filter.vesselness;


import java.util.ArrayList;
import java.util.Collections;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

/**
 * Applies the Frangi Vesselness filter to an image to highlight vessel-like
 * structures. The spacing parameter refers to the physical distance between
 * data points in the RandomAccessibleInterval, and can vary for each
 * dimension of the image.
 * 
 * @author Gabe Selzer
 */
public class DefaultFrangi<T extends RealType<T>>
		extends AbstractUnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		implements Ops.Filter.Vesselness {
	
	public final static int MIN_DIMS = 2;
	
	public final static int MAX_DIMS = 3;
	
	@Parameter
	private int scaleIndex;
	
	@Parameter
	private float[] spacing;
	
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

	private double getDistance(RandomAccess<T> ahead, RandomAccess<T> behind, int d) {
		double distance = 0;

		for (int i = 0; i < d; i++) {
			double separation = (ahead.getLongPosition(d) - behind.getLongPosition(d)) * spacing[d];
			if (separation != 0) {
				distance += (separation * separation);
			}

		}

		return Math.sqrt(distance);
	}

	private double derivative(double val1, double val2, double distance) {
		return ((val2 - val1) / distance);
	}
	
	public long numberOfPoints( RandomAccessibleInterval<T> image ) {
		long totalPoints = 1;
		long [] dimensions = null;
		image.dimensions(dimensions);
		for( long length : dimensions ) {
			totalPoints *= length;
		}
		return totalPoints;
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> input, final RandomAccessibleInterval<T> output) {

		// create denominators used for gaussians later.
		double ad = 2 * alpha * alpha;
		double bd = 2 * beta * beta;

		// set spacing if the parameter is not passed.
		if (spacing == null) {
			spacing = new float[input.numDimensions()];
			for (int i = 0; i < input.numDimensions(); i++)
				spacing[i] = 1;
		}

		// OutOfBoundsMirrorStrategy for use when the cursor reaches the edges.
		OutOfBoundsMirrorFactory<T, RandomAccessibleInterval<T>> osmf = new OutOfBoundsMirrorFactory<T, RandomAccessibleInterval<T>>(
				Boundary.SINGLE);

		Cursor<T> cursor = Views.iterable(input).localizingCursor();

		Matrix hessian = new Matrix(input.numDimensions(), input.numDimensions());

		// use three RandomAccess<T> Objects to find the values needed to calculate the
		// second derivatives.
		RandomAccess<T> current = osmf.create(input);
		RandomAccess<T> behind = osmf.create(input);
		RandomAccess<T> ahead = osmf.create(input);

		RandomAccess<T> outputRA = output.randomAccess();

		while (cursor.hasNext()) {

			cursor.fwd();

			// calculate the hessian
			for (int m = 0; m < input.numDimensions(); m++) {
				for (int n = 0; n < input.numDimensions(); n++) {

					current.setPosition(cursor);
					ahead.setPosition(cursor);
					behind.setPosition(cursor);

					// move one behind to take the first derivative
					behind.move(-1, m);
					if (m != n)
						behind.move(-1, n);

					// take the derivative between the two points
					double derivativeA = derivative(behind.get().getRealDouble(), current.get().getRealDouble(),
							getDistance(behind, current, input.numDimensions()));

					// move one ahead to take the other first derivative
					ahead.move(1, m);
					if (m != n)
						ahead.move(1, n);

					// take the derivative between the two points
					double derivativeB = derivative(current.get().getRealDouble(), ahead.get().getRealDouble(),
							getDistance(current, ahead, input.numDimensions()));

					// take the second derivative using the two first derivatives
					double derivative2 = derivative(derivativeA, derivativeB,
							getDistance(behind, ahead, input.numDimensions()));

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
			Collections.sort(eigenvaluesArrayList);

			// vesselness value
			double v = 0;

			if (input.numDimensions() == 2) {
				double c = 15;
				double cd = 2 * c * c;

				// lambda values
				double l1 = eigenvaluesArrayList.get(0);
				double l2 = eigenvaluesArrayList.get(1);

				// ratio Rb
				double rb = l1 / l2;

				// values for ease of final calculation
				double bn = -(rb * rb);

				if (l2 <= 0)
					v = Math.exp(bn / bd) * (1 - Math.exp(cn / cd));
			} else if (input.numDimensions() == 3) {
				double c = 500;
				double cd = 2 * c * c;

				// lambda values
				double l1 = eigenvaluesArrayList.get(0);
				double l2 = eigenvaluesArrayList.get(1);
				double l3 = eigenvaluesArrayList.get(2);

				// ratios Rb and Ra
				double rb = Math.abs(l1) / Math.sqrt(Math.abs(l2 * l3));
				double ra = Math.abs(l2) / Math.abs(l3);

				// values for ease of final calculation
				double an = -(ra * ra);
				double bn = -(rb * rb);

				if (l2 <= 0 && l3 <= 0)
					v = (1 - Math.exp(an / ad)) * Math.exp(bn / bd) * (1 - Math.exp(cn / cd));

			} else
				throw new RuntimeException("Currently only 2 or 3 dimensional images are supported");

			if (!Double.isNaN(v)) {
				maximumVesselness = Math.max(v, maximumVesselness);
				minimumVesselness = Math.min(v, minimumVesselness);
			}

			outputRA.setPosition(cursor);
			outputRA.get().setReal(v);

		}
	}

}
