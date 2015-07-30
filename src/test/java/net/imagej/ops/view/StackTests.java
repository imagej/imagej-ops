package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.StackView.StackAccessMode;
import net.imglib2.view.Views;

public class StackTests extends AbstractOpTest {

	@Test
	public void defaultStackTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		List<RandomAccessibleInterval<DoubleType>> list = new ArrayList<RandomAccessibleInterval<DoubleType>>();
		list.add(img);
		list.add(img);
		
		RandomAccessibleInterval<DoubleType> il2 = Views.stack(list);
		RandomAccessibleInterval<DoubleType> opr = ops.view().stack(list);

		assertEquals(il2.dimension(2), opr.dimension(2));
	}
	
	@Test
	public void stackWithAccessModeTest() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10 }, new DoubleType());

		List<RandomAccessibleInterval<DoubleType>> list = new ArrayList<RandomAccessibleInterval<DoubleType>>();
		list.add(img);
		list.add(img);
		
		RandomAccessibleInterval<DoubleType> il2 = Views.stack(StackAccessMode.DEFAULT, list);
		RandomAccessibleInterval<DoubleType> opr = ops.view().stack(list, StackAccessMode.DEFAULT);

		assertEquals(il2.dimension(2), opr.dimension(2));
	}

}
