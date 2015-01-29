package net.imagej.ops.features;

import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayCursor;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.roi.EllipseRegionOfInterest;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

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
	protected Img<UnsignedByteType> ellipse;
	protected Img<UnsignedByteType> rotatedEllipse;

	protected OpResolverService fs;

	@Before
	public void setup() {
		ImageGenerator dataGenerator = new ImageGenerator(SEED);
		long[] dim = new long[] { 100, 100 };

		empty = dataGenerator.getEmptyUnsignedByteImg(dim);
		constant = dataGenerator.getConstantUnsignedByteImg(dim, 15);
		random = dataGenerator.getRandomUnsignedByteImg(dim);

		double[] offset = new double[] { 0.0, 0.0 };
		double[] radii = new double[] { 20, 40 };
		ellipse = dataGenerator.getEllipsedBitImage(dim, radii, offset);

		// translate and rotate ellipse
		offset = new double[] { 10.0, -10.0 };
		radii = new double[] { 40, 20 };
		rotatedEllipse = dataGenerator.getEllipsedBitImage(dim, radii, offset);

		fs = context.getService(OpResolverService.class);
	}

	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
				OpResolverService.class);
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
			ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs
					.unsignedBytes(dim);

			UnsignedByteType type = img.firstElement();

			ArrayCursor<UnsignedByteType> cursor = img.cursor();
			while (cursor.hasNext()) {
				cursor.next().set(rand.nextInt((int) type.getMaxValue()));
			}

			return (Img<UnsignedByteType>) img;
		}

		/**
		 * 
		 * @param dim
		 *            a long array with the desired dimensions of the image
		 * @return an {@link Img} of {@link UnsignedByteType} filled with a
		 *         constant value.
		 */
		public Img<UnsignedByteType> getConstantUnsignedByteImg(long[] dim,
				int constant) {
			ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs
					.unsignedBytes(dim);

			UnsignedByteType type = img.firstElement();
			if (constant < type.getMinValue() || constant >= type.getMaxValue()) {
				throw new IllegalArgumentException(
						"Can't create image for constant [" + constant + "]");
			}

			ArrayCursor<UnsignedByteType> cursor = img.cursor();
			while (cursor.hasNext()) {
				cursor.next().set(constant);
			}

			return (Img<UnsignedByteType>) img;
		}

		/**
		 * 
		 * @param dim
		 * @param radii
		 * @return an {@link Img} of {@link BitType} filled with a ellipse
		 */
		public Img<UnsignedByteType> getEllipsedBitImage(long[] dim,
				double[] radii, double[] offset) {

			// create empty bittype image with desired dimensions
			ArrayImg<UnsignedByteType, ByteArray> img = ArrayImgs
					.unsignedBytes(dim);

			// create ellipse
			EllipseRegionOfInterest ellipse = new EllipseRegionOfInterest();
			ellipse.setRadii(radii);

			// set origin in the center of image
			double[] origin = new double[dim.length];
			for (int i = 0; i < dim.length; i++)
				origin[i] = dim[i] / 2;
			ellipse.setOrigin(origin);

			// get iterable intervall and cursor of ellipse
			IterableInterval<UnsignedByteType> ii = ellipse
					.getIterableIntervalOverROI(img);
			Cursor<UnsignedByteType> cursor = ii.cursor();

			// fill image with ellipse
			while (cursor.hasNext()) {
				cursor.next();
				cursor.get().set(255);
			}

			return (Img<UnsignedByteType>) img;
		}
	}

}