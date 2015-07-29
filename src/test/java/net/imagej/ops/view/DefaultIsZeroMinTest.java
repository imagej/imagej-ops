package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class DefaultIsZeroMinTest extends AbstractOpTest {

	@Test
	public void defaultIsZeroMinTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		boolean il2 = Views.isZeroMin(img);
		boolean opr = ops.view().isZeroMin(img);
		assertEquals(il2, opr);

		il2 = Views.isZeroMin(Views.interval(img, new long[] { 1, 1 }, new long[] { 5, 5 }));
		opr = ops.view().isZeroMin(Views.interval(img, new long[] { 1, 1 }, new long[] { 5, 5 }));
		assertEquals(il2, opr);
	}
}
