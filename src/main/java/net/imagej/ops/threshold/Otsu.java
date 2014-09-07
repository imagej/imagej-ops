/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
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

package net.imagej.ops.threshold;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.OpService;
import net.imagej.ops.histogram.HistogramCreate;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = GlobalThresholdMethod.class, name = "otsu")
public class Otsu<T extends RealType<T>> extends
		AbstractFunction<Iterable<T>, T> implements
		GlobalThresholdMethod<Iterable<T>, T> {

	@Parameter
	private OpService ops;

	@Override
	public T compute(Iterable<T> input, T output) {

		// TODO how to handle service == null?
		@SuppressWarnings("unchecked")
		Histogram1d<T> hist = (Histogram1d<T>) ops.run(HistogramCreate.class,
				input);

		final long[] data = hist.toLongArray();
		final int maxValue = (int) hist.getBinCount() - 1;

		// Otsu's threshold algorithm
		// C++ code by Jordan Bevik <Jordan.Bevic@qtiworld.com>
		// ported to ImageJ plugin by G.Landini
		int k, kStar; // k = the current threshold; kStar = optimal
		// threshold
		long n1, n; // N1 = # points with intensity <=k; N = total number
		// of
		// points
		double BCV, BCVmax; // The current Between Class Variance and
		// maximum
		// BCV
		double num, denom; // temporary bookeeping
		int sk; // The total intensity for all histogram points <=k
		int s; // The total intensity of the image
		final int L = maxValue + 1;

		// Initialize values:
		s = 0;
		n = 0;
		for (k = 0; k < L; k++) {
			s += k * data[k]; // Total histogram intensity
			n += data[k]; // Total number of data points
		}

		sk = 0;
		n1 = data[0]; // The entry for zero intensity
		BCV = 0;
		BCVmax = 0;
		kStar = 0;

		// Look at each possible threshold value,
		// calculate the between-class variance, and decide if it's a
		// max
		for (k = 1; k < (L - 1); k++) { // No need to check endpoints k =
			// 0 or k =
			// L-1
			sk += k * data[k];
			n1 += data[k];

			// The float casting here is to avoid compiler warning
			// about loss of
			// precision and
			// will prevent overflow in the case of large saturated
			// images
			denom = (double) (n1) * (n - n1); // Maximum value of
			// denom is
			// (N^2)/4 = approx. 3E10

			if (denom != 0) {
				// Float here is to avoid loss of precision when
				// dividing
				num = (((double) n1 / n) * s) - sk; // Maximum
				// value of
				// num =
				// MAX_VALUE*N = approx 8E7
				BCV = (num * num) / denom;
			} else {
				BCV = 0;
			}

			if (BCV >= BCVmax) { // Assign the best threshold found
				// so far
				BCVmax = BCV;
				kStar = k;
			}
		}
		// kStar += 1; // Use QTI convention that intensity -> 1 if
		// intensity >=
		// k
		// (the algorithm was developed for I-> 1 if I <= k.)

		// at this point the threshold is expressed as a bin number. Convert bin
		// number to corresponding
		// gray level
		hist.getCenterValue(kStar, output);
		return output;
	}
}
