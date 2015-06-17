package net.imagej.ops.view;

import static org.junit.Assert.assertTrue;
import ij.ImageJ;
import ij.ImagePlus;

import java.awt.Dimension;
import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.create.CreateOps.CreateNativeImg;
import net.imagej.ops.view.ViewOps.Rotate;
import net.imagej.ops.view.ViewOps.View;
import net.imagej.ops.viewOp.DefaultView;
import net.imagej.ops.viewOp.ViewMinMax;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImagePlusAdapter;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.MixedTransformView;

import org.junit.Test;

public class RotateTest extends AbstractOpTest {

	@Test
	public void testRotate() {
		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(
				new int[] { 3, 3 }, new DoubleType());
		
		Random r = new Random();
		int i = 0;
		for (DoubleType doubleType : img) {
			doubleType.set(i++);
		}

		MixedTransformView<DoubleType> mtv = (MixedTransformView<DoubleType>) ops
				.run(Rotate.class, img, 0, 1);
		Img<DoubleType> res = (Img<DoubleType>) ops.run(CreateNativeImg.class, mtv);
		// TODO: How to test?
	}
}
