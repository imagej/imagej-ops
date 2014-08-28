package net.imagej.ops;

import java.util.Random;

import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayCursor;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * 
 * Simple class to generate empty, randomly filled or constantly filled images
 * of various types.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public class ImageGenerator {

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
	 * @return an {@link Img} of {@link UnsignedByteType} filled with a constant
	 *         value.
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
}
