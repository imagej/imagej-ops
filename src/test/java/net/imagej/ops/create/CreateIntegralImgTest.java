package net.imagej.ops.create;

import org.junit.Before;
import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.create.integralImg.CreateIntegralImgFromImg;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
public class CreateIntegralImgTest extends AbstractOpTest  {

	Img<ByteType> in;
	RandomAccessibleInterval<DoubleType> out;

	/**
	 * Initialize images.
	 *
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
//		in = generateByteArrayTestImg(true, new long[] { 10, 10 });
		in = generateKnownByteArrayTestImgLarge();

//		out = in.factory().imgFactory(new DoubleType()).create(in, new DoubleType());
	}
	
	/**
	 * @see CreateIntegralImgFromImg
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testIntegralImageCreation() {
		out = (RandomAccessibleInterval<DoubleType>) ops.run(CreateIntegralImgFromImg.class, in);
	}
	
	public ArrayImg<ByteType, ByteArray> generateKnownByteArrayTestImgLarge() {
		final long[] dims = new long[] { 3, 3 };
		final byte[] array = new byte[9];

		array[0] = (byte) 40;
		array[1] = (byte) 40;
		array[2] = (byte) 20;

		array[3] = (byte) 40;
		array[4] = (byte) 40;
		array[5] = (byte) 20;

		array[6] = (byte) 20;
		array[7] = (byte) 20;
		array[8] = (byte) 100;

		return ArrayImgs.bytes(array, dims);
	}

}
