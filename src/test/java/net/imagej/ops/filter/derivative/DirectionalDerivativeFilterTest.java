package net.imagej.ops.filter.derivative;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

import org.junit.Test;

public class DirectionalDerivativeFilterTest extends AbstractOpTest {

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
		RandomAccessibleInterval<FloatType> out = ops.filter().directionalDerivative(img, 0);

		FloatType type = Util.getTypeFromInterval(out).createVariable();
		type.set(4.0f);
		RandomAccess<FloatType> outRA = out.randomAccess();
		for(int i = 0; i < 8; i++) {
			outRA.setPosition(new int[] {9,i});
			assertEquals(type, outRA.get());
			
		}
		outRA.setPosition(new int[] {9,8});
		type.set(3.0f);
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] {9,10});
		type.set(0.0f);
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] {9,11});
		type.set(1.0f);
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] {9,12});
		type.set(3.0f);
		assertEquals(type, outRA.get());
		type.set(4.0f);
		for(int i = 13; i < 20; i++) {
			outRA.setPosition(new int[] {9,i});
			assertEquals(type, outRA.get());
			
		}
		
		type.set(-4.0f);
		for(int i = 0; i < 8; i++) {
			outRA.setPosition(new int[] {12,i});
			assertEquals(type, outRA.get());
			
		}
		outRA.setPosition(new int[] {12,8});
		type.set(-3.0f);
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] {12,10});
		type.set(0.0f);
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] {12,11});
		type.set(-1.0f);
		assertEquals(type, outRA.get());
		outRA.setPosition(new int[] {12,12});
		type.set(-3.0f);
		assertEquals(type, outRA.get());
		type.set(-4.0f);
		for(int i = 13; i < 20; i++) {
			outRA.setPosition(new int[] {12,i});
			assertEquals(type, outRA.get());
			
		}
	}

}
