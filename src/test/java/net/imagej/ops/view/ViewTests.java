package net.imagej.ops.view;

import static org.junit.Assert.assertTrue;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.view.ViewOps.AddDimension;
import net.imagej.ops.view.ViewOps.Collapse;
import net.imagej.ops.view.ViewOps.DropSingletonDimensions;
import net.imagej.ops.view.ViewOps.Extend;
import net.imagej.ops.views.DefaultExtend;
import net.imagej.ops.views.DefaultExtendBorder;
import net.imagej.ops.views.DefaultExtendMirrorDouble;
import net.imagej.ops.views.DefaultExtendMirrorSingle;
import net.imagej.ops.views.DefaultExtendPeriodic;
import net.imagej.ops.views.DefaultExtendRandom;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsPeriodicFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.GenericComposite;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class ViewTests extends AbstractOpTest {

	Img<DoubleType> img;

	@Before
	public void before() {
		img = new ArrayImgFactory<DoubleType>().create(
				new long[] { 10, 10, 10 }, new DoubleType());
	}

	@Test
	public void addDimensionMinMaxTest() {
		long min = 0;
		long max = 20;

		IntervalView<DoubleType> il2 = Views.addDimension(img, min, max);

		IntervalView<DoubleType> opr = (IntervalView<DoubleType>) ops.run(
				AddDimension.class, img, min, max);

		assertTrue(il2.numDimensions() == opr.numDimensions());
		assertTrue(il2.dimension(0) == opr.dimension(0));
		assertTrue(il2.dimension(1) == opr.dimension(1));
		assertTrue(il2.dimension(2) == opr.dimension(2));
		assertTrue(il2.dimension(3) == opr.dimension(3));
	}

	@Test
	public void defaultAddDimensionTest() {
		MixedTransformView<DoubleType> il2 = Views.addDimension(img);

		MixedTransformView<DoubleType> opr = (MixedTransformView<DoubleType>) ops
				.run(AddDimension.class, img);

		assertTrue(il2.numDimensions() == opr.numDimensions());
	}

	@Test
	public void defaultCollapseTestTest() {
		CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>> il2 = Views
				.collapse(img);

		CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>> opr = (CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>>) ops
				.run(Collapse.class, img);

		assertTrue(il2.numDimensions() == opr.numDimensions());
		assertTrue(il2.dimension(0) == opr.dimension(0));
		assertTrue(il2.dimension(1) == opr.dimension(1));
		assertTrue(false);
	}

	@Test
	public void defaultCollapseNummericTest() {
		Views.collapseReal(img, 1);
		
		assertTrue(false);
	}

	@Test
	public void defaultDropSingletonDimensionsTest() {
		IntervalView<DoubleType> singletonDim = Views.addDimension(img, 0, 0);

		RandomAccessibleInterval<DoubleType> il2 = Views.dropSingletonDimensions(singletonDim);
		
		RandomAccessibleInterval<DoubleType> opr = (RandomAccessibleInterval<DoubleType>)ops.run(DropSingletonDimensions.class, singletonDim);
		
		assertTrue(il2.numDimensions() == 3);
		assertTrue(il2.numDimensions() == opr.numDimensions());
	}

	@Test
	public void defaultExtendTest() {
		OutOfBoundsPeriodicFactory<DoubleType, RandomAccessibleInterval<DoubleType>> factory = new OutOfBoundsPeriodicFactory<DoubleType, RandomAccessibleInterval<DoubleType>>();
		ExtendedRandomAccessibleInterval<DoubleType, Img<DoubleType>> il2 = Views.extend(img, factory);
		
		ExtendedRandomAccessibleInterval<DoubleType, Img<DoubleType>> opr = (ExtendedRandomAccessibleInterval<DoubleType, Img<DoubleType>>)ops.run(Extend.class, img, factory);
		
		assertTrue(il2.numDimensions() == opr.numDimensions());
	}

	@Test
	public void defaultExtendBorderTest() {
		ops.run(DefaultExtendBorder.class, img);
	}

	@Test
	public void defaultExtendMirrorDoubleTest() {
		ops.run(DefaultExtendMirrorDouble.class, img);
	}

	@Test
	public void defaultExtendMirrorSingleTest() {
		ops.run(DefaultExtendMirrorSingle.class, img);
	}

	@Test
	public void defaultExtendPeriodicTest() {
		ops.run(DefaultExtendPeriodic.class, img);
	}

	@Test
	public void defaultExtendRandomTest() {
		ops.run(DefaultExtendRandom.class, img, 0, 10);
	}

	@Test
	public void defaultExtendValueTest() {
		ops.run(DefaultExtendBorder.class, img);
	}

	@Test
	public void defaultExtendZeroTest() {
		ops.run(DefaultExtendBorder.class, img);
	}

	@Test
	public void defaultFlatIterableTest() {

	}

	@Test
	public void defaultHyperSliceTest() {

	}

	@Test
	public void defaultInterpolateTest() {

	}

	@Test
	public void defaultInvertAxisTest() {

	}

	@Test
	public void defaultIsZeroMinTest() {

	}

	@Test
	public void defaultOffsetTest() {

	}

	@Test
	public void defaultPermuteTest() {

	}

	@Test
	public void defaultPermuteCoordinateInverseTest() {

	}

	@Test
	public void defaultPermuteCoordinatesTest() {

	}

	@Test
	public void defaultRealCollapseTest() {

	}

	@Test
	public void defaultShearTest() {

	}

	@Test
	public void defaultStackTest() {

	}

	@Test
	public void defaultSubsampleTest() {

	}

	@Test
	public void defaultTranslateTest() {

	}

	@Test
	public void defaultUnshearTest() {

	}

	@Test
	public void defaultViewTest() {

	}

	@Test
	public void defaultZeroMinTest() {

	}

	@Test
	public void offsetIntervalTest() {

	}

	@Test
	public void offsetOriginSizeTest() {

	}

	@Test
	public void rotateAroundAxisTest() {

	}

	@Test
	public void subsampleStepsForDimsTest() {

	}

	@Test
	public void viewMinMaxTest() {

	}
}