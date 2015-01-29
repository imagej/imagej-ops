package net.imagej.ops.features;

import java.util.List;

import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.junit.Test;

public class FeatureSetTest extends AbstractFeatureTest {

	@Test
	public void testFirstOrderStatistics() {

		@SuppressWarnings("unchecked")
		FirstOrderStatFeatureSet<Img<UnsignedByteType>> op = ops.op(
				FirstOrderStatFeatureSet.class, random);

		eval(op.getFeatures(random));
		eval(op.getFeatures(constant));
		eval(op.getFeatures(empty));
	}

	private void eval(final List<Pair<String, DoubleType>> features) {
		for (final Pair<String, DoubleType> result : features) {
			System.out.println(result.getA() + " " + result.getB());

		}

	}
}
