package net.imagej.ops.descriptor3d;

import static org.junit.Assert.assertEquals;
import net.imagej.ops.AbstractOpTest;

import org.junit.Test;

/**
 * This class tests the {@link DefaultBitTypeVertexInterpolator}. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 *
 */
public class BitTypeVertexInterpolatorTest extends AbstractOpTest {

	@Test
	public void interpolatorTest_v2() {
			int[] p1 = new int[]{0,0,0};
			int[] p2 = new int[]{10, 0, 10};
			double v1 = 0;
			double v2 = 1;
			double[] res = (double[]) ops.run(DefaultBitTypeVertexInterpolator.class, p1, p2, v1, v2);
				assertEquals(5, res[0], 1e-10);
				assertEquals(0, res[1], 1e-10);
				assertEquals(5, res[2], 1e-10);
	}
	
	@Test
	public void interpolatorTest_v1() {
			int[] p1 = new int[]{0,0,0};
			int[] p2 = new int[]{10, 0, 10};
			double v1 = 1;
			double v2 = 0;
			double[] res = (double[]) ops.run(DefaultBitTypeVertexInterpolator.class, p1, p2, v1, v2);
			assertEquals(5, res[0], 1e-10);
			assertEquals(0, res[1], 1e-10);
			assertEquals(5, res[2], 1e-10);
	}
	
	@Test
	public void interpolatorTest_equal() {
			int[] p1 = new int[]{0,0,0};
			int[] p2 = new int[]{10, 0, 10};
			double v1 = 1;
			double v2 = 1;
			double[] res = (double[]) ops.run(DefaultBitTypeVertexInterpolator.class, p1, p2, v1, v2);
			assertEquals(5, res[0], 1e-10);
			assertEquals(0, res[1], 1e-10);
			assertEquals(5, res[2], 1e-10);
	}
}