package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.TransformView;
import net.imglib2.view.Views;

public class ShearTest extends AbstractOpTest {

	@Test
	public void defaultShearTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 2, 2 }, new DoubleType());
		Cursor<DoubleType> imgC = img.cursor();
		while (imgC.hasNext()) {
			imgC.next().set(1);
		}

		TransformView<DoubleType> il2 = Views.shear(Views.extendZero(img), 0, 1);
		TransformView<DoubleType> opr = ops.view().shear(Views.extendZero(img), 0, 1);
		Cursor<DoubleType> il2C = Views.interval(il2, new FinalInterval(new long[] { 0, 0 }, new long[] { 3, 3 }))
				.cursor();
		RandomAccess<DoubleType> oprRA = Views
				.interval(opr, new FinalInterval(new long[] { 0, 0 }, new long[] { 3, 3 })).randomAccess();

		while (il2C.hasNext()) {
			il2C.next();
			oprRA.setPosition(il2C);
			assertEquals(il2C.get().get(), oprRA.get().get(), 1e-10);
		}
	}

	@Test
	public void ShearIntervalTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 2, 2 }, new DoubleType());
		Cursor<DoubleType> imgC = img.cursor();
		while (imgC.hasNext()) {
			imgC.next().set(1);
		}

		Cursor<DoubleType> il2 = Views
				.shear(Views.extendZero(img), new FinalInterval(new long[] { 0, 0 }, new long[] { 3, 3 }), 0, 1)
				.cursor();
		RandomAccess<DoubleType> opr = ops.view()
				.shear(Views.extendZero(img), new FinalInterval(new long[] { 0, 0 }, new long[] { 3, 3 }), 0, 1)
				.randomAccess();

		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
	}
}
