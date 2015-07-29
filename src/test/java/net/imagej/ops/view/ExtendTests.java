package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.outofbounds.OutOfBounds;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class ExtendTests extends AbstractOpTest {

	@Test
	public void defaultExtendTest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views
				.extend(img, new OutOfBoundsBorderFactory<DoubleType, RandomAccessibleInterval<DoubleType>>())
				.randomAccess();

		OutOfBounds<DoubleType> opr = ops.view()
				.extend(img, new OutOfBoundsBorderFactory<DoubleType, RandomAccessibleInterval<DoubleType>>())
				.randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendBorderTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendBorder(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendBorder(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendMirrorDoubleTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendMirrorDouble(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendMirrorDouble(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendMirrorSingleTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendMirrorSingle(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendMirrorSingle(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendPeriodicTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendPeriodic(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendPeriodic(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendRandomTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendRandom(img, 0, 0).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendRandom(img, 0, 0).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendValueTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendValue(img, new DoubleType(0)).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendValue(img, new DoubleType(0)).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
	
	@Test
	public void extendZeroTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		OutOfBounds<DoubleType> il2 = Views.extendZero(img).randomAccess();

		OutOfBounds<DoubleType> opr = ops.view().extendZero(img).randomAccess();

		il2.setPosition(new int[] { -1, -1 });
		opr.setPosition(new int[] { -1, -1 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);

		il2.setPosition(new int[] { 11, 11 });
		opr.setPosition(new int[] { 11, 11 });

		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
}
