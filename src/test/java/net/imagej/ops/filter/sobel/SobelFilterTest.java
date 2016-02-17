package net.imagej.ops.filter.sobel;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

import org.junit.Test;

public class SobelFilterTest extends AbstractOpTest {

	@Test
	public void test() {

		Img<FloatType> img = generateFloatArrayTestImg(false, new long[] { 20, 20 });

		Cursor<FloatType> cursorImg = img.cursor();
		int counterX = 0;
		int counterY = 0;
		while (cursorImg.hasNext()) {
			if (counterX > 8 && counterX < 12 || counterY > 8 && counterY < 12) {
				cursorImg.next().setOne();
			} else {
				cursorImg.next().setZero();
			}
			counterX++;
			if (counterX % 20 == 0) {
				counterY++;
			}
			if (counterX == 20) {
				counterX = 0;
			}
			if (counterY == 20) {
				counterY = 0;
			}
		}
		RandomAccessibleInterval<FloatType> out = ops.filter().sobel(img);
		
		RandomAccess<FloatType> outRA = out.randomAccess();
		outRA.setPosition(new int[] { 0, 8 });
		FloatType type = Util.getTypeFromInterval(out).createVariable();
		type.set(4.0f);
		assertEquals(type, outRA.get());
		type.setZero();
		outRA.setPosition(new int[] { 0, 10 });
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] { 10, 8 });
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] { 10, 10 });
		assertEquals(type, outRA.get());
		
		// should be 0
		outRA.setPosition(new int[] {10, 12});
		type.set(0.0f);
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] {12,10});
		assertEquals(type, outRA.get());
	}

}
