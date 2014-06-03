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
package net.imagej.ops.descriptors.moments.zernike;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.geometric.CenterOfGravity;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Andreas Graumann (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * 
 *         TOOD: (a) validate results. (b) does it make sense on "non-bittype"
 *         images?
 */
@Plugin(type = Op.class, label = "Zernike Moments 2D", name = "zernikemoments")
public class ZernikeMomentComputer implements OutputOp<double[]> {

	@Parameter(label = "Zernike Order", min = "1", max = "10", stepSize = "1", initializer = "3")
	private int order;

	@Parameter
	private IterableInterval<? extends RealType<?>> ii;

	@Parameter
	private CenterOfGravity center;

	@Parameter(type = ItemIO.OUTPUT)
	private double[] output;

	@Override
	public double[] getOutput() {
		return output;
	}

	@Override
	public void run() {
		final List<Double> fR = new ArrayList<Double>();

		for (int o = 0; o <= order; o++) {
			final List<Integer> list = computeRepitionsOfOrder(o);

			for (int i = 0; i < list.size(); i++) {
				final double[] val = this.computeMoment(o, list.get(i));
				fR.add(val[0]);
				fR.add(val[1]);
			}
		}

		final double[] finalResult = new double[fR.size()];
		for (int i = 0; i < fR.size(); i++)
			finalResult[i] = fR.get(i);

		output = finalResult;
	}

	private List<Integer> computeRepitionsOfOrder(final int n) {

		final List<Integer> list = new ArrayList<Integer>();
		int m = 0;
		while (m <= n) {
			if ((n - m) % 2 == 0)
				list.add(m);
			m++;
		}

		return list;
	}

	/**
	 * implements the actual algoritm.
	 * 
	 * @return the complex value of the Zernike moment
	 */
	protected double[] computeMoment(final int _n, final int _m) {

		double real = 0;
		double imag = 0;

		// order
		final int n = _n;

		// repetition
		final int m = _m;

		if ((n < 0) || (((n - Math.abs(m)) % 2) != 0) || (Math.abs(m) > n))
			throw new IllegalArgumentException("n and m do not satisfy the"
					+ "Zernike moment properties");

		final double centerX = center.getOutput()[0];
		final double centerY = center.getOutput()[1];
		final double max = Math.max(centerX, centerY);
		final double radius = Math.sqrt(2 * max * max);

		final Polynom polynomOrthogonalRadial = createR(n, m);

		final Cursor<? extends RealType<?>> it = ii.localizingCursor();

		final double minVal = it.get().getMinValue();
		final double maxVal = it.get().getMaxValue();

		while (it.hasNext()) {
			it.fwd();
			final double x = it.getIntPosition(0) - centerX;
			final double y = it.getIntPosition(1) - centerY;

			// compute polar coordinates for x and y
			final double r = Math.sqrt((x * x) + (y * y)) / radius;
			final double ang = m * Math.atan2(y, x);

			final double value = polynomOrthogonalRadial.evaluate(r);
			final double pixel = (it.get().getRealDouble() - minVal)
					/ (maxVal - minVal);

			real += pixel * value * Math.cos(ang);
			imag -= pixel * value * Math.sin(ang);
		}

		real = (real * (n + 1)) / Math.PI;
		imag = (imag * (n + 1)) / Math.PI;

		final double[] res = { real, imag };
		return res;
	}

	/**
	 * create the polynom R_mn. see zernike documentation for more.
	 * 
	 * @param n
	 *            the "order"
	 * @param m
	 *            the "repetition"
	 * @return the F polynom
	 */
	public static Polynom createR(final int n, final int m) {
		final Polynom result = new Polynom(n);
		int sign = 1;
		for (int s = 0; s <= ((n - Math.abs(m)) / 2); ++s) {
			final int pos = n - (2 * s);
			result.setCoefficient(pos, sign * computeF(n, m, s));
			sign = -sign;
		}
		return result;
	}

	/**
	 * compute F(n, m, s). see zernike documentation for more.
	 * 
	 * @param n
	 *            the "order"
	 * @param m
	 *            the "repetition"
	 * @param s
	 *            the index
	 * @return the Coefficient of r^(n-2*s) from R_mn(r)
	 */
	public static int computeF(final int n, final int m, final int s) {
		assert ((n + Math.abs(m)) % 2) == 0;
		assert ((n - Math.abs(m)) % 2) == 0;
		assert (n - Math.abs(m)) >= 0;
		assert (((n - Math.abs(m)) / 2) - s) >= 0;

		final int absN = Math.abs(m);

		final FactComputer fc = new FactComputer(n);
		fc.multiplyByFactorialOf(n - s);
		fc.divideByFactorialOf(s);
		fc.divideByFactorialOf(((n + absN) / 2) - s);
		fc.divideByFactorialOf(((n - absN) / 2) - s);

		return fc.value();
	}
}
