package pixml;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.pixml.DefaultHardClusterer;
import net.imagej.ops.threshold.localBernsen.LocalBernsenThresholdLearner;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DefaultHardClusterer}.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
public class DefaultHardClustererTest extends AbstractOpTest {

	Img<ByteType> in;
	Img<BitType> out;

	/**
	 * Initialize images.
	 *
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
		in = generateByteArrayTestImg(true, new long[] { 10, 10 });
		out = in.factory().imgFactory(new BitType()).create(in, new BitType());
	}

	@Test
	public void testDefaultHardClusterer() {
		@SuppressWarnings("unchecked")
		LocalBernsenThresholdLearner<ByteType, BitType> learner = ops.op(LocalBernsenThresholdLearner.class, in, 0.5, 0.5);
		ops.run(DefaultHardClusterer.class, out, in, learner);
	}

}
