
package net.imagej.ops.geometric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.FileSaver;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.geometric.GeometricOps.ConvexHullOp;
import net.imagej.ops.geometric.GeometricOps.MooreContourOp;
import net.imagej.ops.geometric.twod.Contour;
import net.imagej.ops.geometric.twod.ConvexPolygon;
import net.imagej.ops.geometric.twod.Polygon;
import net.imagej.ops.geometric.twod.RasterizedContour;
import net.imagej.ops.geometric.twod.RasterizedPolygon;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.list.ListImg;
import net.imglib2.img.list.ListRandomAccess;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;

public class GeometricOpsTest extends AbstractOpTest {

	private static final String IMAGE_PATH = "/home/seebacher/Desktop/";
	private static RandomAccessibleInterval<BoolType> img;

	@BeforeClass
	public static void createImage() throws IOException {
		final BufferedImage read = ImageIO.read(GeometricOpsTest.class
			.getResourceAsStream("k.png"));

		final ListImg<BoolType> listImg = new ListImg<BoolType>(new long[] { read
			.getWidth(), read.getHeight() }, new BoolType(false));

		// at each black pixel of the polygon add a "1" label.
		final RandomAccess<BoolType> ra = listImg.randomAccess();
		for (int y = 0; y < read.getWidth(); y++) {
			for (int x = 0; x < read.getWidth(); x++) {
				if (new Color(read.getRGB(x, y)).equals(Color.BLACK)) {
					ra.setPosition(new int[] { x, y });
					ra.get().set(true);
				}
			}
		}

		img = listImg;
	}

	@Test
	public void testMooreContour() {
		final Contour run = (Contour) this.ops.run(MooreContourOp.class, img, true,
			true);

		assertNotNull(run);
		assertEquals("Num Vertices", 1766, run.size());
		// draw to see result polygon
//		drawPolygon(run, "test1_p", IMAGE_PATH);
//		drawContour(run, "test1_c", IMAGE_PATH);
	}

	@Test
	public void testConvexHull() {
		final ConvexPolygon run = (ConvexPolygon) this.ops.run(ConvexHullOp.class,
			new Polygon(((Contour) this.ops.run(MooreContourOp.class, img, true,
				true)).vertices()));

		assertNotNull(run);
		assertEquals("Num Vertices", 25, run.vertices().size());

		// draw to see result polygon
//		drawPolygon(run, "test2_p", IMAGE_PATH);
//		drawContour(run, "test2_c", IMAGE_PATH);
	}

	private void drawPolygon(final Polytop p, final String name,
		final String path)
	{
		final ListImg<UnsignedByteType> temp = new ListImg<UnsignedByteType>(
			Intervals.dimensionsAsLongArray(img), new UnsignedByteType(0));

		final ListRandomAccess<UnsignedByteType> randomAccess = temp.randomAccess();

		final RasterizedPolygon rasterizedPolygon = new RasterizedPolygon(
			new Polygon(p.vertices()));
		final Cursor<BoolType> lc = rasterizedPolygon.localizingCursor();
		while (lc.hasNext()) {
			final BoolType next = lc.next();
			if (next.get()) {
				randomAccess.setPosition(lc);
				randomAccess.get().set(255);
			}
		}

		final ImagePlus imp = ImageJFunctions.wrap(temp, name);
		imp.setFileInfo(new FileInfo());
		final FileSaver fs = new FileSaver(imp);
		fs.saveAsPng(path + name);
	}

	private void drawContour(final Polytop p, final String name,
		final String path)
	{
		final ListImg<UnsignedByteType> temp = new ListImg<UnsignedByteType>(
			Intervals.dimensionsAsLongArray(img), new UnsignedByteType(0));

		final ListRandomAccess<UnsignedByteType> randomAccess = temp.randomAccess();

		final RasterizedContour rasterizedPolygon = new RasterizedContour(
			new Contour(p.vertices()));
		final Cursor<BoolType> lc = rasterizedPolygon.localizingCursor();
		while (lc.hasNext()) {
			final BoolType next = lc.next();
			if (next.get()) {
				randomAccess.setPosition(lc);
				randomAccess.get().set(255);
			}
		}

		final ImagePlus imp = ImageJFunctions.wrap(temp, name);
		imp.setFileInfo(new FileInfo());
		final FileSaver fs = new FileSaver(imp);
		fs.saveAsPng(path + name);
	}
}
