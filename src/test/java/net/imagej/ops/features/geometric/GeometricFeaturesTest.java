package net.imagej.ops.features.geometric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import net.imagej.ops.geometric.polygon.Polygon;
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

		Polygon p = createPolygon();

		List<Pair<String, DoubleType>> compute = ops.op(
				GeometricFeatureSet.class, p).getFeatures(p);
		for (Pair<String, DoubleType> featureResult : compute) {
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
		assertEquals(AreaFeature.NAME, 24332, results.get("area"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link PerimeterFeature} Op.
	 */
	@Test
	public void testPerimeter() {
		// value taken from imagej
		assertEquals(PerimeterFeature.NAME, 866.690475583,
				results.get("perimeter"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link CircularityFeature} Op.
	 */
	@Test
	public void testCircularity() {
		// value taken from imagej
		assertEquals(CircularityFeature.NAME, 0.407061121,
				results.get("circularity"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link MinorAxisFeature} Op.
	 */
	@Test
	public void testMinorAxis() {
		// value taken from imagej
		assertEquals(MinorAxisFeature.NAME, 166.288157325,
				results.get("minoraxis"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link MajorAxisFeature} Op.
	 */
	@Test
	public void testMajorAxis() {
		// value taken from imagej
		assertEquals(MajorAxisFeature.NAME, 186.305898754,
				results.get("majoraxis"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link FeretsDiameterFeature} Op.
	 */
	@Test
	public void testFeretDiameter() {
		// value taken from imagej
		assertEquals(FeretsDiameterFeature.NAME, 252.009920440,
				results.get("feretsdiameter"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link FeretsAngleFeature} Op.
	 */
	@Test
	public void testFeretAngle() {

		// value taken from imagej, angle could be reversed so check 11.44.. and
		// 11.44.. + 180
		boolean isEquals = false;
		if (Math.abs(11.443696697 - results.get("feretsangle")) < AbstractFeatureTest.SMALL_DELTA
				|| Math.abs(11.443696697 + 180 - results
						.get("feretsangle")) < AbstractFeatureTest.SMALL_DELTA) {
			isEquals = true;
		}

		assertTrue("FeretsAngleFeature.NAME Expected [11.443696697] was ["
				+ results.get("feretsangle") + "]", isEquals);
	}

	/**
	 * Test the {@link EccentricityFeature} Op.
	 */
	@Test
	public void testEccentricity() {
		assertEquals(EccentricityFeature.NAME, 1.120379838,
				results.get("eccentricity"),
				AbstractFeatureTest.SMALL_DELTA);

	}

	/**
	 * Test the {@link EccentricityFeature} Op.
	 */
	@Test
	public void testRoundness() {
		assertEquals(RoundnessFeature.NAME, 0.892554441,
				results.get("roundness"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link SolidityFeature} Op.
	 */
	@Test
	public void testSolidity() {
		// value taken from imagej
		assertEquals(SolidityFeature.NAME, 0.759437569,
				results.get("solidity"), AbstractFeatureTest.SMALL_DELTA);
	}
}
