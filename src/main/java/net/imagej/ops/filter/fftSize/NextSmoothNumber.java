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

package net.imagej.ops.filter.fftSize;

/**
 * A class to determine the next <i>k</i>-smooth number (i.e. a number divisible
 * only by prime numbers up to <i>k</i>), given a lower bound <i>n</i>. Based on
 * A. Granville, <i>Smooth numbers: computational number theory and beyond</i>:
 * <blockquote> 5.7. <b>Finding smooth numbers computationally.</b> The obvious
 * way to find y-smooth numbers in (x; x + z) with z &le; x is to initialize an
 * array a[i] := 0 for 1 &le; i &le; z (where a[i] corresponds to x + i). For
 * each successively larger prime power p<sup>j</sup> &le; x + z with p &le; y,
 * determine the smallest i such that p<sup>j</sup> divides x + i and then add
 * log(p) to a[i], a[i + p<sup>j</sup>], a[i + 2p<sup>j</sup>] and so on, up
 * until the end of the array. When we’ve finished, if any a[i] &ge; log(x),
 * then x + i is y-smooth. </blockquote>
 * 
 * @author Johannes Schindelin
 * @author Brian Northan
 */
public class NextSmoothNumber {

	public static int nextSmooth(int x) {
		int result = -1;
		int z = 50;
		while (result == -1) {
			result = nextSmooth(7, x, z);
			x = x + z;
		}
		return result;
	}

	public static int nextSmooth(final int y, final int x, final int z) {
		double delta=0.000001;
		
		final double[] a = new double[z];
		handlePrime(2, x, a);
		handlePrime(3, x, a);
		handlePrime(5, x, a);
		handlePrime(7, x, a);
		double log = Math.log(x);
		for (int i = 0; i < a.length; i++) {
			if (a[i]  >= log - delta) return x + i;
		}
		// System.err.println(Arrays.toString(a));
		// System.err.println(a[32805 - x]);
		return -1;
	}

	private static void handlePrime(final int p, final int x, final double[] a) {
		double log = Math.log(p);
		for (int power = p; power <= x + a.length; power *= p) {
			int j = x % power;
			if (j > 0) j = power - j;
			while (j < a.length) {
				a[j] += log;
				j += power;
			}
		}
	}

	private static int log2(int x) {
		for (int j = 0, k = 1;; j++, k *= 2) {
			if (k >= x) return j;
		}
	}

	public static void main(String... args) {
		int x = args.length == 0 ? 32769 : Integer.parseInt(args[0]);
		System.err.println(log2(x));

		int result = nextSmooth(7, x, 4 * log2(x));

		System.out.println(result);
	}
}
