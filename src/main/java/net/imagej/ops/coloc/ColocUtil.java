
package net.imagej.ops.coloc;

import net.imglib2.IterableInterval;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

/**
 * Utility class for coloc ops.
 *
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
}
