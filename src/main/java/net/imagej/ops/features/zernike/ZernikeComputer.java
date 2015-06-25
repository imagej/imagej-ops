/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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
package net.imagej.ops.features.zernike;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.types.BigComplex;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Computes all Zernike Moments between a minimal and a maximal order
 * 
 * @author Andreas Graumann, University of Konstanz
 */
@Plugin(type = Op.class)
public class ZernikeComputer implements Op {

	@Parameter
	private IterableInterval<? extends RealType<?>> ii;

	@Parameter
	private int orderMin;

	@Parameter
	private int orderMax;

	// List to hold all desired zernike moments
	private List<ZernikeMoment> m_zernikeMoments;

	@Override
	public void run() {
		// initialize list
		m_zernikeMoments = new ArrayList<ZernikeMoment>();
		// compute all desired zernike moments
		fastZernikeComputation(orderMin, orderMax);
	}

	/**
	 * Fast computation of all zernike moments between a minimal and maximal
	 * order
	 * 
	 * @param _nMin
	 *            Minimal order
	 * @param _nMax
	 *            Maximal order
	 */
	public void fastZernikeComputation(int _nMin, int _nMax) {

		// what is the acutal N
		final double width = ii.max(0) - ii.min(0) + 1;
		final double height = ii.max(1) - ii.min(1) + 1;

		// Compute pascal's triangle for binomal coefficients: d[x][y] equals (x
		// over y)
		double[][] d = computePascalsTriangle(_nMax);

		// initialize list with all desired moments, including order and zernike
		// polynomials
		createListOfZernikeMoments(_nMin, _nMax, d);

		// get the cursor of the iterable interval
		final Cursor<? extends RealType<?>> cur = (Cursor<? extends RealType<?>>) ii
				.localizingCursor();

		// count number of pixel inside the unit circle
		int count = 0;

		// run over itarble interval
		while (cur.hasNext()) {
			cur.fwd();

			// get 2d centered coordinates
			final int x = (int) (cur.getIntPosition(0) - ii.min(0));
			final int y = (int) (cur.getIntPosition(1) - ii.min(1));

			// compute polar coordinates for x and y
			final double r = Math.sqrt(Math.pow(2 * x - width + 1, 2)
					+ Math.pow(2 * y - height + 1, 2))
					/ width;

			// calculate theta for this position
			final double theta = Math.atan2(height - 1 - 2 * y, 2 * x - width
					+ 1);

			// get current pixel value
			double pixel = cur.get().getRealDouble();
			if (pixel != 0.0) {
				pixel = 1.0;
			}

			// is pixel within unit circle?
			if (Math.abs(r) <= 1.0) {
				// increment number of pixel inside the unit circle
				count++;

				if (pixel >= 0.0) {

					// calculate all desired moments
					for (ZernikeMoment p : m_zernikeMoments) {
						// evaluate radial polynom at position r
						final double rad = p.getP().evaluate(r);

						// p * rad * exp(-1i * m * theta);
						final BigComplex product = multiplyExp(pixel, rad,
								theta, p.getM());

						// add together
						p.getZm().add(product);
					}
				}
			}
		}

		// normalization
		for (ZernikeMoment p : m_zernikeMoments) {
			normalize(p.getZm(), p.getN(), count);
		}

		 //print calculated moments
		 // printMoments(m_zernikeMoments);
	}

	/**
	 * 
	 * Multiplication of pixel * rad * exp(-m*theta) using eulers formula
	 * (pixel*rad) * (cos(m*theta) - i*sin(m*theta))
	 * 
	 * @param _pixel
	 *            Current pixel
	 * @param _rad
	 *            Computed value of radial polynom,
	 * @param _theta
	 *            Angle of current position
	 * @param _m
	 *            Repitition m
	 * @return Computed term
	 */
	private BigComplex multiplyExp(final double _pixel, final double _rad,
			final double _theta, final int _m) {
		BigComplex c = new BigComplex();
		c.setReal(_pixel * _rad * Math.cos(_m * _theta));
		c.setImag(-(_pixel * _rad * Math.sin(_m * _theta)));
		return c;
	}

	/**
	 * 
	 * Normalization of all calculated zernike moments in complex representation
	 * 
	 * @param _complex
	 *            Complex representation of zernike moment
	 * @param _n
	 *            Order n
	 * @param _count
	 *            Number of pixel within unit circle
	 */
	private void normalize(BigComplex _complex, int _n, int _count) {
		_complex.setReal(_complex.getRealDouble() * (_n + 1) / _count);
		_complex.setImag(_complex.getImaginaryDouble() * (_n + 1) / _count);
	}

