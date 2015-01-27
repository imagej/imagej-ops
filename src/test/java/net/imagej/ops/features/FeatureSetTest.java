package net.imagej.ops.features;

import java.util.Map;
import java.util.Map.Entry;

import net.imagej.ops.Op;
import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imagej.ops.features.sets.HaralickFeatureSet;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.junit.Test;

public class FeatureSetTest extends AbstractFeatureTest {

	// TODO here we should have a dummy featureset with which we can test
	// whether the lazy calculation of features works or not.
	@Test
	public void testHaralickFeatureSet() {

		@SuppressWarnings("unchecked")
		HaralickFeatureSet<UnsignedByteType> op = ops.op(
				HaralickFeatureSet.class, random, 8, 1, "HORIZONTAL");

		eval(op.compute(random));
		eval(op.compute(constant));
		eval(op.compute(empty));
	}

	@Test
	public void testFirstOrderStatistics() {

		@SuppressWarnings("unchecked")
		FirstOrderStatFeatureSet<Img<UnsignedByteType>> op = ops.op(
				FirstOrderStatFeatureSet.class, random);

		eval(op.compute(random));
		eval(op.compute(constant));
		eval(op.compute(empty));
	}

	private void eval(Map<Class<? extends Op>, DoubleType> map) {
		for (final Entry<Class<? extends Op>, DoubleType> result : map
				.entrySet()) {
			System.out.println(result.getKey().getSimpleName() + " "
					+ result.getValue().get());
		}
	}
}
