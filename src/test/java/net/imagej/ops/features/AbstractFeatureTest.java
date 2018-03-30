/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.features;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.imagej.mesh.Mesh;
import net.imagej.mesh.naive.NaiveDoubleMesh;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayCursor;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.roi.EllipseRegionOfInterest;
import net.imglib2.roi.geom.real.DefaultWritablePolygon2D;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.RandomAccessibleIntervalCursor;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.scijava.Context;
import org.scijava.util.LongArray;

/**
 * @author Daniel Seebacher (University of Konstanz)
 * @author Andreas Graumann (University of Konstanz)
 */
public class AbstractFeatureTest extends AbstractOpTest {

	protected static final boolean expensiveTestsEnabled = 
		"enabled".equals(System.getProperty("imagej.ops.expensive.tests"));

	/**
	 * Really small number, used for assertEquals with floating or double values.
	 */
	protected static final double SMALL_DELTA = 1e-07;

	/**
	 * Medium small number, used for assertEquals with very little error margin.
	 */
	protected static final double MEDIUM_DELTA = 1e-5;

	/**
	 * Small number, used for assertEquals if a little error margin is allowed.
	 */
	protected static final double BIG_DELTA = 1e-3;

	/**
	 * Seed
	 */
	protected static final long SEED = 1234567890L;

	/**
	 * Some random images
	 */
	protected Img<UnsignedByteType> empty;
	protected Img<UnsignedByteType> constant;
	protected Img<UnsignedByteType> random;

	protected Img<UnsignedByteType> empty3d;
	protected Img<UnsignedByteType> constant3d;
	protected Img<UnsignedByteType> random3d;

	protected Img<UnsignedByteType> ellipse;
	protected Img<UnsignedByteType> rotatedEllipse;

	@Before
	public void setup() {
		final ImageGenerator dataGenerator = new ImageGenerator(SEED);
		final long[] dim = new long[] { 100, 100 };
		final long[] dim3 = new long[] { 100, 100, 30 };

		empty = dataGenerator.getEmptyUnsignedByteImg(dim);
		constant = dataGenerator.getConstantUnsignedByteImg(dim, 15);
		random = dataGenerator.getRandomUnsignedByteImg(dim);

		empty3d = dataGenerator.getEmptyUnsignedByteImg(dim3);
		constant3d = dataGenerator.getConstantUnsignedByteImg(dim3, 15);
		random3d = dataGenerator.getRandomUnsignedByteImg(dim3);

		double[] offset = new double[] { 0.0, 0.0 };
		double[] radii = new double[] { 20, 40 };
		ellipse = dataGenerator.getEllipsedBitImage(dim, radii, offset);

		// translate and rotate ellipse
		offset = new double[] { 10.0, -10.0 };
		radii = new double[] { 40, 20 };
		rotatedEllipse = dataGenerator.getEllipsedBitImage(dim, radii, offset);
	}

	@Override
	protected Context createContext() {
		return new Context(OpService.class);
	}

	/**
	 * Simple class to generate empty, randomly filled or constantly filled images
	 * of various types.
	 * 
	 * @author Daniel Seebacher (University of Konstanz)
	 * @author Andreas Graumann (University of Konstanz)
	 */
	class ImageGenerator {

		private final Random rand;

		/**
		 * Create the image generator with a predefined seed.
		 * 
		 * @param seed a seed which is used by the random generator.
		 */
		public ImageGenerator(final long seed) {
			this.rand = new Random(seed);
		}

		/**
		 * @param dim a long array with the desired dimensions of the image
		 * @return an empty {@link Img} of {@link UnsignedByteType}.
		 */
		public Img<UnsignedByteType> getEmptyUnsignedByteImg(final long[] dim) {
			return ArrayImgs.unsignedBytes(dim);
		}

		/**
		 * @param dim a long array with the desired dimensions of the image
		 * @return an {@link Img} of {@link UnsignedByteType} filled with random
		 *         values.
		 */
		public Img<UnsignedByteType> getRandomUnsignedByteImg(final long[] dim) {
			final ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs.unsignedBytes(
				dim);

			final UnsignedByteType type = img.firstElement();

			final ArrayCursor<UnsignedByteType> cursor = img.cursor();
			while (cursor.hasNext()) {
				cursor.next().set(rand.nextInt((int) type.getMaxValue()));
			}

			return img;
		}

		/**
		 * @param dim a long array with the desired dimensions of the image
		 * @param constValue constant image value
		 * @return an {@link Img} of {@link UnsignedByteType} filled with a constant
		 *         value.
		 */
		public Img<UnsignedByteType> getConstantUnsignedByteImg(final long[] dim,
			final int constValue)
		{
			final ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs.unsignedBytes(
				dim);

			final UnsignedByteType type = img.firstElement();
			if (constValue < type.getMinValue() || constValue >= type.getMaxValue()) {
				throw new IllegalArgumentException("Can't create image for constant [" +
					constValue + "]");
			}

			final ArrayCursor<UnsignedByteType> cursor = img.cursor();
			while (cursor.hasNext()) {
				cursor.next().set(constValue);
			}

			return img;
		}

