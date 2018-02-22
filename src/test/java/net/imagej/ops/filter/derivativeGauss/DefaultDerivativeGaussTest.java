
package net.imagej.ops.filter.derivativeGauss;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Contains tests for {@link DefaultDerivativeGauss}.
 * 
 * @author Gabe Selzer
 */
public class DefaultDerivativeGaussTest extends AbstractOpTest {

	@Test(expected = IllegalArgumentException.class)
	public void testTooFewDimensions() {
		Img<DoubleType> input = ops.convert().float64(generateFloatArrayTestImg(
			false, 30));

		Img<DoubleType> output = ops.create().img(input);

		ops.filter().derivativeGauss(output, input, new int[] { 1, 0 }, 1d);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTooManyDimensions() {
		Img<DoubleType> input = ops.convert().float64(generateFloatArrayTestImg(
			false, 30, 30, 30));

		Img<DoubleType> output = ops.create().img(input);

		ops.filter().derivativeGauss(output, input, new int[] { 1, 0 }, 1d);
	}
	
	@Test
	public void regressionTest() {
		int width = 10;
		Img<DoubleType> input = ops.convert().float64(generateFloatArrayTestImg(
			false, width, width));

		Img<DoubleType> output = ops.create().img(input);

		// Draw a line on the image
		RandomAccess<DoubleType> inputRA = input.randomAccess();
		inputRA.setPosition(5, 0);
		for (int i = 0; i < 10; i++) {
			inputRA.setPosition(i, 1);
			inputRA.get().set(255);
		}

		// filter the image
		ops.filter().derivativeGauss(output, input, new int[] { 1, 0 }, 0.5);

		Cursor<DoubleType> cursor = output.localizingCursor();
		int currentPixel = 0;
		while (cursor.hasNext()) {
			cursor.fwd();
			assertEquals(cursor.get().getRealDouble(), regressionRowValues[currentPixel % width], 0);
			currentPixel++;
		}
	}

	double[] regressionRowValues = { 0.0, 0.0, 0.0, 2.1876502452391353,
		117.25400606437196, 0.0, -117.25400606437196, -2.1876502452391353, 0.0,
		0.0 };

}
