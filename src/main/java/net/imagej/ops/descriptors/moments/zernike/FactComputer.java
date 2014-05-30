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

class FactComputer {
	/** the highest number this thing can handle. */
	private final int m_high;

	/** array of prime numbers up to m_high. */
	private final ArrayList<Integer> m_primes;

	/** array to memorize the current value. */
	private final int[] m_values;

	/**
	 * test for primality.
	 * 
	 * @param n
	 *            the number to test
	 * @return is the number prime?
	 */
	private boolean isPrime(final int n) {
		for (int i = 0; i < m_primes.size(); ++i) {
			if ((n % m_primes.get(i)) == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * default constructor.
	 * 
	 * @param high
	 *            the highest number we want to handle
	 */
	public FactComputer(final int high) {
		m_high = high;

		m_primes = new ArrayList<Integer>();
		for (int i = 2; i <= high; ++i) {
			if (isPrime(i)) {
				m_primes.add(i);
			}
		}
		m_values = new int[m_primes.size()];
	}

	/**
	 * multiply the result by a single number (the number given as parameter).
	 * 
	 * @param nParam
	 *            the number
	 */
	public void multiplyBy(final int nParam) {
		int n = nParam;
		if ((nParam < 0) || (nParam > m_high)) {
			throw new IllegalArgumentException(
					"Multiplication with out of range number!");
		}
		for (int i = 0; i < m_primes.size(); ++i) {
			while ((n % m_primes.get(i)) == 0) {
				n /= m_primes.get(i);
				m_values[i]++;
			}
		}
	}

	/**
	 * divide the result by a single number (the number given as parameter).
	 * 
	 * @param nParam
	 *            the number
	 */
	public void divideBy(final int nParam) {
		int n = nParam;
		if ((nParam < 0) || (nParam > m_high)) {
			throw new IllegalArgumentException(
					"Division with out of range number!");
		}
		for (int i = 0; i < m_primes.size(); ++i) {
			while ((n % m_primes.get(i)) == 0) {
				n /= m_primes.get(i);
				m_values[i]--;
			}
		}
	}

	/**
	 * multiply the result by the factorial of the number given as parameter.
	 * 
	 * @param n
	 *            the factorial
	 */
	public void multiplyByFactorialOf(final int n) {
		if ((n < 0) || (n > m_high)) {
			throw new IllegalArgumentException(
					"Not able to handle multiplication by " + " factorial of "
							+ n);
		}
		for (int i = 2; i <= n; ++i) {
			multiplyBy(i);
		}
	}

	/**
	 * divide the result by the factorial of the number given as parameter.
	 * 
	 * @param n
	 *            the factorial
	 */
	public void divideByFactorialOf(final int n) {
		if ((n < 0) || (n > m_high)) {
			throw new IllegalArgumentException(
					"Not able to handle division by factorial of " + n);
		}
		for (int i = 2; i <= n; ++i) {
			divideBy(i);
		}
	}

	/**
	 * compute the current value kept here.
	 * 
	 * @return the value computed so far
	 */
	public int value() {
		int result = 1;
		for (int i = 0; i < m_primes.size(); ++i) {
			if (m_values[i] < 0) {
				throw new IllegalArgumentException("Result is not an integer");
			}
			for (int j = 0; j < m_values[i]; ++j) {
				final int oldResult = result;
				result *= m_primes.get(i);
				if ((result / m_primes.get(i)) != oldResult) {
					throw new IllegalArgumentException(
							"Overflow while computing factorial!");
				}
			}
		}
		return result;
	}
}
