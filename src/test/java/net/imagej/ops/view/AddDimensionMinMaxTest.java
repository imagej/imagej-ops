package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class AddDimensionMinMaxTest extends AbstractOpTest {

	@Test
	public void addDimensionMinMaxTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());
		int max = 20;
		int min = 0;
		
		IntervalView<DoubleType> il2 = Views.addDimension(img, min, max);
		
		IntervalView<DoubleType> opr = ops.view().addDimension(img, min, max);
		
		assertEquals(il2.numDimensions(), opr.numDimensions());
		for (int i = 0; i < il2.numDimensions(); i++) {
			assertEquals(il2.dimension(i), opr.dimension(i));
		}

	}
}
