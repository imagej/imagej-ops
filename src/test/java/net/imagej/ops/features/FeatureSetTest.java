package net.imagej.ops.features;

import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imagej.ops.features.sets.HaralickFeatureSet;
import net.imagej.ops.features.sets.HistogramFeatureSet;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Test;

public class FeatureSetTest extends AbstractFeatureTest {

	@Test
	public void testHaralickFeatureSet() {

		@SuppressWarnings("unchecked")
		HaralickFeatureSet<Img<UnsignedByteType>> op = ops.op(
				HaralickFeatureSet.class, random, 8, 1, "HORIZONTAL");

		for (final FeatureResult result : op.compute(random)) {
			System.out.println(result.getName() + " " + result.getValue());
		}

		for (final FeatureResult result : op.compute(constant)) {
			System.out.println(result.getName() + " " + result.getValue());
		}

		for (final FeatureResult result : op.compute(empty)) {
			System.out.println(result.getName() + " " + result.getValue());
		}
	}

	@Test
	public void testFirstOrderStatistics() {

		@SuppressWarnings("unchecked")
		FirstOrderStatFeatureSet<Img<UnsignedByteType>> op = ops.op(
				FirstOrderStatFeatureSet.class, random, new double[] { 50, 60,
						70 });

		for (final FeatureResult result : op.compute(random)) {
			System.out.println(result.getName() + " " + result.getValue());
		}

		for (final FeatureResult result : op.compute(constant)) {
			System.out.println(result.getName() + " " + result.getValue());
		}

		for (final FeatureResult result : op.compute(empty)) {
			System.out.println(result.getName() + " " + result.getValue());
		}
	}

	@Test
	public void testHistogramFeatureSet() {

		@SuppressWarnings("unchecked")
		HistogramFeatureSet<UnsignedByteType> op = ops.op(
				HistogramFeatureSet.class, random, 8.0);

		for (final FeatureResult result : op.compute(random)) {
			System.out.println(result.getName() + " " + result.getValue());
		}

		for (final FeatureResult result : op.compute(constant)) {
			System.out.println(result.getName() + " " + result.getValue());
		}

		for (final FeatureResult result : op.compute(empty)) {
			System.out.println(result.getName() + " " + result.getValue());
		}
	}
}
