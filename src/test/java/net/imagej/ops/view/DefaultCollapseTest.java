package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.GenericComposite;

public class DefaultCollapseTest extends AbstractOpTest {

	@Test
	public void collapseRATest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10, 10 }, new DoubleType());

		CompositeView<DoubleType, ? extends GenericComposite<DoubleType>> il2 = Views
				.collapse((RandomAccessible<DoubleType>) img);

		CompositeView<DoubleType, ? extends GenericComposite<DoubleType>> opr = ops.view()
				.collapse((RandomAccessible<DoubleType>) img);

		assertEquals(il2.numDimensions(), opr.numDimensions());
	}
	
	@Test
	public void collapseRAITest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10, 10 }, new DoubleType());

		CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>> il2 = Views
				.collapse((RandomAccessibleInterval<DoubleType>) img);

		CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>> opr = ops.view()
				.collapse((RandomAccessibleInterval<DoubleType>) img);

		assertEquals(il2.numDimensions(), opr.numDimensions());
	}
}
