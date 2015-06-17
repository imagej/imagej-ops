package net.imagej.ops.view;

import static org.junit.Assert.assertTrue;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.viewOp.DefaultView;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;

import org.junit.Test;

public class IntervalTest extends AbstractOpTest {

	@Test
	public void testInterval() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(
				new int[] { 10, 100 }, new DoubleType());
		
		IntervalView<DoubleType> view = (IntervalView<DoubleType>) ops.run(DefaultView.class, img, img);
	
		assertTrue(view.getSource().equals(img));
	}
}
