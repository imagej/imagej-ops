/*-
 * #%L
 * Fiji's plugin for colocalization analysis.
 * %%
 * Copyright (C) 2009 - 2017 Fiji developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package net.imagej.ops.coloc.threshold;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.coloc.ColocalisationTest;

import org.junit.Test;

/**
 * Tests {@link AutothresholdRegression}.
 *
 * @author Ellen T Arena
 * @param <T>
 * @param <U>
 */
public class AutothresholdRegressionTest<T, U> extends ColocalisationTest 
{
//	@SuppressWarnings("unchecked")
//	@Test
//	public <V> void clampHelperTest() {
//		assertEquals(4, AutothresholdRegression.clamp(5, 1, 4), 0.00001);
//		assertEquals(1, AutothresholdRegression.clamp(-2, 1, 4), 0.00001);
//		assertEquals(1, AutothresholdRegression.clamp(5, 1, 1), 0.00001);
//		assertEquals(2, AutothresholdRegression.clamp(2, -1, 3), 0.00001);
//	}

	/**
	 * This test makes sure the test images A and B lead to the same thresholds,
	 * regardless whether they are added in the order A, B or B, A.
	 */
	@Test
	public void cummutativityTest() {

//		System.out.println(ops.convert().uint8(syntheticNegativeCorrelationImageCh1).iterator().next().getClass().toString());
//		Mean t = ops.op(Ops.Stats.Mean.class, syntheticNegativeCorrelationImageCh1.iterator().next(), syntheticNegativeCorrelationImageCh1);
//		AutothresholdRegressionResults<T,U> result = (AutothresholdRegressionResults<T, U>) ops.run(AutothresholdRegression.class, ops.convert().uint8(positiveCorrelationImageCh1), ops.convert().uint8(positiveCorrelationImageCh1));

		@SuppressWarnings("unchecked")
		AutothresholdRegressionResults<T,U> result1 = (AutothresholdRegressionResults<T, U>) ops.run(AutothresholdRegression.class, syntheticNegativeCorrelationImageCh1, syntheticNegativeCorrelationImageCh2);
		@SuppressWarnings("unchecked")
		AutothresholdRegressionResults<T, U> result2 = (AutothresholdRegressionResults<T, U>) ops.run(AutothresholdRegression.class, syntheticNegativeCorrelationImageCh2, syntheticNegativeCorrelationImageCh1);

		System.out.println("OPS result1 ch1MinThreshold = " + result1.getCh1MinThreshold());
		System.out.println("OPS result2 ch2MinThreshold = " + result2.getCh2MinThreshold());
		System.out.println("OPS result1 ch1MaxThreshold = " + result1.getCh1MaxThreshold());
		System.out.println("OPS result2 ch2MaxThreshold = " + result2.getCh2MaxThreshold());

		System.out.println("OPS result1 ch1MinThreshold = " + result1.getCh2MinThreshold());
		System.out.println("OPS result2 ch2MinThreshold = " + result2.getCh1MinThreshold());
		System.out.println("OPS result1 ch1MaxThreshold = " + result1.getCh2MaxThreshold());
		System.out.println("OPS result2 ch2MaxThreshold = " + result2.getCh1MaxThreshold());

		assertEquals(result1.getCh1MinThreshold(), result2.getCh2MinThreshold());
		assertEquals(result1.getCh1MaxThreshold(), result2.getCh2MaxThreshold());
		assertEquals(result1.getCh2MinThreshold(), result2.getCh1MinThreshold());
		assertEquals(result1.getCh2MaxThreshold(), result2.getCh1MaxThreshold());
	}
}
