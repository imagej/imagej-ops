/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

import ij.ImagePlus;
import ij.io.Opener;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

import javax.imageio.ImageIO;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayCursor;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.EllipseRegionOfInterest;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Before;
import org.scijava.Context;

/**
 * @author Daniel Seebacher (University of Konstanz)
 * @author Andreas Graumann (University of Konstanz)
 */
public class AbstractFeatureTest extends AbstractOpTest {

	/**
	 * Really small number, used for assertEquals with floating or double
	 * values.
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
		ImageGenerator dataGenerator = new ImageGenerator(SEED);
		long[] dim = new long[] { 100, 100 };
		long[] dim3 = new long[] { 100, 100, 30 };

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
	 * 
	 * Simple class to generate empty, randomly filled or constantly filled
	 * images of various types.
	 * 
	 * @author Daniel Seebacher, University of Konstanz.
	 * @author Andreas Graumann, University of Konstanz
	 */
	class ImageGenerator {

		private Random rand;

		/**
		 * Create the image generator with a predefined seed.
		 * 
		 * @param seed
		 *            a seed which is used by the random generator.
		 */
		public ImageGenerator(long seed) {
			this.rand = new Random(seed);
		}

		/**
		 * Default constructor, initialize with random seed.
		 */
		public ImageGenerator() {
			this.rand = new Random();
		}

		/**
		 * 
		 * @param dim
		 *            a long array with the desired dimensions of the image
		 * @return an empty {@link Img} of {@link UnsignedByteType}.
		 */
		public Img<UnsignedByteType> getEmptyUnsignedByteImg(long[] dim) {
			return ArrayImgs.unsignedBytes(dim);
		}

		/**
		 * 
		 * @param dim
		 *            a long array with the desired dimensions of the image
		 * @return an {@link Img} of {@link UnsignedByteType} filled with random
		 *         values.
		 */
		public Img<UnsignedByteType> getRandomUnsignedByteImg(long[] dim) {
			ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs.unsignedBytes(dim);

			UnsignedByteType type = img.firstElement();

			ArrayCursor<UnsignedByteType> cursor = img.cursor();
			while (cursor.hasNext()) {
				cursor.next().set(rand.nextInt((int) type.getMaxValue()));
			}

			return img;
		}

		/**
		 * 
		 * @param dim
		 *            a long array with the desired dimensions of the image
		 * @return an {@link Img} of {@link UnsignedByteType} filled with a
		 *         constant value.
		 */
		public Img<UnsignedByteType> getConstantUnsignedByteImg(long[] dim, int constant) {
			ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs.unsignedBytes(dim);

			UnsignedByteType type = img.firstElement();
			if (constant < type.getMinValue() || constant >= type.getMaxValue()) {
				throw new IllegalArgumentException("Can't create image for constant [" + constant + "]");
			}

			ArrayCursor<UnsignedByteType> cursor = img.cursor();
			while (cursor.hasNext()) {
				cursor.next().set(constant);
			}

			return img;
		}

		/**
		 * 
		 * @param dim
		 * @param radii
		 * @return an {@link Img} of {@link BitType} filled with a ellipse
		 */
		public Img<UnsignedByteType> getEllipsedBitImage(long[] dim, double[] radii, double[] offset) {

			// create empty bittype image with desired dimensions
			ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs.unsignedBytes(dim);

			// create ellipse
			EllipseRegionOfInterest ellipse = new EllipseRegionOfInterest();
			ellipse.setRadii(radii);

			// set origin in the center of image
			double[] origin = new double[dim.length];
			for (int i = 0; i < dim.length; i++)
				origin[i] = dim[i] / 2;
			ellipse.setOrigin(origin);

			// get iterable intervall and cursor of ellipse
			IterableInterval<UnsignedByteType> ii = ellipse.getIterableIntervalOverROI(img);
			Cursor<UnsignedByteType> cursor = ii.cursor();

			// fill image with ellipse
			while (cursor.hasNext()) {
				cursor.next();
				cursor.get().set(255);
			}

			return img;
		}
	}

	protected LabelRegion<?> createLabelRegion2D() throws MalformedURLException, IOException {
		// read simple polygon image
		BufferedImage read = ImageIO.read(AbstractFeatureTest.class.getResourceAsStream("cZgkFsK.png"));

		ImgLabeling<String, IntType> img = new ImgLabeling<String, IntType>(
				ArrayImgs.ints(read.getWidth(), read.getHeight()));

		// at each black pixel of the polygon add a "1" label.
		RandomAccess<LabelingType<String>> randomAccess = img.randomAccess();
		for (int y = 0; y < read.getHeight(); y++) {
			for (int x = 0; x < read.getWidth(); x++) {
				randomAccess.setPosition(new int[] { x, y });
				Color c = new Color(read.getRGB(x, y));
				if (c.getRed() == Color.black.getRed()) {
					randomAccess.get().add("1");
				}
			}
		}

		LabelRegions<String> labelRegions = new LabelRegions<String>(img);
		return labelRegions.getLabelRegion("1");

	}

	protected LabelRegion<String> createLabelRegion3D() throws MalformedURLException,
	IOException
{

	final Opener o = new Opener();
	final ImagePlus imp = o.openImage(AbstractFeatureTest.class.getResource(
		"3d_geometric_features_testlabel.tif").getPath());

	final ImgLabeling<String, IntType> labeling =
		new ImgLabeling<String, IntType>(ArrayImgs.ints(104, 102, 81));

	final RandomAccess<LabelingType<String>> ra = labeling.randomAccess();
	final Img<FloatType> img = ImageJFunctions.convertFloat(imp);
	final Cursor<FloatType> c = img.cursor();
	while (c.hasNext()) {
		final FloatType item = c.next();
		final int[] pos = new int[3];
		c.localize(pos);
		ra.setPosition(pos);
		if (item.get() > 0) {
			ra.get().add("1");
		}
	}
	final LabelRegions<String> labelRegions = new LabelRegions<String>(
		labeling);

	return labelRegions.getLabelRegion("1");

}
}