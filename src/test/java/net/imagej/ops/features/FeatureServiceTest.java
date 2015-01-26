package net.imagej.ops.features;

import static org.junit.Assert.assertEquals;
import net.imagej.ops.OpRef;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MeanFeature;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

public class FeatureServiceTest extends AbstractFeatureTest {

	@Test
	public void testFeatureService() {
		assertEquals(
				127.7534,
				fs.build(new OpRef(MeanFeature.class), DoubleType.class,
						Img.class).compute(random).get(), SMALL_DELTA);
	}
}
