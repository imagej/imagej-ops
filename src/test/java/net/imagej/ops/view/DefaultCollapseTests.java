package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.NativeARGBDoubleType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.GenericComposite;
import net.imglib2.view.composite.NumericComposite;
import net.imglib2.view.composite.RealComposite;

public class DefaultCollapseTests extends AbstractOpTest {

	@Test
	public void defaultCollapseTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 },
				new DoubleType());

		CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>> il2 = Views
				.collapse(img);
		CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>> opr = ops.view()
				.collapse(img);

		assertEquals(il2.numDimensions(), opr.numDimensions());
	}
	
	@Test
	public void defaultNumericCollapseTest() {

		Img<NativeARGBDoubleType> img = new ArrayImgFactory<NativeARGBDoubleType>().create(new int[] { 10, 10 },
				new NativeARGBDoubleType());

		CompositeIntervalView<NativeARGBDoubleType, NumericComposite<NativeARGBDoubleType>> il2 = Views
				.collapseNumeric((RandomAccessibleInterval<NativeARGBDoubleType>) img);
		CompositeIntervalView<NativeARGBDoubleType, NumericComposite<NativeARGBDoubleType>> opr = ops.view()
				.numericCollapse((RandomAccessibleInterval<NativeARGBDoubleType>) img);

		assertEquals(il2.numDimensions(), opr.numDimensions());

		CompositeView<NativeARGBDoubleType, NumericComposite<NativeARGBDoubleType>> il2_2 = Views
				.collapseNumeric((RandomAccessible<NativeARGBDoubleType>) img, 1);
		CompositeView<NativeARGBDoubleType, NumericComposite<NativeARGBDoubleType>> opr_2 = ops.view()
				.numericCollapse((RandomAccessible<NativeARGBDoubleType>) img, 1);

		assertEquals(il2_2.numDimensions(), opr_2.numDimensions());
	}
	
	@Test
	public void defaultRealCollapseTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 },
				new DoubleType());

		CompositeIntervalView<DoubleType, RealComposite<DoubleType>> il2 = Views
				.collapseReal((RandomAccessibleInterval<DoubleType>) img);
		CompositeIntervalView<DoubleType, RealComposite<DoubleType>> opr = ops.view()
				.realCollapse((RandomAccessibleInterval<DoubleType>) img);

		assertEquals(il2.numDimensions(), opr.numDimensions());

		CompositeView<DoubleType, RealComposite<DoubleType>> il2_2 = Views
				.collapseReal((RandomAccessible<DoubleType>) img, 1);
		CompositeView<DoubleType, RealComposite<DoubleType>> opr_2 = ops.view()
				.realCollapse((RandomAccessible<DoubleType>) img, 1);

		assertEquals(il2_2.numDimensions(), opr_2.numDimensions());
	}
}
