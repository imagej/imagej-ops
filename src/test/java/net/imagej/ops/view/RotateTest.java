package net.imagej.ops.view;

import static org.junit.Assert.assertTrue;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.viewOp.Rotate;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

public class RotateTest extends AbstractOpTest {

	@Test
	public void testRotate() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(
				new int[] { 10, 100 }, new DoubleType());

		RandomAccessibleInterval<DoubleType> view = (RandomAccessibleInterval<DoubleType>) ops
				.run(Rotate.class, img, 1, 0);
		assertTrue(img.dimension(0) == view.dimension(1));
		assertTrue(img.dimension(1) == view.dimension(0));
		
		view = (RandomAccessibleInterval<DoubleType>) ops.run(Rotate.class, view, 0, 1);
		assertTrue(img.dimension(0) == view.dimension(0));
		assertTrue(img.dimension(1) == view.dimension(1));
	}
}
