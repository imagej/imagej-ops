package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

public class DefaultPermuteTest extends AbstractOpTest {

	@Test
	public void defaultPermuteTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{10, 10}, new DoubleType());
		
		MixedTransformView<DoubleType> il2 = Views.permute((RandomAccessible<DoubleType>)img, 1, 0);
		MixedTransformView<DoubleType> opr = ops.view().permute(img, 1, 0);
		
		for (int i = 0; i < il2.getTransformToSource().getMatrix().length; i++) {
			for (int j = 0; j < il2.getTransformToSource().getMatrix()[i].length; j++) {
				assertEquals(il2.getTransformToSource().getMatrix()[i][j], opr.getTransformToSource().getMatrix()[i][j],
						1e-10);
			}
		}
	}
	
	@Test
	public void defaultPermuteCoordinatesTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{2, 2}, new DoubleType());
		Cursor<DoubleType> c = img.cursor();
		Random r = new Random();
		while (c.hasNext()) {
			c.next().set(r.nextDouble());
		}
		Cursor<DoubleType> il2 = Views.permuteCoordinates(img, new int[]{0, 1}).cursor();
		RandomAccess<DoubleType> opr = ops.view().permuteCoordinates(img, new int[]{0, 1}).randomAccess();
		
		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
		
	}
	
	@Test
	public void permuteCoordinatesOfDimensionTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{2, 2}, new DoubleType());
		Cursor<DoubleType> c = img.cursor();
		Random r = new Random();
		while (c.hasNext()) {
			c.next().set(r.nextDouble());
		}
		Cursor<DoubleType> il2 = Views.permuteCoordinates(img, new int[]{0, 1}, 1).cursor();
		RandomAccess<DoubleType> opr = ops.view().permuteCoordinates(img, new int[]{0, 1}, 1).randomAccess();
		
		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
		
	}
	
	@Test
	public void defaultPermuteCoordinatesInverseTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{2, 2}, new DoubleType());
		Cursor<DoubleType> c = img.cursor();
		Random r = new Random();
		while (c.hasNext()) {
			c.next().set(r.nextDouble());
		}
		Cursor<DoubleType> il2 = Views.permuteCoordinatesInverse(img, new int[]{0, 1}).cursor();
		RandomAccess<DoubleType> opr = ops.view().permuteCoordinatesInverse(img, new int[]{0, 1}).randomAccess();
		
		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
	}
	
	@Test
	public void permuteCoordinatesInverseOfDimensionTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[]{2, 2}, new DoubleType());
		Cursor<DoubleType> c = img.cursor();
		Random r = new Random();
		while (c.hasNext()) {
			c.next().set(r.nextDouble());
		}
		
		IntervalView<DoubleType> out = Views.permuteCoordinateInverse(img, new int[]{0, 1}, 1);
		
		if(out == null)
			System.out.println("fuck");
		Cursor<DoubleType> il2 = out.cursor();
		RandomAccess<DoubleType> opr = ops.view().permuteCoordinatesInverse(img, new int[]{0, 1}, 1).randomAccess();
		
		while (il2.hasNext()) {
			il2.next();
			opr.setPosition(il2);
			assertEquals(il2.get().get(), opr.get().get(), 1e-10);
		}
	}
}
