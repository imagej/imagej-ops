package net.imagej.ops.features.imagemoments;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.features.FeatureResult;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.sets.ImageMomentsFeatureSet;

import org.junit.Test;

public class ImageMomentsTest extends AbstractFeatureTest {

	/**
	 * Test the {@link MajorAxisFeature} Op.
	 */
	@Test
	public void testMajorAxisFeature() {
		for (FeatureResult fr : ops.op(ImageMomentsFeatureSet.class, random).compute(random)) {
			System.out.println(fr.getName() + "\t" + fr.getValue());
		}
	}
}