		/**
		 * @param dim dimensions of the image
		 * @param radii of the ellipse
		 * @param offset of the ellipse
		 * @return an {@link Img} of {@link BitType} filled with a ellipse
		 */
		@SuppressWarnings({ "deprecation" })
		public Img<UnsignedByteType> getEllipsedBitImage(final long[] dim,
			final double[] radii, final double[] offset)
		{

			// create empty bittype image with desired dimensions
			final ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs.unsignedBytes(
				dim);

			// create ellipse
			final EllipseRegionOfInterest ellipse = new EllipseRegionOfInterest();
			ellipse.setRadii(radii);

			// set origin in the center of image
			final double[] origin = new double[dim.length];
			for (int i = 0; i < dim.length; i++)
				origin[i] = dim[i] / 2;
			ellipse.setOrigin(origin);

			// get iterable intervall and cursor of ellipse
			final IterableInterval<UnsignedByteType> ii = ellipse
				.getIterableIntervalOverROI(img);
			final Cursor<UnsignedByteType> cursor = ii.cursor();

			// fill image with ellipse
			while (cursor.hasNext()) {
				cursor.next();
				cursor.get().set(255);
			}

			return img;
		}
	}

	protected static Img<FloatType> getTestImage2D() {
		return openFloatImg(AbstractFeatureTest.class, "2d_geometric_features_testlabel.tif");
	}
	
	protected static Polygon2D getPolygon() {
		final List<RealPoint> vertices = new ArrayList<>();
		try {
			Files.lines(Paths.get(AbstractFeatureTest.class.getResource("2d_geometric_features_polygon.txt").toURI()))
										.forEach(l -> {
											String[] coord = l.split(" ");
											RealPoint v = new RealPoint(new double[]{	Double.parseDouble(coord[0]), 
																				Double.parseDouble(coord[1])});
											vertices.add(v);
										});
		} catch (IOException | URISyntaxException exc) {
			exc.printStackTrace();
		}
		return new DefaultWritablePolygon2D(vertices);
	}

	protected static Img<FloatType> getTestImage3D() {
		return openFloatImg(AbstractFeatureTest.class, "3d_geometric_features_testlabel.tif");
	}
	
	protected static Mesh getMesh() {
		final Mesh m = new NaiveDoubleMesh();
		// To prevent duplicates, map each (x, y, z) triple to its own index.
		final Map<Vector3D, Long> indexMap = new HashMap<>();
		final LongArray indices = new LongArray();
		try {
			Files.lines(Paths.get(AbstractFeatureTest.class.getResource("3d_geometric_features_mesh.txt").toURI()))
										.forEach(l -> {
											String[] coord = l.split(" ");
											final double x = Double.parseDouble(coord[0]);
											final double y = Double.parseDouble(coord[1]);
											final double z = Double.parseDouble(coord[2]);
											final Vector3D vertex = new Vector3D(x, y, z);
											final long vIndex = indexMap.computeIfAbsent(vertex, //
												v -> m.vertices().add(x, y, z));
											indices.add(vIndex);
										});
		} catch (IOException | URISyntaxException exc) {
			exc.printStackTrace();
		}
		for (int i = 0; i < indices.size(); i += 3) {
			final long v0 = indices.get(i);
			final long v1 = indices.get(i + 1);
			final long v2 = indices.get(i + 2);
			m.triangles().add(v0, v1, v2);
		}
		return m;
	}

	protected static <T extends RealType<T>> LabelRegion<String> createLabelRegion(
		final RandomAccessibleInterval<T> interval, final float min, final float max, long... dims)
	{
		if (dims == null || dims.length == 0) {
			dims = new long[interval.numDimensions()];
			interval.dimensions(dims);
		}
		final ImgLabeling<String, IntType> labeling = 
			new ImgLabeling<>(ArrayImgs.ints(dims));

		final RandomAccess<LabelingType<String>> ra = labeling.randomAccess();
		final RandomAccessibleIntervalCursor<T> c = new RandomAccessibleIntervalCursor<>(interval);		
		final long[] pos = new long[labeling.numDimensions()];
		while (c.hasNext()) {
			final T item = c.next();
			final float value = item.getRealFloat();
			if (value >= min && value <= max) {
				c.localize(pos);
				ra.setPosition(pos);
				ra.get().add("1");
			}
		}
		final LabelRegions<String> labelRegions = new LabelRegions<>(labeling);

		return labelRegions.getLabelRegion("1");

	}
}
