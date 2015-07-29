package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

public class DefaultOffsetTest extends AbstractOpTest {

	@Test
	public void defaultOffsetTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		MixedTransformView<DoubleType> il2 = Views.offset((RandomAccessible<DoubleType>) img, new long[] { 2, 2 });
		MixedTransformView<DoubleType> opr = ops.view().offset((RandomAccessible<DoubleType>) img, new long[] { 2, 2 });

		for (int i = 0; i < il2.getTransformToSource().getMatrix().length; i++) {
			for (int j = 0; j < il2.getTransformToSource().getMatrix()[i].length; j++) {
				assertEquals(il2.getTransformToSource().getMatrix()[i][j], opr.getTransformToSource().getMatrix()[i][j],
						1e-10);
			}
		}
	}

	@Test
	public void defaultOffsetIntervalTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		IntervalView<DoubleType> il2 = Views.offsetInterval(img,
				new FinalInterval(new long[] { 2, 2 }, new long[] { 9, 9 }));
		IntervalView<DoubleType> opr = ops.view().offset(img,
				new FinalInterval(new long[] { 2, 2 }, new long[] { 9, 9 }));

		assertEquals(il2.realMax(0), opr.realMax(0), 1e-10);
		assertEquals(il2.realMin(0), opr.realMin(0), 1e-10);
		assertEquals(il2.realMax(1), opr.realMax(1), 1e-10);
		assertEquals(il2.realMin(1), opr.realMin(1), 1e-10);
	}

	@Test
	public void defaultOffsetStartEndTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		IntervalView<DoubleType> il2 = Views.offsetInterval(img, new long[] { 2, 2 }, new long[] { 9, 9 });
		IntervalView<DoubleType> opr = ops.view().offset(img, new long[] { 2, 2 }, new long[] { 9, 9 });

		assertEquals(il2.realMax(0), opr.realMax(0), 1e-10);
		assertEquals(il2.realMin(0), opr.realMin(0), 1e-10);
		assertEquals(il2.realMax(1), opr.realMax(1), 1e-10);
		assertEquals(il2.realMin(1), opr.realMin(1), 1e-10);
	}
}
