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

public class DefaultAddDimensionTest extends AbstractOpTest {

	@Test
	public void addDimensionTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());
		
		MixedTransformView<DoubleType> il2 = Views.addDimension((RandomAccessible<DoubleType>)img);
		
		MixedTransformView<DoubleType> opr = ops.view().addDimension((RandomAccessible<DoubleType>)img);
		
		assertEquals(il2.numDimensions(), opr.numDimensions());
		boolean[] il2Transform = new boolean[3];
		boolean[] oprTransform = new boolean[3];
		il2.getTransformToSource().getComponentZero(il2Transform);
		opr.getTransformToSource().getComponentZero(oprTransform);
		for (int i = 0; i < il2Transform.length; i++) {
			assertEquals(il2Transform[i], oprTransform[i]);
		}

	}
}
