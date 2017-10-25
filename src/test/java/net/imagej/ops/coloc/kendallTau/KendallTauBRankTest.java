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

package net.imagej.ops.coloc.kendallTau;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.util.Iterator;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.IterablePair;
import net.imglib2.util.Pair;

import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.Ops.Coloc.KendallTau}.
 *
 * @author Ellen T Arena
 */
public class KendallTauBRankTest extends AbstractOpTest {

	// FIXME: Eliminate exhaustive layer in favor of all working tests, even if slow.
	private boolean exhaustive = false;

	@Test
	public void testKendallTauBRankSimple() {
		assumeTrue(!exhaustive);
		// From Armitage P, Berry G. Statistical Methods in Medical Research (3rd edition). Blackwell 1994, p. 466.
		assertTau(23.0 / 45.0, new int[] { 4, 10, 3, 1, 9, 2, 6, 7, 8, 5 }, new int[] { 5, 8, 6, 2, 10, 3, 9, 4, 7, 1 });
	}

	@Test
	public void testKendallTauBRankPathological() {
		assumeTrue(!exhaustive);
		assertTau(Double.NaN, new int[] { 1, 1, 1, 1 }, new int[] { 2, 2, 2, 2 });
	}

	@Test
	public void testKendallTauBRankSomeDuplicates() {
		assumeTrue(!exhaustive);
		// for pairs (1, 3), (1, 2), (2, 1), (3, 1),
		// n = 4,
		// n0 = n * (n - 1) / 2 = 4 * 3 / 2 = 6
		// n1 = 1 + 0 + 0 + 0 = 1
		// n2 = 1 + 0 + 0 + 0 = 1
		// nc = #{ } = 0
		// nd = #{ (1, 3)x(2, 1), (1, 3)x(3, 1), (1, 2)x(2, 1), (1, 2)x(3, 1) } = 4
		// therefore Tau_b = -4 / sqrt(5 * 5) = -0.8
		assertTau(-0.8, new int[] { 1, 1, 2, 3 }, new int[] { 3, 2, 1, 1 });
	}
	
	@Test
	public void exhaustiveKendallTauBRankTesting() {
		assumeTrue(exhaustive);
		final int n = 5, m = 10;
		final int[] values1 = new int[n], values2 = new int[n];
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < n; j++) {
				values1[j] = Math.abs(pseudoRandom()) % m;
				values2[j] = Math.abs(pseudoRandom()) % m;
			}
			
			//final PairIterator<DoubleType> iter = pairIterator(values1, values2);
			final Iterable<Pair<IntType, IntType>> iter = new IterablePair<>(ArrayImgs.ints(values1, n), ArrayImgs.ints(values2, n));
			double kendallValue1 = calculateNaive(iter.iterator());
			double kendallValue2 = (Double) ops.run(KendallTauBRank.class, values1, values2);
			if (Double.isNaN(kendallValue1)) {
				assertTrue("i: " + i + ", value2: " + kendallValue2, Double.isInfinite(kendallValue2) || Double.isNaN(kendallValue2));
			} else {
				assertEquals("i: " + i, kendallValue1, kendallValue2, 1e-10);
			}
		}
	}
	
	private int seed;

	private int pseudoRandom()
	{
		return seed = 3170425 * seed + 132102;
	}
	
	private <T extends RealType<T>, U extends RealType<U>> void assertTau(final double expected, final Iterable<T> img1, final Iterable<U> img2) {
		final double kendallValue = (Double) ops.run(KendallTauBRank.class, img1, img2);
		assertEquals(expected, kendallValue, 1e-10);
	}

	private void assertTau(final double expected, final int[] values1, final int[] values2) {
		assertTau(expected, ArrayImgs.ints(values1, values1.length), ArrayImgs.ints(values2, values2.length));
	}

	private <T extends RealType<T>, U extends RealType<U>> double calculateNaive(final Iterator<Pair<T, U>> iterator) {
		if (!iterator.hasNext()) {
			return Double.NaN;
		}

		// See http://en.wikipedia.org/wiki/Kendall_tau_rank_correlation_coefficient
		int n = 0, max1 = 0, max2 = 0, max = 255;
		int[][] histogram = new int[max + 1][max + 1];
		while (iterator.hasNext()) {
			Pair<T, U> v = iterator.next();
			double ch1 = v.getA().getRealDouble();
			double ch2 = v.getB().getRealDouble();
			if (ch1 < 0 || ch2 < 0 || ch1 > max || ch2 > max) {
				return Double.NaN;
			}
			n++;
			int ch1Int = (int)Math.round(ch1);
			int ch2Int = (int)Math.round(ch2);
			histogram[ch1Int][ch2Int]++;
			if (max1 < ch1Int) {
				max1 = ch1Int;
			}
			if (max2 < ch2Int) {
				max2 = ch2Int;
			}
		}
		long n0 = n * (long)(n - 1) / 2, n1 = 0, n2 = 0, nc = 0, nd = 0;
		for (int i1 = 0; i1 <= max1; i1++) {
			int ch1 = 0;
			for (int i2 = 0; i2 <= max2; i2++) {
				ch1 += histogram[i1][i2];

				int count = histogram[i1][i2];
				for (int j1 = 0; j1 < i1; j1++) {
					for (int j2 = 0; j2 < i2; j2++) {
						nc += count * histogram[j1][j2];
					}
				}
				for (int j1 = 0; j1 < i1; j1++) {
					for (int j2 = i2 + 1; j2 <= max2; j2++) {
						nd += count * histogram[j1][j2];
					}
				}
			}
			n1 += ch1 * (long)(ch1 - 1) / 2;
		}
		for (int i2 = 0; i2 <= max2; i2++) {
			int ch2 = 0;
			for (int i1 = 0; i1 <= max1; i1++) {
				ch2 += histogram[i1][i2];
			}
			n2 += ch2 * (long)(ch2 - 1) / 2;
		}

		return (nc - nd) / Math.sqrt((n0 - n1) * (double)(n0 - n2));
	}
}
