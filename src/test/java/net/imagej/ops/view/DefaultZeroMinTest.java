package net.imagej.ops.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class DefaultZeroMinTest extends AbstractOpTest {

	@Test
	public void defaultZeroMinTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		IntervalView<DoubleType> imgTranslated = Views.interval(
				Views.translate((RandomAccessible<DoubleType>) img, 2, 5), new long[] { 2, 5 }, new long[] { 12, 15 });

		IntervalView<DoubleType> il2 = Views.zeroMin(imgTranslated);
		IntervalView<DoubleType> opr = ops.view().zeroMin(imgTranslated);

		assertTrue(Views.isZeroMin(il2) == Views.isZeroMin(opr));
	}
}
