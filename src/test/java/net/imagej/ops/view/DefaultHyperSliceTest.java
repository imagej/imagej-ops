package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

public class DefaultHyperSliceTest extends AbstractOpTest {

	@Test
	public void defaultHyperSliceTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10, 10 }, new DoubleType());

		MixedTransformView<DoubleType> il2 = Views.hyperSlice((RandomAccessible<DoubleType>) img, 1, 8);
		MixedTransformView<DoubleType> opr = ops.view().hyperSlice(img, 1, 8);

		for (int i = 0; i < il2.getTransformToSource().getMatrix().length; i++) {
			for (int j = 0; j < il2.getTransformToSource().getMatrix()[i].length; j++) {
				assertEquals(il2.getTransformToSource().getMatrix()[i][j], opr.getTransformToSource().getMatrix()[i][j],
						1e-10);
			}
		}
	}
}
