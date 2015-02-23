package net.imagej.ops.features;

import java.util.List;

import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imagej.ops.features.sets.GeometricFeatureSet;
import net.imagej.ops.features.sets.HistogramFeatureSet;
import net.imagej.ops.features.sets.ImageMomentsFeatureSet;
import net.imagej.ops.features.sets.ZernikeFeatureSet;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Pair;

import org.junit.Test;

public class FeatureSetTest extends AbstractFeatureTest {

	@Test
	public void firstOrderTest() {

		@SuppressWarnings("unchecked")
		FirstOrderStatFeatureSet<Img<UnsignedByteType>> op = ops.op(
				FirstOrderStatFeatureSet.class, random);

		eval(op.getFeatureList(random));
		// eval(op.getFeatureList(constant));
		// eval(op.getFeatureList(empty));
	}

	@Test
	public void haralickTest() {

//		@SuppressWarnings("unchecked")
//		HaralickFeatureSet<UnsignedByteType> op = ops.op(
//				HaralickFeatureSet.class, random, 8, 1, "HORIZONTAL");
//
//		eval(op.getFeatureList(random));
//		eval(op.getFeatureList(constant));
//		eval(op.getFeatureList(empty));
	}

	@Test
	public void zernikeTest() {

		@SuppressWarnings("unchecked")
		ZernikeFeatureSet<UnsignedByteType> op = ops.op(
				ZernikeFeatureSet.class, random, true, true, 3, 5);

		eval(op.getFeatureList(random));
		eval(op.getFeatureList(constant));
		eval(op.getFeatureList(empty));
	}

	@Test
	public void geometricTest() {

		GeometricFeatureSet op = ops.op(GeometricFeatureSet.class,
				Polygon.class);

		eval(op.getFeatureList(createPolygon()));
	}

	@Test
	public void momentsTest() {

		@SuppressWarnings("unchecked")
		ImageMomentsFeatureSet<Img<UnsignedByteType>> op = ops.op(
				ImageMomentsFeatureSet.class, random);

		eval(op.getFeatureList(random));
		eval(op.getFeatureList(constant));
		eval(op.getFeatureList(empty));
	}

	@Test
	public void histogramTest() {

		@SuppressWarnings("unchecked")
		HistogramFeatureSet<UnsignedByteType> op = ops.op(
				HistogramFeatureSet.class, random, 256);

		eval(op.getFeatureList(random));
		eval(op.getFeatureList(constant));
		eval(op.getFeatureList(empty));
	}

	private <V extends RealType<V>> void eval(final List<Pair<String, V>> list) {
		for (final Pair<String, V> result : list) {
			System.out.println(result.getA() + " " + result.getB());
		}
	}
}
