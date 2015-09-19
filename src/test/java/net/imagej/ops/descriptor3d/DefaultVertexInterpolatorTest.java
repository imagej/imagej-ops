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
public class DefaultVertexInterpolatorTest extends AbstractOpTest {

	@Test
	public void vertexInterpolator_025_v2_Test() {
		int[] p1 = new int[]{0,0,0};
		int[] p2 = new int[]{10, 0, 10};
		double v1 = 0;
		double v2 = 1;
		double[] res = (double[]) ops.run(DefaultVertexInterpolator.class, p1, p2, v1, v2, 0.25);
		assertEquals(2.5, res[0], 1e-10);
		assertEquals(0, res[1], 1e-10);
		assertEquals(2.5, res[2], 1e-10);
	}
	
	@Test
	public void vertexInterpolator_1_v2_Test() {
		int[] p1 = new int[]{0,0,0};
		int[] p2 = new int[]{10, 0, 10};
		double v1 = 0;
		double v2 = 1;
		double[] res = (double[]) ops.run(DefaultVertexInterpolator.class, p1, p2, v1, v2, 1);
		assertEquals(10, res[0], 1e-10);
		assertEquals(0, res[1], 1e-10);
		assertEquals(10, res[2], 1e-10);
	}
	
	@Test
	public void vertexInterpolator_1_v1_Test() {
		int[] p1 = new int[]{0,0,0};
		int[] p2 = new int[]{10, 0, 10};
		double v1 = 1;
		double v2 = 0;
		double[] res = (double[]) ops.run(DefaultVertexInterpolator.class, p1, p2, v1, v2, 1);
			assertEquals(0, res[0], 1e-10);
			assertEquals(0, res[1], 1e-10);
			assertEquals(0, res[2], 1e-10);
	}
	
	@Test
	public void vertexInterpolator_1_equalValues_Test() {
		int[] p1 = new int[]{0,0,0};
		int[] p2 = new int[]{10, 0, 10};
		double v1 = 1;
		double v2 = 1;
		double[] res = (double[]) ops.run(DefaultVertexInterpolator.class, p1, p2, v1, v2, 1);
		assertEquals(0, res[0], 1e-10);
		assertEquals(0, res[1], 1e-10);
		assertEquals(0, res[2], 1e-10);
	}
}