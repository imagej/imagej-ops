package net.imagej.ops.features;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.imagej.ops.OutputFunction;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MeanFeature;
import net.imglib2.img.Img;

import org.junit.Test;

public class FeatureServiceTest extends AbstractFeatureTest {

	@Test
	public void testFeatureService() {
		OutputFunction<Img, List<FeatureResult>> compiled = fs
				.compile(new FeatureInfo(MeanFeature.class), Img.class);

		assertEquals(127.7534, compiled.compute(random).iterator().next()
				.getValue(), SMALL_DELTA);
	}
}
