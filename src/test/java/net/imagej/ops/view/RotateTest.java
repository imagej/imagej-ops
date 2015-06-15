package net.imagej.ops.view;

import static org.junit.Assert.assertTrue;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;

import org.junit.Test;

public class RotateTest extends AbstractOpTest {

	@Test
	public void testRotate() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(
				new int[] { 10, 100 }, new DoubleType());

		IntervalView<DoubleType> view = (IntervalView<DoubleType>) ops.run(
				Rotate.class, 1, 0, ops.run(Interval.class, img));
		assertTrue(img.dimension(0) == view.dimension(1));
		assertTrue(img.dimension(1) == view.dimension(0));
	}
}
