package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

public class DefaultPermuteTest extends AbstractOpTest {

	@Test
	public void defaultPermuteTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		
		MixedTransformView<DoubleType> il2 = Views.permute((RandomAccessible<DoubleType>)img, 1, 0);
		MixedTransformView<DoubleType> opr = ops.view().permute(img, 1, 0);
		
		for (int i = 0; i < il2.getTransformToSource().getMatrix().length; i++) {
			for (int j = 0; j < il2.getTransformToSource().getMatrix()[i].length; j++) {
				assertEquals(il2.getTransformToSource().getMatrix()[i][j], opr.getTransformToSource().getMatrix()[i][j],
						1e-10);
			}
		}
	}
	
	@Test
	public void defaultPermuteCoordinatesTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		boolean failed = false;
		try {
			IntervalView<DoubleType> opr = ops.view().permuteCoordinates(img, 1, 0);
		} catch (UnsupportedOperationException e) {
			// not supported in the integrated imglib2 version.
			failed = true;
		}
		assertTrue(failed);
	}
	
	@Test
	public void defaultPermuteCoordinatesInverseTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		boolean failed = false;
		try {
			IntervalView<DoubleType> opr = ops.view().permuteCoordinatesInverse(img, new int[]{1, 0}, 0);
		} catch (UnsupportedOperationException e) {
			// not supported in the integrated imglib2 version.
			failed = true;
		}
		assertTrue(failed);
	}
}
