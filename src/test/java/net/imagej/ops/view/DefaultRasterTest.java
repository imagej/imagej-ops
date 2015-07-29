package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.interpolation.randomaccess.FloorInterpolatorFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.RandomAccessibleOnRealRandomAccessible;
import net.imglib2.view.Views;

public class DefaultRasterTest extends AbstractOpTest {

	@Test
	public void defaultRasterTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10,  10}, new DoubleType());
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}
		RealRandomAccessible<DoubleType> realImg = Views.interpolate(img, new FloorInterpolatorFactory<DoubleType>());
		
		RandomAccessibleOnRealRandomAccessible<DoubleType> il2 = Views.raster(realImg);
		RandomAccessibleOnRealRandomAccessible<DoubleType> opr = ops.view().raster(realImg);
		
		Cursor<DoubleType> il2C = Views.interval(il2, img).localizingCursor();
		RandomAccess<DoubleType> oprRA = Views.interval(opr, img).randomAccess();
		
		while (il2C.hasNext()) {
			il2C.next();
			oprRA.setPosition(il2C);
			assertEquals(il2C.get().get(), oprRA.get().get(), 1e-10);
		}
	}
}
