package net.imagej.ops.features;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import net.imagej.ops.OpRef;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MaxFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MinFeature;
import net.imagej.ops.features.haralick.HaralickFeatures;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix2D;
import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imagej.ops.features.sets.GeometricFeatureSet;
import net.imagej.ops.features.sets.Haralick2DFeatureSet;
import net.imagej.ops.features.sets.HistogramFeatureSet;
import net.imagej.ops.features.sets.ImageMomentsFeatureSet;
import net.imagej.ops.features.sets.ZernikeFeatureSet;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Pair;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class FeatureSetTest extends AbstractFeatureTest {

	@Test
	public void firstOrderTest() {

		@SuppressWarnings("unchecked")
		FirstOrderStatFeatureSet<Img<UnsignedByteType>> op = ops.op(
				FirstOrderStatFeatureSet.class, random);

		eval(op.getFeatureList(random));
		eval(op.getFeatureList(constant));
		eval(op.getFeatureList(empty));
	}

	@Test
	public void haralickTest() {
		@SuppressWarnings("unchecked")
		Haralick2DFeatureSet<UnsignedByteType> op = ops.op(
				Haralick2DFeatureSet.class, Img.class, 8d, 1d, "DIAGONAL");

		eval(op.getFeatureList(random));
		eval(op.getFeatureList(constant));
		eval(op.getFeatureList(empty));
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
	public void geometricTest() throws MalformedURLException, IOException {

		GeometricFeatureSet op = ops.op(GeometricFeatureSet.class,
				LabelRegion.class);

		eval(op.getFeatureList(createLabelRegion()));
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
			assertNotNull("Result Name", result.getA());
			assertNotNull("Result Value", result.getB());
		}
	}
}
