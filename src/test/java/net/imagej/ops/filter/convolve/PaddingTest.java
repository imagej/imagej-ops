package net.imagej.ops.filter.convolve;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.outofbounds.OutOfBoundsPeriodicFactory;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.junit.Test;

public class PaddingTest extends AbstractOpTest  {
	@Test
	public void theTest() {
	
		Img<FloatType> ramp = ArrayImgs.floats(256, 256);
		Img<FloatType> average = ArrayImgs.floats(64, 64);

		int i = 0;
		for (FloatType iv : ramp)
			iv.set(i / 256 + (i++) % 256);
		for (FloatType kv : average)
			kv.set(1f / (64 * 64));

		RandomAccessibleInterval<FloatType> one=ops.filter().convolve(ramp, average);
		RandomAccessibleInterval<FloatType> two=ops.filter().convolve(ramp, average, new OutOfBoundsPeriodicFactory<>());
	
		assertEquals(ops.stats().mean(Views.iterable(one)).getRealDouble(), 224.5312520918087, 0.0);
		assertEquals(ops.stats().mean(Views.iterable(two)).getRealDouble(), 255.0000066360226, 0.0);
	}
}