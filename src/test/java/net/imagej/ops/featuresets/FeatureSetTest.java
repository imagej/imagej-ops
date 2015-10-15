
package net.imagej.ops.featuresets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

import ij.ImagePlus;
import ij.io.Opener;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops.Geometric.MainElongation;
import net.imagej.ops.Ops.Geometric.MedianElongation;
import net.imagej.ops.Ops.Geometric.Spareness;
import net.imagej.ops.Ops.Tamura.Directionality;
import net.imagej.ops.image.cooccurrencematrix.MatrixOrientation2D;
import net.imagej.ops.image.cooccurrencematrix.MatrixOrientation3D;
import net.imagej.ops.image.histogram.HistogramCreate;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FeatureSetTest extends AbstractOpTest {

	private static LabelRegion<String> region2D;
	private static Img<FloatType> img2d;

	private static LabelRegion<String> region3D;
	private static Img<FloatType> img3d;

	@BeforeClass
	public static void setupBefore() throws MalformedURLException, IOException {
		// read simple polygon image
		final BufferedImage read = ImageIO.read(FeatureSetTest.class.getResourceAsStream("cZgkFsK.png"));

		img2d = ImageJFunctions
				.convertFloat(new Opener().openImage(FeatureSetTest.class.getResource("cZgkFsK.png").getPath()));

		final ImgLabeling<String, IntType> labeling2d = new ImgLabeling<String, IntType>(
				ArrayImgs.ints(read.getWidth(), read.getHeight()));

		// at each black pixel of the polygon add a "1" label.
		final RandomAccess<LabelingType<String>> randomAccess = labeling2d.randomAccess();
		for (int y = 0; y < read.getHeight(); y++) {
			for (int x = 0; x < read.getWidth(); x++) {
				randomAccess.setPosition(new int[] { x, y });
				final Color c = new Color(read.getRGB(x, y));
				if (c.getRed() == Color.black.getRed()) {
					randomAccess.get().add("1");
				}
			}
		}

		final LabelRegions<String> labelRegions2d = new LabelRegions<String>(labeling2d);
		region2D = labelRegions2d.getLabelRegion("1");

		final Opener o = new Opener();
		final ImagePlus imp = o
				.openImage(FeatureSetTest.class.getResource("3d_geometric_features_testlabel.tif").getPath());

		final ImgLabeling<String, IntType> labeling = new ImgLabeling<String, IntType>(ArrayImgs.ints(104, 102, 81));

		final RandomAccess<LabelingType<String>> ra = labeling.randomAccess();
		img3d = ImageJFunctions.convertFloat(imp);
		final Cursor<FloatType> c = img3d.cursor();
		while (c.hasNext()) {
			final FloatType item = c.next();
			final int[] pos = new int[3];
			c.localize(pos);
			ra.setPosition(pos);
			if (item.get() > 0) {
				ra.get().add("1");
			}
		}
		final LabelRegions<String> labelRegions3d = new LabelRegions<String>(labeling);

		region3D = labelRegions3d.getLabelRegion("1");
	}

	@Test
	public void geom2dFeatureSet() {
		Geometric2DFeatureSet<DoubleType> op = ops.op(Geometric2DFeatureSet.class, region2D, Class[].class,
				DoubleType.class);

		for (Entry<NamedFeature, DoubleType> entry : op.compute(region2D).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());
			assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
					((DoubleType) ops.run(entry.getKey().getOp().getType(), ops.geom().contour(region2D, true, true)))
							.getRealDouble(),
					0.0001);
		}
	}

	@Test
	public void geom3dFeatureSet() {
		Geometric3DFeatureSet<DoubleType> op = ops.op(Geometric3DFeatureSet.class, region3D, Class[].class,
				DoubleType.class);

		for (Entry<NamedFeature, DoubleType> entry : op.compute(region3D).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());

			// FIXME: this hack is not required any more as soon as issue
			// https://github.com/imagej/imagej-ops/issues/231 is resolved.
			if (MainElongation.class.isAssignableFrom(entry.getKey().getOp().getType())
					|| MedianElongation.class.isAssignableFrom(entry.getKey().getOp().getType())
					|| Spareness.class.isAssignableFrom(entry.getKey().getOp().getType())) {
				assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
						((DoubleType) ops.run(entry.getKey().getOp().getType(), region3D)).getRealDouble(), 0.0001);
			} else {
				assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
						((DoubleType) ops.run(entry.getKey().getOp().getType(), ops.geom().marchingcubes(region3D)))
								.getRealDouble(),
						0.0001);
			}
		}
	}

	@Test
	public void tamura2dFeatureSet() {
		Tamura2DFeatureSet<FloatType, RealType> op = ops.op(Tamura2DFeatureSet.class, img2d, Class[].class,
				RealType.class, 16);

		for (Entry<NamedFeature, RealType> entry : op.compute(img2d).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());

			// FIXME: this hack is not required any more as soon as issue
			// https://github.com/imagej/imagej-ops/issues/231 is resolved.
			if (Directionality.class.isAssignableFrom(entry.getKey().getOp().getType())) {
				assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
						((DoubleType) ops.run(entry.getKey().getOp().getType(), img2d, 16)).getRealDouble(), 0.0001);
			} else {
				assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
						((DoubleType) ops.run(entry.getKey().getOp().getType(), img2d)).getRealDouble(), 0.0001);
			}
		}
	}

	@Test
	public void stats2dFeatureSet() {
		StatsFeatureSet<FloatType, RealType> op = ops.op(StatsFeatureSet.class, img2d, Class[].class, RealType.class);

		for (Entry<NamedFeature, RealType> entry : op.compute(img2d).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());
			assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
					((DoubleType) ops.run(entry.getKey().getOp().getType(), img2d)).getRealDouble(), 0.0001);
		}
	}

	@Test
	public void stats3dFeatureSet() {
		StatsFeatureSet<FloatType, RealType> op = ops.op(StatsFeatureSet.class, img3d, Class[].class, RealType.class);

		for (Entry<NamedFeature, RealType> entry : op.compute(img3d).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());
			assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
					((DoubleType) ops.run(entry.getKey().getOp().getType(), img3d)).getRealDouble(), 0.0001);
		}
	}

	@Test
	public void haralick2dFeatureSet() {
		Haralick2DFeatureSet<FloatType, RealType> op = ops.op(Haralick2DFeatureSet.class, img2d, Class[].class,
				RealType.class, 32, 1, "HORIZONTAL");

		for (Entry<NamedFeature, RealType> entry : op.compute(img2d).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());
			assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
					((DoubleType) ops.run(entry.getKey().getOp().getType(), img2d, 32, 1,
							MatrixOrientation2D.HORIZONTAL)).getRealDouble(),
					0.0001);
		}
	}

	@Test
	public void haralick3dFeatureSet() {
		Haralick3DFeatureSet<FloatType, RealType> op = ops.op(Haralick3DFeatureSet.class, img3d, Class[].class,
				RealType.class, 32, 1, "HORIZONTAL");

		for (Entry<NamedFeature, RealType> entry : op.compute(img3d).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());
			assertEquals(entry.getKey().getOp().toString(), entry.getValue().getRealDouble(),
					((DoubleType) ops.run(entry.getKey().getOp().getType(), img3d, 32, 1,
							MatrixOrientation3D.HORIZONTAL)).getRealDouble(),
					0.0001);
		}
	}

	@Test
	public void zernike2dFeatureSet() {
		ZernikeFeatureSet<FloatType> op = ops.op(ZernikeFeatureSet.class, img2d, Class[].class, 2, 4);

		for (Entry<NamedFeature, DoubleType> entry : op.compute(img2d).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());

			int order = Integer.valueOf(entry.getKey().getName().split("Order")[1].split(" ")[1]);
			int repitition = Integer.valueOf(entry.getKey().getName().split("Repetition")[1].split(" ")[1]);

			if (entry.getKey().getName().startsWith("Magnitude")) {
				assertEquals(entry.getKey().getName(), entry.getValue().getRealDouble(),
						ops.zernike().magnitude(img2d, order, repitition).getRealDouble(), 0.0001);
			} else {
				assertEquals(entry.getKey().getName(), entry.getValue().getRealDouble(),
						ops.zernike().phase(img2d, order, repitition).getRealDouble(), 0.0001);
			}
		}
	}

	@Test
	public void histogram2dFeatureSet() {
		HistogramFeatureSet<FloatType> fs = ops.op(HistogramFeatureSet.class, img2d, Class[].class, 256);

		Histogram1d result = ops.op(HistogramCreate.class, img2d, 256).compute(img2d);

		for (Entry<NamedFeature, LongType> entry : fs.compute(img2d).entrySet()) {
			assertNotNull(entry.getKey().getName(), entry.getValue());
			assertEquals(entry.getKey().getName(),
					result.frequency((long) Long.valueOf(entry.getKey().getName().split(":")[1].trim())),
					entry.getValue().get());
		}
	}

}
