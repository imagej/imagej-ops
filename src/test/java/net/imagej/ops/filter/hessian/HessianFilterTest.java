package net.imagej.ops.filter.hessian;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.junit.Test;

public class HessianFilterTest extends AbstractOpTest {

	@Test
	public void test() {
		Img<FloatType> img = generateFloatArrayTestImg(false, new long[] { 50, 50 });

		Cursor<FloatType> cursorImg = img.cursor();
		int counterX = 0;
		int counterY = 0;
		while (cursorImg.hasNext()) {
			if (counterX > 20 && counterX < 30 || counterY > 20 && counterY < 30) {
				cursorImg.next().setOne();
			} else {
				cursorImg.next().setZero();
			}
			counterX++;
			if (counterX % 50 == 0) {
				counterY++;
			}
			if (counterX == 50) {
				counterX = 0;
			}
			if (counterY == 50) {
				counterY = 0;
			}
		}

		RandomAccessibleInterval<FloatType> out = ops.filter().hessian(img);

		IntervalView<FloatType> xx = Views.hyperSlice(Views.hyperSlice(out, 3, 0), 2, 0);
		IntervalView<FloatType> xy = Views.hyperSlice(Views.hyperSlice(out, 3, 0), 2, 1);
		IntervalView<FloatType> yx = Views.hyperSlice(Views.hyperSlice(out, 3, 0), 2, 2);
		IntervalView<FloatType> yy = Views.hyperSlice(Views.hyperSlice(out, 3, 0), 2, 3);

		Cursor<FloatType> xyCursor = xy.cursor();
		Cursor<FloatType> yxCursor = yx.cursor();
		while (xyCursor.hasNext()) {
			xyCursor.fwd();
			yxCursor.fwd();
			assertEquals(xyCursor.get(), yxCursor.get());
		}
		// two numbers represent a coordinate: 20|0 ; 21|0 ...
		int[] positions = new int[] { 20, 0, 21, 0, 19, 31, 19, 30 };
		float[] valuesXX = new float[] { 16.0f, -16.0f, 15.0f, 11.0f };
		float[] valuesXY = new float[] { 0.0f, 0.0f, 1.0f, 3.0f };
		float[] valuesYY = new float[] { 0.0f, 0.0f, 15.0f, 15.0f };

		RandomAccess<FloatType> xxRA = xx.randomAccess();
		RandomAccess<FloatType> xyRA = xy.randomAccess();
		RandomAccess<FloatType> yyRA = yy.randomAccess();
		
		FloatType type = Util.getTypeFromInterval(out).createVariable();
		int i = 0;
		int j = 0;
		while (i < positions.length - 1) {
			int[] pos = new int[2];
			pos[0] = positions[i];
			pos[1] = positions[i + 1];

			xxRA.setPosition(pos);
			type.set(valuesXX[j]);
			assertEquals(type, xxRA.get());

			xyRA.setPosition(pos);
			type.set(valuesXY[j]);
			assertEquals(type, xyRA.get());

			yyRA.setPosition(pos);
			type.set(valuesYY[j]);
			assertEquals(type, yyRA.get());

			i += 2;
			j++;
		}
	}

}
