/*-
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

package net.imagej.ops.coloc;

import java.util.Random;

import net.imglib2.IterableInterval;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

/**
 * Utility class for coloc ops.
 *
 * @author Ellen Arena
 */
public final class ColocUtil
{
		private ColocUtil() {
			// prevent instantiation of utility class
		}
		public static boolean sameIterationOrder(Iterable<?> i1, Iterable<?> i2) {
			if (!(i1 instanceof IterableInterval) || !(i2 instanceof IterableInterval)) {
				return true;
			}
			IterableInterval<?> ii1 = (IterableInterval<?>) i1;
			IterableInterval<?> ii2 = (IterableInterval<?>) i2;
			return Intervals.equalDimensions(ii1, ii2) && Util.equalIterationOrder(ii1, ii2);
		}

	/** Fisher-Yates shuffle. */
	public static void shuffle(int[] array, Random rnd) {
		final int size = array.length;
		for (int i = size - 1; i > 0; i--) {
			final int j = rnd.nextInt(i + 1);
			final int v = array[i];
			array[i] = array[j];
			array[j] = v;
		}
	}
}
