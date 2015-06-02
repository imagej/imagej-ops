package net.imagej.ops.features.geometric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.CircularityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.EccentricityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsAngleFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsDiameterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.PerimeterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RoundnessFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.SolidityFeature;
import net.imagej.ops.features.sets.GeometricFeatureSet;
import net.imglib2.roi.Regions;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.junit.Before;
import org.junit.Test;

/**
 * To get comparable values with ImageJ I created an image of a polygon, read
 * that image into ImageJ and used the Wand (tracing) tool to select the polygon
 * and used the corners of this polygon here.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 *
 */
public class GeometricFeaturesTest extends AbstractFeatureTest {

	private Map<String, Double> results = new HashMap<String, Double>();

	@Before
	public void setup() {

		List<Pair<String, DoubleType>> compute = null;
		try {

			compute = ops.op(GeometricFeatureSet.class, LabelRegion.class)
					.getFeatureList(Regions.iterable(createLabelRegion()));

		} catch (MalformedURLException e) {
			fail("Couldn't create LabelRegion " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't create LabelRegion " + e.getMessage());
		}

		for (final Pair<String, DoubleType> featureResult : compute) {
			results.put(featureResult.getA(), featureResult.getB()
					.getRealDouble());
		}
	}

	/**
	 * Test the {@link AreaFeature} Op.
	 */
	@Test
	public void testArea() {
		// value taken from imagej
		assertEquals(AreaFeature.NAME, 355630.5, results.get(AreaFeature.NAME),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link PerimeterFeature} Op.
	 */
	@Test
	public void testPerimeter() {
		// value taken from imagej
		assertEquals(PerimeterFeature.NAME, 2658.990257670,
				results.get(PerimeterFeature.NAME),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link CircularityFeature} Op.
	 */
	@Test
	public void testCircularity() {
		// value taken from imagej
		assertEquals(CircularityFeature.NAME, 0.632083948,
				results.get(CircularityFeature.NAME),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link MinorAxisFeature} Op.
	 */
	@Test
	public void testMinorAxis() {
		// value taken from imagej
		assertEquals(MinorAxisFeature.NAME, 520.667420750,
				results.get(MinorAxisFeature.NAME),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link MajorAxisFeature} Op.
	 */
	@Test
	public void testMajorAxis() {
		// value taken from imagej
		assertEquals(MajorAxisFeature.NAME, 869.657215429,
				results.get(MajorAxisFeature.NAME), 0.01);
	}

	/**
	 * Test the {@link FeretsDiameterFeature} Op.
	 */
	@Test
	public void testFeretDiameter() {
		// value taken from imagej
		assertEquals(FeretsDiameterFeature.NAME, 908.002202641,
				results.get(FeretsDiameterFeature.NAME),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link FeretsAngleFeature} Op.
	 */
	@Test
	public void testFeretAngle() {

		// value taken from imagej, angle could be reversed so check
		// 148.235410152 and
		// 148.235410152.. + 180
		final double expectedAngle = 148.235410152;
		final double actualAngle = results.get(FeretsAngleFeature.NAME);

		boolean isEquals = false;
		if (Math.abs(expectedAngle - actualAngle) < AbstractFeatureTest.SMALL_DELTA
				|| Math.abs(expectedAngle + 180 - actualAngle) < AbstractFeatureTest.SMALL_DELTA) {
			isEquals = true;
		}

		assertTrue(FeretsAngleFeature.NAME + " Expected [" + expectedAngle
				+ "] was [" + actualAngle + "]", isEquals);
	}

	/**
	 * Test the {@link EccentricityFeature} Op.
	 */
	@Test
	public void testEccentricity() {
		// value taken from imagej
		assertEquals(EccentricityFeature.NAME, 1.670273923,
				results.get(EccentricityFeature.NAME),
				AbstractFeatureTest.BIG_DELTA);

	}

	/**
	 * Test the {@link EccentricityFeature} Op.
	 */
	@Test
	public void testRoundness() {
		// value taken from imagej
		assertEquals(RoundnessFeature.NAME, 0.598704192,
				results.get(RoundnessFeature.NAME),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link SolidityFeature} Op.
	 */
	@Test
	public void testSolidity() {
		// value taken from imagej
		assertEquals(SolidityFeature.NAME, 0.997063173,
				results.get(SolidityFeature.NAME),
				AbstractFeatureTest.BIG_DELTA);
	}
}
