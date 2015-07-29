package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class DefaultDropSingletonDimensionsTest extends AbstractOpTest {

	@Test
	public void dropSingletonDimensionsTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 1, 10 }, new DoubleType());

		RandomAccessibleInterval<DoubleType> il2 = Views.dropSingletonDimensions(img);

		RandomAccessibleInterval<DoubleType> opr = ops.view().dropSingletonDimensions(img);

		assertEquals(il2.numDimensions(), opr.numDimensions());
	}
}
