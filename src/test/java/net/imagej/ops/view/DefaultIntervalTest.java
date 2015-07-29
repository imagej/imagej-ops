package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class DefaultIntervalTest extends AbstractOpTest {

	@Test
	public void defaultIntervalTest() {
		
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}
		
		Cursor<DoubleType> il2 = Views.interval(img, img).localizingCursor();
		RandomAccess<DoubleType> opr = ops.view().interval(img, img).randomAccess();

		
		while (il2.hasNext()) {
			DoubleType e = il2.next();
			opr.setPosition(il2);
			
			assertEquals(e.get(), opr.get().get(), 1e-10);
		}
	}
	
	@Test
	public void intervalMinMaxTest() {
		
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		
		Random r = new Random();
		for (DoubleType d : img) {
			d.set(r.nextDouble());
		}
		
		Cursor<DoubleType> il2 = Views.interval(img, new long[]{1, 1}, new long[]{8,9}).localizingCursor();
		RandomAccess<DoubleType> opr = ops.view().interval(img, new long[]{1, 1}, new long[]{8,9}).randomAccess();
		
		while (il2.hasNext()) {
			DoubleType e = il2.next();
			opr.setPosition(il2);
			
			assertEquals(e.get(), opr.get().get(), 1e-10);
		}
	}
}