	/**
	 * 
	 * Create a list with all zernike moments (order and repitition) between
	 * nMin and nMax
	 * 
	 * @param _nMin
	 *            Minimal order
	 * @param _nMax
	 *            Maximal order
	 * @param _d
	 *            Pascal matrix
	 */
	private void createListOfZernikeMoments(int _nMin, int _nMax, double[][] _d) {
		for (int n = _nMin; n <= _nMax; n++) {
			for (int m = 0; m <= n; m++) {
				if ((n - Math.abs(m)) % 2 == 0) {
					m_zernikeMoments.add(createZernikeMoment(_d, n, m));
				}
			}
		}
	}

	/**
	 * 
	 * Create one zernike moment of order n and repitition m with suitable
	 * radial polynom
	 * 
	 * @param _d
	 *            Pascal matrix
	 * @param _n
	 *            Order n
	 * @param _m
	 *            Repition m
	 * @return Empty Zernike moment of order n and repition m
	 */
	private ZernikeMoment createZernikeMoment(double[][] _d, int _n, int _m) {
		ZernikeMoment p = new ZernikeMoment();
		p.setM(_m);
		p.setN(_n);
		p.setP(createRadialPolynom(_n, _m, _d));
		BigComplex complexNumber = new BigComplex();
		p.setZm(complexNumber);
		return p;
	}

	/**
	 * Prints all calculate Zernike Moments (Complex Number + Phase + Magnitude)
	 * 
	 * @param _polynomials
	 *            List of polynomials
	 */
	void printMoments(List<ZernikeMoment> _polynomials) {
		System.out.println("Print Zernike Moments:\n");

		for (ZernikeMoment p : _polynomials) {
			p.printMoment();
		}

	}

	/**
	 * Efficient calculation of pascal's triangle up to order max
	 * 
	 * @param _max
	 *            maximal order of pascal's triangle
	 * @return pascal's triangle
	 */
	private double[][] computePascalsTriangle(int _max) {
		double[][] d = new double[_max + 1][_max + 1];
		for (int n = 0; n <= _max; n++) {
			for (int k = 0; k <= n; k++) {
				if ((n == 0 && k == 0) || (n == k) || (k == 0)) {
					d[n][k] = 1.0;
					continue;
				} else {
					d[n][k] = (((double) n / (n - k))) * d[n - 1][k];
				}
			}
		}
		return d;
	}

	/**
	 * 
	 * @param _n
	 *            Order n
	 * @param _m
	 *            Repitition m
	 * @param _k
	 *            Radius k
	 * @param _d
	 *            Pascal matrix
	 * @return computed term
	 */
	public static int computeBinomialFactorial(final int _n, final int _m,
			final int _k, double[][] _d) {
		int fac1 = (int) _d[_n - _k][_k];
		int fac2 = (int) _d[_n - 2 * _k][((_n - _m) / 2) - _k];
		int sign = (int) Math.pow(-1, _k);

		return sign * fac1 * fac2;
	}

	/**
	 * 
	 * Creates a radial polynom for zernike moment with order n and repitition m
	 * 
	 * @param _n
	 *            Order n
	 * @param _m
	 *            Repitition m
	 * @param _d
	 *            Pascal matrix
	 * @return Radial polnom for moment of order n and repition m
	 */
	public static Polynom createRadialPolynom(final int _n, final int _m,
			final double[][] _d) {
		final Polynom result = new Polynom(_n);
		for (int s = 0; s <= ((_n - Math.abs(_m)) / 2); ++s) {
			final int pos = _n - (2 * s);
			result.setCoefficient(pos, computeBinomialFactorial(_n, _m, s, _d));
		}
		return result;
	}

	/**
	 * 
	 * @return List containing all computed angles
	 */
	public List<Double> getAngles() {
		// no computed moments available
		if (m_zernikeMoments.size() == 0)
			return null;

		// create result list
		List<Double> res = new ArrayList<Double>();

		for (ZernikeMoment mom : m_zernikeMoments)
			res.add(mom.getPhase());

		return res;
	}

	/**
	 * 
	 * @return List containing all computed magnitudes
	 */
	public List<Double> getMagnitudes() {
		// no computed moments available
		if (m_zernikeMoments.size() == 0)
			return null;

		// create result list
		List<Double> res = new ArrayList<Double>();

		for (ZernikeMoment mom : m_zernikeMoments)
			res.add(mom.getMagnitude());

		return res;
	}

	/**
	 * 
	 * @return List containing all zernike moments
	 */
	public List<ZernikeMoment> getAllZernikeMoments() {
		return m_zernikeMoments;
	}

	/**
	 * 
	 * Returns a specific moment
	 * 
	 * @param _n
	 *            Order n
	 * @param _m
	 *            Repition m
	 * @return Specific moment of order n and repition m if not calculated null
	 */
	public ZernikeMoment getSpecificMoment(int _n, int _m) {
		
		// linear search for suitable moment
		for (ZernikeMoment moment : m_zernikeMoments) {
			if(moment.getN() == _n && moment.getM() == _m)
				return moment;
		}
		
		// no suitable moment found
		// TODO: Calculation of specific moment!
		return null;
	}
}
