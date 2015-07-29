package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RealRandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.interpolation.randomaccess.FloorInterpolatorFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class DefaultInterpolateTest extends AbstractOpTest {

	@Test
	public void defaultInterpolateTest() {
		
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}
		
		RealRandomAccess<DoubleType> il2 = Views.interpolate(img, new FloorInterpolatorFactory<DoubleType>()).realRandomAccess();
		RealRandomAccess<DoubleType> opr = ops.view().interpolate(img, new FloorInterpolatorFactory<DoubleType>()).realRandomAccess();
		
		il2.setPosition(new double[]{1.75, 5.34});
		opr.setPosition(new double[]{1.75, 5.34});
		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		
		il2.setPosition(new double[]{3, 7});
		opr.setPosition(new double[]{3, 7});
		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		
		il2.setPosition(new double[]{8.37, 3.97});
		opr.setPosition(new double[]{8.37, 3.97});
		assertEquals(il2.get().get(), opr.get().get(), 1e-10);
	}
}
