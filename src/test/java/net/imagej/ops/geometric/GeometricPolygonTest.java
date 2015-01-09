package net.imagej.ops.geometric;

import static org.junit.Assert.assertEquals;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.geometric.GeometricOps.BoundingBox;
import net.imagej.ops.geometric.GeometricOps.CenterOfGravity;
import net.imagej.ops.geometric.GeometricOps.ConvexHull;
import net.imagej.ops.geometric.GeometricOps.MooreContours;
import net.imagej.ops.geometric.GeometricOps.SmallestEnclosingRectangle;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.BoundingBoxPolygon;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.MooreContoursPolygon;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.RealPoint;
import net.imglib2.algorithm.region.hypersphere.HyperSphereCursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;

import org.junit.Test;

/**
 * Test the {@link PolygonOps}
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public class GeometricPolygonTest extends AbstractOpTest {

	/**
	 * Test the {@link MooreContours} Op.
	 */
	@Test
	public void testMooreContourOp() {

		// create an image
		Img<BitType> img1 = ArrayImgs.bits(100, 100);
		HyperSphereCursor<BitType> hyperSphereCursor = new HyperSphereCursor<BitType>(
				img1, new long[] { 50, 50 }, 20);
		while (hyperSphereCursor.hasNext()) {
			hyperSphereCursor.next().set(true);
		}

		Polygon contours = (Polygon) ops.run(MooreContoursPolygon.class, img1,
				true, true);

		// our polygon must at least contain the four extrema (top, left,
		// bottom, right)
		boolean containsTop = false;
		boolean containsLeft = false;
		boolean containsBottom = false;
		boolean containsRight = false;
		for (RealPoint rp : contours.getPoints()) {
			if (rp.getDoublePosition(0) == 50 && rp.getDoublePosition(1) == 70) {
				containsTop = true;
			}
			if (rp.getDoublePosition(0) == 30 && rp.getDoublePosition(1) == 50) {
				containsLeft = true;
			}
			if (rp.getDoublePosition(0) == 50 && rp.getDoublePosition(1) == 30) {
				containsBottom = true;
			}
			if (rp.getDoublePosition(0) == 70 && rp.getDoublePosition(1) == 50) {
				containsRight = true;
			}
		}

		assertEquals("Extrema Top", true, containsTop);
		assertEquals("Extrema Left", true, containsLeft);
		assertEquals("Extrema Bottom", true, containsBottom);
		assertEquals("Extrema Right", true, containsRight);
	}

	/**
	 * Test the {@link BoundingBox} Op.
	 */
	@Test
	public void testBoundingBox() {
		Polygon p = new Polygon();

		// add four points (diamond like shape)
		p.add(new RealPoint(50, 70));
		p.add(new RealPoint(30, 50));
		p.add(new RealPoint(50, 30));
		p.add(new RealPoint(70, 50));

		Polygon boundingBox = (Polygon) ops.run(BoundingBoxPolygon.class, p);

		// five points because the first and last one are equal
		assertEquals("Polygon Size", 5, boundingBox.size());

		// and now check that it only contains the four corner points
		boolean containsBottomLeft = false;
		boolean containsBottomRight = false;
		boolean containsTopRight = false;
		boolean containsTopLeft = false;

		for (RealPoint rp : boundingBox.getPoints()) {
			if (rp.getDoublePosition(0) == 30 && rp.getDoublePosition(1) == 30) {
				containsBottomLeft = true;
			}
			if (rp.getDoublePosition(0) == 70 && rp.getDoublePosition(1) == 30) {
				containsBottomRight = true;
			}
			if (rp.getDoublePosition(0) == 70 && rp.getDoublePosition(1) == 70) {
				containsTopRight = true;
			}
			if (rp.getDoublePosition(0) == 30 && rp.getDoublePosition(1) == 70) {
				containsTopLeft = true;
			}
		}

		assertEquals("Extrema BottomLeft", true, containsBottomLeft);
		assertEquals("Extrema BottomRight", true, containsBottomRight);
		assertEquals("Extrema TopRight", true, containsTopRight);
		assertEquals("Extrema TopLeft", true, containsTopLeft);
	}

	/**
	 * Test the {@link ConvexHull} Op.
	 */
	@Test
	public void testConvexHull() {
		Polygon p = new Polygon();

		// add 4 extrema
		p.add(new RealPoint(0, 0));
		p.add(new RealPoint(10, 0));
		p.add(new RealPoint(10, 10));
		p.add(new RealPoint(0, 10));

		// add noise
		for (int i = 0; i < 100; i++) {
			p.add(new RealPoint(Math.random() * 10, Math.random() * 10));
		}

		Polygon convexHull = (Polygon) ops.run(ConvexHull.class, p);

		// five points because the first and last one are equal
		assertEquals("Polygon Size", 5, convexHull.size());

		// and now check that it only contains the four corner points
		boolean containsBottomLeft = false;
		boolean containsBottomRight = false;
		boolean containsTopRight = false;
		boolean containsTopLeft = false;
		for (RealPoint rp : convexHull.getPoints()) {
			if (rp.getDoublePosition(0) == 0 && rp.getDoublePosition(1) == 0) {
				containsBottomLeft = true;
			}
			if (rp.getDoublePosition(0) == 10 && rp.getDoublePosition(1) == 0) {
				containsBottomRight = true;
			}
			if (rp.getDoublePosition(0) == 10 && rp.getDoublePosition(1) == 10) {
				containsTopRight = true;
			}
			if (rp.getDoublePosition(0) == 0 && rp.getDoublePosition(1) == 10) {
				containsTopLeft = true;
			}
		}

		assertEquals("Extrema BottomLeft", true, containsBottomLeft);
		assertEquals("Extrema BottomRight", true, containsBottomRight);
		assertEquals("Extrema TopRight", true, containsTopRight);
		assertEquals("Extrema TopLeft", true, containsTopLeft);
	}
	
	
	/**
	 * Test the {@link CenterOfGravity} Op.
	 */
	@Test
	public void testCenterOfGravity() {
		
		
		// create polygon and add some values
		Polygon p = new Polygon();
		p.add(new RealPoint(0, 0));
		p.add(new RealPoint(10, 0));
		p.add(new RealPoint(10, 10));
		p.add(new RealPoint(0, 10));

		RealPoint centerOfGravity = (RealPoint) ops.run(CenterOfGravity.class, p);

		// five points because the first and last one are equal
		assertEquals("Center of Gravity X", 5d, centerOfGravity.getDoublePosition(0), Double.MIN_VALUE);
		assertEquals("Center of Gravity Y", 5d, centerOfGravity.getDoublePosition(0), Double.MIN_VALUE);
		
		
		// create polygon and add some values
		p = new Polygon();
		p.add(new RealPoint(0, 0));
		p.add(new RealPoint(50, 0));
		p.add(new RealPoint(50, 50));
		p.add(new RealPoint(0, 50));

		centerOfGravity = (RealPoint) ops.run(CenterOfGravity.class, p);

		// five points because the first and last one are equal
		assertEquals("Center of Gravity X", 25d, centerOfGravity.getDoublePosition(0), Double.MIN_VALUE);
		assertEquals("Center of Gravity Y", 25d, centerOfGravity.getDoublePosition(0), Double.MIN_VALUE);
	}
	
	/**
	 * Test the {@link SmallestEnclosingRectangle} Op.
	 */
	@Test
	public void testSmallestEnclosingRectangle() {
		
		Polygon p = new Polygon();

		// add 4 extrema
		p.add(new RealPoint(0, 0));
		p.add(new RealPoint(10, 0));
		p.add(new RealPoint(10, 10));
		p.add(new RealPoint(0, 10));

		// add noise
		for (int i = 0; i < 100; i++) {
			p.add(new RealPoint(Math.random() * 10, Math.random() * 10));
		}


		Polygon ser = (Polygon) ops.run(SmallestEnclosingRectangle.class, p);
		
		
		// five points because the first and last one are equal
		assertEquals("Polygon Size", 5, ser.size());

		// and now check that it only contains the four corner points
		boolean containsBottomLeft = false;
		boolean containsBottomRight = false;
		boolean containsTopRight = false;
		boolean containsTopLeft = false;
		for (RealPoint rp : ser.getPoints()) {
			if (rp.getDoublePosition(0) == 0 && rp.getDoublePosition(1) == 0) {
				containsBottomLeft = true;
			}
			if (rp.getDoublePosition(0) == 10 && rp.getDoublePosition(1) == 0) {
				containsBottomRight = true;
			}
			if (rp.getDoublePosition(0) == 10 && rp.getDoublePosition(1) == 10) {
				containsTopRight = true;
			}
			if (rp.getDoublePosition(0) == 0 && rp.getDoublePosition(1) == 10) {
				containsTopLeft = true;
			}
		}

		assertEquals("Extrema BottomLeft", true, containsBottomLeft);
		assertEquals("Extrema BottomRight", true, containsBottomRight);
		assertEquals("Extrema TopRight", true, containsTopRight);
		assertEquals("Extrema TopLeft", true, containsTopLeft);

	}
}
