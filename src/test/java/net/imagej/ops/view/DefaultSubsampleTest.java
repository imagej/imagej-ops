package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.SubsampleView;
import net.imglib2.view.Views;

public class DefaultSubsampleTest extends AbstractOpTest {

	@Test
	public void defaultSubsampleTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}

		SubsampleView<DoubleType> il2 = Views.subsample((RandomAccessible<DoubleType>) img, 2);
		SubsampleView<DoubleType> opr = ops.view().subsample(img, 2);

		Cursor<DoubleType> il2C = Views.interval(il2, new long[] { 0, 0 }, new long[] { 4, 4 }).localizingCursor();
		RandomAccess<DoubleType> oprRA = opr.randomAccess();

		while (il2C.hasNext()) {
			il2C.next();
			oprRA.setPosition(il2C);
			assertEquals(il2C.get().get(), oprRA.get().get(), 1e-10);
		}
	}
	
	@Test
	public void defaultSubsampleStepsTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}

		SubsampleView<DoubleType> il2 = Views.subsample((RandomAccessible<DoubleType>) img, 2, 1);
		SubsampleView<DoubleType> opr = ops.view().subsample(img, 2, 1);

		Cursor<DoubleType> il2C = Views.interval(il2, new long[] { 0, 0 }, new long[] { 4, 9 }).localizingCursor();
		RandomAccess<DoubleType> oprRA = opr.randomAccess();

		while (il2C.hasNext()) {
			il2C.next();
			oprRA.setPosition(il2C);
			assertEquals(il2C.get().get(), oprRA.get().get(), 1e-10);
		}
	}
}
