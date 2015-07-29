package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class DefaultFlatIterableTest extends AbstractOpTest {

	@Test
	public void defaultFlatIterableTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		Cursor<DoubleType> il2 = Views.flatIterable(img).cursor();

		Cursor<DoubleType> opr = ops.view().flatIterable(img).cursor();

		while (il2.hasNext()) {
			il2.next();
			opr.next();
			assertEquals(il2.getDoublePosition(0), opr.getDoublePosition(0), 1e-10);
			assertEquals(il2.getDoublePosition(1), opr.getDoublePosition(1), 1e-10);
		}
	}
}
