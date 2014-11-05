package net.imagej.ops.polygon;

import java.awt.geom.Point2D;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops.MooreContourExtraction;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.hypersphere.HyperSphereCursor;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * 
 * Test for the Moore Contour Extraction Algorithm. Create 2 circles with the
 * {@link HyperSphereCursor} and check if the resulting {@link Polygon} contains
 * the four extrema (top, bottom, left and right).
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public class MooreContourTest extends AbstractOpTest {

	private RandomAccessibleInterval<BitType> in1;
	private RandomAccessibleInterval<BitType> in2;

	@Before
	public void initImg() {
		// create a empty bittype image
		in1 = ArrayImgs.bits(100, 100);
		in2 = ArrayImgs.bits(100, 100);

		// create a circle
		HyperSphereCursor<BitType> hyperSphereCursor = new HyperSphereCursor<BitType>(
				in1, new long[] { 50, 50 }, 20);
		while (hyperSphereCursor.hasNext()) {
			hyperSphereCursor.next().set(true);
		}

		// create a bigger circle
		hyperSphereCursor = new HyperSphereCursor<BitType>(in2, new long[] {
				50, 50 }, 35);
		while (hyperSphereCursor.hasNext()) {
			hyperSphereCursor.next().set(true);
		}
	}

	@Test
	public void testMooreContourExtraction() {

		// our polygon must contain the four extrema [50, 30], [70, 50],
		// [50, 70] and [30, 50]
		Polygon p = (Polygon) ops.run(MooreContourExtraction.class, in1, true,
				true);
		assertTrue("Extremum 1",
				p.getPoints().contains(new Point2D.Double(50, 30)));
		assertTrue("Extremum 2",
				p.getPoints().contains(new Point2D.Double(70, 50)));
		assertTrue("Extremum 3",
				p.getPoints().contains(new Point2D.Double(50, 70)));
		assertTrue("Extremum 4",
				p.getPoints().contains(new Point2D.Double(30, 50)));

		// now the polygon must contain the four extrema [50, 15], [85, 50],
		// [50, 85] and [15, 50]
		p = (Polygon) ops.run(MooreContourExtraction.class, in2, true, true);
		assertTrue("Extremum 1",
				p.getPoints().contains(new Point2D.Double(50, 15)));
		assertTrue("Extremum 2",
				p.getPoints().contains(new Point2D.Double(85, 50)));
		assertTrue("Extremum 3",
				p.getPoints().contains(new Point2D.Double(50, 85)));
		assertTrue("Extremum 4",
				p.getPoints().contains(new Point2D.Double(15, 50)));
	}
}
