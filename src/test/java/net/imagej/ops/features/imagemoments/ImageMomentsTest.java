package net.imagej.ops.features.imagemoments;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.features.FeatureResult;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment02Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment03Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment12Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment20Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment21Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment30Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment1Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment2Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment3Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment4Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment5Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment6Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment7Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment00Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment01Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment10Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment02Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment03Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment12Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment20Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment21Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment30Feature;
import net.imagej.ops.features.sets.ImageMomentsFeatureSet;

import org.junit.Test;

/**
 * Tests the Image Moment Methods, the results are hardcoded and taken from
 * OpenCV.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 *
 */
public class ImageMomentsTest extends AbstractFeatureTest {

	/**
	 * Test the Moment Ops.
	 */
	@Test
	public void testMoments() {

		Map<String, Double> randomResults = featureResultsToMap(ops.op(
				ImageMomentsFeatureSet.class, random).compute(random));

		assertEquals(Moment00Feature.NAME, 1277534.0,
				randomResults.get(Moment00Feature.NAME), BIG_DELTA);
		assertEquals(Moment10Feature.NAME, 6.3018047E7,
				randomResults.get(Moment10Feature.NAME), BIG_DELTA);
		assertEquals(Moment01Feature.NAME, 6.3535172E7,
				randomResults.get(Moment01Feature.NAME), BIG_DELTA);
		assertEquals(Moment11Feature.NAME, 3.12877962E9,
				randomResults.get(Moment11Feature.NAME), BIG_DELTA);
	}

	/**
	 * Test the Central Moment Ops.
	 */
	@Test
	public void testCentralMoments() {

		Map<String, Double> randomResults = featureResultsToMap(ops.op(
				ImageMomentsFeatureSet.class, random).compute(random));

		assertEquals(CentralMoment11Feature.NAME, -5275876.956702232,
				randomResults.get(CentralMoment11Feature.NAME), BIG_DELTA);
		assertEquals(CentralMoment02Feature.NAME, 1.0694469880269928E9,
				randomResults.get(CentralMoment02Feature.NAME), BIG_DELTA);
		assertEquals(CentralMoment20Feature.NAME, 1.0585772432642083E9,
				randomResults.get(CentralMoment20Feature.NAME), BIG_DELTA);
		assertEquals(CentralMoment12Feature.NAME, 5478324.271270752,
				randomResults.get(CentralMoment12Feature.NAME), BIG_DELTA);
		assertEquals(CentralMoment21Feature.NAME, -2.1636455685491943E8,
				randomResults.get(CentralMoment21Feature.NAME), BIG_DELTA);
		assertEquals(CentralMoment30Feature.NAME, 1.735560232991333E8,
				randomResults.get(CentralMoment30Feature.NAME), BIG_DELTA);
		assertEquals(CentralMoment03Feature.NAME, -4.0994213161157227E8,
				randomResults.get(CentralMoment03Feature.NAME), BIG_DELTA);
	}

	/**
	 * Test the Normalized Central Moment Ops.
	 */
	@Test
	public void testNormalizedCentralMoments() {

		Map<String, Double> randomResults = featureResultsToMap(ops.op(
				ImageMomentsFeatureSet.class, random).compute(random));

		assertEquals(NormalizedCentralMoment11Feature.NAME,
				-3.2325832933879204E-6,
				randomResults.get(NormalizedCentralMoment11Feature.NAME),
				BIG_DELTA);
		assertEquals(NormalizedCentralMoment02Feature.NAME,
				6.552610106398286E-4,
				randomResults.get(NormalizedCentralMoment02Feature.NAME),
				BIG_DELTA);
		assertEquals(NormalizedCentralMoment20Feature.NAME,
				6.486010078361372E-4,
				randomResults.get(NormalizedCentralMoment20Feature.NAME),
				BIG_DELTA);
		assertEquals(NormalizedCentralMoment12Feature.NAME,
				2.969727272701925E-9,
				randomResults.get(NormalizedCentralMoment12Feature.NAME),
				BIG_DELTA);
		assertEquals(NormalizedCentralMoment21Feature.NAME,
				-1.1728837022440002E-7,
				randomResults.get(NormalizedCentralMoment21Feature.NAME),
				BIG_DELTA);
		assertEquals(NormalizedCentralMoment30Feature.NAME,
				9.408242926327751E-8,
				randomResults.get(NormalizedCentralMoment30Feature.NAME),
				BIG_DELTA);
		assertEquals(NormalizedCentralMoment03Feature.NAME,
				-2.22224218245127E-7,
				randomResults.get(NormalizedCentralMoment03Feature.NAME),
				BIG_DELTA);
	}

	/**
	 * Test the Normalized Central Moment Ops.
	 */
	@Test
	public void testHuMoments() {

		Map<String, Double> randomResults = featureResultsToMap(ops.op(
				ImageMomentsFeatureSet.class, random).compute(random));

		assertEquals(HuMoment1Feature.NAME, 0.001303862018475966,
				randomResults.get(HuMoment1Feature.NAME), BIG_DELTA);
		assertEquals(HuMoment2Feature.NAME, 8.615401633994056e-11,
				randomResults.get(HuMoment2Feature.NAME), BIG_DELTA);
		assertEquals(HuMoment3Feature.NAME, 2.406124306990366e-14,
				randomResults.get(HuMoment3Feature.NAME), BIG_DELTA);
		assertEquals(HuMoment4Feature.NAME, 1.246879188175627e-13,
				randomResults.get(HuMoment4Feature.NAME), BIG_DELTA);
		assertEquals(HuMoment5Feature.NAME, -6.610443880647384e-27,
				randomResults.get(HuMoment5Feature.NAME), BIG_DELTA);
		assertEquals(HuMoment6Feature.NAME, 1.131019166855569e-18,
				randomResults.get(HuMoment6Feature.NAME), BIG_DELTA);
		assertEquals(HuMoment7Feature.NAME, 1.716256940536518e-27,
				randomResults.get(HuMoment7Feature.NAME), BIG_DELTA);
	}

	/**
	 * Turns a List of {@link FeatureResult} into a Map.
	 * 
	 * @param results
	 * @return
	 */
	private Map<String, Double> featureResultsToMap(List<FeatureResult> results) {

		Map<String, Double> map = new HashMap<String, Double>();

		for (FeatureResult featureResult : results) {
			map.put(featureResult.getName(), featureResult.getValue());
		}

		return map;
	}
}
